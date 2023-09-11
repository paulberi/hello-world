package se.metria.xplore.sok.fastighet.request;

import org.locationtech.jts.io.ParseException;
import se.metria.xplore.sok.fastighet.model.GeometryOperation;
import se.metria.xplore.sok.fastighet.model.GeometryRequest;
import se.metria.xplore.sok.util.FeatureUtil;
import se.metria.xplore.sok.util.SokUtil;

public class FastighetRequestFactory {
    public static FastighetRequest getRequest(String query, String[] kommun, String typeNames) {
        if (SokUtil.isUUID(query)) {
            return new FastighetRequestUUID(query, kommun, typeNames);
        } else if (SokUtil.isUUIDArray(query)) {
            return new FastighetRequestUUIDArray(query, kommun, typeNames);
        } else if (SokUtil.isExternid(query)) {
            return new FastighetRequestExternid(query, kommun, typeNames);
        } else {
            return new FastighetRequestText(query, kommun, typeNames);
        }
    }

    public static FastighetRequest getGeometryRequest(GeometryRequest request, String[] kommun, String typeNames, int maxFeatures, int startIndex) throws ParseException {
        if (SokUtil.isWKT(request.wkt)) {
            String wkt = request.wkt;
            if (request.bufferDistance != null) {
                wkt = FeatureUtil.bufferWkt(wkt, SokUtil.clampDouble(request.bufferDistance, 0.0, 100.0));
            }
            if (request.operation == GeometryOperation.INTERSECTS) {
                return new FastighetRequestGeometryIntersect(wkt, kommun, typeNames, maxFeatures, startIndex);
            } else if (request.operation == GeometryOperation.WITHIN) {
                return new FastighetRequestGeometryWithin(wkt, kommun, typeNames, maxFeatures, startIndex);
            } else {
                throw new IllegalArgumentException("Invalid operation");
            }
        } else {
            throw new IllegalArgumentException("Invalid WKT");
        }
    }
}
