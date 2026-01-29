# Sistema de Asistencia QR - Plan de Implementación

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implementar API REST con Spring Boot dockerizada y base de datos MySQL para sistema de asistencia escolar con QR.

**Architecture:** API REST con Spring Boot 3.2, autenticación JWT, base de datos MySQL 8 dockerizada. Tres roles (Estudiante, Docente, Representante) con endpoints específicos. Scheduler para marcar faltas automáticas y Firebase para notificaciones push.

**Tech Stack:** Java 17, Spring Boot 3.2, Spring Security, JWT (jjwt), Spring Data JPA, MySQL 8, Docker Compose, Firebase Admin SDK.

---

## Fase 1: Infraestructura y Proyecto Base

### Task 1: Crear estructura del proyecto Spring Boot

**Files:**
- Create: `asistencia-api/pom.xml`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/AsistenciaApplication.java`
- Create: `asistencia-api/src/main/resources/application.yml`

**Step 1: Crear directorio del proyecto**

```bash
mkdir -p asistencia-api/src/main/java/com/lemas/asistencia
mkdir -p asistencia-api/src/main/resources
mkdir -p asistencia-api/src/test/java/com/lemas/asistencia
```

**Step 2: Crear pom.xml con dependencias**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>

    <groupId>com.lemas</groupId>
    <artifactId>asistencia-api</artifactId>
    <version>1.0.0</version>
    <name>asistencia-api</name>
    <description>API REST para Sistema de Asistencia Escolar con QR</description>

    <properties>
        <java.version>17</java.version>
        <jjwt.version>0.12.3</jjwt.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Firebase Admin -->
        <dependency>
            <groupId>com.google.firebase</groupId>
            <artifactId>firebase-admin</artifactId>
            <version>9.2.0</version>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**Step 3: Crear clase principal**

```java
// AsistenciaApplication.java
package com.lemas.asistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AsistenciaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsistenciaApplication.class, args);
    }
}
```

**Step 4: Crear application.yml**

```yaml
# application.yml
spring:
  application:
    name: asistencia-api

  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3307/asistencia_db}
    username: ${SPRING_DATASOURCE_USERNAME:asistencia_user}
    password: ${SPRING_DATASOURCE_PASSWORD:asistencia_pass}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true

server:
  port: 8080

jwt:
  secret: ${JWT_SECRET:clave_secreta_jwt_desarrollo_local_cambiar_en_produccion}
  expiration: 86400000  # 24 horas en milisegundos

firebase:
  enabled: ${FIREBASE_ENABLED:false}
```

**Step 5: Commit**

```bash
git add asistencia-api/
git commit -m "feat: create Spring Boot project structure with dependencies"
```

---

### Task 2: Configurar Docker y MySQL

**Files:**
- Create: `asistencia-api/docker-compose.yml`
- Create: `asistencia-api/Dockerfile`
- Create: `asistencia-api/init.sql`
- Create: `asistencia-api/.dockerignore`

**Step 1: Crear docker-compose.yml**

```yaml
# docker-compose.yml
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
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  api:
    build: .
    container_name: asistencia-api
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/asistencia_db?allowPublicKeyRetrieval=true&useSSL=false
      SPRING_DATASOURCE_USERNAME: asistencia_user
      SPRING_DATASOURCE_PASSWORD: asistencia_pass
      JWT_SECRET: clave_secreta_jwt_produccion_cambiar
      FIREBASE_ENABLED: "false"
    ports:
      - "8080:8080"
    networks:
      - asistencia-net

volumes:
  mysql_data:

networks:
  asistencia-net:
```

**Step 2: Crear Dockerfile**

```dockerfile
# Dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Step 3: Crear .dockerignore**

```
target/
.git/
.idea/
*.iml
.DS_Store
```

**Step 4: Crear init.sql con esquema y datos demo**

```sql
-- init.sql
-- Esquema de base de datos para Sistema de Asistencia

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(13) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    rol ENUM('ESTUDIANTE', 'DOCENTE', 'REPRESENTANTE') NOT NULL,
    fcm_token VARCHAR(255),
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de cursos
CREATE TABLE IF NOT EXISTS cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    nivel VARCHAR(20) NOT NULL,
    paralelo VARCHAR(5) NOT NULL,
    anio_lectivo VARCHAR(9) NOT NULL,
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_curso (nombre, paralelo, anio_lectivo)
);

-- Tabla de estudiantes
CREATE TABLE IF NOT EXISTS estudiantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    curso_id BIGINT NOT NULL,
    codigo_qr VARCHAR(100) NOT NULL UNIQUE,
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

-- Tabla de docentes
CREATE TABLE IF NOT EXISTS docentes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabla de relación docente-curso
CREATE TABLE IF NOT EXISTS docente_curso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    docente_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (docente_id) REFERENCES docentes(id),
    FOREIGN KEY (curso_id) REFERENCES cursos(id),
    UNIQUE KEY uk_docente_curso (docente_id, curso_id)
);

-- Tabla de representantes
CREATE TABLE IF NOT EXISTS representantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabla de relación representante-estudiante
CREATE TABLE IF NOT EXISTS representante_estudiante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    representante_id BIGINT NOT NULL,
    estudiante_id BIGINT NOT NULL,
    parentesco VARCHAR(20) NOT NULL,
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (representante_id) REFERENCES representantes(id),
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id),
    UNIQUE KEY uk_rep_est (representante_id, estudiante_id)
);

-- Tabla de asistencias
CREATE TABLE IF NOT EXISTS asistencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    hora_registro TIME,
    estado_asistencia ENUM('PRESENTE', 'ATRASO', 'FALTA') NOT NULL,
    registrado_por BIGINT,
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id),
    FOREIGN KEY (registrado_por) REFERENCES docentes(id),
    UNIQUE KEY uk_asistencia (estudiante_id, fecha)
);

-- Tabla de parámetros
CREATE TABLE IF NOT EXISTS parametros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(50) NOT NULL UNIQUE,
    valor VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    estado VARCHAR(1) DEFAULT 'A',
    usr_creacion VARCHAR(50),
    fe_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usr_modificacion VARCHAR(50),
    fe_ult_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =====================================================
-- DATOS DE DEMO
-- =====================================================

-- Parámetros del sistema
INSERT INTO parametros (clave, valor, descripcion, usr_creacion) VALUES
('hora_limite_atraso', '07:30', 'Hora límite para marcar presente (después es atraso)', 'SYSTEM'),
('hora_cierre_asistencia', '09:00', 'Hora para marcar falta automática si no hay registro', 'SYSTEM');

-- Curso de demo
INSERT INTO cursos (nombre, nivel, paralelo, anio_lectivo, usr_creacion) VALUES
('3ro Básica', 'Primaria', 'A', '2025-2026', 'SYSTEM');

-- Usuarios de demo (passwords: BCrypt de las contraseñas indicadas)
-- docente123 -> $2a$10$N9qo8uLOickgx2ZMRZoMye.IjqQBrkHXGw8pPJM1RLMkdXNiL7oKO
-- estudiante123 -> $2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.
-- representante123 -> $2a$10$kGnP0.gPz7K0Ub1kYL3YqOQhSMJQe9C0W8FsVPf1CmBjNq3DnJUyu

INSERT INTO usuarios (cedula, nombre, email, password, rol, usr_creacion) VALUES
('1234567890', 'Prof. María García', 'maria.garcia@lemas.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjqQBrkHXGw8pPJM1RLMkdXNiL7oKO', 'DOCENTE', 'SYSTEM'),
('0987654321', 'Carlos Pérez Mora', 'carlos.perez@lemas.edu', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ESTUDIANTE', 'SYSTEM'),
('0987654322', 'Ana López Torres', 'ana.lopez@lemas.edu', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ESTUDIANTE', 'SYSTEM'),
('1122334455', 'Pedro Pérez', 'pedro.perez@gmail.com', '$2a$10$kGnP0gPz7K0Ub1kYL3YqOQhSMJQe9C0W8FsVPf1CmBjNq3DnJUyu', 'REPRESENTANTE', 'SYSTEM'),
('1122334456', 'Laura Torres', 'laura.torres@gmail.com', '$2a$10$kGnP0gPz7K0Ub1kYL3YqOQhSMJQe9C0W8FsVPf1CmBjNq3DnJUyu', 'REPRESENTANTE', 'SYSTEM');

-- Docente
INSERT INTO docentes (usuario_id, usr_creacion) VALUES
((SELECT id FROM usuarios WHERE cedula = '1234567890'), 'SYSTEM');

-- Estudiantes
INSERT INTO estudiantes (usuario_id, curso_id, codigo_qr, usr_creacion) VALUES
((SELECT id FROM usuarios WHERE cedula = '0987654321'), 1, 'QR-EST-001-a1b2c3d4e5f6', 'SYSTEM'),
((SELECT id FROM usuarios WHERE cedula = '0987654322'), 1, 'QR-EST-002-g7h8i9j0k1l2', 'SYSTEM');

-- Representantes
INSERT INTO representantes (usuario_id, usr_creacion) VALUES
((SELECT id FROM usuarios WHERE cedula = '1122334455'), 'SYSTEM'),
((SELECT id FROM usuarios WHERE cedula = '1122334456'), 'SYSTEM');

-- Asignación docente-curso
INSERT INTO docente_curso (docente_id, curso_id, usr_creacion) VALUES
(1, 1, 'SYSTEM');

-- Relación representante-estudiante
INSERT INTO representante_estudiante (representante_id, estudiante_id, parentesco, usr_creacion) VALUES
(1, 1, 'PADRE', 'SYSTEM'),
(2, 2, 'MADRE', 'SYSTEM');

-- Asistencias de demo (últimos 5 días hábiles)
INSERT INTO asistencias (estudiante_id, fecha, hora_registro, estado_asistencia, registrado_por, usr_creacion) VALUES
-- Carlos Pérez: 3 PRESENTE, 1 ATRASO, 1 FALTA
(1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), '07:15:00', 'PRESENTE', 1, 'SYSTEM'),
(1, DATE_SUB(CURDATE(), INTERVAL 4 DAY), '07:20:00', 'PRESENTE', 1, 'SYSTEM'),
(1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), '07:45:00', 'ATRASO', 1, 'SYSTEM'),
(1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), NULL, 'FALTA', NULL, 'SYSTEM'),
(1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '07:10:00', 'PRESENTE', 1, 'SYSTEM'),
-- Ana López: 4 PRESENTE, 1 ATRASO
(2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), '07:10:00', 'PRESENTE', 1, 'SYSTEM'),
(2, DATE_SUB(CURDATE(), INTERVAL 4 DAY), '07:25:00', 'PRESENTE', 1, 'SYSTEM'),
(2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), '07:18:00', 'PRESENTE', 1, 'SYSTEM'),
(2, DATE_SUB(CURDATE(), INTERVAL 2 DAY), '07:50:00', 'ATRASO', 1, 'SYSTEM'),
(2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '07:22:00', 'PRESENTE', 1, 'SYSTEM');
```

**Step 5: Commit**

```bash
git add asistencia-api/docker-compose.yml asistencia-api/Dockerfile asistencia-api/init.sql asistencia-api/.dockerignore
git commit -m "feat: add Docker configuration with MySQL and init script"
```

---

## Fase 2: Modelo de Datos (Entities)

### Task 3: Crear enums y clase base de auditoría

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/enums/Rol.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/enums/EstadoAsistencia.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/enums/Parentesco.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/BaseEntity.java`

**Step 1: Crear enum Rol**

```java
// model/enums/Rol.java
package com.lemas.asistencia.model.enums;

public enum Rol {
    ESTUDIANTE,
    DOCENTE,
    REPRESENTANTE
}
```

**Step 2: Crear enum EstadoAsistencia**

```java
// model/enums/EstadoAsistencia.java
package com.lemas.asistencia.model.enums;

public enum EstadoAsistencia {
    PRESENTE,
    ATRASO,
    FALTA
}
```

**Step 3: Crear enum Parentesco**

```java
// model/enums/Parentesco.java
package com.lemas.asistencia.model.enums;

public enum Parentesco {
    PADRE,
    MADRE,
    TUTOR,
    OTRO
}
```

**Step 4: Crear BaseEntity con campos de auditoría**

```java
// model/entity/BaseEntity.java
package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    @Column(name = "estado", length = 1)
    private String estado = "A";

    @CreatedBy
    @Column(name = "usr_creacion", length = 50, updatable = false)
    private String usrCreacion;

    @CreatedDate
    @Column(name = "fe_creacion", updatable = false)
    private LocalDateTime feCreacion;

    @LastModifiedBy
    @Column(name = "usr_modificacion", length = 50)
    private String usrModificacion;

    @LastModifiedDate
    @Column(name = "fe_ult_modificacion")
    private LocalDateTime feUltModificacion;

    public boolean isActivo() {
        return "A".equals(this.estado);
    }

    public void activar() {
        this.estado = "A";
    }

    public void inactivar() {
        this.estado = "I";
    }
}
```

**Step 5: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/model/
git commit -m "feat: add enums and BaseEntity with audit fields"
```

---

### Task 4: Crear entidades principales (Usuario, Curso, Parametro)

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/Usuario.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/Curso.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/Parametro.java`

**Step 1: Crear entidad Usuario**

```java
// model/entity/Usuario.java
package com.lemas.asistencia.model.entity;

import com.lemas.asistencia.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 13)
    private String cedula;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(name = "fcm_token")
    private String fcmToken;
}
```

**Step 2: Crear entidad Curso**

```java
// model/entity/Curso.java
package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String nivel;

    @Column(nullable = false, length = 5)
    private String paralelo;

    @Column(name = "anio_lectivo", nullable = false, length = 9)
    private String anioLectivo;

    public String getNombreCompleto() {
        return nombre + " " + paralelo;
    }
}
```

**Step 3: Crear entidad Parametro**

```java
// model/entity/Parametro.java
package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parametros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parametro extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String clave;

    @Column(nullable = false, length = 100)
    private String valor;

    @Column(length = 255)
    private String descripcion;
}
```

**Step 4: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/model/entity/
git commit -m "feat: add Usuario, Curso and Parametro entities"
```

---

### Task 5: Crear entidades de roles específicos

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/Estudiante.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/Docente.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/Representante.java`

**Step 1: Crear entidad Estudiante**

```java
// model/entity/Estudiante.java
package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estudiantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estudiante extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "codigo_qr", nullable = false, unique = true, length = 100)
    private String codigoQr;
}
```

**Step 2: Crear entidad Docente**

```java
// model/entity/Docente.java
package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "docentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Docente extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "docente_curso",
        joinColumns = @JoinColumn(name = "docente_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    @Builder.Default
    private Set<Curso> cursos = new HashSet<>();
}
```

**Step 3: Crear entidad Representante**

```java
// model/entity/Representante.java
package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "representantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Representante extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "representante", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<RepresentanteEstudiante> estudiantes = new HashSet<>();
}
```

**Step 4: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/model/entity/
git commit -m "feat: add Estudiante, Docente and Representante entities"
```

---

### Task 6: Crear entidades de relación y asistencia

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/RepresentanteEstudiante.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/entity/Asistencia.java`

**Step 1: Crear entidad RepresentanteEstudiante**

```java
// model/entity/RepresentanteEstudiante.java
package com.lemas.asistencia.model.entity;

import com.lemas.asistencia.model.enums.Parentesco;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "representante_estudiante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepresentanteEstudiante extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representante_id", nullable = false)
    private Representante representante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Parentesco parentesco;
}
```

**Step 2: Crear entidad Asistencia**

```java
// model/entity/Asistencia.java
package com.lemas.asistencia.model.entity;

import com.lemas.asistencia.model.enums.EstadoAsistencia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "asistencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asistencia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_registro")
    private LocalTime horaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asistencia", nullable = false)
    private EstadoAsistencia estadoAsistencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por")
    private Docente registradoPor;
}
```

**Step 3: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/model/entity/
git commit -m "feat: add RepresentanteEstudiante and Asistencia entities"
```

---

## Fase 3: Repositorios

### Task 7: Crear repositorios JPA

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/repository/UsuarioRepository.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/repository/EstudianteRepository.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/repository/DocenteRepository.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/repository/RepresentanteRepository.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/repository/AsistenciaRepository.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/repository/ParametroRepository.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/repository/CursoRepository.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/repository/RepresentanteEstudianteRepository.java`

**Step 1: Crear UsuarioRepository**

```java
// repository/UsuarioRepository.java
package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCedulaAndEstado(String cedula, String estado);
    Optional<Usuario> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
}
```

**Step 2: Crear EstudianteRepository**

```java
// repository/EstudianteRepository.java
package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuarioIdAndEstado(Long usuarioId, String estado);
    Optional<Estudiante> findByCodigoQrAndEstado(String codigoQr, String estado);
    List<Estudiante> findByCursoIdAndEstado(Long cursoId, String estado);

    @Query("SELECT e FROM Estudiante e WHERE e.curso.id = :cursoId AND e.estado = 'A' " +
           "AND e.id NOT IN (SELECT a.estudiante.id FROM Asistencia a WHERE a.fecha = CURRENT_DATE)")
    List<Estudiante> findEstudiantesSinAsistenciaHoy(@Param("cursoId") Long cursoId);
}
```

**Step 3: Crear DocenteRepository**

```java
// repository/DocenteRepository.java
package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
```

**Step 4: Crear RepresentanteRepository**

```java
// repository/RepresentanteRepository.java
package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Representante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepresentanteRepository extends JpaRepository<Representante, Long> {
    Optional<Representante> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
```

**Step 5: Crear AsistenciaRepository**

```java
// repository/AsistenciaRepository.java
package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Asistencia;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    Optional<Asistencia> findByEstudianteIdAndFechaAndEstado(Long estudianteId, LocalDate fecha, String estado);

    List<Asistencia> findByEstudianteIdAndEstadoOrderByFechaDesc(Long estudianteId, String estado);

    @Query("SELECT a FROM Asistencia a WHERE a.estudiante.id = :estudianteId " +
           "AND a.estado = 'A' " +
           "AND (:estadoAsistencia IS NULL OR a.estadoAsistencia = :estadoAsistencia) " +
           "AND (:desde IS NULL OR a.fecha >= :desde) " +
           "AND (:hasta IS NULL OR a.fecha <= :hasta) " +
           "ORDER BY a.fecha DESC")
    List<Asistencia> findByEstudianteWithFilters(
            @Param("estudianteId") Long estudianteId,
            @Param("estadoAsistencia") EstadoAsistencia estadoAsistencia,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    @Query("SELECT a FROM Asistencia a WHERE a.estudiante.curso.id = :cursoId " +
           "AND a.estado = 'A' " +
           "AND (:estadoAsistencia IS NULL OR a.estadoAsistencia = :estadoAsistencia) " +
           "AND (:fecha IS NULL OR a.fecha = :fecha) " +
           "ORDER BY a.fecha DESC, a.estudiante.usuario.nombre ASC")
    List<Asistencia> findByCursoWithFilters(
            @Param("cursoId") Long cursoId,
            @Param("estadoAsistencia") EstadoAsistencia estadoAsistencia,
            @Param("fecha") LocalDate fecha);

    @Query("SELECT a FROM Asistencia a WHERE a.estudiante.id IN " +
           "(SELECT e.id FROM Estudiante e WHERE e.estado = 'A') " +
           "AND a.fecha = CURRENT_DATE AND a.estado = 'A'")
    List<Asistencia> findAsistenciasDeHoy();
}
```

**Step 6: Crear ParametroRepository**

```java
// repository/ParametroRepository.java
package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long> {
    Optional<Parametro> findByClaveAndEstado(String clave, String estado);
}
```

**Step 7: Crear CursoRepository**

```java
// repository/CursoRepository.java
package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByEstado(String estado);
}
```

**Step 8: Crear RepresentanteEstudianteRepository**

```java
// repository/RepresentanteEstudianteRepository.java
package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.RepresentanteEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepresentanteEstudianteRepository extends JpaRepository<RepresentanteEstudiante, Long> {
    List<RepresentanteEstudiante> findByRepresentanteIdAndEstado(Long representanteId, String estado);
    List<RepresentanteEstudiante> findByEstudianteIdAndEstado(Long estudianteId, String estado);
}
```

**Step 9: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/repository/
git commit -m "feat: add JPA repositories for all entities"
```

---

## Fase 4: Seguridad y JWT

### Task 8: Configurar seguridad JWT

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/security/JwtTokenProvider.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/security/JwtAuthenticationFilter.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/security/UserDetailsServiceImpl.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/security/UserPrincipal.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/config/SecurityConfig.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/config/AuditoriaConfig.java`

**Step 1: Crear UserPrincipal**

```java
// security/UserPrincipal.java
package com.lemas.asistencia.security;

import com.lemas.asistencia.model.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;
    private String cedula;
    private String nombre;
    private String password;
    private String rol;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(Usuario usuario) {
        return new UserPrincipal(
                usuario.getId(),
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getPassword(),
                usuario.getRol().name(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }

    @Override
    public String getUsername() {
        return cedula;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

**Step 2: Crear JwtTokenProvider**

```java
// security/JwtTokenProvider.java
package com.lemas.asistencia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(userPrincipal.getCedula())
                .claim("id", userPrincipal.getId())
                .claim("nombre", userPrincipal.getNombre())
                .claim("rol", userPrincipal.getRol())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String getCedulaFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

**Step 3: Crear UserDetailsServiceImpl**

```java
// security/UserDetailsServiceImpl.java
package com.lemas.asistencia.security;

import com.lemas.asistencia.model.entity.Usuario;
import com.lemas.asistencia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCedulaAndEstado(cedula, "A")
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + cedula));
        return UserPrincipal.create(usuario);
    }
}
```

**Step 4: Crear JwtAuthenticationFilter**

```java
// security/JwtAuthenticationFilter.java
package com.lemas.asistencia.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            String cedula = tokenProvider.getCedulaFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(cedula);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

**Step 5: Crear SecurityConfig**

```java
// config/SecurityConfig.java
package com.lemas.asistencia.config;

import com.lemas.asistencia.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/estudiantes/**").hasRole("ESTUDIANTE")
                .requestMatchers("/api/v1/docentes/**").hasRole("DOCENTE")
                .requestMatchers("/api/v1/representantes/**").hasRole("REPRESENTANTE")
                .requestMatchers("/api/v1/parametros/**").hasAnyRole("DOCENTE", "REPRESENTANTE")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

**Step 6: Crear AuditoriaConfig**

```java
// config/AuditoriaConfig.java
package com.lemas.asistencia.config;

import com.lemas.asistencia.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditoriaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("SYSTEM");
            }
            if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
                return Optional.of(userPrincipal.getCedula());
            }
            return Optional.of("SYSTEM");
        };
    }
}
```

**Step 7: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/security/
git add asistencia-api/src/main/java/com/lemas/asistencia/config/
git commit -m "feat: add JWT security configuration with audit support"
```

---

## Fase 5: DTOs y Excepciones

### Task 9: Crear DTOs

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/LoginRequest.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/LoginResponse.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/UsuarioResponse.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/AsistenciaResponse.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/EstudianteResponse.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/CursoResponse.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/RegistrarAsistenciaRequest.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/QrResponse.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/ParametroResponse.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/model/dto/ParametroUpdateRequest.java`

**Step 1: Crear DTOs de autenticación**

```java
// model/dto/LoginRequest.java
package com.lemas.asistencia.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "La cédula es requerida")
    private String cedula;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
```

```java
// model/dto/LoginResponse.java
package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String tipo;
    private UsuarioResponse usuario;
}
```

```java
// model/dto/UsuarioResponse.java
package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {
    private Long id;
    private String cedula;
    private String nombre;
    private String email;
    private String rol;
}
```

**Step 2: Crear DTOs de respuesta**

```java
// model/dto/AsistenciaResponse.java
package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AsistenciaResponse {
    private Long id;
    private EstudianteResponse estudiante;
    private LocalDate fecha;
    private LocalTime horaRegistro;
    private String estadoAsistencia;
    private UsuarioResponse registradoPor;
}
```

```java
// model/dto/EstudianteResponse.java
package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstudianteResponse {
    private Long id;
    private String nombre;
    private String cedula;
    private CursoResponse curso;
    private String parentesco;
}
```

```java
// model/dto/CursoResponse.java
package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursoResponse {
    private Long id;
    private String nombre;
    private String nivel;
    private String paralelo;
    private String anioLectivo;
    private String nombreCompleto;
}
```

```java
// model/dto/QrResponse.java
package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrResponse {
    private String codigoQr;
    private String nombreEstudiante;
    private String curso;
}
```

**Step 3: Crear DTOs de request**

```java
// model/dto/RegistrarAsistenciaRequest.java
package com.lemas.asistencia.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrarAsistenciaRequest {
    @NotBlank(message = "El código QR es requerido")
    private String codigoQr;
}
```

```java
// model/dto/ParametroResponse.java
package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParametroResponse {
    private Long id;
    private String clave;
    private String valor;
    private String descripcion;
}
```

```java
// model/dto/ParametroUpdateRequest.java
package com.lemas.asistencia.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParametroUpdateRequest {
    @NotBlank(message = "El valor es requerido")
    private String valor;
}
```

**Step 4: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/model/dto/
git commit -m "feat: add DTOs for API requests and responses"
```

---

### Task 10: Crear manejo de excepciones

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/exception/ApiException.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/exception/ResourceNotFoundException.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/exception/BadRequestException.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/exception/GlobalExceptionHandler.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/exception/ErrorResponse.java`

**Step 1: Crear excepciones personalizadas**

```java
// exception/ApiException.java
package com.lemas.asistencia.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
```

```java
// exception/ResourceNotFoundException.java
package com.lemas.asistencia.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
```

```java
// exception/BadRequestException.java
package com.lemas.asistencia.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
```

**Step 2: Crear ErrorResponse y GlobalExceptionHandler**

```java
// exception/ErrorResponse.java
package com.lemas.asistencia.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
```

```java
// exception/GlobalExceptionHandler.java
package com.lemas.asistencia.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message("Credenciales inválidas")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(message)
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Error interno del servidor")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

**Step 3: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/exception/
git commit -m "feat: add exception handling with global handler"
```

---

## Fase 6: Servicios

### Task 11: Crear servicios de negocio

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/service/AuthService.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/service/AsistenciaService.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/service/ParametroService.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/service/NotificacionService.java`

**Step 1: Crear AuthService**

```java
// service/AuthService.java
package com.lemas.asistencia.service;

import com.lemas.asistencia.model.dto.LoginRequest;
import com.lemas.asistencia.model.dto.LoginResponse;
import com.lemas.asistencia.model.dto.UsuarioResponse;
import com.lemas.asistencia.model.entity.Usuario;
import com.lemas.asistencia.repository.UsuarioRepository;
import com.lemas.asistencia.security.JwtTokenProvider;
import com.lemas.asistencia.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCedula(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(UsuarioResponse.builder()
                        .id(userPrincipal.getId())
                        .cedula(userPrincipal.getCedula())
                        .nombre(userPrincipal.getNombre())
                        .rol(userPrincipal.getRol())
                        .build())
                .build();
    }

    @Transactional
    public void updateFcmToken(Long usuarioId, String fcmToken) {
        usuarioRepository.findById(usuarioId).ifPresent(usuario -> {
            usuario.setFcmToken(fcmToken);
            usuarioRepository.save(usuario);
        });
    }

    public UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }
}
```

**Step 2: Crear ParametroService**

```java
// service/ParametroService.java
package com.lemas.asistencia.service;

import com.lemas.asistencia.exception.ResourceNotFoundException;
import com.lemas.asistencia.model.dto.ParametroResponse;
import com.lemas.asistencia.model.dto.ParametroUpdateRequest;
import com.lemas.asistencia.model.entity.Parametro;
import com.lemas.asistencia.repository.ParametroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParametroService {

    private final ParametroRepository parametroRepository;

    @Transactional(readOnly = true)
    public List<ParametroResponse> listarTodos() {
        return parametroRepository.findAll().stream()
                .filter(p -> "A".equals(p.getEstado()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParametroResponse actualizar(String clave, ParametroUpdateRequest request) {
        Parametro parametro = parametroRepository.findByClaveAndEstado(clave, "A")
                .orElseThrow(() -> new ResourceNotFoundException("Parámetro no encontrado: " + clave));

        parametro.setValor(request.getValor());
        parametro = parametroRepository.save(parametro);

        return toResponse(parametro);
    }

    @Transactional(readOnly = true)
    public LocalTime getHoraLimiteAtraso() {
        return parametroRepository.findByClaveAndEstado("hora_limite_atraso", "A")
                .map(p -> LocalTime.parse(p.getValor()))
                .orElse(LocalTime.of(7, 30));
    }

    @Transactional(readOnly = true)
    public LocalTime getHoraCierreAsistencia() {
        return parametroRepository.findByClaveAndEstado("hora_cierre_asistencia", "A")
                .map(p -> LocalTime.parse(p.getValor()))
                .orElse(LocalTime.of(9, 0));
    }

    private ParametroResponse toResponse(Parametro parametro) {
        return ParametroResponse.builder()
                .id(parametro.getId())
                .clave(parametro.getClave())
                .valor(parametro.getValor())
                .descripcion(parametro.getDescripcion())
                .build();
    }
}
```

**Step 3: Crear NotificacionService (placeholder para Firebase)**

```java
// service/NotificacionService.java
package com.lemas.asistencia.service;

import com.lemas.asistencia.model.entity.Estudiante;
import com.lemas.asistencia.model.entity.RepresentanteEstudiante;
import com.lemas.asistencia.repository.RepresentanteEstudianteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final RepresentanteEstudianteRepository representanteEstudianteRepository;

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    public void notificarAtraso(Estudiante estudiante) {
        if (!firebaseEnabled) {
            log.info("Firebase deshabilitado. Atraso de {} no notificado.", estudiante.getUsuario().getNombre());
            return;
        }

        List<RepresentanteEstudiante> relaciones =
                representanteEstudianteRepository.findByEstudianteIdAndEstado(estudiante.getId(), "A");

        for (RepresentanteEstudiante relacion : relaciones) {
            String fcmToken = relacion.getRepresentante().getUsuario().getFcmToken();
            if (fcmToken != null && !fcmToken.isEmpty()) {
                enviarPushNotification(
                        fcmToken,
                        "Atraso registrado",
                        estudiante.getUsuario().getNombre() + " llegó tarde hoy."
                );
            }
        }
    }

    private void enviarPushNotification(String token, String title, String body) {
        // TODO: Implementar con Firebase Admin SDK cuando se configure
        log.info("Push notification: token={}, title={}, body={}", token, title, body);
    }
}
```

**Step 4: Crear AsistenciaService**

```java
// service/AsistenciaService.java
package com.lemas.asistencia.service;

import com.lemas.asistencia.exception.BadRequestException;
import com.lemas.asistencia.exception.ResourceNotFoundException;
import com.lemas.asistencia.model.dto.*;
import com.lemas.asistencia.model.entity.*;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.repository.*;
import com.lemas.asistencia.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final EstudianteRepository estudianteRepository;
    private final DocenteRepository docenteRepository;
    private final RepresentanteRepository representanteRepository;
    private final RepresentanteEstudianteRepository representanteEstudianteRepository;
    private final ParametroService parametroService;
    private final NotificacionService notificacionService;
    private final AuthService authService;

    // ==================== ESTUDIANTE ====================

    @Transactional(readOnly = true)
    public QrResponse obtenerMiQr() {
        UserPrincipal user = authService.getCurrentUser();
        Estudiante estudiante = estudianteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        return QrResponse.builder()
                .codigoQr(estudiante.getCodigoQr())
                .nombreEstudiante(estudiante.getUsuario().getNombre())
                .curso(estudiante.getCurso().getNombreCompleto())
                .build();
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> obtenerMisAsistencias(EstadoAsistencia estado, LocalDate desde, LocalDate hasta) {
        UserPrincipal user = authService.getCurrentUser();
        Estudiante estudiante = estudianteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        return asistenciaRepository.findByEstudianteWithFilters(estudiante.getId(), estado, desde, hasta)
                .stream()
                .map(this::toAsistenciaResponse)
                .collect(Collectors.toList());
    }

    // ==================== DOCENTE ====================

    @Transactional(readOnly = true)
    public List<CursoResponse> obtenerMisCursos() {
        UserPrincipal user = authService.getCurrentUser();
        Docente docente = docenteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        return docente.getCursos().stream()
                .filter(c -> "A".equals(c.getEstado()))
                .map(this::toCursoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstudianteResponse> obtenerEstudiantesCurso(Long cursoId) {
        verificarDocenteTieneCurso(cursoId);

        return estudianteRepository.findByCursoIdAndEstado(cursoId, "A").stream()
                .map(e -> toEstudianteResponse(e, null))
                .collect(Collectors.toList());
    }

    @Transactional
    public AsistenciaResponse registrarAsistencia(RegistrarAsistenciaRequest request) {
        UserPrincipal user = authService.getCurrentUser();
        Docente docente = docenteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        Estudiante estudiante = estudianteRepository.findByCodigoQrAndEstado(request.getCodigoQr(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Código QR inválido"));

        // Verificar que el docente tiene asignado el curso del estudiante
        boolean tieneCurso = docente.getCursos().stream()
                .anyMatch(c -> c.getId().equals(estudiante.getCurso().getId()));
        if (!tieneCurso) {
            throw new BadRequestException("No tiene permiso para registrar asistencia de este estudiante");
        }

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        // Verificar si ya existe asistencia para hoy
        if (asistenciaRepository.findByEstudianteIdAndFechaAndEstado(estudiante.getId(), hoy, "A").isPresent()) {
            throw new BadRequestException("Ya se registró asistencia para este estudiante hoy");
        }

        // Determinar estado según la hora
        LocalTime horaLimiteAtraso = parametroService.getHoraLimiteAtraso();
        EstadoAsistencia estadoAsistencia = ahora.isBefore(horaLimiteAtraso)
                ? EstadoAsistencia.PRESENTE
                : EstadoAsistencia.ATRASO;

        Asistencia asistencia = Asistencia.builder()
                .estudiante(estudiante)
                .fecha(hoy)
                .horaRegistro(ahora)
                .estadoAsistencia(estadoAsistencia)
                .registradoPor(docente)
                .build();

        asistencia = asistenciaRepository.save(asistencia);

        // Notificar si es atraso
        if (estadoAsistencia == EstadoAsistencia.ATRASO) {
            notificacionService.notificarAtraso(estudiante);
        }

        return toAsistenciaResponse(asistencia);
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> obtenerAsistenciasCurso(Long cursoId, EstadoAsistencia estado, LocalDate fecha) {
        verificarDocenteTieneCurso(cursoId);

        return asistenciaRepository.findByCursoWithFilters(cursoId, estado, fecha).stream()
                .map(this::toAsistenciaResponse)
                .collect(Collectors.toList());
    }

    // ==================== REPRESENTANTE ====================

    @Transactional(readOnly = true)
    public List<EstudianteResponse> obtenerMisEstudiantes() {
        UserPrincipal user = authService.getCurrentUser();
        Representante representante = representanteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Representante no encontrado"));

        return representanteEstudianteRepository.findByRepresentanteIdAndEstado(representante.getId(), "A")
                .stream()
                .map(re -> toEstudianteResponse(re.getEstudiante(), re.getParentesco().name()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> obtenerAsistenciasEstudiante(Long estudianteId, EstadoAsistencia estado,
                                                                  LocalDate desde, LocalDate hasta) {
        verificarRepresentanteTieneEstudiante(estudianteId);

        return asistenciaRepository.findByEstudianteWithFilters(estudianteId, estado, desde, hasta)
                .stream()
                .map(this::toAsistenciaResponse)
                .collect(Collectors.toList());
    }

    // ==================== HELPERS ====================

    private void verificarDocenteTieneCurso(Long cursoId) {
        UserPrincipal user = authService.getCurrentUser();
        Docente docente = docenteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        boolean tieneCurso = docente.getCursos().stream()
                .anyMatch(c -> c.getId().equals(cursoId));
        if (!tieneCurso) {
            throw new BadRequestException("No tiene acceso a este curso");
        }
    }

    private void verificarRepresentanteTieneEstudiante(Long estudianteId) {
        UserPrincipal user = authService.getCurrentUser();
        Representante representante = representanteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Representante no encontrado"));

        boolean tieneEstudiante = representanteEstudianteRepository
                .findByRepresentanteIdAndEstado(representante.getId(), "A")
                .stream()
                .anyMatch(re -> re.getEstudiante().getId().equals(estudianteId));

        if (!tieneEstudiante) {
            throw new BadRequestException("No tiene acceso a este estudiante");
        }
    }

    private AsistenciaResponse toAsistenciaResponse(Asistencia asistencia) {
        return AsistenciaResponse.builder()
                .id(asistencia.getId())
                .estudiante(toEstudianteResponse(asistencia.getEstudiante(), null))
                .fecha(asistencia.getFecha())
                .horaRegistro(asistencia.getHoraRegistro())
                .estadoAsistencia(asistencia.getEstadoAsistencia().name())
                .registradoPor(asistencia.getRegistradoPor() != null
                        ? toUsuarioResponse(asistencia.getRegistradoPor().getUsuario())
                        : null)
                .build();
    }

    private EstudianteResponse toEstudianteResponse(Estudiante estudiante, String parentesco) {
        return EstudianteResponse.builder()
                .id(estudiante.getId())
                .nombre(estudiante.getUsuario().getNombre())
                .cedula(estudiante.getUsuario().getCedula())
                .curso(toCursoResponse(estudiante.getCurso()))
                .parentesco(parentesco)
                .build();
    }

    private CursoResponse toCursoResponse(Curso curso) {
        return CursoResponse.builder()
                .id(curso.getId())
                .nombre(curso.getNombre())
                .nivel(curso.getNivel())
                .paralelo(curso.getParalelo())
                .anioLectivo(curso.getAnioLectivo())
                .nombreCompleto(curso.getNombreCompleto())
                .build();
    }

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .cedula(usuario.getCedula())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }
}
```

**Step 5: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/service/
git commit -m "feat: add business services for auth, asistencia, parametros and notifications"
```

---

### Task 12: Crear scheduler para faltas automáticas

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/scheduler/FaltaScheduler.java`

**Step 1: Crear FaltaScheduler**

```java
// scheduler/FaltaScheduler.java
package com.lemas.asistencia.scheduler;

import com.lemas.asistencia.model.entity.Asistencia;
import com.lemas.asistencia.model.entity.Estudiante;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.repository.AsistenciaRepository;
import com.lemas.asistencia.repository.EstudianteRepository;
import com.lemas.asistencia.service.ParametroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FaltaScheduler {

    private final EstudianteRepository estudianteRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final ParametroService parametroService;

    // Ejecutar cada minuto para verificar si es hora de marcar faltas
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void marcarFaltasAutomaticas() {
        LocalTime horaCierre = parametroService.getHoraCierreAsistencia();
        LocalTime ahora = LocalTime.now();

        // Solo ejecutar si estamos en la hora de cierre (±1 minuto)
        if (ahora.getHour() != horaCierre.getHour() || ahora.getMinute() != horaCierre.getMinute()) {
            return;
        }

        log.info("Iniciando marcado automático de faltas...");

        LocalDate hoy = LocalDate.now();
        List<Estudiante> todosEstudiantes = estudianteRepository.findAll().stream()
                .filter(e -> "A".equals(e.getEstado()))
                .toList();

        int faltasMarcadas = 0;
        for (Estudiante estudiante : todosEstudiantes) {
            boolean tieneAsistenciaHoy = asistenciaRepository
                    .findByEstudianteIdAndFechaAndEstado(estudiante.getId(), hoy, "A")
                    .isPresent();

            if (!tieneAsistenciaHoy) {
                Asistencia falta = Asistencia.builder()
                        .estudiante(estudiante)
                        .fecha(hoy)
                        .horaRegistro(null)
                        .estadoAsistencia(EstadoAsistencia.FALTA)
                        .registradoPor(null)
                        .build();
                falta.setUsrCreacion("SCHEDULER");
                asistenciaRepository.save(falta);
                faltasMarcadas++;
            }
        }

        log.info("Faltas marcadas automáticamente: {}", faltasMarcadas);
    }
}
```

**Step 2: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/scheduler/
git commit -m "feat: add scheduler for automatic absence marking"
```

---

## Fase 7: Controladores

### Task 13: Crear controladores REST

**Files:**
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/controller/AuthController.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/controller/EstudianteController.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/controller/DocenteController.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/controller/RepresentanteController.java`
- Create: `asistencia-api/src/main/java/com/lemas/asistencia/controller/ParametroController.java`

**Step 1: Crear AuthController**

```java
// controller/AuthController.java
package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.LoginRequest;
import com.lemas.asistencia.model.dto.LoginResponse;
import com.lemas.asistencia.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // JWT es stateless, el logout se maneja en el cliente eliminando el token
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada exitosamente"));
    }

    @PostMapping("/fcm-token")
    public ResponseEntity<Map<String, String>> updateFcmToken(@RequestBody Map<String, String> request) {
        String fcmToken = request.get("fcmToken");
        Long usuarioId = authService.getCurrentUser().getId();
        authService.updateFcmToken(usuarioId, fcmToken);
        return ResponseEntity.ok(Map.of("message", "Token FCM actualizado"));
    }
}
```

**Step 2: Crear EstudianteController**

```java
// controller/EstudianteController.java
package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.AsistenciaResponse;
import com.lemas.asistencia.model.dto.QrResponse;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final AsistenciaService asistenciaService;

    @GetMapping("/mi-qr")
    public ResponseEntity<QrResponse> obtenerMiQr() {
        return ResponseEntity.ok(asistenciaService.obtenerMiQr());
    }

    @GetMapping("/mis-asistencias")
    public ResponseEntity<List<AsistenciaResponse>> obtenerMisAsistencias(
            @RequestParam(required = false) EstadoAsistencia estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(asistenciaService.obtenerMisAsistencias(estado, desde, hasta));
    }
}
```

**Step 3: Crear DocenteController**

```java
// controller/DocenteController.java
package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.*;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final AsistenciaService asistenciaService;

    @GetMapping("/mis-cursos")
    public ResponseEntity<List<CursoResponse>> obtenerMisCursos() {
        return ResponseEntity.ok(asistenciaService.obtenerMisCursos());
    }

    @GetMapping("/cursos/{cursoId}/estudiantes")
    public ResponseEntity<List<EstudianteResponse>> obtenerEstudiantesCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(asistenciaService.obtenerEstudiantesCurso(cursoId));
    }

    @PostMapping("/asistencia")
    public ResponseEntity<AsistenciaResponse> registrarAsistencia(
            @Valid @RequestBody RegistrarAsistenciaRequest request) {
        return ResponseEntity.ok(asistenciaService.registrarAsistencia(request));
    }

    @GetMapping("/cursos/{cursoId}/asistencias")
    public ResponseEntity<List<AsistenciaResponse>> obtenerAsistenciasCurso(
            @PathVariable Long cursoId,
            @RequestParam(required = false) EstadoAsistencia estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(asistenciaService.obtenerAsistenciasCurso(cursoId, estado, fecha));
    }
}
```

**Step 4: Crear RepresentanteController**

```java
// controller/RepresentanteController.java
package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.AsistenciaResponse;
import com.lemas.asistencia.model.dto.EstudianteResponse;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/representantes")
@RequiredArgsConstructor
public class RepresentanteController {

    private final AsistenciaService asistenciaService;

    @GetMapping("/mis-estudiantes")
    public ResponseEntity<List<EstudianteResponse>> obtenerMisEstudiantes() {
        return ResponseEntity.ok(asistenciaService.obtenerMisEstudiantes());
    }

    @GetMapping("/estudiantes/{estudianteId}/asistencias")
    public ResponseEntity<List<AsistenciaResponse>> obtenerAsistenciasEstudiante(
            @PathVariable Long estudianteId,
            @RequestParam(required = false) EstadoAsistencia estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(asistenciaService.obtenerAsistenciasEstudiante(estudianteId, estado, desde, hasta));
    }
}
```

**Step 5: Crear ParametroController**

```java
// controller/ParametroController.java
package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.ParametroResponse;
import com.lemas.asistencia.model.dto.ParametroUpdateRequest;
import com.lemas.asistencia.service.ParametroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parametros")
@RequiredArgsConstructor
public class ParametroController {

    private final ParametroService parametroService;

    @GetMapping
    public ResponseEntity<List<ParametroResponse>> listarTodos() {
        return ResponseEntity.ok(parametroService.listarTodos());
    }

    @PutMapping("/{clave}")
    public ResponseEntity<ParametroResponse> actualizar(
            @PathVariable String clave,
            @Valid @RequestBody ParametroUpdateRequest request) {
        return ResponseEntity.ok(parametroService.actualizar(clave, request));
    }
}
```

**Step 6: Commit**

```bash
git add asistencia-api/src/main/java/com/lemas/asistencia/controller/
git commit -m "feat: add REST controllers for all endpoints"
```

---

## Fase 8: Maven Wrapper y Verificación

### Task 14: Agregar Maven Wrapper y verificar compilación

**Files:**
- Create: `asistencia-api/mvnw`
- Create: `asistencia-api/mvnw.cmd`
- Create: `asistencia-api/.mvn/wrapper/maven-wrapper.properties`

**Step 1: Generar Maven Wrapper**

```bash
cd asistencia-api
mvn wrapper:wrapper -Dmaven=3.9.6
```

**Step 2: Verificar que compila**

```bash
./mvnw clean compile
```

Expected: BUILD SUCCESS

**Step 3: Commit**

```bash
git add .mvn/ mvnw mvnw.cmd
git commit -m "chore: add Maven Wrapper"
```

---

### Task 15: Levantar Docker y probar API

**Step 1: Levantar solo MySQL primero**

```bash
cd asistencia-api
docker-compose up -d mysql
```

**Step 2: Esperar que MySQL esté listo y ejecutar app local**

```bash
./mvnw spring-boot:run
```

Expected: Application started on port 8080

**Step 3: Probar login con curl**

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"cedula":"1234567890","password":"docente123"}'
```

Expected: JSON con token JWT

**Step 4: Probar endpoint protegido**

```bash
TOKEN="<token_del_paso_anterior>"
curl -X GET http://localhost:8080/api/v1/docentes/mis-cursos \
  -H "Authorization: Bearer $TOKEN"
```

Expected: Lista de cursos

**Step 5: Levantar todo con Docker Compose**

```bash
docker-compose down
docker-compose up -d --build
```

**Step 6: Commit final**

```bash
git add -A
git commit -m "feat: complete API implementation with Docker support"
```

---

## Resumen de Credenciales de Demo

| Rol | Cédula | Contraseña |
|-----|--------|------------|
| DOCENTE | 1234567890 | docente123 |
| ESTUDIANTE | 0987654321 | estudiante123 |
| ESTUDIANTE | 0987654322 | estudiante123 |
| REPRESENTANTE | 1122334455 | representante123 |
| REPRESENTANTE | 1122334456 | representante123 |

---

## Próximos Pasos (App Android)

Una vez la API esté funcionando, continuar con:
1. Crear proyecto Android con estructura base
2. Implementar LoginActivity
3. Implementar QrActivity (estudiante)
4. Implementar EscanerQrActivity (docente)
5. Implementar HistorialActivity (representante)
6. Integrar Firebase Cloud Messaging
