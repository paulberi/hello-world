package se.metria.markkoll.service.map;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;
import se.metria.markkoll.repository.fastighet.FastighetOmradeRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.service.VaghallareService;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static se.metria.xplore.maputils.GeometryUtil.*;

/**
 * Service för att rendera bilder för olika typer av kartor i Markkoll.
 * Fem nivåer av kartor:
 *   1. Intrångskarta. Detaljkarta med intrånget i centrum.
 *   2. Intrångsöversikt. Visar var på fastigheten intrånget finns.
 *   3. Fastighetsöversikt. Visar var fastigheten finns i världen.
 *   4. Projektkarta. Visar projektets utsnitt.
 *   5. Projektöversikt. Visar var projektet finns i världen.
 */

@Service
@RequiredArgsConstructor
public class MapService {
    @NonNull
    private final VaghallareService vaghallareService;

    @NonNull
    private final LayerService layerService;

    @NonNull
    private final ScalebarService scalebarService;

    @NonNull
    private final ImageService imageService;

    @NonNull
    private final AvtalsinstallningarRepository avtalsinstallningarRepository;

    @NonNull
    private final FastighetsintrangRepository fastighetsintrangRepository;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @NonNull
    private final FastighetOmradeRepository fastighetOmradeRepository;

    @NonNull
    private final OmradesintrangRepository omradesintrangRepository;

    // Dubbel så hög upplösning som normalt för skarpa PDF-kartor
    public static final Integer HighResolutionDpi = 180;
    // Hämta storlek på bild utifrån vald DPI
    public static final int getHighRes(int originalSize) {
        return Long.valueOf(Math.round(originalSize * (HighResolutionDpi.doubleValue() / 90))).intValue();
    }
    // Bildstorlekar i pixlar, beräkna utifrån vald DPI med ex. https://pixelcalculator.com
    public static final int IMAGE_WIDTH = 1276; // 18cm
    public static final int IMAGE_HEIGHT = 1701; // 24cm
    public static final int OVERVIEW_SIZE = 354; // 5cm
    public static final int BORDER_SIZE = 4;

    // Lista med zoom-nivåer för jämn och fin skala, från Open Street Map, https://wiki.openstreetmap.org/wiki/Zoom_levels
    public static final List<Integer> ZOOM_LEVELS = Arrays.asList(
            500, 1000, 2000, 4000, 8000, 15000, 35000, 70000,
            150000, 250000, 500000, 1000000, 2000000
    );
    public static final String LEGAL_INFO_LEFT = "© Copyright Lantmäteriet";
    public static final String LEGAL_INFO_RIGHT = "Gränserna i kartan stämmer inte alltid överens med verkligheten och är därför inte juridiskt gällande";

    @PostConstruct
    public void init() {
        ImageIO.setUseCache(false);
    }

    /**
     * 1. Intrångskarta. Detaljkarta med intrånget i centrum.
     */
    public BufferedImage
    getIntrangskarta(UUID projektId, UUID fastighetId, Long omradeId, String styles, String cqlFilter) throws IOException {
        var width = IMAGE_WIDTH - 2 * BORDER_SIZE;
        var height = IMAGE_HEIGHT - 2 * BORDER_SIZE;

        // Hämta utbredning för områdesintrång
        var omradesIntrangEnvelope = omradesintrangRepository.getEnvelopeBuffered(fastighetId, projektId, omradeId);
        // Öka på procentuellt uppe och nere för att få plats med infällda kartor och legend
        var adjustedSize = extendExtentByPercentage(omradesIntrangEnvelope, 22, 0, 10, 0);
        // Korrigera utifrån bildens ratio
        var adjustedAspectRatio = extendExtentByAspectRatio(adjustedSize, Double.valueOf(width) / Double.valueOf(height));
        // Ta redo på skala och avrunda utifrån närmaste övre zoom-nivå
        var scaleRatio = calculateScaleRatio(adjustedAspectRatio, width, height, HighResolutionDpi);
        var roundedScaleRatio = findNearestZoomLevel(scaleRatio, ZOOM_LEVELS);
        var extendedExtent = extendExtentByScale(adjustedAspectRatio, roundedScaleRatio, width, height, HighResolutionDpi);

        // Lägg på en liten buffert kring fastighetsområde för att ta med angränsande fastigheter från Metria Maps
        var fastighetOmradeExtent = fastighetOmradeRepository.getExtentBuffered(fastighetId, omradeId, 10);

        // Hämta lager och rendera karta
        var background = layerService.getLayerBackground(width, height, extendedExtent);
        var intrangProjekt = layerService.getLayerIntrangProjekt(projektId, width, height, extendedExtent, styles, cqlFilter);
        var fastighetsytor = layerService.getLayerFastighetsytorMetriaMaps(width, height, extendedExtent, fastighetId, fastighetOmradeExtent);
        var fastighetYta = layerService.getLayerFastighetYta(projektId, fastighetId, width, height, extendedExtent);
        var legend = createBox(layerService.getLegendProjekt(projektId, getHighRes(8), getHighRes(8), HighResolutionDpi, width, height, extendedExtent, scaleRatio, styles, cqlFilter), 10);
        var scalebar = createBox(scalebarService.getImage(roundedScaleRatio, 140, HighResolutionDpi),  10);
        var compass = compassImage();
        var legalInfoLeft = imageService.textImage(LEGAL_INFO_LEFT, 140, 15, 12, Color.DARK_GRAY);
        var legalInfoRight = imageService.textImage(LEGAL_INFO_RIGHT, 545, 15, 12, Color.DARK_GRAY);
        var canvas = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        var graphics = canvas.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT));
        graphics.drawImage(background, BORDER_SIZE, BORDER_SIZE, null);
        graphics.drawImage(intrangProjekt, BORDER_SIZE, BORDER_SIZE, null);
        graphics.drawImage(fastighetsytor, BORDER_SIZE, BORDER_SIZE, null);
        graphics.drawImage(fastighetYta, BORDER_SIZE, BORDER_SIZE, null);
        graphics.drawImage(legend, getHighRes(15),
                IMAGE_HEIGHT - legend.getHeight() - getHighRes(15), null);
        graphics.drawImage(scalebar,
                IMAGE_WIDTH - scalebar.getWidth() - getHighRes(15),
                IMAGE_HEIGHT - scalebar.getHeight() - getHighRes(15), null);
        graphics.drawImage(compass, IMAGE_WIDTH - compass.getWidth() - getHighRes(15), getHighRes(15), null);
        graphics.drawImage(legalInfoLeft, getHighRes(15),IMAGE_HEIGHT - legalInfoLeft.getHeight() - getHighRes(5), null);
        graphics.drawImage(legalInfoRight, IMAGE_WIDTH - legalInfoRight.getWidth() - getHighRes(15),IMAGE_HEIGHT - legalInfoRight.getHeight() - getHighRes(5), null);
        graphics.dispose();

        return canvas;
    }

    /**
     * 2. Intrångsöversikt. Översiktskarta med en ruta som visar var på fastigheten intrånget finns.
     */
    public BufferedImage
    getIntrangsoversikt(UUID projektId, UUID fastighetId, Long omradeId) throws IOException {
        var width = OVERVIEW_SIZE - 2 * BORDER_SIZE;
        var height = OVERVIEW_SIZE - 2 * BORDER_SIZE;

        // Hämta utbredning för områdesintrång
        var extent = omradesintrangRepository.getExtentBuffered(fastighetId, projektId, omradeId).get();

        // Zooma ut för att få en översikt över intrång
        var env = extent.getEnvelopeInternal();
        var center = extent.getCentroid();
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setCentre(new Coordinate(center.getX(), center.getY()));
        Integer extentFactor  = 5;
        Integer extraExtent = 0;
        // Justera upp storlek om områdesintrånget är för litet
        if (env.getWidth() < 100 || env.getHeight() < 100) {
           extraExtent = 300;
        }
        gsf.setWidth(env.getWidth() * extentFactor + extraExtent);
        gsf.setHeight(env.getHeight() * extentFactor + extraExtent);
        var extendedExtent = gsf.createRectangle().getEnvelopeInternal();
        var squareExtent = extendExtentByAspectRatio(extendedExtent,  Double.valueOf(width) / Double.valueOf(height));

        // Hämta lager och rendera karta
        var background = layerService.getLayerBackground(width, height, squareExtent);
        var intrangExtent = layerService.getLayerIntrangExtent(projektId, fastighetId, omradeId,
            width, height, squareExtent);
        var canvas = new BufferedImage(OVERVIEW_SIZE, OVERVIEW_SIZE, BufferedImage.TYPE_INT_ARGB);
        var graphics = canvas.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle(0, 0, OVERVIEW_SIZE, OVERVIEW_SIZE));
        graphics.drawImage(background, BORDER_SIZE, BORDER_SIZE, null);
        graphics.drawImage(intrangExtent, BORDER_SIZE, BORDER_SIZE, null);
        graphics.dispose();

        return canvas;
    }

    /**
     * 3. Fastighetsöversikt. Visar en nål var fastigheten finns i världen.
     */
    public BufferedImage
    getFastighetsoversikt(UUID projektId, UUID fastighetId) throws IOException {
        var width = OVERVIEW_SIZE - 2 * BORDER_SIZE;
        var height = OVERVIEW_SIZE - 2 * BORDER_SIZE;

        // Hämta ut centrum av fastighetens utbredning och lägg på en buffert på 1.5 mil för att få en översikt
        var envelope = fastighetRepository.getExtent(fastighetId).get().getCentroid().buffer(15000).getEnvelopeInternal();
        var squareEnvelope = extendExtentByAspectRatio(envelope, Double.valueOf(width) / Double.valueOf(height));

        // Hämta lager och rendera karta
        var background = layerService.getLayerBackground(width, height, squareEnvelope);
        var fastighetExtent = layerService.getLayerFastighetExtent(projektId, fastighetId,
                width, height, squareEnvelope);
        var canvas = new BufferedImage(OVERVIEW_SIZE, OVERVIEW_SIZE, BufferedImage.TYPE_INT_ARGB);
        var graphics = canvas.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle(0, 0, OVERVIEW_SIZE, OVERVIEW_SIZE));
        graphics.drawImage(background, BORDER_SIZE, BORDER_SIZE, null);
        graphics.drawImage(fastighetExtent, BORDER_SIZE, BORDER_SIZE, null);
        graphics.dispose();

        return canvas;
    }

    /**
     * 4. Projektkarta. Visar projektets utsnitt.
     * Möjlighet att slå på/av en visuell buffert som visar projektområdet.
     */
    @Cacheable("kartor")
    public ByteArrayResource getProjektkarta(UUID projektId, boolean showProjektomrade) throws IOException {
        var width = IMAGE_WIDTH - 2 * BORDER_SIZE;
        var height = IMAGE_HEIGHT - 2 * BORDER_SIZE;

        // Hämta utbredning för fastighetsintrång och utöka för att zooma ut
        var envelope = fastighetsintrangRepository.getEnvelopeByProjektId(projektId);
        envelope.expandBy(2000);

        // Korrigera utifrån bildens ratio
        var adjustedAspectRatio = extendExtentByAspectRatio(envelope, Double.valueOf(width) / Double.valueOf(height));

        // Ta redo på skala och avrunda utifrån närmaste övre zoom-nivå
        var scaleRatio = calculateScaleRatio(adjustedAspectRatio, width, height, HighResolutionDpi);
        var roundedScaleRatio = findNearestZoomLevel(scaleRatio, ZOOM_LEVELS);
        var extendedExtent = extendExtentByScale(adjustedAspectRatio, roundedScaleRatio, width, height, HighResolutionDpi);

        // Hämta lager och rendera karta
        var canvas = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        var background = layerService.getLayerBackgroundSkarp(width, height, extendedExtent);
        var intrang = layerService.getLayerIntrangProjektOversikt(projektId, width, height, extendedExtent, showProjektomrade);
        var legend = createBox(layerService.getLegendProjektOversikt(projektId, getHighRes(8), getHighRes(8), HighResolutionDpi, showProjektomrade), 10);
        var scalebar = createBox(scalebarService.getImage(roundedScaleRatio, 140, HighResolutionDpi), 10);
        var compass = compassImage();
        var legalInfoLeft = imageService.textImage(LEGAL_INFO_LEFT, 140, 15, 12, Color.DARK_GRAY);
        var graphics = canvas.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT));
        graphics.drawImage(background, BORDER_SIZE, BORDER_SIZE, null);
        graphics.drawImage(intrang, BORDER_SIZE, BORDER_SIZE, null);
        graphics.drawImage(legend, getHighRes(15),IMAGE_HEIGHT - legend.getHeight() - getHighRes(15), null);
        graphics.drawImage(scalebar,IMAGE_WIDTH - scalebar.getWidth() - getHighRes(15),IMAGE_HEIGHT - scalebar.getHeight() - getHighRes(15), null);
        graphics.drawImage(compass, IMAGE_WIDTH - compass.getWidth() - getHighRes(15), getHighRes(15), null);
        graphics.drawImage(legalInfoLeft, getHighRes(15),IMAGE_HEIGHT - legalInfoLeft.getHeight() - getHighRes(5), null);

        var baos = new ByteArrayOutputStream();
        ImageIO.write(canvas, "png", baos);
        graphics.dispose();
        return new ByteArrayResource(baos.toByteArray());
    }

    /**
     * 5. Projektöversikt.
     * Coming to a Markkoll near you.
     */

    /**
     * Avtalskarta per delområde. Innehåller intrångskarta, fastighetsöversikt och intrångsöversikt.
     */
    @Cacheable("kartor")
    public ByteArrayResource
    getAvtalskarta(UUID projektId, UUID fastighetId, Long omradeId) throws IOException {
        var filter = createFilterFromArray("avtalstyp", getAvtalstyper(projektId));

        var intrangskarta = getIntrangskarta(projektId, fastighetId, omradeId, LayerStyle.INTRANG_PROJEKT_AVTALSKARTA.toString(), filter);
        var fastighetsoversikt = getFastighetsoversikt(projektId, fastighetId);
        var intrangsoversikt = getIntrangsoversikt(projektId, fastighetId, omradeId);

        var pageCanvas = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        var pageGraphics = pageCanvas.createGraphics();
        pageGraphics.drawImage(intrangskarta, 0, 0, null);
        pageGraphics.drawImage(fastighetsoversikt, getHighRes(15), getHighRes(15), null);
        pageGraphics.drawImage(intrangsoversikt, fastighetsoversikt.getWidth() + getHighRes(15) - BORDER_SIZE, getHighRes(15), null);

        var baos = new ByteArrayOutputStream();
        ImageIO.write(pageCanvas, "png", baos);

        pageGraphics.dispose();

        return new ByteArrayResource(baos.toByteArray());
    }

    private List<String> getAvtalstyper(UUID projektId) {
        List<AvtalstypDto> avtalsTyper = new ArrayList<>(Arrays.asList(AvtalstypDto.INTRANG, AvtalstypDto.SERVIS));

        avtalsTyper.addAll(vaghallareService.vaghallareAvtalstyperInMap(projektId));

        return avtalsTyper.stream().map(AvtalstypDto::toString).collect(Collectors.toList());
    }

    private String createFilterFromArray(String feature, List<String> types) {
        List<String> quoted = types.stream().map(StringUtils::quote).collect(Collectors.toList());
        var filter = String.format("%s IN(%s)", feature, String.join(",", quoted));
        return filter;
    }

    private BufferedImage compassImage() throws IOException {
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream("images/compass_north_180_dpi.png");
        return ImageIO.read(imageStream);
    }

    private BufferedImage createBox(BufferedImage image, int padding) {
        var width = image.getWidth() + 2 * padding;
        var height = image.getHeight() + 2 * padding;
        var img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var graphics = img.createGraphics();
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
        graphics.setComposite(ac);
        graphics.setColor(Color.WHITE);
        graphics.fill(new Rectangle(0, 0, width, height));
        AlphaComposite ac2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        graphics.setComposite(ac2);
        graphics.drawImage(image, padding, padding, null);
        return img;
    }
}
