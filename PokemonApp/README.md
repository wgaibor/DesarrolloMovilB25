# Pokemon App

Una aplicación móvil desarrollada en Java para Android que consume la API de Pokémon (https://pokeapi.co/) y muestra una lista de Pokémon con sus detalles.

## Características

- **Activity Principal**: `MainActivity` que maneja la navegación entre fragments
- **Fragment de Lista**: `PokemonListFragment` que muestra una lista de Pokémon usando RecyclerView
- **Fragment de Detalles**: `PokemonDetailFragment` que muestra información detallada de un Pokémon
- **Consumo de API**: Utiliza Retrofit para consumir la API de Pokémon
- **Carga de Imágenes**: Utiliza Picasso para cargar las imágenes de los Pokémon
- **Navegación**: Navegación fluida entre fragments con soporte para botón atrás

## Estructura del Proyecto

```
app/
├── src/main/
│   ├── java/com/example/pokemonapp/
│   │   ├── MainActivity.java
│   │   ├── model/
│   │   │   ├── Pokemon.java
│   │   │   ├── PokemonType.java
│   │   │   ├── PokemonStat.java
│   │   │   ├── PokemonSprites.java
│   │   │   ├── PokemonAbility.java
│   │   │   └── PokemonListResponse.java
│   │   ├── fragments/
│   │   │   ├── PokemonListFragment.java
│   │   │   └── PokemonDetailFragment.java
│   │   ├── adapter/
│   │   │   └── PokemonAdapter.java
│   │   └── service/
│   │       └── PokemonApiService.java
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── fragment_pokemon_list.xml
│   │   │   ├── fragment_pokemon_detail.xml
│   │   │   └── item_pokemon.xml
│   │   ├── values/
│   │   │   ├── colors.xml
│   │   │   ├── strings.xml
│   │   │   └── themes.xml
│   │   └── drawable/
│   │       └── ic_pokemon_placeholder.xml
│   └── AndroidManifest.xml
```

## Conceptos Aplicados

### Activity
- **MainActivity**: Activity principal que actúa como contenedor para los fragments
- Maneja la navegación entre fragments
- Implementa el patrón de navegación con soporte para botón atrás

### Fragment
- **PokemonListFragment**: Fragment que muestra la lista de Pokémon
- **PokemonDetailFragment**: Fragment que muestra los detalles de un Pokémon específico
- Comunicación entre fragments a través de la Activity principal

### Modelo de Datos
- Clases POJO para representar la estructura de datos de la API
- Serialización/deserialización automática con Gson

### Servicio de API
- **PokemonApiService**: Servicio singleton para consumir la API
- Utiliza Retrofit para las llamadas HTTP
- Manejo de respuestas asíncronas con Callbacks

### Interfaz de Usuario
- RecyclerView para mostrar la lista de Pokémon
- CardView para cada elemento de la lista
- ScrollView para los detalles del Pokémon
- ProgressBar para indicar carga

## Dependencias

- **AndroidX**: Para compatibilidad y componentes modernos
- **Retrofit**: Para consumo de API REST
- **Picasso**: Para carga de imágenes
- **Material Design**: Para componentes de UI

## Configuración

1. Asegúrate de tener Android Studio instalado
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta la aplicación en un dispositivo o emulador

## Permisos

La aplicación requiere los siguientes permisos:
- `INTERNET`: Para consumir la API
- `ACCESS_NETWORK_STATE`: Para verificar conectividad

## API Utilizada

- **PokéAPI**: https://pokeapi.co/
- Endpoints utilizados:
  - `GET /pokemon`: Lista de Pokémon
  - `GET /pokemon/{id}`: Detalles de un Pokémon específico
  - `GET /pokemon/{name}`: Detalles de un Pokémon por nombre

## Funcionalidades

1. **Lista de Pokémon**: Muestra los primeros 20 Pokémon con sus imágenes y tipos
2. **Detalles del Pokémon**: Información completa incluyendo estadísticas, habilidades y tipos
3. **Navegación**: Navegación fluida entre lista y detalles
4. **Carga de Imágenes**: Carga automática de sprites de Pokémon
5. **Manejo de Errores**: Gestión de errores de red y API

## Mejoras Futuras

- Implementar paginación en la lista
- Agregar búsqueda de Pokémon
- Implementar favoritos
- Agregar animaciones de transición
- Implementar caché de imágenes
- Agregar modo offline
