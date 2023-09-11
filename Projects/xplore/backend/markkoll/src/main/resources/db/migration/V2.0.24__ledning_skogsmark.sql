CREATE TABLE elnat.ledning_skogsmark(
    id UUID PRIMARY KEY,
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT,
    ersattning INTEGER NOT NULL DEFAULT 0,
    skapad_av TEXT,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id)
);