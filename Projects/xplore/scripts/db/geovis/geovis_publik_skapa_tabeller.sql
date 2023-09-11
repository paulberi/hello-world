-- Filen innehåller alla tabeller som finns i GeoVis Publik.
-- Scriptet skapar alla tabeller med tillhörande sequences.
-- Namnet på alla scheman som används av GeoVis Publik har ändelsen "ext".
-- Ersätt "referensext" i följande SQL-script med det nya schemanamnet exempelvis "luleaext".
-- Använd inga åäö, blanktecken och bindstreck i namnet.
-- Kör scriptet i "geovisdb" som är databasen för GeoVis i utvecklingsmiljön.

create table if not exists referensext.karta_aldreomsorg
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

alter table referensext.karta_aldreomsorg owner to geovis;

create table if not exists referensext.karta_apotek
(
  id      serial not null
    constraint karta_apotek_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_apotek owner to geovis;


create table if not exists referensext.karta_atervinningscentral
(
  id      serial not null
    constraint karta_atervinningscentral_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_atervinningscentral owner to geovis;


create table if not exists referensext.karta_atervinningsstation
(
  id      serial not null
    constraint karta_atervinningsstation_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_atervinningsstation owner to geovis;


create table if not exists referensext.karta_badplats
(
  id      serial not null
    constraint karta_badplats_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_badplats owner to geovis;


create table if not exists referensext.karta_bankomat
(
  id      serial not null
    constraint karta_bankomat_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_bankomat owner to geovis;


create table if not exists referensext.karta_basketplan
(
  id      serial not null
    constraint karta_basketplan_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_basketplan owner to geovis;


create table if not exists referensext.karta_batplatser
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

alter table referensext.karta_batplatser owner to geovis;


create table if not exists referensext.karta_beachvolley
(
  id      serial not null
    constraint karta_beachvolley_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_beachvolley owner to geovis;


create table if not exists referensext.karta_begravningsplats
(
  id      serial not null
    constraint karta_begravningsplats_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_begravningsplats owner to geovis;


create table if not exists referensext.karta_belaggningsarbete
(
  id      serial not null
    constraint karta_belaggningsarbete_pkey
    primary key,
  geom    geometry(MultiLineString, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_belaggningsarbete owner to geovis;


create table if not exists referensext.karta_bibliotek
(
  id      serial not null
    constraint karta_bibliotek_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_bibliotek owner to geovis;


create table if not exists referensext.karta_biograf
(
  id      serial not null
    constraint karta_biograf_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_biograf owner to geovis;


create table if not exists referensext.karta_boende
(
  id      serial not null
    constraint karta_boende_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_boende owner to geovis;


create table if not exists referensext.karta_bostadstomter_ejbebyggda
(
  id      serial not null
    constraint karta_bostadstomter_ejbebyggda_pkey
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_bostadstomter_ejbebyggda owner to geovis;


create table if not exists referensext.karta_boulebanor
(
  id      serial not null
    constraint karta_boulebanor_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_boulebanor owner to geovis;


create table if not exists referensext.karta_bowling
(
  id      serial not null
    constraint karta_bowling_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_bowling owner to geovis;


create table if not exists referensext.karta_busshallplats
(
  id      serial not null
    constraint karta_busshallplats_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_busshallplats owner to geovis;


create table if not exists referensext.karta_busskort
(
  id      serial not null
    constraint karta_busskort_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_busskort owner to geovis;


create table if not exists referensext.karta_busstation
(
  id      serial not null
    constraint karta_busstation_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_busstation owner to geovis;


create table if not exists referensext.karta_bygdegardar
(
  id      serial not null
    constraint karta_bygdegardar_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_bygdegardar owner to geovis;


create table if not exists referensext.karta_cafe
(
  id      serial not null
    constraint karta_cafe_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_cafe owner to geovis;


create table if not exists referensext.karta_cykelleder
(
  id      serial not null
    constraint karta_cykelleder_pkey
    primary key,
  geom    geometry(MultiLineString, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_cykelleder owner to geovis;


create table if not exists referensext.karta_cykelvagar
(
  id      serial not null
    constraint karta_cykelvagar_pkey
    primary key,
  geom    geometry(MultiLineString, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_cykelvagar owner to geovis;


create table if not exists referensext."karta_daglig-verksamhet"
(
  id      serial not null
    constraint "karta_daglig-verksamhet_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_daglig-verksamhet" owner to geovis;


create table if not exists referensext."karta_detaljplaner-gallande"
(
  id      serial not null
    constraint "karta_detaljplaner-gallande_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(254),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_detaljplaner-gallande" owner to geovis;


create table if not exists referensext."karta_detaljplaner-pagaende"
(
  id      serial not null
    constraint "karta_detaljplaner-pagaende_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(254),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_detaljplaner-pagaende" owner to geovis;


create table if not exists referensext.karta_drivmedelsstation
(
  id      serial not null
    constraint karta_drivmedelsstation_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_drivmedelsstation owner to geovis;


create table if not exists referensext.karta_fiske
(
  id      serial not null
    constraint karta_fiske_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_fiske owner to geovis;


create table if not exists referensext.karta_fiskekort
(
  id      serial not null
    constraint karta_fiskekort_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_fiskekort owner to geovis;


create table if not exists referensext."karta_folkets-park"
(
  id      serial not null
    constraint "karta_folkets-park_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_folkets-park" owner to geovis;


create table if not exists referensext.karta_folktandvard
(
  id      serial not null
    constraint karta_folktandvard_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_folktandvard owner to geovis;


create table if not exists referensext.karta_forskola
(
  id      serial not null
    constraint karta_forskola_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_forskola owner to geovis;


create table if not exists referensext.karta_fotbollsplaner
(
  id      serial not null
    constraint karta_fotbollsplaner_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_fotbollsplaner owner to geovis;


create table if not exists referensext.karta_friidrottsanl
(
  id      serial not null
    constraint karta_friidrottsanl_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_friidrottsanl owner to geovis;


create table if not exists referensext.karta_fritidsgard
(
  id      serial not null
    constraint karta_fritidsgard_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_fritidsgard owner to geovis;


create table if not exists referensext.karta_fritidshem
(
  id      serial not null
    constraint karta_fritidshem_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_fritidshem owner to geovis;


create table if not exists referensext.karta_golfbanor
(
  id      serial not null
    constraint karta_golfbanor_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_golfbanor owner to geovis;


create table if not exists referensext."karta_grav-uppstallningstillstand"
(
  id      serial not null
    constraint "karta_grav-uppstallningstillstand_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_grav-uppstallningstillstand" owner to geovis;


create table if not exists referensext.karta_grundsarskola
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

alter table referensext.karta_grundsarskola owner to geovis;


create table if not exists referensext.karta_gym
(
  id      serial not null
    constraint karta_gym_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_gym owner to geovis;


create table if not exists referensext.karta_gymnasium
(
  id      serial not null
    constraint karta_gymnasium_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_gymnasium owner to geovis;


create table if not exists referensext.karta_hamn
(
  id      serial not null
    constraint karta_hamn_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_hamn owner to geovis;


create table if not exists referensext.karta_hembygdsforening
(
  id      serial not null
    constraint karta_hembygdsforening_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_hembygdsforening owner to geovis;


create table if not exists referensext.karta_hjalpmedelscentral
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

alter table referensext.karta_hjalpmedelscentral owner to geovis;


create table if not exists referensext.karta_hjartstartare
(
  id      serial not null
    constraint karta_hjartstartare_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_hjartstartare owner to geovis;


create table if not exists referensext.karta_hundklubb
(
  id      serial not null
    constraint karta_hundklubb_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_hundklubb owner to geovis;


create table if not exists referensext.karta_hundpasar
(
  id      serial not null
    constraint karta_hundpasar_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_hundpasar owner to geovis;


create table if not exists referensext.karta_idrottshall
(
  id      serial not null
    constraint karta_idrottshall_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_idrottshall owner to geovis;


create table if not exists referensext.karta_isbanor
(
  id      serial not null
    constraint karta_isbanor_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_isbanor owner to geovis;


create table if not exists referensext.karta_kolonilotter
(
  id      serial not null
    constraint karta_kolonilotter_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_kolonilotter owner to geovis;


create table if not exists referensext."karta_kommunagd-mark"
(
  id      serial not null
    constraint "karta_kommunagd-mark_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_kommunagd-mark" owner to geovis;


create table if not exists referensext.karta_kommunkontor
(
  id      serial not null
    constraint karta_kommunkontor_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_kommunkontor owner to geovis;


create table if not exists referensext.karta_kommunrehab
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

alter table referensext.karta_kommunrehab owner to geovis;


create table if not exists referensext.karta_konsumentradgivning
(
  id      serial not null
    constraint karta_konsumentradgivning_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_konsumentradgivning owner to geovis;


create table if not exists referensext.karta_kulturforening
(
  id      serial not null
    constraint karta_kulturforening_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_kulturforening owner to geovis;


create table if not exists referensext.karta_kulturskolan
(
  id      serial not null
    constraint karta_kulturskolan_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_kulturskolan owner to geovis;


create table if not exists referensext.karta_laddstationer
(
  id      serial not null
    constraint karta_laddstationer_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_laddstationer owner to geovis;


create table if not exists referensext.karta_langdskidspar
(
  id      serial not null
    constraint karta_langdskidspar_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_langdskidspar owner to geovis;


create table if not exists referensext.karta_latrinkarl
(
  id      serial not null
    constraint karta_latrinkarl_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_latrinkarl owner to geovis;


create table if not exists referensext."karta_ledig-industrimark"
(
  id      serial not null
    constraint "karta_ledig-industrimark_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_ledig-industrimark" owner to geovis;


create table if not exists referensext."karta_ledig-industrimark_privatagd"
(
  id      serial not null
    constraint "karta_ledig-industrimark_privatagd_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_ledig-industrimark_privatagd" owner to geovis;


create table if not exists referensext."karta_lediga-bostadstomter"
(
  id      serial not null
    constraint "karta_lediga-bostadstomter_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_lediga-bostadstomter" owner to geovis;


create table if not exists referensext.karta_lediga_tomter_flerfamiljshus
(
  id      serial not null
    constraint karta_lediga_tomter_flerfamiljshus_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_lediga_tomter_flerfamiljshus owner to geovis;


create table if not exists referensext.karta_lekplats
(
  id      serial not null
    constraint karta_lekplats_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_lekplats owner to geovis;


create table if not exists referensext.karta_livsmedelsbutik
(
  id      serial not null
    constraint karta_livsmedelsbutik_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_livsmedelsbutik owner to geovis;


create table if not exists referensext.karta_motionsanl
(
  id      serial not null
    constraint karta_motionsanl_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_motionsanl owner to geovis;


create table if not exists referensext.karta_motionsspar_elljusspar
(
  id      serial not null
    constraint karta_motionsspar_elljusspar_pkey
    primary key,
  geom    geometry(MultiLineString, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_motionsspar_elljusspar owner to geovis;


create table if not exists referensext.karta_motionsspar_uppmarkta
(
  id      serial not null
    constraint karta_motionsspar_uppmarkta_pkey
    primary key,
  geom    geometry(MultiLineString, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_motionsspar_uppmarkta owner to geovis;


create table if not exists referensext.karta_motorcrossbana
(
  id      serial not null
    constraint karta_motorcrossbana_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_motorcrossbana owner to geovis;


create table if not exists referensext.karta_naturreservat
(
  id      serial not null
    constraint karta_naturreservat_pkey
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_naturreservat owner to geovis;


create table if not exists referensext."karta_naturvardsprogram-objekt"
(
  id      serial not null
    constraint "karta_naturvardsprogram-objekt_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_naturvardsprogram-objekt" owner to geovis;


create table if not exists referensext."karta_naturvardsprogram-omraden"
(
  id      serial not null
    constraint "karta_naturvardsprogram-omraden_pkey"
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_naturvardsprogram-omraden" owner to geovis;


create table if not exists referensext.karta_obo
(
  id      serial not null
    constraint karta_obo_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_obo owner to geovis;


create table if not exists referensext.karta_offentligatoaletter
(
  id      serial not null
    constraint karta_offentligatoaletter_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_offentligatoaletter owner to geovis;


create table if not exists referensext."karta_okad-sysselsattning"
(
  id      serial not null
    constraint "karta_okad-sysselsattning_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_okad-sysselsattning" owner to geovis;


create table if not exists referensext.karta_park
(
  id      serial not null
    constraint karta_park_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_park owner to geovis;


create table if not exists referensext."karta_parkering-pendlar"
(
  id      serial not null
    constraint "karta_parkering-pendlar_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_parkering-pendlar" owner to geovis;


create table if not exists referensext.karta_parkering_rorelsehindrade
(
  id      serial not null
    constraint karta_parkering_rorelsehindrade_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_parkering_rorelsehindrade owner to geovis;


create table if not exists referensext.karta_parkeringsplatser
(
  id      serial not null
    constraint karta_parkeringsplatser_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_parkeringsplatser owner to geovis;


create table if not exists referensext.karta_polis
(
  id      serial not null
    constraint karta_polis_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_polis owner to geovis;


create table if not exists referensext.karta_postombud
(
  id      serial not null
    constraint karta_postombud_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_postombud owner to geovis;


create table if not exists referensext.karta_racketanl
(
  id      serial not null
    constraint karta_racketanl_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_racketanl owner to geovis;


create table if not exists referensext.karta_raddningstjanst
(
  id      serial not null
    constraint karta_raddningstjanst_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_raddningstjanst owner to geovis;


create table if not exists referensext.karta_reserverade_tomter
(
  id      serial not null
    constraint karta_reserverade_tomter_pkey
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_reserverade_tomter owner to geovis;


create table if not exists referensext.karta_restauranger
(
  id      serial not null
    constraint karta_restauranger_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_restauranger owner to geovis;


create table if not exists referensext.karta_ridanl
(
  id      serial not null
    constraint karta_ridanl_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_ridanl owner to geovis;


create table if not exists referensext.karta_samlingslokal
(
  id      serial not null
    constraint karta_samlingslokal_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_samlingslokal owner to geovis;


create table if not exists referensext.karta_sevardheter_besoksmal
(
  id      serial not null
    constraint karta_sevardheter_besoksmal_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_sevardheter_besoksmal owner to geovis;


create table if not exists referensext.karta_servicehus
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

alter table referensext.karta_servicehus owner to geovis;


create table if not exists referensext.karta_sevardheter_kultur
(
  id      serial not null
    constraint karta_sevardheter_kultur_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_sevardheter_kultur owner to geovis;


create table if not exists referensext.karta_sevardheter_natur
(
  id      serial not null
    constraint karta_sevardheter_natur_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_sevardheter_natur owner to geovis;


create table if not exists referensext.karta_skatepark
(
  id      serial not null
    constraint karta_skatepark_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_skatepark owner to geovis;


create table if not exists referensext.karta_skjutbana
(
  id      serial not null
    constraint karta_skjutbana_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_skjutbana owner to geovis;


create table if not exists referensext.karta_skola
(
  id      serial not null
    constraint karta_skola_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_skola owner to geovis;


create table if not exists referensext.karta_socialkontoret
(
  id      serial not null
    constraint karta_socialkontoret_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_socialkontoret owner to geovis;


create table if not exists referensext.karta_sophamtning
(
  id      serial not null
    constraint karta_sophamtning_pkey
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_sophamtning owner to geovis;


create table if not exists referensext.karta_spontanidrottsplats
(
  id      serial not null
    constraint karta_spontanidrottsplats_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_spontanidrottsplats owner to geovis;


create table if not exists referensext.karta_station
(
  id      serial not null
    constraint karta_station_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_station owner to geovis;


create table if not exists referensext.karta_systembolaget
(
  id      serial not null
    constraint karta_systembolaget_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_systembolaget owner to geovis;


create table if not exists referensext.karta_tagbiljettautomat
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

alter table referensext.karta_tagbiljettautomat owner to geovis;


create table if not exists referensext.karta_torghandel
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

alter table referensext.karta_torghandel owner to geovis;


create table if not exists referensext.karta_torghandel_polygon
(
  id      serial not null
    constraint karta_torghandel_polygon
    primary key,
  geom    geometry(MultiPolygon, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_torghandel_polygon owner to geovis;


create table if not exists referensext."karta_traffpunkt-aktivitetshuset"
(
  id      serial not null
    constraint "karta_traffpunkt-aktivitetshuset_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_traffpunkt-aktivitetshuset" owner to geovis;


create table if not exists referensext."karta_traffpunkt-aldre"
(
  id      serial not null
    constraint "karta_traffpunkt-aldre_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_traffpunkt-aldre" owner to geovis;


create table if not exists referensext."karta_traffpunkt-funktionsnedsatta"
(
  id      serial not null
    constraint "karta_traffpunkt-funktionsnedsatta_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_traffpunkt-funktionsnedsatta" owner to geovis;


create table if not exists referensext.karta_turistinformation
(
  id      serial not null
    constraint karta_turistinformation_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_turistinformation owner to geovis;


create table if not exists referensext.karta_traffpunkt_seniorer
(
  id      serial not null
    constraint karta_traffpunkt_seniorer_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_traffpunkt_seniorer owner to geovis;


create table if not exists referensext."karta_uppstallningsplats"
(
  id      serial not null
    constraint "karta_uppstallningsplats_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_uppstallningsplats" owner to geovis;


create table if not exists referensext."karta_uppstallningsplats-husbilar"
(
  id      serial not null
    constraint "karta_uppstallningsplats-husbilar_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_uppstallningsplats-husbilar" owner to geovis;


create table if not exists referensext.karta_utomhusgym
(
  id      serial not null
    constraint karta_utomhusgym_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_utomhusgym owner to geovis;


create table if not exists referensext.karta_vandringsleder
(
  id      serial not null
    constraint karta_vandringsleder_pkey
    primary key,
  geom    geometry(MultiLineString, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_vandringsleder owner to geovis;


create table if not exists referensext.karta_vardcentral
(
  id      serial not null
    constraint karta_vardcentral_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_vardcentral owner to geovis;


create table if not exists referensext."karta_viktigt-meddelande"
(
  id      serial not null
    constraint "karta_viktigt-meddelande_pkey"
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext."karta_viktigt-meddelande" owner to geovis;


create table if not exists referensext.karta_vuxenutbildning
(
  id      serial not null
    constraint karta_vuxenutbildning_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table referensext.karta_vuxenutbildning owner to geovis;
