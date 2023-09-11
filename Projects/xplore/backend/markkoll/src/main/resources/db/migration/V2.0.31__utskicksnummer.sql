CREATE TABLE utskicksnummer(
    kund_id TEXT PRIMARY KEY,
    batchnummer INTEGER NOT NULL DEFAULT 1
);

INSERT INTO utskicksnummer (kund_id)
    SELECT DISTINCT kund_id
      FROM projekt;
      
ALTER TABLE avtalstrategi
RENAME TO utskicksstrategi;

ALTER TABLE projekt
RENAME COLUMN avtalstrategi TO utskicksstrategi;