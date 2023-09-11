DROP VIEW larmdetaljer;

CREATE OR REPLACE VIEW larmdetaljer AS
SELECT larm.id,
       mo.namn matobjekt,
       mo.id matobjekt_id,
       dm.namn matningstyp,
       mt.id matningstyp_id,
       mo.fastighet,
       ln.namn larmniva,
       gv.gransvarde,
       gv.larm_till_anvandargrupp_id,
       larm.status
FROM larm
         INNER JOIN matobjekt mo on larm.matobjekt_id = mo.id
         INNER JOIN gransvarde gv on larm.gransvarde_id = gv.id
         INNER JOIN matningstyp mt on gv.matningstyp_id = mt.id
         INNER JOIN definition_matningstyp dm on mt.definition_matningstyp_id = dm.id
         INNER JOIN larmniva ln on gv.larmniva_id = ln.id;
