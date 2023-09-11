-- Create tables

create table if not exists karta_anlaggning
(
  id      serial not null
    constraint karta_anlaggning_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_anlaggning owner to ${schemaOwner};


create table if not exists karta_batuthyrning
(
  id      serial not null
    constraint karta_batuthyrning_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_batuthyrning owner to ${schemaOwner};


create table if not exists karta_camping
(
  id      serial not null
    constraint karta_camping_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_camping owner to ${schemaOwner};


create table if not exists karta_discgolf
(
  id      serial not null
    constraint karta_discgolf_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_discgolf owner to ${schemaOwner};


create table if not exists karta_fornlamning
(
  id      serial not null
    constraint karta_fornlamning_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_fornlamning owner to ${schemaOwner};


create table if not exists karta_hembygdsgard
(
  id      serial not null
    constraint karta_hembygdsgard_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_hembygdsgard owner to ${schemaOwner};


create table if not exists karta_intressant_byggnad
(
  id      serial not null
    constraint karta_intressant_byggnad_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_intressant_byggnad owner to ${schemaOwner};


create table if not exists karta_konsthall
(
  id      serial not null
    constraint karta_konsthall_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_konsthall owner to ${schemaOwner};


create table if not exists karta_konstnarlig_gestaltning
(
  id      serial not null
    constraint karta_konstnarlig_gestaltning_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_konstnarlig_gestaltning owner to ${schemaOwner};


create table if not exists karta_museum
(
  id      serial not null
    constraint karta_museum_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_museum owner to ${schemaOwner};


create table if not exists karta_scen
(
  id      serial not null
    constraint karta_scen_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_scen owner to ${schemaOwner};


create table if not exists karta_simhall
(
  id      serial not null
    constraint karta_simhall_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_simhall owner to ${schemaOwner};


create table if not exists karta_skolskjuts
(
  id      serial not null
    constraint karta_skolskjuts_pkey
    primary key,
  geom    geometry(MultiLineString, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_skolskjuts owner to ${schemaOwner};


create table if not exists karta_spar_och_leder
(
  id      serial not null
    constraint karta_spar_och_leder_pkey
    primary key,
  geom    geometry(MultiLineString, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_spar_och_leder owner to ${schemaOwner};


create table if not exists karta_teater
(
  id      serial not null
    constraint karta_teater_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_teater owner to ${schemaOwner};


create table if not exists karta_utegym
(
  id      serial not null
    constraint karta_utegym_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_utegym owner to ${schemaOwner};


create table if not exists karta_utflyktsmal
(
  id      serial not null
    constraint karta_utflyktsmal_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_utflyktsmal owner to ${schemaOwner};


create table if not exists karta_wakeboardbana
(
  id      serial not null
    constraint karta_wakeboardbana_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_wakeboardbana owner to ${schemaOwner};


create table if not exists karta_4h_gard
(
  id      serial not null
    constraint karta_4h_gard_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_4h_gard owner to ${schemaOwner};
