-- Migrering release 1.4.2. Lägg till logghändelser för avtalsparter som inte har någon
-- sedan tidigare, för att det ska visas påminnelser efter en vecka.
INSERT INTO log_avtalsstatus (avtalspart_id, avtalsstatus, skapad_av)
SELECT avtalspart.id, avtalspart.avtalsstatus, 'Markkoll' FROM avtalspart
LEFT OUTER JOIN log_avtalsstatus ON log_avtalsstatus.avtalspart_id = avtalspart.id
WHERE log_avtalsstatus.id IS null;
