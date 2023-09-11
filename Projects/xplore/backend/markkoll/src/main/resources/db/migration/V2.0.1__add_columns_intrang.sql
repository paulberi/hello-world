-- intrang_status
CREATE TABLE IF NOT EXISTS intrang_status
(
    status text NOT NULL,
    PRIMARY KEY (status)
);

INSERT INTO intrang_status (status)
VALUES
    ('NY'),
    ('BEVARAS'),
    ('RASERAS');

-- indatatyp

CREATE TABLE IF NOT EXISTS indatatyp
(
    typ text NOT NULL,
    PRIMARY KEY (typ)
);

INSERT INTO indatatyp (typ)
VALUES
    ('DPCOM'),
    ('DPPOWER');

-- intrang

ALTER TABLE intrang
    ADD COLUMN status text NOT NULL DEFAULT 'NY';

ALTER TABLE intrang
    ADD COLUMN indatatyp text NOT NULL DEFAULT 'DPCOM';

ALTER TABLE intrang
    ADD COLUMN feature_properties jsonb NOT NULL DEFAULT '{}';

ALTER TABLE intrang
    ADD CONSTRAINT fk_status FOREIGN KEY (status)
        REFERENCES intrang_status (status)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

ALTER TABLE intrang
    ADD CONSTRAINT fk_indatatyp FOREIGN KEY (indatatyp)
        REFERENCES indatatyp (typ)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

-- fastighetsintrang
ALTER TABLE fastighetsintrang
    ADD COLUMN status text NOT NULL DEFAULT 'NY';

ALTER TABLE fastighetsintrang
    ADD CONSTRAINT fk_status FOREIGN KEY (status)
        REFERENCES intrang_status (status)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

-- omradesintrang
ALTER TABLE omradesintrang
    ADD COLUMN status text NOT NULL DEFAULT 'NY';

ALTER TABLE omradesintrang
    ADD CONSTRAINT fk_status FOREIGN KEY (status)
        REFERENCES intrang_status (status)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;
