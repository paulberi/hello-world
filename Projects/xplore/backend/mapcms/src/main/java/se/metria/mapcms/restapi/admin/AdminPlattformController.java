package se.metria.mapcms.restapi.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.mapcms.commons.keycloakroles.KeycloakRoles;
import se.metria.mapcms.openapi.adminapi.PlattformAdminApi;
import se.metria.mapcms.openapi.model.SprakDto;
import se.metria.mapcms.service.admin.AdminPlattformService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RequestMapping(value = "/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminPlattformController implements PlattformAdminApi {

    @NonNull
    private final AdminPlattformService plattformService;

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<SprakDto>> listSprak() throws Exception {
        return ResponseEntity.ok(plattformService.listSprak());
    }
}
