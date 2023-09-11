CREATE TABLE avtalsjobb_avtal(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    avtalsjobb_id UUID NOT NULL,
    avtal_id UUID NOT NULL,
    
    CONSTRAINT fk_avtalsjobb_id FOREIGN KEY (avtalsjobb_id) REFERENCES avtalsjobb (id),
    CONSTRAINT fk_avtal_id FOREIGN KEY (avtal_id) REFERENCES avtal (id),
    CONSTRAINT uq_avtalsjobb_avtal UNIQUE(avtalsjobb_id, avtal_id)
);

ALTER TABLE avtalsjobb
DROP projekt_id;