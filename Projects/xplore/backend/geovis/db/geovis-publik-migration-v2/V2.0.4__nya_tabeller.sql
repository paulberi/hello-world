
create table if not exists karta_skridskobana_punkt
(
  id      serial not null
    constraint karta_skridskobana_punkt_pkey
    primary key,
  geom    geometry(MultiPoint, ${epsg}),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_skridskobana_punkt owner to ${schemaOwner};


