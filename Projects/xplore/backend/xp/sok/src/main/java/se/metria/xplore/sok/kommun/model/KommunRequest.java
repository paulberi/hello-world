package se.metria.xplore.sok.kommun.model;

import org.apache.commons.lang3.StringUtils;
import se.metria.xplore.sok.util.LanUtil;
import se.metria.xplore.sok.wfs.WfsRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KommunRequest extends WfsRequest {

    private static final String QUERY_TEMPLATE = "service=WFS&version=2.0.0&request=GetFeature&typeName=metria:Kommun_topo10&cql_filter={cql_filter}&outputFormat=json&count=100";

    protected String lan;

    public KommunRequest(String query, String lan) {
        setQuery(query, false);
        setLan(lan);

        validate();
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        if (lan != null) {
            this.lan = lan.trim();

            if (this.lan.isEmpty()) {
                throw new IllegalArgumentException("Invalid län");
            }
        }
    }

    public String getWfsQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public String getCqlFilter() {
        String cql_filter = "";

        if (query != null && !query.isEmpty()) {
            cql_filter += String.format("strToUpperCase(kommunnamn) LIKE '%s%s'", query.toUpperCase(), (query.length() > 1 ? "%%" : ""));
        }

        if (lan != null && !lan.isEmpty()) {
			if (!cql_filter.isEmpty()) {
        		cql_filter +=  " AND ";
			}

			List<String> lanskoder = LanUtil.mapLansnamnToLanskoder(lan);

			if (lanskoder.isEmpty()) {
				// Ensures that the returned result has no features included
				cql_filter += "lankod = 0";
			} else {
				cql_filter += String.format("lankod IN (%s)", String.join(",", lanskoder));
			}

        }

        return cql_filter;
    }

    private void validate() {
        if (StringUtils.isEmpty(query) && StringUtils.isEmpty(lan)) {
            throw new IllegalArgumentException("Invalid request");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KommunRequest that = (KommunRequest) o;

        if (!Objects.equals(query, that.query)) return false;
        return Objects.equals(lan, that.lan);
    }

    @Override
    public int hashCode() {
        int result = query != null ? query.hashCode() : 0;
        result = 31 * result + (lan != null ? lan.hashCode() : 0);
        return result;
    }
}
