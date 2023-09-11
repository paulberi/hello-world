CREATE SEQUENCE demo_geovis.kladdlager_id_seq
    INCREMENT 1
    START 429
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE demo_geovis.kladdlager_id_seq
    OWNER TO "xplore-admin";

GRANT SELECT ON SEQUENCE demo_geovis.kladdlager_id_seq TO demo_geovis_read;

GRANT SELECT, USAGE ON SEQUENCE demo_geovis.kladdlager_id_seq TO geovis;

GRANT ALL ON SEQUENCE demo_geovis.kladdlager_id_seq TO "xplore-admin";

CREATE TABLE demo_geovis.kladdlager
(
    id integer NOT NULL DEFAULT nextval('demo_geovis.kladdlager_id_seq'::regclass),
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

ALTER TABLE demo_geovis.kladdlager
    OWNER to "xplore-admin";

GRANT SELECT ON TABLE demo_geovis.kladdlager TO demo_geovis_read;

GRANT ALL ON TABLE demo_geovis.kladdlager TO geovis;

GRANT ALL ON TABLE demo_geovis.kladdlager TO "xplore-admin";



CREATE FUNCTION demo_geovis.trigger_func_skapad_kladdlager()
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

ALTER FUNCTION demo_geovis.trigger_func_skapad_kladdlager()
    OWNER TO xplore;


CREATE TRIGGER set_skapad_on_insert_trigger
    BEFORE INSERT
    ON demo_geovis.kladdlager
    FOR EACH ROW
    EXECUTE PROCEDURE demo_geovis.trigger_func_skapad_kladdlager();