CREATE TABLE markkoll.samrad_projekt_i_markkoll
(
    id                  uuid primary key,
    samrad_id            text,
    markkoll_id          uuid,
    constraint fk_markkoll_id foreign key (markkoll_id) references samrad_projekt_i_markkoll (id)
);