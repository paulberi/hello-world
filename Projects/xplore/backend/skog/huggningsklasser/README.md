# Tjänst för analys av skogliga grunddata
Tjänsten består i dagsläget av en enkel proxy till FME som utför sjäva analysen.

Körs i swarmen på https://skogsanalys-utv.metria.se/skog/huggningsklasser och byggs och deployas automatiskt
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

Tjänsten anropas genom att POSTa en JSON med en WKT till /huggningsklasser, t.ex.:

    curl --header "Content-Type: application/json" --data '{"wkt": "POLYGON((323292.59670397802256048 6476748.01381985004991293,323353.85115267738001421 6473287.13746833801269531,326508.45526069303741679 6473195.25579528883099556,326539.08248504274524748 6476748.01381985004991293,323292.59670397802256048 6476748.01381985004991293))"}' http://localhost:9004/huggningsklasser

Eller genom att POSTa en JSON med en fastighet samt en bounding box till samma endpoint, t.ex.:

    curl --header "Content-Type: application/json" --data '{"fastighet":"338294","bbox":[679924.44599968,6589209.03599175,6790.79199968,6589264.49399176]}' http://localhost:9004/huggningsklasser

Som svar får man information om trädhöjder inom den angivna polygonen:

    {
    "medel" : 14.985,
    "referensAr" : 2012,
    "lov" : false,
    "andelUngskog_0_6" : 0.222,
    "andelGallringsskog_6_15" : 0.107,
    "andelSlutavverkningsskog_15" : 0.671
    }
    
## Bygga
Tjänsten byggs enklast genom att i rot-projektet köra:

    mvn package -pl skog/huggningsklasser -am
    
I `target/` hittas sedan en körbar jar som man kan starta med:

    java -jar huggningsklasser-1.0.jar

    
## Felhantering
Tjänsten validerar input och lämnar resterande felhantering till FmeClient att sköta.

Felen som kan inträffa hittas i FmeError (under common/fme).