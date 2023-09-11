-- Ta bort old_scheman som användes tillfälligt vid datamigrering.
DROP SCHEMA IF EXISTS finfo_old, markkoll_old CASCADE;
