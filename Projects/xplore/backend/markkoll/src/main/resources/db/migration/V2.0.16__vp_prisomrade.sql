DELETE FROM elnat.prisomrade
WHERE prisomrade = 'TILLVÄXTOMRADE_3';

INSERT INTO elnat.prisomrade
VALUES
    ('TILLVAXTOMRADE_3');

ALTER TABLE elnat.varderingsprotokoll
ADD prisomrade TEXT NOT NULL DEFAULT 'NORRLANDS_INLAND';

ALTER TABLE elnat.varderingsprotokoll
ADD CONSTRAINT fk_prisomrade FOREIGN KEY (prisomrade) REFERENCES elnat.prisomrade(prisomrade);

ALTER TABLE elnat.ssb_skogsmark
DROP COLUMN prisomrade;