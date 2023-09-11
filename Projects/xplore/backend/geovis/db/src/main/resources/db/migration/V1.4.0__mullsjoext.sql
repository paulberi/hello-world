--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5
-- Dumped by pg_dump version 11.5

-- Started on 2019-10-16 11:30:51 CEST

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
-- TOC entry 13 (class 2615 OID 101309)
-- Name: mullsjoext; Type: SCHEMA; Schema: -; Owner: geovis
--

CREATE SCHEMA mullsjoext;


ALTER SCHEMA mullsjoext OWNER TO geovis;

--
-- TOC entry 897 (class 1259 OID 101310)
-- Name: standard_kommunkarta_punkt_id_seq33; Type: SEQUENCE; Schema: mullsjoext; Owner: geovis
--

CREATE SEQUENCE mullsjoext.standard_kommunkarta_punkt_id_seq33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE mullsjoext.standard_kommunkarta_punkt_id_seq33 OWNER TO geovis;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 898 (class 1259 OID 101312)
-- Name: karta_apotek; Type: TABLE; Schema: mullsjoext; Owner: geovis
--

CREATE TABLE mullsjoext.karta_apotek (
    id integer DEFAULT nextval('mullsjoext.standard_kommunkarta_punkt_id_seq33'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE mullsjoext.karta_apotek OWNER TO geovis;

--
-- TOC entry 7670 (class 2606 OID 101320)
-- Name: karta_apotek standard_kommunkarta_punkt_pkey33; Type: CONSTRAINT; Schema: mullsjoext; Owner: geovis
--

ALTER TABLE ONLY mullsjoext.karta_apotek
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey33 PRIMARY KEY (id);


--
-- TOC entry 7804 (class 0 OID 0)
-- Dependencies: 13
-- Name: SCHEMA mullsjoext; Type: ACL; Schema: -; Owner: geovis
--

GRANT USAGE ON SCHEMA mullsjoext TO "mullsjo-read";
GRANT ALL ON SCHEMA mullsjoext TO "xplore-admin";


-- Completed on 2019-10-16 11:30:52 CEST

--
-- PostgreSQL database dump complete
--

