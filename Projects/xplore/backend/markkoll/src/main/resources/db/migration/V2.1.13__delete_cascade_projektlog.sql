ALTER TABLE IF EXISTS markkoll.projektlogg DROP CONSTRAINT IF EXISTS fk_projekt;

ALTER TABLE IF EXISTS markkoll.projektlogg
    ADD FOREIGN KEY (projekt_id)
    REFERENCES markkoll.projekt (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE CASCADE
    NOT VALID;
