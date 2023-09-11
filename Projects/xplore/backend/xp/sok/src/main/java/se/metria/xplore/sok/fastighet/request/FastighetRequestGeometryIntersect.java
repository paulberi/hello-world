package se.metria.xplore.sok.fastighet.request;

public class FastighetRequestGeometryIntersect extends FastighetRequestGeometry {

    public FastighetRequestGeometryIntersect(String query, String[] kommun, String typeName, int maxFeatures, int startIndex) {
        super(query, kommun, typeName, maxFeatures, startIndex);
    }

    public String getCqlFilter() {
        String cql_filter = String.format("INTERSECTS(geom, %s)", query);

        if (hasKommun()) {
            cql_filter += String.format(" AND kommunnamn IN (%s)", getKommunCqlFormatted());
        }

        return cql_filter;
    }
}
