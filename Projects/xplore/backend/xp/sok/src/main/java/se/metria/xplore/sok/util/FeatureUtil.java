package se.metria.xplore.sok.util;

import org.apache.commons.text.WordUtils;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.opengis.feature.simple.SimpleFeature;

import java.util.List;

public final class FeatureUtil {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static String getAttribute(SimpleFeature feature, String name, boolean titleCase) {
        Object attribute = feature.getAttribute(name);
        if (attribute != null) {
            String value = attribute.toString();
            return titleCase ? WordUtils.capitalizeFully(value, ' ', '-') : value;
        }
        return null;
    }

    public static Coordinate getCoordinate(SimpleFeature feature, String name, boolean swapCoordinate) {
        Object attribute = feature.getAttribute(name);
        if (attribute instanceof MultiPoint) {
            Coordinate coordinate = ((MultiPoint) attribute).getCoordinate();
            if (swapCoordinate) {
                swapCoordinate(coordinate);
            }

            return coordinate;
        }

        return null;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static void swapCoordinate(Coordinate coordinate) {
        double x = coordinate.x;
        coordinate.x = coordinate.y;
        coordinate.y = x;
    }

    public static Point createPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    public static String bufferWkt(String wkt, double distance) throws ParseException {
        if (distance == 0) return wkt;

        WKTReader reader = new WKTReader();
        WKTWriter writer = new WKTWriter();

        Geometry geom = reader.read(wkt);
        geom = bufferGeometry(geom, distance);

        return writer.write(geom);
    }

    public static Geometry bufferGeometry(Geometry geom, double distance) {
        if (distance == 0) return geom;

        BufferParameters bufferParameters = new BufferParameters(3, BufferParameters.CAP_ROUND, BufferParameters.JOIN_ROUND, 3);
        return BufferOp.bufferOp(geom, distance, bufferParameters);
    }

    public static GeometryCollection createGeometryCollection(List<Geometry> geometries) {
        return new GeometryCollection(geometries.toArray(new Geometry[0]), geometryFactory);
    }
}
