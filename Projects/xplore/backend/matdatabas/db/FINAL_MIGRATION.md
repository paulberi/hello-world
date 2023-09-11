# Migrering av dump från SLL

## Förkrav
* En dumpfil från SLL (SQL Server) (.bak)
* Docker för att köra SQL server
* FME Desktop 2019

## Starta SQL Server
Se [docs från MS](https://docs.microsoft.com/en-us/sql/linux/quickstart-install-connect-docker?view=sql-server-linux-2017&pivots=cs1-bash).

I korthet:

    docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=SLLmigrering2" -p 1433:1433 --name sql2 -d -v /Users/anv/sll:/dump mcr.microsoft.com/mssql/server:2017-latest

Här mountas en volym under /dump så vi kan komma åt .bak-filen inuti countainern.
 
 OBS: Ändra sökväg till den lokala dumpfilen.

## Importera dumpen
Öppna ett skal i containern:

    docker exec -it sql2 "bash"

Sedan kan vi köra importen:

    /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P SLLmigrering2 -Q "RESTORE DATABASE [Miljodatabas] FROM DISK = N'/dump/Miljodatabas 20200406.bak' WITH FILE = 1, MOVE N'Miljodatabas' TO N'/var/opt/mssql/data/Miljodatabas.mdf', MOVE N'Miljodatabas2' TO N'/var/opt/mssql/data/Miljodatabas2.ndf', MOVE N'Miljodatabas_log' TO N'/var/opt/mssql/data/Miljodatabas_log.ldf', NOUNLOAD, REPLACE, STATS = 5"

OBS: se till att namnet på .bak-filen är korrekt.

## Migrera med FME

Nu ska det gå att migrera till en clean instans av vår PostgreSQL-databas.

Öppna final_migration.fmw, ändra parameterar för Database Connection för PostgreSQL om nödvändigt
(förinställt för localhost). Parametrarna för SQL Server behöver inte ändras.

Tryck på start.
