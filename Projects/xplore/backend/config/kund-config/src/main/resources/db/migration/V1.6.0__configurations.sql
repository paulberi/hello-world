CREATE TABLE configuration(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    key TEXT NOT NULL,
    value TEXT NOT NULL,
    kund_id UUID NOT NULL,

    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id)
);