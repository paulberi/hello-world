package se.metria.xplore.sok.exceptions;

public class InternalGeofilterException extends GeofilterException {

    public InternalGeofilterException() {
        super();
    }

    public InternalGeofilterException(GeofilterFailureType failureType) {
        super(failureType);
    }

    public InternalGeofilterException(GeofilterFailureType failureType, String message) {
        super(failureType, message);
    }
}
