-- Crear sucursales
INSERT INTO SUCURSAL (nombre, direccion, ciudad)
VALUES 
    ('Sucursal Central', 'Amunátegui 851', 'La Serena'),
    ('Sucursal Norte', 'Av. Chile 1234', 'Antofagasta'),
    ('Sucursal Sur', 'Av. Argentina 5678', 'Concepción');

-- Crear usuarios
INSERT INTO USUARIO (nombre, email, password, telefono, rol, sucursal_id)
VALUES 
    ('Cliente Ejemplo', 'cliente@example.com', 'password123', '555-1234', 'CLIENTE', NULL),
    ('Transportista Ejemplo', 'transportista@example.com', 'password123', '555-5678', 'TRANSPORTISTA', 1),
    ('Administrador Sucursal', 'sucursal@example.com', 'password123', '555-4321', 'SUCURSAL', 1),
    ('Empresa Ejemplo', 'empresa@example.com', 'password123', '555-8765', 'EMPRESA', NULL),
    ('Cliente 2', 'cliente2@example.com', 'password456', '555-2345', 'CLIENTE', NULL),
    ('Transportista 2', 'transportista2@example.com', 'password456', '555-6789', 'TRANSPORTISTA', 2);

-- Crear vehículos
INSERT INTO VEHICULO (patente, capacidad_volumen)
VALUES 
    ('ABC-1234', 0),
    ('DEF-5678', 0),
    ('GHI-9101', 0);

-- Crear transportistas
INSERT INTO TRANSPORTISTA (usuario_id, licencia, disponible, sucursal_id, vehiculo_id)
VALUES 
    (2, 'LIC123456', TRUE, 1, 1),
    (6, 'LIC789101', FALSE, 2, 2);

-- Crear paquetes
INSERT INTO PAQUETE (num_seguimiento, email_cliente, cliente_id, sucursal_origen_id, transportista_id, destinatario, dir_destino, dimensiones_peso, costo, estado, fecha_creacion, fecha_estimada, intentos_entrega, usuario_creador_id)
VALUES 
    ('PKG123456', 'cliente@example.com', 1, 1, 1, 'Destinatario Ejemplo', 'Calle Falsa 123', '30x22x7,10kg', 100.50, 'PENDIENTE', NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY), 0, 3),
    ('PKG123457', 'cliente2@example.com', 5, 2, 2, 'Otro Destinatario', 'Av. Siempre Viva 742', '50x30x15,20kg', 150.75, 'EN_TRANSITO', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY), 1, 3);

-- Crear transacciones financieras
INSERT INTO TRANSACCION_FINANCIERA (sucursal_id, tipo, monto, concepto, fecha, referencia, paquete_id, usuario_id)
VALUES 
    (1, 'INGRESO', 100.50, 'Pago de envío PKG123456', NOW(), 'REF123456', 1, 3),
    (2, 'INGRESO', 150.75, 'Pago de envío PKG123457', NOW(), 'REF123457', 2, 3);

-- Crear logs sucursal
INSERT INTO LOG_SUCURSAL (sucursal_id, tipo_evento, descripcion, fecha, metadata, usuario_id)
VALUES 
    (1, 'CREACION', 'Sucursal creada con éxito', NOW(), JSON_OBJECT('init', TRUE), 3),
    (2, 'ACTUALIZACION', 'Cambio en dirección de sucursal', NOW(), JSON_OBJECT('old_address', 'Av. Chile 1234', 'new_address', 'Av. Perú 4321'), 3);

-- Crear logs transportista
INSERT INTO LOG_TRANSPORTISTA (transportista_id, tipo_evento, paquete_id, descripcion, fecha, metadata)
VALUES 
    (1, 'ASIGNACION', 1, 'Asignado al paquete PKG123456', NOW(), JSON_OBJECT('route', 'Central-La Serena')),
    (2, 'FINALIZACION', 2, 'Entrega finalizada', NOW(), JSON_OBJECT('delivery_time', '2023-12-15T14:00:00'));

-- Crear logs paquete
INSERT INTO LOG_PAQUETE (paquete_id, estado_anterior, estado_nuevo, descripcion, fecha, metadata, usuario_id)
VALUES 
    (1, 'PENDIENTE', 'EN_TRANSITO', 'Paquete recogido por transportista', NOW(), JSON_OBJECT('pickup_time', '2023-12-10T09:00:00'), 3),
    (2, 'PENDIENTE', 'CANCELADO', 'Envío cancelado por el cliente', NOW(), JSON_OBJECT('reason', 'Cambio de dirección'), 5);
