ALTER SCHEMA tranasext OWNER TO geovis;

drop sequence if exists tranasext.adressplatser_tranas_gid_seq cascade;
drop table if exists tranasext.fir_adressplatser_tranas;

drop sequence if exists tranasext.kommunala_bolags_mark_gid_seq cascade;
drop table if exists tranasext.fir_kommunala_bolags_mark;

drop sequence if exists tranasext.kommunalagd_mark_gid_seq cascade;
drop table if exists tranasext.fir_kommunalagd_mark;

drop sequence if exists tranasext.sopdistrikttatorten_sweref99tm_gid_seq cascade;
drop table if exists tranasext.geoa_sopdistrikttatorten_sweref99tm;

drop sequence if exists tranasext.vattenskyddsomr_99tm_gid_seq cascade;
drop table if exists tranasext.geoa_vattenskyddsomr_99tm;

drop sequence if exists tranasext.kladdlager_id_seq cascade;
drop table if exists tranasext.kladdlager;

drop sequence if exists tranasext."strandskydd tranas fr flan_region_gid_seq" cascade;
drop table if exists tranasext."lansstyrelsen_strandskydd tranas fr flan_region";

drop sequence if exists tranasext.sopdistrikttranas_kmn_gid_seq cascade;
drop table if exists tranasext.tranas_kommun_sopdistrikttranas_kmn;

drop sequence if exists tranasext.svarta_50_ars_flode_gid_seq cascade;
drop table if exists tranasext.tranas_kommun_svarta_50_ars_flode;

drop sequence if exists tranasext.svartan_100_ars_flode_gid_seq cascade;
drop table if exists tranasext.tranas_kommun_svartan_100_ars_flode;

drop sequence if exists tranasext.svartan_bhf_gid_seq cascade;
drop table if exists tranasext.tranas_kommun_svartan_bhf;


-- Create tables

drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq33 cascade;
drop table if exists tranasext.karta_apotek;

create table if not exists tranasext.karta_apotek
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

alter table tranasext.karta_apotek owner to geovis;

drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq27 cascade;
drop table if exists tranasext.karta_atervinningscentral;

create table if not exists tranasext.karta_atervinningscentral
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

alter table tranasext.karta_atervinningscentral owner to geovis;

drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq28 cascade;
drop table if exists tranasext.karta_atervinningsstation;

create table if not exists tranasext.karta_atervinningsstation
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

alter table tranasext.karta_atervinningsstation owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq20 cascade;
drop table if exists tranasext.karta_badplats;

create table if not exists tranasext.karta_badplats
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

alter table tranasext.karta_badplats owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq41 cascade;
drop table if exists tranasext.karta_bankomat;

create table if not exists tranasext.karta_bankomat
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

alter table tranasext.karta_bankomat owner to geovis;

drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq56 cascade;
drop table if exists tranasext.karta_basketplan;

create table if not exists tranasext.karta_basketplan
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

alter table tranasext.karta_basketplan owner to geovis;


drop sequence if exists tranasext.batplatser_aneby_id_seq cascade;
drop table if exists tranasext.karta_batplatser;

create table if not exists tranasext.karta_batplatser
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

alter table tranasext.karta_batplatser owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq57 cascade;
drop table if exists tranasext.karta_beachvolley;

create table if not exists tranasext.karta_beachvolley
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

alter table tranasext.karta_beachvolley owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq58 cascade;
drop table if exists tranasext.karta_begravningsplats;

create table if not exists tranasext.karta_begravningsplats
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

alter table tranasext.karta_begravningsplats owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_linje_id_seq cascade;
drop table if exists tranasext.karta_belaggningsarbete;

create table if not exists tranasext.karta_belaggningsarbete
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

alter table tranasext.karta_belaggningsarbete owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq13 cascade;
drop table if exists tranasext.karta_bibliotek;

create table if not exists tranasext.karta_bibliotek
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

alter table tranasext.karta_bibliotek owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq14 cascade;
drop table if exists tranasext.karta_biograf;

create table if not exists tranasext.karta_biograf
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

alter table tranasext.karta_biograf owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq45 cascade;
drop table if exists tranasext.karta_boende;

create table if not exists tranasext.karta_boende
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

alter table tranasext.karta_boende owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq8 cascade;
drop table if exists tranasext.karta_bostadstomter_ejbebyggda;

create table if not exists tranasext.karta_bostadstomter_ejbebyggda
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

alter table tranasext.karta_bostadstomter_ejbebyggda owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq59 cascade;
drop table if exists tranasext.karta_boulebanor;

create table if not exists tranasext.karta_boulebanor
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

alter table tranasext.karta_boulebanor owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq67 cascade;
drop table if exists tranasext.karta_bowling;

create table if not exists tranasext.karta_bowling
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

alter table tranasext.karta_bowling owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq53 cascade;
drop table if exists tranasext.karta_busshallplats;

create table if not exists tranasext.karta_busshallplats
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

alter table tranasext.karta_busshallplats owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq60 cascade;
drop table if exists tranasext.karta_busskort;

create table if not exists tranasext.karta_busskort
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

alter table tranasext.karta_busskort owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq31 cascade;
drop table if exists tranasext.karta_busstation;

create table if not exists tranasext.karta_busstation
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

alter table tranasext.karta_busstation owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq70 cascade;
drop table if exists tranasext.karta_bygdegardar;

create table if not exists tranasext.karta_bygdegardar
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

alter table tranasext.karta_bygdegardar owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq50 cascade;
drop table if exists tranasext.karta_cafe;

create table if not exists tranasext.karta_cafe
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

alter table tranasext.karta_cafe owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_linje_id_seq2 cascade;
drop table if exists tranasext.karta_cykelleder;

create table if not exists tranasext.karta_cykelleder
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

alter table tranasext.karta_cykelleder owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_linje_id_seq3 cascade;
drop table if exists tranasext.karta_cykelvagar;

create table if not exists tranasext.karta_cykelvagar
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

alter table tranasext.karta_cykelvagar owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq9 cascade;
drop table if exists tranasext."karta_daglig-verksamhet";

create table if not exists tranasext."karta_daglig-verksamhet"
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

alter table tranasext."karta_daglig-verksamhet" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq cascade;
drop table if exists tranasext."karta_detaljplaner-gallande";

create table if not exists tranasext."karta_detaljplaner-gallande"
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

alter table tranasext."karta_detaljplaner-gallande" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq1 cascade;
drop table if exists tranasext."karta_detaljplaner-pagaende";

create table if not exists tranasext."karta_detaljplaner-pagaende"
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

alter table tranasext."karta_detaljplaner-pagaende" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq52 cascade;
drop table if exists tranasext.karta_drivmedelsstation;

create table if not exists tranasext.karta_drivmedelsstation
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

alter table tranasext.karta_drivmedelsstation owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq54 cascade;
drop table if exists tranasext.karta_fiske;

create table if not exists tranasext.karta_fiske
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

alter table tranasext.karta_fiske owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq55 cascade;
drop table if exists tranasext.karta_fiskekort;

create table if not exists tranasext.karta_fiskekort
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

alter table tranasext.karta_fiskekort owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq15 cascade;
drop table if exists tranasext."karta_folkets-park";

create table if not exists tranasext."karta_folkets-park"
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

alter table tranasext."karta_folkets-park" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq36 cascade;
drop table if exists tranasext.karta_folktandvard;

create table if not exists tranasext.karta_folktandvard
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

alter table tranasext.karta_folktandvard owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq1 cascade;
drop table if exists tranasext.karta_forskola;

create table if not exists tranasext.karta_forskola
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

alter table tranasext.karta_forskola owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq17 cascade;
drop table if exists tranasext.karta_fotbollsplaner;

create table if not exists tranasext.karta_fotbollsplaner
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

alter table tranasext.karta_fotbollsplaner owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq18 cascade;
drop table if exists tranasext.karta_friidrottsanl;

create table if not exists tranasext.karta_friidrottsanl
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

alter table tranasext.karta_friidrottsanl owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq21 cascade;
drop table if exists tranasext.karta_fritidsgard;

create table if not exists tranasext.karta_fritidsgard
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

alter table tranasext.karta_fritidsgard owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq40 cascade;
drop table if exists tranasext.karta_fritidshem;

create table if not exists tranasext.karta_fritidshem
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

alter table tranasext.karta_fritidshem owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq16 cascade;
drop table if exists tranasext.karta_golfbanor;

create table if not exists tranasext.karta_golfbanor
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

alter table tranasext.karta_golfbanor owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq2 cascade;
drop table if exists tranasext."karta_grav-uppstallningstillstand";

create table if not exists tranasext."karta_grav-uppstallningstillstand"
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

alter table tranasext."karta_grav-uppstallningstillstand" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq74 cascade;
drop table if exists tranasext.karta_gym;

create table if not exists tranasext.karta_gym
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

alter table tranasext.karta_gym owner to geovis;


drop sequence if exists tranasext.karta_gymnasium_id_seq cascade;
drop table if exists tranasext.karta_gymnasium;

create table if not exists tranasext.karta_gymnasium
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

alter table tranasext.karta_gymnasium owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq39 cascade;
drop table if exists tranasext.karta_hamn;

create table if not exists tranasext.karta_hamn
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

alter table tranasext.karta_hamn owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq71 cascade;
drop table if exists tranasext.karta_hembygdsforening;

create table if not exists tranasext.karta_hembygdsforening
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

alter table tranasext.karta_hembygdsforening owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq43 cascade;
drop table if exists tranasext.karta_hjartstartare;

create table if not exists tranasext.karta_hjartstartare
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

alter table tranasext.karta_hjartstartare owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq73 cascade;
drop table if exists tranasext.karta_hundklubb;

create table if not exists tranasext.karta_hundklubb
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

alter table tranasext.karta_hundklubb owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq76 cascade;
drop table if exists tranasext.karta_hundpasar;

create table if not exists tranasext.karta_hundpasar
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

alter table tranasext.karta_hundpasar owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq19 cascade;
drop table if exists tranasext.karta_idrottshall;

create table if not exists tranasext.karta_idrottshall
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

alter table tranasext.karta_idrottshall owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq2 cascade;
drop table if exists tranasext.karta_isbanor;

create table if not exists tranasext.karta_isbanor
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

alter table tranasext.karta_isbanor owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq82 cascade;
drop table if exists tranasext.karta_kolonilotter;

create table if not exists tranasext.karta_kolonilotter
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

alter table tranasext.karta_kolonilotter owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq7 cascade;
drop table if exists tranasext."karta_kommunagd-mark";

create table if not exists tranasext."karta_kommunagd-mark"
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

alter table tranasext."karta_kommunagd-mark" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq7 cascade;
drop table if exists tranasext.karta_kommunkontor;

create table if not exists tranasext.karta_kommunkontor
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

alter table tranasext.karta_kommunkontor owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq4 cascade;
drop table if exists tranasext.karta_konsumentradgivning;

create table if not exists tranasext.karta_konsumentradgivning
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

alter table tranasext.karta_konsumentradgivning owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq69 cascade;
drop table if exists tranasext.karta_kulturforening;

create table if not exists tranasext.karta_kulturforening
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

alter table tranasext.karta_kulturforening owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq78 cascade;
drop table if exists tranasext.karta_kulturskolan;

create table if not exists tranasext.karta_kulturskolan
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

alter table tranasext.karta_kulturskolan owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq34 cascade;
drop table if exists tranasext.karta_laddstationer;

create table if not exists tranasext.karta_laddstationer
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

alter table tranasext.karta_laddstationer owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq61 cascade;
drop table if exists tranasext.karta_langdskidspar;

create table if not exists tranasext.karta_langdskidspar
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

alter table tranasext.karta_langdskidspar owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq79 cascade;
drop table if exists tranasext.karta_latrinkarl;

create table if not exists tranasext.karta_latrinkarl
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

alter table tranasext.karta_latrinkarl owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq6 cascade;
drop table if exists tranasext."karta_ledig-industrimark";

create table if not exists tranasext."karta_ledig-industrimark"
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

alter table tranasext."karta_ledig-industrimark" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq9 cascade;
drop table if exists tranasext."karta_ledig-industrimark_privatagd";

create table if not exists tranasext."karta_ledig-industrimark_privatagd"
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

alter table tranasext."karta_ledig-industrimark_privatagd" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq75 cascade;
drop table if exists tranasext."karta_lediga-bostadstomter";

create table if not exists tranasext."karta_lediga-bostadstomter"
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

alter table tranasext."karta_lediga-bostadstomter" owner to geovis;


drop sequence if exists tranasext.karta_lediga_tomter_flerfamiljshus_id_seq cascade;
drop table if exists tranasext.karta_lediga_tomter_flerfamiljshus;

create table if not exists tranasext.karta_lediga_tomter_flerfamiljshus
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

alter table tranasext.karta_lediga_tomter_flerfamiljshus owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq35 cascade;
drop table if exists tranasext.karta_lekplats;

create table if not exists tranasext.karta_lekplats
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

alter table tranasext.karta_lekplats owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq51 cascade;
drop table if exists tranasext.karta_livsmedelsbutik;

create table if not exists tranasext.karta_livsmedelsbutik
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

alter table tranasext.karta_livsmedelsbutik owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq22 cascade;
drop table if exists tranasext.karta_motionsanl;

create table if not exists tranasext.karta_motionsanl
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

alter table tranasext.karta_motionsanl owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_linje_id_seq4 cascade;
drop table if exists tranasext.karta_motionsspar_elljusspar;

create table if not exists tranasext.karta_motionsspar_elljusspar
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

alter table tranasext.karta_motionsspar_elljusspar owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_linje_id_seq5 cascade;
drop table if exists tranasext.karta_motionsspar_uppmarkta;

create table if not exists tranasext.karta_motionsspar_uppmarkta
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

alter table tranasext.karta_motionsspar_uppmarkta owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq25 cascade;
drop table if exists tranasext.karta_motorcrossbana;

create table if not exists tranasext.karta_motorcrossbana
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

alter table tranasext.karta_motorcrossbana owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq12 cascade;
drop table if exists tranasext.karta_naturreservat;

create table if not exists tranasext.karta_naturreservat
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

alter table tranasext.karta_naturreservat owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq3 cascade;
drop table if exists tranasext."karta_naturvardsprogram-objekt";

create table if not exists tranasext."karta_naturvardsprogram-objekt"
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

alter table tranasext."karta_naturvardsprogram-objekt" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq10 cascade;
drop table if exists tranasext."karta_naturvardsprogram-omraden";

create table if not exists tranasext."karta_naturvardsprogram-omraden"
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

alter table tranasext."karta_naturvardsprogram-omraden" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq38 cascade;
drop table if exists tranasext.karta_obo;

create table if not exists tranasext.karta_obo
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

alter table tranasext.karta_obo owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq68 cascade;
drop table if exists tranasext.karta_offentligatoaletter;

create table if not exists tranasext.karta_offentligatoaletter
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

alter table tranasext.karta_offentligatoaletter owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq10 cascade;
drop table if exists tranasext."karta_okad-sysselsattning";

create table if not exists tranasext."karta_okad-sysselsattning"
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

alter table tranasext."karta_okad-sysselsattning" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq81 cascade;
drop table if exists tranasext.karta_park;

create table if not exists tranasext.karta_park
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

alter table tranasext.karta_park owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq29 cascade;
drop table if exists tranasext."karta_parkering-pendlar";

create table if not exists tranasext."karta_parkering-pendlar"
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

alter table tranasext."karta_parkering-pendlar" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq62 cascade;
drop table if exists tranasext.karta_parkering_rorelsehindrade;

create table if not exists tranasext.karta_parkering_rorelsehindrade
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

alter table tranasext.karta_parkering_rorelsehindrade owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq30 cascade;
drop table if exists tranasext.karta_parkeringsplatser;

create table if not exists tranasext.karta_parkeringsplatser
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

alter table tranasext.karta_parkeringsplatser owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq3 cascade;
drop table if exists tranasext.karta_polis;

create table if not exists tranasext.karta_polis
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

alter table tranasext.karta_polis owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq63 cascade;
drop table if exists tranasext.karta_postombud;

create table if not exists tranasext.karta_postombud
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

alter table tranasext.karta_postombud owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq23 cascade;
drop table if exists tranasext.karta_racketanl;

create table if not exists tranasext.karta_racketanl
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

alter table tranasext.karta_racketanl owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq5 cascade;
drop table if exists tranasext.karta_raddningstjanst;

create table if not exists tranasext.karta_raddningstjanst
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

alter table tranasext.karta_raddningstjanst owner to geovis;


drop sequence if exists tranasext.karta_reserverade_tomter_id_seq cascade;
drop table if exists tranasext.karta_reserverade_tomter;

create table if not exists tranasext.karta_reserverade_tomter
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

alter table tranasext.karta_reserverade_tomter owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq49 cascade;
drop table if exists tranasext.karta_restauranger;

create table if not exists tranasext.karta_restauranger
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

alter table tranasext.karta_restauranger owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq26 cascade;
drop table if exists tranasext.karta_ridanl;

create table if not exists tranasext.karta_ridanl
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

alter table tranasext.karta_ridanl owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq77 cascade;
drop table if exists tranasext.karta_samlingslokal;

create table if not exists tranasext.karta_samlingslokal
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

alter table tranasext.karta_samlingslokal owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq48 cascade;
drop table if exists tranasext.karta_sevardheter_besoksmal;

create table if not exists tranasext.karta_sevardheter_besoksmal
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

alter table tranasext.karta_sevardheter_besoksmal owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq47 cascade;
drop table if exists tranasext.karta_sevardheter_kultur;

create table if not exists tranasext.karta_sevardheter_kultur
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

alter table tranasext.karta_sevardheter_kultur owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq46 cascade;
drop table if exists tranasext.karta_sevardheter_natur;

create table if not exists tranasext.karta_sevardheter_natur
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

alter table tranasext.karta_sevardheter_natur owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq24 cascade;
drop table if exists tranasext.karta_skatepark;

create table if not exists tranasext.karta_skatepark
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

alter table tranasext.karta_skatepark owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq64 cascade;
drop table if exists tranasext.karta_skjutbana;

create table if not exists tranasext.karta_skjutbana
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

alter table tranasext.karta_skjutbana owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq cascade;
drop table if exists tranasext.karta_skola;

create table if not exists tranasext.karta_skola
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

alter table tranasext.karta_skola owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq37 cascade;
drop table if exists tranasext.karta_socialkontoret;

create table if not exists tranasext.karta_socialkontoret
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

alter table tranasext.karta_socialkontoret owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_yta_id_seq11 cascade;
drop table if exists tranasext.karta_sophamtning;

create table if not exists tranasext.karta_sophamtning
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

alter table tranasext.karta_sophamtning owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq72 cascade;
drop table if exists tranasext.karta_spontanidrottsplats;

create table if not exists tranasext.karta_spontanidrottsplats
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

alter table tranasext.karta_spontanidrottsplats owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq80 cascade;
drop table if exists tranasext.karta_station;

create table if not exists tranasext.karta_station
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

alter table tranasext.karta_station owner to geovis;

drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq65 cascade;
drop table if exists tranasext.karta_systembolaget;

create table if not exists tranasext.karta_systembolaget
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

alter table tranasext.karta_systembolaget owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq83 cascade;
drop table if exists tranasext.karta_torghandel;

create table if not exists tranasext.karta_torghandel
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

alter table tranasext.karta_torghandel owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq12 cascade;
drop table if exists tranasext."karta_traffpunkt-aktivitetshuset";

create table if not exists tranasext."karta_traffpunkt-aktivitetshuset"
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

alter table tranasext."karta_traffpunkt-aktivitetshuset" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq11 cascade;
drop table if exists tranasext."karta_traffpunkt-aldre";

create table if not exists tranasext."karta_traffpunkt-aldre"
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

alter table tranasext."karta_traffpunkt-aldre" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq42 cascade;
drop table if exists tranasext."karta_traffpunkt-funktionsnedsatta";

create table if not exists tranasext."karta_traffpunkt-funktionsnedsatta"
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

alter table tranasext."karta_traffpunkt-funktionsnedsatta" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq44 cascade;
drop table if exists tranasext.karta_turistinformation;

create table if not exists tranasext.karta_turistinformation
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

alter table tranasext.karta_turistinformation owner to geovis;


drop sequence if exists tranasext.karta_traffpunkt_seniorer_id_seq cascade;
drop table if exists tranasext.karta_traffpunkt_seniorer;

create table if not exists tranasext.karta_traffpunkt_seniorer
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

alter table tranasext.karta_traffpunkt_seniorer owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq32 cascade;
drop table if exists tranasext."karta_uppstallningsplats";

create table if not exists tranasext."karta_uppstallningsplats"
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

alter table tranasext."karta_uppstallningsplats" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq32 cascade;
drop table if exists tranasext."karta_uppstallningsplats-husbilar";

create table if not exists tranasext."karta_uppstallningsplats-husbilar"
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

alter table tranasext."karta_uppstallningsplats-husbilar" owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq66 cascade;
drop table if exists tranasext.karta_utomhusgym;

create table if not exists tranasext.karta_utomhusgym
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

alter table tranasext.karta_utomhusgym owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_linje_id_seq1 cascade;
drop table if exists tranasext.karta_vandringsleder;

create table if not exists tranasext.karta_vandringsleder
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

alter table tranasext.karta_vandringsleder owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq6 cascade;
drop table if exists tranasext.karta_vardcentral;

create table if not exists tranasext.karta_vardcentral
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

alter table tranasext.karta_vardcentral owner to geovis;


drop sequence if exists tranasext.standard_kommunkarta_punkt_id_seq8 cascade;
drop table if exists tranasext."karta_viktigt-meddelande";

create table if not exists tranasext."karta_viktigt-meddelande"
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

alter table tranasext."karta_viktigt-meddelande" owner to geovis;


drop sequence if exists tranasext.karta_vuxenutbildning_id_seq cascade;
drop table if exists tranasext.karta_vuxenutbildning;

create table if not exists tranasext.karta_vuxenutbildning
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

alter table tranasext.karta_vuxenutbildning owner to geovis;
