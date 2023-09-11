CREATE TABLE matobjekt_typ
(
    id   smallint NOT NULL
        CONSTRAINT pk_matobjekt_typ PRIMARY KEY,
    namn text     NOT NULL
);



INSERT INTO matobjekt_typ (id, namn)
VALUES (0, 'Grundvattennivå'),
       (1, 'Infiltration'),
       (2, 'Rörelse'),
       (3, 'Tunnelvatten'),
       (4, 'Vattenkemi'),
       (5, 'Väderstation');

ALTER TABLE definition_matningstyp
    ADD CONSTRAINT fk_definition_matningstyp_matobjekt_typ FOREIGN KEY (matobjekt_typ)
        REFERENCES matobjekt_typ (id);

ALTER TABLE matobjekt
    ADD CONSTRAINT fk_matobjekt_matobjekt_typ FOREIGN KEY (typ)
        REFERENCES matobjekt_typ (id);

CREATE TABLE tidsenhet
(
    id   smallint NOT NULL
        CONSTRAINT pk_tidsenhet PRIMARY KEY,
    namn text     NOT NULL
);

INSERT INTO tidsenhet (id, namn)
VALUES (0, 'Timme'),
       (1, 'Dag'),
       (2, 'Vecka'),
       (3, 'Månad'),
       (4, 'År');

ALTER TABLE matningstyp
    ADD CONSTRAINT fk_matningstyp_matobjekt_typ FOREIGN KEY (matintervall_tidsenhet)
        REFERENCES tidsenhet (id);
