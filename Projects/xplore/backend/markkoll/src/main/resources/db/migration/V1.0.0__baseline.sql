CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;
CREATE EXTENSION IF NOT EXISTS "postgis" WITH SCHEMA public;
CREATE SCHEMA IF NOT EXISTS finfo;

create table intrang_types
(
	name varchar(80) not null
		constraint intrang_type_pk
			primary key
);

create table avtalsstatus
(
	status varchar(60) not null
		constraint avtalsstatus_pkey
			primary key
);

create table projekttyp
(
	typ varchar(60) not null
		constraint projekttyp_pkey
			primary key
);

create table person
(
	id uuid not null
		constraint person_pkey
			primary key,
	adress text,
	postnummer text,
	postort text,
	telefon text,
	bankkonto text,
	e_post text,
	finfo_id_hash bytea,
	namn text not null,
	personnummer text
);

create table avtalsjobb_status
(
	status varchar(60) not null
		constraint avtalsjobb_status_pkey
			primary key
);

create table intrang_subtypes
(
	name varchar(80) not null
		constraint intrang_subtype_pk
			primary key
);

create table detaljtyper
(
	name varchar(80) not null
		constraint detaljtyper_pk
			primary key
);

create table fastighet
(
	id uuid not null
		constraint fastighet_pkey
			primary key,
	fastighetsbeteckning varchar not null,
	huvudagare_id uuid,
	detaljtyp varchar(80) not null
		constraint detaljtyper_name_fk
			references detaljtyper,
	kommunkod varchar,
	kommunnamn varchar,
	trakt varchar,
	blockenhet varchar,
	externid varchar,
	fnr_fds varchar,
	ytkval integer,
	adat varchar,
	omrtyp varchar
);

create table projektstatus
(
	status text not null
		constraint projektstatus_status_key
			unique
);

create table projekt
(
	id serial not null
		constraint projekt_pk
			primary key,
	namn varchar not null,
	ort varchar,
	projekttyp varchar not null
		constraint projekttyp_fk
			references projekttyp,
	organisation varchar,
	beskrivning varchar,
	skapad_av varchar not null,
	skapad_datum timestamp not null,
	status varchar(255) default 'PAGAENDE'::character varying not null
		constraint fk_projektstatus
			references projektstatus (status),
	ledningsagare varchar(255),
	start_datum timestamp,
	bidragsprojekt boolean default false,
	ledningsstracka varchar(60),
	bestallare text
);

create unique index projekt_id_uindex
	on projekt (id);

create table avtalsjobb
(
	id uuid default uuid_generate_v4() not null
		constraint avtalsjobb_pkey
			primary key,
	status text not null
		constraint status_fk
			references avtalsjobb_status,
	path text,
	projekt_id integer not null
		constraint projekt_id_fk
			references projekt,
	total integer not null,
	generated integer not null
);

create table avtalsinformation
(
	ersattning integer,
	fastighet_id uuid not null
		constraint avtalsinformation_fastighet_fk
			references fastighet,
	projekt_id integer not null
		constraint avtalsinformation_projekt_fk
			references projekt,
	anteckning varchar
);

create index avtalsinformation_fastighet_id_idx
	on avtalsinformation (fastighet_id);

create index avtalsinformation_projekt_id_idx
	on avtalsinformation (projekt_id);

create table importversion
(
	id uuid default uuid_generate_v4() not null
		constraint importversion_pkey
			primary key,
	filnamn text not null,
	projekt_id integer
		constraint fk_projekt_id
			references projekt,
	is_current_version boolean default false not null,
	skapad_datum timestamp default CURRENT_TIMESTAMP not null
);

create table intrang
(
	id uuid default uuid_generate_v4() not null
		constraint intrang_pk
			primary key,
	geom geometry(Geometry,3006) not null,
	type varchar(80) not null
		constraint intrang_types_name_fk
			references intrang_types,
	subtype varchar(80) default 'NONE'::character varying not null
		constraint intrang_subtypes_name_fk
			references intrang_subtypes,
	version_id uuid not null
		constraint fk_version_id
			references importversion
);

create index intrang_geom_idx
	on intrang (geom);

create table samfallighet_fastighet
(
	fastighet_id uuid not null,
	samfallighet_id uuid not null
		constraint samfallighet_fastighet_samfallighet_fkey
			references fastighet,
	fastighet_beteckning varchar
);

create table geometristatus
(
	status text not null
		constraint geometristatus_pkey
			primary key
);

create table projekt_fastigheter
(
	fastighet_id uuid not null
		constraint fk_projekt_fastigheter_fid
			references fastighet,
	version_id uuid not null
		constraint fk_version_id
			references importversion,
	geometristatus text default 'OFORANDRAD'::text not null
		constraint fk_geometristatus
			references geometristatus
);

create table fastighet_omrade
(
	fastighet_id uuid not null
		constraint omrade_fastighet_fkey
			references fastighet,
	omrade_nr integer not null,
	geom geometry(Geometry,3006) not null,
	constraint fastighet_omrade_pkey
		primary key (omrade_nr, fastighet_id)
);

CREATE INDEX idx_fastighet_omrade_geom
          ON fastighet_omrade
       USING gist (geom);

create table agartyp
(
	typ text not null
		constraint agartyp_pkey
			primary key
);

create table markagare
(
	id uuid not null
		constraint markagare_pkey
			primary key,
	andel text not null,
	agare_status text not null
		constraint fk_agare_status
			references avtalsstatus,
	person_id uuid
		constraint fk_person_id
			references person,
	fastighet_id uuid not null
		constraint fk_fastighet_id
			references fastighet,
	agartyp text default 'OKAND'::text not null
		constraint agartyp_fk
			references agartyp,
	inkludera_i_avtal boolean default true not null
);

alter table fastighet
	add constraint fk_huvudagare_id
		foreign key (huvudagare_id) references markagare;

create view current_version(projekt_id, version_id) as
	SELECT projekt.id       AS projekt_id,
       importversion.id AS version_id
FROM projekt
         JOIN importversion ON importversion.projekt_id = projekt.id AND importversion.is_current_version = true;

create view intrang_projekt(id, geom, type, subtype, projektid) as
	SELECT intrang.id,
       intrang.geom,
       intrang.type,
       intrang.subtype,
       projekt.id AS projektid
FROM projekt
         JOIN current_version ON current_version.projekt_id = projekt.id
         JOIN intrang ON current_version.version_id = intrang.version_id;

create table finfo.agare
(
	andel text not null,
	adress text,
	co text,
	personnummer text,
	postnummer text,
	postort text,
	description text,
	fastighet_id uuid not null,
	namn text not null,
	id uuid default uuid_generate_v4() not null
		constraint agare_pkey
			primary key,
	typ text
);

create materialized view intrangsgeometrier as
	SELECT fastighet.fastighetsbeteckning,
       st_intersection(intrang.geom, fastighet_omrade.geom) AS geom,
       fastighet.id                                         AS fastighetsid,
       projekt.id                                           AS projektid,
       intrang.type,
       intrang.subtype,
       fastighet_omrade.omrade_nr
FROM projekt
         JOIN importversion v ON v.projekt_id = projekt.id
         JOIN projekt_fastigheter ON v.id = projekt_fastigheter.version_id
         JOIN fastighet ON fastighet.id = projekt_fastigheter.fastighet_id
         JOIN fastighet_omrade ON fastighet.id = fastighet_omrade.fastighet_id
         JOIN current_version ON current_version.version_id = v.id
         JOIN intrang
              ON current_version.version_id = intrang.version_id AND st_intersects(intrang.geom, fastighet_omrade.geom);

create view intrang_fastighet(fastighetsbeteckning, geom, fastighetsid, projektid, type, subtype) as
	SELECT intrangsgeometrier.fastighetsbeteckning,
       intrangsgeometrier.geom,
       intrangsgeometrier.fastighetsid,
       intrangsgeometrier.projektid,
       intrangsgeometrier.type,
       intrangsgeometrier.subtype
FROM intrangsgeometrier;