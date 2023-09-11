ALTER TABLE omradesintrang
ADD spanningsniva DOUBLE PRECISION;

UPDATE omradesintrang
   SET spanningsniva = intrang.spanningsniva
  FROM intrang
 WHERE ST_Intersects(omradesintrang.geom, intrang.geom);