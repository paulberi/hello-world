# Admin backend application
## Konsol applikation
### Bygg
1. Öppna en terminal i backend katalogen (föräldern till denna katalog)
2. `mvn package -pl admin -am`
### Användning
1. `cd admin/`
2. `./admin-cli.sh` utan argument för vidare instruktioner
## REST API
### Kör lokalt
1. Kör applikationen med IntelliJ
2. Tjänsten är tillgänglig på `localhost:9013/api/`
### API Specifikation
API specifikationen är tillgänglig som en OpenAPI specification [här](../../openapi/admin-api.yaml)