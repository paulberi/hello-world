
--
-- Name: markhandlaggning; Type: SCHEMA; Schema: -; Owner: markhandlaggning
--

-- CREATE SCHEMA IF NOT EXISTS markhandlaggning;

SET client_encoding = 'UTF8';
SET default_tablespace = '';
SET default_with_oids = false;



--
-- Name: avtal; Type: TABLE; Schema: markhandlaggning; Owner: markhandlaggning
--

CREATE TABLE avtal(
    id uuid NOT NULL,
    mime_typ character varying(255),
    filnamn character varying(255),
    skapad_datum timestamp without time zone,
    fil bytea,
    job_id bigint,
    uppdaterad_datum timestamp without time zone,
    CONSTRAINT avtal_pkey PRIMARY KEY (id)
);

--
-- Name: bifogad_fil; Type: TABLE; Schema: markhandlaggning; Owner: markhandlaggning
--

CREATE TABLE bifogad_fil(
    id uuid NOT NULL,
    mime_typ character varying(200),
    filnamn character varying(200),
    skapad_datum timestamp without time zone,
    fil bytea,
    job_id bigint,
    CONSTRAINT bifogad_fil_pkey PRIMARY KEY (id)
);

--
-- Name: projekt; Type: TABLE; Schema: markhandlaggning; Owner: markhandlaggning
--

CREATE TABLE projekt(
    id uuid NOT NULL,
    projektnr character varying(200),
    status character varying(200),
    shape_id uuid,
    markagarlista_id uuid,
    avtals_id uuid,
    skapad_datum timestamp without time zone,
    uppdaterad_datum timestamp without time zone,
    felmeddelande character varying(255),
    avtalstyp character varying(255),
    CONSTRAINT projekt_pkey PRIMARY KEY (id)
);