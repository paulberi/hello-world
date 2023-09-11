alter table definition_matningstyp ADD COLUMN automatisk_granskning boolean;

UPDATE definition_matningstyp SET automatisk_granskning=automatisk_inrapportering;
