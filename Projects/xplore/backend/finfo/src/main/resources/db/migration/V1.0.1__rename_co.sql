ALTER TABLE finfo.samfallighetsforening
RENAME COLUMN co_adress TO co_namn;

ALTER TABLE finfo.samfallighetsforening
RENAME COLUMN adress TO co_adress;

ALTER TABLE finfo.samfallighetsforening
RENAME COLUMN postnummer TO co_postnummer;

ALTER TABLE finfo.samfallighetsforening
RENAME COLUMN postort TO co_postort;