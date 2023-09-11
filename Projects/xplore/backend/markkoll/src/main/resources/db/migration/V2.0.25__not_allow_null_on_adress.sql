update markkoll.person set adress = '' where adress is null;
update markkoll.person set postnummer = '' where adress is null;
update markkoll.person set postort = '' where adress is null;

ALTER TABLE IF EXISTS markkoll.person
ALTER COLUMN adress SET DEFAULT '';

ALTER TABLE IF EXISTS markkoll.person
ALTER COLUMN adress SET NOT NULL;

ALTER TABLE IF EXISTS markkoll.person
ALTER COLUMN postnummer SET DEFAULT '';

ALTER TABLE IF EXISTS markkoll.person
ALTER COLUMN postnummer SET NOT NULL;

ALTER TABLE IF EXISTS markkoll.person
ALTER COLUMN postort SET DEFAULT '';

ALTER TABLE IF EXISTS markkoll.person
ALTER COLUMN postort SET NOT NULL;
