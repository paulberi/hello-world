package se.metria.matdatabas.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:saml/saml.properties")
@ConfigurationProperties(prefix = "saml")
public class SAMLProperties {
	private String entityId;
	private String metadataUrl;
	private String keystorePath;
	private String keystorePassword;
	private String keystoreKeyAlias;
	private String keystoreKeyPassword;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getMetadataUrl() {
		return metadataUrl;
	}

	public void setMetadataUrl(String metadataUrl) {
		this.metadataUrl = metadataUrl;
	}

	public String getKeystorePath() {
		return keystorePath;
	}

	public void setKeystorePath(String keystorePath) {
		this.keystorePath = keystorePath;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getKeystoreKeyAlias() {
		return keystoreKeyAlias;
	}

	public void setKeystoreKeyAlias(String keystoreKeyAlias) {
		this.keystoreKeyAlias = keystoreKeyAlias;
	}

	public String getKeystoreKeyPassword() {
		return keystoreKeyPassword;
	}

	public void setKeystoreKeyPassword(String keystoreKeyPassword) {
		this.keystoreKeyPassword = keystoreKeyPassword;
	}
}
