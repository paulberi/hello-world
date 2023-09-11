CREATE OR REPLACE VIEW matningstyp_matobjekt AS
SELECT
    mt.id AS matningstyp_id,
    dmt.namn AS matningstyp_namn,
    mt.aktiv AS matningstyp_aktiv,
    mt.matansvarig_anvandargrupp_id,
    mo.id AS matobjekt_id,
    mo.namn AS matobjekt_namn,
    mo.typ AS matobjekt_typ,
    mo.fastighet AS matobjekt_fastighet,
    mo.lage AS matobjekt_lage,
    mo.aktiv AS matobjekt_aktiv,
    mo.aktiv AND mt.aktiv AS aktiv
FROM matningstyp mt
     JOIN matobjekt mo ON mo.id = mt.matobjekt_id
     JOIN definition_matningstyp dmt ON mt.definition_matningstyp_id = dmt.id;

CREATE TABLE grupp_kategori
(
    id smallint NOT NULL
        CONSTRAINT pk_grupp_kategori PRIMARY KEY,
    namn text NOT NULL
);


INSERT INTO grupp_kategori (id, namn)
VALUES (0, 'Övrigt'),
       (1, 'Grundvattenmagasin'),
       (2, 'Avrinningsområde'),
       (3, 'Kvarter'),
       (4, 'Dubbgrupp'),
       (5, 'Område');

ALTER TABLE grupp
    ADD CONSTRAINT fk_grupp_kategori FOREIGN KEY (kategori)
        REFERENCES grupp_kategori (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

CREATE TABLE grupp_kartsymbol
(
    id smallint NOT NULL
        CONSTRAINT pk_grupp_kartsymbol PRIMARY KEY,
    namn text NOT NULL
);


INSERT INTO grupp_kartsymbol (id, namn)
VALUES (0, 'Ingen (förvald)'),
       (1, 'Undre grundvattenmagasin'),
       (2, 'Övre grundvattenmagasin'),
       (3, 'Grundvattenmagasin i berg'),
       (4, 'Portryck'),
       (5, 'Energibrunn'),
       (6, 'Pumpgrop'),
       (7, 'Mätdamm'),
       (8, 'Provpunkt');

ALTER TABLE grupp
    ADD CONSTRAINT fk_grupp_kartsymbol FOREIGN KEY (kartsymbol)
        REFERENCES grupp_kartsymbol (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

