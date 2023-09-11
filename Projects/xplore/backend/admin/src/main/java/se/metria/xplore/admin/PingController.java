package se.metria.xplore.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.admin.openapi.api.PingApi;
import se.metria.xplore.admin.security.AdminApiRole;

import javax.annotation.security.RolesAllowed;

@RequestMapping(value = "/api")
@RestController
@CrossOrigin(origins = "*")
public class PingController implements PingApi {
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Override
    public ResponseEntity<String> anonPing() {
        return ResponseEntity.ok("pong!");
    }

    @Override
    @RolesAllowed(AdminApiRole.ADMIN_API_USER)
    public ResponseEntity<String> authPing() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("pong, " + auth.getName() + "!");
    }
}
