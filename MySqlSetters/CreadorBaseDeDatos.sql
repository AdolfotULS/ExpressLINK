-- Crear primero las tablas referenciadas
CREATE TABLE SUCURSAL (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    direccion VARCHAR(255),
    ciudad VARCHAR(100)
);

CREATE TABLE USUARIO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    telefono VARCHAR(20),
    rol ENUM('CLIENTE', 'TRANSPORTISTA', 'SUCURSAL', 'EMPRESA'),
    sucursal_id INT,
    FOREIGN KEY (sucursal_id) REFERENCES SUCURSAL(id)
);

CREATE TABLE VEHICULO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patente VARCHAR(20) UNIQUE,
    capacidad_volumen DOUBLE
);

CREATE TABLE TRANSPORTISTA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    licencia VARCHAR(50),
    disponible BOOLEAN,
    sucursal_id INT,
    vehiculo_id INT,
    FOREIGN KEY (usuario_id) REFERENCES USUARIO(id),
    FOREIGN KEY (sucursal_id) REFERENCES SUCURSAL(id),
    FOREIGN KEY (vehiculo_id) REFERENCES VEHICULO(id)
);

CREATE TABLE PAQUETE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    num_seguimiento VARCHAR(50) UNIQUE,
    email_cliente VARCHAR(255),
    cliente_id INT,
    sucursal_origen_id INT,
    transportista_id INT,
    destinatario VARCHAR(255),
    dir_destino VARCHAR(255),
    dimensiones_peso VARCHAR(255),
    costo DECIMAL(10, 2),
    estado ENUM('PENDIENTE', 'EN_TRANSITO', 'ENTREGADO', 'CANCELADO'),
    fecha_creacion DATETIME,
    fecha_estimada DATETIME,
    intentos_entrega INT,
    usuario_creador_id INT,
    fecha_actualizacion DATETIME,
    FOREIGN KEY (cliente_id) REFERENCES USUARIO(id),
    FOREIGN KEY (sucursal_origen_id) REFERENCES SUCURSAL(id),
    FOREIGN KEY (transportista_id) REFERENCES TRANSPORTISTA(id),
    FOREIGN KEY (usuario_creador_id) REFERENCES USUARIO(id)
);

CREATE TABLE TRANSACCION_FINANCIERA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sucursal_id INT,
    tipo ENUM('INGRESO', 'GASTO'),
    monto DECIMAL(10, 2),
    concepto VARCHAR(255),
    fecha DATETIME,
    referencia VARCHAR(255),
    paquete_id INT,
    usuario_id INT,
    FOREIGN KEY (sucursal_id) REFERENCES SUCURSAL(id),
    FOREIGN KEY (paquete_id) REFERENCES PAQUETE(id),
    FOREIGN KEY (usuario_id) REFERENCES USUARIO(id)
);

CREATE TABLE BALANCE_SUCURSAL (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sucursal_id INT,
    balance_actual DECIMAL(10, 2),
    fecha_actualizacion DATETIME,
    ingresos_periodo DECIMAL(10, 2),
    gastos_periodo DECIMAL(10, 2),
    FOREIGN KEY (sucursal_id) REFERENCES SUCURSAL(id)
);

CREATE TABLE LOG_SUCURSAL (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sucursal_id INT,
    tipo_evento ENUM('CREACION', 'ACTUALIZACION', 'ELIMINACION'),
    descripcion VARCHAR(255),
    fecha DATETIME,
    metadata JSON,
    usuario_id INT,
    FOREIGN KEY (sucursal_id) REFERENCES SUCURSAL(id),
    FOREIGN KEY (usuario_id) REFERENCES USUARIO(id)
);

CREATE TABLE LOG_TRANSPORTISTA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transportista_id INT,
    tipo_evento ENUM('ASIGNACION', 'ACTUALIZACION', 'FINALIZACION'),
    paquete_id INT,
    descripcion VARCHAR(255),
    fecha DATETIME,
    metadata JSON,
    FOREIGN KEY (transportista_id) REFERENCES TRANSPORTISTA(id),
    FOREIGN KEY (paquete_id) REFERENCES PAQUETE(id)
);

CREATE TABLE LOG_PAQUETE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paquete_id INT,
    estado_anterior ENUM('PENDIENTE', 'EN_TRANSITO', 'ENTREGADO', 'CANCELADO'),
    estado_nuevo ENUM('PENDIENTE', 'EN_TRANSITO', 'ENTREGADO', 'CANCELADO'),
    descripcion VARCHAR(255),
    fecha DATETIME,
    metadata JSON,
    usuario_id INT,
    FOREIGN KEY (paquete_id) REFERENCES PAQUETE(id),
    FOREIGN KEY (usuario_id) REFERENCES USUARIO(id)
);


ALTER TABLE log_transportista MODIFY metadata JSON NULL;
ALTER TABLE log_paquete MODIFY metadata JSON NULL;
ALTER TABLE LOG_SUCURSAL MODIFY metadata JSON NULL;
