# Tjänst för analys av skyddade områden
Tjänsten består i dagsläget av en enkel proxy till FME som utför sjäva analysen.

Körs i swarmen på https://skogsanalys-utv.metria.se/skog/skyddade-omraden och byggs och deployas automatiskt
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

Tjänsten anropas genom att POSTa en JSON med en WKT till /skyddade-omraden, t.ex.:

    curl --header "Content-Type: application/json" --data '{"wkt": "MULTIPOLYGON(((820627.9975951198 7290667.978460716,821109.608535396 7290009.449215849,821421.6727671056 7290196.1963151395,821006.4061910511 7290781.00959976,820627.9975951198 7290667.978460716)))"}' http://localhost:9020/skyddade-omraden

Eller genom att POSTa en JSON med en fastighet samt en bounding box till samma endpoint, t.ex.:

    curl --header "Content-Type: application/json" --data '{"fastighet":"338294","bbox":[679924.44599968,6589209.03599175,6790.79199968,6589264.49399176]}' http://localhost:9020/skyddade-omraden

Som svar får man information om skyddade områden inom den angivna polygonen:

    {
      "skyddadeOmraden": [
        {
          "skyddsform": "Fornlämning",
          "id": "L1992:4267",
          "namn": "",
          "areaHa": 0.079
        },
        {
          "skyddsform": "Natura 2000-område",
          "id": "SE0820624",
          "namn": "Alterberget",
          "areaHa": 15.872
        },
        {
          "skyddsform": "Naturreservat",
          "id": "2002782",
          "namn": "Alterberget",
          "areaHa": 15.873
        },
        {
          "skyddsform": "Nyckelbiotop",
          "id": "N 5686-1997",
          "namn": "ALTERBERGET",
          "areaHa": 11.433
        }
      ],
      "kontrolleradeSkyddsformer": [
        {
          "skyddsform": "Djur- och växtskyddsområden"
        },
        {
          "skyddsform": "Kulturreservat"
        },
        {
          "skyddsform": "Landskapsbildskyddsområden"
        },
        {
          "skyddsform": "Naturminnen"
        },
        {
          "skyddsform": "Nationalparker"
        },
        {
          "skyddsform": "Naturreservat"
        },
        {
          "skyddsform": "Naturvårdsområden"
        },
        {
          "skyddsform": "Skogliga biotoppskyddsområden"
        },
        {
          "skyddsform": "Övriga biotopskyddsområden"
        },
        {
          "skyddsform": "Interimistiska förbud"
        },
        {
          "skyddsform": "Vattenskyddsområden"
        },
        {
          "skyddsform": "Natura 2000-områden"
        },
        {
          "skyddsform": "Nyckelbiotoper"
        },
        {
          "skyddsform": "Fornlämningar"
        }
      ]
    }
    
## Bygga
Tjänsten byggs enklast genom att i rot-projektet köra:

    mvn package -pl skog/skyddade-omraden -am
    
I `target/` hittas sedan en körbar jar som man kan starta med:

    java -jar skyddade-omraden-1.0.jar

    
## Felhantering
Tjänsten validerar input och lämnar resterande felhantering till FmeClient att sköta.

Felen som kan inträffa hittas i FmeError (under common/fme).
