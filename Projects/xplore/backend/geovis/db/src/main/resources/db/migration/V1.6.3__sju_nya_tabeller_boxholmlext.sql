
create table if not exists boxholmext.karta_aldreomsorg
(
  id      serial not null
    constraint karta_aldreomsorg_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table boxholmext.karta_aldreomsorg owner to geovis;

create table if not exists boxholmext.karta_grundsarskola
(
  id      serial not null
    constraint karta_grundsarskola_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table boxholmext.karta_grundsarskola owner to geovis;

create table if not exists boxholmext.karta_hjalpmedelscentral
(
  id      serial not null
    constraint karta_hjalpmedelscentral_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table boxholmext.karta_hjalpmedelscentral owner to geovis;


create table if not exists boxholmext.karta_kommunrehab
(
  id      serial not null
    constraint karta_kommunrehab_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table boxholmext.karta_kommunrehab owner to geovis;


create table if not exists boxholmext.karta_servicehus
(
  id      serial not null
    constraint karta_servicehus_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table boxholmext.karta_servicehus owner to geovis;


create table if not exists boxholmext.karta_tagbiljettautomat
(
  id      serial not null
    constraint karta_tagbiljettautomat_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table boxholmext.karta_tagbiljettautomat owner to geovis;


create table if not exists boxholmext.karta_torghandel_polygon
(
  id      serial not null
    constraint karta_torghandel_polygon_pkey
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table boxholmext.karta_torghandel_polygon owner to geovis;

