package se.metria.markkoll.exception;

public class DokumentException extends Exception {
    public DokumentException(String msg) {
        super(msg);
    }

    public DokumentException(Throwable cause) {
        super(cause);
    }
}