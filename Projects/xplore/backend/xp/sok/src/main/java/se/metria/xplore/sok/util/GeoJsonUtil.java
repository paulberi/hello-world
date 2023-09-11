package se.metria.xplore.sok.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import se.metria.xplore.sok.exceptions.GeoJsonMergeException;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.function.Function;

public final class GeoJsonUtil {
    private static final GeoJsonReader geoJsonReader = new GeoJsonReader(new GeometryFactory());
    private static final GeoJsonWriter geoJsonWriter = new GeoJsonWriter();

    private static final String PROPERTIES = "properties";
    private static final String FEATURES = "features";
    private static final String GEOMETRY = "geometry";
    private static final String BBOX = "bbox";
    private static final String TOTAL_FEATURES = "totalFeatures";
    private static final String NUMBER_RETURNED = "numberReturned";

    static {
        geoJsonWriter.setEncodeCRS(false);
    }

    public static String mergeGeometries(String geoJson, String mergePropertyName) throws GeoJsonMergeException {
        try {
            JsonNode json = new ObjectMapper().readTree(geoJson);
            JsonNode featuresNode = json.get(FEATURES);

            if (featuresNode.size() > 1) {
                LinkedHashMap<String, Geometry> mergedGeometries = new LinkedHashMap<>();

                Iterator<JsonNode> iterator = featuresNode.iterator();
                while (iterator.hasNext()) {
                    JsonNode featureNode = iterator.next();
                    JsonNode propertiesNode = featureNode.get(PROPERTIES);

                    Geometry geometry = geoJsonReader.read(featureNode.get(GEOMETRY).toString());
                    String mergePropertyValue = propertiesNode.get(mergePropertyName).asText();

                    if (mergedGeometries.containsKey(mergePropertyValue)) {
                        geometry = mergedGeometries.get(mergePropertyValue).union(geometry);
                        iterator.remove();
                    }

                    mergedGeometries.put(mergePropertyValue, geometry);
                }

                iterator = featuresNode.iterator();
                while (iterator.hasNext()) {
                    JsonNode featureNode = iterator.next();
                    JsonNode propertiesNode = featureNode.get(PROPERTIES);
                    String mergePropertyValue = propertiesNode.get(mergePropertyName).asText();

                    if (mergedGeometries.containsKey(mergePropertyValue)) {
                        Geometry mergedGeometry = mergedGeometries.get(mergePropertyValue);

                        // Merge geometries
                        JsonNode geometryNode = new ObjectMapper().readTree(geoJsonWriter.write(mergedGeometry));
                        ((ObjectNode) featureNode).replace(GEOMETRY, geometryNode);

                        // Expand bounding box
                        Envelope bbox = mergedGeometry.getEnvelopeInternal();
                        ((ObjectNode) propertiesNode).replace(BBOX, new ObjectMapper().readTree(
                                String.format(Locale.ROOT, "[%f, %f, %f, %f]", bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY())));
                    }
                }

                ((ObjectNode) json).put(TOTAL_FEATURES, mergedGeometries.size());
                ((ObjectNode) json).put(NUMBER_RETURNED, mergedGeometries.size());
            }

            return json.toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new GeoJsonMergeException();
        }
    }

    public static String addMappedProperties(String geoJson, String sourcePropertyName, String targetPropertyName, Function<String, String> mapFunction) throws GeoJsonMergeException {
        try {
            JsonNode json = new ObjectMapper().readTree(geoJson);
            JsonNode featuresNode = json.get(FEATURES);

            if (featuresNode.size() > 0) {
                for (JsonNode featureNode : featuresNode) {
                    JsonNode propertiesNode = featureNode.get(PROPERTIES);
                    if (propertiesNode.has(sourcePropertyName)) {
                        JsonNode sourceNode = propertiesNode.get(sourcePropertyName);
                        switch (sourceNode.getNodeType()) {
                            case STRING:
                            case NUMBER: {
                                String sourcePropertyValue = sourceNode.asText();
                                String targetPropertyValue = mapFunction.apply(sourcePropertyValue);
                                ((ObjectNode) propertiesNode).put(targetPropertyName, targetPropertyValue);
                            }
                        }
                    }
                }
            }

            return json.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GeoJsonMergeException();
        }
    }
}
