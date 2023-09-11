CREATE TABLE markkoll.avtal_types
(
    name TEXT PRIMARY KEY
);

INSERT INTO markkoll.avtal_types
VALUES
    ('INTRANG'),
    ('SERVIS'),
    ('REV');

ALTER TABLE markkoll.intrang
    ADD avtalstyp TEXT NOT NULL DEFAULT 'INTRANG';

ALTER TABLE markkoll.intrang
    ADD CONSTRAINT fk_avtalstyp
        FOREIGN KEY (avtalstyp) REFERENCES markkoll.avtal_types;

UPDATE markkoll.intrang
    SET avtalstyp='INTRANG';
