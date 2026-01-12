USE bq8w1cisdiqk6fgpyktc;

-- TABLA USUARIOS

CREATE TABLE usuarios (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    estado ENUM('Activo','Inactivo') DEFAULT 'Activo',
    rol ENUM('Administrador','Analista') DEFAULT 'Analista'
) ENGINE=InnoDB;

alter table usuarios 
add column cedula varchar(10);

-- INSERTAR USUARIOS

INSERT INTO usuarios (usuario, contraseña, nombre_completo, estado, rol) VALUES
('Emilia', 'admin1234', 'Administrador Principal', 'Activo','Administrador'),
('Ariel', 'admin2025', 'Administrador Secundario', 'Activo','Administrador'),
('Josue', 'analista123', 'Juan Pérez', 'Activo','Analista'),
('Anabel', 'clave456', 'María López','Activo','Analista'),
('usuario_inactivo', 'test123', 'Usuario Bloqueado', 'Inactivo','Analista');

-- TABLA SOLICITANTE

CREATE TABLE solicitante (
    id_solicitante INT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    tipos_licencia ENUM('A','B','E','A1','B1') NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- TABLA TRÁMITE

CREATE TABLE tramite (
    id_tramite INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitante INT NOT NULL,
    fecha_solicitud DATE NOT NULL,
    estado ENUM(
        'pendiente',
        'en_examenes',
        'aprobado',
        'reprobado',
        'licencia_emitida'
    ) DEFAULT 'pendiente',
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tramite_solicitante
        FOREIGN KEY (id_solicitante)
        REFERENCES solicitante(id_solicitante),

    CONSTRAINT fk_tramite_usuarios
        FOREIGN KEY (created_by)
        REFERENCES usuarios(ID)
) ENGINE=InnoDB;

-- TABLA REQUISITOS

CREATE TABLE requisitos (
    id_requisito INT AUTO_INCREMENT PRIMARY KEY,
    id_tramite INT NOT NULL,
    certificado_medico BOOLEAN DEFAULT FALSE,
    pago BOOLEAN DEFAULT FALSE,
    multas BOOLEAN DEFAULT FALSE,
    observaciones TEXT,
    aprobado BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_requisitos_tramite
        FOREIGN KEY (id_tramite)
        REFERENCES tramite(id_tramite)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- TABLA EXAMEN

CREATE TABLE examen (
    id_examen INT AUTO_INCREMENT PRIMARY KEY,
    id_tramite INT NOT NULL,
    nota_teorica DECIMAL(5,2) NOT NULL,
    nota_practica DECIMAL(5,2) NOT NULL,
    resultado ENUM('APROBADO','REPROBADO') NOT NULL,

    CONSTRAINT fk_examen_tramite
        FOREIGN KEY (id_tramite)
        REFERENCES tramite(id_tramite)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- TABLA LICENCIA

CREATE TABLE licencia (
    id_licencia INT AUTO_INCREMENT PRIMARY KEY,
    id_tramite INT NOT NULL,
    numero_licencia VARCHAR(20) NOT NULL UNIQUE,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    created_by INT,

    CONSTRAINT fk_licencia_tramite
        FOREIGN KEY (id_tramite)
        REFERENCES tramite(id_tramite),

    CONSTRAINT fk_licencia_usuario
        FOREIGN KEY (created_by)
        REFERENCES usuarios(ID)
) ENGINE=InnoDB;

-- TABLA AUDITORIA

CREATE TABLE auditoria (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    tabla_afectada VARCHAR(50) NOT NULL,
    id_registro INT NOT NULL,
    accion ENUM('INSERT','UPDATE','DELETE') NOT NULL,
    usuario INT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_auditoria_usuario
        FOREIGN KEY (usuario)
        REFERENCES usuarios(ID)
) ENGINE=InnoDB;

-- VERIFICACIONES
SHOW TABLES;
SELECT * FROM usuarios;


