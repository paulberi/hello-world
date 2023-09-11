CREATE TABLE "tranasext"."karta_gymnasium" (LIKE "tranasext"."karta_systembolaget" INCLUDING ALL);
ALTER TABLE "tranasext"."karta_gymnasium" ALTER id DROP DEFAULT;
CREATE SEQUENCE "tranasext"."karta_gymnasium_id_seq";
INSERT INTO "tranasext"."karta_gymnasium" SELECT * FROM "tranasext"."karta_systembolaget";
SELECT setval('"tranasext"."karta_gymnasium_id_seq"', (SELECT max(id) FROM "tranasext"."karta_gymnasium"), true);
ALTER TABLE "tranasext"."karta_gymnasium" ALTER id SET DEFAULT nextval('"tranasext"."karta_gymnasium_id_seq"');
ALTER SEQUENCE "tranasext"."karta_gymnasium_id_seq" OWNED BY "tranasext"."karta_gymnasium".id;

ALTER TABLE tranasext."karta_gymnasium" OWNER TO "xplore-admin";

TRUNCATE TABLE "tranasext"."karta_gymnasium";


CREATE TABLE "tranasext"."karta_vuxenutbildning" (LIKE "tranasext"."karta_systembolaget" INCLUDING ALL);
ALTER TABLE "tranasext"."karta_vuxenutbildning" ALTER id DROP DEFAULT;
CREATE SEQUENCE "tranasext"."karta_vuxenutbildning_id_seq";
INSERT INTO "tranasext"."karta_vuxenutbildning" SELECT * FROM "tranasext"."karta_systembolaget";
SELECT setval('"tranasext"."karta_vuxenutbildning_id_seq"', (SELECT max(id) FROM "tranasext"."karta_vuxenutbildning"), true);
ALTER TABLE "tranasext"."karta_vuxenutbildning" ALTER id SET DEFAULT nextval('"tranasext"."karta_vuxenutbildning_id_seq"');
ALTER SEQUENCE "tranasext"."karta_vuxenutbildning_id_seq" OWNED BY "tranasext"."karta_vuxenutbildning".id;

ALTER TABLE tranasext."karta_vuxenutbildning" OWNER TO "xplore-admin";

TRUNCATE TABLE "tranasext"."karta_vuxenutbildning";


CREATE TABLE "tranasext"."karta_traffpunkt_seniorer" (LIKE "tranasext"."karta_systembolaget" INCLUDING ALL);
ALTER TABLE "tranasext"."karta_traffpunkt_seniorer" ALTER id DROP DEFAULT;
CREATE SEQUENCE "tranasext"."karta_traffpunkt_seniorer_id_seq";
INSERT INTO "tranasext"."karta_traffpunkt_seniorer" SELECT * FROM "tranasext"."karta_systembolaget";
SELECT setval('"tranasext"."karta_traffpunkt_seniorer_id_seq"', (SELECT max(id) FROM "tranasext"."karta_traffpunkt_seniorer"), true);
ALTER TABLE "tranasext"."karta_traffpunkt_seniorer" ALTER id SET DEFAULT nextval('"tranasext"."karta_traffpunkt_seniorer_id_seq"');
ALTER SEQUENCE "tranasext"."karta_traffpunkt_seniorer_id_seq" OWNED BY "tranasext"."karta_traffpunkt_seniorer".id;

ALTER TABLE tranasext."karta_traffpunkt_seniorer" OWNER TO "xplore-admin";

TRUNCATE TABLE "tranasext"."karta_traffpunkt_seniorer";


CREATE TABLE "tranasext"."karta_lediga_tomter_flerfamiljshus" (LIKE "tranasext"."karta_naturreservat" INCLUDING ALL);
ALTER TABLE "tranasext"."karta_lediga_tomter_flerfamiljshus" ALTER id DROP DEFAULT;
CREATE SEQUENCE "tranasext"."karta_lediga_tomter_flerfamiljshus_id_seq";
INSERT INTO "tranasext"."karta_lediga_tomter_flerfamiljshus" SELECT * FROM "tranasext"."karta_naturreservat";
SELECT setval('"tranasext"."karta_lediga_tomter_flerfamiljshus_id_seq"', (SELECT max(id) FROM "tranasext"."karta_lediga_tomter_flerfamiljshus"), true);
ALTER TABLE "tranasext"."karta_lediga_tomter_flerfamiljshus" ALTER id SET DEFAULT nextval('"tranasext"."karta_lediga_tomter_flerfamiljshus_id_seq"');
ALTER SEQUENCE "tranasext"."karta_lediga_tomter_flerfamiljshus_id_seq" OWNED BY "tranasext"."karta_lediga_tomter_flerfamiljshus".id;

ALTER TABLE tranasext."karta_lediga_tomter_flerfamiljshus" OWNER TO "xplore-admin";

TRUNCATE TABLE "tranasext"."karta_lediga_tomter_flerfamiljshus";
