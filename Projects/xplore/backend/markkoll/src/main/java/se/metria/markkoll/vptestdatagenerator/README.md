# VpTestdataGeneratorApplication
Generera värderingsprotokoll i .xlsx-format från specifikationer i .json-format. Det här används
för att generera data för tester till beräkningsmodellen för värderingsprotokollen i Markkoll
frontend.

## Användning:
Skapa en run configuration och kör applikationen från IntelliJ genom att trycka på den gröna
pilen till vänster om "public static void main" och välja "Run...", eller
tryck CTRL+SHIFT+F10 (på Win10, vem vet vilken skum kombination som används på Mac).
 
Två argument behövs i "Program arguments" i konfigurationen för körningen:  
Första argumentet är sökvägen till de json-filer som ska användas för att generera
värderingsprotokollen. JSON-filer för Energibolagens VP finns i "energibolagen_vp_json" i
resources-foldern.  
Andra argumentet är sökvägen där värderingsprotokollen kommer att hamna.