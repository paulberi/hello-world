Metria Docker image för Geoserver
============================================

## Om
Se [Dockerfile](Dockerfile) för detaljer. 

## Bygga (utan nexus)
    
    docker build .

## GeoServer plugins
Läggs till vid behov i [add-plugins.sh](add-plugins.sh).

## Tomcat admin

    admin/go4it!
     
I [context.xml](image/src/main/resources/tomcat-conf/manager-conf/context.xml) är begränsningen att man bara ska komma åt managern från samma maskin (docker-containern) borttagen så att den ska gå att komma åt från hostmaskinen. 

## Geoserver admin
    
    admin/geoserver  

## Portar
Exponerar portar 8080.

## Geoserver data_dir
Geoserverns data-katalog ligger i `$GEOSERVER_DATA_DIR` (/var/geoserver_data_dir) och ägs av tomcat-anv.
För att få persistent lagring av inställningarna bör man starta upp containern med en volume som pekar på GEOSERVER_DATA_DIR.
    
Alla förändringar i inställningarna speglas i XML-filerna samt filstrukturen i `geoserver_data_dir`.   

## Geoservern i UTV
I utvecklingsmiljön körs geoservern här:

    http://xploregeoutv01.prodstod.se:8080/geoserver

## Starta Geoservern lokalt

    docker run --name geoserver -p 8080:8080 -v "$PWD"/geoserver_data_dir:/var/geoserver_data_dir -d taggen_du_satte

Nu bör geoservern vara åtkomlig på

    http://localhost:8080/geoserver

Och inställningarna har hämtats från [geoserver_data_dir](geoserver_data_dir) samt synkas även framöver mot den foldern.

## Ladda om ändringar från filsystemet
Ändrar man i filerna på filsystemet så måste man antingen
 
 1. Klicka på knappen `Reload` under `Server Status -> Configuration and catalog`
 2. POSTa till http://{host}:{port}/geoserver/rest/reload
 
 **Noterbart är att innehållet i global.xml (och andra filer?) inte går att ladda om, utan ändringarna i filen återställs när man försöker ladda om konfigurationen. Troligtvis en bugg i Geoserver?** 
