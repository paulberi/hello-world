CREATE TABLE infobrevsjobb
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status TEXT NOT NULL DEFAULT 'NONE',
    path TEXT,
    dokument_id UUID,
    projekt_id UUID NOT NULL,
    total INTEGER NOT NULL,
    generated INTEGER NOT NULL,

    CONSTRAINT fk_status FOREIGN KEY (status) REFERENCES avtalsjobb_status (status),
    CONSTRAINT fk_dokument FOREIGN KEY (dokument_id) REFERENCES dokument (id),
    CONSTRAINT fk_projekt FOREIGN KEY (projekt_id) REFERENCES projekt (id)
);

CREATE TABLE infobrevsjobb_fastighet(
     id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
     infobrevsjobb_id UUID NOT NULL,
     fastighet_id UUID NOT NULL,

     CONSTRAINT fk_infobrevsjobb_id FOREIGN KEY (infobrevsjobb_id) REFERENCES infobrevsjobb (id),
     CONSTRAINT fk_fastighet_id FOREIGN KEY (fastighet_id) REFERENCES fastighet (id),
     CONSTRAINT uq_infobrevsjobb_fastighet UNIQUE(infobrevsjobb_id, fastighet_id)
);
