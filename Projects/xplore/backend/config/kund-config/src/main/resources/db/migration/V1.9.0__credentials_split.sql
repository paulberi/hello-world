CREATE TABLE fastighetsok(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    kund_id TEXT NOT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    kundmarke TEXT NOT NULL DEFAULT 'markkoll',
    
    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id),
    CONSTRAINT uc_fsok_kund UNIQUE (kund_id)
);

CREATE TABLE metria_maps(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    kund_id TEXT NOT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    
    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id),
    CONSTRAINT uc_mmaps_kund UNIQUE (kund_id)
);

INSERT INTO fastighetsok (kund_id, username, password)
     SELECT kund_id,
            username,
            password
       FROM credentials
      WHERE system = 'FASTIGHETSOK';

INSERT INTO metria_maps (kund_id, username, password)
     SELECT kund_id,
            username,
            password
       FROM credentials
      WHERE system = 'METRIA_MAPS';

DROP TABLE credentials;