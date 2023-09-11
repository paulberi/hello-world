CREATE TABLE beredare(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    projekt_id UUID UNIQUE,
    namn TEXT NOT NULL,
    telefonnummer TEXT,
    adress TEXT,
    postnummer TEXT,
    ort TEXT,

    CONSTRAINT fk_projekt_id FOREIGN KEY (projekt_id) REFERENCES projekt (id)
);

INSERT INTO beredare (projekt_id, namn)
SELECT projekt.id, ''
  FROM projekt;