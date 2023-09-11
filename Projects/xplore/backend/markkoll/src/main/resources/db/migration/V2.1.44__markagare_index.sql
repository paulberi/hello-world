CREATE INDEX idx_markagare
ON avtalspart (markagare_id);

CREATE INDEX idx_person
ON markagare (person_id);