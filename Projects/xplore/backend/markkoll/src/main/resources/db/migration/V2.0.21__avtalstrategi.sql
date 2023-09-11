CREATE TABLE avtalstrategi(
    typ TEXT PRIMARY KEY
);

INSERT INTO avtalstrategi (typ)
    VALUES
        ('FASTIGHET'),
        ('ADRESS'),
        ('FASTIGHETSAGARE');

ALTER TABLE projekt
ADD avtalstrategi TEXT NOT NULL DEFAULT 'ADRESS';

ALTER TABLE projekt
ADD CONSTRAINT fk_avtalstrategi FOREIGN KEY (avtalstrategi) REFERENCES avtalstrategi (typ);