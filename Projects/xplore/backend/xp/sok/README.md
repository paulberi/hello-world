# Tjänst för sökning av adresser och fastigheter i Xplore

## Köra tjänsten

Enklast är att köra igång tjänsten i t.ex. IntelliJ, där man helt enkelt kan välja att köra
*Application-klassen. Porten som tjänsten lyssnar på bestäms av `server.port` i application.properties, men
properties i den filen kan överridas av kör-konfigurationen i IntelliJ.

Tjänsten använder sig av [Metria:s Geokodningstjänst](https://wiki.metria.se/pages/viewpage.action?pageId=45061579) för adresssökning.
Mer om denna tjänst finns under rubriken **Xplore geosöktjänst**

## Bygga
Tjänsten byggs enklast genom att i rot-projektet köra:

    mvn package -pl xp/sok -am
    
I `target/` hittas sedan en körbar jar som man kan starta med:

    java -jar sok-1.0.jar

Eller så kör man SokApplication t.ex. genom IntelliJ IDEA.

## API
Tjänsten har för närvande fem endpoints

    /geocode
    /reverse-geocode
    /fastighet
    /fastighet/delomraden
    /fastighet/intersection
    
Alla utom delområden erbjuder kommunfiltrering mha CQL_FILTER eller intersection mot en kommunmask (polygon). 