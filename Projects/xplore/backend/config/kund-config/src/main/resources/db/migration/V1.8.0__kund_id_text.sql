/* drop constraint */
ALTER TABLE permissions
DROP CONSTRAINT fk_kund;

ALTER TABLE credentials
DROP CONSTRAINT fk_kund;

ALTER TABLE abonnemang
DROP CONSTRAINT fk_kund;

ALTER TABLE configuration
DROP CONSTRAINT fk_kund;

/* alter column */
ALTER TABLE kund
ALTER COLUMN id TYPE TEXT;

ALTER TABLE permissions
ALTER COLUMN kund_id TYPE TEXT;

ALTER TABLE credentials
ALTER COLUMN kund_id TYPE TEXT;

ALTER TABLE abonnemang
ALTER COLUMN kund_id TYPE TEXT;

ALTER TABLE configuration
ALTER COLUMN kund_id TYPE TEXT;

/* add constraint */
ALTER TABLE permissions
ADD CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id);

ALTER TABLE credentials
ADD CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id);

ALTER TABLE abonnemang
ADD CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id);

ALTER TABLE configuration
ADD CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id);