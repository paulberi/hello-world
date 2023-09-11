INSERT INTO indatatyp VALUES ('TRIMBLE_LEDNINGAR');
INSERT INTO indatatyp VALUES ('TRIMBLE_SCHAKT');

-- Migrera befintlig data
UPDATE intrang SET indatatyp = 'TRIMBLE_LEDNINGAR' WHERE indatatyp = 'TRIMBLE';

DELETE FROM indatatyp WHERE typ = 'TRIMBLE';
