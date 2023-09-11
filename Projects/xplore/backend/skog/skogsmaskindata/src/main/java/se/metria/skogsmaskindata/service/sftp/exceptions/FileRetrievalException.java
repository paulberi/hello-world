package se.metria.skogsmaskindata.service.sftp.exceptions;

public class FileRetrievalException extends Exception {

	String filePath = null;

	public FileRetrievalException(String filePath, String message) {
		super(message);
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
}
