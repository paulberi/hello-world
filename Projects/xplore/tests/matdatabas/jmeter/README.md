#Prestandatester i jMeter
https://jmeter.apache.org/
Tester framtagna i samband med släppet av tjänsten. Obs att det finns lokala variabler i scripten som **måste** ändras där resultatfiler skrivs (för lyssnare "Summary Report" som skriver till disk).
Testerna är uppsatta att gå mot intern miljö där basic auth finns på http://xploreutv01.prodstod.se:9009 
ToDo; skapa upp auth mot keycloak

##Köra
Antingen körs test inkl. generering av rapportfil i ett, eller så körs rapportgenereringen för sig efter testet körts (från GUI tex). Rapportfilen kan enbart genereras från konsol, medan testerna går att köra antingen via GUI eller via konsol. Dom tester som har "user-properties" i sin mapp behöver köras utifrån den mappen när resultatfilerna genereras, då det finns vissa egenskaper där som kan vara önskvärda.

Dokumentation hur rapporter genereras: https://jmeter.apache.org/usermanual/generating-dashboard.html
Dokumentation om vad de olika parametrarna betyder för körning via command line: https://jmeter.apache.org/usermanual/get-started.html#non_gui

För att köra hela testet och generera resultat ex.vis för granska.jmx via konsol:
jmeter -n -t C:\/**SÖKVÄG TILL JMETERTEST**granska.jmx -Jusers=1 -Jrampup=1 -Jloop=10 -l C:\/**SÖKVÄG TILL MAPP DÄR RESULTATFIL SKRIVS**.csv -f -e -o C:\/**SÖKVÄG TILL MAPP DÄR RAPPORT SKAPAS**

För att generera rapportfil utifrån ett genererat resultat:
λ jmeter -g C:\/**SÖKVÄG TILL SKAPAD RESULTATFIL**.csv -f -e -o C:\/**SÖKVÄG TILL MAPP DÄR RAPPORT SKAPAS**

Observera att parametrarna -Jusers=1 -Jrampup=1 -Jloop=10 kan ändras efter tycke och smak (läses av testets "thread") där Jusers är antalet användare/threads som körs, Jrampup är tiden till dess att totala antalet användare uppnås och Jloop är hur många gånger en tråd/användare kör testet. Om inget anges är default 1.

##Tester
###Logga in
Simpelt test som kör de anrop som görs Efter att en användare loggat in.

###Rapportera Mätvärde
Komplext flöde där anrop görs som motsvarar när en användare rapporterar in mätdata för de fem första mätobjekten i en slumpvis mätrunda. Komplexiteten ligger i att använda data som inte är statiska då det finns begränsningar vad som får rapporteras och vilka objekt som det går att rapportera data på.
Steg i skriptet
- Öppna rapporteringsfliken och välj slumpvis mätrunda
- Stega till runda i dropdown och "Visa alla mätobjekt i kartan"
- Läsa upp vald runda inkl hämtning av bilder för mätobjekt
- Rapportera mätvärden

###Granska Mätvärde
Komplext flöde där användare läser upp och granskar fem inrapporterade mätvärden (antal går att ställa in i användarvariablerna i skriptet). 
Steg i skriptet:
- Öppna granskningsfliken och välja en slumpvis mätrunda
- Visa mätrundan
- Godkänna mätvärden (indata baseras på tidigare mätvärde för objeket som det rapporteras på)

###Svarstid resp mätrunda
Simpelt test för att kontrollera svarstider för de olika mätrundor som finns (stor variation av storlek på rundorna)