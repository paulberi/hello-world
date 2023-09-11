# DokumentGenerator

Generera dokument i antingen DOCX- eller PDF-format utifrån en Wordmall. 

## Använding
Dokumentgeneratorn letar i en given Wordfil efter DocProperties, och utför textersättning (eller bildersättning) på dessa, med data som användaren av dokumentgeneratorn bifogar.

Med en @DocProperty-annotation så är det möjligt att binda ett värde i ett klassfält till en viss DocProperty:

```Java
class Bindings {
    // Defaultbeteende är att textersättning sker med fältet toString()-metod
    @DocProperty("Default")
    String value;

    // Booleans kommer att ersättas med kryssrutor i dokumentet
    @DocProperty("Boolean")
    Boolean aBoolean;

    // ByteArrayResource tolkas som bilddata, och ersätts med bilder
    @DocProperty("Bild")
    ByteArrayResource aBoolean;

    /* Samlingar av värden (List, Set, o.s.v.) godtas.

    Om en DocProperty som representerar en samling är innesluten i en repeating section content control i
    Worddokumentet, så kommer t.ex. tabellrader inuti content controlen att klonas. 
    */
    @DocProperty("Multiple")
    List<String> values;
}
```

Ta en Wordmall och bindningar, kör dokumentgeneratorn, och du får en ny Wordfil (eller pdf):

```Java
File docxTemplate = new File("templatefil.docx");
Bindings data = ...;

var dokumentGenerator = new DokumentGenerator();
var generatedDocx = dokumentGenerator.docxFromTemplate(docxTemplate, data);
```

## Crash course i WordML--och annat--för den som är olycksalig nog att behöva gräva i koden för dokumentgeneratorn:
Ett Worddokument representeras i huvudsak av en uppsättning XML-filer, där kanske dokumentfilen är den viktigaste.
Dokumentfilen har en hierarkistruktur: Varje dokument har en body. En body innehåller en eller flera paragrafer,
och varje paragraf innehåller en eller flera runs. En run i sin tur innehåller vanligtvis text, men även bilder o.s.v.
är möjligt:

```xml
<w:document>
    <w:body>
        <w:p>
            <w:r>
                <w:t>The cake is a lie</w:t>
            </w:r>
        </w:p>
    </w:body>
</w:document>
```

Document Properties (eller DocProperties) representeras av Fields i WordML, som man kan se som "uppdaterbara" värden,
eller variabler. Ett field är antingen simple eller complex. Jag har själv inte riktigt grävt i varför den distinktionen
görs än. För nuvarande så är det tillräckligt att veta att de finns, och hur strukturen skiljer sig åt.

Ett simpelt Field består av en fldSimple-tag inuti en paragraf (eller en run):
```xml
<w:p>
    <w:fldSimple w:instr=" DOCPROPERTY Namn-på-doc-property \* MERGEFORMAT ">
        <w:r>
            <w:t>DocPropertyns värde, som sätts när vi uppdaterar dokumentets fields</w:t>
        </w:r>
    </w:fldSimple>
</w:p>
```

Komplexa fields är som sagt mer komplexa. Ett komplext field fyller flera runs, och ser ut som följer:
```xml
<w:r>
    <w:fldChar w:fldCharType="begin"/>
</w:r>
<w:r>
    <w:instrText xml:space="preserve"> DOCPROPERTY Namn-på-doc-property \* MERGEFORMAT </w:instrText>
</w:r>
<w:r>
    <w:fldChar w:fldCharType="separate"/>
</w:r>
<w:r>
    <w:t xml:space="preserve">DocPropertyns värde, som sätts när vi uppdaterar dokumentets fields</w:t>
</w:r>
<w:r>
    <w:fldChar w:fldCharType="end"/>
</w:r>
```

För skalära värden så är textersättning simpelt: vi sätter ett nytt värde på DocPropertyn i Word, sen uppdaterar vi
fälten i dokumenten. För bilder, och arrayer av värden, blir saker mer komplicerade. Det vi gör där är att ändra i
själva dokumentets trädstruktur: Vi plockar bort våra fields från dokumentets XML, och ersätter det med antingen en bild,
eller den array med värden som vi angett.