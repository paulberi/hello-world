CREATE TABLE elnat.bilaga_typ(
    typ TEXT PRIMARY KEY
);

INSERT INTO elnat.bilaga_typ (typ)
    VALUES
        ('AKERNORM_74'),
        ('ENSTAKA_TRAD'),
        ('ROTNETTO'),
        ('OVRIGT_INTRANG');
        
CREATE TABLE elnat.bilaga(
    id UUID PRIMARY KEY,
    varderingsprotokoll_id UUID NOT NULL,
    fil_id UUID NOT NULL,
    bilaga_typ TEXT NOT NULL,
    
    CONSTRAINT fk_varderingsprotokoll FOREIGN KEY (varderingsprotokoll_id) REFERENCES elnat.varderingsprotokoll (id),
    CONSTRAINT fk_fil FOREIGN KEY (fil_id) REFERENCES fil(id),
    CONSTRAINT fk_bilaga_typ FOREIGN KEY (bilaga_typ) REFERENCES elnat.bilaga_typ (typ)
);