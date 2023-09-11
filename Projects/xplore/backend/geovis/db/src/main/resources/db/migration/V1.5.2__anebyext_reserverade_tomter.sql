CREATE TABLE "anebyext"."karta_reserverade_tomter" (LIKE "anebyext"."karta_detaljplaner-gallande" INCLUDING ALL);
ALTER TABLE "anebyext"."karta_reserverade_tomter" ALTER id DROP DEFAULT;
CREATE SEQUENCE "anebyext"."karta_reserverade_tomter_id_seq";
INSERT INTO "anebyext"."karta_reserverade_tomter" SELECT * FROM "anebyext"."karta_detaljplaner-gallande";
SELECT setval('"anebyext"."karta_reserverade_tomter_id_seq"', (SELECT max(id) FROM "anebyext"."karta_reserverade_tomter"), true);
ALTER TABLE "anebyext"."karta_reserverade_tomter" ALTER id SET DEFAULT nextval('"anebyext"."karta_reserverade_tomter_id_seq"');
ALTER SEQUENCE "anebyext"."karta_reserverade_tomter_id_seq" OWNED BY "anebyext"."karta_reserverade_tomter".id;

ALTER TABLE anebyext."karta_reserverade_tomter" OWNER TO geovis;

TRUNCATE TABLE "anebyext"."karta_reserverade_tomter";
