package se.metria.xplore.maputils;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.metria.xplore.maputils.GeometryUtil.getSweref99CRS;

@DisplayName("Givet GeometryUtil")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GeometryUtilTest {

    List<Integer> zoomLevels;

    @BeforeEach
    void beforeEach() {
        zoomLevels = Arrays.asList(2000, 4000, 8000);
    }

    @Test
    void så_ska_det_gå_att_hämta_närmsta_zoom_nivå() {
        // Given
        double inputScale = 5500.2312;
        double expected = 8000d;

        // When
        var actual = GeometryUtil.findNearestZoomLevel(inputScale, zoomLevels);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void så_ska_skala_kunna_vara_högre_än_högsta_zoom_nivå() {
        // Given
        double inputScale = 9200d;
        double expected = 9200d;

        // When
        var actual = GeometryUtil.findNearestZoomLevel(inputScale, zoomLevels);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void så_ska_det_gå_att_utöka_extent_procentuellt() throws IOException {
        // Given
        ReferencedEnvelope env = new ReferencedEnvelope(1000, 2000, 1000, 2000, getSweref99CRS()); // 1000 x 1000 m
        var topPercentage = 20d;
        var rightPercentage = 10d;
        var bottomPercentage = 50d;
        var leftPercentage = 5d;

        // When
        var actual = GeometryUtil.extendExtentByPercentage(env, topPercentage, rightPercentage, bottomPercentage, leftPercentage);

        // Then
        assertEquals(2200, actual.getMaxY());
        assertEquals(2100, actual.getMaxX());
        assertEquals(500, actual.getMinY());
        assertEquals(950, actual.getMinX());
        assertEquals(1150, actual.getWidth());
        assertEquals(1700, actual.getHeight());
    }
}
