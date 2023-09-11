--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5
-- Dumped by pg_dump version 11.5

-- Started on 2019-10-16 11:11:00 CEST

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
-- TOC entry 11 (class 2615 OID 78516)
-- Name: demo_geovis; Type: SCHEMA; Schema: -; Owner: geovis
--

CREATE SCHEMA demo_geovis;


ALTER SCHEMA demo_geovis OWNER TO geovis;

--
-- TOC entry 681 (class 1259 OID 78517)
-- Name: batplatser_aneby_id_seq; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.batplatser_aneby_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.batplatser_aneby_id_seq OWNER TO "xplore-admin";

--
-- TOC entry 682 (class 1259 OID 78519)
-- Name: standard_kommunkarta_punkt_id_seq79; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq79
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq79 OWNER TO "xplore-admin";

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 683 (class 1259 OID 78521)
-- Name: gotakanalbolag_kontor; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.gotakanalbolag_kontor (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq79'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.gotakanalbolag_kontor OWNER TO "xplore-admin";

--
-- TOC entry 684 (class 1259 OID 78528)
-- Name: standard_kommunkarta_punkt_id_seq33; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq33 OWNER TO "xplore-admin";

--
-- TOC entry 685 (class 1259 OID 78530)
-- Name: karta_apotek; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_apotek (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq33'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_apotek OWNER TO "xplore-admin";

--
-- TOC entry 686 (class 1259 OID 78537)
-- Name: standard_kommunkarta_punkt_id_seq27; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq27 OWNER TO "xplore-admin";

--
-- TOC entry 687 (class 1259 OID 78539)
-- Name: karta_atervinningscentral; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_atervinningscentral (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq27'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_atervinningscentral OWNER TO "xplore-admin";

--
-- TOC entry 688 (class 1259 OID 78546)
-- Name: standard_kommunkarta_punkt_id_seq28; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq28 OWNER TO "xplore-admin";

--
-- TOC entry 689 (class 1259 OID 78548)
-- Name: karta_atervinningsstation; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_atervinningsstation (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq28'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_atervinningsstation OWNER TO "xplore-admin";

--
-- TOC entry 690 (class 1259 OID 78555)
-- Name: standard_kommunkarta_punkt_id_seq20; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq20 OWNER TO "xplore-admin";

--
-- TOC entry 691 (class 1259 OID 78557)
-- Name: karta_badplats; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_badplats (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq20'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_badplats OWNER TO "xplore-admin";

--
-- TOC entry 692 (class 1259 OID 78564)
-- Name: standard_kommunkarta_punkt_id_seq41; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq41 OWNER TO "xplore-admin";

--
-- TOC entry 693 (class 1259 OID 78566)
-- Name: karta_bankomat; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_bankomat (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq41'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_bankomat OWNER TO "xplore-admin";

--
-- TOC entry 694 (class 1259 OID 78573)
-- Name: standard_kommunkarta_punkt_id_seq56; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq56 OWNER TO "xplore-admin";

--
-- TOC entry 695 (class 1259 OID 78575)
-- Name: karta_basketplan; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_basketplan (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq56'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_basketplan OWNER TO "xplore-admin";

--
-- TOC entry 696 (class 1259 OID 78582)
-- Name: karta_batplatser; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_batplatser (
    id integer DEFAULT nextval('demo_geovis.batplatser_aneby_id_seq'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    beskr1 character varying(254),
    beskr2 character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_batplatser OWNER TO "xplore-admin";

--
-- TOC entry 697 (class 1259 OID 78589)
-- Name: standard_kommunkarta_punkt_id_seq57; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq57 OWNER TO "xplore-admin";

--
-- TOC entry 698 (class 1259 OID 78591)
-- Name: karta_beachvolley; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_beachvolley (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq57'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_beachvolley OWNER TO "xplore-admin";

--
-- TOC entry 699 (class 1259 OID 78598)
-- Name: standard_kommunkarta_punkt_id_seq58; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq58 OWNER TO "xplore-admin";

--
-- TOC entry 700 (class 1259 OID 78600)
-- Name: karta_begravningsplats; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_begravningsplats (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq58'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_begravningsplats OWNER TO "xplore-admin";

--
-- TOC entry 701 (class 1259 OID 78607)
-- Name: standard_kommunkarta_linje_id_seq; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_linje_id_seq OWNER TO "xplore-admin";

--
-- TOC entry 702 (class 1259 OID 78609)
-- Name: karta_belaggningsarbete; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_belaggningsarbete (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_linje_id_seq'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_belaggningsarbete OWNER TO "xplore-admin";

--
-- TOC entry 703 (class 1259 OID 78616)
-- Name: standard_kommunkarta_punkt_id_seq13; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq13 OWNER TO "xplore-admin";

--
-- TOC entry 704 (class 1259 OID 78618)
-- Name: karta_bibliotek; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_bibliotek (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq13'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_bibliotek OWNER TO "xplore-admin";

--
-- TOC entry 705 (class 1259 OID 78625)
-- Name: standard_kommunkarta_punkt_id_seq14; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq14 OWNER TO "xplore-admin";

--
-- TOC entry 706 (class 1259 OID 78627)
-- Name: karta_biograf; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_biograf (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq14'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_biograf OWNER TO "xplore-admin";

--
-- TOC entry 707 (class 1259 OID 78634)
-- Name: standard_kommunkarta_punkt_id_seq45; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq45 OWNER TO "xplore-admin";

--
-- TOC entry 708 (class 1259 OID 78636)
-- Name: karta_boende; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_boende (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq45'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_boende OWNER TO "xplore-admin";

--
-- TOC entry 709 (class 1259 OID 78643)
-- Name: standard_kommunkarta_yta_id_seq8; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq8 OWNER TO "xplore-admin";

--
-- TOC entry 710 (class 1259 OID 78645)
-- Name: karta_bostadstomter_ejbebyggda; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_bostadstomter_ejbebyggda (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq8'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_bostadstomter_ejbebyggda OWNER TO "xplore-admin";

--
-- TOC entry 711 (class 1259 OID 78652)
-- Name: standard_kommunkarta_punkt_id_seq59; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq59 OWNER TO "xplore-admin";

--
-- TOC entry 712 (class 1259 OID 78654)
-- Name: karta_boulebanor; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_boulebanor (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq59'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_boulebanor OWNER TO "xplore-admin";

--
-- TOC entry 713 (class 1259 OID 78661)
-- Name: standard_kommunkarta_punkt_id_seq67; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq67 OWNER TO "xplore-admin";

--
-- TOC entry 714 (class 1259 OID 78663)
-- Name: karta_bowling; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_bowling (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq67'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_bowling OWNER TO "xplore-admin";

--
-- TOC entry 715 (class 1259 OID 78670)
-- Name: standard_kommunkarta_punkt_id_seq53; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq53 OWNER TO "xplore-admin";

--
-- TOC entry 716 (class 1259 OID 78672)
-- Name: karta_busshallplats; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_busshallplats (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq53'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_busshallplats OWNER TO "xplore-admin";

--
-- TOC entry 717 (class 1259 OID 78679)
-- Name: standard_kommunkarta_punkt_id_seq60; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq60 OWNER TO "xplore-admin";

--
-- TOC entry 718 (class 1259 OID 78681)
-- Name: karta_busskort; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_busskort (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq60'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_busskort OWNER TO "xplore-admin";

--
-- TOC entry 719 (class 1259 OID 78688)
-- Name: standard_kommunkarta_punkt_id_seq31; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq31 OWNER TO "xplore-admin";

--
-- TOC entry 720 (class 1259 OID 78690)
-- Name: karta_busstation; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_busstation (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq31'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_busstation OWNER TO "xplore-admin";

--
-- TOC entry 721 (class 1259 OID 78697)
-- Name: standard_kommunkarta_punkt_id_seq70; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq70 OWNER TO "xplore-admin";

--
-- TOC entry 722 (class 1259 OID 78699)
-- Name: karta_bygdegardar; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_bygdegardar (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq70'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_bygdegardar OWNER TO "xplore-admin";

--
-- TOC entry 723 (class 1259 OID 78706)
-- Name: standard_kommunkarta_punkt_id_seq50; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq50 OWNER TO "xplore-admin";

--
-- TOC entry 724 (class 1259 OID 78708)
-- Name: karta_cafe; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_cafe (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq50'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_cafe OWNER TO "xplore-admin";

--
-- TOC entry 725 (class 1259 OID 78715)
-- Name: standard_kommunkarta_linje_id_seq2; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_linje_id_seq2 OWNER TO "xplore-admin";

--
-- TOC entry 726 (class 1259 OID 78717)
-- Name: karta_cykelleder; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_cykelleder (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_linje_id_seq2'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_cykelleder OWNER TO "xplore-admin";

--
-- TOC entry 727 (class 1259 OID 78724)
-- Name: standard_kommunkarta_linje_id_seq3; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_linje_id_seq3 OWNER TO "xplore-admin";

--
-- TOC entry 728 (class 1259 OID 78726)
-- Name: karta_cykelvagar; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_cykelvagar (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_linje_id_seq3'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_cykelvagar OWNER TO "xplore-admin";

--
-- TOC entry 729 (class 1259 OID 78733)
-- Name: standard_kommunkarta_punkt_id_seq9; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq9 OWNER TO "xplore-admin";

--
-- TOC entry 730 (class 1259 OID 78735)
-- Name: karta_daglig-verksamhet; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_daglig-verksamhet" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq9'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_daglig-verksamhet" OWNER TO "xplore-admin";

--
-- TOC entry 731 (class 1259 OID 78742)
-- Name: standard_kommunkarta_yta_id_seq; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq OWNER TO "xplore-admin";

--
-- TOC entry 732 (class 1259 OID 78744)
-- Name: karta_detaljplaner-gallande; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_detaljplaner-gallande" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(254),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_detaljplaner-gallande" OWNER TO "xplore-admin";

--
-- TOC entry 733 (class 1259 OID 78751)
-- Name: standard_kommunkarta_yta_id_seq1; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq1 OWNER TO "xplore-admin";

--
-- TOC entry 734 (class 1259 OID 78753)
-- Name: karta_detaljplaner-pagaende; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_detaljplaner-pagaende" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq1'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(254),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_detaljplaner-pagaende" OWNER TO "xplore-admin";

--
-- TOC entry 735 (class 1259 OID 78760)
-- Name: standard_kommunkarta_punkt_id_seq52; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq52 OWNER TO "xplore-admin";

--
-- TOC entry 736 (class 1259 OID 78762)
-- Name: karta_drivmedelsstation; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_drivmedelsstation (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq52'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_drivmedelsstation OWNER TO "xplore-admin";

--
-- TOC entry 737 (class 1259 OID 78769)
-- Name: standard_kommunkarta_punkt_id_seq54; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq54 OWNER TO "xplore-admin";

--
-- TOC entry 738 (class 1259 OID 78771)
-- Name: karta_fiske; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_fiske (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq54'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_fiske OWNER TO "xplore-admin";

--
-- TOC entry 739 (class 1259 OID 78778)
-- Name: standard_kommunkarta_punkt_id_seq55; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq55 OWNER TO "xplore-admin";

--
-- TOC entry 740 (class 1259 OID 78780)
-- Name: karta_fiskekort; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_fiskekort (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq55'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_fiskekort OWNER TO "xplore-admin";

--
-- TOC entry 741 (class 1259 OID 78787)
-- Name: standard_kommunkarta_punkt_id_seq15; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq15 OWNER TO "xplore-admin";

--
-- TOC entry 742 (class 1259 OID 78789)
-- Name: karta_folkets-park; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_folkets-park" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq15'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_folkets-park" OWNER TO "xplore-admin";

--
-- TOC entry 743 (class 1259 OID 78796)
-- Name: standard_kommunkarta_punkt_id_seq36; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq36 OWNER TO "xplore-admin";

--
-- TOC entry 744 (class 1259 OID 78798)
-- Name: karta_folktandvard; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_folktandvard (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq36'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_folktandvard OWNER TO "xplore-admin";

--
-- TOC entry 745 (class 1259 OID 78805)
-- Name: standard_kommunkarta_punkt_id_seq1; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq1 OWNER TO "xplore-admin";

--
-- TOC entry 746 (class 1259 OID 78807)
-- Name: karta_forskola; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_forskola (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq1'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_forskola OWNER TO "xplore-admin";

--
-- TOC entry 747 (class 1259 OID 78814)
-- Name: standard_kommunkarta_punkt_id_seq17; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq17 OWNER TO "xplore-admin";

--
-- TOC entry 748 (class 1259 OID 78816)
-- Name: karta_fotbollsplaner; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_fotbollsplaner (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq17'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_fotbollsplaner OWNER TO "xplore-admin";

--
-- TOC entry 749 (class 1259 OID 78823)
-- Name: standard_kommunkarta_punkt_id_seq18; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq18 OWNER TO "xplore-admin";

--
-- TOC entry 750 (class 1259 OID 78825)
-- Name: karta_friidrottsanl; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_friidrottsanl (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq18'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_friidrottsanl OWNER TO "xplore-admin";

--
-- TOC entry 751 (class 1259 OID 78832)
-- Name: standard_kommunkarta_punkt_id_seq21; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq21 OWNER TO "xplore-admin";

--
-- TOC entry 752 (class 1259 OID 78834)
-- Name: karta_fritidsgard; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_fritidsgard (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq21'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_fritidsgard OWNER TO "xplore-admin";

--
-- TOC entry 753 (class 1259 OID 78841)
-- Name: standard_kommunkarta_punkt_id_seq40; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq40 OWNER TO "xplore-admin";

--
-- TOC entry 754 (class 1259 OID 78843)
-- Name: karta_fritidshem; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_fritidshem (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq40'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_fritidshem OWNER TO "xplore-admin";

--
-- TOC entry 755 (class 1259 OID 78850)
-- Name: standard_kommunkarta_punkt_id_seq16; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq16 OWNER TO "xplore-admin";

--
-- TOC entry 756 (class 1259 OID 78852)
-- Name: karta_golfbanor; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_golfbanor (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq16'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_golfbanor OWNER TO "xplore-admin";

--
-- TOC entry 757 (class 1259 OID 78859)
-- Name: standard_kommunkarta_yta_id_seq2; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq2 OWNER TO "xplore-admin";

--
-- TOC entry 758 (class 1259 OID 78861)
-- Name: karta_grav-uppstallningstillstand; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_grav-uppstallningstillstand" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq2'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_grav-uppstallningstillstand" OWNER TO "xplore-admin";

--
-- TOC entry 759 (class 1259 OID 78868)
-- Name: standard_kommunkarta_punkt_id_seq74; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq74 OWNER TO "xplore-admin";

--
-- TOC entry 760 (class 1259 OID 78870)
-- Name: karta_gym; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_gym (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq74'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_gym OWNER TO "xplore-admin";

--
-- TOC entry 761 (class 1259 OID 78877)
-- Name: standard_kommunkarta_punkt_id_seq39; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq39 OWNER TO "xplore-admin";

--
-- TOC entry 762 (class 1259 OID 78879)
-- Name: karta_hamn; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_hamn (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq39'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_hamn OWNER TO "xplore-admin";

--
-- TOC entry 763 (class 1259 OID 78886)
-- Name: standard_kommunkarta_punkt_id_seq71; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq71 OWNER TO "xplore-admin";

--
-- TOC entry 764 (class 1259 OID 78888)
-- Name: karta_hembygdsforening; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_hembygdsforening (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq71'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_hembygdsforening OWNER TO "xplore-admin";

--
-- TOC entry 765 (class 1259 OID 78895)
-- Name: standard_kommunkarta_punkt_id_seq43; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq43 OWNER TO "xplore-admin";

--
-- TOC entry 766 (class 1259 OID 78897)
-- Name: karta_hjartstartare; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_hjartstartare (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq43'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_hjartstartare OWNER TO "xplore-admin";

--
-- TOC entry 767 (class 1259 OID 78904)
-- Name: standard_kommunkarta_punkt_id_seq73; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq73 OWNER TO "xplore-admin";

--
-- TOC entry 768 (class 1259 OID 78906)
-- Name: karta_hundklubb; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_hundklubb (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq73'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_hundklubb OWNER TO "xplore-admin";

--
-- TOC entry 769 (class 1259 OID 78913)
-- Name: standard_kommunkarta_punkt_id_seq76; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq76 OWNER TO "xplore-admin";

--
-- TOC entry 770 (class 1259 OID 78915)
-- Name: karta_hundpasar; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_hundpasar (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq76'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_hundpasar OWNER TO "xplore-admin";

--
-- TOC entry 771 (class 1259 OID 78922)
-- Name: standard_kommunkarta_punkt_id_seq19; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq19 OWNER TO "xplore-admin";

--
-- TOC entry 772 (class 1259 OID 78924)
-- Name: karta_idrottshall; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_idrottshall (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq19'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_idrottshall OWNER TO "xplore-admin";

--
-- TOC entry 773 (class 1259 OID 78931)
-- Name: standard_kommunkarta_punkt_id_seq2; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq2 OWNER TO "xplore-admin";

--
-- TOC entry 774 (class 1259 OID 78933)
-- Name: karta_isbanor; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_isbanor (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq2'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_isbanor OWNER TO "xplore-admin";

--
-- TOC entry 775 (class 1259 OID 78940)
-- Name: standard_kommunkarta_yta_id_seq7; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq7 OWNER TO "xplore-admin";

--
-- TOC entry 776 (class 1259 OID 78942)
-- Name: karta_kommunagd-mark; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_kommunagd-mark" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq7'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_kommunagd-mark" OWNER TO "xplore-admin";

--
-- TOC entry 777 (class 1259 OID 78949)
-- Name: standard_kommunkarta_punkt_id_seq7; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq7 OWNER TO "xplore-admin";

--
-- TOC entry 778 (class 1259 OID 78951)
-- Name: karta_kommunkontor; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_kommunkontor (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq7'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_kommunkontor OWNER TO "xplore-admin";

--
-- TOC entry 779 (class 1259 OID 78958)
-- Name: standard_kommunkarta_punkt_id_seq4; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq4 OWNER TO "xplore-admin";

--
-- TOC entry 780 (class 1259 OID 78960)
-- Name: karta_konsumentradgivning; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_konsumentradgivning (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq4'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_konsumentradgivning OWNER TO "xplore-admin";

--
-- TOC entry 781 (class 1259 OID 78967)
-- Name: standard_kommunkarta_punkt_id_seq69; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq69 OWNER TO "xplore-admin";

--
-- TOC entry 782 (class 1259 OID 78969)
-- Name: karta_kulturforening; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_kulturforening (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq69'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_kulturforening OWNER TO "xplore-admin";

--
-- TOC entry 783 (class 1259 OID 78976)
-- Name: standard_kommunkarta_punkt_id_seq78; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq78 OWNER TO "xplore-admin";

--
-- TOC entry 784 (class 1259 OID 78978)
-- Name: karta_kulturskolan; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_kulturskolan (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq78'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_kulturskolan OWNER TO "xplore-admin";

--
-- TOC entry 785 (class 1259 OID 78985)
-- Name: standard_kommunkarta_punkt_id_seq34; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq34 OWNER TO "xplore-admin";

--
-- TOC entry 786 (class 1259 OID 78987)
-- Name: karta_laddstationer; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_laddstationer (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq34'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_laddstationer OWNER TO "xplore-admin";

--
-- TOC entry 787 (class 1259 OID 78994)
-- Name: standard_kommunkarta_punkt_id_seq61; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq61 OWNER TO "xplore-admin";

--
-- TOC entry 788 (class 1259 OID 78996)
-- Name: karta_langdskidspar; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_langdskidspar (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq61'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_langdskidspar OWNER TO "xplore-admin";

--
-- TOC entry 789 (class 1259 OID 79003)
-- Name: standard_kommunkarta_yta_id_seq6; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq6 OWNER TO "xplore-admin";

--
-- TOC entry 790 (class 1259 OID 79005)
-- Name: karta_ledig-industrimark; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_ledig-industrimark" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq6'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_ledig-industrimark" OWNER TO "xplore-admin";

--
-- TOC entry 791 (class 1259 OID 79012)
-- Name: standard_kommunkarta_yta_id_seq9; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq9 OWNER TO "xplore-admin";

--
-- TOC entry 792 (class 1259 OID 79014)
-- Name: karta_ledig-industrimark_privatagd; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_ledig-industrimark_privatagd" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq9'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_ledig-industrimark_privatagd" OWNER TO "xplore-admin";

--
-- TOC entry 793 (class 1259 OID 79021)
-- Name: standard_kommunkarta_punkt_id_seq75; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq75 OWNER TO "xplore-admin";

--
-- TOC entry 794 (class 1259 OID 79023)
-- Name: karta_lediga-bostadstomter; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_lediga-bostadstomter" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq75'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_lediga-bostadstomter" OWNER TO "xplore-admin";

--
-- TOC entry 795 (class 1259 OID 79030)
-- Name: standard_kommunkarta_punkt_id_seq35; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq35 OWNER TO "xplore-admin";

--
-- TOC entry 796 (class 1259 OID 79032)
-- Name: karta_lekplats; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_lekplats (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq35'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_lekplats OWNER TO "xplore-admin";

--
-- TOC entry 797 (class 1259 OID 79039)
-- Name: standard_kommunkarta_punkt_id_seq51; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq51 OWNER TO "xplore-admin";

--
-- TOC entry 798 (class 1259 OID 79041)
-- Name: karta_livsmedelsbutik; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_livsmedelsbutik (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq51'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_livsmedelsbutik OWNER TO "xplore-admin";

--
-- TOC entry 799 (class 1259 OID 79048)
-- Name: standard_kommunkarta_punkt_id_seq22; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq22 OWNER TO "xplore-admin";

--
-- TOC entry 800 (class 1259 OID 79050)
-- Name: karta_motionsanl; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_motionsanl (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq22'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_motionsanl OWNER TO "xplore-admin";

--
-- TOC entry 801 (class 1259 OID 79057)
-- Name: standard_kommunkarta_linje_id_seq4; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_linje_id_seq4 OWNER TO "xplore-admin";

--
-- TOC entry 802 (class 1259 OID 79059)
-- Name: karta_motionsspar_elljusspar; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_motionsspar_elljusspar (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_linje_id_seq4'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_motionsspar_elljusspar OWNER TO "xplore-admin";

--
-- TOC entry 803 (class 1259 OID 79066)
-- Name: standard_kommunkarta_linje_id_seq5; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_linje_id_seq5 OWNER TO "xplore-admin";

--
-- TOC entry 804 (class 1259 OID 79068)
-- Name: karta_motionsspar_uppmarkta; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_motionsspar_uppmarkta (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_linje_id_seq5'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_motionsspar_uppmarkta OWNER TO "xplore-admin";

--
-- TOC entry 805 (class 1259 OID 79075)
-- Name: standard_kommunkarta_punkt_id_seq25; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq25 OWNER TO "xplore-admin";

--
-- TOC entry 806 (class 1259 OID 79077)
-- Name: karta_motorcrossbana; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_motorcrossbana (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq25'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_motorcrossbana OWNER TO "xplore-admin";

--
-- TOC entry 807 (class 1259 OID 79084)
-- Name: standard_kommunkarta_yta_id_seq12; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq12 OWNER TO "xplore-admin";

--
-- TOC entry 808 (class 1259 OID 79086)
-- Name: karta_naturreservat; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_naturreservat (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq12'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_naturreservat OWNER TO "xplore-admin";

--
-- TOC entry 809 (class 1259 OID 79093)
-- Name: standard_kommunkarta_yta_id_seq3; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq3 OWNER TO "xplore-admin";

--
-- TOC entry 810 (class 1259 OID 79095)
-- Name: karta_naturvardsprogram-objekt; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_naturvardsprogram-objekt" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq3'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_naturvardsprogram-objekt" OWNER TO "xplore-admin";

--
-- TOC entry 811 (class 1259 OID 79102)
-- Name: standard_kommunkarta_yta_id_seq10; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq10 OWNER TO "xplore-admin";

--
-- TOC entry 812 (class 1259 OID 79104)
-- Name: karta_naturvardsprogram-omraden; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_naturvardsprogram-omraden" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq10'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_naturvardsprogram-omraden" OWNER TO "xplore-admin";

--
-- TOC entry 813 (class 1259 OID 79111)
-- Name: standard_kommunkarta_punkt_id_seq38; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq38 OWNER TO "xplore-admin";

--
-- TOC entry 814 (class 1259 OID 79113)
-- Name: karta_obo; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_obo (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq38'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_obo OWNER TO "xplore-admin";

--
-- TOC entry 815 (class 1259 OID 79120)
-- Name: standard_kommunkarta_punkt_id_seq68; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq68 OWNER TO "xplore-admin";

--
-- TOC entry 816 (class 1259 OID 79122)
-- Name: karta_offentligatoaletter; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_offentligatoaletter (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq68'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_offentligatoaletter OWNER TO "xplore-admin";

--
-- TOC entry 817 (class 1259 OID 79129)
-- Name: standard_kommunkarta_punkt_id_seq10; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq10 OWNER TO "xplore-admin";

--
-- TOC entry 818 (class 1259 OID 79131)
-- Name: karta_okad-sysselsattning; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_okad-sysselsattning" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq10'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_okad-sysselsattning" OWNER TO "xplore-admin";

--
-- TOC entry 819 (class 1259 OID 79138)
-- Name: standard_kommunkarta_punkt_id_seq29; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq29 OWNER TO "xplore-admin";

--
-- TOC entry 820 (class 1259 OID 79140)
-- Name: karta_parkering-pendlar; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_parkering-pendlar" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq29'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_parkering-pendlar" OWNER TO "xplore-admin";

--
-- TOC entry 821 (class 1259 OID 79147)
-- Name: standard_kommunkarta_punkt_id_seq62; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq62 OWNER TO "xplore-admin";

--
-- TOC entry 822 (class 1259 OID 79149)
-- Name: karta_parkering_rorelsehindrade; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_parkering_rorelsehindrade (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq62'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_parkering_rorelsehindrade OWNER TO "xplore-admin";

--
-- TOC entry 823 (class 1259 OID 79156)
-- Name: standard_kommunkarta_punkt_id_seq30; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq30 OWNER TO "xplore-admin";

--
-- TOC entry 824 (class 1259 OID 79158)
-- Name: karta_parkeringsplatser; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_parkeringsplatser (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq30'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_parkeringsplatser OWNER TO "xplore-admin";

--
-- TOC entry 825 (class 1259 OID 79165)
-- Name: standard_kommunkarta_punkt_id_seq3; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq3 OWNER TO "xplore-admin";

--
-- TOC entry 826 (class 1259 OID 79167)
-- Name: karta_polis; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_polis (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq3'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_polis OWNER TO "xplore-admin";

--
-- TOC entry 827 (class 1259 OID 79174)
-- Name: standard_kommunkarta_punkt_id_seq63; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq63 OWNER TO "xplore-admin";

--
-- TOC entry 828 (class 1259 OID 79176)
-- Name: karta_postombud; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_postombud (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq63'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_postombud OWNER TO "xplore-admin";

--
-- TOC entry 829 (class 1259 OID 79183)
-- Name: standard_kommunkarta_punkt_id_seq23; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq23 OWNER TO "xplore-admin";

--
-- TOC entry 830 (class 1259 OID 79185)
-- Name: karta_racketanl; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_racketanl (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq23'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_racketanl OWNER TO "xplore-admin";

--
-- TOC entry 831 (class 1259 OID 79192)
-- Name: standard_kommunkarta_punkt_id_seq5; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq5 OWNER TO "xplore-admin";

--
-- TOC entry 832 (class 1259 OID 79194)
-- Name: karta_raddningstjanst; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_raddningstjanst (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq5'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_raddningstjanst OWNER TO "xplore-admin";

--
-- TOC entry 833 (class 1259 OID 79201)
-- Name: standard_kommunkarta_punkt_id_seq49; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq49 OWNER TO "xplore-admin";

--
-- TOC entry 834 (class 1259 OID 79203)
-- Name: karta_restauranger; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_restauranger (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq49'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_restauranger OWNER TO "xplore-admin";

--
-- TOC entry 835 (class 1259 OID 79210)
-- Name: standard_kommunkarta_punkt_id_seq26; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq26 OWNER TO "xplore-admin";

--
-- TOC entry 836 (class 1259 OID 79212)
-- Name: karta_ridanl; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_ridanl (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq26'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_ridanl OWNER TO "xplore-admin";

--
-- TOC entry 837 (class 1259 OID 79219)
-- Name: standard_kommunkarta_punkt_id_seq77; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq77 OWNER TO "xplore-admin";

--
-- TOC entry 838 (class 1259 OID 79221)
-- Name: karta_samlingslokal; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_samlingslokal (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq77'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_samlingslokal OWNER TO "xplore-admin";

--
-- TOC entry 839 (class 1259 OID 79228)
-- Name: standard_kommunkarta_punkt_id_seq48; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq48 OWNER TO "xplore-admin";

--
-- TOC entry 840 (class 1259 OID 79230)
-- Name: karta_sevardheter_besoksmal; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_sevardheter_besoksmal (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq48'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_sevardheter_besoksmal OWNER TO "xplore-admin";

--
-- TOC entry 841 (class 1259 OID 79237)
-- Name: standard_kommunkarta_punkt_id_seq47; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq47 OWNER TO "xplore-admin";

--
-- TOC entry 842 (class 1259 OID 79239)
-- Name: karta_sevardheter_kultur; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_sevardheter_kultur (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq47'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_sevardheter_kultur OWNER TO "xplore-admin";

--
-- TOC entry 843 (class 1259 OID 79246)
-- Name: standard_kommunkarta_punkt_id_seq46; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq46 OWNER TO "xplore-admin";

--
-- TOC entry 844 (class 1259 OID 79248)
-- Name: karta_sevardheter_natur; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_sevardheter_natur (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq46'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_sevardheter_natur OWNER TO "xplore-admin";

--
-- TOC entry 845 (class 1259 OID 79255)
-- Name: standard_kommunkarta_punkt_id_seq24; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq24 OWNER TO "xplore-admin";

--
-- TOC entry 846 (class 1259 OID 79257)
-- Name: karta_skatepark; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_skatepark (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq24'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_skatepark OWNER TO "xplore-admin";

--
-- TOC entry 847 (class 1259 OID 79264)
-- Name: standard_kommunkarta_punkt_id_seq64; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq64 OWNER TO "xplore-admin";

--
-- TOC entry 848 (class 1259 OID 79266)
-- Name: karta_skjutbana; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_skjutbana (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq64'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_skjutbana OWNER TO "xplore-admin";

--
-- TOC entry 849 (class 1259 OID 79273)
-- Name: standard_kommunkarta_punkt_id_seq; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq OWNER TO "xplore-admin";

--
-- TOC entry 850 (class 1259 OID 79275)
-- Name: karta_skola; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_skola (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_skola OWNER TO "xplore-admin";

--
-- TOC entry 851 (class 1259 OID 79282)
-- Name: standard_kommunkarta_punkt_id_seq37; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq37 OWNER TO "xplore-admin";

--
-- TOC entry 852 (class 1259 OID 79284)
-- Name: karta_socialkontoret; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_socialkontoret (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq37'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_socialkontoret OWNER TO "xplore-admin";

--
-- TOC entry 853 (class 1259 OID 79291)
-- Name: standard_kommunkarta_yta_id_seq11; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq11 OWNER TO "xplore-admin";

--
-- TOC entry 854 (class 1259 OID 79293)
-- Name: karta_sophamtning; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_sophamtning (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq11'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_sophamtning OWNER TO "xplore-admin";

--
-- TOC entry 855 (class 1259 OID 79300)
-- Name: standard_kommunkarta_punkt_id_seq72; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq72 OWNER TO "xplore-admin";

--
-- TOC entry 856 (class 1259 OID 79302)
-- Name: karta_spontanidrottsplats; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_spontanidrottsplats (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq72'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_spontanidrottsplats OWNER TO "xplore-admin";

--
-- TOC entry 857 (class 1259 OID 79309)
-- Name: standard_kommunkarta_punkt_id_seq65; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq65 OWNER TO "xplore-admin";

--
-- TOC entry 858 (class 1259 OID 79311)
-- Name: karta_systembolaget; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_systembolaget (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq65'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_systembolaget OWNER TO "xplore-admin";

--
-- TOC entry 859 (class 1259 OID 79318)
-- Name: standard_kommunkarta_yta_id_seq4; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_yta_id_seq4 OWNER TO "xplore-admin";

--
-- TOC entry 860 (class 1259 OID 79320)
-- Name: karta_torghandel; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_torghandel (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_yta_id_seq4'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_torghandel OWNER TO "xplore-admin";

--
-- TOC entry 861 (class 1259 OID 79327)
-- Name: standard_kommunkarta_punkt_id_seq12; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq12 OWNER TO "xplore-admin";

--
-- TOC entry 862 (class 1259 OID 79329)
-- Name: karta_traffpunkt-aktivitetshuset; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_traffpunkt-aktivitetshuset" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq12'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_traffpunkt-aktivitetshuset" OWNER TO "xplore-admin";

--
-- TOC entry 863 (class 1259 OID 79336)
-- Name: standard_kommunkarta_punkt_id_seq11; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq11 OWNER TO "xplore-admin";

--
-- TOC entry 864 (class 1259 OID 79338)
-- Name: karta_traffpunkt-aldre; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_traffpunkt-aldre" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq11'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_traffpunkt-aldre" OWNER TO "xplore-admin";

--
-- TOC entry 865 (class 1259 OID 79345)
-- Name: standard_kommunkarta_punkt_id_seq42; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq42 OWNER TO "xplore-admin";

--
-- TOC entry 866 (class 1259 OID 79347)
-- Name: karta_traffpunkt-funktionsnedsatta; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_traffpunkt-funktionsnedsatta" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq42'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_traffpunkt-funktionsnedsatta" OWNER TO "xplore-admin";

--
-- TOC entry 867 (class 1259 OID 79354)
-- Name: standard_kommunkarta_punkt_id_seq44; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq44 OWNER TO "xplore-admin";

--
-- TOC entry 868 (class 1259 OID 79356)
-- Name: karta_turistinformation; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_turistinformation (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq44'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_turistinformation OWNER TO "xplore-admin";

--
-- TOC entry 869 (class 1259 OID 79363)
-- Name: standard_kommunkarta_punkt_id_seq32; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq32 OWNER TO "xplore-admin";

--
-- TOC entry 870 (class 1259 OID 79365)
-- Name: karta_uppstallningsplats-husbilar; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_uppstallningsplats-husbilar" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq32'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_uppstallningsplats-husbilar" OWNER TO "xplore-admin";

--
-- TOC entry 871 (class 1259 OID 79372)
-- Name: standard_kommunkarta_punkt_id_seq66; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq66 OWNER TO "xplore-admin";

--
-- TOC entry 872 (class 1259 OID 79374)
-- Name: karta_utomhusgym; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_utomhusgym (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq66'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_utomhusgym OWNER TO "xplore-admin";

--
-- TOC entry 873 (class 1259 OID 79381)
-- Name: standard_kommunkarta_linje_id_seq1; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_linje_id_seq1 OWNER TO "xplore-admin";

--
-- TOC entry 874 (class 1259 OID 79383)
-- Name: karta_vandringsleder; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_vandringsleder (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_linje_id_seq1'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_vandringsleder OWNER TO "xplore-admin";

--
-- TOC entry 875 (class 1259 OID 79390)
-- Name: standard_kommunkarta_punkt_id_seq6; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq6 OWNER TO "xplore-admin";

--
-- TOC entry 876 (class 1259 OID 79392)
-- Name: karta_vardcentral; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis.karta_vardcentral (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq6'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis.karta_vardcentral OWNER TO "xplore-admin";

--
-- TOC entry 877 (class 1259 OID 79399)
-- Name: standard_kommunkarta_punkt_id_seq8; Type: SEQUENCE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE demo_geovis.standard_kommunkarta_punkt_id_seq8 OWNER TO "xplore-admin";

--
-- TOC entry 878 (class 1259 OID 79401)
-- Name: karta_viktigt-meddelande; Type: TABLE; Schema: demo_geovis; Owner: xplore-admin
--

CREATE TABLE demo_geovis."karta_viktigt-meddelande" (
    id integer DEFAULT nextval('demo_geovis.standard_kommunkarta_punkt_id_seq8'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE demo_geovis."karta_viktigt-meddelande" OWNER TO "xplore-admin";

--
-- TOC entry 7866 (class 2606 OID 79409)
-- Name: gotakanalbolag_kontor gotakanalbolag_kontor_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.gotakanalbolag_kontor
    ADD CONSTRAINT gotakanalbolag_kontor_pkey PRIMARY KEY (id);


--
-- TOC entry 7868 (class 2606 OID 79411)
-- Name: karta_apotek karta_apotek_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_apotek
    ADD CONSTRAINT karta_apotek_pkey PRIMARY KEY (id);


--
-- TOC entry 7870 (class 2606 OID 79413)
-- Name: karta_atervinningscentral karta_atervinningscentral_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_atervinningscentral
    ADD CONSTRAINT karta_atervinningscentral_pkey PRIMARY KEY (id);


--
-- TOC entry 7872 (class 2606 OID 79415)
-- Name: karta_atervinningsstation karta_atervinningsstation_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_atervinningsstation
    ADD CONSTRAINT karta_atervinningsstation_pkey PRIMARY KEY (id);


--
-- TOC entry 7874 (class 2606 OID 79417)
-- Name: karta_badplats karta_badplats_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_badplats
    ADD CONSTRAINT karta_badplats_pkey PRIMARY KEY (id);


--
-- TOC entry 7876 (class 2606 OID 79419)
-- Name: karta_bankomat karta_bankomat_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_bankomat
    ADD CONSTRAINT karta_bankomat_pkey PRIMARY KEY (id);


--
-- TOC entry 7878 (class 2606 OID 79421)
-- Name: karta_basketplan karta_basketplan_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_basketplan
    ADD CONSTRAINT karta_basketplan_pkey PRIMARY KEY (id);


--
-- TOC entry 7880 (class 2606 OID 79423)
-- Name: karta_batplatser karta_batplatser_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_batplatser
    ADD CONSTRAINT karta_batplatser_pkey PRIMARY KEY (id);


--
-- TOC entry 7882 (class 2606 OID 79425)
-- Name: karta_beachvolley karta_beachvolley_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_beachvolley
    ADD CONSTRAINT karta_beachvolley_pkey PRIMARY KEY (id);


--
-- TOC entry 7884 (class 2606 OID 79427)
-- Name: karta_begravningsplats karta_begravningsplats_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_begravningsplats
    ADD CONSTRAINT karta_begravningsplats_pkey PRIMARY KEY (id);


--
-- TOC entry 7886 (class 2606 OID 79429)
-- Name: karta_belaggningsarbete karta_belaggningsarbete_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_belaggningsarbete
    ADD CONSTRAINT karta_belaggningsarbete_pkey PRIMARY KEY (id);


--
-- TOC entry 7888 (class 2606 OID 79431)
-- Name: karta_bibliotek karta_bibliotek_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_bibliotek
    ADD CONSTRAINT karta_bibliotek_pkey PRIMARY KEY (id);


--
-- TOC entry 7890 (class 2606 OID 79433)
-- Name: karta_biograf karta_biograf_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_biograf
    ADD CONSTRAINT karta_biograf_pkey PRIMARY KEY (id);


--
-- TOC entry 7892 (class 2606 OID 79435)
-- Name: karta_boende karta_boende_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_boende
    ADD CONSTRAINT karta_boende_pkey PRIMARY KEY (id);


--
-- TOC entry 7894 (class 2606 OID 79437)
-- Name: karta_bostadstomter_ejbebyggda karta_bostadstomter_ejbebyggda_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_bostadstomter_ejbebyggda
    ADD CONSTRAINT karta_bostadstomter_ejbebyggda_pkey PRIMARY KEY (id);


--
-- TOC entry 7896 (class 2606 OID 79439)
-- Name: karta_boulebanor karta_boulebanor_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_boulebanor
    ADD CONSTRAINT karta_boulebanor_pkey PRIMARY KEY (id);


--
-- TOC entry 7898 (class 2606 OID 79441)
-- Name: karta_bowling karta_bowling_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_bowling
    ADD CONSTRAINT karta_bowling_pkey PRIMARY KEY (id);


--
-- TOC entry 7900 (class 2606 OID 79443)
-- Name: karta_busshallplats karta_busshallplats_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_busshallplats
    ADD CONSTRAINT karta_busshallplats_pkey PRIMARY KEY (id);


--
-- TOC entry 7902 (class 2606 OID 79445)
-- Name: karta_busskort karta_busskort_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_busskort
    ADD CONSTRAINT karta_busskort_pkey PRIMARY KEY (id);


--
-- TOC entry 7904 (class 2606 OID 79447)
-- Name: karta_busstation karta_busstation_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_busstation
    ADD CONSTRAINT karta_busstation_pkey PRIMARY KEY (id);


--
-- TOC entry 7906 (class 2606 OID 79449)
-- Name: karta_bygdegardar karta_bygdegardar_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_bygdegardar
    ADD CONSTRAINT karta_bygdegardar_pkey PRIMARY KEY (id);


--
-- TOC entry 7908 (class 2606 OID 79451)
-- Name: karta_cafe karta_cafe_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_cafe
    ADD CONSTRAINT karta_cafe_pkey PRIMARY KEY (id);


--
-- TOC entry 7910 (class 2606 OID 79453)
-- Name: karta_cykelleder karta_cykelleder_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_cykelleder
    ADD CONSTRAINT karta_cykelleder_pkey PRIMARY KEY (id);


--
-- TOC entry 7912 (class 2606 OID 79455)
-- Name: karta_cykelvagar karta_cykelvagar_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_cykelvagar
    ADD CONSTRAINT karta_cykelvagar_pkey PRIMARY KEY (id);


--
-- TOC entry 7914 (class 2606 OID 79457)
-- Name: karta_daglig-verksamhet karta_daglig-verksamhet_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_daglig-verksamhet"
    ADD CONSTRAINT "karta_daglig-verksamhet_pkey" PRIMARY KEY (id);


--
-- TOC entry 7916 (class 2606 OID 79459)
-- Name: karta_detaljplaner-gallande karta_detaljplaner-gallande_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_detaljplaner-gallande"
    ADD CONSTRAINT "karta_detaljplaner-gallande_pkey" PRIMARY KEY (id);


--
-- TOC entry 7918 (class 2606 OID 79461)
-- Name: karta_detaljplaner-pagaende karta_detaljplaner-pagaende_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_detaljplaner-pagaende"
    ADD CONSTRAINT "karta_detaljplaner-pagaende_pkey" PRIMARY KEY (id);


--
-- TOC entry 7920 (class 2606 OID 79463)
-- Name: karta_drivmedelsstation karta_drivmedelsstation_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_drivmedelsstation
    ADD CONSTRAINT karta_drivmedelsstation_pkey PRIMARY KEY (id);


--
-- TOC entry 7922 (class 2606 OID 79465)
-- Name: karta_fiske karta_fiske_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_fiske
    ADD CONSTRAINT karta_fiske_pkey PRIMARY KEY (id);


--
-- TOC entry 7924 (class 2606 OID 79467)
-- Name: karta_fiskekort karta_fiskekort_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_fiskekort
    ADD CONSTRAINT karta_fiskekort_pkey PRIMARY KEY (id);


--
-- TOC entry 7926 (class 2606 OID 79469)
-- Name: karta_folkets-park karta_folkets-park_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_folkets-park"
    ADD CONSTRAINT "karta_folkets-park_pkey" PRIMARY KEY (id);


--
-- TOC entry 7928 (class 2606 OID 79471)
-- Name: karta_folktandvard karta_folktandvard_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_folktandvard
    ADD CONSTRAINT karta_folktandvard_pkey PRIMARY KEY (id);


--
-- TOC entry 7930 (class 2606 OID 79473)
-- Name: karta_forskola karta_forskola_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_forskola
    ADD CONSTRAINT karta_forskola_pkey PRIMARY KEY (id);


--
-- TOC entry 7932 (class 2606 OID 79475)
-- Name: karta_fotbollsplaner karta_fotbollsplaner_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_fotbollsplaner
    ADD CONSTRAINT karta_fotbollsplaner_pkey PRIMARY KEY (id);


--
-- TOC entry 7934 (class 2606 OID 79477)
-- Name: karta_friidrottsanl karta_friidrottsanl_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_friidrottsanl
    ADD CONSTRAINT karta_friidrottsanl_pkey PRIMARY KEY (id);


--
-- TOC entry 7936 (class 2606 OID 79479)
-- Name: karta_fritidsgard karta_fritidsgard_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_fritidsgard
    ADD CONSTRAINT karta_fritidsgard_pkey PRIMARY KEY (id);


--
-- TOC entry 7938 (class 2606 OID 79481)
-- Name: karta_fritidshem karta_fritidshem_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_fritidshem
    ADD CONSTRAINT karta_fritidshem_pkey PRIMARY KEY (id);


--
-- TOC entry 7940 (class 2606 OID 79483)
-- Name: karta_golfbanor karta_golfbanor_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_golfbanor
    ADD CONSTRAINT karta_golfbanor_pkey PRIMARY KEY (id);


--
-- TOC entry 7942 (class 2606 OID 79485)
-- Name: karta_grav-uppstallningstillstand karta_grav-uppstallningstillstand_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_grav-uppstallningstillstand"
    ADD CONSTRAINT "karta_grav-uppstallningstillstand_pkey" PRIMARY KEY (id);


--
-- TOC entry 7944 (class 2606 OID 79487)
-- Name: karta_gym karta_gym_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_gym
    ADD CONSTRAINT karta_gym_pkey PRIMARY KEY (id);


--
-- TOC entry 7946 (class 2606 OID 79489)
-- Name: karta_hamn karta_hamn_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_hamn
    ADD CONSTRAINT karta_hamn_pkey PRIMARY KEY (id);


--
-- TOC entry 7948 (class 2606 OID 79491)
-- Name: karta_hembygdsforening karta_hembygdsforening_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_hembygdsforening
    ADD CONSTRAINT karta_hembygdsforening_pkey PRIMARY KEY (id);


--
-- TOC entry 7950 (class 2606 OID 79493)
-- Name: karta_hjartstartare karta_hjartstartare_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_hjartstartare
    ADD CONSTRAINT karta_hjartstartare_pkey PRIMARY KEY (id);


--
-- TOC entry 7952 (class 2606 OID 79495)
-- Name: karta_hundklubb karta_hundklubb_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_hundklubb
    ADD CONSTRAINT karta_hundklubb_pkey PRIMARY KEY (id);


--
-- TOC entry 7954 (class 2606 OID 79497)
-- Name: karta_hundpasar karta_hundpasar_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_hundpasar
    ADD CONSTRAINT karta_hundpasar_pkey PRIMARY KEY (id);


--
-- TOC entry 7956 (class 2606 OID 79499)
-- Name: karta_idrottshall karta_idrottshall_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_idrottshall
    ADD CONSTRAINT karta_idrottshall_pkey PRIMARY KEY (id);


--
-- TOC entry 7958 (class 2606 OID 79501)
-- Name: karta_isbanor karta_isbanor_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_isbanor
    ADD CONSTRAINT karta_isbanor_pkey PRIMARY KEY (id);


--
-- TOC entry 7960 (class 2606 OID 79503)
-- Name: karta_kommunagd-mark karta_kommunagd-mark_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_kommunagd-mark"
    ADD CONSTRAINT "karta_kommunagd-mark_pkey" PRIMARY KEY (id);


--
-- TOC entry 7962 (class 2606 OID 79505)
-- Name: karta_kommunkontor karta_kommunkontor_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_kommunkontor
    ADD CONSTRAINT karta_kommunkontor_pkey PRIMARY KEY (id);


--
-- TOC entry 7964 (class 2606 OID 79507)
-- Name: karta_konsumentradgivning karta_konsumentradgivning_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_konsumentradgivning
    ADD CONSTRAINT karta_konsumentradgivning_pkey PRIMARY KEY (id);


--
-- TOC entry 7966 (class 2606 OID 79509)
-- Name: karta_kulturforening karta_kulturforening_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_kulturforening
    ADD CONSTRAINT karta_kulturforening_pkey PRIMARY KEY (id);


--
-- TOC entry 7968 (class 2606 OID 79511)
-- Name: karta_kulturskolan karta_kulturskolan_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_kulturskolan
    ADD CONSTRAINT karta_kulturskolan_pkey PRIMARY KEY (id);


--
-- TOC entry 7970 (class 2606 OID 79513)
-- Name: karta_laddstationer karta_laddstationer_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_laddstationer
    ADD CONSTRAINT karta_laddstationer_pkey PRIMARY KEY (id);


--
-- TOC entry 7972 (class 2606 OID 79515)
-- Name: karta_langdskidspar karta_langdskidspar_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_langdskidspar
    ADD CONSTRAINT karta_langdskidspar_pkey PRIMARY KEY (id);


--
-- TOC entry 7974 (class 2606 OID 79517)
-- Name: karta_ledig-industrimark karta_ledig-industrimark_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_ledig-industrimark"
    ADD CONSTRAINT "karta_ledig-industrimark_pkey" PRIMARY KEY (id);


--
-- TOC entry 7976 (class 2606 OID 79519)
-- Name: karta_ledig-industrimark_privatagd karta_ledig-industrimark_privatagd_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_ledig-industrimark_privatagd"
    ADD CONSTRAINT "karta_ledig-industrimark_privatagd_pkey" PRIMARY KEY (id);


--
-- TOC entry 7978 (class 2606 OID 79521)
-- Name: karta_lediga-bostadstomter karta_lediga-bostadstomter_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_lediga-bostadstomter"
    ADD CONSTRAINT "karta_lediga-bostadstomter_pkey" PRIMARY KEY (id);


--
-- TOC entry 7980 (class 2606 OID 79523)
-- Name: karta_lekplats karta_lekplats_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_lekplats
    ADD CONSTRAINT karta_lekplats_pkey PRIMARY KEY (id);


--
-- TOC entry 7982 (class 2606 OID 79525)
-- Name: karta_livsmedelsbutik karta_livsmedelsbutik_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_livsmedelsbutik
    ADD CONSTRAINT karta_livsmedelsbutik_pkey PRIMARY KEY (id);


--
-- TOC entry 7984 (class 2606 OID 79527)
-- Name: karta_motionsanl karta_motionsanl_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_motionsanl
    ADD CONSTRAINT karta_motionsanl_pkey PRIMARY KEY (id);


--
-- TOC entry 7986 (class 2606 OID 79529)
-- Name: karta_motionsspar_elljusspar karta_motionsspar_elljusspar_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_motionsspar_elljusspar
    ADD CONSTRAINT karta_motionsspar_elljusspar_pkey PRIMARY KEY (id);


--
-- TOC entry 7988 (class 2606 OID 79531)
-- Name: karta_motionsspar_uppmarkta karta_motionsspar_uppmarkta_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_motionsspar_uppmarkta
    ADD CONSTRAINT karta_motionsspar_uppmarkta_pkey PRIMARY KEY (id);


--
-- TOC entry 7990 (class 2606 OID 79533)
-- Name: karta_motorcrossbana karta_motorcrossbana_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_motorcrossbana
    ADD CONSTRAINT karta_motorcrossbana_pkey PRIMARY KEY (id);


--
-- TOC entry 7992 (class 2606 OID 79535)
-- Name: karta_naturreservat karta_naturreservat_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_naturreservat
    ADD CONSTRAINT karta_naturreservat_pkey PRIMARY KEY (id);


--
-- TOC entry 7994 (class 2606 OID 79537)
-- Name: karta_naturvardsprogram-objekt karta_naturvardsprogram-objekt_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_naturvardsprogram-objekt"
    ADD CONSTRAINT "karta_naturvardsprogram-objekt_pkey" PRIMARY KEY (id);


--
-- TOC entry 7996 (class 2606 OID 79539)
-- Name: karta_naturvardsprogram-omraden karta_naturvardsprogram-omraden_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_naturvardsprogram-omraden"
    ADD CONSTRAINT "karta_naturvardsprogram-omraden_pkey" PRIMARY KEY (id);


--
-- TOC entry 7998 (class 2606 OID 79541)
-- Name: karta_obo karta_obo_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_obo
    ADD CONSTRAINT karta_obo_pkey PRIMARY KEY (id);


--
-- TOC entry 8000 (class 2606 OID 79543)
-- Name: karta_offentligatoaletter karta_offentligatoaletter_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_offentligatoaletter
    ADD CONSTRAINT karta_offentligatoaletter_pkey PRIMARY KEY (id);


--
-- TOC entry 8002 (class 2606 OID 79545)
-- Name: karta_okad-sysselsattning karta_okad-sysselsattning_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_okad-sysselsattning"
    ADD CONSTRAINT "karta_okad-sysselsattning_pkey" PRIMARY KEY (id);


--
-- TOC entry 8004 (class 2606 OID 79547)
-- Name: karta_parkering-pendlar karta_parkering-pendlar_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_parkering-pendlar"
    ADD CONSTRAINT "karta_parkering-pendlar_pkey" PRIMARY KEY (id);


--
-- TOC entry 8006 (class 2606 OID 79549)
-- Name: karta_parkering_rorelsehindrade karta_parkering_rorelsehindrade_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_parkering_rorelsehindrade
    ADD CONSTRAINT karta_parkering_rorelsehindrade_pkey PRIMARY KEY (id);


--
-- TOC entry 8008 (class 2606 OID 79551)
-- Name: karta_parkeringsplatser karta_parkeringsplatser_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_parkeringsplatser
    ADD CONSTRAINT karta_parkeringsplatser_pkey PRIMARY KEY (id);


--
-- TOC entry 8010 (class 2606 OID 79553)
-- Name: karta_polis karta_polis_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_polis
    ADD CONSTRAINT karta_polis_pkey PRIMARY KEY (id);


--
-- TOC entry 8012 (class 2606 OID 79555)
-- Name: karta_postombud karta_postombud_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_postombud
    ADD CONSTRAINT karta_postombud_pkey PRIMARY KEY (id);


--
-- TOC entry 8014 (class 2606 OID 79557)
-- Name: karta_racketanl karta_racketanl_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_racketanl
    ADD CONSTRAINT karta_racketanl_pkey PRIMARY KEY (id);


--
-- TOC entry 8016 (class 2606 OID 79559)
-- Name: karta_raddningstjanst karta_raddningstjanst_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_raddningstjanst
    ADD CONSTRAINT karta_raddningstjanst_pkey PRIMARY KEY (id);


--
-- TOC entry 8018 (class 2606 OID 79561)
-- Name: karta_restauranger karta_restauranger_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_restauranger
    ADD CONSTRAINT karta_restauranger_pkey PRIMARY KEY (id);


--
-- TOC entry 8020 (class 2606 OID 79563)
-- Name: karta_ridanl karta_ridanl_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_ridanl
    ADD CONSTRAINT karta_ridanl_pkey PRIMARY KEY (id);


--
-- TOC entry 8022 (class 2606 OID 79565)
-- Name: karta_samlingslokal karta_samlingslokal_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_samlingslokal
    ADD CONSTRAINT karta_samlingslokal_pkey PRIMARY KEY (id);


--
-- TOC entry 8024 (class 2606 OID 79567)
-- Name: karta_sevardheter_besoksmal karta_sevardheter_besoksmal_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_sevardheter_besoksmal
    ADD CONSTRAINT karta_sevardheter_besoksmal_pkey PRIMARY KEY (id);


--
-- TOC entry 8026 (class 2606 OID 79569)
-- Name: karta_sevardheter_kultur karta_sevardheter_kultur_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_sevardheter_kultur
    ADD CONSTRAINT karta_sevardheter_kultur_pkey PRIMARY KEY (id);


--
-- TOC entry 8028 (class 2606 OID 79571)
-- Name: karta_sevardheter_natur karta_sevardheter_natur_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_sevardheter_natur
    ADD CONSTRAINT karta_sevardheter_natur_pkey PRIMARY KEY (id);


--
-- TOC entry 8030 (class 2606 OID 79573)
-- Name: karta_skatepark karta_skatepark_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_skatepark
    ADD CONSTRAINT karta_skatepark_pkey PRIMARY KEY (id);


--
-- TOC entry 8032 (class 2606 OID 79575)
-- Name: karta_skjutbana karta_skjutbana_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_skjutbana
    ADD CONSTRAINT karta_skjutbana_pkey PRIMARY KEY (id);


--
-- TOC entry 8034 (class 2606 OID 79577)
-- Name: karta_skola karta_skola_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_skola
    ADD CONSTRAINT karta_skola_pkey PRIMARY KEY (id);


--
-- TOC entry 8036 (class 2606 OID 79579)
-- Name: karta_socialkontoret karta_socialkontoret_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_socialkontoret
    ADD CONSTRAINT karta_socialkontoret_pkey PRIMARY KEY (id);


--
-- TOC entry 8038 (class 2606 OID 79581)
-- Name: karta_sophamtning karta_sophamtning_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_sophamtning
    ADD CONSTRAINT karta_sophamtning_pkey PRIMARY KEY (id);


--
-- TOC entry 8040 (class 2606 OID 79583)
-- Name: karta_spontanidrottsplats karta_spontanidrottsplats_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_spontanidrottsplats
    ADD CONSTRAINT karta_spontanidrottsplats_pkey PRIMARY KEY (id);


--
-- TOC entry 8042 (class 2606 OID 79585)
-- Name: karta_systembolaget karta_systembolaget_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_systembolaget
    ADD CONSTRAINT karta_systembolaget_pkey PRIMARY KEY (id);


--
-- TOC entry 8044 (class 2606 OID 79587)
-- Name: karta_torghandel karta_torghandel_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_torghandel
    ADD CONSTRAINT karta_torghandel_pkey PRIMARY KEY (id);


--
-- TOC entry 8046 (class 2606 OID 79589)
-- Name: karta_traffpunkt-aktivitetshuset karta_traffpunkt-aktivitetshuset_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_traffpunkt-aktivitetshuset"
    ADD CONSTRAINT "karta_traffpunkt-aktivitetshuset_pkey" PRIMARY KEY (id);


--
-- TOC entry 8048 (class 2606 OID 79591)
-- Name: karta_traffpunkt-aldre karta_traffpunkt-aldre_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_traffpunkt-aldre"
    ADD CONSTRAINT "karta_traffpunkt-aldre_pkey" PRIMARY KEY (id);


--
-- TOC entry 8050 (class 2606 OID 79593)
-- Name: karta_traffpunkt-funktionsnedsatta karta_traffpunkt-funktionsnedsatta_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_traffpunkt-funktionsnedsatta"
    ADD CONSTRAINT "karta_traffpunkt-funktionsnedsatta_pkey" PRIMARY KEY (id);


--
-- TOC entry 8052 (class 2606 OID 79595)
-- Name: karta_turistinformation karta_turistinformation_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_turistinformation
    ADD CONSTRAINT karta_turistinformation_pkey PRIMARY KEY (id);


--
-- TOC entry 8054 (class 2606 OID 79597)
-- Name: karta_uppstallningsplats-husbilar karta_uppstallningsplats-husbilar_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_uppstallningsplats-husbilar"
    ADD CONSTRAINT "karta_uppstallningsplats-husbilar_pkey" PRIMARY KEY (id);


--
-- TOC entry 8056 (class 2606 OID 79599)
-- Name: karta_utomhusgym karta_utomhusgym_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_utomhusgym
    ADD CONSTRAINT karta_utomhusgym_pkey PRIMARY KEY (id);


--
-- TOC entry 8058 (class 2606 OID 79601)
-- Name: karta_vandringsleder karta_vandringsleder_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_vandringsleder
    ADD CONSTRAINT karta_vandringsleder_pkey PRIMARY KEY (id);


--
-- TOC entry 8060 (class 2606 OID 79603)
-- Name: karta_vardcentral karta_vardcentral_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis.karta_vardcentral
    ADD CONSTRAINT karta_vardcentral_pkey PRIMARY KEY (id);


--
-- TOC entry 8062 (class 2606 OID 79605)
-- Name: karta_viktigt-meddelande karta_viktigt-meddelande_pkey; Type: CONSTRAINT; Schema: demo_geovis; Owner: xplore-admin
--

ALTER TABLE ONLY demo_geovis."karta_viktigt-meddelande"
    ADD CONSTRAINT "karta_viktigt-meddelande_pkey" PRIMARY KEY (id);


--
-- TOC entry 8196 (class 0 OID 0)
-- Dependencies: 11
-- Name: SCHEMA demo_geovis; Type: ACL; Schema: -; Owner: geovis
--

GRANT USAGE ON SCHEMA demo_geovis TO demo_geovis_read;


--
-- TOC entry 8197 (class 0 OID 0)
-- Dependencies: 681
-- Name: SEQUENCE batplatser_aneby_id_seq; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.batplatser_aneby_id_seq TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.batplatser_aneby_id_seq TO demo_geovis_read;


--
-- TOC entry 8198 (class 0 OID 0)
-- Dependencies: 682
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq79; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq79 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq79 TO demo_geovis_read;


--
-- TOC entry 8199 (class 0 OID 0)
-- Dependencies: 683
-- Name: TABLE gotakanalbolag_kontor; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.gotakanalbolag_kontor TO geovis;
GRANT SELECT ON TABLE demo_geovis.gotakanalbolag_kontor TO demo_geovis_read;


--
-- TOC entry 8200 (class 0 OID 0)
-- Dependencies: 684
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq33; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq33 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq33 TO demo_geovis_read;


--
-- TOC entry 8201 (class 0 OID 0)
-- Dependencies: 685
-- Name: TABLE karta_apotek; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_apotek TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_apotek TO demo_geovis_read;


--
-- TOC entry 8202 (class 0 OID 0)
-- Dependencies: 686
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq27; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq27 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq27 TO demo_geovis_read;


--
-- TOC entry 8203 (class 0 OID 0)
-- Dependencies: 687
-- Name: TABLE karta_atervinningscentral; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_atervinningscentral TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_atervinningscentral TO demo_geovis_read;


--
-- TOC entry 8204 (class 0 OID 0)
-- Dependencies: 688
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq28; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq28 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq28 TO demo_geovis_read;


--
-- TOC entry 8205 (class 0 OID 0)
-- Dependencies: 689
-- Name: TABLE karta_atervinningsstation; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_atervinningsstation TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_atervinningsstation TO demo_geovis_read;


--
-- TOC entry 8206 (class 0 OID 0)
-- Dependencies: 690
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq20; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq20 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq20 TO demo_geovis_read;


--
-- TOC entry 8207 (class 0 OID 0)
-- Dependencies: 691
-- Name: TABLE karta_badplats; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_badplats TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_badplats TO demo_geovis_read;


--
-- TOC entry 8208 (class 0 OID 0)
-- Dependencies: 692
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq41; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq41 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq41 TO demo_geovis_read;


--
-- TOC entry 8209 (class 0 OID 0)
-- Dependencies: 693
-- Name: TABLE karta_bankomat; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_bankomat TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_bankomat TO demo_geovis_read;


--
-- TOC entry 8210 (class 0 OID 0)
-- Dependencies: 694
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq56; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq56 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq56 TO demo_geovis_read;


--
-- TOC entry 8211 (class 0 OID 0)
-- Dependencies: 695
-- Name: TABLE karta_basketplan; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_basketplan TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_basketplan TO demo_geovis_read;


--
-- TOC entry 8212 (class 0 OID 0)
-- Dependencies: 696
-- Name: TABLE karta_batplatser; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_batplatser TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_batplatser TO demo_geovis_read;


--
-- TOC entry 8213 (class 0 OID 0)
-- Dependencies: 697
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq57; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq57 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq57 TO demo_geovis_read;


--
-- TOC entry 8214 (class 0 OID 0)
-- Dependencies: 698
-- Name: TABLE karta_beachvolley; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_beachvolley TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_beachvolley TO demo_geovis_read;


--
-- TOC entry 8215 (class 0 OID 0)
-- Dependencies: 699
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq58; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq58 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq58 TO demo_geovis_read;


--
-- TOC entry 8216 (class 0 OID 0)
-- Dependencies: 700
-- Name: TABLE karta_begravningsplats; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_begravningsplats TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_begravningsplats TO demo_geovis_read;


--
-- TOC entry 8217 (class 0 OID 0)
-- Dependencies: 701
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq TO demo_geovis_read;


--
-- TOC entry 8218 (class 0 OID 0)
-- Dependencies: 702
-- Name: TABLE karta_belaggningsarbete; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_belaggningsarbete TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_belaggningsarbete TO demo_geovis_read;


--
-- TOC entry 8219 (class 0 OID 0)
-- Dependencies: 703
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq13; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq13 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq13 TO demo_geovis_read;


--
-- TOC entry 8220 (class 0 OID 0)
-- Dependencies: 704
-- Name: TABLE karta_bibliotek; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_bibliotek TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_bibliotek TO demo_geovis_read;


--
-- TOC entry 8221 (class 0 OID 0)
-- Dependencies: 705
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq14; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq14 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq14 TO demo_geovis_read;


--
-- TOC entry 8222 (class 0 OID 0)
-- Dependencies: 706
-- Name: TABLE karta_biograf; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_biograf TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_biograf TO demo_geovis_read;


--
-- TOC entry 8223 (class 0 OID 0)
-- Dependencies: 707
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq45; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq45 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq45 TO demo_geovis_read;


--
-- TOC entry 8224 (class 0 OID 0)
-- Dependencies: 708
-- Name: TABLE karta_boende; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_boende TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_boende TO demo_geovis_read;


--
-- TOC entry 8225 (class 0 OID 0)
-- Dependencies: 709
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq8; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq8 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq8 TO demo_geovis_read;


--
-- TOC entry 8226 (class 0 OID 0)
-- Dependencies: 710
-- Name: TABLE karta_bostadstomter_ejbebyggda; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_bostadstomter_ejbebyggda TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_bostadstomter_ejbebyggda TO demo_geovis_read;


--
-- TOC entry 8227 (class 0 OID 0)
-- Dependencies: 711
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq59; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq59 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq59 TO demo_geovis_read;


--
-- TOC entry 8228 (class 0 OID 0)
-- Dependencies: 712
-- Name: TABLE karta_boulebanor; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_boulebanor TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_boulebanor TO demo_geovis_read;


--
-- TOC entry 8229 (class 0 OID 0)
-- Dependencies: 713
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq67; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq67 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq67 TO demo_geovis_read;


--
-- TOC entry 8230 (class 0 OID 0)
-- Dependencies: 714
-- Name: TABLE karta_bowling; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_bowling TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_bowling TO demo_geovis_read;


--
-- TOC entry 8231 (class 0 OID 0)
-- Dependencies: 715
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq53; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq53 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq53 TO demo_geovis_read;


--
-- TOC entry 8232 (class 0 OID 0)
-- Dependencies: 716
-- Name: TABLE karta_busshallplats; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_busshallplats TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_busshallplats TO demo_geovis_read;


--
-- TOC entry 8233 (class 0 OID 0)
-- Dependencies: 717
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq60; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq60 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq60 TO demo_geovis_read;


--
-- TOC entry 8234 (class 0 OID 0)
-- Dependencies: 718
-- Name: TABLE karta_busskort; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_busskort TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_busskort TO demo_geovis_read;


--
-- TOC entry 8235 (class 0 OID 0)
-- Dependencies: 719
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq31; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq31 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq31 TO demo_geovis_read;


--
-- TOC entry 8236 (class 0 OID 0)
-- Dependencies: 720
-- Name: TABLE karta_busstation; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_busstation TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_busstation TO demo_geovis_read;


--
-- TOC entry 8237 (class 0 OID 0)
-- Dependencies: 721
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq70; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq70 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq70 TO demo_geovis_read;


--
-- TOC entry 8238 (class 0 OID 0)
-- Dependencies: 722
-- Name: TABLE karta_bygdegardar; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_bygdegardar TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_bygdegardar TO demo_geovis_read;


--
-- TOC entry 8239 (class 0 OID 0)
-- Dependencies: 723
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq50; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq50 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq50 TO demo_geovis_read;


--
-- TOC entry 8240 (class 0 OID 0)
-- Dependencies: 724
-- Name: TABLE karta_cafe; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_cafe TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_cafe TO demo_geovis_read;


--
-- TOC entry 8241 (class 0 OID 0)
-- Dependencies: 725
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq2; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq2 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq2 TO demo_geovis_read;


--
-- TOC entry 8242 (class 0 OID 0)
-- Dependencies: 726
-- Name: TABLE karta_cykelleder; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_cykelleder TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_cykelleder TO demo_geovis_read;


--
-- TOC entry 8243 (class 0 OID 0)
-- Dependencies: 727
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq3; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq3 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq3 TO demo_geovis_read;


--
-- TOC entry 8244 (class 0 OID 0)
-- Dependencies: 728
-- Name: TABLE karta_cykelvagar; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_cykelvagar TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_cykelvagar TO demo_geovis_read;


--
-- TOC entry 8245 (class 0 OID 0)
-- Dependencies: 729
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq9; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq9 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq9 TO demo_geovis_read;


--
-- TOC entry 8246 (class 0 OID 0)
-- Dependencies: 730
-- Name: TABLE "karta_daglig-verksamhet"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_daglig-verksamhet" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_daglig-verksamhet" TO demo_geovis_read;


--
-- TOC entry 8247 (class 0 OID 0)
-- Dependencies: 731
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq TO demo_geovis_read;


--
-- TOC entry 8248 (class 0 OID 0)
-- Dependencies: 732
-- Name: TABLE "karta_detaljplaner-gallande"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_detaljplaner-gallande" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_detaljplaner-gallande" TO demo_geovis_read;


--
-- TOC entry 8249 (class 0 OID 0)
-- Dependencies: 733
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq1; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq1 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq1 TO demo_geovis_read;


--
-- TOC entry 8250 (class 0 OID 0)
-- Dependencies: 734
-- Name: TABLE "karta_detaljplaner-pagaende"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_detaljplaner-pagaende" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_detaljplaner-pagaende" TO demo_geovis_read;


--
-- TOC entry 8251 (class 0 OID 0)
-- Dependencies: 735
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq52; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq52 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq52 TO demo_geovis_read;


--
-- TOC entry 8252 (class 0 OID 0)
-- Dependencies: 736
-- Name: TABLE karta_drivmedelsstation; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_drivmedelsstation TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_drivmedelsstation TO demo_geovis_read;


--
-- TOC entry 8253 (class 0 OID 0)
-- Dependencies: 737
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq54; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq54 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq54 TO demo_geovis_read;


--
-- TOC entry 8254 (class 0 OID 0)
-- Dependencies: 738
-- Name: TABLE karta_fiske; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_fiske TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_fiske TO demo_geovis_read;


--
-- TOC entry 8255 (class 0 OID 0)
-- Dependencies: 739
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq55; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq55 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq55 TO demo_geovis_read;


--
-- TOC entry 8256 (class 0 OID 0)
-- Dependencies: 740
-- Name: TABLE karta_fiskekort; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_fiskekort TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_fiskekort TO demo_geovis_read;


--
-- TOC entry 8257 (class 0 OID 0)
-- Dependencies: 741
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq15; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq15 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq15 TO demo_geovis_read;


--
-- TOC entry 8258 (class 0 OID 0)
-- Dependencies: 742
-- Name: TABLE "karta_folkets-park"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_folkets-park" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_folkets-park" TO demo_geovis_read;


--
-- TOC entry 8259 (class 0 OID 0)
-- Dependencies: 743
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq36; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq36 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq36 TO demo_geovis_read;


--
-- TOC entry 8260 (class 0 OID 0)
-- Dependencies: 744
-- Name: TABLE karta_folktandvard; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_folktandvard TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_folktandvard TO demo_geovis_read;


--
-- TOC entry 8261 (class 0 OID 0)
-- Dependencies: 745
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq1; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq1 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq1 TO demo_geovis_read;


--
-- TOC entry 8262 (class 0 OID 0)
-- Dependencies: 746
-- Name: TABLE karta_forskola; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_forskola TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_forskola TO demo_geovis_read;


--
-- TOC entry 8263 (class 0 OID 0)
-- Dependencies: 747
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq17; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq17 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq17 TO demo_geovis_read;


--
-- TOC entry 8264 (class 0 OID 0)
-- Dependencies: 748
-- Name: TABLE karta_fotbollsplaner; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_fotbollsplaner TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_fotbollsplaner TO demo_geovis_read;


--
-- TOC entry 8265 (class 0 OID 0)
-- Dependencies: 749
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq18; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq18 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq18 TO demo_geovis_read;


--
-- TOC entry 8266 (class 0 OID 0)
-- Dependencies: 750
-- Name: TABLE karta_friidrottsanl; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_friidrottsanl TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_friidrottsanl TO demo_geovis_read;


--
-- TOC entry 8267 (class 0 OID 0)
-- Dependencies: 751
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq21; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq21 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq21 TO demo_geovis_read;


--
-- TOC entry 8268 (class 0 OID 0)
-- Dependencies: 752
-- Name: TABLE karta_fritidsgard; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_fritidsgard TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_fritidsgard TO demo_geovis_read;


--
-- TOC entry 8269 (class 0 OID 0)
-- Dependencies: 753
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq40; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq40 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq40 TO demo_geovis_read;


--
-- TOC entry 8270 (class 0 OID 0)
-- Dependencies: 754
-- Name: TABLE karta_fritidshem; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_fritidshem TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_fritidshem TO demo_geovis_read;


--
-- TOC entry 8271 (class 0 OID 0)
-- Dependencies: 755
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq16; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq16 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq16 TO demo_geovis_read;


--
-- TOC entry 8272 (class 0 OID 0)
-- Dependencies: 756
-- Name: TABLE karta_golfbanor; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_golfbanor TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_golfbanor TO demo_geovis_read;


--
-- TOC entry 8273 (class 0 OID 0)
-- Dependencies: 757
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq2; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq2 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq2 TO demo_geovis_read;


--
-- TOC entry 8274 (class 0 OID 0)
-- Dependencies: 758
-- Name: TABLE "karta_grav-uppstallningstillstand"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_grav-uppstallningstillstand" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_grav-uppstallningstillstand" TO demo_geovis_read;


--
-- TOC entry 8275 (class 0 OID 0)
-- Dependencies: 759
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq74; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq74 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq74 TO demo_geovis_read;


--
-- TOC entry 8276 (class 0 OID 0)
-- Dependencies: 760
-- Name: TABLE karta_gym; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_gym TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_gym TO demo_geovis_read;


--
-- TOC entry 8277 (class 0 OID 0)
-- Dependencies: 761
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq39; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq39 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq39 TO demo_geovis_read;


--
-- TOC entry 8278 (class 0 OID 0)
-- Dependencies: 762
-- Name: TABLE karta_hamn; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_hamn TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_hamn TO demo_geovis_read;


--
-- TOC entry 8279 (class 0 OID 0)
-- Dependencies: 763
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq71; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq71 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq71 TO demo_geovis_read;


--
-- TOC entry 8280 (class 0 OID 0)
-- Dependencies: 764
-- Name: TABLE karta_hembygdsforening; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_hembygdsforening TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_hembygdsforening TO demo_geovis_read;


--
-- TOC entry 8281 (class 0 OID 0)
-- Dependencies: 765
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq43; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq43 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq43 TO demo_geovis_read;


--
-- TOC entry 8282 (class 0 OID 0)
-- Dependencies: 766
-- Name: TABLE karta_hjartstartare; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_hjartstartare TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_hjartstartare TO demo_geovis_read;


--
-- TOC entry 8283 (class 0 OID 0)
-- Dependencies: 767
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq73; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq73 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq73 TO demo_geovis_read;


--
-- TOC entry 8284 (class 0 OID 0)
-- Dependencies: 768
-- Name: TABLE karta_hundklubb; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_hundklubb TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_hundklubb TO demo_geovis_read;


--
-- TOC entry 8285 (class 0 OID 0)
-- Dependencies: 769
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq76; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq76 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq76 TO demo_geovis_read;


--
-- TOC entry 8286 (class 0 OID 0)
-- Dependencies: 770
-- Name: TABLE karta_hundpasar; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_hundpasar TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_hundpasar TO demo_geovis_read;


--
-- TOC entry 8287 (class 0 OID 0)
-- Dependencies: 771
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq19; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq19 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq19 TO demo_geovis_read;


--
-- TOC entry 8288 (class 0 OID 0)
-- Dependencies: 772
-- Name: TABLE karta_idrottshall; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_idrottshall TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_idrottshall TO demo_geovis_read;


--
-- TOC entry 8289 (class 0 OID 0)
-- Dependencies: 773
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq2; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq2 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq2 TO demo_geovis_read;


--
-- TOC entry 8290 (class 0 OID 0)
-- Dependencies: 774
-- Name: TABLE karta_isbanor; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_isbanor TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_isbanor TO demo_geovis_read;


--
-- TOC entry 8291 (class 0 OID 0)
-- Dependencies: 775
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq7; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq7 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq7 TO demo_geovis_read;


--
-- TOC entry 8292 (class 0 OID 0)
-- Dependencies: 776
-- Name: TABLE "karta_kommunagd-mark"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_kommunagd-mark" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_kommunagd-mark" TO demo_geovis_read;


--
-- TOC entry 8293 (class 0 OID 0)
-- Dependencies: 777
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq7; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq7 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq7 TO demo_geovis_read;


--
-- TOC entry 8294 (class 0 OID 0)
-- Dependencies: 778
-- Name: TABLE karta_kommunkontor; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_kommunkontor TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_kommunkontor TO demo_geovis_read;


--
-- TOC entry 8295 (class 0 OID 0)
-- Dependencies: 779
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq4; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq4 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq4 TO demo_geovis_read;


--
-- TOC entry 8296 (class 0 OID 0)
-- Dependencies: 780
-- Name: TABLE karta_konsumentradgivning; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_konsumentradgivning TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_konsumentradgivning TO demo_geovis_read;


--
-- TOC entry 8297 (class 0 OID 0)
-- Dependencies: 781
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq69; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq69 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq69 TO demo_geovis_read;


--
-- TOC entry 8298 (class 0 OID 0)
-- Dependencies: 782
-- Name: TABLE karta_kulturforening; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_kulturforening TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_kulturforening TO demo_geovis_read;


--
-- TOC entry 8299 (class 0 OID 0)
-- Dependencies: 783
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq78; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq78 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq78 TO demo_geovis_read;


--
-- TOC entry 8300 (class 0 OID 0)
-- Dependencies: 784
-- Name: TABLE karta_kulturskolan; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_kulturskolan TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_kulturskolan TO demo_geovis_read;


--
-- TOC entry 8301 (class 0 OID 0)
-- Dependencies: 785
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq34; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq34 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq34 TO demo_geovis_read;


--
-- TOC entry 8302 (class 0 OID 0)
-- Dependencies: 786
-- Name: TABLE karta_laddstationer; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_laddstationer TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_laddstationer TO demo_geovis_read;


--
-- TOC entry 8303 (class 0 OID 0)
-- Dependencies: 787
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq61; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq61 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq61 TO demo_geovis_read;


--
-- TOC entry 8304 (class 0 OID 0)
-- Dependencies: 788
-- Name: TABLE karta_langdskidspar; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_langdskidspar TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_langdskidspar TO demo_geovis_read;


--
-- TOC entry 8305 (class 0 OID 0)
-- Dependencies: 789
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq6; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq6 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq6 TO demo_geovis_read;


--
-- TOC entry 8306 (class 0 OID 0)
-- Dependencies: 790
-- Name: TABLE "karta_ledig-industrimark"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_ledig-industrimark" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_ledig-industrimark" TO demo_geovis_read;


--
-- TOC entry 8307 (class 0 OID 0)
-- Dependencies: 791
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq9; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq9 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq9 TO demo_geovis_read;


--
-- TOC entry 8308 (class 0 OID 0)
-- Dependencies: 792
-- Name: TABLE "karta_ledig-industrimark_privatagd"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_ledig-industrimark_privatagd" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_ledig-industrimark_privatagd" TO demo_geovis_read;


--
-- TOC entry 8309 (class 0 OID 0)
-- Dependencies: 793
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq75; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq75 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq75 TO demo_geovis_read;


--
-- TOC entry 8310 (class 0 OID 0)
-- Dependencies: 794
-- Name: TABLE "karta_lediga-bostadstomter"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_lediga-bostadstomter" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_lediga-bostadstomter" TO demo_geovis_read;


--
-- TOC entry 8311 (class 0 OID 0)
-- Dependencies: 795
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq35; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq35 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq35 TO demo_geovis_read;


--
-- TOC entry 8312 (class 0 OID 0)
-- Dependencies: 796
-- Name: TABLE karta_lekplats; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_lekplats TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_lekplats TO demo_geovis_read;


--
-- TOC entry 8313 (class 0 OID 0)
-- Dependencies: 797
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq51; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq51 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq51 TO demo_geovis_read;


--
-- TOC entry 8314 (class 0 OID 0)
-- Dependencies: 798
-- Name: TABLE karta_livsmedelsbutik; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_livsmedelsbutik TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_livsmedelsbutik TO demo_geovis_read;


--
-- TOC entry 8315 (class 0 OID 0)
-- Dependencies: 799
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq22; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq22 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq22 TO demo_geovis_read;


--
-- TOC entry 8316 (class 0 OID 0)
-- Dependencies: 800
-- Name: TABLE karta_motionsanl; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_motionsanl TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_motionsanl TO demo_geovis_read;


--
-- TOC entry 8317 (class 0 OID 0)
-- Dependencies: 801
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq4; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq4 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq4 TO demo_geovis_read;


--
-- TOC entry 8318 (class 0 OID 0)
-- Dependencies: 802
-- Name: TABLE karta_motionsspar_elljusspar; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_motionsspar_elljusspar TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_motionsspar_elljusspar TO demo_geovis_read;


--
-- TOC entry 8319 (class 0 OID 0)
-- Dependencies: 803
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq5; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq5 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq5 TO demo_geovis_read;


--
-- TOC entry 8320 (class 0 OID 0)
-- Dependencies: 804
-- Name: TABLE karta_motionsspar_uppmarkta; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_motionsspar_uppmarkta TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_motionsspar_uppmarkta TO demo_geovis_read;


--
-- TOC entry 8321 (class 0 OID 0)
-- Dependencies: 805
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq25; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq25 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq25 TO demo_geovis_read;


--
-- TOC entry 8322 (class 0 OID 0)
-- Dependencies: 806
-- Name: TABLE karta_motorcrossbana; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_motorcrossbana TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_motorcrossbana TO demo_geovis_read;


--
-- TOC entry 8323 (class 0 OID 0)
-- Dependencies: 807
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq12; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq12 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq12 TO demo_geovis_read;


--
-- TOC entry 8324 (class 0 OID 0)
-- Dependencies: 808
-- Name: TABLE karta_naturreservat; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_naturreservat TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_naturreservat TO demo_geovis_read;


--
-- TOC entry 8325 (class 0 OID 0)
-- Dependencies: 809
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq3; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq3 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq3 TO demo_geovis_read;


--
-- TOC entry 8326 (class 0 OID 0)
-- Dependencies: 810
-- Name: TABLE "karta_naturvardsprogram-objekt"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_naturvardsprogram-objekt" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_naturvardsprogram-objekt" TO demo_geovis_read;


--
-- TOC entry 8327 (class 0 OID 0)
-- Dependencies: 811
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq10; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq10 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq10 TO demo_geovis_read;


--
-- TOC entry 8328 (class 0 OID 0)
-- Dependencies: 812
-- Name: TABLE "karta_naturvardsprogram-omraden"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_naturvardsprogram-omraden" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_naturvardsprogram-omraden" TO demo_geovis_read;


--
-- TOC entry 8329 (class 0 OID 0)
-- Dependencies: 813
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq38; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq38 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq38 TO demo_geovis_read;


--
-- TOC entry 8330 (class 0 OID 0)
-- Dependencies: 814
-- Name: TABLE karta_obo; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_obo TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_obo TO demo_geovis_read;


--
-- TOC entry 8331 (class 0 OID 0)
-- Dependencies: 815
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq68; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq68 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq68 TO demo_geovis_read;


--
-- TOC entry 8332 (class 0 OID 0)
-- Dependencies: 816
-- Name: TABLE karta_offentligatoaletter; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_offentligatoaletter TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_offentligatoaletter TO demo_geovis_read;


--
-- TOC entry 8333 (class 0 OID 0)
-- Dependencies: 817
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq10; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq10 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq10 TO demo_geovis_read;


--
-- TOC entry 8334 (class 0 OID 0)
-- Dependencies: 818
-- Name: TABLE "karta_okad-sysselsattning"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_okad-sysselsattning" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_okad-sysselsattning" TO demo_geovis_read;


--
-- TOC entry 8335 (class 0 OID 0)
-- Dependencies: 819
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq29; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq29 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq29 TO demo_geovis_read;


--
-- TOC entry 8336 (class 0 OID 0)
-- Dependencies: 820
-- Name: TABLE "karta_parkering-pendlar"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_parkering-pendlar" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_parkering-pendlar" TO demo_geovis_read;


--
-- TOC entry 8337 (class 0 OID 0)
-- Dependencies: 821
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq62; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq62 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq62 TO demo_geovis_read;


--
-- TOC entry 8338 (class 0 OID 0)
-- Dependencies: 822
-- Name: TABLE karta_parkering_rorelsehindrade; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_parkering_rorelsehindrade TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_parkering_rorelsehindrade TO demo_geovis_read;


--
-- TOC entry 8339 (class 0 OID 0)
-- Dependencies: 823
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq30; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq30 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq30 TO demo_geovis_read;


--
-- TOC entry 8340 (class 0 OID 0)
-- Dependencies: 824
-- Name: TABLE karta_parkeringsplatser; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_parkeringsplatser TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_parkeringsplatser TO demo_geovis_read;


--
-- TOC entry 8341 (class 0 OID 0)
-- Dependencies: 825
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq3; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq3 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq3 TO demo_geovis_read;


--
-- TOC entry 8342 (class 0 OID 0)
-- Dependencies: 826
-- Name: TABLE karta_polis; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_polis TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_polis TO demo_geovis_read;


--
-- TOC entry 8343 (class 0 OID 0)
-- Dependencies: 827
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq63; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq63 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq63 TO demo_geovis_read;


--
-- TOC entry 8344 (class 0 OID 0)
-- Dependencies: 828
-- Name: TABLE karta_postombud; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_postombud TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_postombud TO demo_geovis_read;


--
-- TOC entry 8345 (class 0 OID 0)
-- Dependencies: 829
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq23; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq23 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq23 TO demo_geovis_read;


--
-- TOC entry 8346 (class 0 OID 0)
-- Dependencies: 830
-- Name: TABLE karta_racketanl; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_racketanl TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_racketanl TO demo_geovis_read;


--
-- TOC entry 8347 (class 0 OID 0)
-- Dependencies: 831
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq5; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq5 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq5 TO demo_geovis_read;


--
-- TOC entry 8348 (class 0 OID 0)
-- Dependencies: 832
-- Name: TABLE karta_raddningstjanst; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_raddningstjanst TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_raddningstjanst TO demo_geovis_read;


--
-- TOC entry 8349 (class 0 OID 0)
-- Dependencies: 833
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq49; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq49 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq49 TO demo_geovis_read;


--
-- TOC entry 8350 (class 0 OID 0)
-- Dependencies: 834
-- Name: TABLE karta_restauranger; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_restauranger TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_restauranger TO demo_geovis_read;


--
-- TOC entry 8351 (class 0 OID 0)
-- Dependencies: 835
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq26; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq26 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq26 TO demo_geovis_read;


--
-- TOC entry 8352 (class 0 OID 0)
-- Dependencies: 836
-- Name: TABLE karta_ridanl; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_ridanl TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_ridanl TO demo_geovis_read;


--
-- TOC entry 8353 (class 0 OID 0)
-- Dependencies: 837
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq77; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq77 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq77 TO demo_geovis_read;


--
-- TOC entry 8354 (class 0 OID 0)
-- Dependencies: 838
-- Name: TABLE karta_samlingslokal; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_samlingslokal TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_samlingslokal TO demo_geovis_read;


--
-- TOC entry 8355 (class 0 OID 0)
-- Dependencies: 839
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq48; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq48 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq48 TO demo_geovis_read;


--
-- TOC entry 8356 (class 0 OID 0)
-- Dependencies: 840
-- Name: TABLE karta_sevardheter_besoksmal; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_sevardheter_besoksmal TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_sevardheter_besoksmal TO demo_geovis_read;


--
-- TOC entry 8357 (class 0 OID 0)
-- Dependencies: 841
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq47; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq47 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq47 TO demo_geovis_read;


--
-- TOC entry 8358 (class 0 OID 0)
-- Dependencies: 842
-- Name: TABLE karta_sevardheter_kultur; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_sevardheter_kultur TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_sevardheter_kultur TO demo_geovis_read;


--
-- TOC entry 8359 (class 0 OID 0)
-- Dependencies: 843
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq46; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq46 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq46 TO demo_geovis_read;


--
-- TOC entry 8360 (class 0 OID 0)
-- Dependencies: 844
-- Name: TABLE karta_sevardheter_natur; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_sevardheter_natur TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_sevardheter_natur TO demo_geovis_read;


--
-- TOC entry 8361 (class 0 OID 0)
-- Dependencies: 845
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq24; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq24 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq24 TO demo_geovis_read;


--
-- TOC entry 8362 (class 0 OID 0)
-- Dependencies: 846
-- Name: TABLE karta_skatepark; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_skatepark TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_skatepark TO demo_geovis_read;


--
-- TOC entry 8363 (class 0 OID 0)
-- Dependencies: 847
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq64; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq64 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq64 TO demo_geovis_read;


--
-- TOC entry 8364 (class 0 OID 0)
-- Dependencies: 848
-- Name: TABLE karta_skjutbana; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_skjutbana TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_skjutbana TO demo_geovis_read;


--
-- TOC entry 8365 (class 0 OID 0)
-- Dependencies: 849
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq TO demo_geovis_read;


--
-- TOC entry 8366 (class 0 OID 0)
-- Dependencies: 850
-- Name: TABLE karta_skola; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_skola TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_skola TO demo_geovis_read;


--
-- TOC entry 8367 (class 0 OID 0)
-- Dependencies: 851
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq37; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq37 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq37 TO demo_geovis_read;


--
-- TOC entry 8368 (class 0 OID 0)
-- Dependencies: 852
-- Name: TABLE karta_socialkontoret; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_socialkontoret TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_socialkontoret TO demo_geovis_read;


--
-- TOC entry 8369 (class 0 OID 0)
-- Dependencies: 853
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq11; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq11 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq11 TO demo_geovis_read;


--
-- TOC entry 8370 (class 0 OID 0)
-- Dependencies: 854
-- Name: TABLE karta_sophamtning; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_sophamtning TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_sophamtning TO demo_geovis_read;


--
-- TOC entry 8371 (class 0 OID 0)
-- Dependencies: 855
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq72; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq72 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq72 TO demo_geovis_read;


--
-- TOC entry 8372 (class 0 OID 0)
-- Dependencies: 856
-- Name: TABLE karta_spontanidrottsplats; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_spontanidrottsplats TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_spontanidrottsplats TO demo_geovis_read;


--
-- TOC entry 8373 (class 0 OID 0)
-- Dependencies: 857
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq65; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq65 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq65 TO demo_geovis_read;


--
-- TOC entry 8374 (class 0 OID 0)
-- Dependencies: 858
-- Name: TABLE karta_systembolaget; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_systembolaget TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_systembolaget TO demo_geovis_read;


--
-- TOC entry 8375 (class 0 OID 0)
-- Dependencies: 859
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq4; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq4 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_yta_id_seq4 TO demo_geovis_read;


--
-- TOC entry 8376 (class 0 OID 0)
-- Dependencies: 860
-- Name: TABLE karta_torghandel; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_torghandel TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_torghandel TO demo_geovis_read;


--
-- TOC entry 8377 (class 0 OID 0)
-- Dependencies: 861
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq12; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq12 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq12 TO demo_geovis_read;


--
-- TOC entry 8378 (class 0 OID 0)
-- Dependencies: 862
-- Name: TABLE "karta_traffpunkt-aktivitetshuset"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_traffpunkt-aktivitetshuset" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_traffpunkt-aktivitetshuset" TO demo_geovis_read;


--
-- TOC entry 8379 (class 0 OID 0)
-- Dependencies: 863
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq11; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq11 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq11 TO demo_geovis_read;


--
-- TOC entry 8380 (class 0 OID 0)
-- Dependencies: 864
-- Name: TABLE "karta_traffpunkt-aldre"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_traffpunkt-aldre" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_traffpunkt-aldre" TO demo_geovis_read;


--
-- TOC entry 8381 (class 0 OID 0)
-- Dependencies: 865
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq42; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq42 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq42 TO demo_geovis_read;


--
-- TOC entry 8382 (class 0 OID 0)
-- Dependencies: 866
-- Name: TABLE "karta_traffpunkt-funktionsnedsatta"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_traffpunkt-funktionsnedsatta" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_traffpunkt-funktionsnedsatta" TO demo_geovis_read;


--
-- TOC entry 8383 (class 0 OID 0)
-- Dependencies: 867
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq44; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq44 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq44 TO demo_geovis_read;


--
-- TOC entry 8384 (class 0 OID 0)
-- Dependencies: 868
-- Name: TABLE karta_turistinformation; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_turistinformation TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_turistinformation TO demo_geovis_read;


--
-- TOC entry 8385 (class 0 OID 0)
-- Dependencies: 869
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq32; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq32 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq32 TO demo_geovis_read;


--
-- TOC entry 8386 (class 0 OID 0)
-- Dependencies: 870
-- Name: TABLE "karta_uppstallningsplats-husbilar"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_uppstallningsplats-husbilar" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_uppstallningsplats-husbilar" TO demo_geovis_read;


--
-- TOC entry 8387 (class 0 OID 0)
-- Dependencies: 871
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq66; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq66 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq66 TO demo_geovis_read;


--
-- TOC entry 8388 (class 0 OID 0)
-- Dependencies: 872
-- Name: TABLE karta_utomhusgym; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_utomhusgym TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_utomhusgym TO demo_geovis_read;


--
-- TOC entry 8389 (class 0 OID 0)
-- Dependencies: 873
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq1; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq1 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_linje_id_seq1 TO demo_geovis_read;


--
-- TOC entry 8390 (class 0 OID 0)
-- Dependencies: 874
-- Name: TABLE karta_vandringsleder; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_vandringsleder TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_vandringsleder TO demo_geovis_read;


--
-- TOC entry 8391 (class 0 OID 0)
-- Dependencies: 875
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq6; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq6 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq6 TO demo_geovis_read;


--
-- TOC entry 8392 (class 0 OID 0)
-- Dependencies: 876
-- Name: TABLE karta_vardcentral; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis.karta_vardcentral TO geovis;
GRANT SELECT ON TABLE demo_geovis.karta_vardcentral TO demo_geovis_read;


--
-- TOC entry 8393 (class 0 OID 0)
-- Dependencies: 877
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq8; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq8 TO geovis;
GRANT SELECT ON SEQUENCE demo_geovis.standard_kommunkarta_punkt_id_seq8 TO demo_geovis_read;


--
-- TOC entry 8394 (class 0 OID 0)
-- Dependencies: 878
-- Name: TABLE "karta_viktigt-meddelande"; Type: ACL; Schema: demo_geovis; Owner: xplore-admin
--

GRANT ALL ON TABLE demo_geovis."karta_viktigt-meddelande" TO geovis;
GRANT SELECT ON TABLE demo_geovis."karta_viktigt-meddelande" TO demo_geovis_read;


--
-- TOC entry 5817 (class 826 OID 79606)
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: demo_geovis; Owner: xplore-admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA demo_geovis REVOKE ALL ON SEQUENCES  FROM "xplore-admin";
ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA demo_geovis GRANT SELECT,USAGE ON SEQUENCES  TO geovis;
ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA demo_geovis GRANT SELECT ON SEQUENCES  TO demo_geovis_read;


--
-- TOC entry 5818 (class 826 OID 79607)
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: demo_geovis; Owner: xplore-admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA demo_geovis REVOKE ALL ON TABLES  FROM "xplore-admin";
ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA demo_geovis GRANT ALL ON TABLES  TO geovis;
ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA demo_geovis GRANT SELECT ON TABLES  TO demo_geovis_read;


-- Completed on 2019-10-16 11:11:05 CEST

--
-- PostgreSQL database dump complete
--

