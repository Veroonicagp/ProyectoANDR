🟣 ReadyToEnjoy 🟣
<p align="center">
  <img src="app_logo.png" alt="ReadyToEnjoy Logo" width="200"/>
</p>
📱 Descripción
ReadyToEnjoy es una aplicación social para compartir y descubrir actividades. Los usuarios pueden registrarse, iniciar sesión, explorar actividades creadas por otros usuarios y publicar sus propias actividades para que la comunidad las disfrute.
✨ Características principales

Sistema de autenticación: Registro e inicio de sesión de usuarios
Feed de actividades: Explora actividades compartidas por toda la comunidad
Creación de actividades: Publica tus propias ideas y planes
Gestión de perfil: Visualiza y administra tus actividades publicadas

🛠️ Tecnologías utilizadas

Kotlin: Lenguaje principal de desarrollo
Jetpack Components:
Navigation: Para la navegación entre fragments
Room: Base de datos local para almacenamiento persistente
DataStore: Almacenamiento de preferencias de usuario
CameraX: Integración de cámara para captura de imágenes
Dagger Hilt: Inyección de dependencias
Coil: Carga y cache eficiente de imágenes
Strapi: Backend headless CMS para la gestión de datos en el servidor
Architecture: MVVM (Model-View-ViewModel)

🏗️ Arquitectura
La aplicación sigue el patrón de arquitectura MVVM (Model-View-ViewModel) y está estructurada en las siguientes capas:

Presentación: Activities, Fragments y ViewModels
Dominio: Casos de uso e interfaces de repositorios
Datos: Implementación de repositorios, fuentes de datos (API y local)

Copyapp/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   └── database/
│   ├── remote/
│   │   ├── api/
│   │   └── dto/
│   └── repositories/
├── di/
│   └── modules/
├── domain/
│   ├── models/
│   ├── repositories/
│   └── usecases/
├── presentation/
│   ├── activities/
│   ├── adapters/
│   ├── fragments/
│   └── viewmodels/
└── utils/
🚀 Instalación

Clona este repositorio
Copygit clone [https://github.com/tuusuario/ReadyToEnjoy.git](https://github.com/Veroonicagp/ProyectoANDR)https://github.com/Veroonicagp/ProyectoANDR

Abre el proyecto en Android Studio
Sincroniza el proyecto con Gradle
Ejecuta la aplicación en un emulador o dispositivo físico

🔄 Flujo de la aplicación

El usuario se registra o inicia sesión
Explora el feed de actividades creadas por otros usuarios
Puede crear sus propias actividades con título, descripción, localización, precio e imagen
Las actividades creadas se muestran tanto en el feed global como en el perfil personal las propias creadas por dcho usuario

👥 Contribuciones
Las contribuciones son bienvenidas. Para contribuir:

Haz fork del proyecto
Crea una rama para tu feature (git checkout -b feature/nueva-caracteristica)
Haz commit de tus cambios (git commit -m 'Añadir nueva característica')
Haz push a la rama (git push origin feature/nueva-caracteristica)
Abre un Pull Request

📞 Contacto
Para cualquier pregunta o sugerencia, contáctame en:

Email: veroniicagnzpns@outlook.com
GitHub: Veroonicagp


<p align="center">
  <b>Ready To Enjoy</b> - ¡Comparte y descubre experiencias únicas!
</p>
