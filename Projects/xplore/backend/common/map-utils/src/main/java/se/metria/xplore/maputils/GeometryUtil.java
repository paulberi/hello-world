package se.metria.xplore.maputils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class GeometryUtil {

    public static Geometry getPointGeometry(int x, int y) {
        GeometryFactory gm = getGeometryFactory();

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("Point");
        typeBuilder.add("geom", Point.class);

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
        builder.add(gm.createPoint(new Coordinate(x, y)));

        return (Point)builder.buildFeature("pointFeature").getDefaultGeometry();
    }

    private static GeometryFactory getGeometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 3006);
    }

    /**
     * Hämta SimpleFeatures från GeoJson.
     */
    public static Set<SimpleFeature> getFeaturesFromGeoJSON(String geoJson) throws IOException {
        Set<SimpleFeature> features = new HashSet<>();
        FeatureJSON featureJSON = new FeatureJSON();

        try (FeatureIterator<SimpleFeature> featureIterator = featureJSON.streamFeatureCollection(new StringReader(geoJson))) {
            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();
                features.add(feature);
            }
            return features;
        }
    }

    /**
     * Hämta Geometry från SimpleFeature.
     */
    public static Geometry getFeatureGeometry(SimpleFeature feature) {
        GeometryAttribute geometryAttribute = feature.getDefaultGeometryProperty();
        final int SRID = 3006;

        // Kontrollera om CRS är SWEREF-99 TM. Om det inte finns någon info förutsetts SWEREF-99.
        // Framöver bör vi bygga in stöd för transformera automatiskt och tillåta alla koordinatsystem.
        if (geometryAttribute.getDescriptor() != null && geometryAttribute.getDescriptor().getCoordinateReferenceSystem() != null) {
            CoordinateReferenceSystem crs = geometryAttribute.getDescriptor().getCoordinateReferenceSystem();
            if (!isSweref99TM(crs)) {
                throw new IllegalArgumentException("Felaktigt koordinatsystem, förutsätter SWEREF-99 TM");
            }
        }
        // Vi behöver sätta SRID till 3006 manuellt för den gör inte det av någon anledning
        Object aGeometryObj = geometryAttribute.getValue();
        if (aGeometryObj instanceof Geometry) {
            Geometry aGeometry = (Geometry) aGeometryObj;
            aGeometry.setSRID(SRID);
            removeZMDimension(aGeometry);

        }
        return (Geometry)aGeometryObj;
    }

    /**
     * Ta bort 3:e och 4:e dimensionen från en Geometry.
     */
    private static void removeZMDimension(Geometry theGeometry) {
        for (Coordinate aCoord : theGeometry.getCoordinates()) {
            if(!Double.isNaN(aCoord.getZ())) {
                aCoord.setZ(Double.NaN);
            }
            if(!Double.isNaN(aCoord.getM())) {
                aCoord.setM(Double.NaN);
            }
        }
        theGeometry.geometryChanged();
    }

    /**
     * Kontrollera om koordinatsystemet är SWEREF-99 TM.
     */
    public static boolean isSweref99TM(CoordinateReferenceSystem crs) {
        Pattern aPattern = Pattern.compile(".*SWEREF.*99.*TM.*", Pattern.CASE_INSENSITIVE);
        return aPattern.matcher(crs.getName().toString()).matches();
    }

    /**
     * Hämta attribut med ett visst namn och en viss typ från en SimpleFeature.
     */
    public static <T> T extractAttribute(SimpleFeature feature, String attributeName, Class<T> type, T defaultValue) {
        var attr = feature.getAttribute(attributeName);

        if (attr == null) {
            return defaultValue;
        }

        if (!type.isInstance(attr)) {
            String err = "Feature-attribut '" + attributeName + "' har fel typ. " +
                    "Förväntad: '" + type.getName() +"', faktisk: '" + attr.getClass().getName() + "'";
            throw new IllegalArgumentException(err);
        }

        //noinspection unchecked
        return (T)attr;
    }

    public static <T> T extractAttribute(SimpleFeature feature, String attributeName, Class<T> type) {
        return extractAttribute(feature, attributeName, type, null);
    }

    /**
     * Omvandla en Geometry till GeoJson.
     */
    public static String toGeoJSON(Geometry g) {
        if (g == null) return "";
        return (new GeoJsonWriter().write(g));
    }

    /**
     * Hämta ut properties som Json från en SimpleFeature.
     */
    public static JsonNode getPropertiesAsJson(SimpleFeature feature) {
        var objectNode = JsonNodeFactory.instance.objectNode();
        var propertyCollection = feature.getProperties();

        for(var p : propertyCollection) {
            var name = p.getName().toString();
            var value = p.getValue();

            if(name == null || name.length() == 0) {
                continue;
            }

            if(value instanceof Long) {
                objectNode.put(name, ((Long)value));
            }
            else if(value instanceof Double) {
                objectNode.put(name, ((Double)value));
            }
            else if(value instanceof Boolean) {
                objectNode.put(name, ((Boolean)value));
            }
            else {
                if(value != null) {
                    objectNode.put(name, value.toString());
                }
                else {
                    objectNode.putNull(name);
                }
            }
        }

        return objectNode;
    }

    /**
     * Beräkna skalratio för utsträckning i karta.
     */
    public static double calculateScaleRatio(Envelope envelope, int imageWidth, int imageHeight, double dpi) throws IOException {
        double scaleRatio;
        CoordinateReferenceSystem sourceCRS;
        try {
            sourceCRS = CRS.decode("EPSG:3006");
        } catch (FactoryException e) {
            throw new IOException("Kan inte dekoda CRS för att beräkna skalratio");
        }
        var env = new ReferencedEnvelope(envelope, sourceCRS);

        final double diagonalGroundDistance = Math.sqrt(env.getWidth() * env.getWidth() + env.getHeight() * env.getHeight());
        final double diagonalPixelDistanceInPixels = Math.sqrt(imageWidth * imageWidth + imageHeight * imageHeight);
        final double diagonalPixelDistanceInMeters = diagonalPixelDistanceInPixels / dpi * 2.54 / 100;
        scaleRatio = diagonalGroundDistance / diagonalPixelDistanceInMeters;

        return scaleRatio;
    }

    /**
     * Ta reda på närmaste zoom-nivå ovanför en specifik skala.
     */
    public static double findNearestZoomLevel(double scale, List<Integer> zoomLevels) {
        var roundedScale = Long.valueOf(Math.round(scale)).intValue();

        int min = Integer.MAX_VALUE;
        int nearest = roundedScale;

        for (int zoomLevel : zoomLevels) {
            final int diff = Math.abs(zoomLevel - roundedScale);

            if (zoomLevel > roundedScale && diff < min) {
                min = diff;
                nearest = zoomLevel;
            }
        }

        return nearest;
    }


    /**
     * Utöka extent utifrån en given ratio.
     * Om bildförhållandet på extentet inte överrensstämmer med bildförhållandet på kartbilden
     * man hämtar så blir kartbilden skev. Beroende på kartbildens storlek så förlänger vi antingen x- eller y-axeln för att få
     * bildförhållandet rätt.
     */
    public static Envelope extendExtentByAspectRatio(Envelope extent, Double aspectRatio) throws IOException {
        var minX = extent.getMinX();
        var minY = extent.getMinY();
        var maxX = extent.getMaxX();
        var maxY = extent.getMaxY();

        /*
         * Ta fallet där vi behöver sträcka på värdena på x-axeln. Om x_m är mittpunkten mellan x_min och x_max så vill
         * vi lösa nedanstående ekvation (1) för att få värdet på d:
         *
         * (x_max - x_min) / (y_max - y_min) = aspectRatio
         *
         * x_max = x_mid + d
         * x_min = x_mid - d
         *
         * x_max - x_min = x_mid + d - (x_mid - d) = 2d
         *
         * (1) 2d / (y_max - y_min) = aspectRatio
         *
         * På liknande sätt så kan vi lösa för fallet där y-axeln behöver tweakas
         * */
        var extentAspectRatio = (maxX - minX) / (maxY - minY);
        if (extentAspectRatio < aspectRatio) {
            var midpoint = (maxX + minX) / 2.;
            var d = aspectRatio / 2. * (maxY - minY);
            return new ReferencedEnvelope(midpoint - d, midpoint + d, minY, maxY, getSweref99CRS());
        }
        else {
            var midpoint = (maxY + minY) / 2.;
            var d = (maxX - minX) / (2. * aspectRatio);
            return new ReferencedEnvelope(minX, maxX, midpoint - d, midpoint + d, getSweref99CRS());
        }
    }

    /**
     * Utöka extent utifrån en given skala.
     */
    public static Envelope extendExtentByScale(Envelope extent, double scale, int imageWidth, int imageHeight, double dpi) throws IOException {

        double imageWidthInInch = imageWidth / dpi;
        double imageHeightInInch = imageHeight / dpi;
        double imageWidthInMeter = imageWidthInInch * 0.0254;
        double imageHeightInMeter = imageHeightInInch * 0.0254;

        // Beräkna halva kartans bredd och höjd med specifik skala
        double dX = (imageWidthInMeter * scale) / 2;
        double dY = (imageHeightInMeter * scale) / 2;

        // Expandera envelope med ny storlek
        var center = extent.centre();
        double minX = center.getX() - dX;
        double maxX = center.getX() + dX;
        double minY = center.getY() - dY;
        double maxY = center.getY() + dY;

        return new ReferencedEnvelope(minX, maxX, minY, maxY, getSweref99CRS());
    }

    /**
     * Utöka extent åt alla riktningar med procent av befintlig bredd och höjd.
     */
    public static Envelope extendExtentByPercentage(Envelope extent, double top, double right, double bottom, double left) throws IOException {
        var width = extent.getWidth();
        var height = extent.getHeight();

        var deltaLeft =  width * (left / 100 + 1) - width;
        var deltaRight = width * (right / 100 + 1) - width;
        var deltaBottom = height * (bottom / 100 + 1) - height;
        var deltaTop = height * (top / 100 + 1) - height;

        var envelope = new ReferencedEnvelope(
                extent.getMinX() - deltaLeft, extent.getMaxX() + deltaRight,
                extent.getMinY() - deltaBottom, extent.getMaxY() + deltaTop, getSweref99CRS());

        return envelope;
    }

    /**
     * Hämta CRS för SWEREF99-TM.
     */
    public static CoordinateReferenceSystem getSweref99CRS() throws IOException {
        CoordinateReferenceSystem crs;
        try {
            crs = CRS.decode("EPSG:3006");
        } catch (FactoryException e) {
            throw new IOException("Kan inte dekoda CRS.");
        }
        return crs;
    }

    public static Geometry createPoint(Double x, Double y) {
        return new GeometryFactory(new PrecisionModel(), 3006).createPoint(new Coordinate(x, y));
    }

    public static Integer getAttributeInt(SimpleFeature feature, String attribute) {
        return extractAttribute(feature, attribute, Long.class, -1L).intValue();
    }

    public static String getAttributeString(SimpleFeature feature, String attribute) {
        return extractAttribute(feature, attribute, String.class, "okänd");
    }
}
