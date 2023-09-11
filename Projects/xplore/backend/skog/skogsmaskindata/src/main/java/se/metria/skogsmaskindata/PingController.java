package se.metria.skogsmaskindata;

import se.metria.skogsmaskindata.openapi.api.PingApi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController implements PingApi {

	public PingController() {
	}

	@Override
	public ResponseEntity<Void> pingGet() {
		return ResponseEntity.ok().build();
	}
}
