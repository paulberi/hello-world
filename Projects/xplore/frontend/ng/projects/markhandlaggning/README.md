# Required
- VSCode
- IntelliJ
- Apache-maven 3.6.3
- Java 11
- Angular CLI

# Markhandläggning Frontend
För att starta frontenden måste du först klona ner xplorerepot, och navigera till frontend/ng/projects foldern
Sedan hämtar vi alla dependencies:

    $ npm install

Sedan kan man starta en lokal dev-server genom:

    $ ng serve markhandlaggning --aot

Öppna din webbläsare och gå till ```localhost:4206```

# Markhandläggning Backend
För att starta backenden måste du istället navigera in i backendmappen, 
i terminalen kör du
    
    $ mvn package -pl markhandlaggning -am

sedan startar du **MarkhandlaggningApplication** via IntelliJ