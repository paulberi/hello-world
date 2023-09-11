package se.metria.xplore.sok.exceptions;

public class ExternalGeofilterException extends GeofilterException {

    public ExternalGeofilterException() {
        super();
    }

    public ExternalGeofilterException(GeofilterFailureType failureType) {
        super(failureType);
    }

    public ExternalGeofilterException(GeofilterFailureType failureType, String message) {
        super(failureType, message);
    }
}
