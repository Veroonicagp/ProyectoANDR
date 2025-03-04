# 🟣 ReadyToEnjoy 🟣

## 📱 Descripción 
ReadyToEnjoy es una aplicación social para compartir y descubrir actividades. Los usuarios pueden registrarse, iniciar sesión, explorar actividades creadas por otros usuarios y publicar sus propias actividades para que la comunidad las disfrute.

## ✨ Características principales

- **Sistema de autenticación**: Registro e inicio de sesión de usuarios
- **Feed de actividades**: Explora actividades compartidas por toda la comunidad
- **Creación de actividades**: Publica tus propias ideas y planes
- **Gestión de perfil**: Visualiza y administra tus actividades publicadas
- **Captura de imágenes**: Toma fotos para tus actividades directamente desde la app

## 🛠️ Tecnologías utilizadas

- **Kotlin**: Lenguaje principal de desarrollo
- **Jetpack Components**:
  - **Navigation**: Para la navegación entre fragments
  - **Room**: Base de datos local para almacenamiento persistente
  - **DataStore**: Almacenamiento de preferencias de usuario
  - **CameraX**: Integración de cámara para captura de imágenes
- **Dagger Hilt**: Inyección de dependencias
- **Coil**: Carga y cache eficiente de imágenes
- **Strapi**: Backend headless CMS para la gestión de datos en el servidor
- **Architecture**: MVVM (Model-View-ViewModel)

## 📸 Capturas de pantalla

<p align="center">
  <img src="screenshot_login.png" width="200" />
  <img src="screenshot_feed.png" width="200" /> 
  <img src="screenshot_create.png" width="200" />
  <img src="screenshot_profile.png" width="200" />
</p>

## 🏗️ Arquitectura

La aplicación sigue el patrón de arquitectura MVVM (Model-View-ViewModel) y está estructurada en las siguientes capas:

- **Presentación**: Activities, Fragments y ViewModels
- **Dominio**: Casos de uso e interfaces de repositorios
- **Datos**: Implementación de repositorios, fuentes de datos (API y local)

```
app/
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
```

## 🚀 Instalación

1. Clona este repositorio
   ```
   git clone https://github.com/Veroonicagp/ProyectoANDR
   ```
2. Abre el proyecto en Android Studio
3. Sincroniza el proyecto con Gradle
4. Ejecuta la aplicación en un emulador o dispositivo físico

## 🔄 Flujo de la aplicación

1. El usuario se registra o inicia sesión
2. Explora el feed de actividades creadas por otros usuarios
3. Puede crear sus propias actividades con título, descripción, localización, precio e imagen
4. Las actividades creadas se muestran tanto en el feed global como en el perfil personal

## 👥 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Haz fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-caracteristica`)
3. Haz commit de tus cambios (`git commit -m 'Añadir nueva característica'`)
4. Haz push a la rama (`git push origin feature/nueva-caracteristica`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 📞 Contacto

Para cualquier pregunta o sugerencia, contáctame en GitHub: [Veroonicagp](https://github.com/Veroonicagp)

---

<p align="center">
  <b>Ready To Enjoy</b> - ¡Comparte y descubre experiencias únicas!
</p>
