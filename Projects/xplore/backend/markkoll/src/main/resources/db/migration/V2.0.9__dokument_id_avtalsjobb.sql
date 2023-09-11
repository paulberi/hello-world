ALTER TABLE avtalsjobb
    ADD COLUMN dokument_id UUID,
    ADD CONSTRAINT fk_dokument FOREIGN KEY (dokument_id) REFERENCES dokument (id);
