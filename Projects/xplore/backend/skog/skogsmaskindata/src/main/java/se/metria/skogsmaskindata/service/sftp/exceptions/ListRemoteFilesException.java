package se.metria.skogsmaskindata.service.sftp.exceptions;

public class ListRemoteFilesException extends Exception{
	String filePath = null;

	public ListRemoteFilesException(String filePath, String message) {
		super(message);
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
}
