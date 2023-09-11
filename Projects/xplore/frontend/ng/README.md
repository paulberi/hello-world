# Xplore frontends
Här bor flera applikationer som alla lever i samma Angular "workspace". Det innebär att de i angular.json är definierade
som separata projekt, och ligger i egna kataloger under projects/. Det innebär också att om vi uppdaterar
angular version så uppdaterar vi alla samtidigt.

Tanken är dela så mycket kod som möjligt mellan de olika applikationerna
och att inte låta dem divergera.

## Snabbstart
Först hämtar vi alla dependencies:

    $ npm install

Sedan kan man starta en lokal dev-server genom t.ex.:

    $ npm start skogsanalys

I exemplet används skogsanalys som applikationsnamn men det hade kunnat
vara t.ex. geovis-extern istället, eller något av projektet under projects/. 

Via dev-servern får man automatiskt reload vid ändringar och möjlighet att debugga med typescript-filer isället för genererad javascript.

## Utveckla med Storybook
För att skapa återanvändbara och avgränsade Angular-komponenter strävar vi efter att utveckla dessa isolerat från applikationerna 
med hjälp av [Storybook](https://storybook.js.org/tutorials/intro-to-storybook/angular/en/get-started/).


* Storybook består av så kallades `stories`, vilka beskriver olika tillstånd 
som en komponent kan befinna sig i. Stories för en komponent finns i en fil som läggs i komponentens mapp och heter t.ex.
[message.stories.ts](projects/lib/ui/feedback/message/message.stories.ts).
* I Storybook finns ett komponentbibliotek med samtliga komponenter som har stories-filer. 
* Ändringar i komponenter uppdateras automatiskt i Storybook.
* Kommenterarer på komponenternas @Input- och @Output-parametrar används för att skapa en API-dokumentation av komponenten.

Starta upp lokalt:
```
npm run storybook
```
Storybook finns sedan tillgänglig på: [http://localhost:6006](http://localhost:6006).

## Xplores designsystem
Xplores designsystem består av ett bibliotek med återanvändbara Angular-komponenter samt riktlinjer för tex. färger och typsnitt som används för att bygga upp
våra applikationer. Syftet är att skapa en enhetlighet i designen, förbättra tillgängligheten och snabba på utvecklingen genom att återanvända komponenter.

Designsystemet byggs upp med Storybook och finns tillgänglig på [https://xplore-utv.metria.se](https://xplore-utv.metria.se).
Riktlinjer finns sparade som .mdx-filer i mappen `[designsystem](projects/storybook/stories/designriktlinjer).

### CI (Continuous Integration)
När bamboo upptäcker att någon pushat till respektive projektkatalog (eller i gemensamma delar, dvs lib/) i git-repot
bygger den en docker image, samt taggar det med ett inkrementerande byggnummer.

Sedan deployar den till utv-miljöns docker-swarm.

### Köra med lokala backend-tjänster
När man utvecklar vill man ibland köra med lokala instanser av backendtjänster vilket kan göras genom att ändra
den aktuella URL:en i respektive [environment.ts](projects/skogsanalys/src/environments/environment.ts) till 
att peka mot localhost istället.

Vill man t.ex. köra mot en lokalt söktjänst görs detta genom att ändra:

    geocoderUrl: "/api/sok/geocode" --> geocoderUrl: "http://localhost:9005/geocode"
    
## Konfiguration
Flera av applikationerna hämtar vid uppstart sin konfiguration från konfigurationstjänsten. 
Dessa versionshanteras [här](../../backend/config/app-config/src/main/resources/config).

## Hantering av moduler
Vi arbetar med moduler enligt Angulars standard, mer finns att läsa här https://angular.io/guide/feature-modules

Genom att dela upp komponenter i feature-modules slipper man hantera enkilda importer i app.module, man behöver bara importera de 
feature-modules som man använder. Genom att varje module hanterar sina egna importer, så blir det väldigt enkelt att hålla reda på när de används. 
En feature-module och dess dependencies kan lazyloadas, vilket gör att applikationen får en snabbare firstload.

## Enhetstester
Det ramverk som används för enhetstester är [Jest](https://github.com/facebook/jest).

Enklaste sättet att köra är via jest [CLI-verktyg](https://jestjs.io/docs/en/cli), som då behöver installeras globalt via npm
(det är redan inkluderat i dev dependencies för detta projekt):

`npm install jest --global`

För att få det att funka på macOS kan man behöva köra `brew install watchman`

Skript för att köra test via `npm test` har modifierats för att istället använda sig av jest. När `npm test` körs genereras även en enkel html-rapport med testresultatet i ng-foldern.
Vid behov kan även jest köras mot enskilda folders och filer, till exempel:

`jest lib/map/services/sok.service.spec.ts #Kör enskilt test`

`jest lib/map/services/* #Kör igenom samtliga spec-filer i services (och undermappar)`

I och med införandet av Jest har Karma tagits bort helt från projektet.
Anledning till att byta från Karma till Jest är av flera skäl:
* Jest har ett funktionsrikt CLI-verktyg, är väldokumenterat samt har ett aktivt community.
* Kan göra testning mha snapshots (mer om detta nedan).
* Är snabbare (både watch-läget och manuell testkörning) och enklare att köra tester utan att behöva bygga hela projektet eller starta en devserver.
* Tidigare skrivna tester kan utan problem behållas.
* Stöd för att köra testerna direkt i IntelliJ.


Referenser till jasmine finns kvar på vissa ställen då e2e-klientester via Protractor fortfarande ska vara möjliga.
Dock är det möjligt att köra e2e-tester via Jest + Puppeteer.

Jest kan även köra tester mot snapshots av komponenter. När enhetstester körs kommer en jämförelse mellan snapshotten och den html som genereras vid körning av testet.
Snapshotsen består av html och genereras (i mappen \_\_snapshots\_\_) initalt då man använder `toMatchSnapshot()` första gången. Dessa snapshots ska sedan versionshanteras.
Om en snapshot behöver uppdateras (vid till exempel medvetna ändringar till komponenten) görs detta på följande sätt:

`jest pathOrPattern/to/spec/files  -u`

Rekommenderat är att endast köra testning mha snapshot för mindre komponenter.
