
USE biblioteca_escolar;

INSERT INTO categorias(nombre, descripcion) VALUES
('Novela', 'Narrativa y ficción'),
('Ciencia', 'Divulgación y ciencias naturales'),
('Tecnología', 'Informática y desarrollo de software'),
('Historia', 'Historia universal y de Chile');

INSERT INTO libros(titulo, autor, editorial, isbn, stock, categoria_id) VALUES
('Cien años de soledad', 'Gabriel García Márquez', 'Sudamericana', '9780307474728', 4, 1),
('Cosmos', 'Carl Sagan', 'Planeta', '9788408053040', 3, 2),
('Clean Code', 'Robert C. Martin', 'Prentice Hall', '9780132350884', 2, 3),
('Historia de Chile', 'Sergio Villalobos', 'Universitaria', '9789561116887', 5, 4);

INSERT INTO estudiantes(rut, nombres, apellidos, email, curso) VALUES
('21.111.111-1', 'Ana', 'Pérez Soto', 'ana.perez@colegio.cl', '4A'),
('22.222.222-2', 'Diego', 'Muñoz Díaz', 'diego.munoz@colegio.cl', '3B');

-- Credenciales ficticias; se almacenan como SHA-256, no como texto plano.
INSERT INTO usuarios(username, password_hash, rol, estudiante_id) VALUES
('bibliotecario', SHA2('Biblioteca123!', 256), 'BIBLIOTECARIO', NULL),
('estudiante1', SHA2('Estudiante123!', 256), 'ESTUDIANTE', 1),
('estudiante2', SHA2('Estudiante234!', 256), 'ESTUDIANTE', 2);

-- Datos de ejemplo para que los reportes tengan resultados desde el primer inicio.
INSERT INTO prestamos(estudiante_id, libro_id, fecha_prestamo, fecha_vencimiento, fecha_devolucion, estado) VALUES
(1, 1, CURRENT_DATE - INTERVAL 30 DAY, CURRENT_DATE - INTERVAL 16 DAY, CURRENT_DATE - INTERVAL 18 DAY, 'DEVUELTO'),
(2, 1, CURRENT_DATE - INTERVAL 20 DAY, CURRENT_DATE - INTERVAL 6 DAY, CURRENT_DATE - INTERVAL 4 DAY, 'ATRASADO'),
(1, 2, CURRENT_DATE - INTERVAL 3 DAY, CURRENT_DATE + INTERVAL 11 DAY, NULL, 'PRESTADO');

-- El libro Cosmos queda con una copia menos debido al préstamo activo anterior.
UPDATE libros SET stock = stock - 1 WHERE id = 2;

INSERT INTO historial_prestamos(prestamo_id, accion, detalle) VALUES
(1, 'PRESTAMO', 'Préstamo de ejemplo registrado.'),
(1, 'DEVOLUCION', 'Devolución de ejemplo a tiempo.'),
(2, 'PRESTAMO', 'Préstamo de ejemplo registrado.'),
(2, 'DEVOLUCION', 'Devolución de ejemplo atrasada.'),
(3, 'PRESTAMO', 'Préstamo activo de ejemplo; stock descontado.');
