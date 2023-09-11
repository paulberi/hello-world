-- Scriptet skapar ett databasschema för GeoVis Vy och Pro.
-- Namnet på alla scheman som används av GeoVis Vy och Pro har ingen ändelse.
-- Ersätt schemanamnet "referens" i följande SQL-script med det nya schemanamnet exempelvis "lulea".
-- Använd inga åäö, blanktecken och bindstreck i namnen.
-- Ersätt "kundensnamn_read" med det aktuella namnet exempelvis "lulea_read".
-- Kör scriptet i "geovisdb" som är databasen för GeoVis i utvecklingsmiljön.


CREATE USER kundensnamn_read WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;

CREATE SCHEMA referens AUTHORIZATION geovis;

GRANT USAGE ON SCHEMA referens TO kundensnamn_read;

GRANT ALL ON SCHEMA referens TO geovis;

GRANT ALL ON SCHEMA referens TO "xplore-admin";

-- Gäller för databasen i utvecklingsmiljön, geovisdb.
SET default_tablespace = geovisdb_ts;

