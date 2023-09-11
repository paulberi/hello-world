--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5
-- Dumped by pg_dump version 11.5

-- Started on 2019-10-16 11:31:15 CEST

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
-- TOC entry 12 (class 2615 OID 49024)
-- Name: odeshogext; Type: SCHEMA; Schema: -; Owner: geovis
--

CREATE SCHEMA odeshogext;


ALTER SCHEMA odeshogext OWNER TO geovis;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 279 (class 1259 OID 51009)
-- Name: karta_batplatser; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_batplatser (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    beskr1 character varying(254),
    beskr2 character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_batplatser OWNER TO geovis;

--
-- TOC entry 280 (class 1259 OID 51015)
-- Name: Batplatser_odeshog_id_seq; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext."Batplatser_odeshog_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext."Batplatser_odeshog_id_seq" OWNER TO geovis;

--
-- TOC entry 8185 (class 0 OID 0)
-- Dependencies: 280
-- Name: Batplatser_odeshog_id_seq; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext."Batplatser_odeshog_id_seq" OWNED BY odeshogext.karta_batplatser.id;


--
-- TOC entry 281 (class 1259 OID 51017)
-- Name: karta_apotek; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_apotek (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_apotek OWNER TO geovis;

--
-- TOC entry 282 (class 1259 OID 51023)
-- Name: karta_atervinningscentral; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_atervinningscentral (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_atervinningscentral OWNER TO geovis;

--
-- TOC entry 283 (class 1259 OID 51029)
-- Name: karta_atervinningsstation; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_atervinningsstation (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_atervinningsstation OWNER TO geovis;

--
-- TOC entry 284 (class 1259 OID 51035)
-- Name: karta_badplats; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_badplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_badplats OWNER TO geovis;

--
-- TOC entry 285 (class 1259 OID 51041)
-- Name: karta_bankomat; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_bankomat (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_bankomat OWNER TO geovis;

--
-- TOC entry 286 (class 1259 OID 51047)
-- Name: karta_basketplan; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_basketplan (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_basketplan OWNER TO geovis;

--
-- TOC entry 287 (class 1259 OID 51053)
-- Name: karta_beachvolley; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_beachvolley (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_beachvolley OWNER TO geovis;

--
-- TOC entry 288 (class 1259 OID 51059)
-- Name: karta_begravningsplats; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_begravningsplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_begravningsplats OWNER TO geovis;

--
-- TOC entry 289 (class 1259 OID 51065)
-- Name: karta_belaggningsarbete; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_belaggningsarbete (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_belaggningsarbete OWNER TO geovis;

--
-- TOC entry 290 (class 1259 OID 51071)
-- Name: karta_bibliotek; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_bibliotek (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_bibliotek OWNER TO geovis;

--
-- TOC entry 291 (class 1259 OID 51077)
-- Name: karta_biograf; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_biograf (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_biograf OWNER TO geovis;

--
-- TOC entry 292 (class 1259 OID 51083)
-- Name: karta_boende; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_boende (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_boende OWNER TO geovis;

--
-- TOC entry 293 (class 1259 OID 51089)
-- Name: karta_bostadstomter_ejbebyggda; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_bostadstomter_ejbebyggda (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_bostadstomter_ejbebyggda OWNER TO geovis;

--
-- TOC entry 294 (class 1259 OID 51095)
-- Name: karta_boulebanor; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_boulebanor (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_boulebanor OWNER TO geovis;

--
-- TOC entry 295 (class 1259 OID 51101)
-- Name: karta_bowling; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_bowling (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_bowling OWNER TO geovis;

--
-- TOC entry 296 (class 1259 OID 51107)
-- Name: karta_busshallplats; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_busshallplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_busshallplats OWNER TO geovis;

--
-- TOC entry 297 (class 1259 OID 51113)
-- Name: karta_busskort; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_busskort (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_busskort OWNER TO geovis;

--
-- TOC entry 298 (class 1259 OID 51119)
-- Name: karta_busstation; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_busstation (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_busstation OWNER TO geovis;

--
-- TOC entry 299 (class 1259 OID 51125)
-- Name: karta_bygdegardar; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_bygdegardar (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_bygdegardar OWNER TO geovis;

--
-- TOC entry 300 (class 1259 OID 51131)
-- Name: karta_cafe; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_cafe (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_cafe OWNER TO geovis;

--
-- TOC entry 301 (class 1259 OID 51137)
-- Name: karta_cykelleder; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_cykelleder (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_cykelleder OWNER TO geovis;

--
-- TOC entry 302 (class 1259 OID 51143)
-- Name: karta_cykelvagar; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_cykelvagar (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_cykelvagar OWNER TO geovis;

--
-- TOC entry 303 (class 1259 OID 51149)
-- Name: karta_daglig-verksamhet; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_daglig-verksamhet" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_daglig-verksamhet" OWNER TO geovis;

--
-- TOC entry 304 (class 1259 OID 51155)
-- Name: karta_detaljplaner-gallande; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_detaljplaner-gallande" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_detaljplaner-gallande" OWNER TO geovis;

--
-- TOC entry 305 (class 1259 OID 51161)
-- Name: karta_detaljplaner-pagaende; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_detaljplaner-pagaende" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_detaljplaner-pagaende" OWNER TO geovis;

--
-- TOC entry 306 (class 1259 OID 51167)
-- Name: karta_drivmedelsstation; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_drivmedelsstation (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_drivmedelsstation OWNER TO geovis;

--
-- TOC entry 307 (class 1259 OID 51173)
-- Name: karta_fiske; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_fiske (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_fiske OWNER TO geovis;

--
-- TOC entry 308 (class 1259 OID 51179)
-- Name: karta_fiskekort; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_fiskekort (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_fiskekort OWNER TO geovis;

--
-- TOC entry 309 (class 1259 OID 51185)
-- Name: karta_folkets-park; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_folkets-park" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_folkets-park" OWNER TO geovis;

--
-- TOC entry 310 (class 1259 OID 51191)
-- Name: karta_folktandvard; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_folktandvard (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_folktandvard OWNER TO geovis;

--
-- TOC entry 311 (class 1259 OID 51197)
-- Name: karta_forskola; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_forskola (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_forskola OWNER TO geovis;

--
-- TOC entry 312 (class 1259 OID 51203)
-- Name: karta_fotbollsplaner; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_fotbollsplaner (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_fotbollsplaner OWNER TO geovis;

--
-- TOC entry 313 (class 1259 OID 51209)
-- Name: karta_friidrottsanl; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_friidrottsanl (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_friidrottsanl OWNER TO geovis;

--
-- TOC entry 314 (class 1259 OID 51215)
-- Name: karta_fritidsgard; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_fritidsgard (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_fritidsgard OWNER TO geovis;

--
-- TOC entry 315 (class 1259 OID 51221)
-- Name: karta_fritidshem; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_fritidshem (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_fritidshem OWNER TO geovis;

--
-- TOC entry 316 (class 1259 OID 51227)
-- Name: karta_golfbanor; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_golfbanor (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_golfbanor OWNER TO geovis;

--
-- TOC entry 317 (class 1259 OID 51233)
-- Name: karta_grav-uppstallningstillstand; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_grav-uppstallningstillstand" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_grav-uppstallningstillstand" OWNER TO geovis;

--
-- TOC entry 318 (class 1259 OID 51239)
-- Name: karta_gym; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_gym (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_gym OWNER TO geovis;

--
-- TOC entry 319 (class 1259 OID 51245)
-- Name: karta_hamn; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_hamn (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_hamn OWNER TO geovis;

--
-- TOC entry 320 (class 1259 OID 51251)
-- Name: karta_hembygdsforening; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_hembygdsforening (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_hembygdsforening OWNER TO geovis;

--
-- TOC entry 321 (class 1259 OID 51257)
-- Name: karta_hjartstartare; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_hjartstartare (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_hjartstartare OWNER TO geovis;

--
-- TOC entry 322 (class 1259 OID 51263)
-- Name: karta_hundklubb; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_hundklubb (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_hundklubb OWNER TO geovis;

--
-- TOC entry 323 (class 1259 OID 51269)
-- Name: karta_hundpasar; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_hundpasar (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_hundpasar OWNER TO geovis;

--
-- TOC entry 324 (class 1259 OID 51275)
-- Name: karta_idrottshall; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_idrottshall (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_idrottshall OWNER TO geovis;

--
-- TOC entry 325 (class 1259 OID 51281)
-- Name: karta_isbanor; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_isbanor (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_isbanor OWNER TO geovis;

--
-- TOC entry 326 (class 1259 OID 51287)
-- Name: karta_kommunagd-mark; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_kommunagd-mark" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_kommunagd-mark" OWNER TO geovis;

--
-- TOC entry 327 (class 1259 OID 51293)
-- Name: karta_kommunkontor; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_kommunkontor (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_kommunkontor OWNER TO geovis;

--
-- TOC entry 328 (class 1259 OID 51299)
-- Name: karta_konsumentradgivning; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_konsumentradgivning (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_konsumentradgivning OWNER TO geovis;

--
-- TOC entry 329 (class 1259 OID 51305)
-- Name: karta_kulturskolan; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_kulturskolan (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_kulturskolan OWNER TO geovis;

--
-- TOC entry 330 (class 1259 OID 51311)
-- Name: karta_laddstationer; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_laddstationer (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_laddstationer OWNER TO geovis;

--
-- TOC entry 331 (class 1259 OID 51317)
-- Name: karta_langdskidspar; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_langdskidspar (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_langdskidspar OWNER TO geovis;

--
-- TOC entry 332 (class 1259 OID 51323)
-- Name: karta_ledig-industrimark; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_ledig-industrimark" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_ledig-industrimark" OWNER TO geovis;

--
-- TOC entry 333 (class 1259 OID 51329)
-- Name: karta_ledig-industrimark_privatagd; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_ledig-industrimark_privatagd" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_ledig-industrimark_privatagd" OWNER TO geovis;

--
-- TOC entry 334 (class 1259 OID 51335)
-- Name: karta_lediga-bostadstomter; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_lediga-bostadstomter" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_lediga-bostadstomter" OWNER TO geovis;

--
-- TOC entry 335 (class 1259 OID 51341)
-- Name: karta_lekplats; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_lekplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_lekplats OWNER TO geovis;

--
-- TOC entry 336 (class 1259 OID 51347)
-- Name: karta_livsmedelsbutik; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_livsmedelsbutik (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_livsmedelsbutik OWNER TO geovis;

--
-- TOC entry 337 (class 1259 OID 51353)
-- Name: karta_motionsanl; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_motionsanl (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_motionsanl OWNER TO geovis;

--
-- TOC entry 338 (class 1259 OID 51359)
-- Name: karta_motionsspar_elljusspar; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_motionsspar_elljusspar (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_motionsspar_elljusspar OWNER TO geovis;

--
-- TOC entry 339 (class 1259 OID 51365)
-- Name: karta_motionsspar_uppmarkta; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_motionsspar_uppmarkta (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_motionsspar_uppmarkta OWNER TO geovis;

--
-- TOC entry 340 (class 1259 OID 51371)
-- Name: karta_motorcrossbana; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_motorcrossbana (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_motorcrossbana OWNER TO geovis;

--
-- TOC entry 341 (class 1259 OID 51377)
-- Name: karta_naturreservat; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_naturreservat (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_naturreservat OWNER TO geovis;

--
-- TOC entry 342 (class 1259 OID 51383)
-- Name: karta_naturvardsprogram-objekt; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_naturvardsprogram-objekt" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_naturvardsprogram-objekt" OWNER TO geovis;

--
-- TOC entry 343 (class 1259 OID 51389)
-- Name: karta_naturvardsprogram-omraden; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_naturvardsprogram-omraden" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_naturvardsprogram-omraden" OWNER TO geovis;

--
-- TOC entry 344 (class 1259 OID 51395)
-- Name: karta_obo; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_obo (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_obo OWNER TO geovis;

--
-- TOC entry 345 (class 1259 OID 51401)
-- Name: karta_offentligatoaletter; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_offentligatoaletter (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_offentligatoaletter OWNER TO geovis;

--
-- TOC entry 346 (class 1259 OID 51407)
-- Name: karta_okad-sysselsattning; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_okad-sysselsattning" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_okad-sysselsattning" OWNER TO geovis;

--
-- TOC entry 347 (class 1259 OID 51413)
-- Name: karta_parkering-pendlar; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_parkering-pendlar" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_parkering-pendlar" OWNER TO geovis;

--
-- TOC entry 348 (class 1259 OID 51419)
-- Name: karta_parkering_rorelsehindrade; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_parkering_rorelsehindrade (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_parkering_rorelsehindrade OWNER TO geovis;

--
-- TOC entry 349 (class 1259 OID 51425)
-- Name: karta_parkeringsplatser; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_parkeringsplatser (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_parkeringsplatser OWNER TO geovis;

--
-- TOC entry 350 (class 1259 OID 51431)
-- Name: karta_polis; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_polis (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_polis OWNER TO geovis;

--
-- TOC entry 351 (class 1259 OID 51437)
-- Name: karta_postombud; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_postombud (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_postombud OWNER TO geovis;

--
-- TOC entry 352 (class 1259 OID 51443)
-- Name: karta_racketanl; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_racketanl (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_racketanl OWNER TO geovis;

--
-- TOC entry 353 (class 1259 OID 51449)
-- Name: karta_raddningstjanst; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_raddningstjanst (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_raddningstjanst OWNER TO geovis;

--
-- TOC entry 354 (class 1259 OID 51455)
-- Name: karta_restauranger; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_restauranger (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_restauranger OWNER TO geovis;

--
-- TOC entry 355 (class 1259 OID 51461)
-- Name: karta_ridanl; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_ridanl (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_ridanl OWNER TO geovis;

--
-- TOC entry 356 (class 1259 OID 51467)
-- Name: karta_sevardheter_besoksmal; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_sevardheter_besoksmal (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_sevardheter_besoksmal OWNER TO geovis;

--
-- TOC entry 357 (class 1259 OID 51473)
-- Name: karta_sevardheter_kultur; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_sevardheter_kultur (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_sevardheter_kultur OWNER TO geovis;

--
-- TOC entry 358 (class 1259 OID 51479)
-- Name: karta_sevardheter_natur; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_sevardheter_natur (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_sevardheter_natur OWNER TO geovis;

--
-- TOC entry 359 (class 1259 OID 51485)
-- Name: karta_skatepark; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_skatepark (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_skatepark OWNER TO geovis;

--
-- TOC entry 360 (class 1259 OID 51491)
-- Name: karta_skjutbana; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_skjutbana (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_skjutbana OWNER TO geovis;

--
-- TOC entry 361 (class 1259 OID 51497)
-- Name: karta_skola; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_skola (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_skola OWNER TO geovis;

--
-- TOC entry 362 (class 1259 OID 51503)
-- Name: karta_socialkontoret; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_socialkontoret (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_socialkontoret OWNER TO geovis;

--
-- TOC entry 363 (class 1259 OID 51509)
-- Name: karta_sophamtning; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_sophamtning (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_sophamtning OWNER TO geovis;

--
-- TOC entry 364 (class 1259 OID 51515)
-- Name: karta_spontanidrottsplats; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_spontanidrottsplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_spontanidrottsplats OWNER TO geovis;

--
-- TOC entry 365 (class 1259 OID 51521)
-- Name: karta_systembolaget; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_systembolaget (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_systembolaget OWNER TO geovis;

--
-- TOC entry 366 (class 1259 OID 51527)
-- Name: karta_torghandel; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_torghandel (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_torghandel OWNER TO geovis;

--
-- TOC entry 367 (class 1259 OID 51533)
-- Name: karta_traffpunkt-aktivitetshuset; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_traffpunkt-aktivitetshuset" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_traffpunkt-aktivitetshuset" OWNER TO geovis;

--
-- TOC entry 368 (class 1259 OID 51539)
-- Name: karta_traffpunkt-aldre; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_traffpunkt-aldre" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_traffpunkt-aldre" OWNER TO geovis;

--
-- TOC entry 369 (class 1259 OID 51545)
-- Name: karta_traffpunkt-funktionsnedsatta; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_traffpunkt-funktionsnedsatta" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_traffpunkt-funktionsnedsatta" OWNER TO geovis;

--
-- TOC entry 370 (class 1259 OID 51551)
-- Name: karta_turistinformation; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_turistinformation (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_turistinformation OWNER TO geovis;

--
-- TOC entry 371 (class 1259 OID 51557)
-- Name: karta_uppstallningsplats-husbilar; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_uppstallningsplats-husbilar" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_uppstallningsplats-husbilar" OWNER TO geovis;

--
-- TOC entry 372 (class 1259 OID 51563)
-- Name: karta_utomhusgym; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_utomhusgym (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_utomhusgym OWNER TO geovis;

--
-- TOC entry 373 (class 1259 OID 51569)
-- Name: karta_vandringsleder; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_vandringsleder (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_vandringsleder OWNER TO geovis;

--
-- TOC entry 374 (class 1259 OID 51575)
-- Name: karta_vardcentral; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext.karta_vardcentral (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext.karta_vardcentral OWNER TO geovis;

--
-- TOC entry 375 (class 1259 OID 51581)
-- Name: karta_viktigt-meddelande; Type: TABLE; Schema: odeshogext; Owner: geovis
--

CREATE TABLE odeshogext."karta_viktigt-meddelande" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE odeshogext."karta_viktigt-meddelande" OWNER TO geovis;

--
-- TOC entry 376 (class 1259 OID 51587)
-- Name: standard_kommunkarta_linje_id_seq; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_linje_id_seq OWNER TO geovis;

--
-- TOC entry 8186 (class 0 OID 0)
-- Dependencies: 376
-- Name: standard_kommunkarta_linje_id_seq; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq OWNED BY odeshogext.karta_belaggningsarbete.id;


--
-- TOC entry 377 (class 1259 OID 51589)
-- Name: standard_kommunkarta_linje_id_seq1; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_linje_id_seq1 OWNER TO geovis;

--
-- TOC entry 8187 (class 0 OID 0)
-- Dependencies: 377
-- Name: standard_kommunkarta_linje_id_seq1; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq1 OWNED BY odeshogext.karta_vandringsleder.id;


--
-- TOC entry 378 (class 1259 OID 51591)
-- Name: standard_kommunkarta_linje_id_seq2; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_linje_id_seq2 OWNER TO geovis;

--
-- TOC entry 8188 (class 0 OID 0)
-- Dependencies: 378
-- Name: standard_kommunkarta_linje_id_seq2; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq2 OWNED BY odeshogext.karta_cykelleder.id;


--
-- TOC entry 379 (class 1259 OID 51593)
-- Name: standard_kommunkarta_linje_id_seq3; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_linje_id_seq3 OWNER TO geovis;

--
-- TOC entry 8189 (class 0 OID 0)
-- Dependencies: 379
-- Name: standard_kommunkarta_linje_id_seq3; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq3 OWNED BY odeshogext.karta_cykelvagar.id;


--
-- TOC entry 380 (class 1259 OID 51595)
-- Name: standard_kommunkarta_linje_id_seq4; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_linje_id_seq4 OWNER TO geovis;

--
-- TOC entry 8190 (class 0 OID 0)
-- Dependencies: 380
-- Name: standard_kommunkarta_linje_id_seq4; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq4 OWNED BY odeshogext.karta_motionsspar_elljusspar.id;


--
-- TOC entry 381 (class 1259 OID 51597)
-- Name: standard_kommunkarta_linje_id_seq5; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_linje_id_seq5 OWNER TO geovis;

--
-- TOC entry 8191 (class 0 OID 0)
-- Dependencies: 381
-- Name: standard_kommunkarta_linje_id_seq5; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_linje_id_seq5 OWNED BY odeshogext.karta_motionsspar_uppmarkta.id;


--
-- TOC entry 382 (class 1259 OID 51599)
-- Name: standard_kommunkarta_punkt_id_seq; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq OWNER TO geovis;

--
-- TOC entry 8192 (class 0 OID 0)
-- Dependencies: 382
-- Name: standard_kommunkarta_punkt_id_seq; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq OWNED BY odeshogext.karta_skola.id;


--
-- TOC entry 383 (class 1259 OID 51601)
-- Name: standard_kommunkarta_punkt_id_seq1; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq1 OWNER TO geovis;

--
-- TOC entry 8193 (class 0 OID 0)
-- Dependencies: 383
-- Name: standard_kommunkarta_punkt_id_seq1; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq1 OWNED BY odeshogext.karta_forskola.id;


--
-- TOC entry 384 (class 1259 OID 51603)
-- Name: standard_kommunkarta_punkt_id_seq10; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq10 OWNER TO geovis;

--
-- TOC entry 8194 (class 0 OID 0)
-- Dependencies: 384
-- Name: standard_kommunkarta_punkt_id_seq10; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq10 OWNED BY odeshogext."karta_okad-sysselsattning".id;


--
-- TOC entry 385 (class 1259 OID 51605)
-- Name: standard_kommunkarta_punkt_id_seq11; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq11 OWNER TO geovis;

--
-- TOC entry 8195 (class 0 OID 0)
-- Dependencies: 385
-- Name: standard_kommunkarta_punkt_id_seq11; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq11 OWNED BY odeshogext."karta_traffpunkt-aldre".id;


--
-- TOC entry 386 (class 1259 OID 51607)
-- Name: standard_kommunkarta_punkt_id_seq12; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq12 OWNER TO geovis;

--
-- TOC entry 8196 (class 0 OID 0)
-- Dependencies: 386
-- Name: standard_kommunkarta_punkt_id_seq12; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq12 OWNED BY odeshogext."karta_traffpunkt-aktivitetshuset".id;


--
-- TOC entry 387 (class 1259 OID 51609)
-- Name: standard_kommunkarta_punkt_id_seq13; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq13 OWNER TO geovis;

--
-- TOC entry 8197 (class 0 OID 0)
-- Dependencies: 387
-- Name: standard_kommunkarta_punkt_id_seq13; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq13 OWNED BY odeshogext.karta_bibliotek.id;


--
-- TOC entry 388 (class 1259 OID 51611)
-- Name: standard_kommunkarta_punkt_id_seq14; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq14 OWNER TO geovis;

--
-- TOC entry 8198 (class 0 OID 0)
-- Dependencies: 388
-- Name: standard_kommunkarta_punkt_id_seq14; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq14 OWNED BY odeshogext.karta_biograf.id;


--
-- TOC entry 389 (class 1259 OID 51613)
-- Name: standard_kommunkarta_punkt_id_seq15; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq15 OWNER TO geovis;

--
-- TOC entry 8199 (class 0 OID 0)
-- Dependencies: 389
-- Name: standard_kommunkarta_punkt_id_seq15; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq15 OWNED BY odeshogext."karta_folkets-park".id;


--
-- TOC entry 390 (class 1259 OID 51615)
-- Name: standard_kommunkarta_punkt_id_seq16; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq16 OWNER TO geovis;

--
-- TOC entry 8200 (class 0 OID 0)
-- Dependencies: 390
-- Name: standard_kommunkarta_punkt_id_seq16; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq16 OWNED BY odeshogext.karta_golfbanor.id;


--
-- TOC entry 391 (class 1259 OID 51617)
-- Name: standard_kommunkarta_punkt_id_seq17; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq17 OWNER TO geovis;

--
-- TOC entry 8201 (class 0 OID 0)
-- Dependencies: 391
-- Name: standard_kommunkarta_punkt_id_seq17; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq17 OWNED BY odeshogext.karta_fotbollsplaner.id;


--
-- TOC entry 392 (class 1259 OID 51619)
-- Name: standard_kommunkarta_punkt_id_seq18; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq18 OWNER TO geovis;

--
-- TOC entry 8202 (class 0 OID 0)
-- Dependencies: 392
-- Name: standard_kommunkarta_punkt_id_seq18; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq18 OWNED BY odeshogext.karta_friidrottsanl.id;


--
-- TOC entry 393 (class 1259 OID 51621)
-- Name: standard_kommunkarta_punkt_id_seq19; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq19 OWNER TO geovis;

--
-- TOC entry 8203 (class 0 OID 0)
-- Dependencies: 393
-- Name: standard_kommunkarta_punkt_id_seq19; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq19 OWNED BY odeshogext.karta_idrottshall.id;


--
-- TOC entry 394 (class 1259 OID 51623)
-- Name: standard_kommunkarta_punkt_id_seq2; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq2 OWNER TO geovis;

--
-- TOC entry 8204 (class 0 OID 0)
-- Dependencies: 394
-- Name: standard_kommunkarta_punkt_id_seq2; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq2 OWNED BY odeshogext.karta_isbanor.id;


--
-- TOC entry 395 (class 1259 OID 51625)
-- Name: standard_kommunkarta_punkt_id_seq20; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq20 OWNER TO geovis;

--
-- TOC entry 8205 (class 0 OID 0)
-- Dependencies: 395
-- Name: standard_kommunkarta_punkt_id_seq20; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq20 OWNED BY odeshogext.karta_badplats.id;


--
-- TOC entry 396 (class 1259 OID 51627)
-- Name: standard_kommunkarta_punkt_id_seq21; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq21 OWNER TO geovis;

--
-- TOC entry 8206 (class 0 OID 0)
-- Dependencies: 396
-- Name: standard_kommunkarta_punkt_id_seq21; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq21 OWNED BY odeshogext.karta_fritidsgard.id;


--
-- TOC entry 397 (class 1259 OID 51629)
-- Name: standard_kommunkarta_punkt_id_seq22; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq22 OWNER TO geovis;

--
-- TOC entry 8207 (class 0 OID 0)
-- Dependencies: 397
-- Name: standard_kommunkarta_punkt_id_seq22; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq22 OWNED BY odeshogext.karta_motionsanl.id;


--
-- TOC entry 398 (class 1259 OID 51631)
-- Name: standard_kommunkarta_punkt_id_seq23; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq23 OWNER TO geovis;

--
-- TOC entry 8208 (class 0 OID 0)
-- Dependencies: 398
-- Name: standard_kommunkarta_punkt_id_seq23; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq23 OWNED BY odeshogext.karta_racketanl.id;


--
-- TOC entry 399 (class 1259 OID 51633)
-- Name: standard_kommunkarta_punkt_id_seq24; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq24 OWNER TO geovis;

--
-- TOC entry 8209 (class 0 OID 0)
-- Dependencies: 399
-- Name: standard_kommunkarta_punkt_id_seq24; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq24 OWNED BY odeshogext.karta_skatepark.id;


--
-- TOC entry 400 (class 1259 OID 51635)
-- Name: standard_kommunkarta_punkt_id_seq25; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq25 OWNER TO geovis;

--
-- TOC entry 8210 (class 0 OID 0)
-- Dependencies: 400
-- Name: standard_kommunkarta_punkt_id_seq25; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq25 OWNED BY odeshogext.karta_motorcrossbana.id;


--
-- TOC entry 401 (class 1259 OID 51637)
-- Name: standard_kommunkarta_punkt_id_seq26; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq26 OWNER TO geovis;

--
-- TOC entry 8211 (class 0 OID 0)
-- Dependencies: 401
-- Name: standard_kommunkarta_punkt_id_seq26; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq26 OWNED BY odeshogext.karta_ridanl.id;


--
-- TOC entry 402 (class 1259 OID 51639)
-- Name: standard_kommunkarta_punkt_id_seq27; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq27 OWNER TO geovis;

--
-- TOC entry 8212 (class 0 OID 0)
-- Dependencies: 402
-- Name: standard_kommunkarta_punkt_id_seq27; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq27 OWNED BY odeshogext.karta_atervinningscentral.id;


--
-- TOC entry 403 (class 1259 OID 51641)
-- Name: standard_kommunkarta_punkt_id_seq28; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq28 OWNER TO geovis;

--
-- TOC entry 8213 (class 0 OID 0)
-- Dependencies: 403
-- Name: standard_kommunkarta_punkt_id_seq28; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq28 OWNED BY odeshogext.karta_atervinningsstation.id;


--
-- TOC entry 404 (class 1259 OID 51643)
-- Name: standard_kommunkarta_punkt_id_seq29; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq29 OWNER TO geovis;

--
-- TOC entry 8214 (class 0 OID 0)
-- Dependencies: 404
-- Name: standard_kommunkarta_punkt_id_seq29; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq29 OWNED BY odeshogext."karta_parkering-pendlar".id;


--
-- TOC entry 405 (class 1259 OID 51645)
-- Name: standard_kommunkarta_punkt_id_seq3; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq3 OWNER TO geovis;

--
-- TOC entry 8215 (class 0 OID 0)
-- Dependencies: 405
-- Name: standard_kommunkarta_punkt_id_seq3; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq3 OWNED BY odeshogext.karta_polis.id;


--
-- TOC entry 406 (class 1259 OID 51647)
-- Name: standard_kommunkarta_punkt_id_seq30; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq30 OWNER TO geovis;

--
-- TOC entry 8216 (class 0 OID 0)
-- Dependencies: 406
-- Name: standard_kommunkarta_punkt_id_seq30; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq30 OWNED BY odeshogext.karta_parkeringsplatser.id;


--
-- TOC entry 407 (class 1259 OID 51649)
-- Name: standard_kommunkarta_punkt_id_seq31; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq31 OWNER TO geovis;

--
-- TOC entry 8217 (class 0 OID 0)
-- Dependencies: 407
-- Name: standard_kommunkarta_punkt_id_seq31; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq31 OWNED BY odeshogext.karta_busstation.id;


--
-- TOC entry 408 (class 1259 OID 51651)
-- Name: standard_kommunkarta_punkt_id_seq32; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq32 OWNER TO geovis;

--
-- TOC entry 8218 (class 0 OID 0)
-- Dependencies: 408
-- Name: standard_kommunkarta_punkt_id_seq32; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq32 OWNED BY odeshogext."karta_uppstallningsplats-husbilar".id;


--
-- TOC entry 409 (class 1259 OID 51653)
-- Name: standard_kommunkarta_punkt_id_seq33; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq33 OWNER TO geovis;

--
-- TOC entry 8219 (class 0 OID 0)
-- Dependencies: 409
-- Name: standard_kommunkarta_punkt_id_seq33; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq33 OWNED BY odeshogext.karta_apotek.id;


--
-- TOC entry 410 (class 1259 OID 51655)
-- Name: standard_kommunkarta_punkt_id_seq34; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq34 OWNER TO geovis;

--
-- TOC entry 8220 (class 0 OID 0)
-- Dependencies: 410
-- Name: standard_kommunkarta_punkt_id_seq34; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq34 OWNED BY odeshogext.karta_laddstationer.id;


--
-- TOC entry 411 (class 1259 OID 51657)
-- Name: standard_kommunkarta_punkt_id_seq35; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq35 OWNER TO geovis;

--
-- TOC entry 8221 (class 0 OID 0)
-- Dependencies: 411
-- Name: standard_kommunkarta_punkt_id_seq35; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq35 OWNED BY odeshogext.karta_lekplats.id;


--
-- TOC entry 412 (class 1259 OID 51659)
-- Name: standard_kommunkarta_punkt_id_seq36; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq36 OWNER TO geovis;

--
-- TOC entry 8222 (class 0 OID 0)
-- Dependencies: 412
-- Name: standard_kommunkarta_punkt_id_seq36; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq36 OWNED BY odeshogext.karta_folktandvard.id;


--
-- TOC entry 413 (class 1259 OID 51661)
-- Name: standard_kommunkarta_punkt_id_seq37; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq37 OWNER TO geovis;

--
-- TOC entry 8223 (class 0 OID 0)
-- Dependencies: 413
-- Name: standard_kommunkarta_punkt_id_seq37; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq37 OWNED BY odeshogext.karta_socialkontoret.id;


--
-- TOC entry 414 (class 1259 OID 51663)
-- Name: standard_kommunkarta_punkt_id_seq38; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq38 OWNER TO geovis;

--
-- TOC entry 8224 (class 0 OID 0)
-- Dependencies: 414
-- Name: standard_kommunkarta_punkt_id_seq38; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq38 OWNED BY odeshogext.karta_obo.id;


--
-- TOC entry 415 (class 1259 OID 51665)
-- Name: standard_kommunkarta_punkt_id_seq39; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq39 OWNER TO geovis;

--
-- TOC entry 8225 (class 0 OID 0)
-- Dependencies: 415
-- Name: standard_kommunkarta_punkt_id_seq39; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq39 OWNED BY odeshogext.karta_hamn.id;


--
-- TOC entry 416 (class 1259 OID 51667)
-- Name: standard_kommunkarta_punkt_id_seq4; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq4 OWNER TO geovis;

--
-- TOC entry 8226 (class 0 OID 0)
-- Dependencies: 416
-- Name: standard_kommunkarta_punkt_id_seq4; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq4 OWNED BY odeshogext.karta_konsumentradgivning.id;


--
-- TOC entry 417 (class 1259 OID 51669)
-- Name: standard_kommunkarta_punkt_id_seq40; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq40 OWNER TO geovis;

--
-- TOC entry 8227 (class 0 OID 0)
-- Dependencies: 417
-- Name: standard_kommunkarta_punkt_id_seq40; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq40 OWNED BY odeshogext.karta_fritidshem.id;


--
-- TOC entry 418 (class 1259 OID 51671)
-- Name: standard_kommunkarta_punkt_id_seq41; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq41 OWNER TO geovis;

--
-- TOC entry 8228 (class 0 OID 0)
-- Dependencies: 418
-- Name: standard_kommunkarta_punkt_id_seq41; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq41 OWNED BY odeshogext.karta_bankomat.id;


--
-- TOC entry 419 (class 1259 OID 51673)
-- Name: standard_kommunkarta_punkt_id_seq42; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq42 OWNER TO geovis;

--
-- TOC entry 8229 (class 0 OID 0)
-- Dependencies: 419
-- Name: standard_kommunkarta_punkt_id_seq42; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq42 OWNED BY odeshogext."karta_traffpunkt-funktionsnedsatta".id;


--
-- TOC entry 420 (class 1259 OID 51675)
-- Name: standard_kommunkarta_punkt_id_seq43; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq43 OWNER TO geovis;

--
-- TOC entry 8230 (class 0 OID 0)
-- Dependencies: 420
-- Name: standard_kommunkarta_punkt_id_seq43; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq43 OWNED BY odeshogext.karta_hjartstartare.id;


--
-- TOC entry 421 (class 1259 OID 51677)
-- Name: standard_kommunkarta_punkt_id_seq44; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq44 OWNER TO geovis;

--
-- TOC entry 8231 (class 0 OID 0)
-- Dependencies: 421
-- Name: standard_kommunkarta_punkt_id_seq44; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq44 OWNED BY odeshogext.karta_turistinformation.id;


--
-- TOC entry 422 (class 1259 OID 51679)
-- Name: standard_kommunkarta_punkt_id_seq45; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq45 OWNER TO geovis;

--
-- TOC entry 8232 (class 0 OID 0)
-- Dependencies: 422
-- Name: standard_kommunkarta_punkt_id_seq45; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq45 OWNED BY odeshogext.karta_boende.id;


--
-- TOC entry 423 (class 1259 OID 51681)
-- Name: standard_kommunkarta_punkt_id_seq46; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq46 OWNER TO geovis;

--
-- TOC entry 8233 (class 0 OID 0)
-- Dependencies: 423
-- Name: standard_kommunkarta_punkt_id_seq46; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq46 OWNED BY odeshogext.karta_sevardheter_natur.id;


--
-- TOC entry 424 (class 1259 OID 51683)
-- Name: standard_kommunkarta_punkt_id_seq47; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq47 OWNER TO geovis;

--
-- TOC entry 8234 (class 0 OID 0)
-- Dependencies: 424
-- Name: standard_kommunkarta_punkt_id_seq47; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq47 OWNED BY odeshogext.karta_sevardheter_kultur.id;


--
-- TOC entry 425 (class 1259 OID 51685)
-- Name: standard_kommunkarta_punkt_id_seq48; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq48 OWNER TO geovis;

--
-- TOC entry 8235 (class 0 OID 0)
-- Dependencies: 425
-- Name: standard_kommunkarta_punkt_id_seq48; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq48 OWNED BY odeshogext.karta_sevardheter_besoksmal.id;


--
-- TOC entry 426 (class 1259 OID 51687)
-- Name: standard_kommunkarta_punkt_id_seq49; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq49 OWNER TO geovis;

--
-- TOC entry 8236 (class 0 OID 0)
-- Dependencies: 426
-- Name: standard_kommunkarta_punkt_id_seq49; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq49 OWNED BY odeshogext.karta_restauranger.id;


--
-- TOC entry 427 (class 1259 OID 51689)
-- Name: standard_kommunkarta_punkt_id_seq5; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq5 OWNER TO geovis;

--
-- TOC entry 8237 (class 0 OID 0)
-- Dependencies: 427
-- Name: standard_kommunkarta_punkt_id_seq5; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq5 OWNED BY odeshogext.karta_raddningstjanst.id;


--
-- TOC entry 428 (class 1259 OID 51691)
-- Name: standard_kommunkarta_punkt_id_seq50; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq50 OWNER TO geovis;

--
-- TOC entry 8238 (class 0 OID 0)
-- Dependencies: 428
-- Name: standard_kommunkarta_punkt_id_seq50; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq50 OWNED BY odeshogext.karta_cafe.id;


--
-- TOC entry 429 (class 1259 OID 51693)
-- Name: standard_kommunkarta_punkt_id_seq51; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq51 OWNER TO geovis;

--
-- TOC entry 8239 (class 0 OID 0)
-- Dependencies: 429
-- Name: standard_kommunkarta_punkt_id_seq51; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq51 OWNED BY odeshogext.karta_livsmedelsbutik.id;


--
-- TOC entry 430 (class 1259 OID 51695)
-- Name: standard_kommunkarta_punkt_id_seq52; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq52 OWNER TO geovis;

--
-- TOC entry 8240 (class 0 OID 0)
-- Dependencies: 430
-- Name: standard_kommunkarta_punkt_id_seq52; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq52 OWNED BY odeshogext.karta_drivmedelsstation.id;


--
-- TOC entry 431 (class 1259 OID 51697)
-- Name: standard_kommunkarta_punkt_id_seq53; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq53 OWNER TO geovis;

--
-- TOC entry 8241 (class 0 OID 0)
-- Dependencies: 431
-- Name: standard_kommunkarta_punkt_id_seq53; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq53 OWNED BY odeshogext.karta_busshallplats.id;


--
-- TOC entry 432 (class 1259 OID 51699)
-- Name: standard_kommunkarta_punkt_id_seq54; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq54 OWNER TO geovis;

--
-- TOC entry 8242 (class 0 OID 0)
-- Dependencies: 432
-- Name: standard_kommunkarta_punkt_id_seq54; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq54 OWNED BY odeshogext.karta_fiske.id;


--
-- TOC entry 433 (class 1259 OID 51701)
-- Name: standard_kommunkarta_punkt_id_seq55; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq55 OWNER TO geovis;

--
-- TOC entry 8243 (class 0 OID 0)
-- Dependencies: 433
-- Name: standard_kommunkarta_punkt_id_seq55; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq55 OWNED BY odeshogext.karta_fiskekort.id;


--
-- TOC entry 434 (class 1259 OID 51703)
-- Name: standard_kommunkarta_punkt_id_seq56; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq56 OWNER TO geovis;

--
-- TOC entry 8244 (class 0 OID 0)
-- Dependencies: 434
-- Name: standard_kommunkarta_punkt_id_seq56; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq56 OWNED BY odeshogext.karta_basketplan.id;


--
-- TOC entry 435 (class 1259 OID 51705)
-- Name: standard_kommunkarta_punkt_id_seq57; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq57 OWNER TO geovis;

--
-- TOC entry 8245 (class 0 OID 0)
-- Dependencies: 435
-- Name: standard_kommunkarta_punkt_id_seq57; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq57 OWNED BY odeshogext.karta_beachvolley.id;


--
-- TOC entry 436 (class 1259 OID 51707)
-- Name: standard_kommunkarta_punkt_id_seq58; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq58 OWNER TO geovis;

--
-- TOC entry 8246 (class 0 OID 0)
-- Dependencies: 436
-- Name: standard_kommunkarta_punkt_id_seq58; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq58 OWNED BY odeshogext.karta_begravningsplats.id;


--
-- TOC entry 437 (class 1259 OID 51709)
-- Name: standard_kommunkarta_punkt_id_seq59; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq59 OWNER TO geovis;

--
-- TOC entry 8247 (class 0 OID 0)
-- Dependencies: 437
-- Name: standard_kommunkarta_punkt_id_seq59; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq59 OWNED BY odeshogext.karta_boulebanor.id;


--
-- TOC entry 438 (class 1259 OID 51711)
-- Name: standard_kommunkarta_punkt_id_seq6; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq6 OWNER TO geovis;

--
-- TOC entry 8248 (class 0 OID 0)
-- Dependencies: 438
-- Name: standard_kommunkarta_punkt_id_seq6; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq6 OWNED BY odeshogext.karta_vardcentral.id;


--
-- TOC entry 439 (class 1259 OID 51713)
-- Name: standard_kommunkarta_punkt_id_seq60; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq60 OWNER TO geovis;

--
-- TOC entry 8249 (class 0 OID 0)
-- Dependencies: 439
-- Name: standard_kommunkarta_punkt_id_seq60; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq60 OWNED BY odeshogext.karta_busskort.id;


--
-- TOC entry 440 (class 1259 OID 51715)
-- Name: standard_kommunkarta_punkt_id_seq61; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq61 OWNER TO geovis;

--
-- TOC entry 8250 (class 0 OID 0)
-- Dependencies: 440
-- Name: standard_kommunkarta_punkt_id_seq61; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq61 OWNED BY odeshogext.karta_langdskidspar.id;


--
-- TOC entry 441 (class 1259 OID 51717)
-- Name: standard_kommunkarta_punkt_id_seq62; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq62 OWNER TO geovis;

--
-- TOC entry 8251 (class 0 OID 0)
-- Dependencies: 441
-- Name: standard_kommunkarta_punkt_id_seq62; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq62 OWNED BY odeshogext.karta_parkering_rorelsehindrade.id;


--
-- TOC entry 442 (class 1259 OID 51719)
-- Name: standard_kommunkarta_punkt_id_seq63; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq63 OWNER TO geovis;

--
-- TOC entry 8252 (class 0 OID 0)
-- Dependencies: 442
-- Name: standard_kommunkarta_punkt_id_seq63; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq63 OWNED BY odeshogext.karta_postombud.id;


--
-- TOC entry 443 (class 1259 OID 51721)
-- Name: standard_kommunkarta_punkt_id_seq64; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq64 OWNER TO geovis;

--
-- TOC entry 8253 (class 0 OID 0)
-- Dependencies: 443
-- Name: standard_kommunkarta_punkt_id_seq64; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq64 OWNED BY odeshogext.karta_skjutbana.id;


--
-- TOC entry 444 (class 1259 OID 51723)
-- Name: standard_kommunkarta_punkt_id_seq65; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq65 OWNER TO geovis;

--
-- TOC entry 8254 (class 0 OID 0)
-- Dependencies: 444
-- Name: standard_kommunkarta_punkt_id_seq65; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq65 OWNED BY odeshogext.karta_systembolaget.id;


--
-- TOC entry 445 (class 1259 OID 51725)
-- Name: standard_kommunkarta_punkt_id_seq66; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq66 OWNER TO geovis;

--
-- TOC entry 8255 (class 0 OID 0)
-- Dependencies: 445
-- Name: standard_kommunkarta_punkt_id_seq66; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq66 OWNED BY odeshogext.karta_utomhusgym.id;


--
-- TOC entry 446 (class 1259 OID 51727)
-- Name: standard_kommunkarta_punkt_id_seq67; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq67 OWNER TO geovis;

--
-- TOC entry 8256 (class 0 OID 0)
-- Dependencies: 446
-- Name: standard_kommunkarta_punkt_id_seq67; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq67 OWNED BY odeshogext.karta_bowling.id;


--
-- TOC entry 447 (class 1259 OID 51729)
-- Name: standard_kommunkarta_punkt_id_seq68; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq68 OWNER TO geovis;

--
-- TOC entry 8257 (class 0 OID 0)
-- Dependencies: 447
-- Name: standard_kommunkarta_punkt_id_seq68; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq68 OWNED BY odeshogext.karta_offentligatoaletter.id;


--
-- TOC entry 448 (class 1259 OID 51731)
-- Name: standard_kommunkarta_punkt_id_seq69; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq69 OWNER TO geovis;

--
-- TOC entry 8258 (class 0 OID 0)
-- Dependencies: 448
-- Name: standard_kommunkarta_punkt_id_seq69; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq69 OWNED BY odeshogext.karta_kulturskolan.id;


--
-- TOC entry 449 (class 1259 OID 51733)
-- Name: standard_kommunkarta_punkt_id_seq7; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq7 OWNER TO geovis;

--
-- TOC entry 8259 (class 0 OID 0)
-- Dependencies: 449
-- Name: standard_kommunkarta_punkt_id_seq7; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq7 OWNED BY odeshogext.karta_kommunkontor.id;


--
-- TOC entry 450 (class 1259 OID 51735)
-- Name: standard_kommunkarta_punkt_id_seq70; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq70 OWNER TO geovis;

--
-- TOC entry 8260 (class 0 OID 0)
-- Dependencies: 450
-- Name: standard_kommunkarta_punkt_id_seq70; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq70 OWNED BY odeshogext.karta_bygdegardar.id;


--
-- TOC entry 451 (class 1259 OID 51737)
-- Name: standard_kommunkarta_punkt_id_seq71; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq71 OWNER TO geovis;

--
-- TOC entry 8261 (class 0 OID 0)
-- Dependencies: 451
-- Name: standard_kommunkarta_punkt_id_seq71; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq71 OWNED BY odeshogext.karta_hembygdsforening.id;


--
-- TOC entry 452 (class 1259 OID 51739)
-- Name: standard_kommunkarta_punkt_id_seq72; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq72 OWNER TO geovis;

--
-- TOC entry 8262 (class 0 OID 0)
-- Dependencies: 452
-- Name: standard_kommunkarta_punkt_id_seq72; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq72 OWNED BY odeshogext.karta_spontanidrottsplats.id;


--
-- TOC entry 453 (class 1259 OID 51741)
-- Name: standard_kommunkarta_punkt_id_seq73; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq73 OWNER TO geovis;

--
-- TOC entry 8263 (class 0 OID 0)
-- Dependencies: 453
-- Name: standard_kommunkarta_punkt_id_seq73; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq73 OWNED BY odeshogext.karta_hundklubb.id;


--
-- TOC entry 454 (class 1259 OID 51743)
-- Name: standard_kommunkarta_punkt_id_seq74; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq74 OWNER TO geovis;

--
-- TOC entry 8264 (class 0 OID 0)
-- Dependencies: 454
-- Name: standard_kommunkarta_punkt_id_seq74; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq74 OWNED BY odeshogext.karta_gym.id;


--
-- TOC entry 455 (class 1259 OID 51745)
-- Name: standard_kommunkarta_punkt_id_seq75; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq75 OWNER TO geovis;

--
-- TOC entry 8265 (class 0 OID 0)
-- Dependencies: 455
-- Name: standard_kommunkarta_punkt_id_seq75; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq75 OWNED BY odeshogext."karta_lediga-bostadstomter".id;


--
-- TOC entry 456 (class 1259 OID 51747)
-- Name: standard_kommunkarta_punkt_id_seq76; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq76 OWNER TO geovis;

--
-- TOC entry 8266 (class 0 OID 0)
-- Dependencies: 456
-- Name: standard_kommunkarta_punkt_id_seq76; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq76 OWNED BY odeshogext.karta_hundpasar.id;


--
-- TOC entry 457 (class 1259 OID 51749)
-- Name: standard_kommunkarta_punkt_id_seq8; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq8 OWNER TO geovis;

--
-- TOC entry 8267 (class 0 OID 0)
-- Dependencies: 457
-- Name: standard_kommunkarta_punkt_id_seq8; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq8 OWNED BY odeshogext."karta_viktigt-meddelande".id;


--
-- TOC entry 458 (class 1259 OID 51751)
-- Name: standard_kommunkarta_punkt_id_seq9; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_punkt_id_seq9 OWNER TO geovis;

--
-- TOC entry 8268 (class 0 OID 0)
-- Dependencies: 458
-- Name: standard_kommunkarta_punkt_id_seq9; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_punkt_id_seq9 OWNED BY odeshogext."karta_daglig-verksamhet".id;


--
-- TOC entry 459 (class 1259 OID 51753)
-- Name: standard_kommunkarta_yta_id_seq; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq OWNER TO geovis;

--
-- TOC entry 8269 (class 0 OID 0)
-- Dependencies: 459
-- Name: standard_kommunkarta_yta_id_seq; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq OWNED BY odeshogext."karta_detaljplaner-gallande".id;


--
-- TOC entry 460 (class 1259 OID 51755)
-- Name: standard_kommunkarta_yta_id_seq1; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq1 OWNER TO geovis;

--
-- TOC entry 8270 (class 0 OID 0)
-- Dependencies: 460
-- Name: standard_kommunkarta_yta_id_seq1; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq1 OWNED BY odeshogext."karta_detaljplaner-pagaende".id;


--
-- TOC entry 461 (class 1259 OID 51757)
-- Name: standard_kommunkarta_yta_id_seq10; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq10 OWNER TO geovis;

--
-- TOC entry 8271 (class 0 OID 0)
-- Dependencies: 461
-- Name: standard_kommunkarta_yta_id_seq10; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq10 OWNED BY odeshogext."karta_naturvardsprogram-omraden".id;


--
-- TOC entry 462 (class 1259 OID 51759)
-- Name: standard_kommunkarta_yta_id_seq11; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq11 OWNER TO geovis;

--
-- TOC entry 8272 (class 0 OID 0)
-- Dependencies: 462
-- Name: standard_kommunkarta_yta_id_seq11; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq11 OWNED BY odeshogext.karta_sophamtning.id;


--
-- TOC entry 463 (class 1259 OID 51761)
-- Name: standard_kommunkarta_yta_id_seq12; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq12 OWNER TO geovis;

--
-- TOC entry 8273 (class 0 OID 0)
-- Dependencies: 463
-- Name: standard_kommunkarta_yta_id_seq12; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq12 OWNED BY odeshogext.karta_naturreservat.id;


--
-- TOC entry 464 (class 1259 OID 51763)
-- Name: standard_kommunkarta_yta_id_seq2; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq2 OWNER TO geovis;

--
-- TOC entry 8274 (class 0 OID 0)
-- Dependencies: 464
-- Name: standard_kommunkarta_yta_id_seq2; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq2 OWNED BY odeshogext."karta_grav-uppstallningstillstand".id;


--
-- TOC entry 465 (class 1259 OID 51765)
-- Name: standard_kommunkarta_yta_id_seq3; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq3 OWNER TO geovis;

--
-- TOC entry 8275 (class 0 OID 0)
-- Dependencies: 465
-- Name: standard_kommunkarta_yta_id_seq3; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq3 OWNED BY odeshogext."karta_naturvardsprogram-objekt".id;


--
-- TOC entry 466 (class 1259 OID 51767)
-- Name: standard_kommunkarta_yta_id_seq4; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq4 OWNER TO geovis;

--
-- TOC entry 8276 (class 0 OID 0)
-- Dependencies: 466
-- Name: standard_kommunkarta_yta_id_seq4; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq4 OWNED BY odeshogext.karta_torghandel.id;


--
-- TOC entry 467 (class 1259 OID 51769)
-- Name: standard_kommunkarta_yta_id_seq6; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq6 OWNER TO geovis;

--
-- TOC entry 8277 (class 0 OID 0)
-- Dependencies: 467
-- Name: standard_kommunkarta_yta_id_seq6; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq6 OWNED BY odeshogext."karta_ledig-industrimark".id;


--
-- TOC entry 468 (class 1259 OID 51771)
-- Name: standard_kommunkarta_yta_id_seq7; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq7 OWNER TO geovis;

--
-- TOC entry 8278 (class 0 OID 0)
-- Dependencies: 468
-- Name: standard_kommunkarta_yta_id_seq7; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq7 OWNED BY odeshogext."karta_kommunagd-mark".id;


--
-- TOC entry 469 (class 1259 OID 51773)
-- Name: standard_kommunkarta_yta_id_seq8; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq8 OWNER TO geovis;

--
-- TOC entry 8279 (class 0 OID 0)
-- Dependencies: 469
-- Name: standard_kommunkarta_yta_id_seq8; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq8 OWNED BY odeshogext.karta_bostadstomter_ejbebyggda.id;


--
-- TOC entry 470 (class 1259 OID 51775)
-- Name: standard_kommunkarta_yta_id_seq9; Type: SEQUENCE; Schema: odeshogext; Owner: geovis
--

CREATE SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE odeshogext.standard_kommunkarta_yta_id_seq9 OWNER TO geovis;

--
-- TOC entry 8280 (class 0 OID 0)
-- Dependencies: 470
-- Name: standard_kommunkarta_yta_id_seq9; Type: SEQUENCE OWNED BY; Schema: odeshogext; Owner: geovis
--

ALTER SEQUENCE odeshogext.standard_kommunkarta_yta_id_seq9 OWNED BY odeshogext."karta_ledig-industrimark_privatagd".id;


--
-- TOC entry 7764 (class 2604 OID 51779)
-- Name: karta_apotek id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_apotek ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq33'::regclass);


--
-- TOC entry 7765 (class 2604 OID 51780)
-- Name: karta_atervinningscentral id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_atervinningscentral ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq27'::regclass);


--
-- TOC entry 7766 (class 2604 OID 51781)
-- Name: karta_atervinningsstation id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_atervinningsstation ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq28'::regclass);


--
-- TOC entry 7767 (class 2604 OID 51782)
-- Name: karta_badplats id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_badplats ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq20'::regclass);


--
-- TOC entry 7768 (class 2604 OID 51783)
-- Name: karta_bankomat id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bankomat ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq41'::regclass);


--
-- TOC entry 7769 (class 2604 OID 51784)
-- Name: karta_basketplan id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_basketplan ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq56'::regclass);


--
-- TOC entry 7763 (class 2604 OID 51785)
-- Name: karta_batplatser id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_batplatser ALTER COLUMN id SET DEFAULT nextval('odeshogext."Batplatser_odeshog_id_seq"'::regclass);


--
-- TOC entry 7770 (class 2604 OID 51786)
-- Name: karta_beachvolley id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_beachvolley ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq57'::regclass);


--
-- TOC entry 7771 (class 2604 OID 51787)
-- Name: karta_begravningsplats id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_begravningsplats ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq58'::regclass);


--
-- TOC entry 7772 (class 2604 OID 51788)
-- Name: karta_belaggningsarbete id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_belaggningsarbete ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_linje_id_seq'::regclass);


--
-- TOC entry 7773 (class 2604 OID 51789)
-- Name: karta_bibliotek id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bibliotek ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq13'::regclass);


--
-- TOC entry 7774 (class 2604 OID 51790)
-- Name: karta_biograf id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_biograf ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq14'::regclass);


--
-- TOC entry 7775 (class 2604 OID 51791)
-- Name: karta_boende id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_boende ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq45'::regclass);


--
-- TOC entry 7776 (class 2604 OID 51792)
-- Name: karta_bostadstomter_ejbebyggda id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bostadstomter_ejbebyggda ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq8'::regclass);


--
-- TOC entry 7777 (class 2604 OID 51793)
-- Name: karta_boulebanor id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_boulebanor ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq59'::regclass);


--
-- TOC entry 7778 (class 2604 OID 51794)
-- Name: karta_bowling id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bowling ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq67'::regclass);


--
-- TOC entry 7779 (class 2604 OID 51795)
-- Name: karta_busshallplats id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_busshallplats ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq53'::regclass);


--
-- TOC entry 7780 (class 2604 OID 51796)
-- Name: karta_busskort id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_busskort ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq60'::regclass);


--
-- TOC entry 7781 (class 2604 OID 51797)
-- Name: karta_busstation id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_busstation ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq31'::regclass);


--
-- TOC entry 7782 (class 2604 OID 51798)
-- Name: karta_bygdegardar id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bygdegardar ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq70'::regclass);


--
-- TOC entry 7783 (class 2604 OID 51799)
-- Name: karta_cafe id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_cafe ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq50'::regclass);


--
-- TOC entry 7784 (class 2604 OID 51800)
-- Name: karta_cykelleder id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_cykelleder ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_linje_id_seq2'::regclass);


--
-- TOC entry 7785 (class 2604 OID 51801)
-- Name: karta_cykelvagar id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_cykelvagar ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_linje_id_seq3'::regclass);


--
-- TOC entry 7786 (class 2604 OID 51802)
-- Name: karta_daglig-verksamhet id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_daglig-verksamhet" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq9'::regclass);


--
-- TOC entry 7787 (class 2604 OID 51803)
-- Name: karta_detaljplaner-gallande id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_detaljplaner-gallande" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq'::regclass);


--
-- TOC entry 7788 (class 2604 OID 51804)
-- Name: karta_detaljplaner-pagaende id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_detaljplaner-pagaende" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq1'::regclass);


--
-- TOC entry 7789 (class 2604 OID 51805)
-- Name: karta_drivmedelsstation id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_drivmedelsstation ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq52'::regclass);


--
-- TOC entry 7790 (class 2604 OID 51806)
-- Name: karta_fiske id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fiske ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq54'::regclass);


--
-- TOC entry 7791 (class 2604 OID 51807)
-- Name: karta_fiskekort id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fiskekort ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq55'::regclass);


--
-- TOC entry 7792 (class 2604 OID 51808)
-- Name: karta_folkets-park id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_folkets-park" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq15'::regclass);


--
-- TOC entry 7793 (class 2604 OID 51809)
-- Name: karta_folktandvard id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_folktandvard ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq36'::regclass);


--
-- TOC entry 7794 (class 2604 OID 51810)
-- Name: karta_forskola id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_forskola ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq1'::regclass);


--
-- TOC entry 7795 (class 2604 OID 51811)
-- Name: karta_fotbollsplaner id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fotbollsplaner ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq17'::regclass);


--
-- TOC entry 7796 (class 2604 OID 51812)
-- Name: karta_friidrottsanl id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_friidrottsanl ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq18'::regclass);


--
-- TOC entry 7797 (class 2604 OID 51813)
-- Name: karta_fritidsgard id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fritidsgard ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq21'::regclass);


--
-- TOC entry 7798 (class 2604 OID 51814)
-- Name: karta_fritidshem id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fritidshem ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq40'::regclass);


--
-- TOC entry 7799 (class 2604 OID 51815)
-- Name: karta_golfbanor id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_golfbanor ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq16'::regclass);


--
-- TOC entry 7800 (class 2604 OID 51816)
-- Name: karta_grav-uppstallningstillstand id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_grav-uppstallningstillstand" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq2'::regclass);


--
-- TOC entry 7801 (class 2604 OID 51817)
-- Name: karta_gym id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_gym ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq74'::regclass);


--
-- TOC entry 7802 (class 2604 OID 51818)
-- Name: karta_hamn id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hamn ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq39'::regclass);


--
-- TOC entry 7803 (class 2604 OID 51819)
-- Name: karta_hembygdsforening id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hembygdsforening ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq71'::regclass);


--
-- TOC entry 7804 (class 2604 OID 51820)
-- Name: karta_hjartstartare id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hjartstartare ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq43'::regclass);


--
-- TOC entry 7805 (class 2604 OID 51821)
-- Name: karta_hundklubb id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hundklubb ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq73'::regclass);


--
-- TOC entry 7806 (class 2604 OID 51822)
-- Name: karta_hundpasar id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hundpasar ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq76'::regclass);


--
-- TOC entry 7807 (class 2604 OID 51823)
-- Name: karta_idrottshall id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_idrottshall ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq19'::regclass);


--
-- TOC entry 7808 (class 2604 OID 51824)
-- Name: karta_isbanor id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_isbanor ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq2'::regclass);


--
-- TOC entry 7809 (class 2604 OID 51825)
-- Name: karta_kommunagd-mark id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_kommunagd-mark" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq7'::regclass);


--
-- TOC entry 7810 (class 2604 OID 51826)
-- Name: karta_kommunkontor id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_kommunkontor ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq7'::regclass);


--
-- TOC entry 7811 (class 2604 OID 51827)
-- Name: karta_konsumentradgivning id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_konsumentradgivning ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq4'::regclass);


--
-- TOC entry 7812 (class 2604 OID 51828)
-- Name: karta_kulturskolan id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_kulturskolan ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq69'::regclass);


--
-- TOC entry 7813 (class 2604 OID 51829)
-- Name: karta_laddstationer id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_laddstationer ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq34'::regclass);


--
-- TOC entry 7814 (class 2604 OID 51830)
-- Name: karta_langdskidspar id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_langdskidspar ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq61'::regclass);


--
-- TOC entry 7815 (class 2604 OID 51831)
-- Name: karta_ledig-industrimark id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_ledig-industrimark" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq6'::regclass);


--
-- TOC entry 7816 (class 2604 OID 51832)
-- Name: karta_ledig-industrimark_privatagd id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_ledig-industrimark_privatagd" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq9'::regclass);


--
-- TOC entry 7817 (class 2604 OID 51833)
-- Name: karta_lediga-bostadstomter id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_lediga-bostadstomter" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq75'::regclass);


--
-- TOC entry 7818 (class 2604 OID 51834)
-- Name: karta_lekplats id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_lekplats ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq35'::regclass);


--
-- TOC entry 7819 (class 2604 OID 51835)
-- Name: karta_livsmedelsbutik id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_livsmedelsbutik ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq51'::regclass);


--
-- TOC entry 7820 (class 2604 OID 51836)
-- Name: karta_motionsanl id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_motionsanl ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq22'::regclass);


--
-- TOC entry 7821 (class 2604 OID 51837)
-- Name: karta_motionsspar_elljusspar id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_motionsspar_elljusspar ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_linje_id_seq4'::regclass);


--
-- TOC entry 7822 (class 2604 OID 51838)
-- Name: karta_motionsspar_uppmarkta id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_motionsspar_uppmarkta ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_linje_id_seq5'::regclass);


--
-- TOC entry 7823 (class 2604 OID 51839)
-- Name: karta_motorcrossbana id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_motorcrossbana ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq25'::regclass);


--
-- TOC entry 7824 (class 2604 OID 51840)
-- Name: karta_naturreservat id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_naturreservat ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq12'::regclass);


--
-- TOC entry 7825 (class 2604 OID 51841)
-- Name: karta_naturvardsprogram-objekt id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_naturvardsprogram-objekt" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq3'::regclass);


--
-- TOC entry 7826 (class 2604 OID 51842)
-- Name: karta_naturvardsprogram-omraden id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_naturvardsprogram-omraden" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq10'::regclass);


--
-- TOC entry 7827 (class 2604 OID 51843)
-- Name: karta_obo id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_obo ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq38'::regclass);


--
-- TOC entry 7828 (class 2604 OID 51844)
-- Name: karta_offentligatoaletter id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_offentligatoaletter ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq68'::regclass);


--
-- TOC entry 7829 (class 2604 OID 51845)
-- Name: karta_okad-sysselsattning id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_okad-sysselsattning" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq10'::regclass);


--
-- TOC entry 7830 (class 2604 OID 51846)
-- Name: karta_parkering-pendlar id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_parkering-pendlar" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq29'::regclass);


--
-- TOC entry 7831 (class 2604 OID 51847)
-- Name: karta_parkering_rorelsehindrade id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_parkering_rorelsehindrade ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq62'::regclass);


--
-- TOC entry 7832 (class 2604 OID 51848)
-- Name: karta_parkeringsplatser id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_parkeringsplatser ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq30'::regclass);


--
-- TOC entry 7833 (class 2604 OID 51849)
-- Name: karta_polis id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_polis ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq3'::regclass);


--
-- TOC entry 7834 (class 2604 OID 51850)
-- Name: karta_postombud id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_postombud ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq63'::regclass);


--
-- TOC entry 7835 (class 2604 OID 51851)
-- Name: karta_racketanl id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_racketanl ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq23'::regclass);


--
-- TOC entry 7836 (class 2604 OID 51852)
-- Name: karta_raddningstjanst id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_raddningstjanst ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq5'::regclass);


--
-- TOC entry 7837 (class 2604 OID 51853)
-- Name: karta_restauranger id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_restauranger ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq49'::regclass);


--
-- TOC entry 7838 (class 2604 OID 51854)
-- Name: karta_ridanl id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_ridanl ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq26'::regclass);


--
-- TOC entry 7839 (class 2604 OID 51855)
-- Name: karta_sevardheter_besoksmal id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_sevardheter_besoksmal ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq48'::regclass);


--
-- TOC entry 7840 (class 2604 OID 51856)
-- Name: karta_sevardheter_kultur id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_sevardheter_kultur ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq47'::regclass);


--
-- TOC entry 7841 (class 2604 OID 51857)
-- Name: karta_sevardheter_natur id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_sevardheter_natur ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq46'::regclass);


--
-- TOC entry 7842 (class 2604 OID 51858)
-- Name: karta_skatepark id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_skatepark ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq24'::regclass);


--
-- TOC entry 7843 (class 2604 OID 51859)
-- Name: karta_skjutbana id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_skjutbana ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq64'::regclass);


--
-- TOC entry 7844 (class 2604 OID 51860)
-- Name: karta_skola id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_skola ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq'::regclass);


--
-- TOC entry 7845 (class 2604 OID 51861)
-- Name: karta_socialkontoret id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_socialkontoret ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq37'::regclass);


--
-- TOC entry 7846 (class 2604 OID 51862)
-- Name: karta_sophamtning id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_sophamtning ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq11'::regclass);


--
-- TOC entry 7847 (class 2604 OID 51863)
-- Name: karta_spontanidrottsplats id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_spontanidrottsplats ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq72'::regclass);


--
-- TOC entry 7848 (class 2604 OID 51864)
-- Name: karta_systembolaget id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_systembolaget ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq65'::regclass);


--
-- TOC entry 7849 (class 2604 OID 51865)
-- Name: karta_torghandel id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_torghandel ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_yta_id_seq4'::regclass);


--
-- TOC entry 7850 (class 2604 OID 51866)
-- Name: karta_traffpunkt-aktivitetshuset id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_traffpunkt-aktivitetshuset" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq12'::regclass);


--
-- TOC entry 7851 (class 2604 OID 51867)
-- Name: karta_traffpunkt-aldre id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_traffpunkt-aldre" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq11'::regclass);


--
-- TOC entry 7852 (class 2604 OID 51868)
-- Name: karta_traffpunkt-funktionsnedsatta id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_traffpunkt-funktionsnedsatta" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq42'::regclass);


--
-- TOC entry 7853 (class 2604 OID 51869)
-- Name: karta_turistinformation id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_turistinformation ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq44'::regclass);


--
-- TOC entry 7854 (class 2604 OID 51870)
-- Name: karta_uppstallningsplats-husbilar id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_uppstallningsplats-husbilar" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq32'::regclass);


--
-- TOC entry 7855 (class 2604 OID 51871)
-- Name: karta_utomhusgym id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_utomhusgym ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq66'::regclass);


--
-- TOC entry 7856 (class 2604 OID 51872)
-- Name: karta_vandringsleder id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_vandringsleder ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_linje_id_seq1'::regclass);


--
-- TOC entry 7857 (class 2604 OID 51873)
-- Name: karta_vardcentral id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_vardcentral ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq6'::regclass);


--
-- TOC entry 7858 (class 2604 OID 51874)
-- Name: karta_viktigt-meddelande id; Type: DEFAULT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_viktigt-meddelande" ALTER COLUMN id SET DEFAULT nextval('odeshogext.standard_kommunkarta_punkt_id_seq8'::regclass);


--
-- TOC entry 7860 (class 2606 OID 51977)
-- Name: karta_batplatser Batplatser_odeshog_pkey; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_batplatser
    ADD CONSTRAINT "Batplatser_odeshog_pkey" PRIMARY KEY (id);


--
-- TOC entry 7878 (class 2606 OID 51979)
-- Name: karta_belaggningsarbete standard_kommunkarta_linje_pkey; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_belaggningsarbete
    ADD CONSTRAINT standard_kommunkarta_linje_pkey PRIMARY KEY (id);


--
-- TOC entry 8046 (class 2606 OID 51981)
-- Name: karta_vandringsleder standard_kommunkarta_linje_pkey1; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_vandringsleder
    ADD CONSTRAINT standard_kommunkarta_linje_pkey1 PRIMARY KEY (id);


--
-- TOC entry 7902 (class 2606 OID 51983)
-- Name: karta_cykelleder standard_kommunkarta_linje_pkey2; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_cykelleder
    ADD CONSTRAINT standard_kommunkarta_linje_pkey2 PRIMARY KEY (id);


--
-- TOC entry 7904 (class 2606 OID 51985)
-- Name: karta_cykelvagar standard_kommunkarta_linje_pkey3; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_cykelvagar
    ADD CONSTRAINT standard_kommunkarta_linje_pkey3 PRIMARY KEY (id);


--
-- TOC entry 7976 (class 2606 OID 51987)
-- Name: karta_motionsspar_elljusspar standard_kommunkarta_linje_pkey4; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_motionsspar_elljusspar
    ADD CONSTRAINT standard_kommunkarta_linje_pkey4 PRIMARY KEY (id);


--
-- TOC entry 7978 (class 2606 OID 51989)
-- Name: karta_motionsspar_uppmarkta standard_kommunkarta_linje_pkey5; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_motionsspar_uppmarkta
    ADD CONSTRAINT standard_kommunkarta_linje_pkey5 PRIMARY KEY (id);


--
-- TOC entry 8022 (class 2606 OID 51991)
-- Name: karta_skola standard_kommunkarta_punkt_pkey; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_skola
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey PRIMARY KEY (id);


--
-- TOC entry 7922 (class 2606 OID 51993)
-- Name: karta_forskola standard_kommunkarta_punkt_pkey1; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_forskola
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey1 PRIMARY KEY (id);


--
-- TOC entry 7992 (class 2606 OID 51995)
-- Name: karta_okad-sysselsattning standard_kommunkarta_punkt_pkey10; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_okad-sysselsattning"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey10 PRIMARY KEY (id);


--
-- TOC entry 8036 (class 2606 OID 51997)
-- Name: karta_traffpunkt-aldre standard_kommunkarta_punkt_pkey11; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_traffpunkt-aldre"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey11 PRIMARY KEY (id);


--
-- TOC entry 8034 (class 2606 OID 51999)
-- Name: karta_traffpunkt-aktivitetshuset standard_kommunkarta_punkt_pkey12; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_traffpunkt-aktivitetshuset"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey12 PRIMARY KEY (id);


--
-- TOC entry 7880 (class 2606 OID 52001)
-- Name: karta_bibliotek standard_kommunkarta_punkt_pkey13; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bibliotek
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey13 PRIMARY KEY (id);


--
-- TOC entry 7882 (class 2606 OID 52003)
-- Name: karta_biograf standard_kommunkarta_punkt_pkey14; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_biograf
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey14 PRIMARY KEY (id);


--
-- TOC entry 7918 (class 2606 OID 52005)
-- Name: karta_folkets-park standard_kommunkarta_punkt_pkey15; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_folkets-park"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey15 PRIMARY KEY (id);


--
-- TOC entry 7932 (class 2606 OID 52007)
-- Name: karta_golfbanor standard_kommunkarta_punkt_pkey16; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_golfbanor
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey16 PRIMARY KEY (id);


--
-- TOC entry 7924 (class 2606 OID 52009)
-- Name: karta_fotbollsplaner standard_kommunkarta_punkt_pkey17; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fotbollsplaner
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey17 PRIMARY KEY (id);


--
-- TOC entry 7926 (class 2606 OID 52011)
-- Name: karta_friidrottsanl standard_kommunkarta_punkt_pkey18; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_friidrottsanl
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey18 PRIMARY KEY (id);


--
-- TOC entry 7948 (class 2606 OID 52013)
-- Name: karta_idrottshall standard_kommunkarta_punkt_pkey19; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_idrottshall
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey19 PRIMARY KEY (id);


--
-- TOC entry 7950 (class 2606 OID 52015)
-- Name: karta_isbanor standard_kommunkarta_punkt_pkey2; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_isbanor
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey2 PRIMARY KEY (id);


--
-- TOC entry 7868 (class 2606 OID 52017)
-- Name: karta_badplats standard_kommunkarta_punkt_pkey20; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_badplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey20 PRIMARY KEY (id);


--
-- TOC entry 7928 (class 2606 OID 52019)
-- Name: karta_fritidsgard standard_kommunkarta_punkt_pkey21; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fritidsgard
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey21 PRIMARY KEY (id);


--
-- TOC entry 7974 (class 2606 OID 52021)
-- Name: karta_motionsanl standard_kommunkarta_punkt_pkey22; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_motionsanl
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey22 PRIMARY KEY (id);


--
-- TOC entry 8004 (class 2606 OID 52023)
-- Name: karta_racketanl standard_kommunkarta_punkt_pkey23; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_racketanl
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey23 PRIMARY KEY (id);


--
-- TOC entry 8018 (class 2606 OID 52025)
-- Name: karta_skatepark standard_kommunkarta_punkt_pkey24; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_skatepark
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey24 PRIMARY KEY (id);


--
-- TOC entry 7980 (class 2606 OID 52027)
-- Name: karta_motorcrossbana standard_kommunkarta_punkt_pkey25; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_motorcrossbana
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey25 PRIMARY KEY (id);


--
-- TOC entry 8010 (class 2606 OID 52029)
-- Name: karta_ridanl standard_kommunkarta_punkt_pkey26; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_ridanl
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey26 PRIMARY KEY (id);


--
-- TOC entry 7864 (class 2606 OID 52031)
-- Name: karta_atervinningscentral standard_kommunkarta_punkt_pkey27; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_atervinningscentral
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey27 PRIMARY KEY (id);


--
-- TOC entry 7866 (class 2606 OID 52033)
-- Name: karta_atervinningsstation standard_kommunkarta_punkt_pkey28; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_atervinningsstation
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey28 PRIMARY KEY (id);


--
-- TOC entry 7994 (class 2606 OID 52035)
-- Name: karta_parkering-pendlar standard_kommunkarta_punkt_pkey29; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_parkering-pendlar"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey29 PRIMARY KEY (id);


--
-- TOC entry 8000 (class 2606 OID 52037)
-- Name: karta_polis standard_kommunkarta_punkt_pkey3; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_polis
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey3 PRIMARY KEY (id);


--
-- TOC entry 7998 (class 2606 OID 52039)
-- Name: karta_parkeringsplatser standard_kommunkarta_punkt_pkey30; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_parkeringsplatser
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey30 PRIMARY KEY (id);


--
-- TOC entry 7896 (class 2606 OID 52041)
-- Name: karta_busstation standard_kommunkarta_punkt_pkey31; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_busstation
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey31 PRIMARY KEY (id);


--
-- TOC entry 8042 (class 2606 OID 52043)
-- Name: karta_uppstallningsplats-husbilar standard_kommunkarta_punkt_pkey32; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_uppstallningsplats-husbilar"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey32 PRIMARY KEY (id);


--
-- TOC entry 7862 (class 2606 OID 52045)
-- Name: karta_apotek standard_kommunkarta_punkt_pkey33; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_apotek
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey33 PRIMARY KEY (id);


--
-- TOC entry 7960 (class 2606 OID 52047)
-- Name: karta_laddstationer standard_kommunkarta_punkt_pkey34; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_laddstationer
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey34 PRIMARY KEY (id);


--
-- TOC entry 7970 (class 2606 OID 52049)
-- Name: karta_lekplats standard_kommunkarta_punkt_pkey35; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_lekplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey35 PRIMARY KEY (id);


--
-- TOC entry 7920 (class 2606 OID 52051)
-- Name: karta_folktandvard standard_kommunkarta_punkt_pkey36; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_folktandvard
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey36 PRIMARY KEY (id);


--
-- TOC entry 8024 (class 2606 OID 52053)
-- Name: karta_socialkontoret standard_kommunkarta_punkt_pkey37; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_socialkontoret
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey37 PRIMARY KEY (id);


--
-- TOC entry 7988 (class 2606 OID 52055)
-- Name: karta_obo standard_kommunkarta_punkt_pkey38; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_obo
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey38 PRIMARY KEY (id);


--
-- TOC entry 7938 (class 2606 OID 52057)
-- Name: karta_hamn standard_kommunkarta_punkt_pkey39; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hamn
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey39 PRIMARY KEY (id);


--
-- TOC entry 7956 (class 2606 OID 52059)
-- Name: karta_konsumentradgivning standard_kommunkarta_punkt_pkey4; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_konsumentradgivning
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey4 PRIMARY KEY (id);


--
-- TOC entry 7930 (class 2606 OID 52061)
-- Name: karta_fritidshem standard_kommunkarta_punkt_pkey40; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fritidshem
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey40 PRIMARY KEY (id);


--
-- TOC entry 7870 (class 2606 OID 52063)
-- Name: karta_bankomat standard_kommunkarta_punkt_pkey41; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bankomat
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey41 PRIMARY KEY (id);


--
-- TOC entry 8038 (class 2606 OID 52065)
-- Name: karta_traffpunkt-funktionsnedsatta standard_kommunkarta_punkt_pkey42; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_traffpunkt-funktionsnedsatta"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey42 PRIMARY KEY (id);


--
-- TOC entry 7942 (class 2606 OID 52067)
-- Name: karta_hjartstartare standard_kommunkarta_punkt_pkey43; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hjartstartare
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey43 PRIMARY KEY (id);


--
-- TOC entry 8040 (class 2606 OID 52069)
-- Name: karta_turistinformation standard_kommunkarta_punkt_pkey44; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_turistinformation
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey44 PRIMARY KEY (id);


--
-- TOC entry 7884 (class 2606 OID 52071)
-- Name: karta_boende standard_kommunkarta_punkt_pkey45; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_boende
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey45 PRIMARY KEY (id);


--
-- TOC entry 8016 (class 2606 OID 52073)
-- Name: karta_sevardheter_natur standard_kommunkarta_punkt_pkey46; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_sevardheter_natur
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey46 PRIMARY KEY (id);


--
-- TOC entry 8014 (class 2606 OID 52075)
-- Name: karta_sevardheter_kultur standard_kommunkarta_punkt_pkey47; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_sevardheter_kultur
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey47 PRIMARY KEY (id);


--
-- TOC entry 8012 (class 2606 OID 52077)
-- Name: karta_sevardheter_besoksmal standard_kommunkarta_punkt_pkey48; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_sevardheter_besoksmal
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey48 PRIMARY KEY (id);


--
-- TOC entry 8008 (class 2606 OID 52079)
-- Name: karta_restauranger standard_kommunkarta_punkt_pkey49; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_restauranger
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey49 PRIMARY KEY (id);


--
-- TOC entry 8006 (class 2606 OID 52081)
-- Name: karta_raddningstjanst standard_kommunkarta_punkt_pkey5; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_raddningstjanst
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey5 PRIMARY KEY (id);


--
-- TOC entry 7900 (class 2606 OID 52083)
-- Name: karta_cafe standard_kommunkarta_punkt_pkey50; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_cafe
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey50 PRIMARY KEY (id);


--
-- TOC entry 7972 (class 2606 OID 52085)
-- Name: karta_livsmedelsbutik standard_kommunkarta_punkt_pkey51; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_livsmedelsbutik
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey51 PRIMARY KEY (id);


--
-- TOC entry 7912 (class 2606 OID 52087)
-- Name: karta_drivmedelsstation standard_kommunkarta_punkt_pkey52; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_drivmedelsstation
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey52 PRIMARY KEY (id);


--
-- TOC entry 7892 (class 2606 OID 52089)
-- Name: karta_busshallplats standard_kommunkarta_punkt_pkey53; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_busshallplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey53 PRIMARY KEY (id);


--
-- TOC entry 7914 (class 2606 OID 52091)
-- Name: karta_fiske standard_kommunkarta_punkt_pkey54; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fiske
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey54 PRIMARY KEY (id);


--
-- TOC entry 7916 (class 2606 OID 52093)
-- Name: karta_fiskekort standard_kommunkarta_punkt_pkey55; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_fiskekort
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey55 PRIMARY KEY (id);


--
-- TOC entry 7872 (class 2606 OID 52095)
-- Name: karta_basketplan standard_kommunkarta_punkt_pkey56; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_basketplan
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey56 PRIMARY KEY (id);


--
-- TOC entry 7874 (class 2606 OID 52097)
-- Name: karta_beachvolley standard_kommunkarta_punkt_pkey57; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_beachvolley
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey57 PRIMARY KEY (id);


--
-- TOC entry 7876 (class 2606 OID 52099)
-- Name: karta_begravningsplats standard_kommunkarta_punkt_pkey58; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_begravningsplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey58 PRIMARY KEY (id);


--
-- TOC entry 7888 (class 2606 OID 52101)
-- Name: karta_boulebanor standard_kommunkarta_punkt_pkey59; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_boulebanor
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey59 PRIMARY KEY (id);


--
-- TOC entry 8048 (class 2606 OID 52103)
-- Name: karta_vardcentral standard_kommunkarta_punkt_pkey6; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_vardcentral
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey6 PRIMARY KEY (id);


--
-- TOC entry 7894 (class 2606 OID 52105)
-- Name: karta_busskort standard_kommunkarta_punkt_pkey60; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_busskort
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey60 PRIMARY KEY (id);


--
-- TOC entry 7962 (class 2606 OID 52107)
-- Name: karta_langdskidspar standard_kommunkarta_punkt_pkey61; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_langdskidspar
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey61 PRIMARY KEY (id);


--
-- TOC entry 7996 (class 2606 OID 52109)
-- Name: karta_parkering_rorelsehindrade standard_kommunkarta_punkt_pkey62; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_parkering_rorelsehindrade
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey62 PRIMARY KEY (id);


--
-- TOC entry 8002 (class 2606 OID 52111)
-- Name: karta_postombud standard_kommunkarta_punkt_pkey63; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_postombud
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey63 PRIMARY KEY (id);


--
-- TOC entry 8020 (class 2606 OID 52113)
-- Name: karta_skjutbana standard_kommunkarta_punkt_pkey64; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_skjutbana
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey64 PRIMARY KEY (id);


--
-- TOC entry 8030 (class 2606 OID 52115)
-- Name: karta_systembolaget standard_kommunkarta_punkt_pkey65; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_systembolaget
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey65 PRIMARY KEY (id);


--
-- TOC entry 8044 (class 2606 OID 52117)
-- Name: karta_utomhusgym standard_kommunkarta_punkt_pkey66; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_utomhusgym
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey66 PRIMARY KEY (id);


--
-- TOC entry 7890 (class 2606 OID 52119)
-- Name: karta_bowling standard_kommunkarta_punkt_pkey67; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bowling
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey67 PRIMARY KEY (id);


--
-- TOC entry 7990 (class 2606 OID 52121)
-- Name: karta_offentligatoaletter standard_kommunkarta_punkt_pkey68; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_offentligatoaletter
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey68 PRIMARY KEY (id);


--
-- TOC entry 7958 (class 2606 OID 52123)
-- Name: karta_kulturskolan standard_kommunkarta_punkt_pkey69; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_kulturskolan
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey69 PRIMARY KEY (id);


--
-- TOC entry 7954 (class 2606 OID 52125)
-- Name: karta_kommunkontor standard_kommunkarta_punkt_pkey7; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_kommunkontor
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey7 PRIMARY KEY (id);


--
-- TOC entry 7898 (class 2606 OID 52127)
-- Name: karta_bygdegardar standard_kommunkarta_punkt_pkey70; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bygdegardar
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey70 PRIMARY KEY (id);


--
-- TOC entry 7940 (class 2606 OID 52129)
-- Name: karta_hembygdsforening standard_kommunkarta_punkt_pkey71; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hembygdsforening
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey71 PRIMARY KEY (id);


--
-- TOC entry 8028 (class 2606 OID 52131)
-- Name: karta_spontanidrottsplats standard_kommunkarta_punkt_pkey72; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_spontanidrottsplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey72 PRIMARY KEY (id);


--
-- TOC entry 7944 (class 2606 OID 52133)
-- Name: karta_hundklubb standard_kommunkarta_punkt_pkey73; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hundklubb
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey73 PRIMARY KEY (id);


--
-- TOC entry 7936 (class 2606 OID 52135)
-- Name: karta_gym standard_kommunkarta_punkt_pkey74; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_gym
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey74 PRIMARY KEY (id);


--
-- TOC entry 7968 (class 2606 OID 52137)
-- Name: karta_lediga-bostadstomter standard_kommunkarta_punkt_pkey75; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_lediga-bostadstomter"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey75 PRIMARY KEY (id);


--
-- TOC entry 7946 (class 2606 OID 52139)
-- Name: karta_hundpasar standard_kommunkarta_punkt_pkey76; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_hundpasar
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey76 PRIMARY KEY (id);


--
-- TOC entry 8050 (class 2606 OID 52141)
-- Name: karta_viktigt-meddelande standard_kommunkarta_punkt_pkey8; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_viktigt-meddelande"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey8 PRIMARY KEY (id);


--
-- TOC entry 7906 (class 2606 OID 52143)
-- Name: karta_daglig-verksamhet standard_kommunkarta_punkt_pkey9; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_daglig-verksamhet"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey9 PRIMARY KEY (id);


--
-- TOC entry 7908 (class 2606 OID 52145)
-- Name: karta_detaljplaner-gallande standard_kommunkarta_yta_pkey; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_detaljplaner-gallande"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey PRIMARY KEY (id);


--
-- TOC entry 7910 (class 2606 OID 52147)
-- Name: karta_detaljplaner-pagaende standard_kommunkarta_yta_pkey1; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_detaljplaner-pagaende"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey1 PRIMARY KEY (id);


--
-- TOC entry 7986 (class 2606 OID 52149)
-- Name: karta_naturvardsprogram-omraden standard_kommunkarta_yta_pkey10; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_naturvardsprogram-omraden"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey10 PRIMARY KEY (id);


--
-- TOC entry 8026 (class 2606 OID 52151)
-- Name: karta_sophamtning standard_kommunkarta_yta_pkey11; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_sophamtning
    ADD CONSTRAINT standard_kommunkarta_yta_pkey11 PRIMARY KEY (id);


--
-- TOC entry 7982 (class 2606 OID 52153)
-- Name: karta_naturreservat standard_kommunkarta_yta_pkey12; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_naturreservat
    ADD CONSTRAINT standard_kommunkarta_yta_pkey12 PRIMARY KEY (id);


--
-- TOC entry 7934 (class 2606 OID 52155)
-- Name: karta_grav-uppstallningstillstand standard_kommunkarta_yta_pkey2; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_grav-uppstallningstillstand"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey2 PRIMARY KEY (id);


--
-- TOC entry 7984 (class 2606 OID 52157)
-- Name: karta_naturvardsprogram-objekt standard_kommunkarta_yta_pkey3; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_naturvardsprogram-objekt"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey3 PRIMARY KEY (id);


--
-- TOC entry 8032 (class 2606 OID 52159)
-- Name: karta_torghandel standard_kommunkarta_yta_pkey4; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_torghandel
    ADD CONSTRAINT standard_kommunkarta_yta_pkey4 PRIMARY KEY (id);


--
-- TOC entry 7964 (class 2606 OID 52161)
-- Name: karta_ledig-industrimark standard_kommunkarta_yta_pkey6; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_ledig-industrimark"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey6 PRIMARY KEY (id);


--
-- TOC entry 7952 (class 2606 OID 52163)
-- Name: karta_kommunagd-mark standard_kommunkarta_yta_pkey7; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_kommunagd-mark"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey7 PRIMARY KEY (id);


--
-- TOC entry 7886 (class 2606 OID 52165)
-- Name: karta_bostadstomter_ejbebyggda standard_kommunkarta_yta_pkey8; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext.karta_bostadstomter_ejbebyggda
    ADD CONSTRAINT standard_kommunkarta_yta_pkey8 PRIMARY KEY (id);


--
-- TOC entry 7966 (class 2606 OID 52167)
-- Name: karta_ledig-industrimark_privatagd standard_kommunkarta_yta_pkey9; Type: CONSTRAINT; Schema: odeshogext; Owner: geovis
--

ALTER TABLE ONLY odeshogext."karta_ledig-industrimark_privatagd"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey9 PRIMARY KEY (id);


--
-- TOC entry 8184 (class 0 OID 0)
-- Dependencies: 12
-- Name: SCHEMA odeshogext; Type: ACL; Schema: -; Owner: geovis
--

GRANT USAGE ON SCHEMA odeshogext TO "odeshog-read";
GRANT ALL ON SCHEMA odeshogext TO "xplore-admin";


-- Completed on 2019-10-16 11:31:18 CEST

--
-- PostgreSQL database dump complete
--

