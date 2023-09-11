# Metria Skogsmaskindata
Denna tjänst läser in paket ifrån en filyta hos ForestLink och sparar dessa i en databas för sen kunna exponera detta data i form av lager via en GeoServer.


## Bygg och deploy
Servern byggs, autotestas och deployas mha av Bamboo planer.

## Test
Beskrivning av E2E-tester och hur det funkar.

## REST API
I denna tjänst ingår ett REST API som finns dokumenterat enligt Open API 3.0

## Hämta/kolla på filer från ForrestLink

För att ansluta till filervern så kan man använda följade kommando. Lösenord finns i application.properties

    sftp -oHostKeyAlgorithms=+ssh-dss metria@test.forestlink.se
