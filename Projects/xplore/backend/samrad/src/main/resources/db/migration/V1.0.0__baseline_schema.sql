CREATE EXTENSION IF NOT EXISTS "postgis" WITH SCHEMA public;
CREATE SCHEMA IF NOT EXISTS projekt;

create table kund
(
    id          text not null primary key,
    namn            text,
    slug            text not null,
    bakgrund        text,
    forgrund        text,
    andring_av      text,
    andring_datum   timestamp

);

create table anvandare
(
    id         uuid not null primary key,
    namn                text,
    epost               text,
    roll                text,
    aktiv               boolean,
    senast_inloggad     timestamp,
    andring_av          text,
    andring_datum       timestamp
);



create table projekt
(
    id           uuid not null primary key,
    rubrik              text  not null,
    ingress             text,
    brodtext            text,
    slug                text,
    status              text,
    andring_av          text,
    andring_datum       timestamp
);

create table synpunkt
(
    id                  uuid not null primary key,
    namn                        text,
    epost                       text,
    rubrik                      text,
    innehall                    text,
    plats                       text,
    status                      text,
    andring_av                  text,
    andring_datum               timestamp

);

create table synpunkt_svar
(
    id               uuid not null primary key,
    status                       text,
    rubrik                       text,
    innehall                     text,
    andring_av                   text,
    andring_datum                timestamp
);

create table nyhet
(
    id                     uuid not null primary key,
    rubrik                      text,
    innehall                    text,
    status                      text,
    andring_av                  text,
    andring_datum               timestamp
);


create table projekt_filer
(
    id               uuid not null primary key ,
    fil_namn             text,
    mimetyp              text,
    fil                 bytea,
    beskrivning         text,
    publik              boolean,
    andring_av          text,
    andring_datum       timestamp
);

create table projekt_aktivitets_Log
(
    id               uuid not null primary key,
    andringar                     text,
    andring_av                    text,
    andring_datum                 timestamp
);

create table prenumeration
(
    id               uuid not null primary key,
    epost                         text,
    andring_av                    text,
    andring_datum                 timestamp
);
