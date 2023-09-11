CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;
CREATE SCHEMA IF NOT EXISTS kund_config;

CREATE TABLE kund
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    namn VARCHAR NOT NULL UNIQUE,
    epost VARCHAR,
    kontaktperson VARCHAR,
    telefon VARCHAR,
    skapad_av VARCHAR NOT NULL,
    skapad_datum TIMESTAMP NOT NULL,
    andrad_av VARCHAR NOT NULL,
    andrad_datum TIMESTAMP NOT NULL
);