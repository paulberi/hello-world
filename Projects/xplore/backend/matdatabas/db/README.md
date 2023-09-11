# Databas

Som en del av avtalet skulle vi importera en existerande mätdatabas, och eftersom
den redan fanns i en temporär lösning när projektet startades började vi med att importera den.

## Import
Eftersom den existerande databasen låg i en SQLServer bestämde vi oss för att konvertera den till
PostgreSQL. För att göra det måste vi ha en instans av SQL Server - se [docker image](https://docs.microsoft.com/en-us/sql/linux/quickstart-install-connect-docker?view=sql-server-linux-2017&pivots=cs1-bash).

Steg för att konvertera:
* Läs in dump till SQL Server 
* Generera skript från databas (SQL Server Management Studio) => script.sql
* Konvertera script.sql till PostgreSQL-format med sqlserver2pgsql
  * Resultatet blir två skript: ett med tabellstrukturen och ett med constraints
* För över data mellan SQL Server och PostgreSQL med FME
* Applicera constraints

### Detaljerad info
För att konvertera skriptet till PostgreSQL använde vi https://github.com/dalibo/sqlserver2pgsql. Den konverterade allt
utan problem. Kommandona nedan antar att man checkat ut konverteringsprogrammet och att man står i katalogen med skripten.

    $ perl sqlserver2pgsql/sqlserver2pgsql.pl -f script.sql -b script-before.sql -a script-after.sql -u script-unsure.sql -relabel_schemas 'dbo=>matdatabas'
    $ docker run -it --rm -v `pwd`:/scripts postgres:10 bash
    # cd /scripts
    # psql -U matdatabas -p 5432 -h xploredb01.prodstod.se -d matdatabas_utv -f script-before.sql
    
    …för över data med fme…
    
    # psql -U matdatabas -p 5432 -h xploredb01.prodstod.se -d matdatabas_utv -f script-after.sql 

Den första kommandorörelsen resulterar i 3 filer:
* script-before.sql innehåller tabelldefinitioner
* script-after.sql innehåller alla constraints.
* script-unsure.sql innehåller saker som skriptet hade problem med (bör vara ganska tom)

Dessa skript är incheckade, och bör uppdateras om vi tar in en ny dump med förändrad struktur.

### FME
Det finns ett FME-skript incheckat som är inställt att föra över data från en SQL Server på localhost till vår 
utvecklingsdatabas. Se katalogen fme.

### Framtida import
Vi kommer att behöva importera databasen från den temporära lösningen åtminstone en gång till inför driftsättning. Exakt
hur det kommer gå till beror lite på om det skett några schemaförändringar på nån av sidorna, men i stora drag:

* Läs in nya dumpen
* Rensa vår nuvarande databas 
* För över data via FME
* Applicera constraints