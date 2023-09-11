ALTER TABLE definition_matningstyp
    ADD CONSTRAINT uq_definition_matningstyp_matobjekt_typ_namn UNIQUE (matobjekt_typ, namn);
