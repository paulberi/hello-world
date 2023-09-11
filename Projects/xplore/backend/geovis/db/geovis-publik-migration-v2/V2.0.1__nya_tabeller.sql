
create table if not exists karta_discgolf
(
  id      serial not null
    constraint karta_discgolf_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_discgolf owner to ${schemaOwner};


create table if not exists karta_vindskydd
(
  id      serial not null
    constraint karta_vindskydd_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_vindskydd owner to ${schemaOwner};
