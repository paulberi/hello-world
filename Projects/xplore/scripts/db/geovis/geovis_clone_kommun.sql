DO $$ 
DECLARE
  -- Byt ut värdet i variabeln dest till den kommun som schema och användare skall skapas för.
  src text := 'aneby';
  dest text := 'tranas';
  src_schema text := src || 'ext'; 
  dest_schema text := dest || 'ext'; 
  dest_user text := dest || '_read';   
  geovis_user text := 'geovis';
  geovisdb text := 'geovisdb'; 
BEGIN
  EXECUTE 'CREATE SCHEMA ' || dest_schema;
  
  EXECUTE 'CREATE USER ' || dest_user || ' WITH LOGIN NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION';
  EXECUTE 'GRANT CONNECT ON DATABASE ' || geovisdb || ' TO ' || dest_user;
  EXECUTE 'GRANT USAGE ON SCHEMA ' || dest_schema || ' TO ' || dest_user;
  EXECUTE 'GRANT ALL ON SCHEMA ' || dest_schema || ' TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT ALL ON TABLES TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT ON TABLES TO ' || dest_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT, USAGE ON SEQUENCES TO ' || geovis_user;
  EXECUTE 'ALTER DEFAULT PRIVILEGES IN SCHEMA ' || dest_schema || ' GRANT SELECT ON SEQUENCES TO ' || dest_user;
  
  PERFORM geovis_clone_schema(src_schema, dest_schema);
  
END $$;