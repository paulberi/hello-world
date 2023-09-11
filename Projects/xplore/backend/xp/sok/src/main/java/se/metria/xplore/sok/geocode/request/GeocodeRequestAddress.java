package se.metria.xplore.sok.geocode.request;

public class GeocodeRequestAddress extends GeocodeRequest {

    private static final String QUERY_TEMPLATE = "outputFormat=json&address={address}&maxAnswers=100";

    public GeocodeRequestAddress(String query, String[] kommun, String maskUrl) {
        setQuery(query);
        setKommun(kommun);
        setMaskUrl(maskUrl);
    }

    public String getQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public Object[] getQueryParameters() {
        return new Object[]{getAddress()};
    }
}