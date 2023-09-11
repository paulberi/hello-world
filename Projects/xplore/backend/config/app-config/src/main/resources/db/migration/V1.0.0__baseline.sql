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
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: config; Type: SCHEMA; Schema: -; Owner: xplore
--

CREATE SCHEMA IF NOT EXISTS config;


ALTER SCHEMA config OWNER TO xplore;

SET default_tablespace = pg_default;

SET default_with_oids = false;

--
-- Name: app_config; Type: TABLE; Schema: config; Owner: xplore; Tablespace: pg_default
--

CREATE TABLE config.app_config (
    id bigint NOT NULL,
    name text NOT NULL,
    description text,
    json jsonb NOT NULL
);


ALTER TABLE config.app_config OWNER TO xplore;

--
-- Name: app_config_restriction; Type: TABLE; Schema: config; Owner: xplore; Tablespace: pg_default
--

CREATE TABLE config.app_config_restriction (
    id bigint NOT NULL,
    app_config_id bigint NOT NULL,
    realms text[],
    clients text[],
    users text[],
    groups text[],
    roles text[]
);


ALTER TABLE config.app_config_restriction OWNER TO xplore;

--
-- Name: app_config_restriction_seq; Type: SEQUENCE; Schema: config; Owner: xplore
--

CREATE SEQUENCE config.app_config_restriction_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE config.app_config_restriction_seq OWNER TO xplore;

--
-- Name: app_config_seq; Type: SEQUENCE; Schema: config; Owner: xplore
--

CREATE SEQUENCE config.app_config_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE config.app_config_seq OWNER TO xplore;

SET default_tablespace = '';

--
-- Name: app_config app_config_name_unique; Type: CONSTRAINT; Schema: config; Owner: xplore
--

ALTER TABLE ONLY config.app_config
    ADD CONSTRAINT app_config_name_unique UNIQUE (name);


--
-- Name: app_config app_config_pkey; Type: CONSTRAINT; Schema: config; Owner: xplore
--

ALTER TABLE ONLY config.app_config
    ADD CONSTRAINT app_config_pkey PRIMARY KEY (id);


--
-- Name: app_config_restriction app_config_restrictions_pkey; Type: CONSTRAINT; Schema: config; Owner: xplore
--

ALTER TABLE ONLY config.app_config_restriction
    ADD CONSTRAINT app_config_restrictions_pkey PRIMARY KEY (id);


SET default_tablespace = pg_default;

--
-- Name: fki_fk_app_config_id; Type: INDEX; Schema: config; Owner: xplore; Tablespace: pg_default
--

CREATE INDEX fki_fk_app_config_id ON config.app_config_restriction USING btree (app_config_id);


SET default_tablespace = '';

--
-- Name: app_config_restriction fk_app_config_id; Type: FK CONSTRAINT; Schema: config; Owner: xplore
--

ALTER TABLE ONLY config.app_config_restriction
    ADD CONSTRAINT fk_app_config_id FOREIGN KEY (app_config_id) REFERENCES config.app_config(id);


--
-- Name: SCHEMA config; Type: ACL; Schema: -; Owner: xplore
--

GRANT USAGE ON SCHEMA config TO "xplore-read";


--
-- Name: TABLE app_config; Type: ACL; Schema: config; Owner: xplore
--

GRANT SELECT ON TABLE config.app_config TO "xplore-read";


--
-- Name: TABLE app_config_restriction; Type: ACL; Schema: config; Owner: xplore
--

GRANT SELECT ON TABLE config.app_config_restriction TO "xplore-read";


--
-- Name: SEQUENCE app_config_restriction_seq; Type: ACL; Schema: config; Owner: xplore
--

GRANT SELECT ON SEQUENCE config.app_config_restriction_seq TO "xplore-read";


--
-- Name: SEQUENCE app_config_seq; Type: ACL; Schema: config; Owner: xplore
--

GRANT SELECT ON SEQUENCE config.app_config_seq TO "xplore-read";


--
-- PostgreSQL database dump complete
--

