package se.metria.skogsmaskindata.service.sftp.exceptions;

public class FileMoveException extends Exception {

	String filePath = null;
	String fileDestination = null;

	public FileMoveException(String filePath, String fileDestination, String message) {
		super(message);
		this.filePath = filePath;
		this.fileDestination = fileDestination;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileDestination() {
		return fileDestination;
	}
}
