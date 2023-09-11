package se.metria.skogsmaskindata.service.imports.shapefile;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShapefileHandlerTest {

    @Test
    void testGodkand() throws IOException, ZipException, ShapefileException {
        ShapefileHandler sh = new ShapefileHandler(unzip("imports/G1254789_G.zip"));

        assertNotNull(sh);

        sh.readFiles();

        assertTrue(sh.isSuccess());

        assertFalse(sh.getFeatures(ShapefileType.KORSPAR).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.RESULTAT).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.PROVYTA).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.KORSPAR_FOR_TRANSPORT).isEmpty());

        // This shapefile is present but has no features
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSPUNKT).isEmpty());

        // These shapefiles aren't present
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSLINJE).isEmpty());
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSPOLYGON).isEmpty());
    }

    @Test
    void testUnderkand() throws IOException, ZipException, ShapefileException {
        ShapefileHandler sh = new ShapefileHandler(unzip("imports/G1254789_U.zip"));

        assertNotNull(sh);

        sh.readFiles();

        assertFalse(sh.isSuccess());

        assertFalse(sh.getFeatures(ShapefileType.KORSPAR).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.RESULTAT).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.PROVYTA).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.KORSPAR_FOR_TRANSPORT).isEmpty());

        // This shapefile is present but has no features
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSPUNKT).isEmpty());

        // These shapefiles aren't present
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSLINJE).isEmpty());
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSPOLYGON).isEmpty());
    }

    @Test
    void testMinimal() throws IOException, ZipException, ShapefileException {
        ShapefileHandler sh = new ShapefileHandler(unzip("imports/G1254789_minimal.zip"));

        assertNotNull(sh);

        sh.readFiles();

        assertFalse(sh.isSuccess());

        assertFalse(sh.getFeatures(ShapefileType.KORSPAR).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.RESULTAT).isEmpty());

        // These shapefiles aren't present
        assertTrue(sh.getFeatures(ShapefileType.PROVYTA).isEmpty());
        assertTrue(sh.getFeatures(ShapefileType.KORSPAR_FOR_TRANSPORT).isEmpty());
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSPUNKT).isEmpty());
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSLINJE).isEmpty());
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSPOLYGON).isEmpty());
    }

    @Test
    void testUtanKorspar() {
        assertThrows(ShapefileException.class, () -> {
            ShapefileHandler sh = new ShapefileHandler(unzip("imports/G1254789_utan_korspar.zip"));
            sh.readFiles();
        });
    }

    @Test
    void testUtanResultat() {
        assertThrows(ShapefileException.class, () -> {
            ShapefileHandler sh = new ShapefileHandler(unzip("imports/G1254789_utan_resultat.zip"));
            sh.readFiles();
        });
    }

    @Test
    void testIpOchIpol() throws IOException, ZipException, ShapefileException {
        ShapefileHandler sh = new ShapefileHandler(unzip("imports/ip_ipol.zip"));

        assertNotNull(sh);

        sh.readFiles();

        assertTrue(sh.isSuccess());

        assertFalse(sh.getFeatures(ShapefileType.KORSPAR).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.RESULTAT).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.INFORMATIONSPUNKT).isEmpty());
        assertFalse(sh.getFeatures(ShapefileType.INFORMATIONSPOLYGON).isEmpty());

        // These shapefiles aren't present
        assertTrue(sh.getFeatures(ShapefileType.INFORMATIONSLINJE).isEmpty());
        assertTrue(sh.getFeatures(ShapefileType.KORSPAR_FOR_TRANSPORT).isEmpty());
        assertTrue(sh.getFeatures(ShapefileType.PROVYTA).isEmpty());
    }

    @Test
    void testBatch() throws IOException, ZipException, ShapefileException, URISyntaxException {
        Collection<File> files = getFileList("/imports/batch");

        for (File file : files) {
            ShapefileHandler sh = new ShapefileHandler(unzip(file));
            sh.readFiles();
            assertFalse(sh.getFeatures(ShapefileType.KORSPAR).isEmpty());
            assertFalse(sh.getFeatures(ShapefileType.RESULTAT).isEmpty());
        }
    }

    public static File unzip(String path) throws IOException, ZipException {
        return unzip(new ClassPathResource(path).getFile());
    }

    public static File unzip(File file) throws IOException, ZipException {
        Path tempDir = Files.createTempDirectory("temp");
        ZipFile zipFile = new ZipFile(file);
        zipFile.extractAll(tempDir.toAbsolutePath().toString());
        return tempDir.toFile();
    }

    private Collection<File> getFileList(String path) throws URISyntaxException {
        return FileUtils.listFiles(Paths.get(getClass().getResource(path).toURI()).toFile(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }
}
