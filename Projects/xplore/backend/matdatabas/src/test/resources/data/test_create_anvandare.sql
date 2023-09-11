SET search_path TO matdatabas;

INSERT INTO anvandare(
    id, namn, foretag, aktiv, inloggnings_namn, behorighet, skicka_epost, e_post)
VALUES (1, 'Testare', 'Metria', true, 'admin', 2,true, 'test@metria.se');
