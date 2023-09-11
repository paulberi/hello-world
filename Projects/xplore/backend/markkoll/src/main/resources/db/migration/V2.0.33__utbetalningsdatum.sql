ALTER TABLE avtalspart
ADD utbetalningsdatum DATE;

CREATE TABLE log_utbetalningsdatum(
    id UUID NOT NULL,
    avtalspart_id UUID NOT NULL,
    utbetalningsdatum DATE NOT NULL,

    skapad_av TEXT NOT NULL,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_avtalspart FOREIGN KEY (avtalspart_id) REFERENCES avtalspart (id)
);