# Avtalskarta
Metoden initializeMap() som hämtar kartlager för intrång, fastighetsgränser och översiktskarta 
behöver ses över och förenklas, speciellt beräkningen av om översikten ska visas "area_factor" är oklart 
hur den fungerar och behöver få lite kärlek. Även tester för presenter-klass behövs.

# LayerDef

Är det nån mening att använda LayerDef och andra typer från xplore när man inte
använder kartkomponenten, LayerService osv? Känns som att det bara blir
dubbelarbete om semantiken i optionerna ska efterlevas (och gör man det inte
är det bara förvirrande).

# Hanteringen av click

Kartan har en @Output som returnerar fastighetsbeteckning. Borde gå att göra
bättre och snygga till koden.

# Fixa tydligheten i kartan

Det ser inte bra ut när rasterbilder inte ligger i sin naturliga
inzoomningsnivå (dvs att man kan zooma in mellan 2 olika zoomnivåer). Det går
att förbjuda att det händer (constrainResolution) men då blir upplevelsen när
man zoomar dålig på trackpad, magic mouse och dylikt.

Kanske blir det nog bra om vi alltid går till en heltals-zoom-nivå när vi
zoomar in till saker automatiskt (projekt, fastigheter)

# Väldigt många WMS-requests

Kan vi slå ihop våra egna lager till ett lager i openlayers?

Kan vi använda ImageWMS istället för TileWMS?


