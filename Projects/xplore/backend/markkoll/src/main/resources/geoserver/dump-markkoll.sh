#!/bin/bash

WORKSPACE=markkoll
DATASTORE=MarkKoll
FEATURES="fastighet fastighet_extent fastighet_yta avtalsstatus_ej_behandlat avtalsstatus_ej_behandlat_nytt avtalsstatus_ej_behandlat_uppdaterat avtalsstatus_ersattning_utbetalas avtalsstatus_ersattning_utbetald avtalsstatus_justeras avtalsstatus_konflikt avtalsstatus_outredd avtalsstatus_paminnelse_skickad avtalsstatus_signerat avtalsstatus_skickat intrang_projekt intrang_brunn intrang_extent intrang_mittpunkt intrang_kabelskap intrang_luftledning intrang_markledning intrang_markskap intrang_mittlinjeredovisad_samfallighet intrang_natstation intrang_previous_version intrang_strak intrang_teknikbod"
LAYERS="$FEATURES"
STYLES="$FEATURES intrang_projekt_projektomrade"
LAYERGROUPS="avtalsstatus intrang_fastighet intrang"
RESOURCES="styles/area_again.svg styles/mittlinjestrak.svg styles/place.svg"

usage() {
  echo "$0 -h host:port -u user:password"
}

while getopts h:u: flag
do
  case "${flag}" in
    h) HOST=${OPTARG};;
    u) AUTH=${OPTARG};;
    *) usage && exit 1
  esac
done

if [ -z "$HOST" ] || [ -z "$AUTH" ]; then
  usage && exit 1
fi

./dump.sh \
  -h "$HOST" \
  -u "$AUTH" \
  -w "$WORKSPACE" \
  -d "$DATASTORE" \
  -f "$FEATURES" \
  -l "$LAYERS" \
  -g "$LAYERGROUPS" \
  -s "$STYLES" \
  -r "$RESOURCES"

