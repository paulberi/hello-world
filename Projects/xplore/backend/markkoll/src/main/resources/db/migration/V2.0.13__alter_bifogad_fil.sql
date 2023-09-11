ALTER TABLE IF EXISTS markkoll.bifogad_fil
    RENAME TO fil;

ALTER TABLE IF EXISTS markkoll.fil
    ADD COLUMN IF NOT EXISTS skapad_av text NOT NULL DEFAULT '';

ALTER TABLE IF EXISTS markkoll.fil
    ADD COLUMN IF NOT EXISTS kund_id text NOT NULL DEFAULT '';

UPDATE
    markkoll.fil f
SET
    skapad_av = d.skapad_av,
    kund_id = d.kund_id
FROM
    markkoll.dokument d
WHERE
    f.id = d.fil_id;
