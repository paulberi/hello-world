# Workspaces och administrativa script för Geoserver
I denna katalog finns ett antal script för att synkronisera workspace-konfiguration mellan Geoserver-installationer,
t ex utv-miljön och testmiljön. Syftet är att etablera ett arbetssätt där man kan konfigurera resurserna via
Geoserverns vanliga webbgränssnitt i -utv, och sen "pusha" ändringarna till -test och vidare till -prod utan manuella
handgrepp på Geoservern i dessa miljöer.

I katalogen finns även, som en följd av att man kör scripten, en kopia av alla resurser som syncas.
Dessa versionshanteras som vanligt via git.

## Definitioner
*Resurs* -  en resurs på Geoservern, det kan vara en stil, ett lager, en workspace.
            Varje resurs identifieras unikt via en REST-path.

*Workspace* - den mest överordnade resursen på Geoservern, en sorts behållare i vilken övriga resurser som
              kartlager ordnas. Det är normalt på Workspace-nivån vi konfigurerar behörigheter i Geoservern.

## Script
[dump.sh](dump.sh)      - laddar ner en workspace från en käll-Geoserver
[upload.sh](upload.sh)  - laddar upp en workspace till en käll-Geoserver

[dump-markkoll.sh](dump-markkoll.sh)    - Wrapper runt [dump.sh](dump.sh) som konfigurerar vilka resurser
                                            som ska syncas för workspacen ```markkoll```

## Avgräsnsningar
Dessa script är medvetet inte särskilt avancerade, målet är att stödja ett staging-arbetsflöde där man kopierar
resurser från en Geoserver till en annan. De är inte gjorda för att t ex programmatiskt skapa helt nya workspaces
eller lager. Där är det sannolikt mer lämpligt att använda ett API, t ex de python- eller Java-APIer vi använt
för att sätta upp nya kunder i Skogsmaskindata och Geovis.  
            
## Arbetsflöde för publicering
De script som körs följer ett enkelt flöde:
1. Filer för ett utpekat workspace hämtas via HTTP REST-anrop från käll-Geoservern
2. Motsvarande workspace på mål-Geoservern ersätts **i sin helhet** med filerna som hämtats
 
### Uppdatering av befintliga resurser
1. Genomför de ändringar som avses göras i workspacen ```markkoll``` på Geoservern lokalt eller i utv-miljön
2. Kör scriptet ```dump-markkoll.sh```
3. (Valfritt) Kolla diffarna i filerna som uppdaterats i [workspaces/] så de stämmer överens med förväntingarna
4. Kör scriptet ```upload.sh``` för att ladda upp resurserna till mål-Geoservern

### Hantering av nya resurser
1. Lägg till ett nytt skikt på Geoservern lokalt eller i utv-miljön
2. Redigera scriptet ```dump-markkoll.sh``` och lägg till de nya resurserna
    1. Här behöver man ha lite koll på strukturen på workspaces i REST-APIet.
       Publicerar man t ex en nytt skikt från en databastabell som inte tidigare varit publicerad behöver det skapas
       flera resurser: en FEATURETYPE, ett LAYER och sannolikt en STYLE också, om man skapat en ny sådan i Geoservern.
       Publicerar man samma tabell igen, men som ett nytt lager, ska FEATURE-definitionen finnas sen tidigare
2. Kör scriptet ```dump-markkoll.sh```
3. (Valfritt) Kolla diffarna i filerna som uppdaterats i [workspaces/] så de stämmer överens med förväntingarna
4. Kör scriptet ```upload.sh``` för att ladda upp resurserna till mål-Geoservern

### Ta bort resurser
I grunden behöver resursen tas bort från ```dump-markkoll.sh```, eftersom scriptet inte detekterar att en tidigare
hanterad resurs blivit borttagen.
*Förbättringsförslag*: Kanske lika bra att dump-scriptet rensar hela workspace-katalogen (för angiven workspace) innan
det laddar ner scripten på nytt? Gäller ju att ha lite koll så man inte checkar in deleten om något går fel.
Kanske backup-recovery från scriptet?


# TODOs och förbättringsförslag
Specifika script för varje workspace känns lite onödigt, borde göras om så markkoll-workspacen bara har en configfil
som listar resurserna. Sen kan den läsas in av dump.sh 
