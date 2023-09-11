CREATE SCHEMA IF NOT EXISTS elnat;
CREATE SCHEMA IF NOT EXISTS fiber;
-- TODO: Kolla upp: Varför finns samma attribut i både el och fiber-infon?

-- Kopiera El-projektinfo till ny tabell i el-schemat
SELECT id, bestallare, ledningsagare, ledningsstracka
INTO elnat.projekt
FROM markkoll.projekt
WHERE projekttyp IN ('LOKALNAT', 'REGIONNAT');
ALTER TABLE elnat.projekt ADD CONSTRAINT projekt_pkey PRIMARY KEY (id);
ALTER TABLE elnat.projekt ADD CONSTRAINT fk_projekt FOREIGN KEY (id) REFERENCES markkoll.projekt (id);

-- Kopiera Fiber-projektinfo till ny tabell i el-schemat
SELECT id, bestallare, ledningsagare, ledningsstracka, bidragsprojekt
INTO fiber.projekt
FROM markkoll.projekt
WHERE projekttyp IN ('FIBER');
ALTER TABLE fiber.projekt ADD CONSTRAINT projekt_pkey PRIMARY KEY (id);
ALTER TABLE fiber.projekt ADD CONSTRAINT fk_projekt FOREIGN KEY (id) REFERENCES markkoll.projekt (id);

-- Ta bort från gamla projekttabellen
ALTER TABLE markkoll.projekt DROP COLUMN bestallare;
ALTER TABLE markkoll.projekt DROP COLUMN ledningsagare;
ALTER TABLE markkoll.projekt DROP COLUMN ledningsstracka;
ALTER TABLE markkoll.projekt DROP COLUMN bidragsprojekt;
