ALTER SCHEMA ${schema} OWNER TO ${schemaOwner};

-- Create table, function and trigger

create table if not exists kladdlager
(
	id serial not null
		constraint kladdlager_pkey
			primary key,
	skapad timestamp with time zone not null,
	geometry geometry not null,
	skapad_av varchar(40),
	stil json
);

alter table kladdlager owner to ${schemaOwner};

CREATE FUNCTION trigger_func_skapad_kladdlager()
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

ALTER FUNCTION trigger_func_skapad_kladdlager()
    OWNER TO ${schemaOwner};


create trigger set_skapad_on_insert_trigger
	before insert
	on kladdlager
	for each row
	execute procedure trigger_func_skapad_kladdlager();
