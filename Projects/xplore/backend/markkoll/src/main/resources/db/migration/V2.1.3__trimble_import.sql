INSERT INTO indatatyp VALUES ('TRIMBLE');

-- Migrera befintlig data
UPDATE intrang SET indatatyp = 'TRIMBLE' WHERE indatatyp = 'TRIMBLE_LEDNINGAR' OR indatatyp = 'TRIMBLE_SCHAKT';

DELETE FROM indatatyp WHERE typ = 'TRIMBLE_LEDNINGAR';
DELETE FROM indatatyp WHERE typ = 'TRIMBLE_SCHAKT';
