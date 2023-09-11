CREATE TABLE abonnemang_typ(
    typ TEXT PRIMARY KEY
);

INSERT INTO abonnemang_typ (typ)
    VALUES
        ('GULD'),
        ('SILVER');

CREATE TABLE abonnemang(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    kund_id UUID NOT NULL,
    produkt TEXT NOT NULL,
    typ TEXT NOT NULL,

    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id),
    CONSTRAINT fk_produkt FOREIGN KEY (produkt) REFERENCES produkt (namn),
    CONSTRAINT fk_typ FOREIGN KEY (typ) REFERENCES abonnemang_typ (typ)
);