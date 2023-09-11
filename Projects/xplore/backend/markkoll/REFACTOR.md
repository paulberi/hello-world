# GeometryService
Varför är det en @Service? @Transactional?
Verkar vara en generell klass som läser ut SimpleFeatures from en Shapefil.

Begreppen "SKANOVA" och "OWNER" borde inte höra hemma här.

# GeometryHelper
Om det inte är en instans av Geometry så castas det till Geometry ändå. Måste finnas nån felhantering här. 

# ShapeFileHelper
Blandning av generell hantering av Zip-filer, Shapefiler och Intrång. Gör ansvaret mer uppenbart.
 
# IntrangService
intrangFromFeatures känns lite märklig. Det är @Transactional och loggar att den importerat saker men den gör
inte det.
