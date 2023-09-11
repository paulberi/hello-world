\set ON_ERROR_STOP
\set ECHO all
BEGIN;
CREATE SCHEMA IF NOT EXISTS "matdatabas";


CREATE TABLE "matdatabas"."analys"( 
	"id" int NOT NULL,
	"matobjekt_id" int NOT NULL,
	"analys_datum" timestamp NOT NULL,
	"kommentar" varchar(500),
	"rapportor" varchar(60));

CREATE TABLE "matdatabas"."analys_bifogad_rapport"( 
	"analys_id" int NOT NULL,
	"bifogad_fil_id" int NOT NULL);

CREATE TABLE "matdatabas"."anvandare"( 
	"id" int NOT NULL,
	"namn" varchar(130),
	"foretag" varchar(60),
	"aktiv" boolean NOT NULL,
	"inloggnings_namn" varchar(130),
	"behorighet" smallint NOT NULL,
	"default_kartlager_id" int,
	"skicka_epost" boolean NOT NULL,
	"e_post" varchar(130),
	"inloggad_senast" timestamp,
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int,
	"authentication_key" varchar(20));

CREATE TABLE "matdatabas"."anvandargrupp"( 
	"id" int NOT NULL,
	"namn" varchar(60) NOT NULL,
	"beskrivning" varchar(500),
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."anvandargrupp_anvandare"( 
	"anvandar_id" int NOT NULL,
	"anvandargrupp_id" int NOT NULL);

CREATE TABLE "matdatabas"."bifogad_fil"( 
	"id" int NOT NULL,
	"mime_typ" varchar(130) NOT NULL,
	"filnamn" varchar(255) NOT NULL,
	"skapad_datum" timestamp,
	"fil" bytea NOT NULL,
	"thumbnail" bytea);

CREATE TABLE "matdatabas"."definition_matningstyp"( 
	"id" int NOT NULL,
	"matobjekt_typ" smallint NOT NULL,
	"namn" varchar(60) NOT NULL,
	"storhet" varchar(20),
	"enhet" varchar(12) NOT NULL,
	"decimaler" smallint NOT NULL,
	"beraknad_storhet" varchar(20),
	"beraknad_enhet" varchar(12),
	"beraknad_decimaler" smallint,
	"felkoder" varchar(500) NOT NULL,
	"graftyp" smallint NOT NULL,
	"automatisk_inrapportering" boolean NOT NULL,
	"beskrivning" varchar(500),
	"andringsbar" boolean NOT NULL,
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."gransvarde"( 
	"id" int NOT NULL,
	"matningstyp_id" int NOT NULL,
	"typ_av_kontroll" smallint NOT NULL,
	"gransvarde" double precision NOT NULL,
	"larmniva_id" int NOT NULL,
	"larm_till_anvandargrupp_id" int,
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."grupp"( 
	"id" int NOT NULL,
	"kategori" smallint NOT NULL,
	"namn" varchar(60) NOT NULL,
	"kartsymbol" smallint NOT NULL,
	"beskrivning" varchar(500),
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."grupp_matobjekt"( 
	"matobjekt_id" int NOT NULL,
	"grupp_id" int NOT NULL);

CREATE TABLE "matdatabas"."handelse"( 
	"id" int NOT NULL,
	"matobjekt_id" int NOT NULL,
	"benamning" varchar(60) NOT NULL,
	"beskrivning" varchar(500),
	"loggad_datum" timestamp NOT NULL,
	"loggad_av_id" int);

CREATE TABLE "matdatabas"."handelse_bifogad_bild"( 
	"handelse_id" int NOT NULL,
	"bifogad_fil_id" int NOT NULL);

CREATE TABLE "matdatabas"."kartlager"( 
	"id" int NOT NULL,
	"namn" varchar(60) NOT NULL,
	"grupp" varchar(60),
	"ordning" smallint NOT NULL,
	"visa" boolean NOT NULL,
	"beskrivning" varchar(500),
	"direktlank" boolean NOT NULL,
	"omrade_n1" numeric(10, 3),
	"omrade_e1" numeric(10, 3),
	"omrade_n2" numeric(10, 3),
	"omrade_e2" numeric(10, 3),
	"andringsbar" boolean NOT NULL,
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."kartlager_fil"( 
	"id" int NOT NULL,
	"kartlager_id" int NOT NULL,
	"filnamn" varchar(255) NOT NULL,
	"skapad_datum" timestamp,
	"fil" varchar NOT NULL);

CREATE TABLE "matdatabas"."larm"( 
	"id" bigint NOT NULL,
	"matobjekt_id" int NOT NULL,
	"gransvarde_id" int NOT NULL,
	"matning_id" bigint NOT NULL,
	"status" smallint NOT NULL);

CREATE TABLE "matdatabas"."larmniva"( 
	"id" int NOT NULL,
	"namn" varchar(60) NOT NULL,
	"beskrivning" varchar(130),
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."matning"( 
	"id" bigint NOT NULL,
	"matningstyp_id" int NOT NULL,
	"avlast_datum" timestamp NOT NULL,
	"avlast_varde" double precision,
	"inom_detektionsomrade" smallint NOT NULL,
	"beraknat_varde" double precision,
	"status" smallint NOT NULL,
	"kommentar" varchar(250),
	"felkod" varchar(60),
	"rapportor" varchar(60));

CREATE TABLE "matdatabas"."matningslogg"( 
	"id" bigint NOT NULL,
	"matning_id" bigint NOT NULL,
	"loggat_datum" timestamp NOT NULL,
	"loggat_av_id" int,
	"handelse" smallint NOT NULL,
	"beskrivning" varchar(500));

CREATE TABLE "matdatabas"."matningstyp"( 
	"id" int NOT NULL,
	"matobjekt_id" int,
	"definition_matningstyp_id" int NOT NULL,
	"matansvarig_anvandargrupp_id" int,
	"matintervall_antal_ganger" smallint NOT NULL,
	"matintervall_tidsenhet" smallint NOT NULL,
	"aktiv" boolean NOT NULL,
	"paminnelse_dagar" smallint NOT NULL,
	"instrument" varchar(60),
	"granskas_automatiskt" boolean NOT NULL,
	"granskas_min" double precision,
	"granskas_max" double precision,
	"berakning_konstant" double precision,
	"berakning_referensniva" double precision,
	"max_pejlbart_djup" double precision,
	"fixpunkt" varchar(20),
	"enhet" varchar(12) NOT NULL,
	"decimaler" smallint NOT NULL,
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."matobjekt"( 
	"id" int NOT NULL,
	"typ" smallint NOT NULL,
	"namn" varchar(60) NOT NULL,
	"aktiv" boolean NOT NULL,
	"kontrollprogram" boolean NOT NULL,
	"pos_n" numeric(10, 3) NOT NULL,
	"pos_e" numeric(10, 3) NOT NULL,
	"fastighet" varchar(60),
	"lage" varchar(500),
	"bifogad_bild_id" int,
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."matobjekt_bifogat_dokument"( 
	"matobjekt_id" int NOT NULL,
	"bifogad_fil_id" int NOT NULL);

CREATE TABLE "matdatabas"."matrunda"( 
	"id" int NOT NULL,
	"namn" varchar(60) NOT NULL,
	"aktiv" boolean NOT NULL,
	"beskrivning" varchar(500),
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."matrunda_matningstyp"( 
	"matrunda_id" int NOT NULL,
	"matningstyp_id" int NOT NULL,
	"ordning" smallint NOT NULL);

CREATE TABLE "matdatabas"."meddelanden_startsida"( 
	"id" int NOT NULL,
	"datum" timestamp NOT NULL,
	"rubrik" varchar(130) NOT NULL,
	"url" varchar(255),
	"meddelande" varchar(500) NOT NULL,
	"skapad_datum" timestamp,
	"andrad_datum" timestamp(7),
	"andrad_av_id" int);

CREATE TABLE "matdatabas"."systemlogg"( 
	"id" bigint NOT NULL,
	"loggat_datum" timestamp NOT NULL,
	"loggat_av_id" int,
	"handelse" smallint NOT NULL,
	"beskrivning" varchar(500));

COMMIT;
