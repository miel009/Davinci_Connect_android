const functions = require("firebase-functions");
const admin = require("firebase-admin");
const axios = require("axios");
const cors = require("cors")({ origin: true });

admin.initializeApp();

// Lee la key de dos lugares: env o functions:config (para evitar el crash)
const GEMINI_API_KEY =
  process.env.GEMINI_API_KEY ||
  (functions.config().gemini && functions.config().gemini.key) ||
  "";

const GEMINI_URL =
  "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

// ⚠️ Endpoint compatible con Android (ChatService.ChatReq -> { text, uid })
exports.chatLeo = functions.https.onRequest((req, res) => {
  cors(req, res, async () => {
    try {
      if (req.method !== "POST") {
        return res.status(405).json({ error: "Use POST" });
      }

      if (!GEMINI_API_KEY) {
        return res.status(500).json({
          error:
            "GEMINI_API_KEY no configurada. Ejecuta: firebase functions:config:set gemini.key=\"TU_API_KEY\"",
        });
      }

      const { text, uid } = req.body || {};
      if (!text) return res.status(400).json({ error: "text required" });

      // (Opcional) Cargar contexto del alumno desde Firestore
      let contexto = {};
      if (uid) {
        const db = admin.firestore();
        const userDoc = await db.collection("users").doc(uid).get();
        const alumno = userDoc.exists ? userDoc.data() : {};

        // Ajusta a tu esquema real si tienes estas subcolecciones:
        const mats = await db.collection("alumnos").doc(uid).collection("materias").get();
        const materias = mats.docs.map((d) => d.data());

        const notas = await db.collection("alumnos").doc(uid).collection("calificaciones").get();
        const calificaciones = notas.docs.map((d) => d.data());

        const horarios = await db.collection("alumnos").doc(uid).collection("horarios").get();
        const horariosData = horarios.docs.map((d) => d.data());

        contexto = { alumno, materias, calificaciones, horarios: horariosData };
      }

      const systemPrompt = `
Eres "Leo", asistente académico. Responde en español, claro y breve.
Usa el CONTEXTO JSON si responde a la pregunta (horarios, materias, aulas, notas).
Si falta dato, pide precisión o explica dónde verlo en el campus.
Contexto JSON:
${JSON.stringify(contexto).slice(0, 25000)}
`;

      const payload = {
        contents: [
          {
            role: "user",
            parts: [{ text: `${systemPrompt}\n\nPregunta del alumno: ${text}` }],
          },
        ],
      };

      const r = await axios.post(`${GEMINI_URL}?key=${GEMINI_API_KEY}`, payload, {
        headers: { "Content-Type": "application/json" },
      });

      const reply =
        r.data?.candidates?.[0]?.content?.parts?.[0]?.text ||
        "No pude generar respuesta ahora.";
      return res.json({ reply });
    } catch (e) {
      console.error(e);
      return res.status(500).json({ error: e.message });
    }
  });
});
