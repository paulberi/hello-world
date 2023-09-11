ALTER TABLE systemlogg
    ADD CONSTRAINT fk_systemlogg_anvandare FOREIGN KEY (loggat_av_id)
        REFERENCES anvandare (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

