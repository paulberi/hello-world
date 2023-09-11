CREATE TABLE system
(
    namn TEXT PRIMARY KEY 
);

INSERT INTO system (namn)
    VALUES
        ('METRIA_MAPS'),
        ('FASTIGHETSOK');
        
CREATE TABLE credentials
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    kund_id UUID NOT NULL,
    system TEXT NOT NULL,
    username TEXT,
    password TEXT,
    
    CONSTRAINT uq_kund_system UNIQUE(kund_id, system),
    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES kund (id),
    CONSTRAINT fk_system FOREIGN KEY (system) REFERENCES system (namn)
);