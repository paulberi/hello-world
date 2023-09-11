package se.metria.xplore.sok.wfs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

public abstract class WfsRequest {

    protected String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        setQuery(query, true);
    }

    public void setQuery(String query, boolean validate) {
        if (query != null) {
            this.query = query.trim();
        }

        if (validate && StringUtils.isEmpty(query)) {
            throw new IllegalArgumentException("Invalid query");
        }
    }

    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    public abstract String getWfsQueryTemplate();

    public String getWfsQuery() {
    	return null;
	}

    public abstract String getCqlFilter();
}