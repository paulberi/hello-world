package se.metria.xplore.sok.fastighet.request;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FastighetRequestUUIDArray extends FastighetRequest {
    private final String QUERY_TEMPLATE;

    public FastighetRequestUUIDArray(String query, String[] kommun, String typeNames) {
        super(query, kommun);
        QUERY_TEMPLATE = String.format("service=WFS&version=2.0.0&srsName=EPSG:3006&request=GetFeature&typeNames=%s&cql_filter={cql_filter}&outputFormat=application/json&count=1000&sortBy=omrnr", typeNames);
    }

    public String getWfsQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public String getCqlFilter() {

        List<String> uuids = Arrays.asList(query.split("\\s*,\\s*"));

        String cql_filter = String.format("objekt_id IN (%s)", uuids.stream().collect(Collectors.joining("','", "'", "'")));

        if (hasKommun()) {
            cql_filter += String.format(" AND kommunnamn IN (%s)", getKommunCqlFormatted());
        }

        return cql_filter;
    }
}
