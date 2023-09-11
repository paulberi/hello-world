package se.metria.xplore.sok.fastighet.request;

import se.metria.xplore.maputils.CqlEscape;

public class FastighetRequestExternid extends FastighetRequest{
    private final String QUERY_TEMPLATE;

    public FastighetRequestExternid(String query, String[] kommun, String typeNames) {
        super(query, kommun);
        QUERY_TEMPLATE = String.format("service=WFS&version=2.0.0&srsName=EPSG:3006&request=GetFeature&typeNames=%s&cql_filter={cql_filter}&outputFormat=application/json&count=1000&sortBy=omrnr", typeNames);
    }

    public String getWfsQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public String getCqlFilter() {
        String externid = query.substring("externid:".length());

        String cql_filter = String.format("externid='%s'", CqlEscape.cqlEscape(externid));

        if (hasKommun()) {
            cql_filter += String.format(" AND kommunnamn IN (%s)", getKommunCqlFormatted());
        }

        return cql_filter;
    }
}
