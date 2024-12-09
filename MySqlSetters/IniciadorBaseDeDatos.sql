-- Crear una sucursal
INSERT INTO SUCURSAL (nombre, direccion, ciudad)
VALUES ('Sucursal Central', 'Amunátegui 851', 'La Serena');

-- Crear usuarios
INSERT INTO USUARIO (nombre, email, password, telefono, rol, sucursal_id)
VALUES 
    ('Cliente Ejemplo', 'cliente@example.com', 'password123', '555-1234', 'CLIENTE', NULL),
    ('Transportista Ejemplo', 'transportista@example.com', 'password123', '555-5678', 'TRANSPORTISTA', 1),
    ('Administrador Sucursal', 'sucursal@example.com', 'password123', '555-4321', 'SUCURSAL', 1),
    ('Empresa Ejemplo', 'empresa@example.com', 'password123', '555-8765', 'EMPRESA', NULL);

-- Crear un vehículo
INSERT INTO VEHICULO (patente, capacidad_volumen)
VALUES ('ABC-1234', 15.5);

-- Crear un transportista
INSERT INTO TRANSPORTISTA (usuario_id, licencia, disponible, sucursal_id, vehiculo_id)
VALUES (2, 'LIC123456', TRUE, 1, 1);

-- Crear un paquete
INSERT INTO PAQUETE (num_seguimiento, email_cliente, cliente_id, sucursal_origen_id, transportista_id, destinatario, dir_destino, dimensiones_peso, costo, estado, fecha_creacion, fecha_estimada, intentos_entrega, usuario_creador_id)
VALUES ('PKG123456', 'cliente@example.com', 1, 1, 1, 'Destinatario Ejemplo', 'Calle Falsa 123', '30x22x7,10kg', 100.50, 'PENDIENTE', NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY), 0, 3);
