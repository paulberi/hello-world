CREATE TABLE kallfil(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    fil_id UUID NOT NULL,
    indatatyp TEXT NOT NULL,

    CONSTRAINT fk_fil FOREIGN KEY (fil_id) REFERENCES fil (id),
    CONSTRAINT fk_indata_typ FOREIGN KEY (indatatyp) REFERENCES indatatyp (typ)
);

CREATE TABLE indata(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    
    geom GEOMETRY(Geometry, 3006) NOT NULL,
    feature_properties JSONB NOT NULL DEFAULT '{}',
    layer TEXT NOT NULL,
    
    kallfil_id UUID NOT NULL,
    projekt_id UUID NOT NULL,

    CONSTRAINT fk_kallfil FOREIGN KEY (kallfil_id) REFERENCES kallfil(id),
    CONSTRAINT fk_projekt FOREIGN KEY (projekt_id) REFERENCES projekt (id)
);

ALTER TABLE intrang
DROP COLUMN indatatyp;

ALTER TABLE intrang
DROP COLUMN feature_properties;