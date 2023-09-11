package se.metria.xplore.sok.exceptions;

public enum GeofilterFailureType {
    UNKNOWN,
    INTERNAL_ERROR,
    MISSING_MASK,
    EMPTY_MASK,
    MASK_DOWNLOAD,
    MASK_CONVERSION
}
