# Markkoll Backend

## Sätt upp egen databas

Det går att köra mot den gemensamma utv-databasen men är bra att ha en egen man kan testa mot.

### Starta en ny PostgreSQL med PostGIS och initiera databasen

```shell
$ cd backend/markkoll
$ docker run -d -p 5432:5432 -e POSTGRES_USER=markkoll -e POSTGRES_PASSWORD=mysecretpassword postgis/postgis:10-2.5
$ mvn flyway:info -Ddb.server=localhost -Ddb.user=markkoll -Ddb.passwd=mysecretpassword -Ddb.database=markkoll
$ mvn flyway:migrate -Ddb.server=localhost -Ddb.user=markkoll -Ddb.passwd=mysecretpassword -Ddb.database=markkoll
```

### Starta servern med rätt parameterar

Sätt parametrarna nedan - helst via run-configuration i IntelliJ istället för den incheckade filen.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/markkoll
spring.datasource.username=markkoll
spring.datasource.password=mysecretpassword
```

### Ta databasdump

För att testa migreringar och dylikt kan det vara bra att kunna ta databasdumper från andra miljöer
och restora dem i en annan.

Exempel för testmiljön:
```shell
$ pg_dump --file dump_test_1 --host xploredb01.prodstod.se --port 5432 --username xplore-admin --verbose --format=c markkoll_test
```

För prodmiljön måste vi gå via ssh-tunnel. Exempel:
```shell
$ pg_dump --file dump_prod_1 --host localhost --port 15432 --username xplore-admin --verbose --format=c markkoll
```

### Restore av databas

Restore av prod-dump som togs ovan till lokal databas:
```shell
$ pg_restore --clean --host=localhost --port=5432 --username=markkoll --verbose --format=c --dbname markkoll dump_prod_1
```

Om det redan finns data i den behöver du köra en `flyway:clean` först. Och beroende på om det tillkommit nya
migreringsskript behöver du köra `flyway:migrate` efter återställningen.

Om man kör i driftsmiljöerna körs migreringarna automatiskt när servern startas, men det görs inte som standard
när man kör lokalt.

(OBS: se sektionen om baseline om det inte går att köra migrate rakt av pga ändrade skript.)

## Sätt upp egen geoserver

Om man kör en egen databas måste man också köra en egen geoserver om man vill att kartan ska reflektera de projekt
man laddat in.

För att starta samma docker-image som körs på våra geoserver-maskiner:

```shell
$ cd ~/tmp
$ docker run -d -p 8080:8080 -v "$PWD"/geoserver_data_dir:/var/geoserver_data_dir nexus.metria.se:5000/xplore/geoserver:2.17.2
```

Sedan måste lagerdefinitionerna från markkoll laddas in. Här hämtar vi dem från utv-miljön och skickar in till den
lokala:

```shell
$ cd backend/markkoll/src/main/resources/geoserver
$ ./dump-markkoll.sh -h xploregeoutv01.prodstod.se:8080 -u admin:<password>
$ ./upload.sh -u "admin:geoserver" -d markkoll:mysecretpassword -n markkoll local
```

Observera att i nuläget måste upload.sh redigeras så att miljön `local` pekar mot din miljö, t.ex. enligt:

```shell
HOST_LOCAL=localhost:8080
DATASTORE_HOST_LOCAL=172.17.0.1
```

`HOST_LOCAL` pekar på geoservern och `DATASTORE_HOST_LOCAL` ska peka på PostgreSQL-databasen som startats sen tidigare.
Observera att adressen måste vara nåbar från geoserverns docker-container.

### Peka frontend till den lokala geoservern

Redigera `frontend/ng/projects/markkoll/proxy.conf.js` och ange den lokala adressen för Markkolls geoserver:

```json
{
  ...
  "/geoserver/markkoll": {
      "target": "http://localhost:8080",
      "changeOrigin": true
  }
}
```

## Flyway baseline

Om man har gjort om databasskripten (vilket inte bör hända annat än i nödfall) och en ny baseline skapats
så måste man se till att sätta denna baseline när man migrerar till en existerande miljö.

Om t.ex. skriptet `1.0.1` motsvarar det som ligger i prod, och resten av skripten ska appliceras, men 
`flyway_schema_history` i prod ser helt annorlunda ut, gör man så här:

* Se till att baseline i `pom.xml` är `1.0.1`
* Droppa tabellen `flyway_schema_history`
* Kör `mvn flyway:baseline -Ddb.server=localhost -Ddb.port=15432 -Ddb.user=markkoll -Ddb.passwd=<passwd> -Ddb.database=markkoll`

När servern sedan startas ska migreringarna köras från baseline-versionen.

## Flyway target

Om man vill migrera databasen upp till en särskild version (för att t.ex. kunna fylla på med testdata från produktionsmiljön) så kan man sätta en target version i pom.xml. 

Lägg till följande i <configuration> för "flyway-maven-plugin" för att endast migrera till och med version 1.1.9.

```xml
  <target>1.1.9</target>
```
