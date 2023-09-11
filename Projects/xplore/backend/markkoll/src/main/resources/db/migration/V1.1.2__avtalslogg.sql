CREATE TABLE log_avtalsstatus
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    avtalspart_id UUID NOT NULL,
    avtalsstatus TEXT NOT NULL,
    
    skapad_av TEXT NOT NULL,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_avtalspart FOREIGN KEY (avtalspart_id) REFERENCES avtalspart (id),
    CONSTRAINT fk_avtalsstatus FOREIGN KEY (avtalsstatus) REFERENCES avtalsstatus (status)
);

CREATE TABLE log_geometristatus
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    avtal_id UUID NOT NULL,
    geometristatus TEXT NOT NULL,
    
    skapad_av TEXT NOT NULL,
    skapad_datum TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_avtal FOREIGN KEY (avtal_id) REFERENCES avtal (id),
    CONSTRAINT fk_geometristatus FOREIGN KEY (geometristatus) REFERENCES geometristatus (status)
);