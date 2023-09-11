-- Ta bort projektstatus-kolumn från projekt-tabell
ALTER TABLE projekt
DROP COLUMN IF EXISTS status;

-- Ta bort projektstatus-tabellen
DROP TABLE IF EXISTS projektstatus;
