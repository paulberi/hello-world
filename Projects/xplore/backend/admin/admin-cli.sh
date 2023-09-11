#!/bin/bash

if [ $# -eq 0 ]; then
    echo "No arguments provided"
    echo ""
    cat << EOM

CLI script för att använda admin applikationen.


Kommandon:
keycloak
    list-users
        <realm>: Listar användare på server i angiven realm
    spreadsheet
        print-as-users
            <filename>: Läser innehåll i angiven fil (se format nedan) och skriver ut som tolkade användare
        create-users
            <realm>
                <filename>: Läser in användare från angiven fil (se format nedan) och skapar dem under angiven realm på server

geoserver
    publish
        datastore
            <datastore>
                <srs>
                    <style>: Publicera alla lager i en datastore
    toggle-caching
        workspace
            <workspace>
                enabled|disabled: Ställ in tile caching för en workspace
        layer-group
            <workspace>
                <layerGroup>
                    enabled|disabled: Ställ in tile caching för en layer group
    set-style
        workspace
            <workspace>
                <style>: Sätt en stil för alla lager i en workspace
        layergroup
            <workspace>
                <layerGroup>
                    <style>: Sätt en stil för alla lager i en layer group

Flaggor:
--skip-first-rows=<unsigned integer>: Används när programmet läser in en fil. Hoppar över lika många rader som man anger för att inte få med titel etc...
--number-of-sheets=<unsigned integer>: Används när programmet läser in en fil. Anger hur många blad man vill läsa in. Default är att läsa alla blad.

Input-fil format:
.xlsx
Fält:
    användarnamn: Sträng, obligatoriskt
    e-post: Sträng
    förnamn: Sträng
    efternamn: Sträng
    roller: Sträng, komma-separerade namn på roller som finns i KeyCloak (ex "geovis_user, kund_admin, kund_user")
    aktiv: Boolean, säger om användaren ska vara aktiv efter den skapats
    temp_lösen: Sträng, sätts om man vill att användarens lösenord ska återställas till ett tillfälligt lösen med angivet värde
Default kolumnordning:
    användarnamn|e-post|förnamn|efternamn|roller|aktiv|temp_lösen
Alla kolumner förutom användarnamn kan lämnas tomma. Om en rad saknar användarnamn kommer hela raden att ignoreras.

Exempel på användning:
./admin-cli.sh keycloak spreadsheet print-as-users /path/to/file/filename.xlsx --skip-first-rows=X
Försöker att läsa in innehållet i angiven fil och skriva ut i konsolen formaterat som KeyCloak-användare. Hoppar över de X första raderna i indatat.
Används för att testa att filen kan läsas.

./admin-cli.sh keycloak list-users <realm>
Skriver ut användare från KeyCloak-servern i konsolen. Användarnamn och lösenord för behörighet kommer att efterfrågas.

./admin-cli.sh keycloak spreadsheet create-users <realm> /path/to/file/filename.xlsx --skip-first-rows=0
Läser in användare från angiven fil (hoppar över de 0 första raderna) och skapar dem på KeyCloak-servern. Användarnamn och lösenord för behörighet kommer att efterfrågas.

EOM
    exit 1
fi

java -jar -Dspring.main.web-application-type=NONE -Dlogging.level.root=warn -Dspring.main.banner-mode=off target/admin-1.0.jar --admin.console.enabled=true "$@"