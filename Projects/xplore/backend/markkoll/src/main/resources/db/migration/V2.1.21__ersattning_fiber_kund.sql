CREATE TABLE fiber.vardering_config(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    markskap_skog INTEGER NOT NULL DEFAULT 0,
    markskap_jordbruksimpediment INTEGER NOT NULL DEFAULT 0,
    markskap_ovrig_mark INTEGER NOT NULL DEFAULT 0,
    
    optobrunn_skog INTEGER NOT NULL DEFAULT 0,
    optobrunn_jordbruksimpediment INTEGER NOT NULL DEFAULT 0,
    optobrunn_ovrig_mark INTEGER NOT NULL DEFAULT 0,
    
    teknikbod_skog_6x6m INTEGER NOT NULL DEFAULT 0,
    teknikbod_skog_8x8m INTEGER NOT NULL DEFAULT 0,
    teknikbod_skog_10x10m INTEGER NOT NULL DEFAULT 0,
    
    teknikbod_jordbruksimpediment_6x6m INTEGER NOT NULL DEFAULT 0,
    teknikbod_jordbruksimpediment_8x8m INTEGER NOT NULL DEFAULT 0,
    teknikbod_jordbruksimpediment_10x10m INTEGER NOT NULL DEFAULT 0,
    
    teknikbod_ovrig_mark_6x6m INTEGER NOT NULL DEFAULT 0,
    teknikbod_ovrig_mark_8x8m INTEGER NOT NULL DEFAULT 0,
    teknikbod_ovrig_mark_10x10m INTEGER NOT NULL DEFAULT 0,
    
    schablonersattning_optoror_1m NUMERIC(5, 2) DEFAULT 0,
    schablonersattning_optoror_2m NUMERIC(5, 2) DEFAULT 0,
    
    grundersattning INTEGER NOT NULL DEFAULT 0,
    minimiersattning INTEGER NOT NULL DEFAULT 0,
    
    tillagg_expropriationslagen INTEGER DEFAULT 0,
    sarskild_ersattning INTEGER DEFAULT 0
);

CREATE TABLE fiber.vardering_kund_agare(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    vardering_config_id UUID NOT NULL,
    kund_id TEXT,
    person_id UUID,
    
    CONSTRAINT fk_vardering_config FOREIGN KEY (vardering_config_id) REFERENCES fiber.vardering_config (id),
    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES config.kund (id),
    CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES person (id),
    CONSTRAINT uq_kund_person UNIQUE (kund_id, person_id)
);

/* Metria default */
INSERT INTO fiber.vardering_config (
                id,
                markskap_skog, markskap_jordbruksimpediment, markskap_ovrig_mark,
                optobrunn_skog, optobrunn_jordbruksimpediment, optobrunn_ovrig_mark,
                teknikbod_skog_6x6m, teknikbod_skog_8x8m, teknikbod_skog_10x10m,
                teknikbod_jordbruksimpediment_6x6m, teknikbod_jordbruksimpediment_8x8m, teknikbod_jordbruksimpediment_10x10m,
                teknikbod_ovrig_mark_6x6m, teknikbod_ovrig_mark_8x8m, teknikbod_ovrig_mark_10x10m,
                schablonersattning_optoror_1m, schablonersattning_optoror_2m,
                grundersattning, minimiersattning,
                tillagg_expropriationslagen, sarskild_ersattning
            )
    VALUES
        (
            '246101c1-c5af-402a-b072-7ee140e52c6e',
            750, 750, 750,
            750, 750, 750,
            3450, 4200, 5100,
            4050, 4500, 5100,
            4050, 4500, 5100,
            6.29, 7.86,
            0, 2415,
            0, 0
        );
    
INSERT INTO fiber.vardering_kund_agare (vardering_config_id, kund_id, person_id)
    VALUES
        ('246101c1-c5af-402a-b072-7ee140e52c6e', NULL, NULL)