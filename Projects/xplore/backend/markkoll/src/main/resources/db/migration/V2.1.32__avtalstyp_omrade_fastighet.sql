ALTER TABLE omradesintrang
ADD COLUMN avtalstyp TEXT NOT NULL DEFAULT 'INTRANG';

ALTER TABLE omradesintrang
ADD CONSTRAINT fk_avtalstyp FOREIGN KEY (avtalstyp) REFERENCES avtal_types (name);

ALTER TABLE fastighetsintrang
ADD COLUMN avtalstyp TEXT NOT NULL DEFAULT 'INTRANG';

ALTER TABLE fastighetsintrang
ADD CONSTRAINT fk_avtalstyp FOREIGN KEY (avtalstyp) REFERENCES avtal_types (name);

DELETE FROM omradesintrang;

INSERT INTO omradesintrang (fastighet_id, omrade_nr, type, subtype, spanningsniva, status, version_id, geom, littera, avtalstyp)
SELECT fastighet_omrade.fastighet_id,
       fastighet_omrade.omrade_nr,
       intrang.type,
       intrang.subtype,
       intrang.spanningsniva,
       intrang.status,
       intrang.version_id,
       st_intersection(fastighet_omrade.geom, intrang.geom) AS geom,
       intrang.littera,
       intrang.avtalstyp
  FROM intrang
  JOIN fastighet_omrade
    ON st_intersects(fastighet_omrade.geom, intrang.geom);
    
DELETE FROM fastighetsintrang;

INSERT INTO fastighetsintrang (fastighet_id, type, subtype, status, version_id, geom, littera, avtalstyp)
SELECT   fastighet_omrade.fastighet_id,
         intrang.type,
         intrang.subtype,
         intrang.status,
         intrang.version_id,
         st_intersection(fastighet_omrade.geom, st_union(intrang.geom)) AS geom,
         intrang.littera,
         intrang.avtalstyp
  FROM   intrang
  JOIN   fastighet_omrade
    ON   st_intersects(fastighet_omrade.geom, intrang.geom)
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