CREATE TABLE samfallighet(
    fastighet_id UUID PRIMARY KEY,

    CONSTRAINT fk_fastighet_id FOREIGN KEY (fastighet_id) REFERENCES fastighet (id)
);

INSERT INTO samfallighet (fastighet_id)
SELECT DISTINCT samfallighet_id
  FROM samfallighet_fastighet;

CREATE TABLE samfallighet_mer_info(
    id UUID PRIMARY KEY,

    samf_id UUID NOT NULL,
    kund_id TEXT NOT NULL,

    firmatecknare TEXT,
    co_namn TEXT,
    co_adress TEXT,
    co_postnummer TEXT,
    co_postort TEXT,
    forvaltande_beteckning TEXT,
    andamal TEXT,
    foreningstyp TEXT,
    organisationsnummer TEXT,

    CONSTRAINT fk_samf_id FOREIGN KEY (samf_id) REFERENCES samfallighet (fastighet_id),
    CONSTRAINT fk_kund_id FOREIGN KEY (kund_id) REFERENCES config.kund (id),
    CONSTRAINT uc_samf_kund UNIQUE (samf_id, kund_id)
);

CREATE TABLE atgard(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    samf_mer_info_id UUID NOT NULL,
    
    atgardstyp TEXT NOT NULL,
    registreringsdatum DATE NOT NULL,
    aktbeteckning TEXT NOT NULL,
    atgard TEXT NOT NULL,
    
    CONSTRAINT fk_samf_mer_info_id FOREIGN KEY (samf_mer_info_id) REFERENCES samfallighet_mer_info (id)
);

CREATE TABLE pagaende_fastighetsbildning(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    samf_mer_info_id UUID NOT NULL,
    
    arendestatus TEXT NOT NULL,
    dagboksnummer TEXT NOT NULL,
    
    CONSTRAINT fk_samf_mer_info_id FOREIGN KEY (samf_mer_info_id) REFERENCES samfallighet_mer_info (id)
);

CREATE TABLE rattighet(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    samf_mer_info_id UUID NOT NULL,
    
    andamal TEXT NOT NULL,
    rattsforhallande TEXT NOT NULL,
    aktbeteckning TEXT NOT NULL,
    rattighetsbeskrivning TEXT NOT NULL,
    
    CONSTRAINT fk_samf_mer_info_id FOREIGN KEY (samf_mer_info_id) REFERENCES samfallighet_mer_info (id)
);

CREATE TABLE styrelsemedlem(
    markagare_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    funktion TEXT NOT NULL,

    CONSTRAINT fk_markagare_id FOREIGN KEY (markagare_id) REFERENCES markagare (id)
);

ALTER TABLE samfallighet_fastighet
DROP CONSTRAINT uc_fastighet_samfallighet;

ALTER TABLE samfallighet_fastighet
ADD COLUMN id UUID PRIMARY KEY DEFAULT uuid_generate_v4 ();

ALTER TABLE samfallighet_fastighet
ADD CONSTRAINT uc_samfallighet_fastighet UNIQUE (samfallighet_id, fastighet_id);

ALTER TABLE samfallighet_fastighet
DROP CONSTRAINT fk_samfallighet_fastighet_samfallighet;

ALTER TABLE samfallighet_fastighet
ADD CONSTRAINT fk_samfallighet FOREIGN KEY (samfallighet_id) REFERENCES samfallighet (fastighet_id);

ALTER TABLE markagare
ALTER COLUMN kund_id
SET NOT NULL;

ALTER TABLE person
ALTER COLUMN kund_id
SET NOT NULL;

INSERT INTO agartyp (typ)
VALUES ('STYRELSEMEDLEM');

ALTER TABLE person
ALTER COLUMN postnummer
DROP NOT NULL;

ALTER TABLE person
ALTER COLUMN postort
DROP NOT NULL;