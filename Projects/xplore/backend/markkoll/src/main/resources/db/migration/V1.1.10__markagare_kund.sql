ALTER TABLE person
ADD COLUMN kund_id TEXT;

ALTER TABLE markagare
ADD COLUMN kund_id TEXT;

ALTER TABLE projekt
RENAME COLUMN kund TO kund_id;