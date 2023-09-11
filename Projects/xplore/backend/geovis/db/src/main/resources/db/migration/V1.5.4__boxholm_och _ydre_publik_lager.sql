-- Funktion för att klona schema. klonar alla tabeller och sekvenser.
CREATE OR REPLACE FUNCTION geovis_clone_schema(source_schema text, dest_schema text) RETURNS void AS
$$

DECLARE
  object text;
  source_table text;
  dest_table text;
  default_ text;
  column_ text;
BEGIN
  FOR object IN
    SELECT sequence_name::text FROM information_schema.SEQUENCES WHERE sequence_schema = source_schema
  LOOP
    EXECUTE 'CREATE SEQUENCE ' || dest_schema || '.' || '"' || object || '"';
  END LOOP;

  FOR object IN
    SELECT TABLE_NAME::text FROM information_schema.TABLES WHERE table_schema = source_schema
  LOOP
    source_table := source_schema || '.' || '"' || object || '"';
    dest_table := dest_schema || '.' || '"' || object || '"';
    EXECUTE 'CREATE TABLE ' || dest_table || ' (LIKE ' || source_table || ' INCLUDING CONSTRAINTS INCLUDING INDEXES INCLUDING DEFAULTS)';

    FOR column_, default_ IN
      SELECT column_name::text, REPLACE(column_default::text, source_schema, dest_schema) FROM information_schema.COLUMNS WHERE table_schema = dest_schema AND TABLE_NAME = object AND column_default LIKE 'nextval(%' || source_schema || '%::regclass)'
    LOOP
      EXECUTE 'ALTER TABLE ' || dest_table || ' ALTER COLUMN ' || column_ || ' SET DEFAULT ' || default_;
    END LOOP;
  END LOOP;

END;

$$ LANGUAGE plpgsql VOLATILE;

DO $$
DECLARE
  -- Byt ut värdet i variabeln dest till den kommun som schema och användare skall skapas för.
  src text := 'aneby';
  dest text := 'boxholm';
  src_schema text := src || 'ext';
  dest_schema text := dest || 'ext';
  dest_user text := dest || '_read';
  geovis_user text := 'geovis';
  geovisdb text := 'geovisdb';
BEGIN
  EXECUTE 'CREATE SCHEMA ' || dest_schema;

  EXECUTE 'CREATE USER IF NOT EXISTS' || dest_user || ' WITH LOGIN NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION';
  EXECUTE 'GRANT CONNECT ON DATABASE ' || geovisdb || ' TO ' || dest_user;
  EXECUTE 'GRANT USAGE ON SCHEMA ' || dest_schema || ' TO ' || dest_user;
  EXECUTE 'GRANT ALL ON SCHEMA ' || dest_schema || ' TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT ALL ON TABLES TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT ON TABLES TO ' || dest_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT, USAGE ON SEQUENCES TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT ON SEQUENCES TO ' || dest_user;

  PERFORM geovis_clone_schema(src_schema, dest_schema);

END $$;


DO $$
DECLARE
  -- Byt ut värdet i variabeln dest till den kommun som schema och användare skall skapas för.
  src text := 'aneby';
  dest text := 'ydre';
  src_schema text := src || 'ext';
  dest_schema text := dest || 'ext';
  dest_user text := dest || '_read';
  geovis_user text := 'geovis';
  geovisdb text := 'geovisdb';
BEGIN
  EXECUTE 'CREATE SCHEMA ' || dest_schema;

  EXECUTE 'CREATE USER IF NOT EXISTS' || dest_user || ' WITH LOGIN NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION';
  EXECUTE 'GRANT CONNECT ON DATABASE ' || geovisdb || ' TO ' || dest_user;
  EXECUTE 'GRANT USAGE ON SCHEMA ' || dest_schema || ' TO ' || dest_user;
  EXECUTE 'GRANT ALL ON SCHEMA ' || dest_schema || ' TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT ALL ON TABLES TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT ON TABLES TO ' || dest_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT, USAGE ON SEQUENCES TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT ON SEQUENCES TO ' || dest_user;

  PERFORM geovis_clone_schema(src_schema, dest_schema);

END $$;

