CREATE TABLE felkod
(
    felkod text NOT NULL
        CONSTRAINT pk_felkod PRIMARY KEY
);


INSERT INTO felkod (felkod)
VALUES ('Ok'),
       ('Flödar'),
       ('Torr'),
       ('Fruset'),
       ('Hinder'),
       ('Annat fel');

UPDATE matning SET felkod = 'Ok' where felkod IS NULL;
UPDATE matning SET felkod = 'Ok' where felkod = '';

ALTER TABLE matning
    ALTER COLUMN felkod SET NOT NULL;

ALTER TABLE matning
    ADD CONSTRAINT fk_matning_felkod FOREIGN KEY (felkod)
        REFERENCES felkod (felkod);
