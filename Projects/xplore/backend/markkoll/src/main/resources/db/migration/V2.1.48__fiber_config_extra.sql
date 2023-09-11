ALTER TABLE fiber.vardering_config
ADD minimiersattning_enbart_markledning BOOL NOT NULL DEFAULT true;

ALTER TABLE fiber.vardering_config
ADD sarskild_ersattning_maxbelopp INTEGER NOT NULL DEFAULT 0;