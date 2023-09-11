ALTER SCHEMA ${schema} OWNER TO ${schemaOwner};

-- Create tables

create table if not exists karta_aldreomsorg
(
  id      serial not null
    constraint karta_aldreomsorg_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_aldreomsorg owner to ${schemaOwner};


create table if not exists karta_apotek
(
  id      serial not null
    constraint karta_apotek_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_apotek owner to ${schemaOwner};


create table if not exists karta_atervinningscentral
(
  id      serial not null
    constraint karta_atervinningscentral_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_atervinningscentral owner to ${schemaOwner};


create table if not exists karta_atervinningsstation
(
  id      serial not null
    constraint karta_atervinningsstation_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_atervinningsstation owner to ${schemaOwner};


create table if not exists karta_badplats
(
  id      serial not null
    constraint karta_badplats_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_badplats owner to ${schemaOwner};


create table if not exists karta_bankomat
(
  id      serial not null
    constraint karta_bankomat_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_bankomat owner to ${schemaOwner};


create table if not exists karta_basketplan
(
  id      serial not null
    constraint karta_basketplan_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_basketplan owner to ${schemaOwner};


create table if not exists karta_batplatser
(
  id      serial not null
    constraint "karta_batplatser_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_batplatser owner to ${schemaOwner};


create table if not exists karta_beachvolley
(
  id      serial not null
    constraint karta_beachvolley_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_beachvolley owner to ${schemaOwner};


create table if not exists karta_begravningsplats
(
  id      serial not null
    constraint karta_begravningsplats_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_begravningsplats owner to ${schemaOwner};


create table if not exists karta_belaggningsarbete
(
  id      serial not null
    constraint karta_belaggningsarbete_pkey
    primary key,
  geom    geometry(MultiLineString, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_belaggningsarbete owner to ${schemaOwner};


create table if not exists karta_bibliotek
(
  id      serial not null
    constraint karta_bibliotek_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_bibliotek owner to ${schemaOwner};


create table if not exists karta_biograf
(
  id      serial not null
    constraint karta_biograf_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_biograf owner to ${schemaOwner};


create table if not exists karta_boende
(
  id      serial not null
    constraint karta_boende_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_boende owner to ${schemaOwner};


create table if not exists karta_bostadstomter_ejbebyggda
(
  id      serial not null
    constraint karta_bostadstomter_ejbebyggda_pkey
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_bostadstomter_ejbebyggda owner to ${schemaOwner};


create table if not exists karta_boulebanor
(
  id      serial not null
    constraint karta_boulebanor_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_boulebanor owner to ${schemaOwner};


create table if not exists karta_bowling
(
  id      serial not null
    constraint karta_bowling_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_bowling owner to ${schemaOwner};


create table if not exists karta_busshallplats
(
  id      serial not null
    constraint karta_busshallplats_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_busshallplats owner to ${schemaOwner};


create table if not exists karta_busskort
(
  id      serial not null
    constraint karta_busskort_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_busskort owner to ${schemaOwner};


create table if not exists karta_busstation
(
  id      serial not null
    constraint karta_busstation_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_busstation owner to ${schemaOwner};


create table if not exists karta_bygdegardar
(
  id      serial not null
    constraint karta_bygdegardar_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_bygdegardar owner to ${schemaOwner};


create table if not exists karta_cafe
(
  id      serial not null
    constraint karta_cafe_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_cafe owner to ${schemaOwner};


create table if not exists karta_cykelleder
(
  id      serial not null
    constraint karta_cykelleder_pkey
    primary key,
  geom    geometry(MultiLineString, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_cykelleder owner to ${schemaOwner};


create table if not exists karta_cykelvagar
(
  id      serial not null
    constraint karta_cykelvagar_pkey
    primary key,
  geom    geometry(MultiLineString, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_cykelvagar owner to ${schemaOwner};


create table if not exists "karta_daglig-verksamhet"
(
  id      serial not null
    constraint "karta_daglig-verksamhet_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_daglig-verksamhet" owner to ${schemaOwner};


create table if not exists "karta_detaljplaner-gallande"
(
  id      serial not null
    constraint "karta_detaljplaner-gallande_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(254),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_detaljplaner-gallande" owner to ${schemaOwner};


create table if not exists "karta_detaljplaner-pagaende"
(
  id      serial not null
    constraint "karta_detaljplaner-pagaende_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(254),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_detaljplaner-pagaende" owner to ${schemaOwner};


create table if not exists karta_drivmedelsstation
(
  id      serial not null
    constraint karta_drivmedelsstation_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_drivmedelsstation owner to ${schemaOwner};


create table if not exists karta_fiske
(
  id      serial not null
    constraint karta_fiske_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_fiske owner to ${schemaOwner};


create table if not exists karta_fiskekort
(
  id      serial not null
    constraint karta_fiskekort_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_fiskekort owner to ${schemaOwner};


create table if not exists "karta_folkets-park"
(
  id      serial not null
    constraint "karta_folkets-park_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_folkets-park" owner to ${schemaOwner};


create table if not exists karta_folktandvard
(
  id      serial not null
    constraint karta_folktandvard_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_folktandvard owner to ${schemaOwner};


create table if not exists karta_forskola
(
  id      serial not null
    constraint karta_forskola_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_forskola owner to ${schemaOwner};


create table if not exists karta_fotbollsplaner
(
  id      serial not null
    constraint karta_fotbollsplaner_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_fotbollsplaner owner to ${schemaOwner};


create table if not exists karta_friidrottsanl
(
  id      serial not null
    constraint karta_friidrottsanl_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_friidrottsanl owner to ${schemaOwner};


create table if not exists karta_fritidsgard
(
  id      serial not null
    constraint karta_fritidsgard_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_fritidsgard owner to ${schemaOwner};


create table if not exists karta_fritidshem
(
  id      serial not null
    constraint karta_fritidshem_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_fritidshem owner to ${schemaOwner};


create table if not exists karta_golfbanor
(
  id      serial not null
    constraint karta_golfbanor_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_golfbanor owner to ${schemaOwner};


create table if not exists "karta_grav-uppstallningstillstand"
(
  id      serial not null
    constraint "karta_grav-uppstallningstillstand_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_grav-uppstallningstillstand" owner to ${schemaOwner};


create table if not exists karta_grundsarskola
(
  id      serial not null
    constraint karta_grundsarskola_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_grundsarskola owner to ${schemaOwner};


create table if not exists karta_gym
(
  id      serial not null
    constraint karta_gym_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_gym owner to ${schemaOwner};


create table if not exists karta_gymnasium
(
  id      serial not null
    constraint karta_gymnasium_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_gymnasium owner to ${schemaOwner};


create table if not exists karta_hjalpmedelscentral
(
  id      serial not null
    constraint karta_hjalpmedelscentral_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_hjalpmedelscentral owner to ${schemaOwner};


create table if not exists karta_hamn
(
  id      serial not null
    constraint karta_hamn_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_hamn owner to ${schemaOwner};


create table if not exists karta_hembygdsforening
(
  id      serial not null
    constraint karta_hembygdsforening_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_hembygdsforening owner to ${schemaOwner};


create table if not exists karta_hjartstartare
(
  id      serial not null
    constraint karta_hjartstartare_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_hjartstartare owner to ${schemaOwner};


create table if not exists karta_hundklubb
(
  id      serial not null
    constraint karta_hundklubb_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_hundklubb owner to ${schemaOwner};


create table if not exists karta_hundpasar
(
  id      serial not null
    constraint karta_hundpasar_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_hundpasar owner to ${schemaOwner};


create table if not exists karta_idrottshall
(
  id      serial not null
    constraint karta_idrottshall_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_idrottshall owner to ${schemaOwner};


create table if not exists karta_isbanor
(
  id      serial not null
    constraint karta_isbanor_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_isbanor owner to ${schemaOwner};


create table if not exists karta_kolonilotter
(
  id      serial not null
    constraint karta_kolonilotter_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_kolonilotter owner to ${schemaOwner};


create table if not exists "karta_kommunagd-mark"
(
  id      serial not null
    constraint "karta_kommunagd-mark_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_kommunagd-mark" owner to ${schemaOwner};


create table if not exists karta_kommunkontor
(
  id      serial not null
    constraint karta_kommunkontor_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_kommunkontor owner to ${schemaOwner};


create table if not exists karta_kommunrehab
(
  id      serial not null
    constraint karta_kommunrehab_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_kommunrehab owner to ${schemaOwner};


create table if not exists karta_konsumentradgivning
(
  id      serial not null
    constraint karta_konsumentradgivning_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_konsumentradgivning owner to ${schemaOwner};


create table if not exists karta_kulturforening
(
  id      serial not null
    constraint karta_kulturforening_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_kulturforening owner to ${schemaOwner};


create table if not exists karta_kulturskolan
(
  id      serial not null
    constraint karta_kulturskolan_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_kulturskolan owner to ${schemaOwner};


create table if not exists karta_laddstationer
(
  id      serial not null
    constraint karta_laddstationer_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_laddstationer owner to ${schemaOwner};


create table if not exists karta_langdskidspar
(
  id      serial not null
    constraint karta_langdskidspar_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_langdskidspar owner to ${schemaOwner};


create table if not exists karta_latrinkarl
(
  id      serial not null
    constraint karta_latrinkarl_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_latrinkarl owner to ${schemaOwner};


create table if not exists "karta_ledig-industrimark"
(
  id      serial not null
    constraint "karta_ledig-industrimark_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_ledig-industrimark" owner to ${schemaOwner};


create table if not exists "karta_ledig-industrimark_privatagd"
(
  id      serial not null
    constraint "karta_ledig-industrimark_privatagd_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_ledig-industrimark_privatagd" owner to ${schemaOwner};


create table if not exists "karta_lediga-bostadstomter"
(
  id      serial not null
    constraint "karta_lediga-bostadstomter_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_lediga-bostadstomter" owner to ${schemaOwner};


create table if not exists karta_lediga_tomter_flerfamiljshus
(
  id      serial not null
    constraint karta_lediga_tomter_flerfamiljshus_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_lediga_tomter_flerfamiljshus owner to ${schemaOwner};


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


create table if not exists karta_livsmedelsbutik
(
  id      serial not null
    constraint karta_livsmedelsbutik_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_livsmedelsbutik owner to ${schemaOwner};


create table if not exists karta_motionsanl
(
  id      serial not null
    constraint karta_motionsanl_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_motionsanl owner to ${schemaOwner};


create table if not exists karta_motionsspar_elljusspar
(
  id      serial not null
    constraint karta_motionsspar_elljusspar_pkey
    primary key,
  geom    geometry(MultiLineString, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_motionsspar_elljusspar owner to ${schemaOwner};


create table if not exists karta_motionsspar_uppmarkta
(
  id      serial not null
    constraint karta_motionsspar_uppmarkta_pkey
    primary key,
  geom    geometry(MultiLineString, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_motionsspar_uppmarkta owner to ${schemaOwner};


create table if not exists karta_motorcrossbana
(
  id      serial not null
    constraint karta_motorcrossbana_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_motorcrossbana owner to ${schemaOwner};


create table if not exists karta_naturreservat
(
  id      serial not null
    constraint karta_naturreservat_pkey
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_naturreservat owner to ${schemaOwner};


create table if not exists "karta_naturvardsprogram-objekt"
(
  id      serial not null
    constraint "karta_naturvardsprogram-objekt_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_naturvardsprogram-objekt" owner to ${schemaOwner};


create table if not exists "karta_naturvardsprogram-omraden"
(
  id      serial not null
    constraint "karta_naturvardsprogram-omraden_pkey"
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_naturvardsprogram-omraden" owner to ${schemaOwner};


create table if not exists karta_obo
(
  id      serial not null
    constraint karta_obo_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_obo owner to ${schemaOwner};


create table if not exists karta_offentligatoaletter
(
  id      serial not null
    constraint karta_offentligatoaletter_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_offentligatoaletter owner to ${schemaOwner};


create table if not exists "karta_okad-sysselsattning"
(
  id      serial not null
    constraint "karta_okad-sysselsattning_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_okad-sysselsattning" owner to ${schemaOwner};


create table if not exists karta_park
(
  id      serial not null
    constraint karta_park_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_park owner to ${schemaOwner};


create table if not exists "karta_parkering-pendlar"
(
  id      serial not null
    constraint "karta_parkering-pendlar_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_parkering-pendlar" owner to ${schemaOwner};


create table if not exists karta_parkering_rorelsehindrade
(
  id      serial not null
    constraint karta_parkering_rorelsehindrade_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_parkering_rorelsehindrade owner to ${schemaOwner};


create table if not exists karta_parkeringsplatser
(
  id      serial not null
    constraint karta_parkeringsplatser_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_parkeringsplatser owner to ${schemaOwner};


create table if not exists karta_polis
(
  id      serial not null
    constraint karta_polis_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_polis owner to ${schemaOwner};


create table if not exists karta_postombud
(
  id      serial not null
    constraint karta_postombud_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_postombud owner to ${schemaOwner};


create table if not exists karta_racketanl
(
  id      serial not null
    constraint karta_racketanl_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_racketanl owner to ${schemaOwner};


create table if not exists karta_raddningstjanst
(
  id      serial not null
    constraint karta_raddningstjanst_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_raddningstjanst owner to ${schemaOwner};


create table if not exists karta_reserverade_tomter
(
  id      serial not null
    constraint karta_reserverade_tomter_pkey
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_reserverade_tomter owner to ${schemaOwner};


create table if not exists karta_restauranger
(
  id      serial not null
    constraint karta_restauranger_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_restauranger owner to ${schemaOwner};


create table if not exists karta_ridanl
(
  id      serial not null
    constraint karta_ridanl_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_ridanl owner to ${schemaOwner};


create table if not exists karta_samlingslokal
(
  id      serial not null
    constraint karta_samlingslokal_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_samlingslokal owner to ${schemaOwner};


create table if not exists karta_servicehus
(
  id      serial not null
    constraint karta_servicehus_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_servicehus owner to ${schemaOwner};

create table if not exists karta_sevardheter_besoksmal
(
  id      serial not null
    constraint karta_sevardheter_besoksmal_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_sevardheter_besoksmal owner to ${schemaOwner};


create table if not exists karta_sevardheter_kultur
(
  id      serial not null
    constraint karta_sevardheter_kultur_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_sevardheter_kultur owner to ${schemaOwner};


create table if not exists karta_sevardheter_natur
(
  id      serial not null
    constraint karta_sevardheter_natur_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_sevardheter_natur owner to ${schemaOwner};


create table if not exists karta_skatepark
(
  id      serial not null
    constraint karta_skatepark_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_skatepark owner to ${schemaOwner};


create table if not exists karta_skjutbana
(
  id      serial not null
    constraint karta_skjutbana_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_skjutbana owner to ${schemaOwner};


create table if not exists karta_skola
(
  id      serial not null
    constraint karta_skola_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_skola owner to ${schemaOwner};


create table if not exists karta_socialkontoret
(
  id      serial not null
    constraint karta_socialkontoret_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_socialkontoret owner to ${schemaOwner};


create table if not exists karta_sophamtning
(
  id      serial not null
    constraint karta_sophamtning_pkey
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_sophamtning owner to ${schemaOwner};


create table if not exists karta_spontanidrottsplats
(
  id      serial not null
    constraint karta_spontanidrottsplats_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_spontanidrottsplats owner to ${schemaOwner};


create table if not exists karta_station
(
  id      serial not null
    constraint karta_station_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_station owner to ${schemaOwner};


create table if not exists karta_systembolaget
(
  id      serial not null
    constraint karta_systembolaget_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_systembolaget owner to ${schemaOwner};


create table if not exists karta_tagbiljettautomat
(
  id      serial not null
    constraint karta_tagbiljettautomat_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_tagbiljettautomat owner to ${schemaOwner};


create table if not exists karta_torghandel
(
  id      serial not null
    constraint karta_torghandel_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_torghandel owner to ${schemaOwner};


create table if not exists karta_torghandel_polygon
(
  id      serial not null
    constraint karta_torghandel_polygon_pkey
    primary key,
  geom    geometry(MultiPolygon, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_torghandel_polygon owner to ${schemaOwner};


create table if not exists "karta_traffpunkt-aktivitetshuset"
(
  id      serial not null
    constraint "karta_traffpunkt-aktivitetshuset_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_traffpunkt-aktivitetshuset" owner to ${schemaOwner};


create table if not exists "karta_traffpunkt-aldre"
(
  id      serial not null
    constraint "karta_traffpunkt-aldre_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_traffpunkt-aldre" owner to ${schemaOwner};


create table if not exists "karta_traffpunkt-funktionsnedsatta"
(
  id      serial not null
    constraint "karta_traffpunkt-funktionsnedsatta_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_traffpunkt-funktionsnedsatta" owner to ${schemaOwner};


create table if not exists karta_turistinformation
(
  id      serial not null
    constraint karta_turistinformation_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_turistinformation owner to ${schemaOwner};


create table if not exists karta_traffpunkt_seniorer
(
  id      serial not null
    constraint karta_traffpunkt_seniorer_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_traffpunkt_seniorer owner to ${schemaOwner};


create table if not exists "karta_uppstallningsplats"
(
  id      serial not null
    constraint "karta_uppstallningsplats_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_uppstallningsplats" owner to ${schemaOwner};


create table if not exists "karta_uppstallningsplats-husbilar"
(
  id      serial not null
    constraint "karta_uppstallningsplats-husbilar_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_uppstallningsplats-husbilar" owner to ${schemaOwner};


create table if not exists karta_utomhusgym
(
  id      serial not null
    constraint karta_utomhusgym_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_utomhusgym owner to ${schemaOwner};


create table if not exists karta_vandringsleder
(
  id      serial not null
    constraint karta_vandringsleder_pkey
    primary key,
  geom    geometry(MultiLineString, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_vandringsleder owner to ${schemaOwner};


create table if not exists karta_vardcentral
(
  id      serial not null
    constraint karta_vardcentral_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_vardcentral owner to ${schemaOwner};


create table if not exists "karta_viktigt-meddelande"
(
  id      serial not null
    constraint "karta_viktigt-meddelande_pkey"
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table "karta_viktigt-meddelande" owner to ${schemaOwner};


create table if not exists karta_vuxenutbildning
(
  id      serial not null
    constraint karta_vuxenutbildning_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_vuxenutbildning owner to ${schemaOwner};


create table if not exists karta_anlaggning
(
  id      serial not null
    constraint karta_anlaggning_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiLineString, ${epsg}),
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
  geom    geometry(MultiLineString, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
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
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_4h_gard owner to ${schemaOwner};


create table if not exists karta_naturskola
(
  id      serial not null
    constraint karta_naturskola_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_naturskola owner to ${schemaOwner};


create table if not exists karta_badhus
(
  id      serial not null
    constraint karta_badhus_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_badhus owner to ${schemaOwner};
