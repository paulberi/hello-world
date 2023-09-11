-- User: "skargardsstiftelsen-read"
-- DROP USER "skargardsstiftelsen-read";

CREATE USER "skargardsstiftelsen-read" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;

-- SCHEMA: skargardsstiftelsen
-- DROP SCHEMA skargardsstiftelsen ;

CREATE SCHEMA skargardsstiftelsen AUTHORIZATION geovis;
GRANT USAGE ON SCHEMA skargardsstiftelsen TO skargardsstiftelsen_read;
GRANT ALL ON SCHEMA skargardsstiftelsen TO geovis;

ALTER DEFAULT PRIVILEGES IN SCHEMA skargardsstiftelsen
GRANT ALL ON TABLES TO geovis;

ALTER DEFAULT PRIVILEGES IN SCHEMA skargardsstiftelsen
GRANT SELECT ON TABLES TO skargardsstiftelsen_read;

ALTER DEFAULT PRIVILEGES IN SCHEMA skargardsstiftelsen
GRANT SELECT, USAGE ON SEQUENCES TO geovis;

ALTER DEFAULT PRIVILEGES IN SCHEMA skargardsstiftelsen
GRANT SELECT ON SEQUENCES TO skargardsstiftelsen_read;

-- SEQUENCE: skargardsstiftelsen.f_region_gid_seq
-- DROP SEQUENCE skargardsstiftelsen.f_region_gid_seq;

CREATE SEQUENCE skargardsstiftelsen.f_region_gid_seq
    INCREMENT 1
    START 21
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE skargardsstiftelsen.f_region_gid_seq OWNER TO "xplore-admin";

GRANT SELECT ON SEQUENCE skargardsstiftelsen.f_region_gid_seq TO skargardsstiftelsen_read;
GRANT ALL ON SEQUENCE skargardsstiftelsen.f_region_gid_seq TO geovis;
GRANT ALL ON SEQUENCE skargardsstiftelsen.f_region_gid_seq TO "xplore-admin";

-- SEQUENCE: skargardsstiftelsen.t_region_gid_seq
-- DROP SEQUENCE skargardsstiftelsen.t_region_gid_seq;
CREATE SEQUENCE skargardsstiftelsen.t_region_gid_seq
    INCREMENT 1
    START 421
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE skargardsstiftelsen.t_region_gid_seq OWNER TO "xplore-admin";

GRANT SELECT ON SEQUENCE skargardsstiftelsen.t_region_gid_seq TO skargardsstiftelsen_read;
GRANT ALL ON SEQUENCE skargardsstiftelsen.t_region_gid_seq TO geovis;
GRANT ALL ON SEQUENCE skargardsstiftelsen.t_region_gid_seq TO "xplore-admin";

-- Table: skargardsstiftelsen.f_region
-- DROP TABLE skargardsstiftelsen.f_region;

CREATE TABLE skargardsstiftelsen.f_region
(
    id integer NOT NULL DEFAULT nextval('skargardsstiftelsen.f_region_gid_seq'::regclass),
    fnr_fds character varying(9) COLLATE pg_catalog."default",
    externid character varying(64) COLLATE pg_catalog."default",
    detaljtyp character varying(10) COLLATE pg_catalog."default",
    kommunkod character varying(4) COLLATE pg_catalog."default",
    kommunnamn character varying(16) COLLATE pg_catalog."default",
    trakt character varying(40) COLLATE pg_catalog."default",
    blockenhet character varying(9) COLLATE pg_catalog."default",
    omrnr integer,
    fastighet character varying(54) COLLATE pg_catalog."default",
    ytkval integer,
    fdsmatch integer,
    adat character varying(16) COLLATE pg_catalog."default",
    objectid double precision,
    shape_area numeric,
    shape_len numeric,
    egen boolean,
    geom geometry(MultiPolygonZM,3009),
    CONSTRAINT f_region_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE geovisdb_ts
)
WITH (
    OIDS = FALSE
)
TABLESPACE geovisdb_ts;

ALTER TABLE skargardsstiftelsen.f_region OWNER to "xplore-admin";
GRANT ALL ON TABLE skargardsstiftelsen.f_region TO geovis;
GRANT SELECT ON TABLE skargardsstiftelsen.f_region TO "skargardsstiftelsen-read";
GRANT ALL ON TABLE skargardsstiftelsen.f_region TO "xplore-admin";

-- Table: skargardsstiftelsen.t_region
-- DROP TABLE skargardsstiftelsen.t_region;

CREATE TABLE skargardsstiftelsen.t_region
(
    id integer NOT NULL DEFAULT nextval('skargardsstiftelsen.t_region_gid_seq'::regclass),
    fnr_fds character varying(9) COLLATE pg_catalog."default",
    externid character varying(64) COLLATE pg_catalog."default",
    detaljtyp character varying(10) COLLATE pg_catalog."default",
    kommunkod character varying(4) COLLATE pg_catalog."default",
    kommunnamn character varying(16) COLLATE pg_catalog."default",
    trakt character varying(40) COLLATE pg_catalog."default",
    blockenhet character varying(9) COLLATE pg_catalog."default",
    omrnr integer,
    fastighet character varying(54) COLLATE pg_catalog."default",
    ytkval integer,
    fdsmatch integer,
    adat character varying(16) COLLATE pg_catalog."default",
    objectid double precision,
    shape_area numeric,
    shape_len numeric,
    egen boolean,
    geom geometry(MultiPolygonZM,3009),
    CONSTRAINT t_region_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE geovisdb_ts
)
WITH (
    OIDS = FALSE
)
TABLESPACE geovisdb_ts;

ALTER TABLE skargardsstiftelsen.t_region OWNER to "xplore-admin";

GRANT ALL ON TABLE skargardsstiftelsen.t_region TO geovis;
GRANT SELECT ON TABLE skargardsstiftelsen.t_region TO "skargardsstiftelsen-read";
GRANT ALL ON TABLE skargardsstiftelsen.t_region TO "xplore-admin";


-- SEQUENCE: skargardsstiftelsen.kladdlager_id_seq
-- DROP SEQUENCE skargardsstiftelsen.kladdlager_id_seq;

CREATE SEQUENCE skargardsstiftelsen.kladdlager_id_seq
    INCREMENT 1
    START 847
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE skargardsstiftelsen.kladdlager_id_seq
    OWNER TO "xplore-admin";

GRANT SELECT ON SEQUENCE skargardsstiftelsen.kladdlager_id_seq TO skargardsstiftelsen_read;
GRANT SELECT, USAGE ON SEQUENCE skargardsstiftelsen.kladdlager_id_seq TO geovis;
GRANT ALL ON SEQUENCE skargardsstiftelsen.kladdlager_id_seq TO "xplore-admin";


-- FUNCTION: skargardsstiftelsen.trigger_func_skapad_kladdlager()
-- DROP FUNCTION skargardsstiftelsen.trigger_func_skapad_kladdlager();

CREATE FUNCTION skargardsstiftelsen.trigger_func_skapad_kladdlager()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
  NEW.skapad = NOW();
  RETURN NEW;
END;
$BODY$;

ALTER FUNCTION skargardsstiftelsen.trigger_func_skapad_kladdlager()
    OWNER TO xplore;


-- Table: skargardsstiftelsen.kladdlager
-- DROP TABLE skargardsstiftelsen.kladdlager;

CREATE TABLE skargardsstiftelsen.kladdlager
(
    id integer NOT NULL DEFAULT nextval('skargardsstiftelsen.kladdlager_id_seq'::regclass),
    skapad timestamp with time zone NOT NULL,
    geometry geometry NOT NULL,
    skapad_av character varying(40) COLLATE pg_catalog."default",
    stil json,
    CONSTRAINT kladdlager_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE geovisdb_ts
)
WITH (
    OIDS = FALSE
)
TABLESPACE geovisdb_ts;

ALTER TABLE skargardsstiftelsen.kladdlager
    OWNER to "xplore-admin";

GRANT ALL ON TABLE skargardsstiftelsen.kladdlager TO geovis;
GRANT SELECT ON TABLE skargardsstiftelsen.kladdlager TO skargardsstiftelsen_read;
GRANT ALL ON TABLE skargardsstiftelsen.kladdlager TO "xplore-admin";


-- SEQUENCE: skargardsstiftelsen.standard_sgs_punkt_id_seq1
-- DROP SEQUENCE skargardsstiftelsen.standard_sgs_punkt_id_seq1;

CREATE SEQUENCE skargardsstiftelsen.standard_sgs_punkt_id_seq1
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE skargardsstiftelsen.standard_sgs_punkt_id_seq1
    OWNER TO "xplore-admin";

GRANT SELECT ON SEQUENCE skargardsstiftelsen.standard_sgs_punkt_id_seq1 TO demo_geovis_read;
GRANT SELECT, USAGE ON SEQUENCE skargardsstiftelsen.standard_sgs_punkt_id_seq1 TO geovis;
GRANT ALL ON SEQUENCE skargardsstiftelsen.standard_sgs_punkt_id_seq1 TO "xplore-admin";

-- Table: skargardsstiftelsen.karta_egna_byggnader
-- DROP TABLE skargardsstiftelsen.karta_egna_byggnader;

CREATE TABLE skargardsstiftelsen.karta_egna_byggnader
(
    id integer NOT NULL DEFAULT nextval('skargardsstiftelsen.standard_sgs_punkt_id_seq1'::regclass),
    geom geometry(MultiPoint,3009),
    namn character varying(80) COLLATE pg_catalog."default",
    beskr character varying(254) COLLATE pg_catalog."default",
    link character varying(254) COLLATE pg_catalog."default",
    adress character varying(254) COLLATE pg_catalog."default",
    kontakt character varying(254) COLLATE pg_catalog."default",
    CONSTRAINT karta_polis_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE geovisdb_ts
)
WITH (
    OIDS = FALSE
)
TABLESPACE geovisdb_ts;

ALTER TABLE skargardsstiftelsen.karta_egna_byggnader
    OWNER to "xplore-admin";

GRANT ALL ON TABLE skargardsstiftelsen.karta_egna_byggnader TO geovis;
GRANT SELECT ON TABLE skargardsstiftelsen.karta_egna_byggnader TO skargardsstiftelsen_read;
GRANT ALL ON TABLE skargardsstiftelsen.karta_egna_byggnader TO "xplore-admin";



-- Index: f_region_geom_idx
-- Indexes are special lookup tables that the database search engine can
-- use to speed up data retrieval. Simply put, an index is a pointer to
-- data in a table. An index in a database is very similar to an index in
-- the back of a book.
-- DROP INDEX skargardsstiftelsen.f_region_geom_idx;

CREATE INDEX f_region_geom_idx
    ON skargardsstiftelsen.f_region USING gist
    (geom)
    TABLESPACE geovisdb_ts;


-- Index: t_region_geom_idx
-- Indexes are special lookup tables that the database search engine can
-- use to speed up data retrieval. Simply put, an index is a pointer to
-- data in a table. An index in a database is very similar to an index in
-- the back of a book.
-- DROP INDEX skargardsstiftelsen.t_region_geom_idx;

CREATE INDEX t_region_geom_idx
    ON skargardsstiftelsen.t_region USING gist
    (geom)
    TABLESPACE geovisdb_ts;
