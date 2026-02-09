-- Creamos la base de datos para el proyecto Clinica Gratuita
create database if not exists clinica_gratuita;

-- Usamos la BD
use clinica_gratuita;

-- Creamos las tablas según sus relaciones
create table if not exists pacientes(
	id_paciente int auto_increment primary key,
    nombre varchar(100) not null,
    edad int not null check (edad >= 0 and edad <= 120),
    sexo enum('Masculino','Femenino') not null,
    direccion varchar(100) not null,
    -- validamos que el email sea único
    email varchar(100) unique,
    telefono varchar(20),
    
    -- Ingresamos validaciones a los datos email y teléfono
    constraint validar_email_paciente check (email like '%_@__%.__%'),
    constraint formato_telefono_paciente check (telefono regexp '^[0-9]{10}$')
);

create table if not exists doctores(
	id_doctor int auto_increment primary key,
    nombre varchar(100) not null,
    email varchar(100) unique,
    telefono varchar(20) unique,
    especialidad varchar(60) not null,
    
    -- Ingresamos validaciones a los datos email y teléfono
    constraint validar_email_doctor check (email like '%_@__%.__%'),
    constraint formato_telefono_doctor check (telefono regexp '^[0-9]{10}$')
);

create table if not exists citas_medicas(
	id_cita int auto_increment primary key,
    id_paciente int not null,
    id_doctor int not null,
    fecha date not null,
    hora time not null,
    motivo_consulta text not null,
    estado enum('programada', 'en curso', 'completada', 'cancelada') not null default 'programada',
    
	foreign key (id_paciente) 
    references pacientes(id_paciente)
    on delete cascade on update cascade,
    
    foreign key (id_doctor)
    references doctores(id_doctor)
    on delete cascade on update cascade
);

create table if not exists tratamientos(
	id_tratamiento int auto_increment primary key,
    id_cita int not null,
    descripcion text not null,
    duracion int not null,
    
    foreign key (id_cita)
    references citas_medicas(id_cita)
    on delete cascade on update cascade
);

create table if not exists medicamentos(
	id_medicamento int auto_increment primary key,
    nombre varchar(60) not null
);

create table if not exists tratamientos_medicamentos(
    id_tratamiento int not null,
    id_medicamento int not null,
    indicaciones VARCHAR(255) NOT NULL,
    primary key (id_tratamiento, id_medicamento),
    
    foreign key (id_tratamiento)
    references tratamientos(id_tratamiento)
    on delete cascade on update cascade,
    
    foreign key (id_medicamento)
    references medicamentos(id_medicamento)
    on delete cascade on update cascade
);

/*
	Vamos a crear algunas vistas que se utilizarán dentro de Java
*/
-- Vista para el Dashboard Principal
create view vista_agenda_diaria as
select
	c.id_cita,
    c.hora,
    p.nombre as paciente,
    p.edad,
    c.motivo_consulta,
    c.estado
from citas_medicas c
join pacientes p on c.id_paciente = p.id_paciente
where c.estado != 'cancelada'
order by c.hora asc;

-- Vista para la Pantalla de Receta
CREATE VIEW vista_receta_completa AS
SELECT 
    t.id_cita,
    p.nombre AS paciente,
    d.nombre AS doctor,
    m.nombre AS medicamento,
    tm.indicaciones,
    t.duracion AS dias_tratamiento
FROM tratamientos t
JOIN citas_medicas c ON t.id_cita = c.id_cita
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN doctores d ON c.id_doctor = d.id_doctor
JOIN tratamientos_medicamentos tm ON t.id_tratamiento = tm.id_tratamiento
JOIN medicamentos m ON tm.id_medicamento = m.id_medicamento;

DELIMITER //

CREATE PROCEDURE sp_finalizar_consulta(
    IN _id_cita INT,
    IN _descripcion TEXT,
    IN _duracion INT
)
BEGIN
    -- Insertamos el tratamiento
    INSERT INTO tratamientos (id_cita, descripcion, duracion) 
    VALUES (_id_cita, _descripcion, _duracion);
    
    -- Actualizamos el estado de la cita automáticamente
    UPDATE citas_medicas 
    SET estado = 'completada' 
    WHERE id_cita = _id_cita;
END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER tr_validar_fecha_cita
BEFORE INSERT ON citas_medicas
FOR EACH ROW
BEGIN
    IF NEW.fecha < CURDATE() THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: No se puede agendar una cita en una fecha pasada.';
    END IF;
END //

DELIMITER ;

DELIMITER //

CREATE FUNCTION fn_total_consultas_paciente(_id_paciente INT) 
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total FROM citas_medicas 
    WHERE id_paciente = _id_paciente AND estado = 'completada';
    RETURN total;
END //

DELIMITER ;

-- 1. Insertar datos maestros
INSERT INTO pacientes (nombre, edad, sexo, direccion, email, telefono) 
VALUES ('Juan Pérez', 30, 'Masculino', 'Av. Siempre Viva 123', 'juan@mail.com', '1234567890');

INSERT INTO doctores (nombre, email, telefono, especialidad) 
VALUES ('Dra. García', 'garcia@clinica.com', '0987654321', 'Pediatría');

INSERT INTO medicamentos (nombre) 
VALUES ('Ibuprofeno 400mg'), ('Amoxicilina 500mg');

-- 2. Agendar una cita (Prueba del Trigger)
-- Tip: Si pones una fecha pasada, verás el error que configuraste.
INSERT INTO citas_medicas (id_paciente, id_doctor, fecha, hora, motivo_consulta) 
VALUES (1, 1, CURDATE(), '10:00:00', 'Dolor de garganta y fiebre');

-- 3. Ver el Dashboard (Prueba de Vista 1)
SELECT * FROM vista_agenda_diaria;

-- 4. Finalizar Consulta (Prueba del SP)
-- Esto creará el tratamiento y cambiará el estado de la cita a 'completada'
CALL sp_finalizar_consulta(1, 'Infección leve de garganta. Reposo por 3 días.', 3);

-- 5. Agregar medicamentos a la receta (Tabla N:M)
INSERT INTO tratamientos_medicamentos (id_tratamiento, id_medicamento, indicaciones) 
VALUES (1, 2, 'Tomar 1 cápsula cada 8 horas');

-- 6. Ver la Receta Final (Prueba de Vista 2)
SELECT * FROM vista_receta_completa;