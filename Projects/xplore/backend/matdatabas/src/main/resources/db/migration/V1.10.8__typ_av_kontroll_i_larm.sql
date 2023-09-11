ALTER TABLE larm
    ADD COLUMN typ_av_kontroll smallint;

UPDATE larm
    SET typ_av_kontroll = gransvarde.typ_av_kontroll
    FROM gransvarde
    WHERE larm.gransvarde_id = gransvarde.id;

ALTER TABLE larm
    ALTER COLUMN typ_av_kontroll set NOT NULL;