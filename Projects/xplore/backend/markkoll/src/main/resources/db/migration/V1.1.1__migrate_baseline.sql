-- Projekt
CREATE TABLE projekt_id_lookup
(
    id INTEGER PRIMARY KEY,
    uuid UUID NOT NULL DEFAULT uuid_generate_v4 ()
);

INSERT INTO projekt_id_lookup (id)
     SELECT markkoll_old.projekt.id
       FROM markkoll_old.projekt;

CREATE FUNCTION projekt_uuid(projekt_id INTEGER)
RETURNS UUID
LANGUAGE plpgsql
AS
$$
DECLARE
    ret UUID;
BEGIN
    SELECT projekt_id_lookup.uuid
      INTO ret
      FROM projekt_id_lookup
     WHERE projekt_id_lookup.id = @projekt_id;
     
    RETURN ret;
END;
$$;

INSERT INTO projekt
     SELECT projekt_uuid(id),
            namn,
            ort,
            projekttyp,
            organisation,
            beskrivning,
            skapad_av,
            skapad_datum,
            status,
            ledningsagare,
            start_datum,
            bidragsprojekt,
            ledningsstracka
       FROM markkoll_old.projekt;
       
-- Importversioner
INSERT INTO importversion
     SELECT id,
            filnamn,
            projekt_uuid(projekt_id),
            skapad_datum
       FROM markkoll_old.importversion;

INSERT INTO current_version
     SELECT projekt_uuid(markkoll_old.importversion.projekt_id),
            markkoll_old.importversion.id
       FROM markkoll_old.importversion
      WHERE markkoll_old.importversion.is_current_version = TRUE;
       
-- Fastigheter
INSERT INTO fastighet (id, fastighetsbeteckning, detaljtyp, kommunnamn, trakt, blockenhet, extern_id, fnr_fds, ytkval, adat, omrtyp)
     SELECT id,
            fastighetsbeteckning,
            detaljtyp,
            kommunnamn,
            trakt,
            blockenhet,
            externid,
            fnr_fds,
            ytkval,
            adat,
            omrtyp
       FROM markkoll_old.fastighet;

INSERT INTO fastighet_omrade
     SELECT *
       FROM markkoll_old.fastighet_omrade;

INSERT INTO samfallighet_fastighet (fastighet_id, samfallighet_id, fastighetsbeteckning)
     SELECT fastighet_id,
            samfallighet_id,
            fastighet_beteckning
       FROM markkoll_old.samfallighet_fastighet;
       
-- Avtal
    INSERT INTO avtal (fastighet_id, projekt_id)
SELECT DISTINCT fastighet.id,
                importversion.projekt_id
           FROM fastighet
           JOIN markkoll_old.projekt_fastigheter
             ON markkoll_old.projekt_fastigheter.fastighet_id = fastighet.id
           JOIN importversion
             ON importversion.id = markkoll_old.projekt_fastigheter.version_id;

    UPDATE avtal
       SET anteckning = markkoll_old.avtalsinformation.anteckning,
           ersattning = COALESCE(markkoll_old.avtalsinformation.ersattning, 0)
      FROM avtal av
INNER JOIN markkoll_old.avtalsinformation
        ON av.fastighet_id = markkoll_old.avtalsinformation.fastighet_id
     WHERE avtal.fastighet_id = markkoll_old.avtalsinformation.fastighet_id
       AND avtal.projekt_id = projekt_uuid(markkoll_old.avtalsinformation.projekt_id);

INSERT INTO avtal_geometristatus (version_id, avtal_id, geometristatus)
     SELECT version_id,
            avtal.id,
            geometristatus
       FROM markkoll_old.projekt_fastigheter
       JOIN avtal
         ON avtal.fastighet_id = markkoll_old.projekt_fastigheter.fastighet_id;
       
INSERT INTO avtalsjobb
     SELECT id,
            status,
            path,
            projekt_uuid(projekt_id),
            total,
            generated
       FROM markkoll_old.avtalsjobb;
    
-- Markägare
INSERT INTO person (namn, adress, personnummer, e_post, postnummer, postort, telefon, bankkonto)
     SELECT markkoll_old.person.namn,
            markkoll_old.person.adress,
            finfo_old.agare.personnummer,
            max(markkoll_old.person.e_post),
            max(markkoll_old.person.postnummer),
            max(markkoll_old.person.postort),
            max(markkoll_old.person.telefon),
            max(markkoll_old.person.bankkonto)
       FROM markkoll_old.person
  LEFT JOIN finfo_old.agare
         ON finfo_old.agare.namn = markkoll_old.person.namn
        AND finfo_old.agare.adress = markkoll_old.person.adress
   GROUP BY (markkoll_old.person.namn, markkoll_old.person.adress, finfo_old.agare.personnummer);

INSERT INTO markagare (id, person_id, fastighet_id, agartyp, andel)
     SELECT markkoll_old.markagare.id,
            markkoll.person.id,
            fastighet_id,
            agartyp,
            andel
       FROM markkoll_old.markagare
       JOIN markkoll_old.person
         ON markkoll_old.person.id = markkoll_old.markagare.person_id
       JOIN markkoll.person
         ON markkoll.person.namn = markkoll_old.person.namn
        AND markkoll.person.adress = markkoll_old.person.adress;

INSERT INTO avtalspart (avtal_id, markagare_id, avtalsstatus, inkludera_i_avtal)
     SELECT avtal.id,
            markkoll.markagare.id,
            markkoll_old.markagare.agare_status,
            markkoll_old.markagare.inkludera_i_avtal
       FROM markkoll.markagare
       JOIN avtal
         ON avtal.fastighet_id = markkoll.markagare.fastighet_id
       JOIN markkoll_old.fastighet
         ON markkoll_old.fastighet.id = markkoll.markagare.fastighet_id
       JOIN markkoll_old.markagare
         ON markkoll_old.markagare.id = markkoll.markagare.id;

INSERT INTO huvudagare (avtal_id, avtalspart_id)
     SELECT avtalspart.avtal_id,
            avtalspart.id
       FROM avtalspart
       JOIN markkoll_old.fastighet
         ON markkoll_old.fastighet.huvudagare_id = avtalspart.markagare_id;
      
-- Intrång
INSERT INTO intrang
     SELECT *
       FROM markkoll_old.intrang;

INSERT INTO omradesintrang (fastighet_id, omrade_nr, version_id, geom, type, subtype)
     SELECT fastighet_omrade.fastighet_id,
            fastighet_omrade.omrade_nr,
            intrang.version_id,
            st_intersection(intrang.geom, fastighet_omrade.geom) AS geom,
            intrang.type,
            intrang.subtype
       FROM fastighet_omrade
       JOIN avtal
         ON avtal.fastighet_id = fastighet_omrade.fastighet_id
       JOIN avtal_geometristatus
         ON avtal_geometristatus.avtal_id = avtal.id
       JOIN intrang
         ON intrang.version_id = avtal_geometristatus.version_id
        AND st_intersects(intrang.geom, fastighet_omrade.geom);
       
INSERT INTO fastighetsintrang (fastighet_id, version_id, geom, type, subtype)
     SELECT markkoll_old.intrang_fastighet.fastighetsid,
            importversion.id,
            markkoll_old.intrang_fastighet.geom,
            markkoll_old.intrang_fastighet.type,
            markkoll_old.intrang_fastighet.subtype
       FROM markkoll_old.intrang_fastighet
       JOIN projekt
         ON projekt.id = projekt_uuid(markkoll_old.intrang_fastighet.projektid)
       JOIN importversion
         ON importversion.projekt_id = projekt.id;
    
-- finfo
INSERT INTO finfo.agare
     SELECT id,
            andel,
            adress,
            co,
            personnummer,
            postnummer,
            postort,
            description,
            fastighet_id,
            namn,
            COALESCE(typ, 'LF')
       FROM finfo_old.agare;
       
-- uppstädning
DROP TABLE    projekt_id_lookup;
DROP FUNCTION projekt_uuid;