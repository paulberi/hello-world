ALTER TABLE avtal
ADD eget_tillvaratagande INTEGER DEFAULT 0;

ALTER TABLE avtal
RENAME COLUMN rotnetto_typ TO tillvaratagande_typ;

ALTER TABLE elnat.rotnetto_typ
RENAME TO tillvaratagande_typ;
