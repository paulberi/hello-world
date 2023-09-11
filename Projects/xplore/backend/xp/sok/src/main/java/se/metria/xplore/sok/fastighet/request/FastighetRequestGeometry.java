package se.metria.xplore.sok.fastighet.request;

import org.springframework.http.HttpMethod;

public abstract class FastighetRequestGeometry extends FastighetRequest {
    private final String queryTemplate;

    public FastighetRequestGeometry(String query, String[] kommun, String typeName, int maxFeatures, int startIndex) {
        super(query, kommun);
        // Detta borde vara FWS version 2.0.0, men det kräver andra ändringar
        queryTemplate = String.format("service=WFS&version=1.0.0&srsName=EPSG:3006&request=GetFeature&typeName=%s&cql_filter={cql_filter}&outputFormat=application/json&maxFeatures=%d&startIndex=%d",
                typeName, maxFeatures, startIndex);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    public String getWfsQueryTemplate() {
        return queryTemplate;
    }

    @Override
    public String getWfsQuery() {
        return queryTemplate.replace("{cql_filter}", getCqlFilter());
    }
}
