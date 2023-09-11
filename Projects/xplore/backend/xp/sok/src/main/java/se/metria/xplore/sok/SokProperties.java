package se.metria.xplore.sok;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sok-adress")
public class SokProperties {

	@Value("${sok.geocode.endpoint}")
	private String geocodeEndpoint;

	@Value("${sok.wfs.endpoint}")
	private String wfsEndpoint;

	@Value("${sok.auth:#{null}}")
	private String auth;

	public String getGeocodeEndpoint() {
		return geocodeEndpoint;
	}

	public void setGeocodeEndpoint(String geocodeEndpoint) {
		this.geocodeEndpoint = geocodeEndpoint;
	}

	public String getWfsEndpoint() {
		return wfsEndpoint;
	}

	public void setWfsEndpoint(String wfsEndpoint) {
		this.wfsEndpoint = wfsEndpoint;
	}

    public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}
}
