INSERT INTO elnat.tillvaratagande_typ (typ)
    VALUES ('EJ_BESLUTAT');

UPDATE avtal
SET tillvaratagande_typ = 'EJ_BESLUTAT'
WHERE tillvaratagande_typ = 'DECISION_PENDING';

ALTER TABLE avtal
ALTER COLUMN tillvaratagande_typ SET DEFAULT 'EJ_BESLUTAT';

DELETE FROM elnat.tillvaratagande_typ
      WHERE typ = 'DECISION_PENDING';