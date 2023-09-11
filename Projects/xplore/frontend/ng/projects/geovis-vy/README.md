# Geovis Vy
Alla kommuner delar instans och applikation, det enda som skiljer är konfigurationen av denna.

Applikationen kräver alltid inloggning.

I dagsläget är det endast möjligt att köra Aneby kommun.

## Konfiguration
Applikationen behöver vid uppstart veta vilken konfiguration som ska användas (finns ingen default),
vilket hämtas från [defaultConfigurationUrl](environments/environment.ts). Sedan anropas konfigurationstjänsten för
att få den kommunspecifika konfigurationen inladdad i applikationen.

## Utvecklingsmiljö
Den körande instansen i docker-swarmen nås genom att använda den endas icke-kommunspecifika hosten:

    http://geovis-vy-utv.metria.se

Denna host konfigureras i nginx i proxyn med Anebys konfigurationsfil, URL:er till Geoservern, filarean osv. 

Tanken är denna ska ersättas med kommunspecifika hostar, t.ex.:

    http://geovis-vy-aneby-utv.metria.se

## Köra lokalt
Applikationen körs på

    http://localhost:4201

Eftersom man kör mot localhost så måste alla relativa pather få rätt target för att fungera.
Detta görs i [proxy.conf.json](./proxy.conf.json) och den är förkonfigurerad med Aneby.
Vill man köra med en annan kommun (när dessa är tillgängliga) så måste man ändra host:en till något annat lämpligt, t.ex.

    geovis-vy-aneby-utv.metria.se --> geovis-vy-odeshog-utv.metria.se
