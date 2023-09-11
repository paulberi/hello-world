CREATE TABLE avtal_mappstrategi(
    strategi TEXT PRIMARY KEY
);

INSERT INTO avtal_mappstrategi
    VALUES ('FLAT'), ('HIERARCHICAL');

ALTER TABLE avtalsinstallningar
ADD COLUMN mappstrategi TEXT NOT NULL DEFAULT 'HIERARCHICAL';

ALTER TABLE avtalsinstallningar
ADD CONSTRAINT fk_folderstrategi FOREIGN KEY (mappstrategi) REFERENCES avtal_mappstrategi (strategi);

ALTER TABLE avtalsjobb
ADD COLUMN mappstrategi TEXT NOT NULL DEFAULT 'HIERARCHICAL';

ALTER TABLE avtalsjobb
ADD CONSTRAINT fk_folderstrategi FOREIGN KEY (mappstrategi) REFERENCES avtal_mappstrategi (strategi);