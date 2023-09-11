CREATE OR REPLACE VIEW markkoll.intrang_fastighet_info
            (fastighetsbeteckning, length, fastighet_id, projekt_id, type, subtype, status) AS
    SELECT fastighet.fastighetsbeteckning,
           st_length(omradesintrang.geom) AS length,
           omradesintrang.fastighet_id    AS fastighet_id,
           importversion.projekt_id       AS projekt_id,
           omradesintrang.type,
           omradesintrang.subtype,
           omradesintrang.status,
           projekt.kund_id
      FROM omradesintrang
      JOIN importversion
        ON importversion.id = omradesintrang.version_id
      JOIN current_version
        ON current_version.version_id = importversion.id
      JOIN fastighet
        ON fastighet.id = omradesintrang.fastighet_id
      JOIN projekt
        ON projekt.id = importversion.projekt_id;

-- Uppdatering av getStatusForFastighet
CREATE OR REPLACE FUNCTION markkoll."getStatusForFastighet"(
    "projektId" UUID,
    "fastighetId" uuid)
    RETURNS varchar
    LANGUAGE 'sql'

    COST 100
    VOLATILE

AS $BODY$
  SELECT avtalspart.avtalsstatus status
    FROM markkoll.fastighet
    JOIN markkoll.avtal on avtal.fastighet_id=fastighet.id
    JOIN markkoll.projekt on projekt.id=avtal.projekt_id
    JOIN markkoll.markagare ON markagare.fastighet_id = fastighet.id
    JOIN markkoll.avtalspart on avtalspart.markagare_id = markagare.id AND avtalspart.avtal_id = avtal.id

   WHERE fastighet.id=$2 AND projekt.id = $1 AND avtalspart.inkludera_i_avtal = true
ORDER BY avtalspart.avtalsstatus != 'AVTALSKONFLIKT',
         avtalspart.avtalsstatus != 'EJ_BEHANDLAT',
         avtalspart.avtalsstatus != 'AVTAL_JUSTERAS',
         avtalspart.avtalsstatus != 'PAMINNELSE_SKICKAD',
         avtalspart.avtalsstatus != 'AVTAL_SKICKAT',
         avtalspart.avtalsstatus != 'AVTAL_SIGNERAT',
         avtalspart.avtalsstatus != 'ERSATTNING_UTBETALAS',
         avtalspart.avtalsstatus != 'ERSATTNING_UTBETALD'


  LIMIT 1
$BODY$;

CREATE OR REPLACE FUNCTION markkoll."getGeometristatusForFastighet"(
    "projektId" UUID,
    "fastighetId" UUID,
    "versionId" UUID)
    RETURNS varchar
    LANGUAGE 'sql'

    COST 100
    VOLATILE

AS $BODY$
    SELECT markkoll.avtal_geometristatus.geometristatus
    FROM markkoll.fastighetsforteckning
        JOIN markkoll.avtal on avtal.id = fastighetsforteckning.avtal_id
        LEFT JOIN markkoll.avtal_geometristatus on avtal.id = avtal_geometristatus.avtal_id
        AND markkoll.avtal_geometristatus.version_id = $3

    WHERE fastighetsforteckning.fastighet_id=$2 AND fastighetsforteckning.projekt_id = $1 AND fastighetsforteckning.excluded = false

$BODY$;

DROP VIEW IF EXISTS markkoll.avtalsstatus_view;
CREATE VIEW markkoll.avtalsstatus_view
AS
SELECT projekt.id as projekt_id,
       fastighet.id as fastighet_id,
       COALESCE(markkoll."getStatusForFastighet"(projekt.id, fastighet.id), 'EJ_BEHANDLAT'::character varying) AS status,
       projekt.kund_id
  FROM markkoll.projekt
  JOIN avtal
    ON avtal.projekt_id = projekt.id
  JOIN fastighet
    ON fastighet.id = avtal.fastighet_id;


-- Avtalsstatus för en fastighet. Används av Geoserver.
DROP VIEW IF EXISTS markkoll.fastighetsstatus;
CREATE VIEW markkoll.fastighetsstatus
AS
SELECT   fastighetsforteckning.fastighet_id AS fastighet_id,
         projekt.id as projekt_id,
         fastighet.fastighetsbeteckning,
         fastighet_omrade.geom as geom,
         fastighet.detaljtyp,
         COALESCE(markkoll."getGeometristatusForFastighet"(projekt.id, fastighet.id, avtal_geometristatus.version_id),
              CASE fastighetsforteckning.excluded
                  WHEN false THEN 'OFORANDRAD'::character varying END) AS geometristatus,
         COALESCE(markkoll."getStatusForFastighet"(projekt.id, fastighet.id), 'EJ_BEHANDLAT'::character varying) AS status,
         CASE fastighet.detaljtyp
             WHEN 'SAMFO'::text THEN 1
             WHEN 'FASTO'::text THEN 1
             ELSE 0
             END AS outredd,
         projekt.kund_id
FROM     markkoll.fastighetsforteckning
             JOIN avtal ON avtal.id = fastighetsforteckning.avtal_id
             JOIN fastighet ON fastighet.id = fastighetsforteckning.fastighet_id
             JOIN fastighet_omrade ON fastighet_omrade.fastighet_id = fastighet.id
             JOIN projekt on fastighetsforteckning.projekt_id = projekt.id
             LEFT JOIN avtal_geometristatus ON avtal.id = avtal_geometristatus.avtal_id
             JOIN markkoll.current_version ON current_version.projekt_id = projekt.id
WHERE 		 avtal_geometristatus.version_id = current_version.version_id
   OR        avtal_geometristatus IS NULL;

-- Fastighetsyta med fastighetsgräns och fastighetsbeteckning. Används av Geoserver.
CREATE OR REPLACE VIEW markkoll.fastighet_yta AS
SELECT  projekt.id as projekt_id,
        fastighetsforteckning.fastighet_id,
        fastighet.fastighetsbeteckning,
        fastighet_omrade.omrade_nr,
        fastighet_omrade.geom as geom,
        projekt.kund_id
FROM    markkoll.fastighetsforteckning
            JOIN fastighet ON fastighet.id = fastighetsforteckning.fastighet_id
            JOIN projekt on fastighetsforteckning.projekt_id = projekt.id
            JOIN fastighet_omrade ON fastighet_omrade.fastighet_id = fastighet.id;

-- Transformera extent till en kvadratisk ruta. Minsta storlek är 50 meter bred/hög
CREATE OR REPLACE FUNCTION markkoll."transformToSquareBox"(extent box2d) RETURNS geometry AS $$
DECLARE
    width float;
    height float;
    expandWidth float = 0;
    expandHeight float = 0;
    minSize float = 50;
BEGIN
    width = st_xmax(extent) - st_xmin(extent);
    height = st_ymax(extent) - st_ymin(extent);

    -- Sätt minsta storlek på kvadrat
    IF width < minSize AND height < minSize THEN
        expandWidth = (minSize - width)/2;
        expandHeight = (minSize - height)/2;
    ELSEIF width > height THEN
        expandHeight = (width - height)/2;
    ELSE
        expandWidth = (height - width)/2;
    END IF;

    RETURN st_expand(extent, expandWidth, expandHeight)::geometry;
END;
$$ LANGUAGE plpgsql;

-- Intrångsutbredning som en kvadratisk ruta. Används av Geoserver.
DROP VIEW IF EXISTS markkoll.intrang_extent_geom;
CREATE OR REPLACE VIEW markkoll.intrang_extent
AS
SELECT   projekt.id as projekt_id,
         omradesintrang.fastighet_id,
         omradesintrang.omrade_nr,
         "transformToSquareBox"(st_extent(st_buffer(omradesintrang.geom, 5::double precision))) AS geom,
         projekt.kund_id
FROM     omradesintrang
             JOIN fastighet_omrade ON omradesintrang.fastighet_id = fastighet_omrade.fastighet_id AND omradesintrang.omrade_nr = fastighet_omrade.omrade_nr
             JOIN current_version ON current_version.version_id = omradesintrang.version_id
             JOIN projekt ON projekt.id = current_version.projekt_id
GROUP BY projekt.id, omradesintrang.fastighet_id, omradesintrang.omrade_nr;

-- Mittpunkt för områdesintrång. Används av Geoserver.
DROP VIEW IF EXISTS markkoll.intrang_mittpunkt;
CREATE OR REPLACE VIEW markkoll.intrang_mittpunkt
AS
WITH omradesintrang_max_length AS
         (SELECT omradesintrang.fastighet_id,
                 omradesintrang.omrade_nr,
                 max(st_length(omradesintrang.geom)) as max_length
          FROM omradesintrang
          GROUP BY omradesintrang.fastighet_id, omradesintrang.omrade_nr)
SELECT projekt.id as projekt_id,
       omradesintrang.fastighet_id,
       omradesintrang.omrade_nr,
       st_pointonsurface(omradesintrang.geom) AS geom,
       projekt.kund_id
FROM omradesintrang
         JOIN omradesintrang_max_length
              ON omradesintrang.fastighet_id = omradesintrang_max_length.fastighet_id
                  AND omradesintrang.omrade_nr = omradesintrang_max_length.omrade_nr
         JOIN current_version ON current_version.version_id = omradesintrang.version_id
         JOIN projekt ON projekt.id = current_version.projekt_id
WHERE st_length(omradesintrang.geom) = omradesintrang_max_length.max_length
GROUP BY projekt.id, omradesintrang.geom, omradesintrang.fastighet_id, omradesintrang.omrade_nr;

-- Fastighetsutbredning. Används av Geoserver.
DROP VIEW IF EXISTS markkoll.fastighet_extent;
CREATE OR REPLACE VIEW markkoll.fastighet_extent AS
SELECT   fastighet.id AS fastighet_id,
         projekt.id as projekt_id,
         st_extent(fastighet_omrade.geom)::geometry as geom,
         projekt.kund_id
FROM     markkoll.projekt
             JOIN avtal ON avtal.projekt_id = projekt.id
             JOIN fastighet ON fastighet.id = avtal.fastighet_id
             JOIN fastighet_omrade ON fastighet_omrade.fastighet_id = fastighet.id
GROUP BY fastighet.id, projekt.id;

DROP VIEW IF EXISTS intrang_fastighet;
CREATE OR REPLACE VIEW intrang_fastighet AS
    SELECT fastighet.fastighetsbeteckning,
           fastighetsintrang.geom,
           fastighetsintrang.fastighet_id AS fastighet_id,
           current_version.projekt_id,
           fastighetsintrang.type,
           fastighetsintrang.subtype,
           fastighetsintrang.status,
           st_geometrytype(fastighetsintrang.geom) AS geometry_type,
           projekt.kund_id,
           fastighetsintrang.avtalstyp
      FROM markkoll.fastighetsintrang
      JOIN current_version
        ON current_version.version_id = fastighetsintrang.version_id
      JOIN fastighet
        ON fastighet.id = fastighetsintrang.fastighet_id
      JOIN projekt
        ON projekt.id = current_version.projekt_id;

DROP VIEW IF EXISTS intrang_projekt;
create or replace view markkoll.intrang_projekt(id, geom, type, subtype, status, projekt_id) as
SELECT DISTINCT intrang.id,
                intrang.geom,
                intrang.type,
                intrang.subtype,
                intrang.status,
                projekt.id AS projekt_id,
                projekt.kund_id,
                intrang.avtalstyp
           FROM markkoll.projekt
           JOIN markkoll.current_version
             ON current_version.projekt_id = projekt.id
           JOIN markkoll.intrang
             ON current_version.version_id = intrang.version_id;

-- Visar intrångsgeometrier från föregående version för alla projekt
DROP VIEW IF EXISTS intrang_projekt_previous_version;
CREATE OR REPLACE VIEW markkoll.intrang_projekt_previous_version AS
SELECT intrang.id,
       intrang.geom,
       intrang.type,
       intrang.subtype,
       intrang.status,
       importversion.projekt_id AS projekt_id,
       projekt.kund_id
FROM   intrang
  JOIN importversion on intrang.version_id = importversion.id
  JOIN
     (
        select max(skapad_datum) skapad_datum,
               importversion.projekt_id
          from importversion
          join current_version
            on current_version.projekt_id = importversion.projekt_id
         where importversion.id != current_version.version_id
      group by importversion.projekt_id
     ) sq ON importversion.skapad_datum = sq.skapad_datum AND importversion.projekt_id = sq.projekt_id
  JOIN projekt
    ON projekt.id = importversion.projekt_id;

DROP VIEW IF EXISTS indata_view;
CREATE OR REPLACE VIEW markkoll.indata_view AS
SELECT indata.id,
       indata.geom,
       indata.layer,
       indata.projekt_id,
       projekt.kund_id
  FROM indata
  JOIN projekt
    ON projekt.id = indata.projekt_id;