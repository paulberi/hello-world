
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;
CREATE EXTENSION IF NOT EXISTS "postgis" WITH SCHEMA public;

CREATE SCHEMA IF NOT EXISTS mapcms;

CREATE TABLE mapcms.anvandare
(
    id uuid default uuid_generate_v4() NOT NULL,
    kund_id uuid NOT NULL,
    epost text NOT NULL,
    roll text NOT NULL,
    aktiv boolean NULL   DEFAULT true,
    senast_inloggad timestamp without time zone NULL
)
;

CREATE TABLE mapcms.anvandare_dialog_part
(
    anvandare_id uuid NOT NULL,
    part_id uuid NOT NULL
)
;

CREATE TABLE mapcms.auditlogg
(
    id uuid default uuid_generate_v4() NOT NULL,
    projekt_id uuid NOT NULL,
    vem text NOT NULL,
    vad text NOT NULL,
    nar timestamp without time zone NOT NULL
)
;

CREATE TABLE mapcms.dialog
(
    id uuid default uuid_generate_v4() NOT NULL,
    projekt_id uuid NOT NULL,
    rubrik text NOT NULL,
    plats geometry NULL,
    publicering_status text NOT NULL,
    dialog_status text NOT NULL,
    skapad_datum timestamp without time zone NOT NULL,
    skapad_av text NOT NULL
)
;

CREATE TABLE mapcms.dialog_part
(
    id uuid default uuid_generate_v4() NOT NULL,
    dialog_id uuid NOT NULL,
    namn text NOT NULL,
    epost text NULL,
    skapare boolean NOT NULL
)
;

CREATE TABLE mapcms.fil
(
    id uuid default uuid_generate_v4() NOT NULL,
    mimetyp text NOT NULL,
    filnamn text NOT NULL,
    beskrivning text NULL,
    fil bytea NOT NULL,
    skapad_datum timestamp without time zone NOT NULL,
    skapad_av text NOT NULL,
    andrad_datum timestamp without time zone NULL,
    andrad_av text NULL
)
;

CREATE TABLE mapcms.geometri
(
    id uuid default uuid_generate_v4() NOT NULL,
    projekt_id uuid NULL,
    geom geometry NULL,
    properties jsonb NULL
)
;

CREATE TABLE mapcms.kund
(
    id uuid default uuid_generate_v4() NOT NULL,
    vht_nyckel text NOT NULL,
    slug text NOT NULL,
    namn text NOT NULL,
    logotyp uuid NULL,
    egenskaper jsonb NULL,
    standardsprak text NOT NULL,
    andrad_datum timestamp without time zone NULL,
    andrad_av text NULL
)
;

CREATE TABLE mapcms.kund_sprak
(
    kund_id uuid NOT NULL,
    sprak_kod text NOT NULL
)
;

CREATE TABLE mapcms.meddelande
(
    id uuid default uuid_generate_v4() NOT NULL,
    fran uuid NOT NULL,
    text text NOT NULL,
    publicering_status text NOT NULL,
    skapad_datum timestamp without time zone NOT NULL,
    skapad_av text NOT NULL
)
;

CREATE TABLE mapcms.meddelande_filer
(
    meddelande_id uuid NOT NULL,
    fil_id uuid NOT NULL
)
;

CREATE TABLE mapcms.person
(
    pnr text NOT NULL,
    namn text NOT NULL,
    senast_inloggad timestamp without time zone NULL
)
;

CREATE TABLE mapcms.person_dialog_part
(
    person_pnr text NOT NULL,
    part_id uuid NOT NULL
)
;

CREATE TABLE mapcms.projekt
(
    id uuid default uuid_generate_v4() NOT NULL,
    slug text NOT NULL,
    kund_id uuid NOT NULL,
    rubrik text NOT NULL,
    ingress text NOT NULL,
    brodtext text NOT NULL,
    publicering_status text NOT NULL,
    skapad_datum timestamp without time zone NOT NULL,
    skapad_av text NOT NULL,
    andrad_datum timestamp without time zone NULL,
    andrad_av text NULL
)
;

CREATE TABLE mapcms.projekt_filer
(
    projekt_id uuid NOT NULL,
    fil_id uuid NOT NULL
)
;

CREATE TABLE mapcms.projekt_oversattning
(
    projekt_id uuid NOT NULL,
    sprak_kod text NOT NULL,
    rubrik text NOT NULL,
    ingress text NOT NULL,
    brodtext text NOT NULL
)
;

CREATE TABLE mapcms.sprak
(
    kod text NOT NULL,
    namn text NOT NULL,
    namn_org text NOT NULL
)
;

/* Create Primary Keys, Indexes, Uniques, Checks */

ALTER TABLE mapcms.anvandare ADD CONSTRAINT "PK_anvandare"
    PRIMARY KEY (id)
;

CREATE INDEX "IXFK_anvandare_kund" ON mapcms.anvandare (kund_id ASC)
;

CREATE INDEX "IX_epost" ON mapcms.anvandare (epost ASC)
;

ALTER TABLE mapcms.anvandare_dialog_part ADD CONSTRAINT "PK_anvandare_dialog_part"
    PRIMARY KEY (anvandare_id,part_id)
;

CREATE INDEX "IXFK_anvandare_dialog_part_anvandare" ON mapcms.anvandare_dialog_part (anvandare_id ASC)
;

CREATE INDEX "IXFK_anvandare_dialog_part_dialog_part" ON mapcms.anvandare_dialog_part (part_id ASC)
;

ALTER TABLE mapcms.auditlogg ADD CONSTRAINT "PK_auditlogg"
    PRIMARY KEY (id)
;

CREATE INDEX "IXFK_auditlogg_projekt" ON mapcms.auditlogg (projekt_id ASC)
;

ALTER TABLE mapcms.dialog ADD CONSTRAINT "PK_dialog"
    PRIMARY KEY (id)
;

CREATE INDEX "IXFK_dialog_projekt" ON mapcms.dialog (projekt_id ASC)
;

ALTER TABLE mapcms.dialog_part ADD CONSTRAINT "PK_dialog_part"
    PRIMARY KEY (id)
;

CREATE INDEX "IXFK_dialog_part_dialog" ON mapcms.dialog_part (dialog_id ASC)
;

ALTER TABLE mapcms.fil ADD CONSTRAINT "PK_fil"
    PRIMARY KEY (id)
;

ALTER TABLE mapcms.geometri ADD CONSTRAINT "PK_geometri"
    PRIMARY KEY (id)
;

CREATE INDEX "IXFK_geometri_projekt" ON mapcms.geometri (projekt_id ASC)
;

ALTER TABLE mapcms.kund ADD CONSTRAINT "PK_kund"
    PRIMARY KEY (id)
;

ALTER TABLE mapcms.kund
    ADD CONSTRAINT "AK_kund_slug" UNIQUE (slug)
;

ALTER TABLE mapcms.kund
    ADD CONSTRAINT "AK_kund_vht_nyckel" UNIQUE (vht_nyckel)
;

CREATE INDEX "IXFK_kund_fil" ON mapcms.kund (logotyp ASC)
;

CREATE INDEX "IXFK_kund_sprak" ON mapcms.kund (standardsprak ASC)
;

ALTER TABLE mapcms.kund_sprak ADD CONSTRAINT "PK_kund_sprak"
    PRIMARY KEY (kund_id,sprak_kod)
;

CREATE INDEX "IXFK_kund_sprak_kund" ON mapcms.kund_sprak (kund_id ASC)
;

CREATE INDEX "IXFK_kund_sprak_sprak" ON mapcms.kund_sprak (sprak_kod ASC)
;

ALTER TABLE mapcms.meddelande ADD CONSTRAINT "PK_meddelande"
    PRIMARY KEY (id)
;

CREATE INDEX "IXFK_meddelande_dialog_part" ON mapcms.meddelande (fran ASC)
;

ALTER TABLE mapcms.meddelande_filer ADD CONSTRAINT "PK_meddelande_filer"
    PRIMARY KEY (meddelande_id,fil_id)
;

CREATE INDEX "IXFK_meddelande_filer_fil" ON mapcms.meddelande_filer (fil_id ASC)
;

CREATE INDEX "IXFK_meddelande_filer_meddelande" ON mapcms.meddelande_filer (meddelande_id ASC)
;

ALTER TABLE mapcms.person ADD CONSTRAINT "PK_person"
    PRIMARY KEY (pnr)
;

ALTER TABLE mapcms.person_dialog_part ADD CONSTRAINT "PK_person_dialog_part"
    PRIMARY KEY (person_pnr,part_id)
;

CREATE INDEX "IXFK_person_dialog_part_dialog_part" ON mapcms.person_dialog_part (part_id ASC)
;

CREATE INDEX "IXFK_person_dialog_part_person" ON mapcms.person_dialog_part (person_pnr ASC)
;

ALTER TABLE mapcms.projekt ADD CONSTRAINT "PK_projekt"
    PRIMARY KEY (id)
;

ALTER TABLE mapcms.projekt
    ADD CONSTRAINT "AK_projekt_slug" UNIQUE (slug)
;

CREATE INDEX "IXFK_projekt_kund" ON mapcms.projekt (kund_id ASC)
;

ALTER TABLE mapcms.projekt_filer ADD CONSTRAINT "PK_projekt_filer"
    PRIMARY KEY (projekt_id,fil_id)
;

CREATE INDEX "IXFK_projekt_filer_fil" ON mapcms.projekt_filer (fil_id ASC)
;

CREATE INDEX "IXFK_projekt_filer_projekt" ON mapcms.projekt_filer (projekt_id ASC)
;

ALTER TABLE mapcms.projekt_oversattning ADD CONSTRAINT "PK_projekt_oversattning"
    PRIMARY KEY (projekt_id,sprak_kod)
;

CREATE INDEX "IXFK_projekt_oversattning_projekt" ON mapcms.projekt_oversattning (projekt_id ASC)
;

CREATE INDEX "IXFK_projekt_oversattning_sprak" ON mapcms.projekt_oversattning (sprak_kod ASC)
;

ALTER TABLE mapcms.sprak ADD CONSTRAINT "PK_sprak"
    PRIMARY KEY (kod)
;

/* Create Foreign Key Constraints */

ALTER TABLE mapcms.anvandare ADD CONSTRAINT "FK_anvandare_kund"
    FOREIGN KEY (kund_id) REFERENCES kund (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.anvandare_dialog_part ADD CONSTRAINT "FK_anvandare_dialog_part_anvandare"
    FOREIGN KEY (anvandare_id) REFERENCES mapcms.anvandare (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.anvandare_dialog_part ADD CONSTRAINT "FK_anvandare_dialog_part_dialog_part"
    FOREIGN KEY (part_id) REFERENCES mapcms.dialog_part (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.auditlogg ADD CONSTRAINT "FK_auditlogg_projekt"
    FOREIGN KEY (projekt_id) REFERENCES mapcms.projekt (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.dialog ADD CONSTRAINT "FK_dialog_projekt"
    FOREIGN KEY (projekt_id) REFERENCES mapcms.projekt (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.dialog_part ADD CONSTRAINT "FK_dialog_part_dialog"
    FOREIGN KEY (dialog_id) REFERENCES mapcms.dialog (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.geometri ADD CONSTRAINT "FK_geometri_projekt"
    FOREIGN KEY (projekt_id) REFERENCES mapcms.projekt (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.kund ADD CONSTRAINT "FK_kund_fil"
    FOREIGN KEY (logotyp) REFERENCES mapcms.fil (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.kund ADD CONSTRAINT "FK_kund_sprak"
    FOREIGN KEY (standardsprak) REFERENCES mapcms.sprak (kod) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.kund_sprak ADD CONSTRAINT "FK_kund_sprak_kund"
    FOREIGN KEY (kund_id) REFERENCES mapcms.kund (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.kund_sprak ADD CONSTRAINT "FK_kund_sprak_sprak"
    FOREIGN KEY (sprak_kod) REFERENCES mapcms.sprak (kod) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.meddelande ADD CONSTRAINT "FK_meddelande_dialog_part"
    FOREIGN KEY (fran) REFERENCES mapcms.dialog_part (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.meddelande_filer ADD CONSTRAINT "FK_meddelande_filer_fil"
    FOREIGN KEY (fil_id) REFERENCES mapcms.fil (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.meddelande_filer ADD CONSTRAINT "FK_meddelande_filer_meddelande"
    FOREIGN KEY (meddelande_id) REFERENCES mapcms.meddelande (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.person_dialog_part ADD CONSTRAINT "FK_person_dialog_part_dialog_part"
    FOREIGN KEY (part_id) REFERENCES mapcms.dialog_part (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.person_dialog_part ADD CONSTRAINT "FK_person_dialog_part_person"
    FOREIGN KEY (person_pnr) REFERENCES mapcms.person (pnr) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.projekt ADD CONSTRAINT "FK_projekt_kund"
    FOREIGN KEY (kund_id) REFERENCES mapcms.kund (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.projekt_filer ADD CONSTRAINT "FK_projekt_filer_fil"
    FOREIGN KEY (fil_id) REFERENCES mapcms.fil (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.projekt_filer ADD CONSTRAINT "FK_projekt_filer_projekt"
    FOREIGN KEY (projekt_id) REFERENCES mapcms.projekt (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.projekt_oversattning ADD CONSTRAINT "FK_projekt_oversattning_projekt"
    FOREIGN KEY (projekt_id) REFERENCES mapcms.projekt (id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE mapcms.projekt_oversattning ADD CONSTRAINT "FK_projekt_oversattning_sprak"
    FOREIGN KEY (sprak_kod) REFERENCES mapcms.sprak (kod) ON DELETE No Action ON UPDATE No Action
;
