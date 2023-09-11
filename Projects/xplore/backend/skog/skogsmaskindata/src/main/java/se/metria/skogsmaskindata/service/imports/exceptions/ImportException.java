package se.metria.skogsmaskindata.service.imports.exceptions;

public abstract class ImportException extends Exception {

    public ImportException() {
        super();
    }

    public ImportException(String message) {
        super(message);
    }
}
