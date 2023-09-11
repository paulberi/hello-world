# 2020-09-08:
   
Lägg till följande i nginx-configurationen:

```
        location /configAuthIssuer {
                default_type text/plain;
                return 200 'https://miljokoll.metria.se/auth/realms/Miljokoll';
        }
``` 

## Keycloak:
Skapa klient miljokoll enligt hur den är uppsatt i keycloak-utv. (Men i rätt realm)
Skapa batchUser SYSTEM

## Variabler
Se över om några värden i application.properties behöver override

```
keycloakadmin.baseUrl
keycloak.auth-server-url
keycloakadmin.adminBaseUrl
keycloak.realm
keycloakadmin.batchPassword
```
