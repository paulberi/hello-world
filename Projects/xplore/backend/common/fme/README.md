# FME-klient
Klienten används av alla tjänster som behöver anropa FME (t.ex. skogsanalystjänster) och är därför väldigt generaliserad.

Tanken är att slippa duplicera samma kod i alla tjänsterna och därigenom ha mindre redundans och kod att underhålla.

## Konfiguration

Görs i fme.properties med t.ex. host, inloggningsuppgifter och anropstimeouter.

Inställningarna kan överridas i de enskilda tjänsterna genom Spring Boots inbyggda hierarkiska sätt att hantera properties.

## Felhantering
Anropet till FME är hårdkodat till att innehålla en begäran om att returnera felen i JSON-format. 
Dessa fel parsas och mappas om till felen som finns specade i FmeError.  

Rent implementationsmässigt hanteras felen mha FmeResponseErrorHandler och FmeExceptionHandler.
