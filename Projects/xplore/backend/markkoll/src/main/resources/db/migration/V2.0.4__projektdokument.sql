CREATE TABLE dokumenttyp
(
    typ TEXT PRIMARY KEY
);

CREATE TABLE dokument (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    namn TEXT NOT NULL,
    path TEXT,
    kund_id TEXT NOT NULL,
    dokumenttyp TEXT NOT NULL,
    skapad_av VARCHAR NOT NULL,
    skapad_datum TIMESTAMP NOT NULL,
    selected BOOLEAN NOT NULL,

    CONSTRAINT fk_dokumenttyp FOREIGN KEY (dokumenttyp) REFERENCES dokumenttyp (typ)
);

INSERT INTO dokumenttyp (typ)
    VALUES ('MARKUPPLATELSEAVTAL'),
           ('INFOBREV');