package se.metria.skogsanalys.service.rapport;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import se.metria.skogsanalys.exception.UnauthorizedException;
import se.metria.xplore.keycloak.service.KeyCloakService;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Service
public class RapportFetcher {
	private final WebClient webClient;
	private final KeyCloakService keyCloakService;

	@Value("${skogsanalys.rapport.pdf-service-url}")
	private String pdfServiceUrl;

	@Value( "${skogsanalys.rapport.report-page-url}" )
	private String reportPageUrl;

	@Value("${skogsanalys.rapport.timeout:120000}")
	private String timeout;


	public RapportFetcher(WebClient.Builder webClientBuilder,
                          KeyCloakService keyCloakService) {
		this.webClient = webClientBuilder.build();
		this.keyCloakService = keyCloakService;
	}

	public ByteArrayResource fetchReport(String skoglig, String lay, String ext, String title, Boolean inverteraFastigheter, BigDecimal fastighetsgranserOpacity) {
		String reportPageUrlTemplate = reportPageUrl + "/r";
		var timeoutMillis = Integer.parseInt(timeout);

		var reportUrlBuilder = UriComponentsBuilder.fromHttpUrl(reportPageUrlTemplate);
		reportUrlBuilder = reportUrlBuilder.queryParam("skoglig", skoglig);
		reportUrlBuilder = reportUrlBuilder.queryParam("lay", lay);
		reportUrlBuilder = reportUrlBuilder.queryParam("ext", ext);
		reportUrlBuilder = reportUrlBuilder.queryParam("title", title);
		reportUrlBuilder = reportUrlBuilder.queryParam("inverteraFastigheter", inverteraFastigheter);
		reportUrlBuilder = reportUrlBuilder.queryParam("fastighetsgranserOpacity", fastighetsgranserOpacity);

		String accessToken = getKeycloakAccessToken();

		String authUrl = reportPageUrl + "/setSpecialAuth?specialAuthHeader="+
				URLEncoder.encode("Bearer " + accessToken, StandardCharsets.UTF_8);

		PdfServiceParams params = new PdfServiceParams(reportUrlBuilder.build().toUriString());
		params.setAuthOption("authType", "url");
		params.setAuthOption("authUrl", authUrl);
		params.setPdfOption("timeout", String.valueOf(timeoutMillis));

		return webClient.post()
				.uri(pdfServiceUrl)
				.body(BodyInserters.fromObject(params))
				.accept(MediaType.APPLICATION_PDF)
				.retrieve()
				.bodyToMono(ByteArrayResource.class)
				.block();
	}

	private String getKeycloakAccessToken(){
		var authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			throw new UnauthorizedException();
		}

		var principal = (Principal)authentication.getPrincipal();

		var keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;

		return keycloakPrincipal.getKeycloakSecurityContext().getTokenString();
	}

}
