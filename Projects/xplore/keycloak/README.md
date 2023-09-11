# Teman för Keycloak
Här finns egna teman för Keycloak, tex. för att styla inloggningssidan.

## Kom igång
I mappen ```theme``` finns våra egna teman. Dessa är baserade på ett bastema som följer med Keycloak (finns på servern under /apps/keycloak-[senaste version]/themes). Det vi gör är att anpassa bastemat med hjälp av html/css samt text genom lokaliserade språkfiler (.properties). 

Det är viktigt att vi endast ändrar det vi behöver, till exempel enstaka properties i en språkfil, för att underlätta framtida uppgradering av Keycloak. 

[Dokumentation för att skapa teman med Keycloak](https://www.keycloak.org/docs/latest/server_development/#creating-a-theme)

## Utveckla lokalt
För att underlätta utveckling lokalt kan du starta en lokal container av Keycloak genom att köra följande:

    cd /docker-develop
    docker-compose up -d

Nu startas Keycloak på http://localhost:9090/auth och ändringar i teman synkas till containern.
Användarnamn: admin Lösenord: admin

För att testa inloggning mot Keycloak med en frontendklient kan du ladda ner en [exempel-applikation för Angular](https://github.com/manfredsteyer/angular-oauth2-oidc).

Lokal test av produktions imagen:
Kommentera ut "ENV KC_DB=postgres" i Dockerfile för att få lokal databas, eller skicka in databasvariablerna till en postgresdatabas.

    docker build . -t xplore_keycloak
    docker run --rm -p 9090:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin xplore_keycloak --hostname-strict false --proxy edge  --hostname-strict-https=false
    
