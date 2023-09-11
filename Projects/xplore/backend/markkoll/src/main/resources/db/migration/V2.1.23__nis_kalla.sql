CREATE TABLE markkoll.niskalla(
    kund_id TEXT PRIMARY KEY,
    dp_com BOOLEAN NOT NULL DEFAULT FALSE,
    dp_power BOOLEAN NOT NULL DEFAULT FALSE,
    trimble BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES config.kund (id)
);