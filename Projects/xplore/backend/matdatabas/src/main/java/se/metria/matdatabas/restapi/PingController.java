package se.metria.matdatabas.restapi;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.PingApi;
import se.metria.matdatabas.security.MatdatabasRole;

import javax.annotation.security.RolesAllowed;

@RequestMapping(value = "/api")
@RestController
public class PingController implements PingApi {

	@Override
	public ResponseEntity<String> anonPing() {
		return ResponseEntity.ok("pong!");
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<String> authPing() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return ResponseEntity.ok("pong, " + auth.getName() + "!");
	}
}
