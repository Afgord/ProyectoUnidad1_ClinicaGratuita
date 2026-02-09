-- Creamos la base de datos para el proyecto Clinica Gratuita
create database clinica_gratuita;

-- Usamos la BD
use clinica_gratuita;

-- Creamos las tablas según sus relaciones
create table pacientes(
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

create table doctores(
	id_doctor int auto_increment primary key,
    nombre varchar(100) not null,
    email varchar(100) unique,
    telefono varchar(20) unique,
    especialidad varchar(60) not null,
    
    -- Ingresamos validaciones a los datos email y teléfono
    constraint validar_email_doctor check (email like '%_@__%.__%'),
    constraint formato_telefono_doctor check (telefono regexp '^[0-9]{10}$')
);

create table citas_medicas(
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

create table tratamientos(
	id_tratamiento int auto_increment primary key,
    id_cita int not null,
    descripcion text not null,
    duracion int not null,
    
    foreign key (id_cita)
    references citas_medicas(id_cita)
    on delete cascade on update cascade
);

create table medicamentos(
	id_medicamento int auto_increment primary key,
    nombre varchar(60) not null
);

create table tratamientos_medicamentos(
    id_tratamiento int not null,
    id_medicamento int not null,
    primary key (id_tratamiento, id_medicamento),
    
    foreign key (id_tratamiento)
    references tratamientos(id_tratamiento)
    on delete cascade on update cascade,
    
    foreign key (id_medicamento)
    references medicamentos(id_medicamento)
    on delete cascade on update cascade
);