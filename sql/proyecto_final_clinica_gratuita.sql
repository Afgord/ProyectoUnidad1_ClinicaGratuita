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
    email varchar(100) unique not null,
    telefono varchar(10) not null,
    
    -- Ingresamos validaciones a los datos email y teléfono
    constraint validar_email_paciente check (email like '%_@__%.__%'),
    constraint formato_telefono_paciente check (telefono regexp '^[0-9]{10}$')
);

create table if not exists doctores(
	id_doctor int auto_increment primary key,
    nombre varchar(100) not null,
    email varchar(100) unique not null,
    telefono varchar(10) unique not null,
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
    indicaciones text NOT NULL,
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
create view vista_receta_completa AS
select 
    t.id_cita,
    p.nombre as paciente,
    d.nombre as doctor,
    m.nombre as medicamento,
    tm.indicaciones,
    t.duracion AS dias_tratamiento
from tratamientos t
join citas_medicas c on t.id_cita = c.id_cita
join pacientes p on c.id_paciente = p.id_paciente
join doctores d on c.id_doctor = d.id_doctor
join tratamientos_medicamentos tm on t.id_tratamiento = tm.id_tratamiento
JOIN medicamentos m ON tm.id_medicamento = m.id_medicamento;

-- Creamos una vista para seleccionar el doctor a la hora de programar cita
create view vista_seleccion_doctor as
select id_doctor, CONCAT(nombre, ' - ', especialidad) as inf_doctor
from doctores;

DELIMITER //

/**
	Ahora generemos los SP que se encargarán de interactuar con Java para
    agregar, actualizar o eliminar datos en nuestra BD
*/
-- Creamos el SP sp_registrar_pacientes, el cual se encargará de registrar nuevos pacientes
delimiter //
	create procedure sp_registrar_paciente(
		IN param_nombre varchar(100),
		IN param_edad int,
		IN param_sexo enum('Masculino','Femenino'),
		IN param_direccion varchar(100),
		IN param_email varchar(100),
		IN param_telefono varchar(10)
	)
	begin
		declare exit handler for sqlexception
        begin
			rollback;
            signal sqlstate '45000' set message_text = 'ERROR: El paciente ya está registrado.';
		end;
        
        start transaction;
			insert into pacientes (nombre, edad, sexo, direccion, email, telefono)
			values (param_nombre, param_edad, param_sexo, param_direccion, param_email, param_telefono);
		commit;
	end //
delimiter ;

-- Creamos el SP sp_registrar_doctor, el cual se encargará de agregar nuevos doctores
delimiter //
create procedure sp_registrar_doctor(
    IN param_nombre varchar(100),
    IN param_email varchar(100),
    IN param_telefono varchar(10),
    IN param_especialidad varchar(60)
)
begin
	declare exit handler for sqlexception
    begin
		rollback;
        signal sqlstate '45000' set message_text = 'ERROR: El doctor ya está registrado';
	end;
    
    start transaction;
		insert into doctores (nombre, email, telefono, especialidad)
		select (param_nombre, param_email, param_telefono, param_especialidad);
	commit;
end //
delimiter ;

-- Creamos el SP sp_agendar_cita, el cual nos permitirá agendar una nueva
-- cita médica a un paciente
DELIMITER //
create procedure sp_agendar_cita(
    in param_id_paciente int,
    in param_id_doctor int,
    in param_fecha date,
    in param_hora time,
    in param_motivo text
)
begin
	-- Declaramos una variable tempral cita_existente de tipo int para validar
    -- fechas en que un doctor ya tenga cita programada. 
    declare cita_existente int;
    
    -- declaramos un exit handler para excepciones de sql
	declare exit handler for sqlexception
        begin
			rollback;
            signal sqlstate '45000' set message_text = 'ERROR, no se pudo agendar la cita.';
		end;
        
        -- comenzamos la transacción de agendar una nueva cita
        start transaction;
        
        -- realizamos la consulta para validar si una cita existe. en este caso revisamos en la
        -- tabla citas_medicas el id del doctor, la fecha, hora y estado de una cita.
        select count(*) into cita_existente
        from citas_medicas
        where id_doctor = param_id_doctor
        and fecha = param_fecha
        and hora = param_hora
        and estado != 'cancelada';
        
        -- si la comparación no nos regresa 0 quiere decir que está ocupada esa fecha.
        if cita_existente > 0 then
			signal sqlstate '45000' set message_text = 'Cita existente en esa fecha.  Favor de seleccionar otra.';
		else
			-- de estar disponible esa fecha se procede a capturar la nueva cita.
			insert into citas_medicas (id_paciente, id_doctor, fecha, hora, motivo_consulta)
			values (param_id_paciente, param_id_doctor, param_fecha, param_hora, param_motivo);
			commit;
		end if;
END //
DELIMITER ;

-- Creamos el SP sp_finalizar_consulta el cual nos permitirá guardar los cambios
-- una vez finalice la consulta de un paciente
delimiter //
create procedure sp_finalizar_consulta(
    in param_id_cita int,
    in param_descripcion text,
    in param_duracion int
)
begin
	declare exit handler for sqlexception
    begin
		rollback;
        signal sqlstate '45000' set message_text = 'Error al finalizar la consulta.';
	end;
    
    start transaction;
    -- Insertamos los datos del tratamiento
		insert into tratamientos (id_cita, descripcion, duracion) 
		values (param_id_cita, param_descripcion, param_duracion);
    
		-- Actualizamos el estado de la cita automáticamente
		update citas_medicas 
		set estado = 'completada' 
		where id_cita = param_id_cita;
        
        select last_insert_id() as id_tratamiento;
	commit;
end //

create procedure sp_tratamiento_medicamento(
	in param_id_tratamiento int,
    in param_id_medicamento int,
    in param_indicaciones text
)

begin
	declare exit handler for sqlexception
    begin
		signal sqlstate '45000' set message_text = 'ERROR: El medicamento ya se encuentra en la receta o los datos son inválidos.';
	end;
    
    insert into tratamientos_medicamentos(id_tratamiento, id_medicamento, indicaciones)
    select (param_id_tratamiento, param_id_medicamento, param_indicaciones);
end //
delimiter ;

/**
* Ahora procedemos a crear algunos triggers para monitorear la integridad de
* los datos y las operaciones que se realizarán en nuestra BD
*/

delimiter //

create trigger tr_validar_fecha_cita
before insert on citas_medicas
for each row
begin
    if new.fecha < CURDATE() then
        signal sqlstate '45000' 
        set message_text = 'Error: No se puede agendar una cita en una fecha pasada.';
    end if;
    
    if not (
		(new.hora >= '07:00:00' and new.hora <= '13:00:00') OR
        (new.hora >= '15:00:00' and new.hora <= '19:00:00')
    ) then
		signal sqlstate '45000' set message_text = 'Solo se atiende de 7 AM - 1 PM y 3 PM - 7 PM.';
	end if;
    
    if weekday(new.fecha) >= 5 then
		signal sqlstate '45000'
        set message_text = 'Solo hay consultas los días Lunes a Viernes.';
	end if;
end //

delimiter ;

delimiter //

create function fn_total_consultas_paciente(param_id_paciente int) 
returns int
deterministic
begin
    declare total int;
    select COUNT(*) into total from citas_medicas 
    where id_paciente = param_id_paciente and estado = 'completada';
    return total;
end //

delimiter ;