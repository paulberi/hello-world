package se.metria.skogsmaskindata.utils;

import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;

public class FeatureUtils {
	private static GeometryFactory geometryFactory = getGeometryFactory();

	private static GeometryFactory getGeometryFactory() {
		return new GeometryFactory(new PrecisionModel(), 3006);
	}
	public static String getString(SimpleFeature feature, String attr) {
		return (String) feature.getAttribute(attr);
	}

	public static Double getDouble(SimpleFeature feature, String attr) {
		return (Double) feature.getAttribute(attr);
	}

	public static MultiPoint getMultiPoint(SimpleFeature feature) {
		Object geom = feature.getDefaultGeometry();
		if (geom instanceof Point) {
			return geometryFactory.createMultiPoint(new Point[]{(Point) geom});
		} else if (geom instanceof MultiPoint) {
			return (MultiPoint) geom;
		} else {
			throw new IllegalArgumentException("SimpleFeature is not of Point or MultiPoint type");
		}
	}

	public static MultiPolygon getMultiPolygon(SimpleFeature feature) {
		Object geom = feature.getDefaultGeometry();
		if (geom instanceof Polygon) {
			return geometryFactory.createMultiPolygon(new Polygon[]{(Polygon) geom});
		} else if (geom instanceof MultiPolygon) {
			return (MultiPolygon) geom;
		} else {
			throw new IllegalArgumentException("SimpleFeature is not of Polygon or MultiPolygon type");
		}
	}

	public static MultiLineString getMultiLineString(SimpleFeature feature) {
		Object geom = feature.getDefaultGeometry();
		if (geom instanceof LineString) {
			return geometryFactory.createMultiLineString(new LineString[]{(LineString) geom});
		} else if (geom instanceof MultiLineString) {
			return (MultiLineString) geom;
		} else {
			throw new IllegalArgumentException("SimpleFeature is not of LineString or MultiLineString type");
		}
	}
}
