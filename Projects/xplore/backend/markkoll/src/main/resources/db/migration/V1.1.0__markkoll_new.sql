ALTER SCHEMA finfo RENAME TO finfo_old;
ALTER SCHEMA markkoll RENAME TO markkoll_old;

CREATE SCHEMA finfo;
CREATE SCHEMA markkoll;

ALTER TABLE markkoll_old.flyway_schema_history SET SCHEMA markkoll;

-- Projekt
CREATE TABLE projekttyp
(
    typ TEXT PRIMARY KEY
);

INSERT INTO projekttyp (typ)
    VALUES
        ('FIBER');

CREATE TABLE projektstatus
(
    status TEXT PRIMARY KEY
);

INSERT INTO projektstatus (status)
    VALUES
        ('PAGAENDE');

CREATE TABLE projekt
(
    id UUID PRIMARY KEY,
    namn TEXT NOT NULL,
    ort TEXT,
    projekttyp TEXT NOT NULL,
    organisation TEXT,
    beskrivning TEXT,
    skapad_av TEXT NOT NULL,
    skapad_datum TIMESTAMP NOT NULL,
    status TEXT default 'PAGAENDE'::character varying NOT NULL,
    ledningsagare TEXT,
    start_datum TIMESTAMP,
    bidragsprojekt boolean default false,
    ledningsstracka TEXT,
    bestallare text,
    
    CONSTRAINT fk_projekttyp FOREIGN KEY (projekttyp) REFERENCES projekttyp (typ),
    CONSTRAINT fk_projektstatus FOREIGN KEY (status) REFERENCES projektstatus (status)
);

-- Importversioner
CREATE TABLE importversion
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    filnamn TEXT NOT NULL,
    projekt_id UUID NOT NULL,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_projekt FOREIGN KEY (projekt_id) REFERENCES projekt (id)
);

CREATE TABLE current_version
(
    projekt_id UUID PRIMARY KEY,
    version_id UUID NOT NULL,
    
    CONSTRAINT fk_projekt FOREIGN KEY (projekt_id) REFERENCES projekt (id),
    CONSTRAINT fk_version FOREIGN KEY (version_id) REFERENCES importversion (id)
);

-- Fastigheter
CREATE TABLE detaljtyp
(
    typ TEXT PRIMARY KEY
);

INSERT INTO detaljtyp (typ)
    VALUES
        ('FASTIGHET'),
        ('SAMF'),
        ('FASTO'),
        ('SAMFO'),
        ('OSPEC'),
        ('3DFASTIGH'),
        ('3DSAMF');
        
CREATE TABLE fastighet
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    fastighetsbeteckning TEXT NOT NULL,
    detaljtyp TEXT NOT NULL,
    kommunnamn TEXT,
    trakt TEXT,
    blockenhet TEXT,
    extern_id TEXT NOT NULL,
    fnr_fds TEXT,
    ytkval INTEGER,
    adat TEXT,
    omrtyp TEXT,
    
    CONSTRAINT fk_detaljtyp FOREIGN KEY (detaljtyp) REFERENCES detaljtyp (typ)
);

CREATE TABLE fastighet_omrade
(
    fastighet_id UUID NOT NULL,
    omrade_nr INTEGER NOT NULL,
    geom GEOMETRY(Geometry, 3006) NOT NULL,
    
    CONSTRAINT fk_fastighet FOREIGN KEY (fastighet_id) REFERENCES fastighet (id),
    CONSTRAINT pk_fastighet_omrade PRIMARY KEY (omrade_nr, fastighet_id)
);

CREATE INDEX idx_fastighet_omrade_geom
          ON fastighet_omrade
       USING gist (geom);

CREATE TABLE geometristatus
(
    status TEXT PRIMARY KEY
);

CREATE TABLE samfallighet_fastighet
(
    fastighet_id UUID NOT NULL,
    samfallighet_id UUID NOT NULL,
    fastighetsbeteckning VARCHAR,

    CONSTRAINT fk_samfallighet_fastighet_samfallighet FOREIGN KEY (samfallighet_id)
        REFERENCES fastighet (id) MATCH SIMPLE,
    CONSTRAINT uc_fastighet_samfallighet UNIQUE (fastighet_id, samfallighet_id)
);

-- Avtal
CREATE TABLE avtalsstatus
(
    status TEXT PRIMARY KEY
);

INSERT INTO avtalsstatus (status)
    VALUES
        ('EJ_BEHANDLAT'),
        ('AVTAL_SKICKAT'),
        ('AVTAL_JUSTERAS'),
        ('AVTAL_SIGNERAT'),
        ('AVTALSKONFLIKT');
        
CREATE TABLE avtal
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    fastighet_id UUID NOT NULL,
    projekt_id UUID NOT NULL,
    anteckning TEXT,
    ersattning INTEGER NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_projekt FOREIGN KEY (projekt_id) REFERENCES projekt (id),
    CONSTRAINT fk_fastighet FOREIGN KEY (fastighet_id) REFERENCES fastighet (id),
    CONSTRAINT uc_fastighet_projekt UNIQUE (fastighet_id, projekt_id)
);

INSERT INTO geometristatus
    VALUES
        ('OFORANDRAD'),
        ('NY'),
        ('BORTTAGEN'),
        ('UPPDATERAD');
        
CREATE TABLE avtal_geometristatus
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    version_id UUID NOT NULL,
    avtal_id UUID NOT NULL,
    geometristatus TEXT NOT NULL DEFAULT 'OFORANDRAD',
    
    CONSTRAINT fk_version FOREIGN KEY (version_id) REFERENCES importversion (id),
    CONSTRAINT fk_avtal FOREIGN KEY (avtal_id) REFERENCES avtal (id),
    CONSTRAINT fk_geometristatus FOREIGN KEY (geometristatus) REFERENCES geometristatus (status),
    CONSTRAINT uc_version_avtal UNIQUE (version_id, avtal_id)
);

CREATE INDEX idx_avtal_fastighet_id
          ON avtal
       USING btree (fastighet_id ASC NULLS LAST);

CREATE INDEX idx_avtal_projekt_id
          ON markkoll.avtal
       USING btree (projekt_id ASC NULLS LAST);
        
CREATE TABLE avtalsjobb_status
(
    status TEXT PRIMARY KEY
);

INSERT INTO avtalsjobb_status (status)
    VALUES
        ('NONE'),
        ('IN_PROGRESS'),
        ('DONE'),
        ('ERROR');
        
CREATE TABLE avtalsjobb
(
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	status TEXT NOT NULL DEFAULT 'NONE',
	path TEXT,
	projekt_id UUID NOT NULL,
	total INTEGER NOT NULL,
	generated INTEGER NOT NULL,
    
    CONSTRAINT fk_status FOREIGN KEY (status) REFERENCES avtalsjobb_status (status),
    CONSTRAINT fk_projekt FOREIGN KEY (projekt_id) REFERENCES projekt (id)
);

-- Markägare
CREATE TABLE person
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    adress TEXT,
    postnummer TEXT,
    postort TEXT,
    telefon TEXT,
    bankkonto TEXT,
    e_post TEXT,
    namn TEXT NOT NULL,
    personnummer TEXT
);

CREATE TABLE agartyp
(
    typ TEXT PRIMARY KEY
);

INSERT INTO agartyp
    VALUES
        ('LF'),
        ('TR'),
        ('OKAND'),
        ('OMBUD');
        
CREATE TABLE markagare
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    person_id UUID NOT NULL,
    fastighet_id UUID NOT NULL,
    agartyp TEXT NOT NULL DEFAULT 'LF',
    andel TEXT NOT NULL,
    
    CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES person (id),
    CONSTRAINT fk_fastighet FOREIGN KEY (fastighet_id) REFERENCES fastighet (id),
    CONSTRAINT fk_agartyp FOREIGN KEY (agartyp) REFERENCES agartyp (typ),
    CONSTRAINT uc_person_fastighet UNIQUE (person_id, fastighet_id)
);

CREATE TABLE avtalspart
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    avtal_id UUID NOT NULL,
    signatar BOOLEAN NOT NULL DEFAULT false,
    markagare_id UUID NOT NULL,
    avtalsstatus TEXT NOT NULL DEFAULT 'EJ_BEHANDLAT',
    inkludera_i_avtal BOOLEAN NOT NULL DEFAULT true,

    CONSTRAINT fk_avtal FOREIGN KEY (avtal_id) REFERENCES avtal (id),
    CONSTRAINT fk_markagare FOREIGN KEY (markagare_id) REFERENCES markagare (id),
    CONSTRAINT fk_avtalsstatus FOREIGN KEY (avtalsstatus) REFERENCES avtalsstatus (status),
    CONSTRAINT uc_avtal_markagare UNIQUE (avtal_id, markagare_id)
);

CREATE TABLE huvudagare
(
    avtal_id UUID PRIMARY KEY,
    avtalspart_id UUID NOT NULL,
    
    CONSTRAINT fk_avtal FOREIGN KEY (avtal_id) REFERENCES avtal (id),
    CONSTRAINT fk_avtalspart FOREIGN KEY (avtalspart_id) REFERENCES avtalspart (id)
);

-- Intrång
CREATE TABLE intrang_types
(
    name TEXT PRIMARY KEY
);

INSERT INTO intrang_types
    VALUES
        ('BRUNN'),
        ('MARKSKAP'),
        ('STRAK');

CREATE TABLE intrang_subtypes
(
    name TEXT PRIMARY KEY
);

INSERT INTO intrang_subtypes
    VALUES
        ('NONE'),
        ('LUFTSTRAK'),
        ('MARKSTRAK');

CREATE TABLE intrang
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    geom GEOMETRY(Geometry, 3006) NOT NULL,
    type TEXT NOT NULL,
    subtype TEXT NOT NULL,
    version_id UUID NOT NULL,
    
    CONSTRAINT fk_version FOREIGN KEY (version_id) REFERENCES importversion (id),
    CONSTRAINT fk_intrangtyp FOREIGN KEY (type) REFERENCES intrang_types (name),
    CONSTRAINT fk_intrangsubtyp FOREIGN KEY (subtype) REFERENCES intrang_subtypes (name)
);

CREATE INDEX idx_intrang_geom
          ON intrang
       USING gist (geom);
    
CREATE TABLE fastighetsintrang
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    fastighet_id UUID NOT NULL,
    version_id UUID NOT NULL,
    
    geom GEOMETRY(Geometry, 3006) NOT NULL,
    type TEXT NOT NULL,
    subtype TEXT NOT NULL,
    
    CONSTRAINT fk_fastighet FOREIGN KEY (fastighet_id) REFERENCES fastighet (id),
    CONSTRAINT fk_version FOREIGN KEY (version_id) REFERENCES importversion (id),
    CONSTRAINT fk_intrangtyp FOREIGN KEY (type) REFERENCES intrang_types (name),
    CONSTRAINT fk_intrangsubtyp FOREIGN KEY (subtype) REFERENCES intrang_subtypes (name)
);

CREATE INDEX idx_fastighetsintrang_geom
          ON fastighetsintrang
       USING gist (geom);
          
CREATE TABLE omradesintrang
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    fastighet_id UUID NOT NULL,
    omrade_nr Integer NOT NULL,
    version_id UUID NOT NULL,
    
    geom GEOMETRY(Geometry, 3006) NOT NULL,
    type TEXT NOT NULL,
    subtype TEXT NOT NULL,
    
    CONSTRAINT fk_fastighet_omrnr FOREIGN KEY (fastighet_id, omrade_nr) REFERENCES fastighet_omrade (fastighet_id, omrade_nr),
    CONSTRAINT fk_version FOREIGN KEY (version_id) REFERENCES importversion (id),
    CONSTRAINT fk_intrangtyp FOREIGN KEY (type) REFERENCES intrang_types (name),
    CONSTRAINT fk_intrangsubtyp FOREIGN KEY (subtype) REFERENCES intrang_subtypes (name)
);

CREATE INDEX idx_omradesintrang_geom
          ON omradesintrang
       USING gist (geom);
          
-- finfo
CREATE TABLE IF NOT EXISTS finfo.agare
(
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	andel TEXT NOT NULL,
	adress TEXT,
	co TEXT NOT NULL,
	personnummer TEXT NOT NULL,
	postnummer TEXT NOT NULL,
	postort TEXT NOT NULL,
	description TEXT,
	fastighet_id UUID NOT NULL,
	namn TEXT NOT NULL,
	typ TEXT NOT NULL DEFAULT 'LF',
    importerad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);