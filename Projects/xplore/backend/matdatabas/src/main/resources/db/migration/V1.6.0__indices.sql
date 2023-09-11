CREATE INDEX matning_avlast_datum_idx ON matning (avlast_datum);
CREATE INDEX matning_matningstyp_id_idx ON matning (matningstyp_id);
CREATE INDEX matobjekt_namn_idx on matobjekt (lower(namn));
