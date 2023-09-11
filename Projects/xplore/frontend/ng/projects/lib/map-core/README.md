# Map Core

Modulen innehåller basfunktionalitet för kartvisning.

Syftet är inte att tillhandahålla diverse widgets och tillägsfunktioner.

## CSS
Viss global CSS som kommer från openlayers måste inkluderas i den användade applikationen. Om man använder SCSS kan man skriva

    @import url(~ol/ol.css);

i sin styles.scss. Flera applikationer får den här filen indirekt via andra stil-imports så se till att den inte kommer med dubbelt.
