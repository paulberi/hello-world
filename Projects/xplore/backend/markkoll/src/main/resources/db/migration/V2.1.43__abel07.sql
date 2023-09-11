INSERT INTO avtal_types (name)
    VALUES ('ABEL07');

CREATE TABLE berakning_rev_type(
    type TEXT PRIMARY KEY
);

INSERT INTO berakning_rev_type (type)
    VALUES
        ('ENBART_REV'),
        ('REV_GRUNDERSATTNING'),
        ('REV_FULL_ERSATTNING');
        
CREATE TABLE berakning_abel07_type(
    type TEXT PRIMARY KEY
);

INSERT INTO berakning_abel07_type (type)
    VALUES
        ('ENBART_ABEL07'),
        ('ABEL07_GRUNDERSATTNING'),
        ('ABEL07_FULL_ERSATTNING');

CREATE TABLE avtalsinstallningar(
    projekt_id UUID PRIMARY KEY,
    berakning_abel07 TEXT NOT NULL DEFAULT 'ENBART_ABEL07',
    berakning_rev TEXT NOT NULL DEFAULT 'ENBART_REV',

    CONSTRAINT fk_projekt_id FOREIGN KEY (projekt_id) REFERENCES projekt (id),
    CONSTRAINT fk_berakning_abel07 FOREIGN KEY (berakning_abel07) REFERENCES berakning_abel07_type (type),
    CONSTRAINT fk_berakning_rev FOREIGN KEY (berakning_rev) REFERENCES berakning_rev_type (type)
);

INSERT INTO avtalsinstallningar (projekt_id, berakning_abel07, berakning_rev)
     SELECT id,
            'ENBART_ABEL07',
            'ENBART_REV'
       FROM projekt;