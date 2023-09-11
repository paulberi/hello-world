ALTER TABLE markkoll.samrad_projekt_i_markkoll

DROP CONSTRAINT fk_markkoll_id;

ALTER TABLE markkoll.samrad_projekt_i_markkoll

DROP COLUMN markkoll_id;

ALTER TABLE markkoll.samrad_projekt_i_markkoll

ADD COLUMN markkoll_projekt_id uuid not null ;

ALTER TABLE markkoll.samrad_projekt_i_markkoll

ADD constraint fk_markkoll_projekt_id foreign key (markkoll_projekt_id) references projekt (id);
