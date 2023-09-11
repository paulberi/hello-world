package se.metria.xplore.sok.geocode.request;

public class GeocodeRequestAddressCoordinate extends GeocodeRequest {

    private static final String QUERY_TEMPLATE = "outputFormat=json&address={address}&x={x}&y={y}&maxAnswers=100";

    public GeocodeRequestAddressCoordinate(String query, Double x, Double y, String[] kommun, String maskUrl) {
        setQuery(query);
        setX(x);
        setY(y);
        setKommun(kommun);
		setMaskUrl(maskUrl);
    }

    public String getQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public Object[] getQueryParameters() {
        return new Object[]{getAddress(), getX(), getY()};
    }
}