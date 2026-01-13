# Aplicación de Cafetería

Aplicación Android para gestionar el menú de una cafetería, permitiendo agregar desayunos y bebidas, y realizar pedidos mediante un carrito de compras.

## Características

- ✅ Gestión de artículos (Desayunos y Bebidas)
- ✅ Agregar artículos con imagen, nombre y precio
- ✅ Almacenamiento en Firebase Cloud Firestore
- ✅ Almacenamiento de imágenes en Firebase Storage
- ✅ Carrito de compras
- ✅ Interfaz intuitiva con Material Design

## Configuración de Firebase

### Paso 1: Crear un Proyecto en Firebase Console

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Haz clic en "Agregar proyecto" o selecciona un proyecto existente
3. Ingresa el nombre del proyecto (ej: "Cafeteria")
4. Sigue los pasos del asistente para crear el proyecto

### Paso 2: Agregar una Aplicación Android

1. En la página de descripción general del proyecto, haz clic en el icono de Android
2. Ingresa el **Package name** de tu aplicación:
   - El package name debe ser: `com.lemas.cafeteria`
   - Este debe coincidir exactamente con el `applicationId` en `app/build.gradle`
3. (Opcional) Ingresa un **App nickname** y **Debug signing certificate SHA-1**
4. Haz clic en "Registrar app"

### Paso 3: Descargar el archivo google-services.json

1. Descarga el archivo `google-services.json`
2. Coloca el archivo en la siguiente ubicación:
   ```
   app/google-services.json
   ```
   ⚠️ **Importante**: El archivo debe estar en la carpeta `app/` (no en `app/src/`)

### Paso 4: Configurar Cloud Firestore Database

1. En la consola de Firebase, ve a **Firestore Database** en el menú lateral
2. Haz clic en "Crear base de datos"
3. Selecciona el modo de seguridad:
   - **Modo de prueba** (para desarrollo): Permite lectura/escritura durante 30 días
   - **Modo de producción** (recomendado): Requiere reglas de seguridad personalizadas
4. Selecciona la ubicación de la base de datos (elige la más cercana a tus usuarios)
5. Haz clic en "Habilitar"

#### Configurar Reglas de Seguridad (Modo de Producción)

Si elegiste modo de producción, necesitas configurar las reglas de seguridad:

1. Ve a la pestaña **Reglas** en Firestore Database
2. Reemplaza las reglas con las siguientes (para desarrollo, permite lectura/escritura):

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Permitir lectura y escritura a todos los usuarios autenticados
    match /articulos/{document=**} {
      allow read, write: if request.auth != null;
    }
    
    // Para desarrollo, puedes usar estas reglas más permisivas:
    // match /{document=**} {
    //   allow read, write: if true;
    // }
  }
}
```

3. Haz clic en "Publicar"

#### Estructura de la Base de Datos

La aplicación creará automáticamente la siguiente estructura:

```
articulos/
  ├── {documentId}/
      ├── nombre: string
      ├── tipo: string ("desayuno" o "bebida")
      ├── precioUnitario: number
      └── imagenUrl: string
```

### Paso 5: Configurar Firebase Storage

1. En la consola de Firebase, ve a **Storage** en el menú lateral
2. Haz clic en "Empezar"
3. Revisa las reglas de seguridad y haz clic en "Siguiente"
4. Selecciona la ubicación de Storage (debe coincidir con la de Firestore)
5. Haz clic en "Listo"

#### Configurar Reglas de Seguridad de Storage

1. Ve a la pestaña **Reglas** en Storage
2. Reemplaza las reglas con las siguientes:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Permitir lectura y escritura a usuarios autenticados
    match /articulos/{allPaths=**} {
      allow read, write: if request.auth != null;
    }
    
    // Para desarrollo, puedes usar estas reglas más permisivas:
    // match /{allPaths=**} {
    //   allow read, write: if true;
    // }
  }
}
```

3. Haz clic en "Publicar"

#### Estructura de Storage

Las imágenes se almacenarán en la siguiente ruta:

```
articulos/
  └── {uuid}.jpg
```

### Paso 6: Verificar la Configuración

1. Asegúrate de que el archivo `google-services.json` esté en `app/google-services.json`
2. Verifica que el plugin de Google Services esté configurado en `app/build.gradle`:
   ```gradle
   plugins {
       alias(libs.plugins.android.application)
       alias(libs.plugins.google.gms.google.services)
   }
   ```
3. Verifica que las dependencias de Firebase estén agregadas en `app/build.gradle`:
   ```gradle
   dependencies {
       implementation libs.firebase.firestore
       implementation libs.firebase.storage
       // ... otras dependencias
   }
   ```

## Uso de la Aplicación

### Agregar Artículos

1. Navega a la pestaña **Desayunos** o **Bebidas**
2. Haz clic en el botón **+** en la parte superior derecha del toolbar
3. Completa el formulario:
   - **Nombre del Artículo**: Ingresa el nombre del desayuno o bebida
   - **Precio Unitario**: Ingresa el precio en formato numérico
   - **Imagen**: Selecciona una imagen desde la galería
4. Haz clic en **Guardar**

### Agregar al Carrito

1. En la lista de artículos, haz clic en cualquier artículo
2. El artículo se agregará automáticamente al carrito
3. Verás un mensaje de confirmación

### Ver y Gestionar el Carrito

1. Haz clic en el icono de **cesta** en el toolbar
2. En la pantalla del carrito podrás:
   - Ver todos los artículos agregados
   - Aumentar o disminuir la cantidad
   - Eliminar artículos
   - Ver el total a pagar
3. Haz clic en **Pagar** para procesar el pedido

## Estructura del Proyecto

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/lemas/cafeteria/
│   │   │   ├── activity/
│   │   │   │   ├── MenuActivity.java
│   │   │   │   └── CarritoActivity.java
│   │   │   ├── fragment/
│   │   │   │   ├── DesayunosFragment.java
│   │   │   │   └── BebidasFragment.java
│   │   │   ├── adapter/
│   │   │   │   ├── ArticuloAdapter.java
│   │   │   │   └── CarritoAdapter.java
│   │   │   ├── dialog/
│   │   │   │   └── AgregarArticuloDialog.java
│   │   │   ├── model/
│   │   │   │   ├── Articulo.java
│   │   │   │   └── CarritoItem.java
│   │   │   └── util/
│   │   │       └── CarritoManager.java
│   │   └── res/
│   │       ├── layout/
│   │       ├── menu/
│   │       └── values/
│   └── google-services.json
└── build.gradle
```

## Dependencias Principales

- **Firebase Auth**: Autenticación de usuarios
- **Firebase Firestore**: Base de datos NoSQL
- **Firebase Storage**: Almacenamiento de archivos
- **Glide**: Carga y caché de imágenes
- **Material Design Components**: Componentes de UI modernos
- **RecyclerView**: Lista eficiente de elementos

## Permisos Requeridos

La aplicación requiere los siguientes permisos (ya configurados en `AndroidManifest.xml`):

- `INTERNET`: Para conectarse a Firebase
- `READ_EXTERNAL_STORAGE`: Para leer imágenes de la galería (Android < 13)
- `READ_MEDIA_IMAGES`: Para leer imágenes de la galería (Android 13+)

## Solución de Problemas

### Error: "google-services.json not found"
- Verifica que el archivo esté en `app/google-services.json`
- Sincroniza el proyecto con Gradle (File → Sync Project with Gradle Files)

### Error: "FirebaseApp not initialized"
- Verifica que el plugin `google-services` esté aplicado en `app/build.gradle`
- Asegúrate de que el package name coincida con el registrado en Firebase

### Error al subir imágenes
- Verifica las reglas de seguridad de Storage
- Asegúrate de tener conexión a internet
- Verifica que el usuario esté autenticado (si usas reglas con autenticación)

### Los artículos no se muestran
- Verifica las reglas de seguridad de Firestore
- Asegúrate de que los documentos tengan el campo `tipo` correcto ("desayuno" o "bebida")
- Verifica la conexión a internet

## Notas Importantes

- ⚠️ Las reglas de seguridad mostradas en este README son para **desarrollo**. Para producción, implementa reglas más restrictivas.
- 🔒 En producción, considera implementar autenticación de usuarios antes de permitir operaciones de escritura.
- 📱 La aplicación está optimizada para Android 7.0 (API 24) y superiores.

## Licencia

Este proyecto es para fines educativos.
