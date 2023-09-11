ALTER TABLE elnat.markledning
    ADD COLUMN skapad_av TEXT,
    ADD COLUMN skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE elnat.punktersattning
    ADD COLUMN skapad_av TEXT,
    ADD COLUMN skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE elnat.ssb_skogsmark
    ADD COLUMN skapad_av TEXT,
    ADD COLUMN skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE elnat.ssb_vaganlaggning
    ADD COLUMN skapad_av TEXT,
    ADD COLUMN skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;