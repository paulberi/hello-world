CREATE SCHEMA IF NOT EXISTS config;

CREATE TABLE config.kund(
    id TEXT PRIMARY KEY
);

CREATE TABLE config.markkoll_user(
    id TEXT PRIMARY KEY,
    fornamn TEXT NOT NULL,
    efternamn TEXT NOT NULL,
    email TEXT NOT NULL,
    kund_id TEXT NOT NULL,

    CONSTRAINT fk_kund FOREIGN KEY (kund_id) REFERENCES config.kund (id)
);

CREATE TABLE config.role_type(
    type TEXT PRIMARY KEY
);

CREATE TABLE config.user_role(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    user_id TEXT NOT NULL,
    role_type TEXT NOT NULL,
    object_id TEXT NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES config.markkoll_user (id),
    CONSTRAINT fk_role_type FOREIGN KEY (role_type) REFERENCES config.role_type (type),
    CONSTRAINT uc_user_role UNIQUE (user_id, role_type, object_id)
);

INSERT INTO config.role_type
    VALUES
        ('KUNDADMIN'),
        ('PROJEKTADMIN'),
        ('PROJEKTHANDLAGGARE');
