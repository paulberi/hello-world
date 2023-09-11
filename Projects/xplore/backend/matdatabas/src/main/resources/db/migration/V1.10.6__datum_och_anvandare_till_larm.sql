ALTER TABLE larm
    ADD COLUMN kvitterad_datum timestamp without time zone,
    ADD COLUMN kvitterad_av_id integer,
    ADD CONSTRAINT fk_larm_anvandare FOREIGN KEY (kvitterad_av_id) REFERENCES anvandare (id);
