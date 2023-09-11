# Geovis Extern
Alla kommuner delar instans och applikation, det enda som skiljer är konfigurationen av denna.

Applikationen använder inte inloggningsmekanismen, utan åtkomsten förutsätts vara alltid publik. 

## Konfiguration
Applikationen behöver vid uppstart veta vilken konfiguration som ska användas (finns ingen default),
vilket hämtas från [defaultConfigurationUrl](environments/environment.ts). Sedan anropas konfigurationstjänsten för
att få den kommunspecifika konfigurationen inladdad i applikationen.

## Utvecklingsmiljö
Den körande instansen i docker-swarmen nås genom att använda de kommunspecifika host:arna, t.ex.

    http://geovis-aneby-utv.metria.se

Denna host konfigureras i nginx i proxyn med rätt konfigurationsfil, URL:er till Geoservern, filarean osv.

## Köra lokalt
Applikationen körs på

    http://localhost:4202

Eftersom man kör mot localhost så måste alla relativa pather få rätt target för att fungera.
Detta görs i [proxy.conf.json](./proxy.conf.json) och den är förkonfigurerad med Aneby.
Vill man köra med en annan kommun så måste man ändra host:en till något annat lämpligt, t.ex.

    geovis-aneby-utv.metria.se --> geovis-odeshog-utv.metria.se
