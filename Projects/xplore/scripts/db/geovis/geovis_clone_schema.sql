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