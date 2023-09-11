ALTER TABLE elnat.varderingsprotokoll
ADD rotnetto INTEGER DEFAULT 0;

UPDATE elnat.varderingsprotokoll
SET rotnetto = elnat.rotnetto.ersattning
FROM elnat.varderingsprotokoll vp
JOIN elnat.rotnetto
  ON vp.id = elnat.rotnetto.varderingsprotokoll_id;

DROP TABLE elnat.rotnetto;

DROP TABLE elnat.ovrigt_intrang;

CREATE TABLE elnat.ovrigt_intrang(
    id UUID PRIMARY KEY,
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT DEFAULT '',
    ersattning INTEGER DEFAULT 0,
    skapad_av TEXT,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id)
);