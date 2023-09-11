package se.metria.xplore.sok.exceptions;

public abstract class GeofilterException extends Exception {
    private GeofilterFailureType failureType;

    public GeofilterException() {
        super();
        failureType = GeofilterFailureType.UNKNOWN;
    }

    public GeofilterException(GeofilterFailureType failureType) {
        super();
        this.failureType = failureType;
    }

    public GeofilterException(GeofilterFailureType failureType, String message) {
        super(message);
        this.failureType = failureType;
    }
}
