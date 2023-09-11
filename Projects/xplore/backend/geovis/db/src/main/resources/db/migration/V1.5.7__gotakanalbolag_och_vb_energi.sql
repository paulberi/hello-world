-- User: gotakanalbolag_read
-- DROP USER gotakanalbolag_read;

CREATE USER gotakanalbolag_read WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;

-- User: vb_energi_read
-- DROP USER vb_energi_read;

CREATE USER vb_energi_read WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;


-- SCHEMA: gotakanalbolag
-- DROP SCHEMA gotakanalbolag ;

CREATE SCHEMA gotakanalbolag
    AUTHORIZATION geovis;

GRANT ALL ON SCHEMA gotakanalbolag TO geovis;

GRANT USAGE ON SCHEMA gotakanalbolag TO gotakanalbolag_read;

ALTER DEFAULT PRIVILEGES IN SCHEMA gotakanalbolag
GRANT ALL ON TABLES TO geovis;

ALTER DEFAULT PRIVILEGES IN SCHEMA gotakanalbolag
GRANT SELECT ON TABLES TO gotakanalbolag_read;


-- SEQUENCE: gotakanalbolag.kladdlager_id_seq
-- DROP SEQUENCE gotakanalbolag.kladdlager_id_seq;

CREATE SEQUENCE gotakanalbolag.kladdlager_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE gotakanalbolag.kladdlager_id_seq
    OWNER TO "xplore-admin";

GRANT SELECT ON SEQUENCE gotakanalbolag.kladdlager_id_seq TO gotakanalbolag_read;

GRANT SELECT, USAGE ON SEQUENCE gotakanalbolag.kladdlager_id_seq TO geovis;


-- Table: gotakanalbolag.kladdlager
-- DROP TABLE gotakanalbolag.kladdlager;

CREATE TABLE gotakanalbolag.kladdlager
(
    id integer NOT NULL DEFAULT nextval('gotakanalbolag.kladdlager_id_seq'::regclass),
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

ALTER TABLE gotakanalbolag.kladdlager
    OWNER to "xplore-admin";

GRANT ALL ON TABLE gotakanalbolag.kladdlager TO geovis;

GRANT SELECT ON TABLE gotakanalbolag.kladdlager TO gotakanalbolag_read;

GRANT ALL ON TABLE gotakanalbolag.kladdlager TO "xplore-admin";


-- FUNCTION: gotakanalbolag.trigger_func_skapad_kladdlager()
-- DROP FUNCTION gotakanalbolag.trigger_func_skapad_kladdlager();

CREATE FUNCTION gotakanalbolag.trigger_func_skapad_kladdlager()
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

ALTER FUNCTION gotakanalbolag.trigger_func_skapad_kladdlager()
    OWNER TO xplore;


-- Trigger: set_skapad_on_insert_trigger
-- DROP TRIGGER set_skapad_on_insert_trigger ON gotakanalbolag.kladdlager;

CREATE TRIGGER set_skapad_on_insert_trigger
    BEFORE INSERT
    ON gotakanalbolag.kladdlager
    FOR EACH ROW
    EXECUTE PROCEDURE gotakanalbolag.trigger_func_skapad_kladdlager();


-- SCHEMA: vb_energi
-- DROP SCHEMA vb_energi ;

CREATE SCHEMA vb_energi
    AUTHORIZATION geovis;

GRANT ALL ON SCHEMA vb_energi TO geovis;

GRANT USAGE ON SCHEMA vb_energi TO vb_energi_read;

ALTER DEFAULT PRIVILEGES IN SCHEMA vb_energi
GRANT ALL ON TABLES TO geovis;

ALTER DEFAULT PRIVILEGES IN SCHEMA vb_energi
GRANT SELECT ON TABLES TO vb_energi_read;


-- SEQUENCE: vb_energi.kladdlager_id_seq
-- DROP SEQUENCE vb_energi.kladdlager_id_seq;

CREATE SEQUENCE vb_energi.kladdlager_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE vb_energi.kladdlager_id_seq
    OWNER TO "xplore-admin";

GRANT SELECT ON SEQUENCE vb_energi.kladdlager_id_seq TO vb_energi_read;

GRANT SELECT, USAGE ON SEQUENCE vb_energi.kladdlager_id_seq TO geovis;


-- Table: vb_energi.kladdlager
-- DROP TABLE vb_energi.kladdlager;

CREATE TABLE vb_energi.kladdlager
(
    id integer NOT NULL DEFAULT nextval('vb_energi.kladdlager_id_seq'::regclass),
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

ALTER TABLE vb_energi.kladdlager
    OWNER to "xplore-admin";

GRANT ALL ON TABLE vb_energi.kladdlager TO geovis;

GRANT SELECT ON TABLE vb_energi.kladdlager TO vb_energi_read;

GRANT ALL ON TABLE vb_energi.kladdlager TO "xplore-admin";


-- FUNCTION: vb_energi.trigger_func_skapad_kladdlager()
-- DROP FUNCTION vb_energi.trigger_func_skapad_kladdlager();

CREATE FUNCTION vb_energi.trigger_func_skapad_kladdlager()
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

ALTER FUNCTION vb_energi.trigger_func_skapad_kladdlager()
    OWNER TO xplore;


-- Trigger: set_skapad_on_insert_trigger
-- DROP TRIGGER set_skapad_on_insert_trigger ON vb_energi.kladdlager;

CREATE TRIGGER set_skapad_on_insert_trigger
    BEFORE INSERT
    ON vb_energi.kladdlager
    FOR EACH ROW
    EXECUTE PROCEDURE vb_energi.trigger_func_skapad_kladdlager();

-- Ta bort följande tabel.
DROP TABLE IF EXISTS demo_geovis.gotakanalbolag_kontor CASCADE;
