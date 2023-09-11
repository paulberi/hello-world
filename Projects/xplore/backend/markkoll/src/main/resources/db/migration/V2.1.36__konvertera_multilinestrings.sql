DELETE FROM omradesintrang;

DELETE FROM fastighetsintrang;

UPDATE intrang
   SET geom = ST_LineMerge(geom)
 WHERE ST_GeometryType(geom) = 'ST_MultiLineString';

INSERT INTO omradesintrang (fastighet_id, omrade_nr, type, subtype, spanningsniva, status, version_id, geom, littera, avtalstyp)
     SELECT fastighet_omrade.fastighet_id,
            fastighet_omrade.omrade_nr,
            intrang.type,
            intrang.subtype,
            intrang.spanningsniva,
            intrang.status,
            intrang.version_id,
            ST_Intersection(fastighet_omrade.geom, intrang.geom) AS geom,
            intrang.littera,
            intrang.avtalstyp
       FROM intrang
       JOIN fastighet_omrade
         ON ST_Intersects(fastighet_omrade.geom, intrang.geom);

INSERT INTO fastighetsintrang (fastighet_id, type, subtype, status, version_id, geom, littera, avtalstyp)
     SELECT fastighet_omrade.fastighet_id,
            intrang.type,
            intrang.subtype,
            intrang.status,
            intrang.version_id,
            ST_Intersection(fastighet_omrade.geom, st_union(intrang.geom)) AS geom,
            intrang.littera,
            intrang.avtalstyp
       FROM intrang
       JOIN fastighet_omrade
         ON ST_Intersects(fastighet_omrade.geom, intrang.geom)
   GROUP BY fastighet_omrade.fastighet_id,
            intrang.id,
            intrang.type,
            intrang.subtype,
            intrang.spanningsniva,
            intrang.status,
            intrang.version_id,
            fastighet_omrade.geom,
            intrang.littera,
            intrang.avtalstyp;