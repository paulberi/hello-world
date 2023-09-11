package se.metria.xplore.sok.util;

import org.apache.commons.io.IOUtils;
import org.geotools.wfs.GML;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.xml.sax.SAXException;
import se.metria.xplore.sok.exceptions.GmlConversionException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class GmlUtil {

    private static final GML gml = new GML(GML.Version.GML3);

    public static SimpleFeatureCollection gml2sfc(String xml, Charset charset) throws GmlConversionException {
        try {
            // Behövs eftersom man annars får följande:
            // Parsing failed for Envelope: java.lang.RuntimeException: Could not create crs: urn:x-ogc:def:crs:EPSG:3006
            xml = xml.replaceAll("urn:x-ogc:def:crs:", "");

            InputStream is = IOUtils.toInputStream(xml, charset);

            return gml.decodeFeatureCollection(is);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
            throw new GmlConversionException();
        }
    }
}
