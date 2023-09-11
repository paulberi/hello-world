# Autentisering
Lösningen integrerar mot kundens ADFS via SAML.  Det innebär att vi inte utför någon autentisering själv, men vi har ändå en lokal användardatabas som vi mappar användar-id:t mot. 

I utvecklingsmiljöerna går vi mot vår egen Keycloak-server som också har stöd för SAML.

## Implementation
Vi använder en [extension](https://github.com/spring-projects/spring-security-saml) till Spring för att hantera alla detaljer kring SAML.

Extensionen är dock på väg att integreras in i Spring Security i en nyare skepnad, så när nästa version är släppt (Spring Boot 2.2), eller när vi väl bestämmer för att uppdatera, så måste vi se över det.  Se mer i sektionen för [SAML](https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#saml2) i dokumentationen för Spring Framework 5.2 (som Spring Boot 2.2 kommer att använda).

Det står att single logout ännu inte är implementerat i den nya lösningen. Innebär det att man inte kan logga ut mot IDP:n? Eller är det bara att man inte kan ta emot logut-requests från IDP:n?

Det är något oklart om vår nuvarande lösning kommer att sluta fungera om vi uppdaterar.

## Login
Klienten börjar med att göra ett request till `/api/user/user-details` och om det returnerar 403 så initieras en login via SAML. När man efter en lyckad inloggning dirigeras tillbaka till startsidan så kommer anropet att gå igenom.

Normalt sätt returnerar servern själv en vidaredirigering med 302, men eftersom det inte går att hejda med javascript kan vi inte ha det så.

