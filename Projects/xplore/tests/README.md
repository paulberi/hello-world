# Automatiska tester
"E2E" eller integrationstester mot våra olika miljöer och applikationer. Testar att grundläggade funktionalitet är
på plats.

## Cucumber
Testerna använder sig av [cucumber](https://cucumber.io/) som är ett ramverk för beteende driven utveckling. 
Det finns bra [guider](https://cucumber.io/docs/guides/) som beskriver hur det funkar och hur man skriver tester.

Lite förenklat kan man säga att man beskriver ett eller flera scenarion i .feature-filer t.ex:
>**Egenskap**: Hämta en kommuns verksamhetsobjekt
> 
>**Scenario**: En admin hämtar verksamhetsobjekt  
>**Givet** att jag är inloggad som en admin   
>**Och** att det finns ett läsbart lager   
>**När** jag försöker läsa från lagret   
>**Så** ska jag få en lista med objekt  

Ett scenario innehåller ett antal steg, i detta fall fyra stycken, och dessa steg behöver mappas till metoder som utför eller verifierar det steget vill göra. 

För att mappa en metod till ett steg i scenariot så annoterar man metoden med stegbeskrivningen. 
```java
@Givet("att jag är inloggad som en admin")
public void inloggad_som_en_admin() {
  String username = config.getProperty("adminUser");
  String password = config.getProperty("adminUserPassword");
  session.login(tokenUrl, "geovis-vy", username, password);
}

@Givet("att det finns ett läsbart lager")
public void ett_läsbart_lager() {
  featureType = config.getProperty("wfsFeatureType");
}

@När("jag försöker läsa från lagret")
public void läs_från_lagret() {
  result = wfsClient.getFeatures(owsUrl, featureType, 10);
}

@Så("ska jag få en lista med objekt")
public void en_lista_med_objekt() {
  List<String> list = from(result).getList("FeatureCollection.featureMembers.fastighetsytor.@gml:id");
  assertTrue(!list.isEmpty());
}
```

## Köra lokalt

Det går att köra testerna i IntelliJ men om man vill kan man också köra via mvn och terminalen.

Kör skogsanalys-tester mot defaultmiljön (utv):

    $ mvn test -pl skogsanalys -am

Kör samma tester mot test-miljön:

    $ mvn test -pl skogsanalys -am -Dskogsanalys.config=skogsanalys-test
    

Testerna körs på liknande sätt mot de andra applikationera.

## Bamboo
Finns uppsatt planer för att köra dem i [Bamboo](https://bamboo.metria.se/browse/GE).