--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5
-- Dumped by pg_dump version 11.5

-- Started on 2019-10-16 11:38:25 CEST

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
-- TOC entry 21 (class 2615 OID 100211)
-- Name: ips; Type: SCHEMA; Schema: -; Owner: xplore
--

CREATE SCHEMA ips;


ALTER SCHEMA ips OWNER TO xplore;

--
-- TOC entry 2356 (class 1255 OID 100212)
-- Name: trigger_func_skapad_kladdlager(); Type: FUNCTION; Schema: ips; Owner: xplore
--

CREATE FUNCTION ips.trigger_func_skapad_kladdlager() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.skapad = NOW();
  RETURN NEW;
END;
$$;


ALTER FUNCTION ips.trigger_func_skapad_kladdlager() OWNER TO xplore;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 895 (class 1259 OID 100213)
-- Name: kladdlager; Type: TABLE; Schema: ips; Owner: xplore
--

CREATE TABLE ips.kladdlager (
    id integer NOT NULL,
    skapad timestamp with time zone NOT NULL,
    geometry public.geometry NOT NULL,
    skapad_av character varying(40),
    stil json
);


ALTER TABLE ips.kladdlager OWNER TO xplore;

--
-- TOC entry 896 (class 1259 OID 100219)
-- Name: ips_kladdlager_pkey_seq; Type: SEQUENCE; Schema: ips; Owner: xplore
--

CREATE SEQUENCE ips.ips_kladdlager_pkey_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ips.ips_kladdlager_pkey_seq OWNER TO xplore;

--
-- TOC entry 7805 (class 0 OID 0)
-- Dependencies: 896
-- Name: ips_kladdlager_pkey_seq; Type: SEQUENCE OWNED BY; Schema: ips; Owner: xplore
--

ALTER SEQUENCE ips.ips_kladdlager_pkey_seq OWNED BY ips.kladdlager.id;


--
-- TOC entry 7668 (class 2604 OID 100221)
-- Name: kladdlager id; Type: DEFAULT; Schema: ips; Owner: xplore
--

ALTER TABLE ONLY ips.kladdlager ALTER COLUMN id SET DEFAULT nextval('ips.ips_kladdlager_pkey_seq'::regclass);


--
-- TOC entry 7670 (class 2606 OID 100223)
-- Name: kladdlager kladdlager_test_pkey; Type: CONSTRAINT; Schema: ips; Owner: xplore
--

ALTER TABLE ONLY ips.kladdlager
    ADD CONSTRAINT kladdlager_test_pkey PRIMARY KEY (id);


--
-- TOC entry 7671 (class 2620 OID 100224)
-- Name: kladdlager set_skapad_on_insert_trigger; Type: TRIGGER; Schema: ips; Owner: xplore
--

CREATE TRIGGER set_skapad_on_insert_trigger BEFORE INSERT ON ips.kladdlager FOR EACH ROW EXECUTE PROCEDURE ips.trigger_func_skapad_kladdlager();


-- Completed on 2019-10-16 11:38:25 CEST

--
-- PostgreSQL database dump complete
--

