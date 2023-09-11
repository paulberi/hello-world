-- Add berakning_referensnivå, max_pejlbart_djup, matobjekt_typ, pos_n, pos_e to to the view

drop view matningstyp_matobjekt;

create or replace view matningstyp_matobjekt as
SELECT mt.id                 AS matningstyp_id,
       dmt.namn              AS matningstyp_namn,
       mt.aktiv              AS matningstyp_aktiv,
       mt.berakning_referensniva        AS matningstyp_berakning_referensniva,
       mt.max_pejlbart_djup             AS matningstyp_max_pejlbart_djup,
       mt.matansvarig_anvandargrupp_id,
       mo.id                 AS matobjekt_id,
       mo.namn               AS matobjekt_namn,
       mot.namn              AS matobjekt_typ,
       mo.typ                AS matobjekt_typ_id,
       mo.fastighet          AS matobjekt_fastighet,
       mo.lage               AS matobjekt_lage,
       mo.aktiv              AS matobjekt_aktiv,
       mo.pos_n              AS matobjekt_pos_n,
       mo.pos_e              AS matobjekt_pos_e,
       mo.aktiv AND mt.aktiv AS aktiv
FROM matningstyp mt
         JOIN matobjekt mo ON mo.id = mt.matobjekt_id
         JOIN matobjekt_typ mot ON mo.typ = mot.id
         JOIN definition_matningstyp dmt ON mt.definition_matningstyp_id = dmt.id;


