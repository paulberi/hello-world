# GeoVis
Klientdelen av GeoVis, som i nuläget bygger på https://github.com/origo-map/origo

## Uppdelning
Varje kommun har en katalog där kommunspecifik konfiguration ingår. Till exempel vilka lager som ska finnas tillgängliga, 
uppstartsområde och så vidare.

### common
Här ligger gemensamma filer som ingår i alla byggen. Främst Origo-relaterade saker. Filen origo.min.js är en tidigare
version än det senaste 2.x-spåret av Origo, men ska så småningom uppdateras.

## Bygge
Exempel:
`docker build -f Dockerfile.odeshog -t odeshog .`

## Köra lokalt
Eftersom respektive `index.json` innehåller relativa sökvägar så funkar inte dessa när man kör lokalt (den försöker t.ex. accessa geoservern på localhost:8082 istf localhost:8080).

Lösningen är att köra scriptet [run-geovis.sh](run-geovis.sh) med kommunen som inparameter, t.ex.

    ./run-geovis.sh odeshog

Vill man köra med en egen geoserver (istf för den på `xploregeoutv01.prodstod.se`) så inkluderar man den som andra parameter:

    ./run-geovis.sh odeshog geoserver

Då startas `geoserver` samt önskad kommuninstans, t.ex. `geovis-odeshog` på lämplig port.

Därefter modifieras sökvägarna i Docker-containern med (rätta) hårdkodade sökvägar till geoservern samt `geovis-sok`.

Skulle någon container redan köras så stoppas den och laddas om.
