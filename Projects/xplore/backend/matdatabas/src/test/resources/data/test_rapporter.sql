SET search_path TO matdatabas;

-- Sätt bara in vilken mätningstyp som helst, enbart id intressant för oss
INSERT INTO matningstyp VALUES (2343,1,1,2,1,3,true,14,'Mätobjekt1',false,0,0,1,22.78,13.25,'A','m',2,NULL,'2019-09-23 11:12:27.460000',2);
INSERT INTO matningstyp VALUES (2350,1,1,2,1,3,true,14,'Mätobjekt1',false,0,0,1,22.78,13.25,'A','m',2,NULL,'2019-09-23 11:12:27.460000',2);

-- Samma sak med bild-ID
INSERT INTO bifogad_fil VALUES ('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee','image/png','Untitled.png','2020-08-11 11:02:27.126999','X','X');

-- och gränsvärden
INSERT INTO gransvarde VALUES (303,2343,0,1,1,NULL,'2020-08-11 08:14:50.837532','2020-08-12 13:25:13.784911',1);