CREATE TABLE fiber.vp_dupe_ids(
    vp_id uuid NOT NULL
);

INSERT INTO fiber.vp_dupe_ids (vp_id)
WITH vp_dupe as (
    SELECT avtal_id
    FROM fiber.varderingsprotokoll vp
    GROUP BY vp.avtal_id
    HAVING COUNT(*) > 1
)
SELECT vp.id
  FROM fiber.varderingsprotokoll vp
  JOIN vp_dupe
    ON vp.avtal_id = vp_dupe.avtal_id;

DELETE from fiber.markledning ml
WHERE ml.varderingsprotokoll_id IN (SELECT vp_id FROM fiber.vp_dupe_ids);

DELETE from fiber.ovrig_intrangsersattning oi
WHERE oi.varderingsprotokoll_id IN (SELECT vp_id FROM fiber.vp_dupe_ids);

DELETE from fiber.punktersattning p
WHERE p.varderingsprotokoll_id IN (SELECT vp_id FROM fiber.vp_dupe_ids);

DELETE from fiber.varderingsprotokoll vp
WHERE vp.id IN (SELECT vp_id FROM fiber.vp_dupe_ids);

DROP TABLE fiber.vp_dupe_ids;






CREATE TABLE elnat.vp_dupe_ids(
    vp_id uuid NOT NULL
);

INSERT INTO elnat.vp_dupe_ids (vp_id)
WITH vp_dupe as (
    SELECT avtal_id
    FROM elnat.varderingsprotokoll vp
    GROUP BY vp.avtal_id
    HAVING COUNT(*) > 1
)
SELECT vp.id
  FROM elnat.varderingsprotokoll vp
  JOIN vp_dupe
    ON vp.avtal_id = vp_dupe.avtal_id;

DELETE from elnat.bilaga b
WHERE b.varderingsprotokoll_id IN (SELECT vp_id FROM elnat.vp_dupe_ids);

DELETE from elnat.hinder_akermark h
WHERE h.varderingsprotokoll_id IN (SELECT vp_id FROM elnat.vp_dupe_ids);

DELETE from elnat.ledning_skogsmark ls
WHERE ls.varderingsprotokoll_id IN (SELECT vp_id FROM elnat.vp_dupe_ids);

DELETE from elnat.markledning ml
WHERE ml.varderingsprotokoll_id IN (SELECT vp_id FROM elnat.vp_dupe_ids);

DELETE from elnat.ovrigt_intrang oi
WHERE oi.varderingsprotokoll_id IN (SELECT vp_id FROM elnat.vp_dupe_ids);

DELETE from elnat.punktersattning p
WHERE p.varderingsprotokoll_id IN (SELECT vp_id FROM elnat.vp_dupe_ids);

DELETE from elnat.ssb_skogsmark ssb
WHERE ssb.varderingsprotokoll_id IN (SELECT vp_id FROM elnat.vp_dupe_ids);

DELETE from elnat.ssb_vaganlaggning ssb
WHERE ssb.varderingsprotokoll_id IN (SELECT vp_id FROM elnat.vp_dupe_ids);

DELETE from elnat.varderingsprotokoll vp
WHERE vp.id IN (SELECT vp_id FROM elnat.vp_dupe_ids);






ALTER TABLE fiber.varderingsprotokoll
ADD CONSTRAINT uc_avtal UNIQUE (avtal_id); 

ALTER TABLE elnat.varderingsprotokoll
ADD CONSTRAINT uc_avtal UNIQUE (avtal_id); 