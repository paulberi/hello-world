CREATE SCHEMA IF NOT EXISTS elnat;

CREATE TABLE elnat.varderingsprotokoll(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    avtal_id UUID NOT NULL,
    
    /* beräkningskonfiguration*/
    lagspanning BOOLEAN NOT NULL DEFAULT false,
    storskogsbruksavtalet BOOLEAN NOT NULL DEFAULT false,
    ingen_grundersattning BOOLEAN NOT NULL DEFAULT false,
    forhojd_minimumersattning BOOLEAN NOT NULL DEFAULT false,
    
    /* metadata */
    ledning TEXT NOT NULL DEFAULT '',
    koncessionslopnr TEXT NOT NULL DEFAULT '',
    varderingstidpunkt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    varderingsman_och_foretag TEXT NOT NULL DEFAULT '',
    
    CONSTRAINT fk_avtal FOREIGN KEY (avtal_id) REFERENCES avtal (id)
);

CREATE TABLE elnat.markledning(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    langd INTEGER NOT NULL DEFAULT 0,
    bredd INTEGER NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_varderingsprotokoll_id FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll(id)
);

CREATE TABLE elnat.punktersattning_typ(
    typ TEXT PRIMARY KEY
);
INSERT INTO elnat.punktersattning_typ
VALUES
    ('NATSTATION'),
    ('KABELSKAP'),
    ('SJOKABELSKYLT');

CREATE TABLE elnat.punktersattning_markslag(
    markslag TEXT PRIMARY KEY
);
INSERT INTO elnat.punktersattning_markslag
VALUES
    ('SKOG'),
    ('JORDBRUKSIMPEDIMENT'),
    ('OVRIG_MARK');

CREATE TABLE elnat.punktersattning_storlek(
    storlek TEXT PRIMARY KEY
);
INSERT INTO elnat.punktersattning_storlek
VALUES
    ('6X6_METER'),
    ('8X8_METER'),
    ('10X10_METER');
    
CREATE TABLE elnat.punktersattning(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    antal INTEGER NOT NULL DEFAULT 0,
    typ TEXT NOT NULL,
    markslag TEXT NOT NULL,
    storlek TEXT NOT NULL,

    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id),
    CONSTRAINT fk_typ FOREIGN KEY (typ) REFERENCES elnat.punktersattning_typ (typ),
    CONSTRAINT fk_markslag FOREIGN KEY (markslag) REFERENCES elnat.punktersattning_markslag (markslag),
    CONSTRAINT fk_storlek FOREIGN KEY (storlek) REFERENCES elnat.punktersattning_storlek (storlek)
);

CREATE TABLE elnat.prisomrade(
    prisomrade TEXT PRIMARY KEY
);
INSERT INTO elnat.prisomrade
VALUES
    ('NORRLANDS_INLAND'),
    ('NORRLANDS_KUSTLAND'),
    ('TILLVÄXTOMRADE_3'),
    ('TILLVAXTOMRADE_4A'),
    ('TILLVAXTOMRADE_4B');

CREATE TABLE elnat.ssb_skogsmark(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    langd INTEGER NOT NULL DEFAULT 0,
    bredd INTEGER NOT NULL DEFAULT 0,
    prisomrade TEXT NOT NULL,

    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id),
    CONSTRAINT fk_prisomrade FOREIGN KEY (prisomrade) REFERENCES elnat.prisomrade (prisomrade)
);

CREATE TABLE elnat.zon(
    zon TEXT PRIMARY KEY
);
INSERT INTO elnat.zon
VALUES
    ('ZON_1'),
    ('ZON_2');
    
CREATE TABLE elnat.ssb_vaganlaggning(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    langd INTEGER NOT NULL DEFAULT 0,
    zon TEXT NOT NULL,

    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id),
    CONSTRAINT fk_zon FOREIGN KEY (zon) REFERENCES elnat.zon (zon)
);

/* 74 års åkernorm */
CREATE TABLE elnat.akernorm_omrade(
    omrade TEXT PRIMARY KEY
);

INSERT INTO elnat.akernorm_omrade
VALUES
    ('NO'),
    ('NN'),
    ('SSK'),
    ('GSK'),
    ('SS'),
    ('GNS'),
    ('GMB'),
    ('GSS');

CREATE TABLE elnat.bilaga_74_ars_akernorm(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    vardetidpunkt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fastighetsnr INTEGER NOT NULL DEFAULT 0,
    kpi INTEGER NOT NULL DEFAULT 0,
    omrade TEXT NOT NULL,

    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id),
    CONSTRAINT fk_omrade FOREIGN KEY (omrade) REFERENCES elnat.akernorm_omrade (omrade)
);

CREATE TABLE elnat.hinder_markslag(
    markslag TEXT PRIMARY KEY
);
INSERT INTO elnat.hinder_markslag
VALUES
    ('AKERMARK'),
    ('BETESMARK'),
    ('IMPEDIMENT');
    
CREATE TABLE elnat.hinder(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    bilaga_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    langd_l1 INTEGER NOT NULL DEFAULT 0,
    langd_l2 INTEGER NOT NULL DEFAULT 0,
    langd_l3 INTEGER NOT NULL DEFAULT 0,
    markslag TEXT NOT NULL,

    CONSTRAINT fk_bilaga FOREIGN KEY (bilaga_id) REFERENCES elnat.bilaga_74_ars_akernorm (id),
    CONSTRAINT fk_markslag FOREIGN KEY (markslag) REFERENCES elnat.hinder_markslag (markslag)
);

/* Enstaka träd */
CREATE TABLE elnat.tillvaxtomrade(
    tillvaxtomrade TEXT PRIMARY KEY
);
INSERT INTO elnat.tillvaxtomrade
VALUES
    ('TILLVAXTOMRADE_1'),
    ('TILLVAXTOMRADE_2'),
    ('TILLVAXTOMRADE_3'),
    ('TILLVAXTOMRADE_4A'),
    ('TILLVAXTOMRADE_4B'),
    ('TILLVAXTOMRADE_5');

CREATE TABLE elnat.bilaga_enstaka_trad(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    tillvaxtomrade TEXT NOT NULL,
    vardetidpunkt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fastighetsnr INTEGER NOT NULL DEFAULT 0,
    nis_ib TEXT NOT NULL DEFAULT '',

    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id),
    CONSTRAINT fk_tillvaxtomrade FOREIGN KEY (tillvaxtomrade) REFERENCES elnat.tillvaxtomrade (tillvaxtomrade)
);

CREATE TABLE elnat.vaganlaggning(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    bilaga_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    langd INTEGER NOT NULL DEFAULT 0,
    area INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT fk_bilaga FOREIGN KEY (bilaga_id) REFERENCES elnat.bilaga_enstaka_trad (id)
);

CREATE TABLE elnat.ovrigt_intrang_skogsmark(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    bilaga_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    langd INTEGER NOT NULL DEFAULT 0,
    bredd INTEGER NOT NULL DEFAULT 0,
    area INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT fk_bilaga FOREIGN KEY (bilaga_id) REFERENCES elnat.bilaga_enstaka_trad (id)
);

CREATE TABLE elnat.tradslag(
    tradslag TEXT PRIMARY KEY
);
INSERT INTO elnat.tradslag
VALUES
    ('TALL'),
    ('GRAN'),
    ('LOV');

CREATE TABLE elnat.enstaka_trad(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    bilaga_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',
    diameter INTEGER NOT NULL DEFAULT 0,
    antal INTEGER NOT NULL DEFAULT 0,
    tradslag TEXT NOT NULL,

    CONSTRAINT fk_tradslag FOREIGN KEY (tradslag) REFERENCES elnat.tradslag (tradslag),
    CONSTRAINT fk_bilaga FOREIGN KEY (bilaga_id) REFERENCES elnat.bilaga_enstaka_trad (id)
);

/* Övrigt intrång*/
CREATE TABLE elnat.bilaga_ovrigt_intrang(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,

    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id)
);

CREATE TABLE elnat.ovrigt_intrang(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    bilaga_ovrigt_intrang_id UUID NOT NULL,
    beskrivning TEXT NOT NULL DEFAULT '',

    CONSTRAINT fk_bilaga FOREIGN KEY (bilaga_ovrigt_intrang_id) REFERENCES elnat.bilaga_ovrigt_intrang (id)
);

/* Haglöfs HMS */
CREATE TABLE elnat.bilaga_haglofs_hms(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,

    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id)
);

CREATE TABLE elnat.haglofs_hms(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    bilaga_id UUID NOT NULL,
    ersattning INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT fk_bilaga FOREIGN KEY (bilaga_id) REFERENCES elnat.bilaga_haglofs_hms (id)
);

/* Skapa upp tomma värderingsprotokoll för existerande avtal */
INSERT INTO elnat.varderingsprotokoll (avtal_id)
     SELECT id
       FROM avtal;
