-- GeoVis Vy för Skärgårdsstiftelsen

drop sequence if exists skargardsstiftelsen.standard_sgs_punkt_id_seq1 cascade;
drop table if exists skargardsstiftelsen.karta_egna_byggnader;

create table if not exists skargardsstiftelsen.karta_egna_byggnader
(
  id      serial not null
    constraint karta_egna_byggnader_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table skargardsstiftelsen.f_region owner to geovis;
alter table skargardsstiftelsen.karta_egna_byggnader owner to geovis;
alter table skargardsstiftelsen.t_region owner to geovis;


-- GeoVis Publik för Aneby

drop sequence if exists anebyext.batplatser_aneby_id_seq cascade;
drop table if exists anebyext.karta_batplatser;

create table if not exists anebyext.karta_batplatser
(
  id      serial not null
    constraint "karta_batplatser_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table anebyext.karta_batplatser owner to geovis;

drop sequence if exists anebyext.standard_kommunkarta_yta_id_seq4 cascade;
drop table if exists anebyext.karta_torghandel;

create table if not exists anebyext.karta_torghandel
(
  id      serial not null
    constraint karta_torghandel_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table anebyext.karta_torghandel owner to geovis;
