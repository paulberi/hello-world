package se.metria.markkoll.exception;

/**
 * Fel vid kommunikation med Metria Fsök.
 */
public class FsokException extends RuntimeException {

    public FsokException(String message) {
        super(message);
    }

    public FsokException(String message, Throwable cause) {
        super(message, cause);
    }
}
