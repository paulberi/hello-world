CREATE TABLE elnat.rotnetto(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    ersattning INTEGER NOT NULL DEFAULT 0,
    skapad_av TEXT,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_varderingsprotokoll_id FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll(id)
);

CREATE TABLE elnat.rotnetto_typ(
    typ TEXT PRIMARY KEY
);
INSERT INTO elnat.rotnetto_typ
VALUES
    ('ROTNETTO'),
    ('EGET_TILLVARATAGANDE'),
    ('DECISION_PENDING');

ALTER TABLE avtal
    ADD COLUMN rotnetto_typ text DEFAULT 'DECISION_PENDING',
    ADD CONSTRAINT fk_typ FOREIGN KEY (rotnetto_typ) REFERENCES elnat.rotnetto_typ (typ);
