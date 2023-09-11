INSERT INTO config.kund
SELECT DISTINCT kund_id
           FROM projekt;

INSERT INTO acl_sid (principal, sid)
    VALUES
        (false, 'ROLE_admin');

CREATE OR REPLACE FUNCTION class_id(clazz TEXT)
RETURNS BIGINT
LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN CASE
        WHEN clazz = 'PROJEKT' THEN 1
        WHEN clazz = 'KUND' THEN 2
        WHEN clazz = 'DOKUMENTMALL' THEN 3
        WHEN clazz = 'FIL' THEN 4
        WHEN clazz = 'VERSION' THEN 5
        WHEN clazz = 'AVTAL' THEN 6
        WHEN clazz = 'AVTALSPART' THEN 7
        WHEN clazz = 'FIBER_VP' THEN 8
        WHEN clazz = 'FIBER_AKERSKOGSMARK' THEN 9
        WHEN clazz = 'FIBER_MARKLEDNING' THEN 10
        WHEN clazz = 'FIBER_OVRIGINTRANGSERSATTNING' THEN 11
        WHEN clazz = 'FIBER_PUNKTERSATTNING' THEN 12
        WHEN clazz = 'ELNAT_VP' THEN 13
        WHEN clazz = 'ELNAT_LEDNINGSKOGSMARK' THEN 14
        WHEN clazz = 'ELNAT_MARKLEDNING' THEN 15
        WHEN clazz = 'ELNAT_PUNKTERSATTNING' THEN 16
        WHEN clazz = 'ELNAT_ROTNETTO' THEN 17
        WHEN clazz = 'ELNAT_SSBSKOGSMARK' THEN 18
        WHEN clazz = 'ELNAT_SSBVAGANLAGGNING' THEN 19
        WHEN clazz = 'ELNAT_BILAGA' THEN 20
    END;
END
$$;

INSERT INTO acl_class (id, class, class_id_type)
    VALUES
        (class_id('PROJEKT'), 'se.metria.markkoll.entity.projekt.ProjektEntity', 'java.util.UUID'),
        (class_id('KUND'), 'se.metria.markkoll.entity.admin.KundEntity', 'java.lang.String'),
        (class_id('DOKUMENTMALL'), 'se.metria.markkoll.entity.DokumentmallEntity', 'java.util.UUID'),
        (class_id('FIL'), 'se.metria.markkoll.entity.FilEntity', 'java.util.UUID'),
        (class_id('VERSION'), 'se.metria.markkoll.entity.ImportVersionEntity', 'java.util.UUID'),
        (class_id('AVTAL'), 'se.metria.markkoll.entity.avtal.AvtalEntity', 'java.util.UUID'),
        (class_id('AVTALSPART'), 'se.metria.markkoll.entity.markagare.AvtalspartEntity', 'java.util.UUID'),
        (class_id('FIBER_VP'), 'se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity', 'java.util.UUID'),
        (class_id('FIBER_AKERSKOGSMARK'), 'se.metria.markkoll.entity.vardering.fiber.FiberIntrangAkerOchSkogsmarkEntity', 'java.util.UUID'),
        (class_id('FIBER_MARKLEDNING'), 'se.metria.markkoll.entity.vardering.fiber.FiberMarkledningEntity', 'java.util.UUID'),
        (class_id('FIBER_OVRIGINTRANGSERSATTNING'), 'se.metria.markkoll.entity.vardering.fiber.FiberOvrigIntrangsersattningEntity', 'java.util.UUID'),
        (class_id('FIBER_PUNKTERSATTNING'), 'se.metria.markkoll.entity.vardering.fiber.FiberPunktersattningEntity', 'java.util.UUID'),
        (class_id('ELNAT_VP'), 'se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity', 'java.util.UUID'),
        (class_id('ELNAT_LEDNINGSKOGSMARK'), 'se.metria.markkoll.entity.vardering.elnat.ElnatLedningSkogsmarkEntity', 'java.util.UUID'),
        (class_id('ELNAT_MARKLEDNING'), 'se.metria.markkoll.entity.vardering.elnat.ElnatMarkledningEntity', 'java.util.UUID'),
        (class_id('ELNAT_PUNKTERSATTNING'), 'se.metria.markkoll.entity.vardering.elnat.ElnatPunktersattningEntity', 'java.util.UUID'),
        (class_id('ELNAT_ROTNETTO'), 'se.metria.markkoll.entity.vardering.elnat.ElnatRotnettoEntity', 'java.util.UUID'),
        (class_id('ELNAT_SSBSKOGSMARK'), 'se.metria.markkoll.entity.vardering.elnat.ElnatSsbSkogsmarkEntity', 'java.util.UUID'),
        (class_id('ELNAT_SSBVAGANLAGGNING'), 'se.metria.markkoll.entity.vardering.elnat.ElnatSsbVaganlaggningEntity', 'java.util.UUID'),
        (class_id('ELNAT_BILAGA'), 'se.metria.markkoll.entity.vardering.bilaga.BilagaEntity', 'java.util.UUID')
        ;
        
ALTER SEQUENCE acl_class_id_seq RESTART WITH 21;

/* kunder */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
  WITH kunder AS (SELECT DISTINCT kund_id FROM projekt)
SELECT class_id('KUND'),
       kunder.kund_id,
       null,
       1,
       true
  FROM kunder;

/* dokumentmallar */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('DOKUMENTMALL'),
       dokumentmall.id,
       acl_object_identity.id,
       1,
       true
  FROM dokumentmall
  JOIN acl_object_identity
    ON dokumentmall.kund_id = acl_object_identity.object_id_identity;
    
/* filer */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('FIL'),
       dokumentmall.fil_id,
       acl_object_identity.id,
       1,
       true
  FROM dokumentmall
  JOIN acl_object_identity
    ON CAST(dokumentmall.id AS TEXT) = acl_object_identity.object_id_identity;
    
/* projekt */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('PROJEKT'),
       projekt.id,
       acl_object_identity.id,
       1,
       true
  FROM projekt
  JOIN acl_object_identity
    ON CAST(projekt.kund_id AS TEXT) = acl_object_identity.object_id_identity;

/* Importversioner */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('VERSION'),
       importversion.id,
       acl_object_identity.id,
       1,
       true
  FROM importversion
  JOIN acl_object_identity
    ON CAST(importversion.projekt_id AS TEXT) = acl_object_identity.object_id_identity;

/* Avtal */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('AVTAL'),
       avtal.id,
       acl_object_identity.id,
       1,
       true
  FROM avtal
  JOIN acl_object_identity
    ON CAST(avtal.projekt_id AS TEXT) = acl_object_identity.object_id_identity;

/* Avtalspart */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('AVTALSPART'),
       avtalspart.id,
       acl_object_identity.id,
       1,
       true
  FROM avtalspart
  JOIN acl_object_identity
    ON CAST(avtalspart.avtal_id AS TEXT) = acl_object_identity.object_id_identity;
    
/** FIBER **/
/* FiberVP */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('FIBER_VP'),
       fiber.varderingsprotokoll.id,
       acl_object_identity.id,
       1,
       true
 FROM fiber.varderingsprotokoll
 JOIN acl_object_identity
   ON CAST(fiber.varderingsprotokoll.avtal_id AS TEXT) = acl_object_identity.object_id_identity;

/* FiberVP Åker och Skogsmark*/
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('FIBER_AKERSKOGSMARK'),
       fiber.aker_och_skogsmark.id,
       acl_object_identity.id,
       1,
       true
 FROM fiber.aker_och_skogsmark
 JOIN acl_object_identity
   ON CAST(fiber.aker_och_skogsmark.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* FiberVP Markledning */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('FIBER_MARKLEDNING'),
       fiber.markledning.id,
       acl_object_identity.id,
       1,
       true
 FROM fiber.markledning
 JOIN acl_object_identity
   ON CAST(fiber.markledning.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* FiberVP Övrig intrångsersättning */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('FIBER_OVRIGINTRANGSERSATTNING'),
       fiber.ovrig_intrangsersattning.id,
       acl_object_identity.id,
       1,
       true
 FROM fiber.ovrig_intrangsersattning
 JOIN acl_object_identity
   ON CAST(fiber.ovrig_intrangsersattning.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* FiberVP Punktersättning */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('FIBER_PUNKTERSATTNING'),
       fiber.punktersattning.id,
       acl_object_identity.id,
       1,
       true
 FROM fiber.punktersattning
 JOIN acl_object_identity
   ON CAST(fiber.punktersattning.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* ELNÄT */
/* ElnätVP */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('ELNAT_VP'),
       elnat.varderingsprotokoll.id,
       acl_object_identity.id,
       1,
       true
 FROM elnat.varderingsprotokoll
 JOIN acl_object_identity
   ON CAST(elnat.varderingsprotokoll.avtal_id AS TEXT) = acl_object_identity.object_id_identity;

/* ElnätVP Ledning skogsmark*/
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('ELNAT_LEDNINGSKOGSMARK'),
       elnat.ledning_skogsmark.id,
       acl_object_identity.id,
       1,
       true
 FROM elnat.ledning_skogsmark
 JOIN acl_object_identity
   ON CAST(elnat.ledning_skogsmark.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* ElnätVP Markledning */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('ELNAT_MARKLEDNING'),
       elnat.markledning.id,
       acl_object_identity.id,
       1,
       true
 FROM elnat.markledning
 JOIN acl_object_identity
   ON CAST(elnat.markledning.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* ElnätVP Punktersättning */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('ELNAT_PUNKTERSATTNING'),
       elnat.punktersattning.id,
       acl_object_identity.id,
       1,
       true
 FROM elnat.punktersattning
 JOIN acl_object_identity
   ON CAST(elnat.punktersattning.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* ElnätVP Rotnetto */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('ELNAT_ROTNETTO'),
       elnat.rotnetto.id,
       acl_object_identity.id,
       1,
       true
 FROM elnat.rotnetto
 JOIN acl_object_identity
   ON CAST(elnat.rotnetto.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* ElnätVP SsbSkogsmark */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('ELNAT_SSBSKOGSMARK'),
       elnat.ssb_skogsmark.id,
       acl_object_identity.id,
       1,
       true
 FROM elnat.ssb_skogsmark
 JOIN acl_object_identity
   ON CAST(elnat.ssb_skogsmark.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* ElnätVP SsbVäganläggning */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('ELNAT_SSBVAGANLAGGNING'),
       elnat.ssb_vaganlaggning.id,
       acl_object_identity.id,
       1,
       true
 FROM elnat.ssb_vaganlaggning
 JOIN acl_object_identity
   ON CAST(elnat.ssb_vaganlaggning.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;

/* ElnätVP Bilaga */
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT class_id('ELNAT_BILAGA'),
       elnat.bilaga.id,
       acl_object_identity.id,
       1,
       true
 FROM elnat.bilaga
 JOIN acl_object_identity
   ON CAST(elnat.bilaga.varderingsprotokoll_id AS TEXT) = acl_object_identity.object_id_identity;
   
/* Ge globala admins fulla rättigheter till alla redan existerande domänobjekt, för att ge oss möjlighet
att "initialisera" dom */
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT id, 0, 1, 1, true, false, false
  FROM acl_object_identity
 WHERE acl_object_identity.object_id_class = 2;

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT id, 1, 1, 2, true, false, false
  FROM acl_object_identity
 WHERE acl_object_identity.object_id_class = 2;

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT id, 2, 1, 4, true, false, false
  FROM acl_object_identity
 WHERE acl_object_identity.object_id_class = 2;

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT id, 3, 1, 8, true, false, false
  FROM acl_object_identity
 WHERE acl_object_identity.object_id_class = 2;

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT id, 4, 1, 16, true, false, false
  FROM acl_object_identity
 WHERE acl_object_identity.object_id_class = 2;
  
DROP FUNCTION class_id;