use bdbancos;

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

insert into pais (nombre) values('Argentina');
insert into provincia (id_pais, nombre) 
values(1, 'Buenos Aires'),
(1, 'Santa fe'),
(1, 'Entre rios'),
(1, 'Cordoba');

insert into localidad(id_provincia, nombre) values(1, 'San Miguel'), (1, 'CABA'), (1, 'Olivos'), (1, 'Tres de febrero');
