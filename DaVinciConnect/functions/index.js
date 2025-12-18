
const { onRequest } = require("firebase-functions/v2/https");
const { setGlobalOptions } = require("firebase-functions/v2/options");
const { defineSecret } = require("firebase-functions/params");
const admin = require("firebase-admin");
const axios = require("axios");
const cors = require("cors")({ origin: true });

const GEMINI_API_KEY = defineSecret("GEMINI_API_KEY");

setGlobalOptions({
  region: "us-central1",
  timeoutSeconds: 30,
  memory: "512MiB",
  secrets: [GEMINI_API_KEY],
});

admin.initializeApp();

const GEMINI_URL =
  "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

exports.chatLeo = onRequest(async (req, res) => {
  cors(req, res, async () => {
    try {
      if (req.method !== "POST") {
        return res.status(405).json({ error: "Use POST" });
      }

      const { text, uid } = req.body || {};
      if (!text) return res.status(400).json({ error: "text required" });


      let contexto = {};
      if (uid) {
        const db = admin.firestore();

        // USUARIO
        const userDoc = await db.collection("users").doc(uid).get();
        const alumno = userDoc.exists ? userDoc.data() : {};

        // MATERIAS DEL USUARIO (ruta correcta)
        const mats = await db.collection("users").doc(uid).collection("materias").get();
        const materias = mats.docs.map((d) => ({
          id: d.id,
          ...d.data()
        }));

        contexto = { alumno, materias };
        console.log("UID recibido:", uid);
        console.log("Contexto generado:", contexto);

        // MARCAR SESIÓN ACTIVA POR USUARIO
        if (!global.__sesionActiva) global.__sesionActiva = {}; // inicializar

        // Si es la primera vez que el usuario habla → activar sesión
        if (!global.__sesionActiva[uid]) {
          global.__sesionActiva[uid] = true;
        }

        // Inyectar al contexto (para que el modelo lo use)
        contexto.sesionActiva = global.__sesionActiva[uid];

      }


      // Prompt 
      const systemPrompt = `
Eres "Leo", asistente académico del sistema DavinciConnect.
NO te presentes si el usuario ya está conversando.
Responde siempre en español, claro, amable y personalizado.

FORMATO OBLIGATORIO para listar materias:
- NO usar asteriscos ni Markdown.
- Usar lista limpia con "•" o "–".
- Mostrar: código, nombre y horario si existen.
- Formato por materia:

  • CODIGO – Nombre de la materia
    Horario: HH:MM 

REGLAS PARA HORARIOS:
- Si el usuario pregunta “horario”, “a qué hora es”, “cuando tengo clase” o similar:
  → Busca el campo "horario" dentro del CONTEXTO JSON de esa materia.
- Si existe, muéstralo.
- Si no existe, indica que no está registrado.
IMPORTANTE:
  NO saludes, NO te presentes.
  Responde directo.
  Si no existe aulgun dato, indica que no está registrado y recomienda que deben contactarse con el Departamento de alumnos a la direccion: alumnos@dv.com.


Usa siempre el CONTEXTO JSON.
Luego de cada respuesta pregunta si desea consultar algo más, pero no repitas preguntas en una misma respuesta. 
-Siempre trata de dar una respuesta al alumno, busca entre sus datos si es necesario, pero no le menciones que buscaste entre sus datos. 


CONTEXTO JSON:
${JSON.stringify(contexto).slice(0, 25000)}

Pregunta del alumno:
${text}
`;
  
      // PREG + MEMORIA
      // Normalizar pregunta
      const pregunta = text.toLowerCase();
      let accionDetectada = null;
      let materiaDetectada = null;

      // Inicializar memoria si no existe
      if (!global.__ultimaMateria) global.__ultimaMateria = null;

      // detectar "acción": horario / sede / lugar
      if (pregunta.includes("horario") || pregunta.includes("hora") || pregunta.includes("cuando tengo") || pregunta.includes("a qué hora"))
        accionDetectada = "horario";

      if (pregunta.includes("sede") || pregunta.includes("donde curso") || pregunta.includes("ubicación") || pregunta.includes("edificio"))
        accionDetectada = "sede";

      // intentar detectar la materia por coincidencia parcial
      if (contexto.materias && contexto.materias.length > 0) {
        for (const m of contexto.materias) {
          const codigo = m.id.toLowerCase();
          const nombre = (m.descripcion || "").toLowerCase();

          if (
            pregunta.includes(codigo) ||
            (nombre && pregunta.includes(nombre.split(" ")[0])) ||    // primera palabra del nombre
            (nombre && pregunta.includes(nombre))                    // nombre completo
          ) {
            materiaDetectada = m;
            global.__ultimaMateria = m; // guardar en memoria
            break;
          }
        }
      }

      // Si no detectó materia en esta pregunta, usar la última mencionada
      if (!materiaDetectada && global.__ultimaMateria) {
        materiaDetectada = global.__ultimaMateria;
      }

      contexto.analisis = {
        accionDetectada,
        materiaDetectada,
        memoriaActiva: global.__ultimaMateria || null
      };



      const payload = {
        contents: [
          {
            role: "model",
            parts: [{ text: systemPrompt }],
          },
          {
            role: "user",
            parts: [{ text }],
          }
        ]
      };

      const apiKey = GEMINI_API_KEY.value();

      const r = await axios.post(`${GEMINI_URL}?key=${apiKey}`, payload, {
        headers: { "Content-Type": "application/json" },
      });

      const reply =
        r.data?.candidates?.[0]?.content?.parts?.[0]?.text ||
        "No pude generar respuesta ahora.";

      return res.json({ reply });
    } catch (e) {
      console.error("Error en chatLeo:", e.response?.data || e.message);
      return res.status(500).json({ error: e.message });
    }
  });
});
