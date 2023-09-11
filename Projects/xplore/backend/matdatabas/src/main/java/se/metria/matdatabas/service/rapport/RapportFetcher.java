package se.metria.matdatabas.service.rapport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.xplore.keycloak.service.KeyCloakService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class RapportFetcher {
	private final WebClient webClient;
	private final KeyCloakService keyCloakService;

	@Value("${miljokoll.rapport.pdf-service-url}")
	private String pdfServiceUrl;

	@Value( "${miljokoll.rapport.report-page-url}" )
	private String reportPageUrl;

	public RapportFetcher(WebClient.Builder webClientBuilder,
						  KeyCloakService keyCloakService) {
		this.webClient = webClientBuilder.build();
		this.keyCloakService = keyCloakService;
	}

	public ByteArrayResource fetchReport(Integer reportId) {
		String reportPageUrlTemplate = reportPageUrl + "/rapport/%d";

		String reportUrl = String.format(reportPageUrlTemplate, reportId);
		String token = keyCloakService.getNewBatchUserToken().getAccess_token();

		String authUrl = reportPageUrl + "/setSpecialAuth?specialAuthHeader="+
				URLEncoder.encode("Bearer "+token, StandardCharsets.UTF_8);
		PdfServiceParams params = new PdfServiceParams(reportUrl);
		params.setAuthOption("authType", "url");
		params.setAuthOption("authUrl", authUrl);

		return webClient.post()
				.uri(pdfServiceUrl)
				.body(BodyInserters.fromObject(params))
				.accept(MediaType.APPLICATION_PDF)
				.retrieve()
				.bodyToMono(ByteArrayResource.class)
				.block();
	}
}
