package se.metria.markkoll.service.dokument.dokumentgenerator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.docx4j.Docx4J;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.docProps.custom.Properties;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.service.dokument.TestBindingsFont;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.MemoryAppender;
import se.metria.markkoll.util.dokument.dokumentgenerator.DokumentGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static se.metria.markkoll.util.dokument.dokumentgenerator.DokumentWarnings.IMAGE_DATA_MISSING;

@SuppressWarnings("NonAsciiCharacters")
@MarkkollServiceTest
@DisplayName("Givet Dokumentgenerator")
public class DokumentGeneratorTest {
    DokumentGenerator dokumentGenerator = new DokumentGenerator();

    @Test
    void så_ska_textersättning_utföras_på_nästlade_fält() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestTemplate.docx").getInputStream();
        var data = new TestBindingsNested();
        data.getIntePrimitiv().setPrimitiv("testa testa");

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals("testa testa", getDocProperty(generated, "TEST-namn"));
    }

    @Test
    void så_ska_textersättning_kunna_utföras() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestTemplate.docx").getInputStream();
        var data = new TestBindings();
        data.setNamn("testfält");

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals("testfält", getDocProperty(generated, "TEST-namn"));
    }

    @Test
    void så_ska_textersättning_med_doc_property_som_är_null_ersättas_med_tom_sträng() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestTemplate.docx").getInputStream();
        var data = new TestBindings();
        data.setNamn(null);

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals("", getDocProperty(generated, "TEST-namn"));
    }

    @Test
    void så_ska_bildersättning_kunna_utföras() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestTemplate.docx").getInputStream();
        var data = new TestBindingsImage();
        data.setImage(new ByteArrayResource(new ClassPathResource("dokumentGenerator/image.png").getInputStream().readAllBytes()));

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals(1, countObjects(generated, Drawing.class));
    }

    @Test
    void så_ska_textersättning_med_datum_vara_möjligt() throws Exception {
        // Given
        var date = "2021-03-04";
        var docx = new ClassPathResource("dokumentGenerator/TestTemplate.docx").getInputStream();
        var data = new TestBindingsDate();
        data.setDate(LocalDate.parse(date));

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals(date, getDocProperty(generated, "TEST-namn"));
    }

    @Test
    void så_ska_textersättning_med_en_lista_vara_möjligt() throws Exception {
        // Given
        var items = Arrays.asList("1", "2", "3");
        var docx = new ClassPathResource("dokumentGenerator/TestTemplate.docx").getInputStream();
        var data = new TestBindingsList();
        data.setList(items);

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertTrue(hasString(generated, items.get(0)));
        assertTrue(hasString(generated, items.get(1)));
        assertTrue(hasString(generated, items.get(2)));
    }

    @Test
    void så_ska_textersättning_med_en_lista_och_repeating_section_vara_möjligt() throws Exception {
        // Given
        var items = Arrays.asList("1", "2", "3");
        var docx = new ClassPathResource("dokumentGenerator/TestRepeat.docx").getInputStream();
        var data = new TestBindingsList();
        data.setList(items);

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals(items.size(), countObjects(generated, Tbl.class));
        assertTrue(hasString(generated, items.get(0)));
        assertTrue(hasString(generated, items.get(1)));
        assertTrue(hasString(generated, items.get(2)));
    }

    @Test
    void så_ska_bildersättning_med_en_lista_vara_möjligt() throws Exception {
        // Given
        var items = CollectionUtil.asSet(
                new ByteArrayResource(new ClassPathResource("dokumentGenerator/image.png").getInputStream().readAllBytes()),
                new ByteArrayResource(new ClassPathResource("dokumentGenerator/image2.png").getInputStream().readAllBytes())
        );

        var docx = new ClassPathResource("dokumentGenerator/TestTemplate.docx").getInputStream();
        var data = new TestBindingsImageList();
        data.setImages(items);

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals(items.size(), countObjects(generated, Drawing.class));
    }

    @Test
    void så_ska_bildersättning_med_en_lista_och_repeating_section_vara_möjligt() throws Exception {
        // Given
        var items = CollectionUtil.asSet(
                new ByteArrayResource(new ClassPathResource("dokumentGenerator/image.png").getInputStream().readAllBytes()),
                new ByteArrayResource(new ClassPathResource("dokumentGenerator/image2.png").getInputStream().readAllBytes())
        );

        var docx = new ClassPathResource("dokumentGenerator/TestRepeat.docx").getInputStream();
        var data = new TestBindingsImageList();
        data.setImages(items);

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals(items.size(), countObjects(generated, Drawing.class));
        assertEquals(items.size(), countObjects(generated, Tbl.class));
    }

    @Test
    void så_ska_booleska_värden_ersättas_med_checkboxar() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestBoolean.docx").getInputStream();
        var data = new TestBindingsBoolean();
        data.setBooleanTrue(true);
        data.setBooleanFalse(false);

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals("☒", getDocProperty(generated, "TEST-cb1"));
        assertEquals("☐", getDocProperty(generated, "TEST-cb2"));
    }

    @Test
    void så_ska_bildersättning_med_doc_property_som_är_null_generera_varning() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestTemplate.docx").getInputStream();
        var data = new TestBindingsImage();
        data.setImage(null);
        var memoryAppender = getMemoryAppender();

        // When
        dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals(1, memoryAppender.search(IMAGE_DATA_MISSING.getValue(), Level.WARN).size());
    }

    @Test
    void så_ska_ersättning_med_multivärden_ske_för_alla_ställen_med_den_doc_propertyn() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestMultipleMulti.docx").getInputStream();
        var data = new TestBindingsMulti();
        var img = new ClassPathResource("dokumentGenerator/image.png").getInputStream().readAllBytes();
        data.text = Arrays.asList("sträng1", "sträng2");
        data.map = Arrays.asList(new ByteArrayResource(img), new ByteArrayResource(img));

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertEquals(2 * data.map.size(), countObjects(generated, Drawing.class));
        assertTrue(hasString(generated, "sträng1", 2));
        assertTrue(hasString(generated, "sträng2", 2));
    }

    @Test
    void så_ska_typsnittet_bli_korrekt_vid_textersättning() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestFont.docx").getInputStream();
        var data = new TestBindingsFont();
        data.setFont(List.of("text1"));
        data.setBigFont(List.of("text2"));

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertTrue(textHasFont(generated, "text1", null, 9));
        assertTrue(textHasFont(generated, "text2", "Times New Roman", 20));
    }

    @Test
    void så_ska_de_runs_som_hör_till_doc_propertyn_bytas_ut_istället_för_paragrafen() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestRuns.docx").getInputStream();
        var data = new TestBindingsRuns();
        data.setMarkagare(Arrays.asList("placeholder 1", "placeholder 2"));

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertTrue(hasString(generated, "Hej testrubrik"));
        assertTrue(hasString(generated, "placeholder 1"));
        assertTrue(hasString(generated, "placeholder 2"));
    }

    @Test
    void så_ska_de_runs_som_hör_till_doc_propertyn_bytas_ut_istället_för_paragrafen_i_repeatable_sections()
        throws Exception
    {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/TestRunsRepeatable.docx").getInputStream();
        var data = new TestBindingsRunsRepeatable();
        data.setKartor(Arrays.asList("placeholder 1", "placeholder 2"));

        // When
        var generated = dokumentGenerator.docxFromTemplate(docx, data);

        // Then
        assertTrue(hasString(generated, "Kartbilaga"));
        assertTrue(hasString(generated, "placeholder 1"));
        assertTrue(hasString(generated, "placeholder 2"));
    }

    @Test
    void så_ska_det_gå_att_fylla_ett_dokument_med_doc_properties() throws Exception {
        // Given
        var docx = new ClassPathResource("dokumentGenerator/EmptyDoc.docx").getInputStream();
        var docProperties = Arrays.asList("TEST-namn");

        // When
        var generated = dokumentGenerator.addDocProperties(docx, TestBindingsNested.class);

        var sortedDocProperties = new ArrayList<>(docProperties);
        Collections.sort(sortedDocProperties);

        // Then
        assertLinesMatch(sortedDocProperties, getDocProperties(generated));
    }

    private String getDocProperty(ByteArrayResource docxResource, String docProperty) throws IOException, Docx4JException {
        var wordMLPackage = Docx4J.load(docxResource.getInputStream());
        var docProps = wordMLPackage.getDocPropsCustomPart();

        return docProps.getProperty(docProperty).getLpwstr();
    }

    private Boolean hasString(ByteArrayResource docxRersource, String value) throws Exception {
        return hasString(docxRersource, value, 1);
    }

    private Boolean hasString(ByteArrayResource docxRersource, String value, Integer multiplicity) throws Exception {
        var wmlPackage = Docx4J.load(docxRersource.getInputStream());
        var textFinder = new ClassFinder(Text.class);
        new TraversalUtil(wmlPackage.getMainDocumentPart().getContent(), textFinder);

        var count = 0;
        for (Object o : textFinder.results) {
            Object o2 = XmlUtils.unwrap(o);
            if (o2 instanceof Text) {
                Text t = (Text)o2;
                if (t.getValue().equals(value)) {
                    count++;
                }
            }
        }

        return count == multiplicity;
    }

    private Integer countObjects(ByteArrayResource docx, Class<?> clazz) throws Exception {
        var wmlPackage = Docx4J.load(docx.getInputStream());
        var drawingFinder = new ClassFinder(clazz);
        new TraversalUtil(wmlPackage.getMainDocumentPart().getContent(), drawingFinder);

        return drawingFinder.results.size();
    }

    private MemoryAppender getMemoryAppender() {
        Logger logger = (Logger) LoggerFactory.getLogger(DokumentGenerator.class);

        var memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.WARN);
        logger.addAppender(memoryAppender);
        memoryAppender.start();

        return memoryAppender;
    }

    private boolean textHasFont(ByteArrayResource docx, String text, String fontName, Integer fontSize) throws Exception {
        var wmlPackage = Docx4J.load(docx.getInputStream());
        var textFinder = new ClassFinder(Text.class);
        new TraversalUtil(wmlPackage.getMainDocumentPart().getContent(), textFinder);

        for (Object o : textFinder.results) {
            Object o2 = XmlUtils.unwrap(o);
            if (o2 instanceof Text) {
                Text t = (Text) o2;
                if (t.getValue().equals(text)) {
                    var parent = (R) t.getParent();
                    var rpr = parent.getRPr();
                    var actualFontSize = rpr.getSzCs().getVal().intValue();
                    if (actualFontSize == fontSize * 2) {
                        // fontName == null ger "default" typsnitt
                        if (fontName == null || rpr.getRFonts().getAscii().equals(fontName)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private List<String> getDocProperties(ByteArrayResource docx) throws IOException, Docx4JException {
        var wmlPackage = Docx4J.load(docx.getInputStream());

        var docProps = wmlPackage.getDocPropsCustomPart();
        return docProps.getContents().getProperty().stream()
            .map(Properties.Property::getName)
            .collect(Collectors.toList());
    }
}
