## Xplore Admin
PoC för ett administrationsgränsnitt mot Keycloak för att hantera flera användare samtidigt på ett enklare sätt. Applikationen kräver inloggning med admin-behörigheter i Keycloak.

## Konfiguration
API:et är automatiskt genererat utifrån [OpenApi specifikation](./../../../../admin/src/resources/oas/api.yaml) i backend. Autentisering görs via Keycloak. 

## Utvecklingsmiljö
Finns inte driftsatt än så länge.

## Köra lokalt
För att köra applikationen behöver du först installera [Node.js](https://nodejs.org/en/). Applikationen körs genom att skriva följande kommando i terminalen.
    
    cd /xplore/frontend/ng
    npm run start admin

Applikationen nås via http://localhost:4207.

Eftersom man kör mot localhost så måste alla relativa pather få rätt target för att fungera. Detta görs i [proxy.conf.json](./proxy.conf.json). API:et är mockat i frontend med [MirageJs](https://www.miragejs.com) och slås på/av med miljövariabeln `apiMock` i [environment.ts](./src/environment/environment.ts). 


