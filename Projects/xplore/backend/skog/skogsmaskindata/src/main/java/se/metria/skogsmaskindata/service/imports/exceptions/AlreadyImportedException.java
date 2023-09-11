package se.metria.skogsmaskindata.service.imports.exceptions;

public class AlreadyImportedException extends ImportException {
	public AlreadyImportedException() {
		super();
	}

	public AlreadyImportedException(String message) {
		super(message);
	}
}
