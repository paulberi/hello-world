# Miljödatabas för omgivningspåverkan
Backend för Miljödatabas för omgivningspåverkan

## Kom igång
Enklast är att köra MatdatabasApplication från IntelliJ efter att man kört bygget via maven. OBS: default går applikationen mot 
databasen i utv-miljön, men följande properties måste vara satta utifrån:

    spring.datasource.username=matdatabas
    spring.datasource.password=<password>
    keycloak.adminPassword=<password>

Exempelvis genom miljövariabler (SPRING_DATASOURCE_USERNAME=... etc) men det finns många
olika sätt att göra det på. Om IntelliJ har skapat en run-configuration automatiskt är det enkelt att
lägga in overrides för properties i den.

Se https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config
för olika sätt.

## Bygg och deploy
Lokalt byggs tjänsten enklast genom att i rot-projektet (backend) köra:

    mvn package -pl matdatabas -am

Servern byggs, autotestas och deployas mha av Bamboo planer.

https://bamboo.metria.se/browse/XPLOR

## REST API
I denna tjänst ingår ett REST API som finns dokumenterat enligt Open API 3.0
