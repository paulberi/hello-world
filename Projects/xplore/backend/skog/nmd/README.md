# Tjänst för analys av trädslag och produktiv skogsmark
Tjänsten består i dagsläget av en enkel proxy till FME som utför sjäva analysen.

Körs i swarmen på https://skogsanalys-utv.metria.se/skog/nmd och byggs och deployas automatiskt
när man pushar till repot.

Tjänsten bygger upp en request mot FME och nyttjar därefter FmeClient i common för själva anropet och felhanteringen mot FME.

## Köra tjänsten

Enklast är att köra igång tjänsten i t.ex. IntelliJ, där man helt enkelt kan välja att köra
*Application-klassen. Porten som tjänsten lyssnar på bestäms av `server.port` i application.properties, men
properties i den filen kan överridas av kör-konfigurationen i IntelliJ.

### Auktorisering
Normal kräver tjänsten en giltigt token i Authorization-headern, men det kan stängas av med 
`authorization.disabled=true`

I exemplen nedan förutsätts det att auktoriseringen är avaktiverad.

### Inloggning till FME
Görs mha default-inställningarna i fme.properties under common/fme. Dessa kan överridas i application.properties om det skulle behövas. Skriptet som ska köras är det enda som måste finnas i application.properties.

### Anropa

Tjänsten anropas genom att POSTa en JSON med en WKT till /nmd, t.ex.:

    curl --header "Content-Type: application/json" --data '{"wkt": "POLYGON((323292.59670397802256048 6476748.01381985004991293,323353.85115267738001421 6473287.13746833801269531,326508.45526069303741679 6473195.25579528883099556,326539.08248504274524748 6476748.01381985004991293,323292.59670397802256048 6476748.01381985004991293))"}' http://localhost:9001/nmd

Eller genom att POSTa en JSON med en fastighet samt en bounding box till samma endpoint, t.ex.:

    curl --header "Content-Type: application/json" --data '{"fastighet":"338294","bbox":[679924.44599968,6589209.03599175,6790.79199968,6589264.49399176]}' http://localhost:9001/nmd

Som svar får man information om skogen inom den angivna polygonen:

    {
        "tallskogHa": 2.28,
        "granskogHa": 0,
        "barrblandskogHa": 0,
        "lovblandadBarrskogHa": 1.75,
        "triviallovHa": 2.94,
        "adellovHa": 3.35,
        "triviallovAdellovHa": 0.9,
        "skogHa": 11.31,
        "temporartEjSkogHa": 0.09,
        "skogVatmarkHa": 0.51,
        "oppenVatmarkHa": 0.86,
        "akermarkHa": 0,
        "ovrigOppenMarkHa": 7.78,
        "exploateradMarkHa": 0.77,
        "vattenHa": 0.04,
        "molnOklassatHa": 0,
        "Areal_raster_ha": 21.27,
        "Area_ha": 21.2,
        "referensAr": 2018,
        "raster_status": "OK"
    }
    
## Bygga
Tjänsten byggs enklast genom att i rot-projektet köra:

    mvn package -pl skog/nmd -am
    
I `target/` hittas sedan en körbar jar som man kan starta med:

    java -jar nmd-1.0.jar

## Felhantering
Tjänsten validerar input och lämnar resterande felhantering till FmeClient att sköta.

Felen som kan inträffa hittas i FmeError (under common/fme).
