package se.metria.xplore.sok.geocode.request;

public class GeocodeRequestFactory {
    public static GeocodeRequest getRequest(String query, Double x, Double y, String[] kommun, String maskUrl) {
        if (query != null) {
            if (x == null || y == null) {
                return new GeocodeRequestAddress(query, kommun, maskUrl);
            } else {
                return new GeocodeRequestAddressCoordinate(query, x, y, kommun, maskUrl);
            }
        } else {
            return new GeocodeRequestCoordinate(x, y, kommun, maskUrl);
        }
    }
}