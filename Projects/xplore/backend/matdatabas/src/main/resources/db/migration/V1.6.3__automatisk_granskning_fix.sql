UPDATE definition_matningstyp SET automatisk_granskning=automatisk_inrapportering;
alter table definition_matningstyp alter column automatisk_granskning SET NOT NULL;
