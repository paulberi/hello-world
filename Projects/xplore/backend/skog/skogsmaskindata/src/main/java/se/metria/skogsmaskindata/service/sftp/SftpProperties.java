package se.metria.skogsmaskindata.service.sftp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SftpProperties {

	@Value("${sftp.url-to-remote}")
	private String ftpUrl;

	@Value("${sftp.user}")
	private String user;

	@Value("${sftp.password}")
	private String password;

	@Value("${sftp.knownhostkey}")
	private String knownHostKey;

	public String getRemoteUrl() {
		return ftpUrl;
	}


	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getKnownHostKey() {
		return knownHostKey;
	}

}
