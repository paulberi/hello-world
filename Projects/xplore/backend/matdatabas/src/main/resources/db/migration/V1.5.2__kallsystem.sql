CREATE TABLE kallsystem
(
    namn text PRIMARY KEY,
    beskrivning text,
    default_godkand boolean NOT NULL,
    manuell_import boolean NOT NULL
);

INSERT INTO kallsystem(
    namn, beskrivning, default_godkand, manuell_import
)
VALUES('SMHI metobs', 'SMHIs API för meteorologiska observationer', true, true),
       ('Stockholms Hamnar', 'Webbsida med vattenstånd hanterat av Stockholms Hamnar', true,true),
       ('KOLIBRI', 'Molntjänst för automatiskt uppladdad instrumentdata för Keller-instrument', false, true),
       ('MiljöKoll', 'Mätvärden registrerade direkt i MiljöKoll (antingen via API eller Web-appen)', false, false),
       ('Okänd', 'Det är inte känt varifrån detta mätdata kommer', false, false),
       ('Annat (granskade mätningar)', 'Annat system, importen innehåller redan granskade mätningar', true, true),
       ('Annat (ej granskade mätningar)', 'Annat system, importen innehåller ogranskade mätningar', false, true);

alter table matning ADD COLUMN kallsystem text;

UPDATE matning SET kallsystem='Okänd';

alter table matning alter column kallsystem  SET NOT NULL;
alter table matning add FOREIGN KEY (kallsystem)
 REFERENCES kallsystem(namn);
