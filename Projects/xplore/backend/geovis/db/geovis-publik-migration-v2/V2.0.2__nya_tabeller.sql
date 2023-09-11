create table if not exists karta_botanik
(
    id      serial not null
    constraint karta_botanik_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_botanik owner to ${schemaOwner};


create table if not exists karta_by
(
  id      serial not null
    constraint karta_by_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_by owner to ${schemaOwner};


create table if not exists karta_geologi
(
    id      serial not null
    constraint karta_geologi_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_geologi owner to ${schemaOwner};


create table if not exists karta_historia
(
    id      serial not null
    constraint karta_historia_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_historia owner to ${schemaOwner};


create table if not exists karta_natur
(
    id      serial not null
    constraint karta_natur_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_natur owner to ${schemaOwner};


create table if not exists karta_sjomack
(
    id      serial not null
    constraint karta_sjomack_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_sjomack owner to ${schemaOwner};


create table if not exists karta_skidbacke
(
    id      serial not null
    constraint karta_skidbacke_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_skidbacke owner to ${schemaOwner};


create table if not exists karta_stuga
(
    id      serial not null
    constraint karta_stuga_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_stuga owner to ${schemaOwner};


create table if not exists karta_sopmajor
(
    id      serial not null
    constraint karta_sopmajor_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_sopmajor owner to ${schemaOwner};


create table if not exists karta_gasthamn
(
    id      serial not null
    constraint karta_gasthamn_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_gasthamn owner to ${schemaOwner};


create table if not exists karta_hotell
(
    id      serial not null
    constraint karta_hotell_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_hotell owner to ${schemaOwner};


create table if not exists karta_konst
(
    id      serial not null
    constraint karta_konst_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_konst owner to ${schemaOwner};


create table if not exists karta_konstgrasplan
(
    id      serial not null
    constraint karta_konstgrasplan_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_konstgrasplan owner to ${schemaOwner};


create table if not exists karta_ishall
(
    id      serial not null
    constraint karta_ishall_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_ishall owner to ${schemaOwner};


create table if not exists karta_lekplats
(
    id      serial not null
    constraint karta_lekplats_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_lekplats owner to ${schemaOwner};


create table if not exists karta_gardsbutik
(
    id      serial not null
    constraint karta_gardsbutik_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_gardsbutik owner to ${schemaOwner};


create table if not exists karta_gatukok
(
    id      serial not null
    constraint karta_gatukok_pkey
    primary key,
    geom    geometry(MultiPoint, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_gatukok owner to ${schemaOwner};


create table if not exists karta_padelbana
(
    id      serial not null
    constraint karta_padelbana_pkey
    primary key,
    geom    geometry(MultiLineString, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_padelbana owner to ${schemaOwner};


create table if not exists karta_skridskobana
(
    id      serial not null
    constraint karta_skridskobana_pkey
    primary key,
    geom    geometry(MultiLineString, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_skridskobana owner to ${schemaOwner};


create table if not exists karta_skidspar
(
    id      serial not null
    constraint karta_skidspar_pkey
    primary key,
    geom    geometry(MultiLineString, ${epsg}),
    namn    varchar(80),
    beskr   varchar(254),
    link    varchar(254),
    adress  varchar(254),
    kontakt varchar(254)
    );

alter table karta_skidspar owner to ${schemaOwner};



