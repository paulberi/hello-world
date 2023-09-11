package se.metria.matdatabas.common;

import org.apache.commons.imaging.ImageReadException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ImageMetadataReaderTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void constructor_ShouldCreateWithInputStream() throws IOException, ImageReadException {
        var file = new File("src/test/resources/images/thumbnail.jpg");
        var input = new FileInputStream(file);
        var reader = new ImageMetadataReader(input);
        assertNotNull(reader);
    }

    @Test
    void getWidth_ShouldReturnWidthInPixels() throws IOException, ImageReadException {
        var reader = new ImageMetadataReader("src/test/resources/images/thumbnail.jpg");
        assertEquals(490, reader.getImageWidth());
    }

    @Test
    void getHeight_ShouldReturnHeightInPixels() throws IOException, ImageReadException {
        var reader = new ImageMetadataReader("src/test/resources/images/thumbnail.jpg");
        assertEquals(490, reader.getImageHeight());
    }

    @Test
    void getOrientation_ShouldReturnZeroIfExifOrientationIsNotPresent() throws IOException, ImageReadException {
        var reader = new ImageMetadataReader("src/test/resources/images/thumbnail.jpg");
        assertEquals(0, reader.getOrientation());
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/images/orientation-landscape-0.jpg, 0",
            "src/test/resources/images/orientation-landscape-1.jpg, 1",
            "src/test/resources/images/orientation-landscape-2.jpg, 2",
            "src/test/resources/images/orientation-landscape-3.jpg, 3",
            "src/test/resources/images/orientation-landscape-4.jpg, 4",
            "src/test/resources/images/orientation-landscape-5.jpg, 5",
            "src/test/resources/images/orientation-landscape-6.jpg, 6",
            "src/test/resources/images/orientation-landscape-7.jpg, 7",
            "src/test/resources/images/orientation-landscape-8.jpg, 8",
            "src/test/resources/images/orientation-portrait-0.jpg, 0",
            "src/test/resources/images/orientation-portrait-1.jpg, 1",
            "src/test/resources/images/orientation-portrait-2.jpg, 2",
            "src/test/resources/images/orientation-portrait-3.jpg, 3",
            "src/test/resources/images/orientation-portrait-4.jpg, 4",
            "src/test/resources/images/orientation-portrait-5.jpg, 5",
            "src/test/resources/images/orientation-portrait-6.jpg, 6",
            "src/test/resources/images/orientation-portrait-7.jpg, 7",
            "src/test/resources/images/orientation-portrait-8.jpg, 8"})
    void getOrientation_ShouldReturnValidExifOrientation(String imagePath, Integer expected) throws IOException, ImageReadException {
        var reader = new ImageMetadataReader(imagePath);
        assertEquals(expected, reader.getOrientation());
    }
}