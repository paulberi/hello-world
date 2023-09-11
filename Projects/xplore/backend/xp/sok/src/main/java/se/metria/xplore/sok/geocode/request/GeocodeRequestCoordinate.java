package se.metria.xplore.sok.geocode.request;

public class GeocodeRequestCoordinate extends GeocodeRequest {

    private static final String QUERY_TEMPLATE = "outputFormat=json&x={x}&y={y}";

    public GeocodeRequestCoordinate(Double x, Double y, String[] kommun, String maskUrl) {
        setX(x);
		setY(y);
        setKommun(kommun);
		setMaskUrl(maskUrl);
    }

    public String getQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public Object[] getQueryParameters() {
        return new Object[]{getX(), getY()};
    }
}