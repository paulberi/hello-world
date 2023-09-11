CREATE TABLE fastighetsforteckning_anledning
(
    anledning TEXT PRIMARY KEY
);

INSERT INTO fastighetsforteckning_anledning (anledning)
    VALUES
        ('IMPORTVERSION'),
        ('MANUELLT_TILLAGD');

CREATE TABLE fastighetsforteckning
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    projekt_id UUID NOT NULL,
    fastighet_id UUID NOT NULL,
    avtal_id UUID NOT NULL,
    anledning TEXT NOT NULL,
    
    CONSTRAINT fk_projekt FOREIGN KEY (projekt_id) REFERENCES projekt (id),
    CONSTRAINT fk_fastighet FOREIGN KEY (fastighet_id) REFERENCES fastighet (id),
    CONSTRAINT fk_avtal FOREIGN KEY (avtal_id) REFERENCES avtal (id),
    CONSTRAINT fk_anledning FOREIGN KEY (anledning) REFERENCES fastighetsforteckning_anledning (anledning),
    CONSTRAINT uc_avtal UNIQUE (avtal_id)
);

INSERT INTO fastighetsforteckning (projekt_id, fastighet_id, avtal_id, anledning)
    SELECT projekt_id,
           fastighet_id,
           id,
           'IMPORTVERSION'
      FROM avtal;

CREATE TABLE manuell_fastighethandelse_typ(
    typ TEXT PRIMARY KEY
);

INSERT INTO manuell_fastighethandelse_typ
    VALUES
        ('TILLAGD'),
        ('BORTTAGEN');

CREATE TABLE projektlogg_manuell_fastighethandelse(
    id UUID PRIMARY KEY,
    fastighetsbeteckning TEXT NOT NULL,
    typ TEXT NOT NULL,

    CONSTRAINT fk_typ FOREIGN KEY (typ) REFERENCES manuell_fastighethandelse_typ (typ)
);