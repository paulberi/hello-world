CREATE TABLE elnat.hinder_akermark(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    varderingsprotokoll_id UUID NOT NULL,
    beskrivning TEXT DEFAULT '',
    ersattning INTEGER DEFAULT 0,
    
    CONSTRAINT fk_varderingsprotokoll_id FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id)
);

ALTER TABLE elnat.ledning_skogsmark
DROP COLUMN skapad_av;
ALTER TABLE elnat.ledning_skogsmark
DROP COLUMN skapad_datum;

ALTER TABLE elnat.markledning
DROP COLUMN skapad_av;
ALTER TABLE elnat.markledning
DROP COLUMN skapad_datum;

ALTER TABLE elnat.ovrigt_intrang
DROP COLUMN skapad_av;
ALTER TABLE elnat.ovrigt_intrang
DROP COLUMN skapad_datum;

ALTER TABLE elnat.punktersattning
DROP COLUMN skapad_av;
ALTER TABLE elnat.punktersattning
DROP COLUMN skapad_datum;

ALTER TABLE elnat.ssb_skogsmark
DROP COLUMN skapad_av;
ALTER TABLE elnat.ssb_skogsmark
DROP COLUMN skapad_datum;

ALTER TABLE elnat.ssb_vaganlaggning
DROP COLUMN skapad_av;
ALTER TABLE elnat.ssb_vaganlaggning
DROP COLUMN skapad_datum;

DELETE
  FROM acl_object_identity 
 USING acl_class
 WHERE acl_object_identity.object_id_class = acl_class.id
   AND (acl_class.class = 'se.metria.markkoll.entity.vardering.elnat.ElnatLedningSkogsmarkEntity'
    OR acl_class.class = 'se.metria.markkoll.entity.vardering.elnat.ElnatMarkledningEntity'
    OR acl_class.class = 'se.metria.markkoll.entity.vardering.elnat.ElnatPunktersattningEntity'
    OR acl_class.class = 'se.metria.markkoll.entity.vardering.elnat.ElnatRotnettoEntity'
    OR acl_class.class = 'se.metria.markkoll.entity.vardering.elnat.ElnatSsbSkogsmarkEntity'
    OR acl_class.class = 'se.metria.markkoll.entity.vardering.elnat.ElnatSsbVaganlaggningEntity');