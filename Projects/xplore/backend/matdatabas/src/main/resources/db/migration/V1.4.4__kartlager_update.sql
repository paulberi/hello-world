ALTER TABLE kartlager
    DROP COLUMN direktlank,
    DROP COLUMN omrade_n1,
    DROP COLUMN omrade_e1,
    DROP COLUMN omrade_n2,
    DROP COLUMN omrade_e2;

INSERT INTO kartlager (id, namn, grupp, ordning, visa, beskrivning, andringsbar, skapad_datum, andrad_datum,
                       andrad_av_id)
VALUES (1, 'Grundkarta', null, 0, true, 'Systemets bakgrundskarta', false, now(), now(), 0),
       (2, 'Mätobjekt', null, 100, true, 'Visar symboler för de mätobjekt som sökts fram', false, now(), now(), 0)
ON CONFLICT DO NOTHING;

SELECT setval('kartlager_id_seq', (SELECT max(id) FROM kartlager));

CREATE TABLE kartlager_fil_stil
(
    stil text NOT NULL
        CONSTRAINT pk_kartlager_fil_stil PRIMARY KEY
);


INSERT INTO kartlager_fil_stil (stil)
VALUES ('Anläggning'),
       ('Influensområde'),
       ('Grundläggning på lera'),
       ('Okänd grundläggning'),
       ('Undre grundvattenmagasin'),
       ('Grundvattenförekomst'),
       ('Känslig grundläggning');

ALTER TABLE kartlager_fil
    ADD COLUMN stil text NOT NULL default 'Anläggning' REFERENCES kartlager_fil_stil (stil);
