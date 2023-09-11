CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Remove contraints and views that will block the migration.
ALTER TABLE analys_bifogad_rapport DROP CONSTRAINT fk_analys_bifogad_rapport_bifogad_fil;
ALTER TABLE handelse_bifogad_bild DROP CONSTRAINT fk_handelse_bifogad_bild_bifogad_fil;
ALTER TABLE matobjekt_bifogat_dokument DROP CONSTRAINT fk_matobjekt_dokument_bifogad_fil;
ALTER TABLE matobjekt DROP CONSTRAINT fk_matobjekt_bifogad_fil;
DROP VIEW matrunda_matningar;

-- Migrate table to uuid.
ALTER TABLE bifogad_fil ALTER COLUMN id DROP DEFAULT;

ALTER TABLE bifogad_fil
    ALTER COLUMN id TYPE uuid USING uuid_generate_v5(uuid_nil(), id::text);

ALTER TABLE bifogad_fil ALTER COLUMN id SET DEFAULT uuid_generate_v4();

-- Migrate all tables referencing this table.
ALTER TABLE analys_bifogad_rapport
    ALTER COLUMN bifogad_fil_id TYPE uuid USING uuid_generate_v5(uuid_nil(), bifogad_fil_id::text);

ALTER TABLE handelse_bifogad_bild
    ALTER COLUMN bifogad_fil_id TYPE uuid USING uuid_generate_v5(uuid_nil(), bifogad_fil_id::text);

ALTER TABLE matobjekt_bifogat_dokument
    ALTER COLUMN bifogad_fil_id TYPE uuid USING uuid_generate_v5(uuid_nil(), bifogad_fil_id::text);

ALTER TABLE matobjekt
    ALTER COLUMN bifogad_bild_id TYPE uuid USING uuid_generate_v5(uuid_nil(), bifogad_bild_id::text);

-- Reinstate constraints and views.
ALTER TABLE analys_bifogad_rapport
    ADD CONSTRAINT fk_matobjekt_bifogad_fil FOREIGN KEY (bifogad_fil_id)
        REFERENCES bifogad_fil (id);

ALTER TABLE handelse_bifogad_bild
    ADD CONSTRAINT fk_matobjekt_bifogad_fil FOREIGN KEY (bifogad_fil_id)
        REFERENCES bifogad_fil (id);

ALTER TABLE matobjekt_bifogat_dokument
    ADD CONSTRAINT fk_matobjekt_bifogad_fil FOREIGN KEY (bifogad_fil_id)
        REFERENCES bifogad_fil (id);

ALTER TABLE matobjekt
    ADD CONSTRAINT fk_matobjekt_bifogad_fil FOREIGN KEY (bifogad_bild_id)
        REFERENCES bifogad_fil (id);

CREATE OR REPLACE VIEW matrunda_matningar AS
SELECT mrmt.matrunda_id,
       mrmt.matningstyp_id,
       mrmt.ordning,
       m.avlast_varde,
       m.avlast_datum,
       m.status,
       m.felkod,
       m.rank,
       dm.storhet,
       dm.namn as matningstyp,
       mt.enhet,
       mt.fixpunkt,
       mt.max_pejlbart_djup,
       mo.id as matobjekt_id,
       mo.namn as matobjekt,
       mo.bifogad_bild_id,
       mo.lage,
       mo.fastighet
FROM matrunda_matningstyp mrmt
         LEFT JOIN (
    SELECT id,
           matningstyp_id,
           avlast_varde,
           avlast_datum,
           status,
           felkod,
           rank() OVER (partition by matningstyp_id ORDER BY avlast_datum DESC)
    FROM matning
) m ON mrmt.matningstyp_id = m.matningstyp_id
         INNER JOIN
     matningstyp mt ON mrmt.matningstyp_id = mt.id
         INNER JOIN
     definition_matningstyp dm ON mt.definition_matningstyp_id = dm.id
         INNER JOIN
     matobjekt mo ON mt.matobjekt_id = mo.id
order by ordning, m.rank;
