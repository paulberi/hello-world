# Castor integration

(bara lite påbörjad, fyll gärna på)

## Starta tjänsten lokalt

Tjänsten går att starta lokalt, enklast genom att starta den i Intellij.

### application.properties

Man måste lägga till giltiga värden till följande inställningar:

````
finfo.auth.username=username
finfo.auth.password=password
finfo.kundmarke=kundmarke

## Castor
castor.auth.customer-id=customerId
castor.auth.api-key=apiKey
````

### Certifikat

Om man vill köra tjänsten lokalt måste man lägga till certifikatet i javas keystore. Det certifikat som ska läggas
till är filen `cert_ekero_base64.cer` som finns i rot-mappen för projektet.

För att göra det så ska man ställa sig i katalogen `$JAVA_HOME/lib/security` och köra
kommandot:

````
keytool -importcert -file $filsökväg_till_cert_ekero_base64.cer -alias cert_ekero_base64.cer -cacerts -storepass changeit -noprompt
````

`-file` anger var filen som innehåller certifikatet finns.

`-alias` anger ett lite mer läsvänligt namn till certifikatet.

### Webproxy

Anropen mot castor måste gå via en proxy när det körs i docker-swarmen, när man kör lokalt behövs inte det. Man kan
kommentera ut följande rader i filen `src/main/java/se/metria/xplore/castor/integration/restapi/CastorSendIntegrationController.java`

````
httpConduit.getClient().setProxyServer(proxyHost);
httpConduit.getClient().setProxyServerPort(proxyPort);
````

## Testanrop

För att testa att tjänsten är igång kan man göra testanrop.

### Ping

Görs med ett GET-anrop till:

    http://localhost:9015/castorapi/ping

curl:

    curl "http://localhost:9015/castorapi/ping"

### Hämta från castor

Görs med ett GET-anrop till:

    http://localhost:9015/castorapi/getCastorSelections

curl:

    curl "http://localhost:9015/castorapi/getCastorSelections"

### Skicka till castor

Görs med ett POST-anrop till:

    http://localhost:9015/castorapi/sendToCastor

Med en body med en JSON som innehåller `requestName` och `realEstateIdentifierList`
(en lista med valda fastigheters `uuid` och `beteckning`), ungefär:

````
{"requestName":"Testtest","realEstateIdentifierList":[
  {"uuid":"909a6a4d-4925-90ec-e040-ed8f66444c3f","beteckning":"VALDEMARSVIK ALKÄRRET 18"},
  {"uuid":"909a6a4d-4926-90ec-e040-ed8f66444c3f","beteckning":"VALDEMARSVIK ALKÄRRET 19"},
  {"uuid":"909a6a4d-4927-90ec-e040-ed8f66444c3f","beteckning":"VALDEMARSVIK ALKÄRRET 20"}]} 
````

curl:

````
curl -X POST -H "Content-Type: application/json" --data '{"requestName":"Testtest","realEstateIdentifierList":[{"uuid":"909a6a4d-4925-90ec-e040-ed8f66444c3f","beteckning":"VALDEMARSVIK ALKÄRRET 18"}, {"uuid":"909a6a4d-4926-90ec-e040-ed8f66444c3f","beteckning":"VALDEMARSVIK ALKÄRRET 19"}, {"uuid":"909a6a4d-4927-90ec-e040-ed8f66444c3f","beteckning":"VALDEMARSVIK ALKÄRRET 20"}]}'  http://localhost:9015/castorapi/sendToCastor
````

## Test mot Castor

För att se om anropen från tjänsten hamnar i Castor finns det ett testprogram
som Prosona har gjort. 

## Test anrop mot fastighetsök

För att testa så att anrop går fram till fastighetsök, för att testa att användaren är uppsatt rätt och att det inte blir något fel på vägen går det att göra SOAP-anropen direkt och se om det kommer något svar. Antingen från någon testklient för SOAP, till exempel [SoapUI](https://www.soapui.org/tools/soapui/) eller med ett curl-anrop från kommandoraden. En fördel med curl är att det då enkelt går att göra test från den maskin där tjänsten ska köras.

````bash

# $FSOKAUTH är användare:lösen i base65-encoding
# I exemplet används url:en till fastighetsöks produktionsserver, ska
# test göras mot fsöks testserver så ändra url.
curl -X POST https://fastighetsok.metria.se/fsok-2.3-ws/webservice -H 'Content-Type: text/xml' -H 'SOAPAction:' -H 'Authorization: Basic $FSOKAUTH' -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:fin="http://www.metria.com/finfo/data/finfodatamodell">
   <soapenv:Header/>
   <soapenv:Body>
      <fin:sokPaNyckel>

         <!--Optional:-->
         <fin:feature>?</fin:feature>
         <fin:information>XML022</fin:information>
         <fin:kundmarke>testtest</fin:kundmarke>
         <fin:nyckel>909a6a88-49c0-90ec-e040-ed8f66444c3f</fin:nyckel>
      </fin:sokPaNyckel>
   </soapenv:Body>
</soapenv:Envelope>'
````
