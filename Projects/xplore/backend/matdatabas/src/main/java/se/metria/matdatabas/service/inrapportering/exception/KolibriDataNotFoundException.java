package se.metria.matdatabas.service.inrapportering.exception;

public class KolibriDataNotFoundException extends RuntimeException{
    public KolibriDataNotFoundException() {
    }

    public KolibriDataNotFoundException(String message) {
        super(message);
    }

    public KolibriDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public KolibriDataNotFoundException(Throwable cause) {
        super(cause);
    }

    public KolibriDataNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
