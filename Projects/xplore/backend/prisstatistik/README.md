# Tjänst för prisstatistik över fastighetsförsäljningar 
Tjänsten består i dagsläget av en enkel proxy till FME som utför sjäva analysen.

## Köra tjänsten

Enklast är att köra igång tjänsten i t.ex. IntelliJ, där man helt enkelt kan välja att köra
*Application-klassen. Porten som tjänsten lyssnar på bestäms av `server.port` i application.properties, men
properties i den filen kan överridas av kör-konfigurationen i IntelliJ.

### Auktorisering
Normal kräver tjänsten en giltigt token i Authorization-headern, men det kan stängas av med 
`authorization.disabled=true`

### Anropa

Tjänsten anropas genom att anropa /prisstatistik med ett GET-anrop, t.ex.:

    curl http://localhost:8080/prisstatistik

TODO: Alla parametervärdena är hårdkodade men kan ska kunna skicka in en punkt osv.

Som svar får man information om prisstatistik kring fastighetsförsäljning runt
den angivna punkten:

    {
        "medelKopsumma" : "1601",
        "medelAreal" : "332771",
        "antalOverlatelser" : "50",
        "medelByggvarde" : "355",
        "medelLantbruksvarde" : "591",
        "medelMarkvarde" : "140",
        "medelTottaxvarde" : "1086"
    }

## Bygga
Tjänsten byggs enklast genom att i rot-projektet köra:

    mvn package -pl prisstatistik -am
    
I `target/` hittas sedan en körbar jar som man kan starta med:

    java -jar prisstatistik-1.0.jar
    
## Problem:
* Felhantering från FMEs sida? Det finns fel i FMEs log men de skickas
inte tillbaka.
