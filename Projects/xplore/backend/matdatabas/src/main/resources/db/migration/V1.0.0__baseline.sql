--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5
-- Dumped by pg_dump version 11.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
-- SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: matdatabas; Type: SCHEMA; Schema: -; Owner: matdatabas
--

-- CREATE SCHEMA IF NOT EXISTS matdatabas;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: analys; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE analys (
    id integer NOT NULL,
    matobjekt_id integer NOT NULL,
    analys_datum timestamp without time zone NOT NULL,
    kommentar character varying(500),
    rapportor character varying(60)
);


--
-- Name: analys_bifogad_rapport; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE analys_bifogad_rapport (
    analys_id integer NOT NULL,
    bifogad_fil_id integer NOT NULL
);



--
-- Name: analys_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE analys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: analys_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--


--
-- Name: anvandare; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE anvandare (
    id integer NOT NULL,
    namn character varying(130),
    foretag character varying(60),
    aktiv boolean DEFAULT true NOT NULL,
    inloggnings_namn character varying(130),
    behorighet smallint NOT NULL,
    default_kartlager_id integer,
    skicka_epost boolean DEFAULT false NOT NULL,
    e_post character varying(130),
    inloggad_senast timestamp without time zone,
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer,
    authentication_key character varying(20)
);



--
-- Name: anvandare_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE anvandare_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: anvandare_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE anvandare_id_seq OWNED BY anvandare.id;


--
-- Name: anvandargrupp; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE anvandargrupp (
    id integer NOT NULL,
    namn character varying(60) NOT NULL,
    beskrivning character varying(500),
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: anvandargrupp_anvandare; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE anvandargrupp_anvandare (
    anvandar_id integer NOT NULL,
    anvandargrupp_id integer NOT NULL
);




--
-- Name: anvandargrupp_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE anvandargrupp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: anvandargrupp_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE anvandargrupp_id_seq OWNED BY anvandargrupp.id;


--
-- Name: bifogad_fil; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE bifogad_fil (
    id integer NOT NULL,
    mime_typ character varying(130) NOT NULL,
    filnamn character varying(255) NOT NULL,
    skapad_datum timestamp without time zone,
    fil bytea NOT NULL,
    thumbnail bytea
);




--
-- Name: bifogad_fil_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE bifogad_fil_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: bifogad_fil_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE bifogad_fil_id_seq OWNED BY bifogad_fil.id;


--
-- Name: definition_matningstyp; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE definition_matningstyp (
    id integer NOT NULL,
    matobjekt_typ smallint NOT NULL,
    namn character varying(60) NOT NULL,
    storhet character varying(20),
    enhet character varying(12) NOT NULL,
    decimaler smallint DEFAULT 4 NOT NULL,
    beraknad_storhet character varying(20),
    beraknad_enhet character varying(12),
    beraknad_decimaler smallint,
    felkoder character varying(500) DEFAULT 'Hinder, Annat fel'::character varying NOT NULL,
    graftyp smallint DEFAULT 0 NOT NULL,
    automatisk_inrapportering boolean NOT NULL,
    beskrivning character varying(500),
    andringsbar boolean NOT NULL,
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: definition_matningstyp_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE definition_matningstyp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: definition_matningstyp_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE definition_matningstyp_id_seq OWNED BY definition_matningstyp.id;


--
-- Name: gransvarde; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE gransvarde (
    id integer NOT NULL,
    matningstyp_id integer NOT NULL,
    typ_av_kontroll smallint NOT NULL,
    gransvarde double precision NOT NULL,
    larmniva_id integer NOT NULL,
    larm_till_anvandargrupp_id integer,
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: gransvarde_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE gransvarde_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: gransvarde_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE gransvarde_id_seq OWNED BY gransvarde.id;


--
-- Name: grupp; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE grupp (
    id integer NOT NULL,
    kategori smallint NOT NULL,
    namn character varying(60) NOT NULL,
    kartsymbol smallint DEFAULT 0 NOT NULL,
    beskrivning character varying(500),
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: grupp_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE grupp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: grupp_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE grupp_id_seq OWNED BY grupp.id;


--
-- Name: grupp_matobjekt; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE grupp_matobjekt (
    matobjekt_id integer NOT NULL,
    grupp_id integer NOT NULL
);




--
-- Name: handelse; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE handelse (
    id integer NOT NULL,
    matobjekt_id integer NOT NULL,
    benamning character varying(60) NOT NULL,
    beskrivning character varying(500),
    loggad_datum timestamp without time zone NOT NULL,
    loggad_av_id integer
);




--
-- Name: handelse_bifogad_bild; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE handelse_bifogad_bild (
    handelse_id integer NOT NULL,
    bifogad_fil_id integer NOT NULL
);




--
-- Name: handelse_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE handelse_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: handelse_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE handelse_id_seq OWNED BY handelse.id;


--
-- Name: kartlager; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE kartlager (
    id integer NOT NULL,
    namn character varying(60) NOT NULL,
    grupp character varying(60),
    ordning smallint NOT NULL,
    visa boolean DEFAULT true NOT NULL,
    beskrivning character varying(500),
    direktlank boolean DEFAULT false NOT NULL,
    omrade_n1 numeric(10,3),
    omrade_e1 numeric(10,3),
    omrade_n2 numeric(10,3),
    omrade_e2 numeric(10,3),
    andringsbar boolean NOT NULL,
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: kartlager_fil; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE kartlager_fil (
    id integer NOT NULL,
    kartlager_id integer NOT NULL,
    filnamn character varying(255) NOT NULL,
    skapad_datum timestamp without time zone,
    fil character varying NOT NULL
);




--
-- Name: kartlager_fil_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE kartlager_fil_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: kartlager_fil_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE kartlager_fil_id_seq OWNED BY kartlager_fil.id;


--
-- Name: kartlager_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE kartlager_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: kartlager_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE kartlager_id_seq OWNED BY kartlager.id;


--
-- Name: larm; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE larm (
    id bigint NOT NULL,
    matobjekt_id integer NOT NULL,
    gransvarde_id integer NOT NULL,
    matning_id bigint NOT NULL,
    status smallint DEFAULT 0 NOT NULL
);




--
-- Name: larm_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE larm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: larm_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE larm_id_seq OWNED BY larm.id;


--
-- Name: larmniva; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE larmniva (
    id integer NOT NULL,
    namn character varying(60) NOT NULL,
    beskrivning character varying(130),
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: larmniva_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE larmniva_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: larmniva_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE larmniva_id_seq OWNED BY larmniva.id;


--
-- Name: matning; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE matning (
    id bigint NOT NULL,
    matningstyp_id integer NOT NULL,
    avlast_datum timestamp without time zone NOT NULL,
    avlast_varde double precision,
    inom_detektionsomrade smallint DEFAULT 1 NOT NULL,
    beraknat_varde double precision,
    status smallint DEFAULT 0 NOT NULL,
    kommentar character varying(250),
    felkod character varying(60),
    rapportor character varying(60)
);




--
-- Name: matning_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE matning_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: matning_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE matning_id_seq OWNED BY matning.id;


--
-- Name: matningslogg; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE matningslogg (
    id bigint NOT NULL,
    matning_id bigint NOT NULL,
    loggat_datum timestamp without time zone NOT NULL,
    loggat_av_id integer,
    handelse smallint NOT NULL,
    beskrivning character varying(500)
);




--
-- Name: matningslogg_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE matningslogg_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: matningslogg_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE matningslogg_id_seq OWNED BY matningslogg.id;


--
-- Name: matningstyp; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE matningstyp (
    id integer NOT NULL,
    matobjekt_id integer,
    definition_matningstyp_id integer NOT NULL,
    matansvarig_anvandargrupp_id integer,
    matintervall_antal_ganger smallint NOT NULL,
    matintervall_tidsenhet smallint NOT NULL,
    aktiv boolean DEFAULT true NOT NULL,
    paminnelse_dagar smallint DEFAULT 3 NOT NULL,
    instrument character varying(60),
    granskas_automatiskt boolean DEFAULT false NOT NULL,
    granskas_min double precision,
    granskas_max double precision,
    berakning_konstant double precision,
    berakning_referensniva double precision,
    max_pejlbart_djup double precision,
    fixpunkt character varying(20),
    enhet character varying(12) NOT NULL,
    decimaler smallint NOT NULL,
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: matningstyp_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE matningstyp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: matningstyp_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE matningstyp_id_seq OWNED BY matningstyp.id;


--
-- Name: matobjekt; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE matobjekt (
    id integer NOT NULL,
    typ smallint NOT NULL,
    namn character varying(60) NOT NULL,
    aktiv boolean DEFAULT true NOT NULL,
    kontrollprogram boolean DEFAULT false NOT NULL,
    pos_n numeric(10,3) NOT NULL,
    pos_e numeric(10,3) NOT NULL,
    fastighet character varying(60),
    lage character varying(500),
    bifogad_bild_id integer,
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: matobjekt_bifogat_dokument; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE matobjekt_bifogat_dokument (
    matobjekt_id integer NOT NULL,
    bifogad_fil_id integer NOT NULL
);




--
-- Name: matobjekt_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE matobjekt_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: matobjekt_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE matobjekt_id_seq OWNED BY matobjekt.id;


--
-- Name: matrunda; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE matrunda (
    id integer NOT NULL,
    namn character varying(60) NOT NULL,
    aktiv boolean DEFAULT false NOT NULL,
    beskrivning character varying(500),
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: matrunda_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE matrunda_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: matrunda_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE matrunda_id_seq OWNED BY matrunda.id;


--
-- Name: matrunda_matningstyp; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE matrunda_matningstyp (
    matrunda_id integer NOT NULL,
    matningstyp_id integer NOT NULL,
    ordning smallint DEFAULT 0 NOT NULL
);




--
-- Name: meddelanden_startsida; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE meddelanden_startsida (
    id integer NOT NULL,
    datum timestamp without time zone NOT NULL,
    rubrik character varying(130) NOT NULL,
    url character varying(255),
    meddelande character varying(500) NOT NULL,
    skapad_datum timestamp without time zone,
    andrad_datum timestamp(6) without time zone,
    andrad_av_id integer
);




--
-- Name: meddelanden_startsida_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE meddelanden_startsida_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: meddelanden_startsida_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE meddelanden_startsida_id_seq OWNED BY meddelanden_startsida.id;


--
-- Name: systemlogg; Type: TABLE; Schema: matdatabas; Owner: matdatabas
--

CREATE TABLE systemlogg (
    id bigint NOT NULL,
    loggat_datum timestamp without time zone NOT NULL,
    loggat_av_id integer,
    handelse smallint NOT NULL,
    beskrivning character varying(500)
);




--
-- Name: systemlogg_id_seq; Type: SEQUENCE; Schema: matdatabas; Owner: matdatabas
--

CREATE SEQUENCE systemlogg_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: systemlogg_id_seq; Type: SEQUENCE OWNED BY; Schema: matdatabas; Owner: matdatabas
--

ALTER SEQUENCE systemlogg_id_seq OWNED BY systemlogg.id;


--
-- Name: analys id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY analys ALTER COLUMN id SET DEFAULT nextval('analys_id_seq'::regclass);


--
-- Name: anvandare id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandare ALTER COLUMN id SET DEFAULT nextval('anvandare_id_seq'::regclass);


--
-- Name: anvandargrupp id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandargrupp ALTER COLUMN id SET DEFAULT nextval('anvandargrupp_id_seq'::regclass);


--
-- Name: bifogad_fil id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY bifogad_fil ALTER COLUMN id SET DEFAULT nextval('bifogad_fil_id_seq'::regclass);


--
-- Name: definition_matningstyp id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY definition_matningstyp ALTER COLUMN id SET DEFAULT nextval('definition_matningstyp_id_seq'::regclass);


--
-- Name: gransvarde id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY gransvarde ALTER COLUMN id SET DEFAULT nextval('gransvarde_id_seq'::regclass);


--
-- Name: grupp id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY grupp ALTER COLUMN id SET DEFAULT nextval('grupp_id_seq'::regclass);


--
-- Name: handelse id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY handelse ALTER COLUMN id SET DEFAULT nextval('handelse_id_seq'::regclass);


--
-- Name: kartlager id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY kartlager ALTER COLUMN id SET DEFAULT nextval('kartlager_id_seq'::regclass);


--
-- Name: kartlager_fil id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY kartlager_fil ALTER COLUMN id SET DEFAULT nextval('kartlager_fil_id_seq'::regclass);


--
-- Name: larm id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY larm ALTER COLUMN id SET DEFAULT nextval('larm_id_seq'::regclass);


--
-- Name: larmniva id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY larmniva ALTER COLUMN id SET DEFAULT nextval('larmniva_id_seq'::regclass);


--
-- Name: matning id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matning ALTER COLUMN id SET DEFAULT nextval('matning_id_seq'::regclass);


--
-- Name: matningslogg id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matningslogg ALTER COLUMN id SET DEFAULT nextval('matningslogg_id_seq'::regclass);


--
-- Name: matningstyp id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matningstyp ALTER COLUMN id SET DEFAULT nextval('matningstyp_id_seq'::regclass);


--
-- Name: matobjekt id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matobjekt ALTER COLUMN id SET DEFAULT nextval('matobjekt_id_seq'::regclass);


--
-- Name: matrunda id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matrunda ALTER COLUMN id SET DEFAULT nextval('matrunda_id_seq'::regclass);


--
-- Name: meddelanden_startsida id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY meddelanden_startsida ALTER COLUMN id SET DEFAULT nextval('meddelanden_startsida_id_seq'::regclass);


--
-- Name: systemlogg id; Type: DEFAULT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY systemlogg ALTER COLUMN id SET DEFAULT nextval('systemlogg_id_seq'::regclass);


--
-- Name: analys pk_analys; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY analys
    ADD CONSTRAINT pk_analys PRIMARY KEY (id);


--
-- Name: anvandare pk_anvandare; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandare
    ADD CONSTRAINT pk_anvandare PRIMARY KEY (id);


--
-- Name: anvandargrupp pk_anvandargrupp; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandargrupp
    ADD CONSTRAINT pk_anvandargrupp PRIMARY KEY (id);


--
-- Name: bifogad_fil pk_bifogad_fil; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY bifogad_fil
    ADD CONSTRAINT pk_bifogad_fil PRIMARY KEY (id);


--
-- Name: gransvarde pk_gransvarde; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY gransvarde
    ADD CONSTRAINT pk_gransvarde PRIMARY KEY (id);


--
-- Name: grupp pk_grupp; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY grupp
    ADD CONSTRAINT pk_grupp PRIMARY KEY (id);


--
-- Name: handelse pk_handelselogg; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY handelse
    ADD CONSTRAINT pk_handelselogg PRIMARY KEY (id);


--
-- Name: kartlager pk_kartlager; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY kartlager
    ADD CONSTRAINT pk_kartlager PRIMARY KEY (id);


--
-- Name: kartlager_fil pk_kartlager_fil; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY kartlager_fil
    ADD CONSTRAINT pk_kartlager_fil PRIMARY KEY (id);


--
-- Name: larm pk_larm; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY larm
    ADD CONSTRAINT pk_larm PRIMARY KEY (id);


--
-- Name: larmniva pk_larmniva; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY larmniva
    ADD CONSTRAINT pk_larmniva PRIMARY KEY (id);


--
-- Name: matning pk_matning; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matning
    ADD CONSTRAINT pk_matning PRIMARY KEY (id);


--
-- Name: matningstyp pk_matnings_typ; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matningstyp
    ADD CONSTRAINT pk_matnings_typ PRIMARY KEY (id);


--
-- Name: matningslogg pk_matningslogg; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matningslogg
    ADD CONSTRAINT pk_matningslogg PRIMARY KEY (id);


--
-- Name: definition_matningstyp pk_matningstyp_definition; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY definition_matningstyp
    ADD CONSTRAINT pk_matningstyp_definition PRIMARY KEY (id);


--
-- Name: matobjekt pk_matobjekt; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matobjekt
    ADD CONSTRAINT pk_matobjekt PRIMARY KEY (id);


--
-- Name: matrunda pk_matrunda; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matrunda
    ADD CONSTRAINT pk_matrunda PRIMARY KEY (id);


--
-- Name: meddelanden_startsida pk_meddelanden_startsida; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY meddelanden_startsida
    ADD CONSTRAINT pk_meddelanden_startsida PRIMARY KEY (id);


--
-- Name: systemlogg pk_systemlogg; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY systemlogg
    ADD CONSTRAINT pk_systemlogg PRIMARY KEY (id);


--
-- Name: anvandare uq_anvandare_inloggnings_namn; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandare
    ADD CONSTRAINT uq_anvandare_inloggnings_namn UNIQUE (inloggnings_namn);


--
-- Name: anvandargrupp uq_anvandargrupp_namn; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandargrupp
    ADD CONSTRAINT uq_anvandargrupp_namn UNIQUE (namn);


--
-- Name: grupp uq_grupp_namn; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY grupp
    ADD CONSTRAINT uq_grupp_namn UNIQUE (namn);


--
-- Name: kartlager uq_kartlager_namn; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY kartlager
    ADD CONSTRAINT uq_kartlager_namn UNIQUE (namn);


--
-- Name: larmniva uq_larmniva_namn; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY larmniva
    ADD CONSTRAINT uq_larmniva_namn UNIQUE (namn);


--
-- Name: matobjekt uq_matobjekt_namn; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matobjekt
    ADD CONSTRAINT uq_matobjekt_namn UNIQUE (namn);


--
-- Name: matrunda uq_matrunda_namn; Type: CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matrunda
    ADD CONSTRAINT uq_matrunda_namn UNIQUE (namn);


--
-- Name: analys_bifogad_rapport fk_analys_bifogad_rapport_analys; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY analys_bifogad_rapport
    ADD CONSTRAINT fk_analys_bifogad_rapport_analys FOREIGN KEY (analys_id) REFERENCES analys(id);


--
-- Name: analys_bifogad_rapport fk_analys_bifogad_rapport_bifogad_fil; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY analys_bifogad_rapport
    ADD CONSTRAINT fk_analys_bifogad_rapport_bifogad_fil FOREIGN KEY (bifogad_fil_id) REFERENCES bifogad_fil(id);


--
-- Name: analys fk_analys_matobjekt; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY analys
    ADD CONSTRAINT fk_analys_matobjekt FOREIGN KEY (matobjekt_id) REFERENCES matobjekt(id);


--
-- Name: anvandare fk_anvandare_kartlager; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandare
    ADD CONSTRAINT fk_anvandare_kartlager FOREIGN KEY (default_kartlager_id) REFERENCES kartlager(id);


--
-- Name: anvandargrupp_anvandare fk_anvandargrupp_anvandare_anvandare; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandargrupp_anvandare
    ADD CONSTRAINT fk_anvandargrupp_anvandare_anvandare FOREIGN KEY (anvandar_id) REFERENCES anvandare(id);


--
-- Name: anvandargrupp_anvandare fk_anvandargrupp_anvandare_anvandargrupp; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY anvandargrupp_anvandare
    ADD CONSTRAINT fk_anvandargrupp_anvandare_anvandargrupp FOREIGN KEY (anvandargrupp_id) REFERENCES anvandargrupp(id);


--
-- Name: gransvarde fk_gransvarde_anvandargrupp; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY gransvarde
    ADD CONSTRAINT fk_gransvarde_anvandargrupp FOREIGN KEY (larm_till_anvandargrupp_id) REFERENCES anvandargrupp(id);


--
-- Name: gransvarde fk_gransvarde_larmniva; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY gransvarde
    ADD CONSTRAINT fk_gransvarde_larmniva FOREIGN KEY (larmniva_id) REFERENCES larmniva(id);


--
-- Name: gransvarde fk_gransvarde_matningstyp; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY gransvarde
    ADD CONSTRAINT fk_gransvarde_matningstyp FOREIGN KEY (matningstyp_id) REFERENCES matningstyp(id);


--
-- Name: handelse_bifogad_bild fk_handelse_bifogad_bild_bifogad_fil; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY handelse_bifogad_bild
    ADD CONSTRAINT fk_handelse_bifogad_bild_bifogad_fil FOREIGN KEY (bifogad_fil_id) REFERENCES bifogad_fil(id);


--
-- Name: handelse_bifogad_bild fk_handelse_bifogad_bild_handelse; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY handelse_bifogad_bild
    ADD CONSTRAINT fk_handelse_bifogad_bild_handelse FOREIGN KEY (handelse_id) REFERENCES handelse(id);


--
-- Name: handelse fk_handelse_matobjekt; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY handelse
    ADD CONSTRAINT fk_handelse_matobjekt FOREIGN KEY (matobjekt_id) REFERENCES matobjekt(id);


--
-- Name: kartlager_fil fk_kartlager_fil_kartlager; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY kartlager_fil
    ADD CONSTRAINT fk_kartlager_fil_kartlager FOREIGN KEY (kartlager_id) REFERENCES kartlager(id);


--
-- Name: larm fk_larm_gransvarde; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY larm
    ADD CONSTRAINT fk_larm_gransvarde FOREIGN KEY (gransvarde_id) REFERENCES gransvarde(id);


--
-- Name: larm fk_larm_matning; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY larm
    ADD CONSTRAINT fk_larm_matning FOREIGN KEY (matning_id) REFERENCES matning(id);


--
-- Name: matning fk_matning_matningstyp; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matning
    ADD CONSTRAINT fk_matning_matningstyp FOREIGN KEY (matningstyp_id) REFERENCES matningstyp(id);


--
-- Name: matningstyp fk_matnings_typ_matobjekt; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matningstyp
    ADD CONSTRAINT fk_matnings_typ_matobjekt FOREIGN KEY (matobjekt_id) REFERENCES matobjekt(id);


--
-- Name: matningslogg fk_matningslogg_matning; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matningslogg
    ADD CONSTRAINT fk_matningslogg_matning FOREIGN KEY (matning_id) REFERENCES matning(id);


--
-- Name: matningstyp fk_matningstyp_anvandargrupp; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matningstyp
    ADD CONSTRAINT fk_matningstyp_anvandargrupp FOREIGN KEY (matansvarig_anvandargrupp_id) REFERENCES anvandargrupp(id);


--
-- Name: matningstyp fk_matningstyp_definition_matningstyp; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matningstyp
    ADD CONSTRAINT fk_matningstyp_definition_matningstyp FOREIGN KEY (definition_matningstyp_id) REFERENCES definition_matningstyp(id);


--
-- Name: matobjekt fk_matobjekt_bifogad_fil; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matobjekt
    ADD CONSTRAINT fk_matobjekt_bifogad_fil FOREIGN KEY (bifogad_bild_id) REFERENCES bifogad_fil(id);


--
-- Name: matobjekt_bifogat_dokument fk_matobjekt_dokument_bifogad_fil; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matobjekt_bifogat_dokument
    ADD CONSTRAINT fk_matobjekt_dokument_bifogad_fil FOREIGN KEY (bifogad_fil_id) REFERENCES bifogad_fil(id);


--
-- Name: matobjekt_bifogat_dokument fk_matobjekt_dokument_matobjekt; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matobjekt_bifogat_dokument
    ADD CONSTRAINT fk_matobjekt_dokument_matobjekt FOREIGN KEY (matobjekt_id) REFERENCES matobjekt(id);


--
-- Name: grupp_matobjekt fk_matobjekt_grupp_grupp; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY grupp_matobjekt
    ADD CONSTRAINT fk_matobjekt_grupp_grupp FOREIGN KEY (grupp_id) REFERENCES grupp(id);


--
-- Name: grupp_matobjekt fk_matobjekt_grupp_matobjekt; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY grupp_matobjekt
    ADD CONSTRAINT fk_matobjekt_grupp_matobjekt FOREIGN KEY (matobjekt_id) REFERENCES matobjekt(id);


--
-- Name: matrunda_matningstyp fk_matrunda_matningstyp_matningstyp; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matrunda_matningstyp
    ADD CONSTRAINT fk_matrunda_matningstyp_matningstyp FOREIGN KEY (matningstyp_id) REFERENCES matningstyp(id);


--
-- Name: matrunda_matningstyp fk_matrunda_matningstyp_matrunda; Type: FK CONSTRAINT; Schema: matdatabas; Owner: matdatabas
--

ALTER TABLE ONLY matrunda_matningstyp
    ADD CONSTRAINT fk_matrunda_matningstyp_matrunda FOREIGN KEY (matrunda_id) REFERENCES matrunda(id);


--
-- PostgreSQL database dump complete
--

