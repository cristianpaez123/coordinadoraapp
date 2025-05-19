# 📱 Coordinadora


## ⚡ App Distribution

🔗 **Acceso a la app para pruebas**  
La aplicación está disponible para testers mediante **Firebase App Distribution**.  
Utiliza el siguiente enlace para descargar e instalar la última versión de la app:

👉 [**Instalar desde Firebase App Distribution**](https://appdistribution.firebase.dev/i/476d9f65ce40aa55)

> Asegúrate de haber iniciado sesión con uno de los correos de prueba autorizados antes de acceder al enlace.

---

## 👥 Usuarios de Prueba

Para acceder a la aplicación, puedes utilizar uno de los siguientes usuarios de prueba registrados en **Firebase Authentication**:

| Correo electrónico           | Contraseña |
|------------------------------|------------|
| oscart@coordinadora.com      | 123456     |
| sdhajan@coordinadora.com     | 123456     |
| camilov@coordinadora.com     | 123456     |



## 📝 Descripción del Problema
Esta aplicación Android está diseñada para facilitar la lectura y procesamiento de códigos QR que contienen coordenadas geográficas (latitud y longitud). Al escanear un código QR, la app extrae la ubicación codificada y:


Funcionalidad principal:
---

El usuario debe autenticarse previamente mediante Firebase Authentication, utilizando sus credenciales válidas para acceder al flujo principal de la aplicación.
La registra en una base de datos local para su posterior consulta.

La muestra en una lista que agrupa todas las ubicaciones previamente escaneadas.

La visualiza en un mapa junto con la ubicación actual del usuario.

---

## ✅ Requisitos Previos

- Android Studio Flamingo o superior  
- Java 11 o superior  
- Un emulador o dispositivo físico con Android 8.0 (API 24) o superior

---

## 🚀 Tecnologías Utilizadas

- 💻 **Lenguaje**: Java  
- 🧠 **Arquitectura**: MVVM (Model-View-ViewModel)  
- ⚙️ **Asíncronía**: RxJava  
- 🧩 **Inyección de Dependencias**: Dagger 2  
- 🚀 **CI/CD**: GitHub Actions + Firebase App Distribution  
- 🔐 **Servicios**: Firebase Auth, Firebase App Distribution  
- 🗺️ **Mapas**: Google Maps API  
- 📷 **Escaneo QR**: ML Kit (Barcode Scanning)  
- 🌐 **Networking**: Volley  
- 🔄 **Ciclo de vida y estado**: ViewModel y LiveData  
- 🧪 **Testing**: Pruebas unitarias con JUnit / Mockito
---

## 🛠️ Instrucciones de Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/cristianpaez123/coordinadoraapp

   Abre el proyecto en Android Studio

2. Sincroniza las dependencias de Gradle

3. Configura un emulador o conecta un dispositivo físico

4. Haz clic en el botón "Run" para ejecutar la aplicación

---

## 🔄 CI/CD 

1. Este proyecto incluye un pipeline de CI/CD configurado con GitHub Actions que:

2. Compila la aplicación

3. Ejecuta pruebas unitarias

4. Genera el APK

5. Publica el APK en Firebase App Distribution

---

## 🧠 Estructura del Proyecto

El proyecto sigue una Clean Architecture con separación por capas y responsabilidades bien definidas:
```
com.example.coordinadoraapp
│
├── data                    # Fuente de datos: local, remota y mapeadores
│   ├── dto                 # Modelos de transporte de datos (API/DB)
│   ├── local               # Almacenamiento local (Room, SharedPrefs, etc.)
│   ├── mapper              # Transformadores de modelos
│   ├── payload             # Estructuras de solicitud/respuesta
│   ├── remote              # Implementaciones de red (API, Firebase)
│   └── repository          # Implementaciones concretas de repositorios
│
├── di                      # Inyección de dependencias con Dagger 2
│   ├── anotation           # Anotaciones personalizadas para Dagger
│   └── viewmodel           # Módulos y binding de ViewModels
│   ├── AppComponent        # Componente principal de Dagger
│   ├── AppModule           # Proporciona instancias globales
│   ├── NetworkModule       # Configuración de servicios de red
│   ├── RepositoryModule    # Bindings de repositorios
│   ├── SchedulerModule     # Proveedores de Schedulers (RxJava)
│   └── UseCaseModule       # Proveedores de casos de uso
│
├── domain                  # Lógica de negocio y entidades puras
│   ├── model               # Modelos del dominio
│   ├── repository          # Interfaces abstractas de acceso a datos
│   ├── usecase             # Casos de uso como `ScanQr`, `GetUserLocation`, etc.
│   └── QrResultListener    # Interface para manejar resultados del QR
│
├── sync                    # Lógica de sincronización de ubicaciones
│   └── LocationSyncManager
│
├── ui                      # Capa de presentación
│   ├── launcher            # Pantalla de inicio
│   ├── login               # Flujo de autenticación
│   ├── mainActivity        # Contenedor principal de navegación
│   ├── Map                 # Visualización e interacción con el mapa
│   ├── mapper              # Mapeadores específicos de UI
│   └── model               # Modelos de UI (state, events, etc.)
│
├── utils                   # Clases auxiliares y validaciones
│   ├── CameraPermissionManager  
│   ├── CredentialValidator  
│   ├── FirebaseErrorMapper  
│   ├── QrOverlay  
│   └── UbicationPermissionManager  
│
└── MyApplication           # Clase de inicialización global (Dagger, Firebase, etc.)
```
---

## ⚙️ Decisiones Técnicas

**MVVM**: Separación de responsabilidades entre UI y lógica de negocio.

**RxJava**: Flujo reactivo para manejar eventos y respuestas asincrónicas.

**Dagger2**: Inyección de dependencias para un código modular y testeable.

**Volley**: Comunicación eficiente con APIs REST.

**ML Kit**: Lectura de códigos QR directamente desde la cámara.

**Firebase Auth**: Autenticación rápida y segura.

**GitHub Actions + Firebase**: Flujo de integración y despliegue continuo.

**Sincronización en tiempo real con la base de datos**  
  Se implementa una clase `LocationSyncManager` para manejar de forma asíncrona la actualizacion con la base de datos.
  ```text
                ┌─────────────────────────────┐
                │        Local Database       │
                └────────────┬────────────────┘
                             │ (emite events)
                             ▼
                ┌─────────────────────────────┐
                │     LocationSyncManager     │
                └────────────┬────────────────┘
                             │ (update data)
                             ▼
          ┌─────────────────────────────┐
          │          Firerbase DB       │
          └────────────┬────────────────┘
                     
```
---

## 🚧 Limitaciones y Mejoras Futuras

- 🔍 **Cobertura parcial en pruebas de casos de uso**: Se planea extender la cobertura de pruebas unitarias, especialmente en la capa de `usecase` y `repository`, para mejorar la confiabilidad y detección temprana de errores lógicos.

- ♻️ **Mayor reutilización de lógica común**: Se deben abstraer más comportamientos comunes en clases genéricas y utilitarias, para evitar duplicación de código y mejorar la mantenibilidad.

- ⚠️ **Manejo de errores limitado**: El sistema de gestión de errores en autenticación, red y lectura de QR puede fortalecerse para cubrir más escenarios y mostrar mensajes de usuario más claros.

- 🧭 **Optimización del rendimiento en el mapa**: Se podrían aplicar mejoras en la carga de mapas y renderizado de rutas para dispositivos de gama baja o con conectividad limitada.

- 🔐 **Validación de seguridad**: Se pueden añadir validaciones más estrictas sobre los datos escaneados del QR para evitar coordenadas inválidas o manipuladas.

---
## Test

Vinculo de invitacion App distribution
https://appdistribution.firebase.dev/i/476d9f65ce40aa55



---
## 👤 Autor

Nombre: Cristian Paez

GitHub: @cristianpaez123

Correo: cristianpaezguerrero@gmail.com

Celular: +57 300 702 5600

---

