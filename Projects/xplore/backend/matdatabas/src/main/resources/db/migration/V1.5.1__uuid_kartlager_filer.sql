-- Migrate table to uuid.
ALTER TABLE kartlager_fil ALTER COLUMN id DROP DEFAULT;

ALTER TABLE kartlager_fil
    ALTER COLUMN id TYPE uuid USING uuid_generate_v4();

ALTER TABLE kartlager_fil ALTER COLUMN id SET DEFAULT uuid_generate_v4();

