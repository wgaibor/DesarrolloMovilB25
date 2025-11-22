# The Simpsons App

Aplicación móvil Android desarrollada en Java que consume la API de The Simpsons (https://thesimpsonsapi.com/) para mostrar información sobre los personajes de la serie.

## Características

- **Lista de Personajes**: Muestra una lista paginada de todos los personajes de The Simpsons
- **Scroll Infinito**: Carga automáticamente más personajes al hacer scroll
- **Detalles del Personaje**: Vista detallada con información completa de cada personaje
- **Imágenes**: Carga de imágenes de los personajes desde la API
- **Material Design**: Interfaz moderna siguiendo las guías de Material Design

## Tecnologías Utilizadas

- **Java**: Lenguaje de programación
- **Android SDK**: Framework de desarrollo Android
- **Retrofit**: Cliente HTTP para llamadas a la API REST
- **Picasso**: Biblioteca para carga y caché de imágenes
- **RecyclerView**: Componente para listas eficientes
- **Fragments**: Arquitectura basada en fragmentos

## Estructura del Proyecto

```
SimpsonsApp/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/simpsonsapp/
│   │       │   ├── adapter/
│   │       │   │   └── CharacterAdapter.java
│   │       │   ├── fragments/
│   │       │   │   ├── CharacterListFragment.java
│   │       │   │   └── CharacterDetailFragment.java
│   │       │   ├── model/
│   │       │   │   ├── Character.java
│   │       │   │   └── CharacterListResponse.java
│   │       │   ├── service/
│   │       │   │   ├── ApiClient.java
│   │       │   │   └── SimpsonsApiService.java
│   │       │   └── MainActivity.java
│   │       └── res/
│   │           ├── layout/
│   │           ├── values/
│   │           └── drawable/
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## Configuración

### Requisitos

- Android Studio (versión recomendada: Flamingo o superior)
- Android SDK 24 o superior
- JDK 8 o superior

### Instalación

1. Clona o descarga el proyecto
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta la aplicación en un emulador o dispositivo físico

### Permisos

La aplicación requiere los siguientes permisos (ya configurados en el AndroidManifest.xml):

- `INTERNET`: Para realizar llamadas a la API
- `ACCESS_NETWORK_STATE`: Para verificar el estado de la conexión

## API Utilizada

La aplicación utiliza la API pública de The Simpsons:
- **Base URL**: `https://thesimpsonsapi.com/api/`
- **Documentación**: https://thesimpsonsapi.com/

### Endpoints Utilizados

- `GET /characters?page={page}&per_page={per_page}`: Obtiene una lista paginada de personajes
- `GET /characters/{id}`: Obtiene los detalles de un personaje específico

## Funcionalidades

### Lista de Personajes

- Muestra una lista de personajes con su imagen, nombre, ocupación y estado
- Scroll infinito que carga automáticamente más personajes
- Indicador de carga mientras se obtienen los datos

### Detalles del Personaje

- Imagen del personaje
- Nombre completo
- Edad
- Género
- Ocupación
- Fecha de nacimiento
- Estado (Vivo/Fallecido)
- Frases famosas (hasta 5 frases)

## Dependencias

```gradle
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.9.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.recyclerview:recyclerview:1.3.0'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.picasso:picasso:2.8'
implementation 'androidx.fragment:fragment:1.6.1'
```

## Autor

Desarrollado como proyecto educativo para el curso de Desarrollo de Aplicaciones Móviles.

## Licencia

Este proyecto es de código abierto y está disponible para uso educativo.

