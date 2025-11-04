# 📱 Davinci Connect 
Proyecto para seminario final.

**Descripción:**

Este repositorio contiene la versión **Android** del sistema de gestión académica **DaVinci Connect**, que incluye a **ChatLeo**, un asistente académico inteligente integrado con **Firebase** y la **API de Gemini** , que complementa la versión web disponible en: https://github.com/miel009/Davinci_Connect_Web.git
El objetivo principal de esta versión móvil es extender la plataforma a dispositivos Android, permitiendo a estudiantes y docentes consultar información académica de manera ágil y conversar con ChatLeo desde cualquier lugar.

La aplicación busca:

🎓 Facilitar el acceso móvil a horarios, materias y calificaciones.  
🤖 Ofrecer asistencia académica personalizada mediante IA.  
☁️ Integrarse con la infraestructura en la nube de Firebase y Gemini.

---

## Tecnologías usadas

![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=flat&logo=androidstudio&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat&logo=firebase&logoColor=black)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=flat&logo=node.js&logoColor=white)
![Google Gemini](https://img.shields.io/badge/Gemini_AI-4285F4?style=flat&logo=google&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-007FFF?style=flat&logo=square&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)

---

## Configuración inicial

Antes de ejecutar el proyecto, instalar:

- Android Studio (última versión)
- Node.js v22
- Firebase CLI

  ```bash
  npm install -g firebase-tools
  ```
Iniciá sesión en Firebase:

  ```bash
  firebase login
  ```
Instalar dependencias de las funciones ,en DaVinciConnect (File → Open → DaVinciConnect). 
Instalara: firebase-admin, firebase-functions, axios y cors.

  ```bash
  cd functions
  npm install
  npm install firebase-functions@latest firebase-admin@latest axios cors
  ```

Probar el backend localmente, ejecutar en la carpeta functions:

 ``` bash
 firebase emulators:start --only functions
 ```

Ir  al chat con ChatLeo y preguntarle por "materias".

