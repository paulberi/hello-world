CREATE TABLE config.app_login_info (
    realm varchar NOT NULL,
    client_id varchar NOT NULL,
    json jsonb NOT NULL,
    PRIMARY KEY (realm, client_id)
);

ALTER TABLE config.app_login_info OWNER TO xplore;

GRANT SELECT ON TABLE config.app_login_info TO "xplore-read";
