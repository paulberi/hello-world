INSERT INTO kund_config.kund (id, namn, epost, kontaktperson, telefon, skapad_av, skapad_datum, andrad_av, andrad_datum)
    VALUES ('556799-2242', 'Metria AB', 'metria@metria.se', 'Jenny Odeblom', 0, 'Emil Edberg', current_date, 'Emil Edberg', current_date),
           ('XX6799-1111', 'Jenny El AB', 'metria@metria.se', 'Jenny Odeblom', 0, 'Emil Edberg', current_date, 'Emil Edberg', current_date),
           ('XX6799-2222', 'Andrés El AB', 'metria@metria.se', 'André Öberg', 0, 'Emil Edberg', current_date, 'Emil Edberg', current_date),
           ('XX6799-3333', 'Jonnys El AB', 'metria@metria.se', 'Jonny Siikavaara', 0, 'Emil Edberg', current_date, 'Emil Edberg', current_date)


ON CONFLICT DO NOTHING;

INSERT INTO kund_config.users (id, firstname, lastname, email)
    VALUES
        ('emil.edberg@metria.se', 'Emil', 'Edberg', 'emil.edberg@metria.se'),
        ('christoffer.karlsson@metria.se', 'Christoffer', 'Karlsson', 'christoffer.karlsson@metria.se'),
        ('jenny.odeblom@metria.se', 'Jenny', 'Odeblom', 'jenny.odeblom@metria.se'),
        ('henrik.bylund@metria.se', 'Henrik', 'Bylund', 'henrik.bylund@metria.se'),
        ('john.eriksson@metria.se', 'John', 'Eriksson', 'john.eriksson@metria.se'),
        ('joel.nandorf@metria.se', 'Joel', 'Nandorf', 'joel.nandorf@metria.se'),
        ('martin.aslin@metria.se', 'Martin', 'Åslin', 'martin.aslin@metria.se'),
        ('andre.oberg@metria.se', 'André', 'Öberg', 'andre.oberg@metria.se'),
        ('jonny.siikavaara@metria.se', 'Jonny', 'Siikavaara', 'jonny.siikavaara@metria.se'),

        ('jenny.odeblom@gmail.com', 'Jenny', 'Odeblom', 'jenny.odeblom@gmail.com')
ON CONFLICT DO NOTHING;

INSERT INTO kund_config.permissions (id, user_id, roll, produkt, kund_id)
    VALUES
        -- Metria AB
        (public.uuid_generate_v4(), 'emil.edberg@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'christoffer.karlsson@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'jenny.odeblom@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'henrik.bylund@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'john.eriksson@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'joel.nandorf@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'martin.aslin@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', '556799-2242'),

        (public.uuid_generate_v4(), 'emil.edberg@metria.se', 'ANALYS', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'christoffer.karlsson@metria.se', 'ANALYS', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'jenny.odeblom@metria.se', 'ANALYS', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'henrik.bylund@metria.se', 'ANALYS', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'john.eriksson@metria.se', 'ANALYS', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'joel.nandorf@metria.se', 'ANALYS', 'MARKKOLL', '556799-2242'),
        (public.uuid_generate_v4(), 'martin.aslin@metria.se', 'ANALYS', 'MARKKOLL', '556799-2242'),

        -- Jennys El
        (public.uuid_generate_v4(), 'jenny.odeblom@gmail.com', 'MARKHANDLAGGARE', 'MARKKOLL', 'XX6799-1111'),

        (public.uuid_generate_v4(), 'jenny.odeblom@gmail.com', 'ANALYS', 'MARKKOLL', 'XX6799-1111'),

        -- Andrés El AB
        (public.uuid_generate_v4(), 'andre.oberg@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', 'XX6799-2222'),

        (public.uuid_generate_v4(), 'andre.oberg@metria.se', 'ANALYS', 'MARKKOLL', 'XX6799-2222'),

        -- Jonnys El AB
        (public.uuid_generate_v4(), 'jonny.siikavaara@metria.se', 'MARKHANDLAGGARE', 'MARKKOLL', 'XX6799-3333'),

        (public.uuid_generate_v4(), 'jonny.siikavaara@metria.se', 'ANALYS', 'MARKKOLL', 'XX6799-3333')
    ON CONFLICT DO NOTHING;

INSERT INTO kund_config.fastighetsok(id, kund_id, username, password, kundmarke)
    VALUES
       (public.uuid_generate_v4(),'556799-2242', 'markkoll@metria.se', 'products-long-remember-home', 'markkoll'),
       (public.uuid_generate_v4(),'XX6799-1111', 'markkoll@metria.se', 'products-long-remember-home', 'markkoll'),
       (public.uuid_generate_v4(),'XX6799-2222', 'markkoll@metria.se', 'products-long-remember-home', 'markkoll'),
       (public.uuid_generate_v4(),'XX6799-3333', 'markkoll@metria.se', 'products-long-remember-home', 'markkoll')
    ON CONFLICT DO NOTHING;

INSERT INTO kund_config.metria_maps(id, kund_id, username, password)
    VALUES
        (public.uuid_generate_v4(), '556799-2242', 'onecomarkkoll', 'Storm25sekMeter'),
        (public.uuid_generate_v4(), 'XX6799-1111', 'onecomarkkoll', 'Storm25sekMeter'),
        (public.uuid_generate_v4(), 'XX6799-2222', 'onecomarkkoll', 'Storm25sekMeter'),
        (public.uuid_generate_v4(), 'XX6799-3333', 'onecomarkkoll', 'Storm25sekMeter')
    ON CONFLICT DO NOTHING;