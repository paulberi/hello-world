package se.metria.xplore.sok.fastighet.request;

public class FastighetRequestUUID extends FastighetRequest {
    private final String QUERY_TEMPLATE;

    public FastighetRequestUUID(String query, String[] kommun, String typeNames) {
        super(query, kommun);
        QUERY_TEMPLATE = String.format("service=WFS&version=2.0.0&srsName=EPSG:3006&request=GetFeature&typeNames=%s&cql_filter={cql_filter}&outputFormat=application/json&count=1000&sortBy=omrnr", typeNames);
    }

    public String getWfsQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public String getCqlFilter() {
        String cql_filter = String.format("objekt_id='%s'", query);

        if (hasKommun()) {
            cql_filter += String.format(" AND kommunnamn IN (%s)", getKommunCqlFormatted());
        }

        return cql_filter;
    }
}
