package se.metria.skogsmaskindata.service.sftp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import se.metria.skogsmaskindata.service.sftp.exceptions.FileDeletionException;
import se.metria.skogsmaskindata.service.sftp.exceptions.FileMoveException;
import se.metria.skogsmaskindata.service.sftp.exceptions.FileRetrievalException;
import se.metria.skogsmaskindata.service.sftp.exceptions.ListRemoteFilesException;

import org.springframework.stereotype.Service;

@Service
public class SftpService {

	private SftpProperties sftpProperties;
	private ChannelSftp sftpChannel = null;

	public SftpService(SftpProperties sftpProperties) {
		this.sftpProperties = sftpProperties;
	}

	public List<String> listFilesOnRemote(String path) throws JSchException, ListRemoteFilesException {
		try {
			Vector<ChannelSftp.LsEntry> entries = getSftpChannel().ls(path);
			return entries.stream().map(ChannelSftp.LsEntry::getFilename).collect(Collectors.toList());
		} catch (SftpException e) {
			throw new ListRemoteFilesException(path, e.getMessage());
		}
	}

	ChannelSftp getSftpChannel() throws JSchException {
		if (sftpChannel == null || !sftpChannel.isConnected()) {
			JSch jsch = new JSch();
			jsch.setKnownHosts(new ByteArrayInputStream(sftpProperties.getKnownHostKey().getBytes()));
			Session session = jsch.getSession(sftpProperties.getUser(), sftpProperties.getRemoteUrl());
			session.setPassword(sftpProperties.getPassword());
			session.connect();

			sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
		}
		return sftpChannel;
	}

	public InputStream getRemoteFile(String pathToFile) throws FileRetrievalException, JSchException {
		try {
			InputStream is = getSftpChannel().get(pathToFile);
			if(is == null) {
				throw new FileRetrievalException(pathToFile, "InputStream was null for path: "+ pathToFile);
			}
			return is;
		} catch (SftpException e) {
			throw new FileRetrievalException(pathToFile, e.getMessage());
		}
	}

	public void deleteRemoteFile(String pathToFile) throws FileDeletionException {
		try {
			getSftpChannel().rm(pathToFile);
		} catch (JSchException | SftpException e) {
			throw new FileDeletionException(pathToFile, e.getMessage());
		}
	}

	public void moveRemoteFile(String pathToFile, String pathToDestination) throws FileMoveException {
		try {
			getSftpChannel().rename(pathToFile, pathToDestination);
		} catch(JSchException | SftpException e) {
			throw new FileMoveException(pathToFile, pathToDestination, e.getMessage());
		}
	}

}
