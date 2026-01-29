# Sistema de Asistencia Escolar con QR

## Resumen

Sistema para registro de asistencia escolar mediante códigos QR. Los estudiantes presentan su QR al docente, quien lo escanea para registrar la asistencia. Los representantes pueden consultar el historial de asistencias de sus hijos y reciben notificaciones cuando llegan atrasados.

## Arquitectura General

```
┌─────────────────────────────────────────────────────────────┐
│                      DOCKER COMPOSE                         │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────────┐    ┌─────────────────────┐        │
│  │   API Spring Boot   │───▶│       MySQL         │        │
│  │     Puerto 8080     │    │    Puerto 3306      │        │
│  │  + Scheduler Jobs   │    │  + Parámetros       │        │
│  └─────────────────────┘    └─────────────────────┘        │
└─────────────────────────────────────────────────────────────┘
          │
          ▼
┌─────────────────────┐
│  Firebase Cloud     │ ◀── Notificaciones push (atrasos)
│    Messaging        │
└─────────────────────┘
          │
          ▼
┌─────────────────────────────────────────────────────────────┐
│                   APPS ANDROID (Java)                       │
├───────────────────┬───────────────────┬─────────────────────┤
│    Estudiante     │     Docente       │   Representante     │
│  - Ver su QR      │  - Escanear QR    │  - Ver asistencias  │
│  - Ver historial  │  - Listar curso   │  - Recibir alertas  │
│  - Filtrar estado │  - Filtrar estado │  - Filtrar estado   │
│  - Cerrar sesión  │  - Cerrar sesión  │  - Cerrar sesión    │
└───────────────────┴───────────────────┴─────────────────────┘
```

## Componentes

| Componente | Tecnología |
|------------|------------|
| Backend API | Spring Boot 3.2 + Java 17 |
| Base de datos | MySQL 8.0 (dockerizada) |
| Autenticación | JWT (usuario/contraseña) |
| Notificaciones | Firebase Cloud Messaging |
| App móvil | Android (Java) |
| Contenedores | Docker Compose |

## Actores y Funcionalidades

| Actor | Funcionalidades |
|-------|-----------------|
| Estudiante | Ver su QR, consultar historial, filtrar por estado, cerrar sesión |
| Docente | Escanear QR, ver cursos asignados, registrar asistencia, historial por curso |
| Representante | Ver hijos vinculados, historial de asistencias, recibir alertas de atrasos |

## Reglas de Negocio

- **PRESENTE**: Escaneo antes de las 07:30 (parametrizable)
- **ATRASO**: Escaneo entre 07:30 y 09:00 → notifica al representante vía push
- **FALTA**: Sin escaneo después de las 09:00 (job automático marca falta)

## Modelo de Datos

### Campos de Auditoría (en todas las tablas)

| Campo | Tipo | Descripción |
|-------|------|-------------|
| estado | VARCHAR(1) | 'A' activo, 'I' inactivo |
| usr_creacion | VARCHAR(50) | Usuario que creó |
| fe_creacion | DATETIME | Fecha de creación |
| usr_modificacion | VARCHAR(50) | Usuario que modificó |
| fe_ult_modificacion | DATETIME | Última modificación |

### Tablas

#### usuarios
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| cedula | VARCHAR(13) UNIQUE | Cédula de identidad |
| nombre | VARCHAR(100) | Nombre completo |
| email | VARCHAR(100) | Correo electrónico |
| password | VARCHAR(255) | Contraseña encriptada |
| rol | ENUM | ESTUDIANTE, DOCENTE, REPRESENTANTE |
| fcm_token | VARCHAR(255) | Token Firebase para push |

#### cursos
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| nombre | VARCHAR(50) | Nombre del curso |
| nivel | VARCHAR(20) | Nivel educativo |
| paralelo | VARCHAR(5) | Paralelo (A, B, C...) |
| anio_lectivo | VARCHAR(9) | Año lectivo (2025-2026) |

#### estudiantes
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| usuario_id | BIGINT FK | Referencia a usuarios |
| curso_id | BIGINT FK | Referencia a cursos |
| codigo_qr | VARCHAR(100) UNIQUE | UUID para el QR |

#### docentes
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| usuario_id | BIGINT FK | Referencia a usuarios |

#### docente_curso
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| docente_id | BIGINT FK | Referencia a docentes |
| curso_id | BIGINT FK | Referencia a cursos |

#### representantes
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| usuario_id | BIGINT FK | Referencia a usuarios |

#### representante_estudiante
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| representante_id | BIGINT FK | Referencia a representantes |
| estudiante_id | BIGINT FK | Referencia a estudiantes |
| parentesco | VARCHAR(20) | PADRE, MADRE, TUTOR, OTRO |

#### asistencias
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| estudiante_id | BIGINT FK | Referencia a estudiantes |
| fecha | DATE | Fecha del registro |
| hora_registro | TIME | Hora del escaneo |
| estado_asistencia | ENUM | PRESENTE, ATRASO, FALTA |
| registrado_por | BIGINT FK | Docente que registró |

#### parametros
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT PK | Identificador |
| clave | VARCHAR(50) UNIQUE | Identificador del parámetro |
| valor | VARCHAR(100) | Valor del parámetro |
| descripcion | VARCHAR(255) | Descripción del parámetro |

## Endpoints API REST

Base URL: `http://localhost:8080/api/v1`

### Autenticación
```
POST   /auth/login          → { cedula, password } → { token, rol, usuario }
POST   /auth/logout         → Invalida token
POST   /auth/refresh        → Renueva token JWT
```

### Estudiantes
```
GET    /estudiantes/mi-qr           → Retorna QR del estudiante logueado
GET    /estudiantes/mis-asistencias → Lista asistencias propias
GET    /estudiantes/mis-asistencias?estado=ATRASO&desde=2025-01-01&hasta=2025-01-31
```

### Docentes
```
GET    /docentes/mis-cursos                     → Cursos asignados
GET    /docentes/cursos/{cursoId}/estudiantes   → Lista estudiantes del curso
POST   /docentes/asistencia                     → { codigo_qr } → Registra asistencia
GET    /docentes/cursos/{cursoId}/asistencias   → Historial del curso
GET    /docentes/cursos/{cursoId}/asistencias?estado=FALTA&fecha=2025-01-28
```

### Representantes
```
GET    /representantes/mis-estudiantes                      → Hijos vinculados
GET    /representantes/estudiantes/{id}/asistencias         → Historial del hijo
GET    /representantes/estudiantes/{id}/asistencias?estado=PRESENTE
```

### Parámetros
```
GET    /parametros                  → Lista todos
PUT    /parametros/{clave}          → Actualiza valor
```

## Estructura del Proyecto Spring Boot

```
asistencia-api/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── src/main/java/com/lemas/asistencia/
    ├── AsistenciaApplication.java
    ├── config/
    │   ├── SecurityConfig.java
    │   ├── AuditoriaConfig.java
    │   └── FirebaseConfig.java
    ├── controller/
    │   ├── AuthController.java
    │   ├── EstudianteController.java
    │   ├── DocenteController.java
    │   ├── RepresentanteController.java
    │   └── ParametroController.java
    ├── service/
    │   ├── AuthService.java
    │   ├── AsistenciaService.java
    │   ├── NotificacionService.java
    │   ├── QrService.java
    │   └── FaltaSchedulerService.java
    ├── repository/
    ├── model/
    │   ├── entity/
    │   ├── dto/
    │   └── enums/
    ├── security/
    │   ├── JwtTokenProvider.java
    │   ├── JwtAuthenticationFilter.java
    │   └── UserDetailsServiceImpl.java
    ├── scheduler/
    │   └── FaltaScheduler.java
    └── exception/
```

## Configuración Docker

### docker-compose.yml
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: asistencia-db
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: asistencia_db
      MYSQL_USER: asistencia_user
      MYSQL_PASSWORD: asistencia_pass
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - asistencia-net

  api:
    build: .
    container_name: asistencia-api
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/asistencia_db
      SPRING_DATASOURCE_USERNAME: asistencia_user
      SPRING_DATASOURCE_PASSWORD: asistencia_pass
      JWT_SECRET: clave_secreta_jwt_cambiar_en_produccion
    ports:
      - "8080:8080"
    networks:
      - asistencia-net

volumes:
  mysql_data:

networks:
  asistencia-net:
```

### Dockerfile
```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Estructura App Android

```
app/src/main/java/com/lemas/asistencia/
├── AsistenciaApp.java
├── api/
│   ├── ApiClient.java
│   ├── AuthInterceptor.java
│   └── services/
├── model/
├── ui/
│   ├── login/
│   │   └── LoginActivity.java
│   ├── estudiante/
│   │   ├── QrActivity.java
│   │   └── HistorialEstudianteActivity.java
│   ├── docente/
│   │   ├── EscanerQrActivity.java
│   │   ├── CursoListActivity.java
│   │   └── HistorialCursoActivity.java
│   └── representante/
│       ├── HijosListActivity.java
│       └── HistorialHijoActivity.java
├── adapter/
├── util/
│   ├── SessionManager.java
│   ├── QrGenerator.java
│   └── Constants.java
└── firebase/
    └── MyFirebaseMessagingService.java
```

## Datos de Demo

### Usuarios de Prueba

| Rol | Cédula | Contraseña | Nombre |
|-----|--------|------------|--------|
| DOCENTE | 1234567890 | docente123 | Prof. María García |
| ESTUDIANTE | 0987654321 | estudiante123 | Carlos Pérez Mora |
| ESTUDIANTE | 0987654322 | estudiante123 | Ana López Torres |
| REPRESENTANTE | 1122334455 | representante123 | Pedro Pérez (padre de Carlos) |
| REPRESENTANTE | 1122334456 | representante123 | Laura Torres (madre de Ana) |

### Datos de Contexto

- **Curso**: 3ro Básica A (año lectivo 2025)
  - Docente asignado: Prof. María García
  - Estudiantes: Carlos Pérez, Ana López

- **Parámetros**:
  - hora_limite_atraso = "07:30"
  - hora_cierre_asistencia = "09:00"

- **Asistencias de ejemplo** (últimos 5 días):
  - Carlos: 3 PRESENTE, 1 ATRASO, 1 FALTA
  - Ana: 4 PRESENTE, 1 ATRASO

## Flujo de Prueba

1. Login como docente → escanear QR de Carlos → ver registro
2. Login como estudiante Carlos → ver su QR y su historial
3. Login como representante Pedro → ver asistencias de Carlos con filtros
