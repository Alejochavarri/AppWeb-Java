create database if not exists bdbancos;
use bdbancos;
create table if not exists tipo_usuario(
	id int not null auto_increment,
	nombre varchar(30) not null,
    deleted boolean not null default false,
    primary key(id),
    unique key(nombre)
);

create table if not exists tipo_cuenta(
	id int not null auto_increment,
    nombre varchar(30) not null,
    deleted boolean not null default false,
    primary key(id),
    unique key(nombre)
);

create table if not exists links(
	id int not null auto_increment,
	nombre_recurso VARCHAR(100) NOT NULL,
	id_tipo_usuario int not null,
    redirige VARCHAR(100) NOT NULL,
	deleted boolean NOT NULL,
    primary key(id),
    foreign key (id_tipo_usuario) references tipo_usuario(id),
    UNIQUE KEY (nombre_recurso, id_tipo_usuario)
);

create table if not exists usuario(
id INT NOT NULL AUTO_INCREMENT,
usuario VARCHAR(45) NOT NULL,
contrasena VARCHAR(100) NOT NULL,
id_tipo_usuario INT NOT NULL,
deleted boolean NOT NULL,
PRIMARY KEY (id),
foreign key (id_tipo_usuario) references tipo_usuario(id),
unique key(usuario)
);

create table if not exists pais(
	id int not null auto_increment,
	deleted boolean not null default false,
    nombre varchar(30) not null,
    primary key (id),
    unique(nombre)
);

create table if not exists provincia(
	id int not null auto_increment,
    id_pais int not null,
	nombre varchar(30) not null,
    deleted boolean not null default false,
    primary key (id),
    foreign key (id_pais) references pais(id),
    unique key (id_pais, nombre)
);

create table if not exists localidad(
	id int not null auto_increment,
    id_provincia int not null,
    nombre varchar(30) not null,
    deleted boolean not null default false,
	primary key (id),
	foreign key (id_provincia) references provincia(id),
    unique key(id_provincia, nombre)
);

create table if not exists cliente(
id INT NOT NULL AUTO_INCREMENT,
id_usuario INT NOT NULL,
dni INT NOT NULL,
cuil BIGINT NOT NULL,
nombre VARCHAR(45) NOT NULL,
apellido VARCHAR(45) NOT NULL,
sexo ENUM('MASCULINO', 'FEMENINO', 'OTRO') NOT NULL,
id_pais INT NOT NULL,
fecha_nacimiento DATE NOT NULL,
direccion VARCHAR(100) NOT NULL,
id_localidad INT NOT NULL,
id_provincia INT NOT NULL,
correo_electronico VARCHAR(45) NOT NULL,
telefono VARCHAR(30) NOT NULL,
deleted boolean NOT NULL,
UNIQUE KEY (DNI),
UNIQUE KEY (CUIL),
UNIQUE KEY (correo_electronico),
UNIQUE KEY (telefono),
PRIMARY KEY (id),
FOREIGN KEY (id_usuario) REFERENCES usuario (id),
FOREIGN KEY (id_localidad) REFERENCES localidad(id),
FOREIGN KEY (id_provincia) REFERENCES provincia(id),
FOREIGN KEY (id_pais) REFERENCES pais(id)
);

create table if not exists cuenta(
id INT NOT NULL AUTO_INCREMENT,
id_tipo_cuenta int NOT NULL,
id_cliente INT NOT NULL,
fecha_creacion DATE NOT NULL,
cbu BIGINT NOT NULL,
saldo DECIMAL(10, 2) NOT NULL,
deleted boolean NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id_cliente) REFERENCES cliente(id),
FOREIGN KEY (id_tipo_cuenta) REFERENCES tipo_cuenta (id),
UNIQUE KEY (cbu)
);

CREATE TABLE if not exists movimiento (
id INT AUTO_INCREMENT,
idcuenta_origen INT NULL,
idcuenta_destino INT NULL,
fecha DATE NOT NULL,
detalle VARCHAR(100),
importe DECIMAL(10, 2) NOT NULL,
tipo_movimiento ENUM('ALTA_DE_CUENTA', 'ALTA_DE_PRESTAMO', 'PAGO_DE_PRESTAMO', 'TRANSFERENCIA') NOT NULL,
FOREIGN KEY (idcuenta_origen) REFERENCES cuenta(id),
FOREIGN KEY (idcuenta_destino) REFERENCES cuenta(id),
PRIMARY KEY (id)
);

CREATE TABLE if not exists prestamo(
id INT NOT NULL AUTO_INCREMENT,
id_cliente INT NOT NULL,
fecha_contratacion DATE NOT NULL,
importe_con_intereses DECIMAL(10, 2) NOT NULL,
importe_pedido DECIMAL(10, 2) NOT NULL,
plazo_pago_mes INT NOT NULL,
monto_por_mes DECIMAL(10, 2) NOT NULL,
cuotas INT NOT NULL,
id_cuenta INT NOT NULL,
estado ENUM ('APROBADO', 'DESAPROBADO', 'BAJO_REVISION') NOT NULL,
interes_anual DECIMAL NOT NULL DEFAULT 2,
deleted boolean NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id_cliente) REFERENCES cliente(id),
FOREIGN KEY (id_cuenta) REFERENCES cuenta(id)
);

CREATE TABLE IF NOT EXISTS cuota_prestamo(
    id INT NOT NULL AUTO_INCREMENT,
    id_prestamo INT NOT NULL,
    importe_cuota DECIMAL(10,2) NOT NULL,
    cuota INT NOT NULL,
    estado ENUM ('PAGADA', 'PENDIENTE', 'IMPAGA') NOT NULL DEFAULT 'PENDIENTE',
    deleted boolean not null default false,
    PRIMARY KEY(id),
    FOREIGN KEY(id_prestamo) REFERENCES prestamo(id),
    UNIQUE KEY(id_prestamo, cuota)
);

Alter table bdbancos.movimiento modify column idcuenta_destino int null;

INSERT INTO prestamo (id_cliente,
DELIMITER //
CREATE TRIGGER after_prestamo_update 
AFTER UPDATE ON prestamo 
FOR EACH ROW
BEGIN
    DECLARE i INT DEFAULT 1;

    IF NEW.estado = 'APROBADO' AND OLD.estado = 'BAJO_REVISION' THEN
        WHILE i <= NEW.plazo_pago_mes DO
            INSERT INTO cuota_prestamo (id_prestamo, importe_cuota, cuota, estado, deleted)
            VALUES (NEW.id, NEW.monto_por_mes, i, 'PENDIENTE', false);
            SET i = i + 1;
        END WHILE;

        INSERT INTO movimiento (idcuenta_origen, idcuenta_destino, fecha, detalle, importe, tipo_movimiento)
        VALUES (NEW.id_cuenta, NEW.id_cuenta, CURDATE(), 'Alta de préstamo', NEW.importe_pedido, 'ALTA_DE_PRESTAMO');
        
        UPDATE cuenta SET saldo = saldo + NEW.importe_pedido WHERE id = NEW.id_cuenta;
    END IF;
END //
DELIMITER ;
update bdbancos.links set redirige = 'ClienteDashboardServlet?load=1' WHERE id = 7;
ALTER TABLE cuenta ADD CONSTRAINT chk_saldo CHECK (saldo >= 0);
DELIMITER //
CREATE TRIGGER after_cuenta_create
AFTER INSERT ON cuenta 
FOR EACH ROW
BEGIN
        INSERT INTO movimiento (idcuenta_origen, idcuenta_destino, fecha, detalle, importe, tipo_movimiento)
        VALUES (null, NEW.id, CURDATE(), 'Alta de cuenta', 10000, 'ALTA_DE_CUENTA');
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_transferencia(IN cuenta_origen INT, IN cuenta_destino INT, IN cbu_origen BIGINT, IN cbu_destino BIGINT,
IN Detalle VARCHAR(100) ,IN monto DECIMAL(10,2))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'ERROR';
    END;

    START TRANSACTION;

    UPDATE cuenta SET saldo = saldo - monto WHERE cbu = cbu_origen;
    UPDATE cuenta SET saldo = saldo + monto WHERE cbu = cbu_destino;
    
	INSERT INTO movimiento (idcuenta_origen, idcuenta_destino, fecha, detalle, importe, tipo_movimiento) VALUES 
	(cuenta_origen,cuenta_destino , SYSDATE(), Detalle, monto, 'TRANSFERENCIA');
    
    SELECT 'OK';
    COMMIT;
    
    
   
END //

DELIMITER ;
DELIMITER //

CREATE PROCEDURE sp_crear_usuario (
    IN p_usuario VARCHAR(45),
    IN p_contrasena VARCHAR(100),
    IN p_id_tipo_usuario INT,
    IN p_dni INT,
    IN p_cuil BIGINT,
    IN p_nombre VARCHAR(45),
    IN p_apellido VARCHAR(45),
    IN p_sexo ENUM('MASCULINO', 'FEMENINO', 'OTRO'),
    IN p_id_pais INT,
    IN p_fecha_nacimiento DATE,
    IN p_direccion VARCHAR(100),
    IN p_id_localidad INT,
    IN p_id_provincia INT,
    IN p_correo_electronico VARCHAR(45),
    IN p_telefono VARCHAR(30),
    OUT p_resultado INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
		SET p_resultado = -1;
    END;

    START TRANSACTION;

    INSERT INTO usuario (usuario, contrasena, id_tipo_usuario, deleted)
    VALUES (p_usuario, p_contrasena, p_id_tipo_usuario, FALSE);

    SET @last_user_id = LAST_INSERT_ID();

    INSERT INTO cliente (id_usuario, dni, cuil, nombre, apellido, sexo, id_pais, fecha_nacimiento, direccion, id_localidad, id_provincia, correo_electronico, telefono, deleted)
    VALUES (@last_user_id, p_dni, p_cuil, p_nombre, p_apellido, p_sexo, p_id_pais, p_fecha_nacimiento, p_direccion, p_id_localidad, p_id_provincia, p_correo_electronico, p_telefono, FALSE);

    COMMIT;
	SET p_resultado = 1;
END //

DELIMITER ;
DELIMITER //

CREATE PROCEDURE sp_modificar_usuario (
    IN p_id_usuario INT,
    IN p_usuario VARCHAR(45),
    IN p_contrasena VARCHAR(100),
    IN p_id_tipo_usuario INT,
    IN p_id_cliente INT,
    IN p_dni INT,
    IN p_cuil BIGINT,
    IN p_nombre VARCHAR(45),
    IN p_apellido VARCHAR(45),
    IN p_sexo ENUM('MASCULINO', 'FEMENINO', 'OTRO'),
    IN p_id_pais INT,
    IN p_fecha_nacimiento DATE,
    IN p_direccion VARCHAR(100),
    IN p_id_localidad INT,
    IN p_id_provincia INT,
    IN p_correo_electronico VARCHAR(45),
    IN p_telefono VARCHAR(30),
    OUT p_resultado INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
		SET p_resultado = -1;
    END;

    -- Iniciar la transacción
    START TRANSACTION;
    UPDATE usuario
    SET usuario = p_usuario,
        contrasena = p_contrasena,
        id_tipo_usuario = p_id_tipo_usuario
    WHERE id = p_id_usuario;
    IF ROW_COUNT() = 0 THEN
        ROLLBACK;
		SET p_resultado = -1;
    END IF;

    UPDATE cliente
    SET dni = p_dni,
        cuil = p_cuil,
        nombre = p_nombre,
        apellido = p_apellido,
        sexo = p_sexo,
        id_pais = p_id_pais,
        fecha_nacimiento = p_fecha_nacimiento,
        direccion = p_direccion,
        id_localidad = p_id_localidad,
        id_provincia = p_id_provincia,
        correo_electronico = p_correo_electronico,
        telefono = p_telefono
    WHERE id = p_id_cliente;

    IF ROW_COUNT() = 0 THEN
        ROLLBACK;
		SET p_resultado = -1;
    END IF;

    COMMIT;
    SET p_resultado = 1;
END //

DELIMITER ;

insert into tipo_usuario (nombre) values ('ADMINISTRADOR'), ('CLIENTE');
insert into tipo_cuenta (nombre) values ('CAJA_DE_AHORRO'), ('CUENTA CORRIENTE');
insert into links (nombre_recurso, id_tipo_usuario, redirige, deleted) values
	('Mis cuentas', 2, 'CuentaServlet?load=1', FALSE),
    ('Mis transferencias', 2, 'MisTransferencias.jsp', FALSE),
	('Mi historial', 2, 'HistorialServlet?load=1' ,FALSE),
    ('Solicitar prestamo', 2, 'PedirPrestamoServlet?load=1' , FALSE),
    ('Gestionar prestamo', 2, 'GestionPrestamos?load=1' , FALSE),
    ('Mi perfil', 2, 'MiPerfil.jsp' , FALSE),
    ('Dashboard', 2, 'ClienteDashboard.jsp' , FALSE),
    ('Gestion de cuentas', 1, 'GestionCuentas.jsp' , FALSE),
    ('Gestion de clientes', 1, 'GestionClientes?load=1' , FALSE),
    ('Autorizacion de prestamos', 1, 'AutorizarPrestamoServlet?load=1' , FALSE),
    ('Reportes', 1, 'Reportes.jsp' ,FALSE),
    ('Dashboard', 1, 'AdminDashboardServlet?load=1' , FALSE);
insert into pais (nombre) values
('Argentina'),
('Chile'),
('Uruguay'),
('Brasil'),
('Bolivia'),
('Paraguay'),
('Colombia'),
('Venezuela'),
('Ecuador'),
('Mexico'),
('Estados Unidos'),
('Guatemala'),
('Puerto Rico'),
('Cuba'),
('Jamaica');
insert into provincia (id_pais, nombre) values
(1, 'Buenos Aires'),
(1, 'Santa fe'),
(1, 'Entre rios'),
(1, 'Cordoba'),
(1, 'Misiones'),
(1, 'Chubut'),
(1, 'Rio Negro'),
(1, 'Santa Cruz'),
(1, 'Tierra del fuego'),
(1, 'Salta'),
(1, 'Jujuy'),
(1, 'La Pampa'),
(1, 'Catamarca'),
(1, 'Tucuman'),
(1, 'Neuquen');
insert into localidad(id_provincia, nombre) values
(1, 'San Miguel'), 
(1, 'CABA'), 
(1, 'Olivos'), 
(1, 'Tres de febrero'),
(1, 'Jose c Paz'), 
(1, 'Muniz'), 
(1, 'Del Viso'), 
(1, 'Pilar'),
(1, 'Garin'), 
(1, 'Escobar'), 
(1, 'San Isidro'), 
(1, 'Vicente Lopez'),
(1, 'Moreno'), 
(1, 'Tigre'), 
(1, 'Beccar');
INSERT INTO usuario (usuario, contrasena, id_tipo_usuario, deleted) VALUES 
('user1', 'password1', 2, 0),
('admin1', 'adminpassword1', 1, 0),
('cliente1', 'cliente1', 2, 0),
('cliente2', 'cliente2', 2, 0),
('cliente3', 'cliente3', 2, 0),
('cliente4', 'cliente4', 2, 0),
('cliente5', 'cliente5', 2, 0),
('cliente6', 'cliente6', 2, 0),
('cliente7', 'cliente7', 2, 0),
('cliente8', 'cliente8', 2, 0),
('cliente9', 'cliente9', 2, 0),
('cliente10', 'cliente10', 2, 0),
('cliente11', 'cliente11', 2, 0),
('cliente12', 'cliente12', 2, 0),
('cliente13', 'cliente13', 2, 0);
INSERT INTO cliente (id_usuario, dni, cuil, nombre, apellido, sexo, id_pais, fecha_nacimiento, direccion, id_localidad, id_provincia, correo_electronico, telefono, deleted) VALUES 
(1, 12345678, 20123456789, 'Carlos', 'Luna', 'MASCULINO', 1, '1980-01-01', 'Calle 1', 1, 1, 'carlos.luna@mail.com', '123456789', 0),
(2, 23456789, 20234567890, 'Martina', 'Garcia', 'FEMENINO', 2, '1985-02-15', 'Avenida 2', 2, 1, 'martina.garcia@mail.com', '234567890', 0),
(3, 34567890, 20345678901, 'Luis', 'Fernandez', 'MASCULINO', 3, '1990-03-20', 'Boulevard 3', 3, 1, 'luis.fernandez@mail.com', '345678901', 0),
(4, 45678901, 20456789012, 'Ana', 'Martinez', 'FEMENINO', 4, '1992-04-25', 'Calle 4', 4, 1, 'ana.martinez@mail.com', '456789012', 0),
(5, 56789012, 20567890123, 'Juan', 'Perez', 'MASCULINO', 5, '1985-05-30', 'Avenida 5', 5, 1, 'juan.perez@mail.com', '567890123', 0),
(6, 67890123, 20678901234, 'Sofia', 'Gomez', 'FEMENINO', 6, '1993-06-05', 'Boulevard 6', 6, 1, 'sofia.gomez@mail.com', '678901234', 0),
(7, 78901234, 20789012345, 'Miguel', 'Rodriguez', 'MASCULINO', 7, '1984-07-10', 'Calle 7', 7, 1, 'miguel.rodriguez@mail.com', '789012345', 0),
(8, 89012345, 20890123456, 'Valentina', 'Lopez', 'FEMENINO', 8, '1994-08-15', 'Avenida 8', 8, 1, 'valentina.lopez@mail.com', '890123456', 0),
(9, 90123456, 20901234567, 'Javier', 'Sanchez', 'MASCULINO', 9, '1987-09-20', 'Boulevard 9', 9, 1, 'javier.sanchez@mail.com', '901234567', 0),
(10, 12345679, 20123456780, 'Laura', 'Diaz', 'FEMENINO', 10, '1991-10-25', 'Calle 10', 10, 1, 'laura.diaz@mail.com', '123456790', 0),
(11, 23456780, 20234567891, 'Lucas', 'Torres', 'MASCULINO', 11, '1983-11-30', 'Avenida 11', 11, 1, 'lucas.torres@mail.com', '234567891', 0),
(12, 34567891, 20345678902, 'Camila', 'Romero', 'FEMENINO', 12, '1989-12-05', 'Boulevard 12', 12, 1, 'camila.romero@mail.com', '345678902', 0),
(13, 45678902, 20456789013, 'Sebastian', 'Alvarez', 'MASCULINO', 13, '1986-01-10', 'Calle 13', 13, 1, 'sebastian.alvarez@mail.com', '456789013', 0),
(14, 56789013, 20567890124, 'Lucia', 'Moreno', 'FEMENINO', 14, '1992-02-15', 'Avenida 14', 14, 1, 'lucia.moreno@mail.com', '567890124', 0),
(15, 67890124, 20678901235, 'Matias', 'Gutierrez', 'MASCULINO', 15, '1988-03-20', 'Boulevard 15', 15, 1, 'matias.gutierrez@mail.com', '678901235', 0);
INSERT INTO cuenta (id_tipo_cuenta, id_cliente, fecha_creacion, cbu, saldo, deleted) VALUES 
(1, 1, '2020-01-01', 123456789012345678, 1000.50, 0),
(2, 1, '2021-02-01', 234567890123456789, 2500.75, 0),
(1, 2, '2020-03-01', 345678901234567890, 3000.00, 0),
(1, 2, '2019-04-01', 456789012345678901, 1500.20, 0),
(2, 2, '2018-05-01', 567890123456789012, 4000.40, 0),
(1, 3, '2021-06-01', 678901234567890123, 3500.30, 0),
(1, 3, '2022-07-01', 789012345678901234, 5000.50, 0),
(2, 4, '2019-08-01', 890123456789012345, 4500.75, 0),
(1, 4, '2020-09-01', 901234567890123456, 2000.60, 0),
(1, 5, '2021-10-01', 123456780123456789, 5500.80, 0),
(2, 5, '2022-11-01', 234567890123456788, 6000.90, 0),
(1, 6, '2020-12-01', 345678901234567889, 7000.00, 0),
(1, 6, '2019-01-01', 456789012345678800, 8000.10, 0),
(2, 7, '2018-02-01', 567890123456789011, 9000.20, 0),
(1, 7, '2021-03-01', 678901234567890122, 10000.30, 0);
INSERT INTO prestamo (id_cliente, fecha_contratacion, importe_con_intereses, importe_pedido, plazo_pago_mes, monto_por_mes, cuotas, id_cuenta, estado, interes_anual, deleted) VALUES 
(1, '2023-01-01', 1200.00, 1000.00, 12, 100.00, 12, 1, 'BAJO_REVISION', 2.0, 0),
(2, '2023-02-01', 2400.00, 2000.00, 24, 100.00, 24, 4, 'BAJO_REVISION', 2.0, 0),
(3, '2023-03-01', 3600.00, 3000.00, 36, 100.00, 36, 6, 'BAJO_REVISION', 2.0, 0),
(4, '2023-04-01', 4800.00, 4000.00, 48, 100.00, 48, 8, 'BAJO_REVISION', 2.0, 0),
(5, '2023-05-01', 6000.00, 5000.00, 60, 100.00, 60, 10, 'BAJO_REVISION', 2.0, 0),
(6, '2023-06-01', 7200.00, 6000.00, 72, 100.00, 72, 12, 'BAJO_REVISION', 2.0, 0),
(7, '2023-07-01', 8400.00, 7000.00, 84, 100.00, 84, 14, 'BAJO_REVISION', 2.0, 0),
(8, '2023-08-01', 9600.00, 8000.00, 96, 100.00, 96, 1, 'BAJO_REVISION', 2.0, 0),
(9, '2023-09-01', 10800.00, 9000.00, 108, 100.00, 108, 2, 'BAJO_REVISION', 2.0, 0),
(10, '2023-10-01', 12000.00, 10000.00, 120, 100.00, 120, 3, 'BAJO_REVISION', 2.0, 0),
(11, '2023-11-01', 13200.00, 11000.00, 132, 100.00, 132, 4, 'BAJO_REVISION', 2.0, 0),
(12, '2023-12-01', 14400.00, 12000.00, 144, 100.00, 144, 5, 'BAJO_REVISION', 2.0, 0),
(13, '2024-01-01', 15600.00, 13000.00, 156, 100.00, 156, 5, 'BAJO_REVISION', 2.0, 0),
(14, '2024-02-01', 16800.00, 14000.00, 168, 100.00, 168, 8, 'BAJO_REVISION', 2.0, 0),
(15, '2024-03-01', 18000.00, 15000.00, 180, 100.00, 180, 10, 'BAJO_REVISION', 2.0, 0);








