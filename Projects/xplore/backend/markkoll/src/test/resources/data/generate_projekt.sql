INSERT INTO markkoll.projekt(id, namn, ort, projektTyp, beskrivning, skapad_av, skapad_datum, organisation, status)
WITH base(id, n1,n2,n3,n4,n5,n6,n7,n8) AS
     (
         SELECT id
            ,MIN(CASE WHEN rn = 1 THEN nr END)
            ,MIN(CASE WHEN rn = 2 THEN nr END)
            ,MIN(CASE WHEN rn = 3 THEN nr END)
            ,MIN(CASE WHEN rn = 4 THEN nr END)
            ,MIN(CASE WHEN rn = 5 THEN nr END)
            ,MIN(CASE WHEN rn = 6 THEN nr END)
            ,MIN(CASE WHEN rn = 7 THEN nr END)

         FROM generate_series(1,1000) id -- Antal projekt du vill skapa
            ,LATERAL( SELECT nr, ROW_NUMBER() OVER (ORDER BY id * random())
                FROM generate_series(1,900) nr
             ) sub(nr, rn)
         GROUP BY id
     ), dict(lorem_ipsum, names, chars, types, ort, organisation) AS
     (
         SELECT 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacus arcu, blandit non semper elementum, fringilla sodales est.
            Ut porttitor blandit sapien pellentesque pretium. Donec ut diam sed urna venenatis hendrerit. Nulla eros arcu, mattis vitae congue cursus, tincidunt sed turpis. Curabitur non enim diam, eget elementum dolor. Vivamus enim tortor, tempor at vehicula ac, malesuada id est. Praesent at nibh eget metus dapibus dapibus. Donec arcu orci, sagittis eu interdum vitae, facilisis quis nibh.
            Mauris luctus molestie velit, at vestibulum magna cursus sit amet. Nulla in accumsan libero. Donec sed sem lectus. Mauris congue sapien et diam euismod vitae scelerisque diam tincidunt. Praesent a justo enim, vitae venenatis dolor. Donec in tortor at magna dapibus suscipit sit amet a libero. Vivamus porttitor rhoncus tellus, at luctus nisl semper bibendum. Fusce eget accumsan orci. Qout'
            ,'{"Jenny","John","Emil","Henrik","Joel","Christoffer","Mikael","Mattias","Jill"}'::text[]
            ,'{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}'::text[]
            ,'{"Elnät", "Fibernät", "Kraftledning"}'::text[]
            ,'{"Umeå","Skellefteå","Luleå","Sundsvall","Dorotea","Storuman","Tärnaby","Piteå","Robertsfors","Sävar","Gunnismark"}'::text[]
            ,'{"Organisation A","Organisation B"}'::text[]
            ,'{"Pågående", "Avslutat"}'::text[]
         )
SELECT b.id, sub.*
FROM base b
   ,LATERAL (
    SELECT format('Projekt %s', CEILING(random() * 10000)+10000)
        ,ort[b.n1 % 11+1]
        ,types[b.n2 % 3+1]
        ,substring(lorem_ipsum::text, b.n3, 100)
        ,format('%s %s', names[b.n4 % 9+1], chars[b.n5 % 26+1])
        ,NOW() - '1 day'::INTERVAL * (b.n6 % 365)
        ,organisation[b.n5 % 2+1]
        ,status[b.n8 % 2+1]
    FROM dict
    ) AS sub(namn, ort, skapad_av, beskrivning, projektTyp, skapad_datum, organisation, status);