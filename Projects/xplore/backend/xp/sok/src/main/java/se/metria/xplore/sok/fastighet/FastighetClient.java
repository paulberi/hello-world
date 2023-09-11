package se.metria.xplore.sok.fastighet;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.metria.xplore.sok.fastighet.request.FastighetRequest;
import se.metria.xplore.sok.util.SokUtil;
import se.metria.xplore.sok.wfs.WfsClient;

@Service
public class FastighetClient {
	private final WfsClient wfsClient;

	public FastighetClient(WfsClient wfsClient) {
		this.wfsClient = wfsClient;
	}

	public ResponseEntity<String> request(FastighetRequest request) {
		ResponseEntity<String> response = wfsClient.request(request);
		return SokUtil.createJsonResponse(response.getBody());
	}
}
