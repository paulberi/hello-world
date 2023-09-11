package se.metria.skogsmaskindata.service.sftp.exceptions;

public class FileDeletionException extends Exception {

	String filePath = null;

	public FileDeletionException(String filePath, String message) {
		super(message);
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
}
