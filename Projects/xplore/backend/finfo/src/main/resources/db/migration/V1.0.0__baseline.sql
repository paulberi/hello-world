CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

CREATE TABLE finfo.agare
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    andel TEXT,
    adress TEXT,
    co TEXT NOT NULL,
    personnummer TEXT NOT NULL,
    postnummer TEXT NOT NULL,
    postort TEXT NOT NULL,
    description TEXT,
    fastighet_id UUID NOT NULL,
    namn TEXT NOT NULL,
    typ TEXT DEFAULT 'LF'::TEXT NOT NULL,
    importerad_datum TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE finfo.samfallighetsforening
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    foreningstyp TEXT NOT NULL,
    foreningsnamn TEXT NOT NULL,
    lan TEXT NOT NULL,
    sate TEXT NOT NULL,
    co_adress TEXT,
    adress TEXT,
    postnummer TEXT,
    postort TEXT,
    telefonnummer TEXT,
    orgnr TEXT,
    faststallelsedatum DATE,
    registreringsdatum DATE,
    avregistreringsdatum DATE,
    styrelsedatum DATE,
    rakenskapsar_from_manad INTEGER,
    rakenskapsar_from_dag INTEGER,
    rakenskapsar_tom_manad INTEGER,
    rakenskapsar_tom_dag INTEGER,
    under_ajourforing BOOLEAN,
    senaste_ajourforingsdatum DATE,
    firmatecknare TEXT,
    anmarkning TEXT,
    uuid UUID,
    
    importerad_datum TIMESTAMP NOT NULL
);

CREATE TABLE finfo.forvaltningsobjekt(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    samfallighetsforening_id UUID NOT NULL,

    anmarkning TEXT,
    objektsinformation TEXT,
    andamalstyp TEXT,

    CONSTRAINT fk_samfallighetsforening FOREIGN KEY (samfallighetsforening_id)
        REFERENCES finfo.samfallighetsforening(id)
);

CREATE TABLE finfo.styrelsemedlem(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    samfallighetsforening_id UUID NOT NULL,
    
    namn TEXT NOT NULL,
    co_adress TEXT,
    utdelningsadress TEXT,
    postnummer TEXT,
    postort TEXT,
    land TEXT,
    lansstyrelsediarienummer TEXT,
    datum_for_lansstyrelsebeslut TIMESTAMP,
    medlemstyp TEXT,
    anmarkning TEXT,
    
    CONSTRAINT fk_samfallighetsforening FOREIGN KEY (samfallighetsforening_id)
        REFERENCES finfo.samfallighetsforening(id)
);

CREATE TABLE finfo.styrelsefunktion(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    styrelsemedlem_id UUID NOT NULL,
    
    styrelsefunktion_typ TEXT NOT NULL,
    
    CONSTRAINT fk_styrelsemedlem FOREIGN KEY (styrelsemedlem_id)
        REFERENCES finfo.styrelsemedlem(id)
);

CREATE TABLE finfo.registerenhet_typ(
    typ TEXT PRIMARY KEY
);

INSERT INTO finfo.registerenhet_typ
    VALUES
        ('FASTIGHET'),
        ('SAMFALLIGHET'),
        ('GEMENSAMHETSANLAGGNING');

CREATE TABLE finfo.registerenhet(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    
    andamal TEXT,
    forvaltande_beteckning TEXT,
    importerad_datum TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    transaktion_typ TEXT,
    uuid UUID NOT NULL,
    typ TEXT NOT NULL,
    
    CONSTRAINT fk_typ FOREIGN KEY (typ) REFERENCES finfo.registerenhet_typ (typ)
);

CREATE TABLE finfo.pagaende_fastighetsbildning(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    
    arende_dagboksnummer TEXT,
    arendestatus TEXT,
    registerenhet_id UUID NOT NULL,
    
    CONSTRAINT fk_registerenhet_id FOREIGN KEY (registerenhet_id)
        REFERENCES finfo.registerenhet (id)
);

CREATE TABLE finfo.rattighet(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    
    aktbeteckning TEXT,
    andamal TEXT,
    rattighetstyp TEXT,
    rattsforhallande TEXT,
    rattighetsbeskrivning TEXT,
    rattighetsanmarkning TEXT,
    registerenhet_id UUID NOT NULL,
    
    CONSTRAINT fk_registerenhet_id FOREIGN KEY (registerenhet_id)
        REFERENCES finfo.registerenhet (id)
);

CREATE TABLE atgardstyp(
    typ TEXT PRIMARY KEY
);

INSERT INTO atgardstyp (typ)
    VALUES
        ('FASTIGHETSRATTLIG'),
        ('TEKNISK');

CREATE TABLE berord_av_atgard(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    
    aktbeteckning TEXT,
    registreringsdatum DATE,
    registerenhet_id UUID NOT NULL,
    atgardstyp TEXT NOT NULL,
    
    CONSTRAINT fk_registerenhet_id FOREIGN KEY (registerenhet_id)
        REFERENCES finfo.registerenhet (id)
);

CREATE TABLE atgard(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    
    typ TEXT NOT NULL,
    berord_av_atgard_id UUID NOT NULL,
    
    CONSTRAINT fk_berord_av_atgard_id FOREIGN KEY (berord_av_atgard_id)
        REFERENCES berord_av_atgard (id)
);

CREATE TABLE finfo.import_job(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type TEXT NOT NULL,
    status TEXT NOT NULL,
    resource TEXT
);