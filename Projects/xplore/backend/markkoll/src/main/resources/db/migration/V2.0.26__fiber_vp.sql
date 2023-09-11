ALTER TABLE fiber.projekt
    ADD varderingsprotokoll BOOLEAN DEFAULT TRUE;

ALTER TABLE IF EXISTS markkoll.avtal
    ALTER COLUMN spanningsniva DROP NOT NULL;

CREATE TABLE fiber.varderingsprotokoll(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    avtal_id UUID NOT NULL,

    /* metadata */
    varderingstidpunkt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    varderingsman_och_foretag TEXT NOT NULL DEFAULT '',

    /* config */
    sarskild_ersattning DOUBLE PRECISION NOT NULL DEFAULT 25.0,

    CONSTRAINT fk_avtal FOREIGN KEY (avtal_id) REFERENCES avtal (id)
);

CREATE TABLE fiber.markledning(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    langd INTEGER NOT NULL DEFAULT 0,
    bredd INTEGER NOT NULL DEFAULT 1,
    skapad_av TEXT,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_varderingsprotokoll_id FOREIGN KEY (varderingsprotokoll_id) REFERENCES fiber.varderingsprotokoll(id)
);

CREATE TABLE fiber.punktersattning_typ(
    typ TEXT PRIMARY KEY
);
INSERT INTO fiber.punktersattning_typ
    VALUES
        ('MARKSKAP_JORDBRUKSIMPEDIMENT'),
        ('MARKSKAP_OVRIGMARK'),
        ('MARKSKAP_SKOG'),
        ('MARKSKAP_EJ_KLASSIFICERAD'),
        ('OPTOBRUNN_JORDBRUKSIMPEDIMENT'),
        ('OPTOBRUNN_OVRIGMARK'),
        ('OPTOBRUNN_SKOG'),
        ('OPTOBRUNN_EJ_KLASSIFICERAD'),
        ('SITE_JORDBRUKSIMPEDIMENT_6X6M'),
        ('SITE_JORDBRUKSIMPEDIMENT_8X8M'),
        ('SITE_JORDBRUKSIMPEDIMENT_10X10M'),
        ('SITE_OVRIGMARK_6X6M'),
        ('SITE_OVRIGMARK_8X8M'),
        ('SITE_OVRIGMARK_10X10M'),
        ('SITE_SKOG_6X6M'),
        ('SITE_SKOG_8X8M'),
        ('SITE_SKOG_10X10M'),
        ('SITE_EJ_KLASSIFICERAD');

CREATE TABLE fiber.punktersattning(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    antal INTEGER NOT NULL DEFAULT 0,
    typ TEXT NOT NULL,
    skapad_av TEXT,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES fiber.varderingsprotokoll (id),
    CONSTRAINT fk_typ FOREIGN KEY (typ) REFERENCES fiber.punktersattning_typ (typ)
);

CREATE TABLE fiber.aker_och_skogsmark(
                                  id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
                                  varderingsprotokoll_id UUID NOT NULL,
                                  beskrivning TEXT NOT NULL DEFAULT '',
                                  ersattning INTEGER NOT NULL DEFAULT 0,
                                  skapad_av TEXT,
                                  skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_varderingsprotokoll_id FOREIGN KEY (varderingsprotokoll_id) REFERENCES fiber.varderingsprotokoll(id)
);

CREATE TABLE fiber.ovrig_intrangsersattning(
                                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
                                         varderingsprotokoll_id UUID NOT NULL,
                                         beskrivning TEXT NOT NULL DEFAULT '',
                                         ersattning INTEGER NOT NULL DEFAULT 0,
                                         skapad_av TEXT,
                                         skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                         CONSTRAINT fk_varderingsprotokoll_id FOREIGN KEY (varderingsprotokoll_id) REFERENCES fiber.varderingsprotokoll(id)
);

/* Skapa upp tomma värderingsprotokoll för existerande avtal */
INSERT INTO fiber.varderingsprotokoll (avtal_id)
    SELECT id
        FROM avtal;
