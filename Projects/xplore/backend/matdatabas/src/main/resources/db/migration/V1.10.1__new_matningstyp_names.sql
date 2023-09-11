ALTER TABLE definition_matningstyp
    DROP CONSTRAINT uq_definition_matningstyp_matobjekt_typ_namn;

ALTER TABLE definition_matningstyp
    ADD CONSTRAINT uq_definition_matningstyp_matobjekt_typ_namn_storhet UNIQUE (matobjekt_typ, namn, storhet);
