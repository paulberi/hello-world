CREATE TABLE ledningsagare(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    kund_id TEXT NOT NULL,
    namn TEXT NOT NULL,
    
    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES config.kund (id)
);

INSERT INTO ledningsagare (namn, kund_id)
WITH ledningsagare_table AS (
    SELECT DISTINCT elprojekt.ledningsagare AS ledningsagare, projekt.kund_id AS kund_id
      FROM projekt
      JOIN elnat.projekt elprojekt
        ON elprojekt.id = projekt.id
     WHERE ledningsagare <> ''
)
SELECT ledningsagare, kund_id
  FROM ledningsagare_table;

INSERT INTO ledningsagare (namn, kund_id)
WITH ledningsagare_table AS (
    SELECT DISTINCT fiberprojekt.ledningsagare AS ledningsagare, projekt.kund_id AS kund_id
      FROM projekt
      JOIN fiber.projekt fiberprojekt
        ON fiberprojekt.id = projekt.id
     WHERE ledningsagare <> ''
)
     SELECT ledningsagare, kund_id
       FROM ledningsagare_table;

UPDATE elnat.projekt
SET ledningsagare = NULL
WHERE ledningsagare = '';

UPDATE fiber.projekt
SET ledningsagare = NULL
WHERE ledningsagare = '';