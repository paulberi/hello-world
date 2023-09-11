ALTER TABLE markkoll.avtalsjobb
    DROP CONSTRAINT fk_dokument,
    DROP COLUMN dokument_id;

ALTER TABLE markkoll.infobrevsjobb
    DROP CONSTRAINT fk_dokument,
    DROP COLUMN dokument_id;