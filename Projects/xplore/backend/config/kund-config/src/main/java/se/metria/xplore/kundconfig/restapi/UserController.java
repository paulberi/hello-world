package se.metria.xplore.kundconfig.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.kundconfig.openapi.api.UserApi;
import se.metria.xplore.kundconfig.openapi.model.PermissionsDto;
import se.metria.xplore.kundconfig.openapi.model.ProduktDto;
import se.metria.xplore.kundconfig.openapi.model.XpUserDto;
import se.metria.xplore.kundconfig.security.KundConfigRole;
import se.metria.xplore.kundconfig.service.PermissionsService;
import se.metria.xplore.kundconfig.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class UserController implements UserApi {

    @NonNull
    private final PermissionsService permissionsService;

    @NonNull
    private final UserService userService;

    @Override
    public ResponseEntity<List<PermissionsDto>> getPermissions(String userId, ProduktDto produktDto) {
        var permissions = permissionsService.getPermissions(userId, produktDto);

        return ResponseEntity.ok(permissions);
    }

    @Override
    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public ResponseEntity<List<XpUserDto>> getAllUsers() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<XpUserDto> getUser(String id) {
        var user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}
