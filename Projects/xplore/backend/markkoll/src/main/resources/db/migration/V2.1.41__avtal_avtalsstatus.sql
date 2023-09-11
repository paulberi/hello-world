ALTER TABLE avtal
ADD avtalsstatus TEXT NOT NULL DEFAULT 'EJ_BEHANDLAT';

ALTER TABLE avtal
ADD CONSTRAINT fk_avtalsstatus FOREIGN KEY (avtalsstatus) REFERENCES avtalsstatus (status);

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

UPDATE avtal
SET avtalsstatus = COALESCE(markkoll."getStatusForFastighet"(projekt_id, fastighet_id), 'EJ_BEHANDLAT'::character varying);