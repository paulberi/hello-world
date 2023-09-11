-- Kladdlager används bara i GeoVis Vy och Pro.
-- Scriptet skapar databastabellen med tillhörande sequence för kladdlager.
-- Ersätt schemanamnet "referens" i följande SQL-script med det nya schemanamnet exempelvis "lulea".
-- Använd inga åäö, blanktecken och bindstreck i namnet.
-- Kör scriptet i "geovisdb" som är databasen för GeoVis i utvecklingsmiljön.

create table if not exists referens.kladdlager
(
	id serial not null
		constraint kladdlager_pkey
			primary key,
	skapad timestamp with time zone not null,
	geometry geometry not null,
	skapad_av varchar(40),
	stil json
);

alter table referens.kladdlager owner to geovis;

CREATE FUNCTION referens.trigger_func_skapad_kladdlager()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
  NEW.skapad = NOW();
  RETURN NEW;
END;
$BODY$;

ALTER FUNCTION referens.trigger_func_skapad_kladdlager()
    OWNER TO xplore;

create trigger set_skapad_on_insert_trigger
	before insert
	on referens.kladdlager
	for each row
	execute procedure referens.trigger_func_skapad_kladdlager();
