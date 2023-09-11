
ALTER SCHEMA ips OWNER TO geovis;
ALTER TABLE ips.kladdlager OWNER TO geovis;

drop sequence if exists skargardsstiftelsen.kladdlager_id_seq cascade;
drop sequence if exists skargardsstiftelsen.kladdlager_id_seq1 cascade;
drop table if exists skargardsstiftelsen.kladdlager;

create table if not exists skargardsstiftelsen.kladdlager
(
	id serial not null
		constraint kladdlager_pkey
			primary key,
	skapad timestamp with time zone not null,
	geometry geometry not null,
	skapad_av varchar(40),
	stil json
);

alter table skargardsstiftelsen.kladdlager owner to geovis;

create trigger set_skapad_on_insert_trigger
	before insert
	on skargardsstiftelsen.kladdlager
	for each row
	execute procedure skargardsstiftelsen.trigger_func_skapad_kladdlager();


drop sequence if exists vb_energi.kladdlager_id_seq cascade;
drop table if exists vb_energi.kladdlager;

create table if not exists vb_energi.kladdlager
(
	id serial not null
		constraint kladdlager_pkey
			primary key,
	skapad timestamp with time zone not null,
	geometry geometry not null,
	skapad_av varchar(40),
	stil json
);

alter table vb_energi.kladdlager owner to geovis;

create trigger set_skapad_on_insert_trigger
	before insert
	on vb_energi.kladdlager
	for each row
	execute procedure vb_energi.trigger_func_skapad_kladdlager();


drop sequence if exists gotakanalbolag.kladdlager_id_seq cascade;
drop table if exists gotakanalbolag.kladdlager;

create table if not exists gotakanalbolag.kladdlager
(
	id serial not null
		constraint kladdlager_pkey
			primary key,
	skapad timestamp with time zone not null,
	geometry geometry not null,
	skapad_av varchar(40),
	stil json
);

alter table gotakanalbolag.kladdlager owner to geovis;

create trigger set_skapad_on_insert_trigger
	before insert
	on gotakanalbolag.kladdlager
	for each row
	execute procedure gotakanalbolag.trigger_func_skapad_kladdlager();


drop sequence if exists demo_geovis.kladdlager_id_seq cascade;
drop table if exists demo_geovis.kladdlager;

create table if not exists demo_geovis.kladdlager
(
	id serial not null
		constraint kladdlager_pkey
			primary key,
	skapad timestamp with time zone not null,
	geometry geometry not null,
	skapad_av varchar(40),
	stil json
);

alter table demo_geovis.kladdlager owner to geovis;

create trigger set_skapad_on_insert_trigger
	before insert
	on demo_geovis.kladdlager
	for each row
	execute procedure demo_geovis.trigger_func_skapad_kladdlager();


drop sequence if exists tranasext.tranasext_kladdlager_pkey_seq cascade;
drop table if exists tranasext.kladdlager;

create table if not exists tranasext.kladdlager
(
	id serial not null
		constraint kladdlager_test_pkey
			primary key,
	skapad timestamp with time zone not null,
	geometry geometry not null,
	skapad_av varchar(40),
	stil json
);

alter table tranasext.kladdlager owner to "xplore-admin";

create trigger set_skapad_on_insert_trigger
	before insert
	on tranasext.kladdlager
	for each row
	execute procedure tranasext.trigger_func_skapad_kladdlager();
