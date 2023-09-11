# Backend

# Databashantering

Xplore-projektet använder flyway (https://flywaydb.org) för versionshantering av databaserna.

Det finns en flyway-instans uppsatt för varje databas i xplore. Versions-filerna för de olika databaserna
innehåller endast scheman, med undantag för xplore-databasen som har config-filer.

## Process

Ifall något databas-arbete är gjort i en databas behöver ändringarna versionshanteras för enkel överföring
till andra miljöer (utv/test/prod).

Det finns två sätt göra ändringar i databasen och det påverkar hur man arbetar med flyway; antingen direkt mot databasen via pgAdmin eller genom flyway versionsskript.

### Ändringar via versionsskript (rekommenredat):
* Skapa en ny versionsfil under ```{databas}/src/main/resources/db/migration``` som innehåller SQL med förändringarna.
* Kör ```mvn flyway:migrate -Ddb.passwd={PASSWORD}``` mot de miljöer du vill uppdatera.
	* För att gå mot en annan miljö, antingen lägg till flaggorna ```mvn flyway:migrate -Ddb.passwd={PASSWORD} -Ddb.server={SERVER} -Ddb.database={DATABASE}``` eller ändra i pom.xml

Om möjligt är det här det rekommenderade arbetssättet. Det går att, via pgAdmin, få ut SQL queries för de ändringar du gör (utan att applicera dom)
genom att i pgAdmin-dialoger (t.ex en table properties dialog) gå till SQL-tabben.

### Ändringar via pgAdmin:
Den här metoden är mest lämplig om ändringar har införts i databasen och man har glömt skapa versionsskript.

* Gör dina ändringar i pgAdmin (utv-miljön)
* Skapa schema-dumpar av de databaser du vill använda (pgAdmin -> högerklicka databas -> backup -> only schema, use insert commands)
	* Använd apgdiff (https://github.com/fordfrog/apgdiff) för att generera de SQL queries som krävs för att gå från ett schema till ett annat
		* Exempel användning:
			```
			java -jar apgdiff-2.6.0-SNAPSHOT.jar xplore_test_dump xplore_utv_dump
			```
			Ovanstående kommando genererar den SQL som krävs för att överföra ändringar från utv-miljön till test-miljön.
		* VIKTIGT: Se över de genererade SQL-queries, apgdiff hanterar inte alla queries väl.
		  Byte av tabellnamn kommer resultera i att tabellen droppas och en ny skapas.
		  Det är även möjligt att apgdiff inte kan hantera vissa queries så dumparna kan behöva ses över manuellt ibland.
* Skapa en ny versionsfil under ```{databas}/src/main/resources/db/migration``` som innehåller SQL med förändringarna.
* Kör ```mvn flyway:migrate -Ddb.passwd={PASSWORD}``` mot den miljö du vill uppdatera. Kontrollera i ``pom.xml`` att den går mot rätt databas.
	* För att gå mot en annan miljö, antingen lägg till flaggorna ```mvn flyway:migrate -Ddb.passwd={PASSWORD} -Ddb.server={SERVER} -Ddb.database={DATABASE}``` eller ändra i pom.xml

### Jooq definitionerna behöver även uppdateras
```mvn jooq-codegen:generate  -Pjooq-codegen-xml  -Ddb.passwd={PASSWORD} -Ddb.server={SERVER}  -Ddb.database={DATABASE}```
