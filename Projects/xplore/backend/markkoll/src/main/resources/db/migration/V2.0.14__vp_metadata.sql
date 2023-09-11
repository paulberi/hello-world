ALTER TABLE markkoll.avtal
    ADD COLUMN stationsnamn TEXT NOT NULL DEFAULT '',
    ADD COLUMN matande_station TEXT NOT NULL DEFAULT '',
    ADD COLUMN till_station TEXT NOT NULL DEFAULT '',
    ADD COLUMN fran_station TEXT NOT NULL DEFAULT '',
    ADD COLUMN uppdragsnummer TEXT NOT NULL DEFAULT '',
    ADD COLUMN markslag TEXT NOT NULL DEFAULT '',
    ADD COLUMN spanningsniva TEXT NOT NULL DEFAULT '';