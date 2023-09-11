-- En vy för att bekvämt ta fram det n senaste mätningarna som ingår i en viss mätrunda
-- "rank" anger ordningen av mätningar inom en viss mätningstyp, så ett villkor på t.ex.
-- WHERE rank <= 3 ger de tre senaste mätningarna.
CREATE OR REPLACE VIEW matrunda_matningar AS
SELECT mrmt.matrunda_id,
       mrmt.matningstyp_id,
       mrmt.ordning,
       m.avlast_varde,
       m.avlast_datum,
       m.status,
       m.felkod,
       m.rank,
       dm.storhet,
       dm.namn as matningstyp,
       mt.enhet,
       mt.fixpunkt,
       mt.max_pejlbart_djup,
       mo.id as matobjekt_id,
       mo.namn as matobjekt,
       mo.bifogad_bild_id,
       mo.lage,
       mo.fastighet
FROM matrunda_matningstyp mrmt
         LEFT JOIN (
    SELECT id,
           matningstyp_id,
           avlast_varde,
           avlast_datum,
           status,
           felkod,
           rank() OVER (partition by matningstyp_id ORDER BY avlast_datum DESC)
    FROM matning
) m ON mrmt.matningstyp_id = m.matningstyp_id
         INNER JOIN
     matningstyp mt ON mrmt.matningstyp_id = mt.id
         INNER JOIN
     definition_matningstyp dm ON mt.definition_matningstyp_id = dm.id
         INNER JOIN
     matobjekt mo ON mt.matobjekt_id = mo.id
order by ordning, m.rank;
