CREATE SEQUENCE rapport_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE rapport
(
    id integer PRIMARY KEY default nextval('rapport_id_seq'::regclass),
    namn text NOT NULL UNIQUE,
    aktiv boolean NOT NULL,
    beskrivning text,
    inledning_rubrik text,
    inledning_information text,
    tidsintervall text NOT NULL,
    startdatum timestamp NOT NULL,
    rorelseReferensdatum timestamp,
    lagesbild UUID REFERENCES bifogad_fil(id),
    senast_skickad timestamp,
    dataperiod_from text NOT NULL,
    skapad_datum timestamp NOT NULL,
    andrad_datum timestamp,
    andrad_av_id integer REFERENCES anvandare(id)
);

ALTER SEQUENCE rapport_id_seq OWNED BY rapport.id;

/* --- */

CREATE SEQUENCE rapport_mottagare_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE rapport_mottagare
(
    id integer PRIMARY KEY default nextval('rapport_mottagare_seq'::regclass),
    rapport_id integer REFERENCES rapport(id),
    namn TEXT NOT NULL,
    epost TEXT NOT NULL,
    UNIQUE (rapport_id, namn, epost)
);

ALTER SEQUENCE rapport_mottagare_seq OWNED BY rapport_mottagare.id;

/* --- */

CREATE SEQUENCE rapport_graf_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE rapport_graf
(
    id integer PRIMARY KEY default nextval('rapport_graf_seq'::regclass),
    rapport_id integer REFERENCES rapport(id),
    rubrik TEXT NOT NULL,
    info TEXT NOT NULL
);

ALTER SEQUENCE rapport_graf_seq OWNED BY rapport_graf.id;

/* --- */

CREATE SEQUENCE rapport_graf_matningstyp_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE rapport_graf_matningstyp
(
    id integer PRIMARY KEY default nextval('rapport_graf_matningstyp_seq'::regclass),
    rapport_graf_id integer REFERENCES rapport_graf(id),
    matningstyp_id integer REFERENCES matningstyp(id)
);

ALTER SEQUENCE rapport_graf_matningstyp_seq OWNED BY rapport_graf_matningstyp.id;

/* --- */

CREATE SEQUENCE rapport_graf_gransvarde_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE rapport_graf_gransvarde
(
    id integer PRIMARY KEY default nextval('rapport_graf_gransvarde_seq'::regclass),
    rapport_graf_id integer REFERENCES rapport_graf(id),
    gransvarde_id integer REFERENCES gransvarde(id)
);

ALTER SEQUENCE rapport_graf_gransvarde_seq OWNED BY rapport_graf_gransvarde.id;

/* Make it a scheduled job. */
INSERT INTO scheduler_job_info (id, cron_expression, cron_job, job_class, job_name)
VALUES (5, '0 0 1 ? * *', true, 'rapportJob', 'Generera rapporter');
