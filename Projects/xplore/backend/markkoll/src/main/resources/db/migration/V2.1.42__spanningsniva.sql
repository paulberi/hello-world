ALTER TABLE elnat.varderingsprotokoll
ADD spanningsniva TEXT DEFAULT '';

UPDATE elnat.varderingsprotokoll
   SET spanningsniva = avtal.spanningsniva
  FROM avtal
 WHERE elnat.varderingsprotokoll.avtal_id = avtal.id;

ALTER TABLE avtal
DROP COLUMN spanningsniva;