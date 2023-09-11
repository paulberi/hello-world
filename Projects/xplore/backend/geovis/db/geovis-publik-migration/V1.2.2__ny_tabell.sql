-- Delete old table and related sequence
drop sequence if exists karta_badhus_id_seq cascade;
drop table if exists karta_badhus cascade;


-- Create tables
create table if not exists karta_badhus
(
  id      serial not null
    constraint karta_badhus_pkey
    primary key,
  geom    geometry(MultiPoint, 3009),
  namn    varchar(80),
  beskr   varchar(254),
  link    varchar(254),
  adress  varchar(254),
  kontakt varchar(254)
);

alter table karta_badhus owner to ${schemaOwner};
