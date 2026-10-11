
CREATE DATABASE IF NOT EXISTS biblioteca_escolar
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_escolar;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS historial_prestamos;
DROP TABLE IF EXISTS prestamos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS libros;
DROP TABLE IF EXISTS estudiantes;
DROP TABLE IF EXISTS categorias;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(160) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    editorial VARCHAR(120) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    stock INT NOT NULL DEFAULT 0,
    categoria_id INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_libro_stock CHECK (stock >= 0),
    CONSTRAINT fk_libro_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE estudiantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(80) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    curso VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(40) NOT NULL UNIQUE,
    password_hash CHAR(64) NOT NULL,
    rol ENUM('BIBLIOTECARIO','ESTUDIANTE') NOT NULL,
    estudiante_id INT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_usuario_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id),
    CONSTRAINT chk_usuario_estudiante CHECK (
        (rol = 'BIBLIOTECARIO' AND estudiante_id IS NULL) OR
        (rol = 'ESTUDIANTE' AND estudiante_id IS NOT NULL)
    )
);

CREATE TABLE prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id INT NOT NULL,
    libro_id INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    fecha_devolucion DATE NULL,
    estado ENUM('PRESTADO','DEVUELTO','ATRASADO') NOT NULL DEFAULT 'PRESTADO',
    CONSTRAINT fk_prestamo_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id),
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (libro_id) REFERENCES libros(id),
    CONSTRAINT chk_fechas_prestamo CHECK (fecha_vencimiento >= fecha_prestamo)
);

CREATE TABLE historial_prestamos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prestamo_id INT NOT NULL,
    accion ENUM('PRESTAMO','DEVOLUCION') NOT NULL,
    detalle VARCHAR(255) NOT NULL,
    fecha_evento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_historial_prestamo FOREIGN KEY (prestamo_id) REFERENCES prestamos(id)
);

CREATE INDEX idx_prestamo_activo ON prestamos(fecha_devolucion, fecha_vencimiento);
CREATE INDEX idx_prestamo_estudiante ON prestamos(estudiante_id, fecha_prestamo);
CREATE INDEX idx_prestamo_libro ON prestamos(libro_id);
