CREATE TABLE bakgrundskarta
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    namn TEXT NOT NULL,
    bakgrundskarta jsonb NOT NULL
);

ALTER TABLE kund
ADD COLUMN  bakgrundskarta_id UUID;

ALTER TABLE kund
    ADD CONSTRAINT fk_bakgrundskarta
    FOREIGN KEY (bakgrundskarta_id)
    REFERENCES bakgrundskarta (id);


