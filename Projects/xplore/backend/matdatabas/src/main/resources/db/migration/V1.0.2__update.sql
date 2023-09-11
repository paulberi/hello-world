CREATE OR REPLACE VIEW matansvar AS
SELECT mt.id, mo.namn matobjekt, mo.id matobjekt_id, dm.namn matningstyp, mo.fastighet, mt.matansvarig_anvandargrupp_id
FROM matobjekt mo
         INNER JOIN matningstyp mt ON mo.id = mt.matobjekt_id
         INNER JOIN definition_matningstyp dm ON mt.definition_matningstyp_id = dm.id;


CREATE OR REPLACE VIEW larmdetaljer AS
SELECT larm.id,
       mo.namn matobjekt,
       mo.id matobjekt_id,
       dm.namn matningstyp,
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


CREATE OR REPLACE VIEW aktiva_matningar AS
SELECT
    mo.id matobjekt_id,
    mt.matansvarig_anvandargrupp_id,
    m.*
FROM matning m
         INNER JOIN matningstyp mt on m.matningstyp_id = mt.id
         INNER JOIN matobjekt mo on mt.matobjekt_id = mo.id
WHERE mt.aktiv = true AND mo.aktiv = true;
