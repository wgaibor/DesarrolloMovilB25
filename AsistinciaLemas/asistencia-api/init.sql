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

-- Usuarios de demo
-- Contraseñas: docente123, estudiante123, representante123
-- BCrypt hashes generados con cost factor 10
INSERT INTO usuarios (cedula, nombre, email, password, rol, usr_creacion) VALUES
('1234567890', 'Prof. María García', 'maria.garcia@lemas.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjqQBrkHXGw8pPJM1RLMkdXNiL7oKO', 'DOCENTE', 'SYSTEM'),
('0987654321', 'Carlos Pérez Mora', 'carlos.perez@lemas.edu', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ESTUDIANTE', 'SYSTEM'),
('0987654322', 'Ana López Torres', 'ana.lopez@lemas.edu', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ESTUDIANTE', 'SYSTEM'),
('1122334455', 'Pedro Pérez', 'pedro.perez@gmail.com', '$2a$10$kGnP0.gPz7K0Ub1kYL3YqOQhSMJQe9C0W8FsVPf1CmBjNq3DnJUyu', 'REPRESENTANTE', 'SYSTEM'),
('1122334456', 'Laura Torres', 'laura.torres@gmail.com', '$2a$10$kGnP0.gPz7K0Ub1kYL3YqOQhSMJQe9C0W8FsVPf1CmBjNq3DnJUyu', 'REPRESENTANTE', 'SYSTEM');

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
