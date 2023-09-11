# Mitt Metria Sälj

## Konfiguration

### Admin- och Shop-API

Applikationen använder Vendures admin API och shop API. Exempelvis används Admin API:et för att lägga order åt kund och Shop API för att hämta produkter och jsonscheman.

Både shop och admin-API:ernas typer och services genereras automatiskt (se codegen.yaml). Schemana som används för generering ligger i xplore/graphql och behöver hämtas manuellt från https://ehandel.metria.se/api/v1/admin/graphql (admin) och https://ehandel.metria.se/api/v1/graphql (shop) vid eventuella uppdateringar av api:et.

## Behörighet

För att använda tjänsten just nu behöver man vara säljare.

## Kör lokalt

```terminal
> cd /xplore/frontend/ng
```

Starta applikationen genom att köra:

```terminal
> npm start mitt-metria
```

Alternativt kommandot nedan, för att kunna köra --watch på kodgenereringen vilket gör att du inte behöver kompilera om när du gör ändringar i .graphql-filer. (rekommenderas):

```terminal
> npm run dev -- -- mitt-metria
```

Kör sedan applikationen på
http://localhost:4209
