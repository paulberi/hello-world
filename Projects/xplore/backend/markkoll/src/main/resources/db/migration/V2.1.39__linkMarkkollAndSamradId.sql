ALTER TABLE markkoll.samrad_projekt_i_markkoll

DROP CONSTRAINT fk_markkoll_id;

ALTER TABLE markkoll.samrad_projekt_i_markkoll

ADD  constraint fk_markkoll_id foreign key (markkoll_id) references projekt (id);