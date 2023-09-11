# Internt Produktionsstöd Plan och Mät
Applikationen använder en statisk konfiguration "internt-produktionsstöd", som anges i [app.config.ts](app/app.config.ts).  

Applikationen kräver alltid inloggning.

## Konfiguration
Applikationen hämtar vid uppstart konfigurationen från konfigurationstjänsten. Tekniskt sett kunde 
man hämta en annan konfiguration genom att ange detta som en överridande query parameter, men det 
används inte för närvarande.

## Utvecklingsmiljö
Den körande instansen i docker-swarmen nås genom:

    https://internt-produktionsstod-utv.metria.se

Denna host konfigureras i proxyns nginx med URL:er till skogstjänster, MetriaMaps osv. Dessa anges 
endast med relativa pather i konfigurationsfilen. 

## Köra lokalt
Applikationen körs på

    http://localhost:4204

Eftersom man kör mot localhost så måste alla relativa pather få rätt target för att fungera.
Detta görs i [proxy.conf.json](./proxy.conf.json).
