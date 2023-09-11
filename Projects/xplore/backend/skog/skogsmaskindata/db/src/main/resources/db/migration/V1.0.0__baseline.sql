--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5
-- Dumped by pg_dump version 11.5

-- Started on 2019-10-16 14:05:24 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 11 (class 2615 OID 43685)
-- Name: forestlink; Type: SCHEMA; Schema: -; Owner: xplore-admin
--

CREATE SCHEMA forestlink;


ALTER SCHEMA forestlink OWNER TO "xplore-admin";

--
-- TOC entry 1 (class 3079 OID 43686)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 5331 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- TOC entry 4 (class 3079 OID 43695)
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- TOC entry 5332 (class 0 OID 0)
-- Dependencies: 4
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- TOC entry 3 (class 3079 OID 45194)
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- TOC entry 5333 (class 0 OID 0)
-- Dependencies: 3
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 215 (class 1259 OID 45205)
-- Name: import; Type: TABLE; Schema: forestlink; Owner: xplore-admin
--

CREATE TABLE forestlink.import (
    id bigint NOT NULL,
    objektnummer character varying NOT NULL,
    organisation text NOT NULL,
    enddate timestamp(4) with time zone NOT NULL,
    pakettyp text NOT NULL
);


ALTER TABLE forestlink.import OWNER TO "xplore-admin";

--
-- TOC entry 216 (class 1259 OID 45211)
-- Name: informationslinjer; Type: TABLE; Schema: forestlink; Owner: xplore-admin
--

CREATE TABLE forestlink.informationslinjer (
    id bigint NOT NULL,
    import_id bigint NOT NULL,
    company text,
    type text,
    sort text,
    essay text,
    geom public.geometry(MultiLineString,3006) NOT NULL
);


ALTER TABLE forestlink.informationslinjer OWNER TO "xplore-admin";

--
-- TOC entry 217 (class 1259 OID 45217)
-- Name: gv_informationslinjer_avv; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationslinjer_avv WITH (security_barrier='false') AS
 SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation
   FROM (forestlink.informationslinjer
     JOIN forestlink.import ON (((import.id = informationslinjer.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));


ALTER TABLE forestlink.gv_informationslinjer_avv OWNER TO "xplore-admin";

--
-- TOC entry 218 (class 1259 OID 45221)
-- Name: gv_informationslinjer_gal; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationslinjer_gal AS
 SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation
   FROM (forestlink.informationslinjer
     JOIN forestlink.import ON (((import.id = informationslinjer.import_id) AND (import.pakettyp = 'Gallring'::text))));


ALTER TABLE forestlink.gv_informationslinjer_gal OWNER TO "xplore-admin";

--
-- TOC entry 219 (class 1259 OID 45225)
-- Name: gv_informationslinjer_mb; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationslinjer_mb AS
 SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation
   FROM (forestlink.informationslinjer
     JOIN forestlink.import ON (((import.id = informationslinjer.import_id) AND (import.pakettyp = 'Markberedning'::text))));


ALTER TABLE forestlink.gv_informationslinjer_mb OWNER TO "xplore-admin";

--
-- TOC entry 220 (class 1259 OID 45229)
-- Name: informationspolygoner; Type: TABLE; Schema: forestlink; Owner: xplore-admin
--

CREATE TABLE forestlink.informationspolygoner (
    id bigint NOT NULL,
    import_id bigint NOT NULL,
    company text,
    type text,
    sort text,
    essay text,
    geom public.geometry(MultiPolygon,3006) NOT NULL
);


ALTER TABLE forestlink.informationspolygoner OWNER TO "xplore-admin";

--
-- TOC entry 221 (class 1259 OID 45235)
-- Name: gv_informationspolygoner_avv; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationspolygoner_avv WITH (security_barrier='false') AS
 SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation
   FROM (forestlink.informationspolygoner
     JOIN forestlink.import ON (((import.id = informationspolygoner.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));


ALTER TABLE forestlink.gv_informationspolygoner_avv OWNER TO "xplore-admin";

--
-- TOC entry 222 (class 1259 OID 45239)
-- Name: gv_informationspolygoner_gal; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationspolygoner_gal AS
 SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation
   FROM (forestlink.informationspolygoner
     JOIN forestlink.import ON (((import.id = informationspolygoner.import_id) AND (import.pakettyp = 'Gallring'::text))));


ALTER TABLE forestlink.gv_informationspolygoner_gal OWNER TO "xplore-admin";

--
-- TOC entry 223 (class 1259 OID 45243)
-- Name: gv_informationspolygoner_mb; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationspolygoner_mb AS
 SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation
   FROM (forestlink.informationspolygoner
     JOIN forestlink.import ON (((import.id = informationspolygoner.import_id) AND (import.pakettyp = 'Markberedning'::text))));


ALTER TABLE forestlink.gv_informationspolygoner_mb OWNER TO "xplore-admin";

--
-- TOC entry 224 (class 1259 OID 45247)
-- Name: informationspunkter; Type: TABLE; Schema: forestlink; Owner: xplore-admin
--

CREATE TABLE forestlink.informationspunkter (
    id bigint NOT NULL,
    import_id bigint NOT NULL,
    company text,
    type text,
    sort text,
    essay text,
    geom public.geometry(MultiPoint,3006) NOT NULL
);


ALTER TABLE forestlink.informationspunkter OWNER TO "xplore-admin";

--
-- TOC entry 225 (class 1259 OID 45253)
-- Name: gv_informationspunkter_avv; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationspunkter_avv WITH (security_barrier='false') AS
 SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation
   FROM (forestlink.informationspunkter
     JOIN forestlink.import ON (((import.id = informationspunkter.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));


ALTER TABLE forestlink.gv_informationspunkter_avv OWNER TO "xplore-admin";

--
-- TOC entry 226 (class 1259 OID 45257)
-- Name: gv_informationspunkter_gal; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationspunkter_gal AS
 SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation
   FROM (forestlink.informationspunkter
     JOIN forestlink.import ON (((import.id = informationspunkter.import_id) AND (import.pakettyp = 'Gallring'::text))));


ALTER TABLE forestlink.gv_informationspunkter_gal OWNER TO "xplore-admin";

--
-- TOC entry 227 (class 1259 OID 45261)
-- Name: gv_informationspunkter_mb; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_informationspunkter_mb AS
 SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation
   FROM (forestlink.informationspunkter
     JOIN forestlink.import ON (((import.id = informationspunkter.import_id) AND (import.pakettyp = 'Markberedning'::text))));


ALTER TABLE forestlink.gv_informationspunkter_mb OWNER TO "xplore-admin";

--
-- TOC entry 228 (class 1259 OID 45265)
-- Name: korspar; Type: TABLE; Schema: forestlink; Owner: xplore-admin
--

CREATE TABLE forestlink.korspar (
    id bigint NOT NULL,
    import_id bigint NOT NULL,
    name text,
    activity text,
    starttime text,
    endtime text,
    corr_status text,
    agg_status text,
    operator text,
    geom public.geometry(MultiLineString,3006) NOT NULL
);


ALTER TABLE forestlink.korspar OWNER TO "xplore-admin";

--
-- TOC entry 229 (class 1259 OID 45271)
-- Name: gv_korspar_avv; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_korspar_avv WITH (security_barrier='false') AS
 SELECT korspar.id,
    korspar.import_id,
    korspar.name,
    korspar.activity,
    korspar.starttime,
    korspar.endtime,
    korspar.corr_status,
    korspar.agg_status,
    korspar.operator,
    korspar.geom,
    import.organisation
   FROM (forestlink.korspar
     JOIN forestlink.import ON (((import.id = korspar.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));


ALTER TABLE forestlink.gv_korspar_avv OWNER TO "xplore-admin";

--
-- TOC entry 230 (class 1259 OID 45275)
-- Name: korspar_for_transport; Type: TABLE; Schema: forestlink; Owner: xplore
--

CREATE TABLE forestlink.korspar_for_transport (
    id bigint NOT NULL,
    import_id bigint NOT NULL,
    name text,
    activity text,
    starttime text,
    endtime text,
    corr_status text,
    agg_status text,
    operator text,
    geom public.geometry(MultiLineString,3006) NOT NULL
);


ALTER TABLE forestlink.korspar_for_transport OWNER TO xplore;

--
-- TOC entry 231 (class 1259 OID 45281)
-- Name: gv_korspar_for_transport_avv; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_korspar_for_transport_avv WITH (security_barrier='false') AS
 SELECT korspar_for_transport.id,
    korspar_for_transport.import_id,
    korspar_for_transport.name,
    korspar_for_transport.activity,
    korspar_for_transport.starttime,
    korspar_for_transport.endtime,
    korspar_for_transport.corr_status,
    korspar_for_transport.agg_status,
    korspar_for_transport.operator,
    korspar_for_transport.geom,
    import.organisation
   FROM (forestlink.korspar_for_transport
     JOIN forestlink.import ON (((import.id = korspar_for_transport.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));


ALTER TABLE forestlink.gv_korspar_for_transport_avv OWNER TO "xplore-admin";

--
-- TOC entry 232 (class 1259 OID 45285)
-- Name: gv_korspar_for_transport_gal; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_korspar_for_transport_gal AS
 SELECT korspar_for_transport.id,
    korspar_for_transport.import_id,
    korspar_for_transport.name,
    korspar_for_transport.activity,
    korspar_for_transport.starttime,
    korspar_for_transport.endtime,
    korspar_for_transport.corr_status,
    korspar_for_transport.agg_status,
    korspar_for_transport.operator,
    korspar_for_transport.geom,
    import.organisation
   FROM (forestlink.korspar_for_transport
     JOIN forestlink.import ON (((import.id = korspar_for_transport.import_id) AND (import.pakettyp = 'Gallring'::text))));


ALTER TABLE forestlink.gv_korspar_for_transport_gal OWNER TO "xplore-admin";

--
-- TOC entry 233 (class 1259 OID 45289)
-- Name: gv_korspar_for_transport_mb; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_korspar_for_transport_mb AS
 SELECT korspar_for_transport.id,
    korspar_for_transport.import_id,
    korspar_for_transport.name,
    korspar_for_transport.activity,
    korspar_for_transport.starttime,
    korspar_for_transport.endtime,
    korspar_for_transport.corr_status,
    korspar_for_transport.agg_status,
    korspar_for_transport.operator,
    korspar_for_transport.geom,
    import.organisation
   FROM (forestlink.korspar_for_transport
     JOIN forestlink.import ON (((import.id = korspar_for_transport.import_id) AND (import.pakettyp = 'Markberedning'::text))));


ALTER TABLE forestlink.gv_korspar_for_transport_mb OWNER TO "xplore-admin";

--
-- TOC entry 234 (class 1259 OID 45293)
-- Name: gv_korspar_gal; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_korspar_gal AS
 SELECT korspar.id,
    korspar.import_id,
    korspar.name,
    korspar.activity,
    korspar.starttime,
    korspar.endtime,
    korspar.corr_status,
    korspar.agg_status,
    korspar.operator,
    korspar.geom,
    import.organisation
   FROM (forestlink.korspar
     JOIN forestlink.import ON (((import.id = korspar.import_id) AND (import.pakettyp = 'Gallring'::text))));


ALTER TABLE forestlink.gv_korspar_gal OWNER TO "xplore-admin";

--
-- TOC entry 235 (class 1259 OID 45297)
-- Name: gv_korspar_mb; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_korspar_mb AS
 SELECT korspar.id,
    korspar.import_id,
    korspar.name,
    korspar.activity,
    korspar.starttime,
    korspar.endtime,
    korspar.corr_status,
    korspar.agg_status,
    korspar.operator,
    korspar.geom,
    import.organisation
   FROM (forestlink.korspar
     JOIN forestlink.import ON (((import.id = korspar.import_id) AND (import.pakettyp = 'Markberedning'::text))));


ALTER TABLE forestlink.gv_korspar_mb OWNER TO "xplore-admin";

--
-- TOC entry 236 (class 1259 OID 45301)
-- Name: provytor; Type: TABLE; Schema: forestlink; Owner: xplore
--

CREATE TABLE forestlink.provytor (
    id bigint NOT NULL,
    import_id bigint NOT NULL,
    driver text,
    marktyp text,
    kommentar text,
    h3 text,
    m3 text,
    t3 text,
    fyra text,
    fem text,
    sjuxsju text,
    laagor text,
    sm text,
    lf text,
    mp text,
    x text,
    y text,
    z text,
    fc_olcolor text,
    fc_olsize text,
    fc_style text,
    fc_color text,
    geom public.geometry(MultiPoint,3006) NOT NULL,
    nr text
);


ALTER TABLE forestlink.provytor OWNER TO xplore;

--
-- TOC entry 237 (class 1259 OID 45307)
-- Name: gv_provytor; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_provytor AS
 SELECT provytor.id,
    provytor.import_id,
    provytor.driver,
    provytor.marktyp,
    provytor.kommentar,
    provytor.h3,
    provytor.m3,
    provytor.t3,
    provytor.fyra,
    provytor.fem,
    provytor.sjuxsju,
    provytor.laagor,
    provytor.sm,
    provytor.lf,
    provytor.mp,
    provytor.x,
    provytor.y,
    provytor.z,
    provytor.fc_olcolor,
    provytor.fc_olsize,
    provytor.fc_style,
    provytor.fc_color,
    provytor.geom,
    import.organisation
   FROM (forestlink.provytor
     JOIN forestlink.import ON ((import.id = provytor.import_id)));


ALTER TABLE forestlink.gv_provytor OWNER TO "xplore-admin";

--
-- TOC entry 238 (class 1259 OID 45312)
-- Name: resultat; Type: TABLE; Schema: forestlink; Owner: xplore-admin
--

CREATE TABLE forestlink.resultat (
    id bigint NOT NULL,
    import_id bigint NOT NULL,
    name text,
    area double precision,
    perimeter double precision,
    length double precision,
    geom public.geometry(MultiPolygon,3006) NOT NULL,
    status text NOT NULL
);


ALTER TABLE forestlink.resultat OWNER TO "xplore-admin";

--
-- TOC entry 239 (class 1259 OID 45318)
-- Name: gv_resultat_avv; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_resultat_avv WITH (security_barrier='false') AS
 SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation
   FROM (forestlink.resultat
     JOIN forestlink.import ON (((import.id = resultat.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));


ALTER TABLE forestlink.gv_resultat_avv OWNER TO "xplore-admin";

--
-- TOC entry 240 (class 1259 OID 45322)
-- Name: gv_resultat_gal; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_resultat_gal AS
 SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation
   FROM (forestlink.resultat
     JOIN forestlink.import ON (((import.id = resultat.import_id) AND (import.pakettyp = 'Gallring'::text))));


ALTER TABLE forestlink.gv_resultat_gal OWNER TO "xplore-admin";

--
-- TOC entry 241 (class 1259 OID 45326)
-- Name: gv_resultat_mb; Type: VIEW; Schema: forestlink; Owner: xplore-admin
--

CREATE VIEW forestlink.gv_resultat_mb AS
 SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation
   FROM (forestlink.resultat
     JOIN forestlink.import ON (((import.id = resultat.import_id) AND (import.pakettyp = 'Markberedning'::text))));


ALTER TABLE forestlink.gv_resultat_mb OWNER TO "xplore-admin";

--
-- TOC entry 242 (class 1259 OID 45330)
-- Name: import_seq; Type: SEQUENCE; Schema: forestlink; Owner: xplore-admin
--

CREATE SEQUENCE forestlink.import_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forestlink.import_seq OWNER TO "xplore-admin";

--
-- TOC entry 243 (class 1259 OID 45332)
-- Name: importdata; Type: TABLE; Schema: forestlink; Owner: xplore-admin
--

CREATE TABLE forestlink.importdata (
    parent_id bigint NOT NULL,
    xml text NOT NULL,
    pdf bytea
);


ALTER TABLE forestlink.importdata OWNER TO "xplore-admin";

--
-- TOC entry 244 (class 1259 OID 45338)
-- Name: importstatus; Type: TABLE; Schema: forestlink; Owner: xplore-admin
--

CREATE TABLE forestlink.importstatus (
    parent_id bigint NOT NULL,
    status text NOT NULL,
    started timestamp(4) with time zone,
    completed timestamp(4) with time zone,
    cause text
);


ALTER TABLE forestlink.importstatus OWNER TO "xplore-admin";

--
-- TOC entry 245 (class 1259 OID 45344)
-- Name: informationslinjer_seq; Type: SEQUENCE; Schema: forestlink; Owner: xplore-admin
--

CREATE SEQUENCE forestlink.informationslinjer_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forestlink.informationslinjer_seq OWNER TO "xplore-admin";

--
-- TOC entry 246 (class 1259 OID 45346)
-- Name: informationspolygoner_seq; Type: SEQUENCE; Schema: forestlink; Owner: xplore-admin
--

CREATE SEQUENCE forestlink.informationspolygoner_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forestlink.informationspolygoner_seq OWNER TO "xplore-admin";

--
-- TOC entry 247 (class 1259 OID 45348)
-- Name: informationspunkter_seq; Type: SEQUENCE; Schema: forestlink; Owner: xplore-admin
--

CREATE SEQUENCE forestlink.informationspunkter_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forestlink.informationspunkter_seq OWNER TO "xplore-admin";

--
-- TOC entry 248 (class 1259 OID 45350)
-- Name: korspar_for_transport_seq; Type: SEQUENCE; Schema: forestlink; Owner: xplore-admin
--

CREATE SEQUENCE forestlink.korspar_for_transport_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forestlink.korspar_for_transport_seq OWNER TO "xplore-admin";

--
-- TOC entry 249 (class 1259 OID 45352)
-- Name: korspar_seq; Type: SEQUENCE; Schema: forestlink; Owner: xplore-admin
--

CREATE SEQUENCE forestlink.korspar_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forestlink.korspar_seq OWNER TO "xplore-admin";

--
-- TOC entry 250 (class 1259 OID 45354)
-- Name: provytor_seq; Type: SEQUENCE; Schema: forestlink; Owner: xplore-admin
--

CREATE SEQUENCE forestlink.provytor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forestlink.provytor_seq OWNER TO "xplore-admin";

--
-- TOC entry 251 (class 1259 OID 45356)
-- Name: resultat_seq; Type: SEQUENCE; Schema: forestlink; Owner: xplore-admin
--

CREATE SEQUENCE forestlink.resultat_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE forestlink.resultat_seq OWNER TO "xplore-admin";

--
-- TOC entry 5144 (class 2606 OID 45358)
-- Name: resultat StatusCheck; Type: CHECK CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE forestlink.resultat
    ADD CONSTRAINT "StatusCheck" CHECK ((status = ANY (ARRAY['G'::text, 'U'::text]))) NOT VALID;


--
-- TOC entry 5148 (class 2606 OID 45360)
-- Name: import import_objektnummer_organisation_pakettyp_key; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.import
    ADD CONSTRAINT import_objektnummer_organisation_pakettyp_key UNIQUE (objektnummer, organisation, pakettyp);


--
-- TOC entry 5150 (class 2606 OID 45362)
-- Name: import import_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.import
    ADD CONSTRAINT import_pkey PRIMARY KEY (id);


--
-- TOC entry 5166 (class 2606 OID 45364)
-- Name: importdata importdata_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.importdata
    ADD CONSTRAINT importdata_pkey PRIMARY KEY (parent_id);


--
-- TOC entry 5168 (class 2606 OID 45366)
-- Name: importstatus importstatus_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.importstatus
    ADD CONSTRAINT importstatus_pkey PRIMARY KEY (parent_id);


--
-- TOC entry 5152 (class 2606 OID 45368)
-- Name: informationslinjer informationslinjer_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.informationslinjer
    ADD CONSTRAINT informationslinjer_pkey PRIMARY KEY (id);


--
-- TOC entry 5154 (class 2606 OID 45370)
-- Name: informationspolygoner informationspolygoner_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.informationspolygoner
    ADD CONSTRAINT informationspolygoner_pkey PRIMARY KEY (id);


--
-- TOC entry 5156 (class 2606 OID 45372)
-- Name: informationspunkter informationspunkter_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.informationspunkter
    ADD CONSTRAINT informationspunkter_pkey PRIMARY KEY (id);


--
-- TOC entry 5160 (class 2606 OID 45374)
-- Name: korspar_for_transport korspar_for_transport_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore
--

ALTER TABLE ONLY forestlink.korspar_for_transport
    ADD CONSTRAINT korspar_for_transport_pkey PRIMARY KEY (id);


--
-- TOC entry 5158 (class 2606 OID 45376)
-- Name: korspar korspar_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.korspar
    ADD CONSTRAINT korspar_pkey PRIMARY KEY (id);


--
-- TOC entry 5162 (class 2606 OID 45378)
-- Name: provytor provytor_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore
--

ALTER TABLE ONLY forestlink.provytor
    ADD CONSTRAINT provytor_pkey PRIMARY KEY (id);


--
-- TOC entry 5164 (class 2606 OID 45380)
-- Name: resultat resultat_pkey; Type: CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.resultat
    ADD CONSTRAINT resultat_pkey PRIMARY KEY (id);


--
-- TOC entry 5171 (class 2606 OID 45381)
-- Name: informationspunkter fk_import_id; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.informationspunkter
    ADD CONSTRAINT fk_import_id FOREIGN KEY (import_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5175 (class 2606 OID 45386)
-- Name: resultat fk_import_id; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.resultat
    ADD CONSTRAINT fk_import_id FOREIGN KEY (import_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5170 (class 2606 OID 45391)
-- Name: informationspolygoner fk_import_id; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.informationspolygoner
    ADD CONSTRAINT fk_import_id FOREIGN KEY (import_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5169 (class 2606 OID 45396)
-- Name: informationslinjer fk_import_id; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.informationslinjer
    ADD CONSTRAINT fk_import_id FOREIGN KEY (import_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5172 (class 2606 OID 45401)
-- Name: korspar fk_korspar_import_id; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.korspar
    ADD CONSTRAINT fk_korspar_import_id FOREIGN KEY (import_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5176 (class 2606 OID 45406)
-- Name: importdata importdata_import_id_fkey; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.importdata
    ADD CONSTRAINT importdata_import_id_fkey FOREIGN KEY (parent_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5177 (class 2606 OID 45411)
-- Name: importstatus importstatus_import_id_fkey; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore-admin
--

ALTER TABLE ONLY forestlink.importstatus
    ADD CONSTRAINT importstatus_import_id_fkey FOREIGN KEY (parent_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5173 (class 2606 OID 45416)
-- Name: korspar_for_transport korspar_for_transport_import_id_fkey; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore
--

ALTER TABLE ONLY forestlink.korspar_for_transport
    ADD CONSTRAINT korspar_for_transport_import_id_fkey FOREIGN KEY (import_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5174 (class 2606 OID 45421)
-- Name: provytor provytor_import_id_fkey; Type: FK CONSTRAINT; Schema: forestlink; Owner: xplore
--

ALTER TABLE ONLY forestlink.provytor
    ADD CONSTRAINT provytor_import_id_fkey FOREIGN KEY (import_id) REFERENCES forestlink.import(id);


--
-- TOC entry 5330 (class 0 OID 0)
-- Dependencies: 11
-- Name: SCHEMA forestlink; Type: ACL; Schema: -; Owner: xplore-admin
--

GRANT ALL ON SCHEMA forestlink TO forestlink;
GRANT USAGE ON SCHEMA forestlink TO c_forestlink;


--
-- TOC entry 5334 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE import; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.import TO c_forestlink;
GRANT ALL ON TABLE forestlink.import TO forestlink;
GRANT SELECT ON TABLE forestlink.import TO read_forestlink;


--
-- TOC entry 5335 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE informationslinjer; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.informationslinjer TO c_forestlink;
GRANT ALL ON TABLE forestlink.informationslinjer TO forestlink;
GRANT SELECT ON TABLE forestlink.informationslinjer TO read_forestlink;


--
-- TOC entry 5336 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE gv_informationslinjer_avv; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationslinjer_avv TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationslinjer_avv TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationslinjer_avv TO read_forestlink;


--
-- TOC entry 5337 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE gv_informationslinjer_gal; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationslinjer_gal TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationslinjer_gal TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationslinjer_gal TO read_forestlink;


--
-- TOC entry 5338 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE gv_informationslinjer_mb; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationslinjer_mb TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationslinjer_mb TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationslinjer_mb TO read_forestlink;


--
-- TOC entry 5339 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE informationspolygoner; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.informationspolygoner TO c_forestlink;
GRANT ALL ON TABLE forestlink.informationspolygoner TO forestlink;
GRANT SELECT ON TABLE forestlink.informationspolygoner TO read_forestlink;


--
-- TOC entry 5340 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE gv_informationspolygoner_avv; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationspolygoner_avv TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationspolygoner_avv TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationspolygoner_avv TO read_forestlink;


--
-- TOC entry 5341 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE gv_informationspolygoner_gal; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationspolygoner_gal TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationspolygoner_gal TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationspolygoner_gal TO read_forestlink;


--
-- TOC entry 5342 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE gv_informationspolygoner_mb; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationspolygoner_mb TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationspolygoner_mb TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationspolygoner_mb TO read_forestlink;


--
-- TOC entry 5343 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE informationspunkter; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.informationspunkter TO c_forestlink;
GRANT ALL ON TABLE forestlink.informationspunkter TO forestlink;
GRANT SELECT ON TABLE forestlink.informationspunkter TO read_forestlink;


--
-- TOC entry 5344 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE gv_informationspunkter_avv; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationspunkter_avv TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationspunkter_avv TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationspunkter_avv TO read_forestlink;


--
-- TOC entry 5345 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE gv_informationspunkter_gal; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationspunkter_gal TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationspunkter_gal TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationspunkter_gal TO read_forestlink;


--
-- TOC entry 5346 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE gv_informationspunkter_mb; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_informationspunkter_mb TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_informationspunkter_mb TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_informationspunkter_mb TO read_forestlink;


--
-- TOC entry 5347 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE korspar; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.korspar TO c_forestlink;
GRANT ALL ON TABLE forestlink.korspar TO forestlink;
GRANT SELECT ON TABLE forestlink.korspar TO read_forestlink;


--
-- TOC entry 5348 (class 0 OID 0)
-- Dependencies: 229
-- Name: TABLE gv_korspar_avv; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_korspar_avv TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_korspar_avv TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_korspar_avv TO read_forestlink;


--
-- TOC entry 5349 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE korspar_for_transport; Type: ACL; Schema: forestlink; Owner: xplore
--

GRANT SELECT ON TABLE forestlink.korspar_for_transport TO c_forestlink;
GRANT ALL ON TABLE forestlink.korspar_for_transport TO forestlink;
GRANT SELECT ON TABLE forestlink.korspar_for_transport TO read_forestlink;


--
-- TOC entry 5350 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE gv_korspar_for_transport_avv; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_korspar_for_transport_avv TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_korspar_for_transport_avv TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_korspar_for_transport_avv TO read_forestlink;


--
-- TOC entry 5351 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE gv_korspar_for_transport_gal; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_korspar_for_transport_gal TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_korspar_for_transport_gal TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_korspar_for_transport_gal TO read_forestlink;


--
-- TOC entry 5352 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE gv_korspar_for_transport_mb; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_korspar_for_transport_mb TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_korspar_for_transport_mb TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_korspar_for_transport_mb TO read_forestlink;


--
-- TOC entry 5353 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE gv_korspar_gal; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_korspar_gal TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_korspar_gal TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_korspar_gal TO read_forestlink;


--
-- TOC entry 5354 (class 0 OID 0)
-- Dependencies: 235
-- Name: TABLE gv_korspar_mb; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_korspar_mb TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_korspar_mb TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_korspar_mb TO read_forestlink;


--
-- TOC entry 5355 (class 0 OID 0)
-- Dependencies: 236
-- Name: TABLE provytor; Type: ACL; Schema: forestlink; Owner: xplore
--

GRANT SELECT ON TABLE forestlink.provytor TO c_forestlink;
GRANT ALL ON TABLE forestlink.provytor TO forestlink;
GRANT SELECT ON TABLE forestlink.provytor TO read_forestlink;


--
-- TOC entry 5356 (class 0 OID 0)
-- Dependencies: 237
-- Name: TABLE gv_provytor; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_provytor TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_provytor TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_provytor TO read_forestlink;


--
-- TOC entry 5357 (class 0 OID 0)
-- Dependencies: 238
-- Name: TABLE resultat; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.resultat TO c_forestlink;
GRANT ALL ON TABLE forestlink.resultat TO forestlink;
GRANT SELECT ON TABLE forestlink.resultat TO read_forestlink;


--
-- TOC entry 5358 (class 0 OID 0)
-- Dependencies: 239
-- Name: TABLE gv_resultat_avv; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_resultat_avv TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_resultat_avv TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_resultat_avv TO read_forestlink;


--
-- TOC entry 5359 (class 0 OID 0)
-- Dependencies: 240
-- Name: TABLE gv_resultat_gal; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_resultat_gal TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_resultat_gal TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_resultat_gal TO read_forestlink;


--
-- TOC entry 5360 (class 0 OID 0)
-- Dependencies: 241
-- Name: TABLE gv_resultat_mb; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.gv_resultat_mb TO c_forestlink;
GRANT ALL ON TABLE forestlink.gv_resultat_mb TO forestlink;
GRANT SELECT ON TABLE forestlink.gv_resultat_mb TO read_forestlink;


--
-- TOC entry 5361 (class 0 OID 0)
-- Dependencies: 242
-- Name: SEQUENCE import_seq; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE forestlink.import_seq TO c_forestlink;
GRANT ALL ON SEQUENCE forestlink.import_seq TO forestlink;
GRANT SELECT ON SEQUENCE forestlink.import_seq TO read_forestlink;


--
-- TOC entry 5362 (class 0 OID 0)
-- Dependencies: 243
-- Name: TABLE importdata; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.importdata TO c_forestlink;
GRANT ALL ON TABLE forestlink.importdata TO forestlink;
GRANT SELECT ON TABLE forestlink.importdata TO read_forestlink;


--
-- TOC entry 5363 (class 0 OID 0)
-- Dependencies: 244
-- Name: TABLE importstatus; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT ON TABLE forestlink.importstatus TO c_forestlink;
GRANT ALL ON TABLE forestlink.importstatus TO forestlink;
GRANT SELECT ON TABLE forestlink.importstatus TO read_forestlink;


--
-- TOC entry 5364 (class 0 OID 0)
-- Dependencies: 245
-- Name: SEQUENCE informationslinjer_seq; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE forestlink.informationslinjer_seq TO c_forestlink;
GRANT ALL ON SEQUENCE forestlink.informationslinjer_seq TO forestlink;
GRANT SELECT ON SEQUENCE forestlink.informationslinjer_seq TO read_forestlink;


--
-- TOC entry 5365 (class 0 OID 0)
-- Dependencies: 246
-- Name: SEQUENCE informationspolygoner_seq; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE forestlink.informationspolygoner_seq TO c_forestlink;
GRANT ALL ON SEQUENCE forestlink.informationspolygoner_seq TO forestlink;
GRANT SELECT ON SEQUENCE forestlink.informationspolygoner_seq TO read_forestlink;


--
-- TOC entry 5366 (class 0 OID 0)
-- Dependencies: 247
-- Name: SEQUENCE informationspunkter_seq; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE forestlink.informationspunkter_seq TO c_forestlink;
GRANT ALL ON SEQUENCE forestlink.informationspunkter_seq TO forestlink;
GRANT SELECT ON SEQUENCE forestlink.informationspunkter_seq TO read_forestlink;


--
-- TOC entry 5367 (class 0 OID 0)
-- Dependencies: 248
-- Name: SEQUENCE korspar_for_transport_seq; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE forestlink.korspar_for_transport_seq TO c_forestlink;
GRANT ALL ON SEQUENCE forestlink.korspar_for_transport_seq TO forestlink;
GRANT SELECT ON SEQUENCE forestlink.korspar_for_transport_seq TO read_forestlink;


--
-- TOC entry 5368 (class 0 OID 0)
-- Dependencies: 249
-- Name: SEQUENCE korspar_seq; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE forestlink.korspar_seq TO c_forestlink;
GRANT ALL ON SEQUENCE forestlink.korspar_seq TO forestlink;
GRANT SELECT ON SEQUENCE forestlink.korspar_seq TO read_forestlink;


--
-- TOC entry 5369 (class 0 OID 0)
-- Dependencies: 250
-- Name: SEQUENCE provytor_seq; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE forestlink.provytor_seq TO c_forestlink;
GRANT ALL ON SEQUENCE forestlink.provytor_seq TO forestlink;
GRANT SELECT ON SEQUENCE forestlink.provytor_seq TO read_forestlink;


--
-- TOC entry 5370 (class 0 OID 0)
-- Dependencies: 251
-- Name: SEQUENCE resultat_seq; Type: ACL; Schema: forestlink; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE forestlink.resultat_seq TO c_forestlink;
GRANT ALL ON SEQUENCE forestlink.resultat_seq TO forestlink;
GRANT SELECT ON SEQUENCE forestlink.resultat_seq TO read_forestlink;


--
-- TOC entry 5371 (class 0 OID 0)
-- Dependencies: 203
-- Name: TABLE geography_columns; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.geography_columns TO forestlink;
GRANT SELECT ON TABLE public.geography_columns TO read_forestlink;


--
-- TOC entry 5372 (class 0 OID 0)
-- Dependencies: 204
-- Name: TABLE geometry_columns; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.geometry_columns TO forestlink;
GRANT SELECT ON TABLE public.geometry_columns TO read_forestlink;


--
-- TOC entry 5373 (class 0 OID 0)
-- Dependencies: 213
-- Name: TABLE raster_columns; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.raster_columns TO forestlink;
GRANT SELECT ON TABLE public.raster_columns TO read_forestlink;


--
-- TOC entry 5374 (class 0 OID 0)
-- Dependencies: 214
-- Name: TABLE raster_overviews; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.raster_overviews TO forestlink;
GRANT SELECT ON TABLE public.raster_overviews TO read_forestlink;


--
-- TOC entry 5375 (class 0 OID 0)
-- Dependencies: 201
-- Name: TABLE spatial_ref_sys; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.spatial_ref_sys TO forestlink;
GRANT SELECT ON TABLE public.spatial_ref_sys TO c_forestlink;
GRANT SELECT ON TABLE public.spatial_ref_sys TO read_forestlink;


-- Completed on 2019-10-16 14:05:39 CEST

--
-- PostgreSQL database dump complete
--

