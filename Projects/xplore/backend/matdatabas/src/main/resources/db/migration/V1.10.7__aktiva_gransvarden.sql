ALTER TABLE gransvarde
    ADD COLUMN aktiv boolean DEFAULT true NOT NULL;

ALTER TABLE larm
    ADD COLUMN larmniva_id integer,
    ADD COLUMN gransvarde double precision,
    ADD COLUMN anvandargrupp_id integer,
    ADD COLUMN varde double precision;

WITH G as (
    SELECT gransvarde.id,
        gransvarde.gransvarde,
        larmniva.id AS larmnivaId,
           gransvarde.larm_till_anvandargrupp_id AS anvandargruppId
    FROM gransvarde
        INNER JOIN larmniva
            ON gransvarde.larmniva_id = larmniva.id
)
UPDATE larm L
    SET larmniva_id = G.larmnivaId,
        gransvarde = G.gransvarde,
        anvandargrupp_id = G.anvandargruppId
    FROM G
    WHERE L.gransvarde_id = G.id;

UPDATE larm
    SET varde = CASE
            WHEN matning.beraknat_varde IS NOT NULL THEN matning.beraknat_varde
            ELSE matning.avlast_varde
            END
    FROM matning
    WHERE larm.matning_id = matning.id;

ALTER TABLE larm
    ALTER COLUMN larmniva_id set NOT NULL,
    ALTER COLUMN gransvarde set NOT NULL,
    ALTER COLUMN varde set NOT NULL;