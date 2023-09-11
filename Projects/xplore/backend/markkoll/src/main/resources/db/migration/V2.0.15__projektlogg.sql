CREATE TABLE projektlogg(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),

    projekt_id UUID NOT NULL,
    skapad_av TEXT NOT NULL,
    skapad_datum TIMESTAMP NOT NULL,

    CONSTRAINT fk_projekt FOREIGN KEY (projekt_id) REFERENCES projekt (id)
);

CREATE TABLE projektlogg_avtalhandelse(
    id UUID PRIMARY KEY,
    avtalsjobb_id UUID NOT NULL,
    antal_fastigheter INTEGER,
    
    CONSTRAINT fk_avtalsjobb FOREIGN KEY (avtalsjobb_id) REFERENCES avtalsjobb (id)
);

CREATE TABLE projektlogg_infobrevhandelse(
    id UUID PRIMARY KEY,
    infobrevsjobb_id UUID NOT NULL,
    antal_fastigheter INTEGER,
    
    CONSTRAINT fk_infobrevsjobb FOREIGN KEY (infobrevsjobb_id) REFERENCES infobrevsjobb (id)
);

CREATE TABLE projekthandelsetyp(
    typ TEXT PRIMARY KEY
);

INSERT INTO projekthandelsetyp
    VALUES
        ('HAMTA_MARKAGARE'),
        ('PROJEKTINFORMATION_REDIGERAD'),
        ('VERSION_BORTTAGEN'),
        ('VERSION_ATERSTALLD'),
        ('VERSION_IMPORTERAD');

CREATE TABLE projektlogg_projekthandelse(
    Id UUID PRIMARY KEY,
    projekthandelsetyp TEXT NOT NULL,

    CONSTRAINT fk_projekthandelsetyp FOREIGN KEY (projekthandelsetyp) REFERENCES projekthandelsetyp (typ)
);