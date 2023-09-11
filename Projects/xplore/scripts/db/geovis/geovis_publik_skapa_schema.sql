-- Scriptet skapar ett databasschema för GeoVis Publik.
-- Namnet på alla scheman som används av GeoVis Publik har ändelsen "ext".
-- Ersätt "referensext" i följande SQL-script med det nya schemanamnet exempelvis "luleaext".
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

CREATE SCHEMA referensext AUTHORIZATION geovis;

GRANT USAGE ON SCHEMA referensext TO kundensnamn_read;

GRANT ALL ON SCHEMA referensext TO geovis;

GRANT ALL ON SCHEMA referensext TO "xplore-admin";

-- Gäller för databasen i utvecklingsmiljön, geovisdb.
SET default_tablespace = geovisdb_ts;
