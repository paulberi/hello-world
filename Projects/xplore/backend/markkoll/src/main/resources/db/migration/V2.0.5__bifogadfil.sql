CREATE TABLE bifogad_fil (
     id uuid NOT NULL PRIMARY KEY,
     mime_typ character varying(130) NOT NULL,
     filnamn character varying(255) NOT NULL,
     skapad_datum timestamp without time zone,
     fil bytea NOT NULL
);

TRUNCATE TABLE dokument;

ALTER TABLE dokument
    DROP COLUMN IF EXISTS path,
    ADD COLUMN fil_id uuid NOT NULL,
    ADD CONSTRAINT fk_bifogadfil FOREIGN KEY (fil_id) REFERENCES bifogad_fil (id) ON DELETE CASCADE;