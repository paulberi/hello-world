CREATE TABLE produkt(
    namn TEXT PRIMARY KEY
);

INSERT INTO produkt (namn)
    VALUES
        ('MARKKOLL'),
        ('MILJOKOLL');

CREATE TABLE roll(
    namn TEXT PRIMARY KEY
);

INSERT INTO roll (namn)
    VALUES
        ('KUNDADMIN'),
        ('ANVANDARE'),
        ('MARKHANDLAGGARE');

CREATE TABLE permissions(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    user_id TEXT NOT NULL,
    roll TEXT NOT NULL,
    produkt TEXT NOT NULL,
    kund_id UUID NOT NULL,
    
    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id),
    CONSTRAINT fk_produkt FOREIGN KEY (produkt) REFERENCES produkt (namn),
    CONSTRAINT fk_roll FOREIGN KEY (roll) REFERENCES roll (namn)
);