package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.locationtech.jts.geom.Envelope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;
import se.metria.markkoll.repository.fastighet.FastighetOmradeRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.service.map.ImageService;
import se.metria.markkoll.service.map.LayerService;
import se.metria.markkoll.service.map.MapService;
import se.metria.markkoll.service.map.ScalebarService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.service.map.MapService.*;
import static se.metria.xplore.maputils.GeometryUtil.*;

/**
 * Använd följande funktion för att spara ned resultatet i en ny bildfil om t.ex. stylingen ändras på kartorna.
 * Glöm inte att kopiera in filen till rätt ställe.
 * FileUtil.save(map, "namn_på_bild.png");
 */

@DisplayName("Givet MapService")
@MarkkollServiceTest
public class MapServiceTest {
    MapService mapService;

    VaghallareService mockVaghallareService;
    LayerService mockLayerService;
    ScalebarService mockScalebarService;
    AvtalsinstallningarRepository mockAvtalsinstallningarRepository;
    FastighetsintrangRepository mockFastighetsintrangRepository;
    FastighetRepository mockFastighetRepository;
    FastighetOmradeRepository mockFastighetOmradeRepository;
    OmradesintrangRepository mockOmradesintrangRepository;
    ImageService mockImageService;

    @BeforeEach
    void beforeEach() throws Exception {
        mockVaghallareService = mock(VaghallareService.class);
        mockLayerService = mock(LayerService.class);
        mockScalebarService = mock(ScalebarService.class);
        mockFastighetsintrangRepository = mock(FastighetsintrangRepository.class);
        mockFastighetRepository = mock(FastighetRepository.class);
        mockFastighetOmradeRepository = mock(FastighetOmradeRepository.class);
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);
        mockImageService = mock(ImageService.class);
        mockAvtalsinstallningarRepository = mock(AvtalsinstallningarRepository.class);

        when(mockLayerService.getLayerBackgroundSkarp(any(), any(), any()))
                .thenReturn(loadImage("bakgrund_skarp.jpeg"));
        when(mockLayerService.getLayerIntrangProjektOversikt(any(), any(), any(), any(), eq(true)))
                .thenReturn(loadImage("intrang_projekt_projektomrade.png"));
        when(mockLayerService.getLayerIntrangProjektOversikt(any(), any(), any(), any(), eq(false)))
                .thenReturn(loadImage("intrang_projekt.png"));
        when(mockLayerService.getLegendProjektOversikt(any(), any(), any(), any(), anyBoolean()))
                .thenReturn(loadImage("legend_intrang_projekt.png"));
        when(mockScalebarService.getImage(any(), anyDouble(), anyDouble()))
                .thenReturn(loadImage("scalebar.png"));
        when(mockImageService.textImage(eq(LEGAL_INFO_LEFT), anyInt(), anyInt(), anyInt(), any()))
                .thenReturn(loadImage("legal-info-left.png"));
        mapService = new MapService(mockVaghallareService, mockLayerService, mockScalebarService, mockImageService,
            mockAvtalsinstallningarRepository, mockFastighetsintrangRepository, mockFastighetRepository, mockFastighetOmradeRepository,
            mockOmradesintrangRepository);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void så_ska_det_gå_att_hämta_projektkarta_med_och_utan_projektområde(Boolean showProjektomrade) throws Exception {
        // Given
        var width = IMAGE_WIDTH - 2 * BORDER_SIZE;
        var height = IMAGE_HEIGHT - 2 * BORDER_SIZE;
        var legendIconWidth = getHighRes(8);
        var legendIconHeight = getHighRes(8);
        var dpi = HighResolutionDpi;
        var projektId = mock(UUID.class);
        var envelope = new Envelope();
        envelope.expandBy(2000);
        var squareExtent = extendExtentByAspectRatio(envelope, Double.valueOf(width) / Double.valueOf(height));
        var scaleRatio = calculateScaleRatio(squareExtent, width, height, HighResolutionDpi);
        var roundedScaleRatio = findNearestZoomLevel(scaleRatio, ZOOM_LEVELS);
        var expandedExtent = extendExtentByScale(squareExtent, roundedScaleRatio, width, height, HighResolutionDpi);
        var scalebarWidth = 140d;

        when(mockFastighetsintrangRepository.getEnvelopeByProjektId(any())).thenReturn(envelope);

        // When
        var map = mapService.getProjektkarta(projektId, showProjektomrade);

        // Then
        if (showProjektomrade) {
            // FileUtil.save(map, "projektkarta_projektomrade.png");
            assertEquals(getImageResource("projektkarta_projektomrade.png"), map);

        } else {
            // FileUtil.save(map, "projektkarta.png");
            assertEquals(getImageResource("projektkarta.png"), map);
        }

        verify(mockLayerService).getLayerBackgroundSkarp(eq(width), eq(height), eq(expandedExtent));
        verify(mockLayerService).getLayerIntrangProjektOversikt(eq(projektId), eq(width), eq(height), eq(expandedExtent), eq(showProjektomrade));
        verify(mockLayerService).getLegendProjektOversikt(eq(projektId), eq(legendIconWidth), eq(legendIconHeight), eq(dpi), eq(showProjektomrade));
        verify(mockScalebarService).getImage(eq(roundedScaleRatio), eq(scalebarWidth), eq(dpi.doubleValue()));
    }

    private BufferedImage loadImage(String fileName) throws IOException  {
        var stream = new ClassPathResource("map/" + fileName).getInputStream();

        return ImageIO.read(stream);
    }

    private ByteArrayResource getImageResource(String fileName) throws IOException {
        return new ByteArrayResource(new ClassPathResource("map/" + fileName).getInputStream().readAllBytes());
    }
}
