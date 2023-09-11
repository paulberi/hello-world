package se.metria.xplore.sok.util;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.junit.jupiter.api.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.core.io.ClassPathResource;
import se.metria.xplore.sok.exceptions.GmlConversionException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GmlUtilIT {

    @Test
    public void testSuccess() throws GmlConversionException, IOException {
        String xml = readFile("gml/GML_ISO-8859-1.xml", StandardCharsets.ISO_8859_1);
        SimpleFeatureCollection sfc = GmlUtil.gml2sfc(xml, StandardCharsets.ISO_8859_1);

        assertEquals(6, sfc.size());

        SimpleFeatureIterator iter = sfc.features();
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            assertEquals("GRANNÄSUDDEN", feature.getAttribute("ORTNAMN"));
        }
        iter.close();
    }

    @Test
    public void testInvalidEncodingDuringDecoding() throws GmlConversionException, IOException {
        String xml = readFile("gml/GML_ISO-8859-1.xml", StandardCharsets.ISO_8859_1);
        SimpleFeatureCollection sfc = GmlUtil.gml2sfc(xml, StandardCharsets.UTF_8);

        assertEquals(6, sfc.size());

        SimpleFeatureIterator iter = sfc.features();
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            String ortnamn = feature.getAttribute("ORTNAMN").toString();
            assertNotSame("GRANNÄSUDDEN", ortnamn);
            assertTrue(ortnamn.startsWith("GRANN"));
            assertTrue(ortnamn.endsWith("UDDEN"));
        }
        iter.close();
    }

    @Test
    public void testUnexpectedEncodingOnSourceGML() throws IOException {
        String xml = readFile("gml/GML_UTF-8.xml", StandardCharsets.UTF_8);
        assertThrows(GmlConversionException.class, () -> {
            GmlUtil.gml2sfc(xml, StandardCharsets.ISO_8859_1);
        });
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        File resource = new ClassPathResource(path).getFile();
        return new String(Files.readAllBytes(resource.toPath()), encoding);
    }
}
