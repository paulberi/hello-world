package se.metria.xplore.sok.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import se.metria.xplore.sok.SokProperties;

public abstract class BaseClient {
	protected SokProperties properties;

	public BaseClient(SokProperties properties) {
		this.properties = properties;
	}

	protected HttpHeaders getHeaders() {
		return getHeaders(null);
	}

	protected HttpHeaders getHeaders(MediaType contentType) {
		HttpHeaders headers = new HttpHeaders();

		if (contentType != null) {
			headers.setContentType(contentType);
		}

		String auth = getAuth();
		if (auth != null) {
			headers.set("Authorization", auth);
		}

		return headers;
	}

	protected String getAuth() {
		// Authorization till tjänsterna antas finnas i ingående anrop (läggs på av t.ex nginx eller liknande).
		// För att köra på localhost kan man sätta auth istället i application.properties
		if (properties.getAuth() != null) {
			return properties.getAuth();
		}

		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
	}
}
