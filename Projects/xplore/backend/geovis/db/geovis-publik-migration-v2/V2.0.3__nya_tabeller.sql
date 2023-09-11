
create table if not exists karta_aktivitetspark
(
  id      serial not null
    constraint karta_aktivitetspark_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_aktivitetspark owner to ${schemaOwner};


