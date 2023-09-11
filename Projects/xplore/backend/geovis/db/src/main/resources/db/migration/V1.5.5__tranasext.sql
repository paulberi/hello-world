
DROP TABLE IF EXISTS tranasext."karta_lediga_tomter_flerfamiljshus" CASCADE;

CREATE TABLE tranasext."karta_lediga_tomter_flerfamiljshus" (LIKE  tranasext."karta_skatepark" INCLUDING ALL);
ALTER TABLE tranasext."karta_lediga_tomter_flerfamiljshus" ALTER id DROP DEFAULT;
CREATE SEQUENCE tranasext."karta_lediga_tomter_flerfamiljshus_id_seq";
INSERT INTO tranasext."karta_lediga_tomter_flerfamiljshus" SELECT * FROM tranasext."karta_skatepark";
SELECT setval('"tranasext"."karta_lediga_tomter_flerfamiljshus_id_seq"', (SELECT max(id) FROM "tranasext"."karta_lediga_tomter_flerfamiljshus"), true);
ALTER TABLE tranasext."karta_lediga_tomter_flerfamiljshus" ALTER id SET DEFAULT nextval('"tranasext"."karta_lediga_tomter_flerfamiljshus_id_seq"');
ALTER SEQUENCE tranasext."karta_lediga_tomter_flerfamiljshus_id_seq" OWNED BY tranasext."karta_lediga_tomter_flerfamiljshus".id;

ALTER TABLE tranasext."karta_lediga_tomter_flerfamiljshus" OWNER TO "xplore-admin";