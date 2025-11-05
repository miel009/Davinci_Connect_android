const { onRequest } = require("firebase-functions/v2/https");
const { setGlobalOptions } = require("firebase-functions/v2/options");
const { defineSecret } = require("firebase-functions/params"); // 👈 aseguramos que se importe
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
        const userDoc = await db.collection("users").doc(uid).get();
        const alumno = userDoc.exists ? userDoc.data() : {};

        const mats = await db.collection("alumnos").doc(uid).collection("materias").get();
        const materias = mats.docs.map((d) => d.data());

        const notas = await db.collection("alumnos").doc(uid).collection("calificaciones").get();
        const calificaciones = notas.docs.map((d) => d.data());

        const horarios = await db.collection("alumnos").doc(uid).collection("horarios").get();
        const horariosData = horarios.docs.map((d) => d.data());

        contexto = { alumno, materias, calificaciones, horarios: horariosData };
      }

      // Prompt 
      const systemPrompt = `Eres "Leo", asistente académico. Responde en español, claro y breve.
Usa el CONTEXTO JSON si responde a la pregunta (horarios, materias, aulas, notas).
Si falta dato, pide precisión o explica dónde verlo en el campus.
Contexto JSON:
${JSON.stringify(contexto).slice(0, 25000)}
Pregunta del alumno: ${text}`;

      const payload = {
        contents: [
          {
            role: "user",
            parts: [{ text: systemPrompt }],
          },
        ],
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
