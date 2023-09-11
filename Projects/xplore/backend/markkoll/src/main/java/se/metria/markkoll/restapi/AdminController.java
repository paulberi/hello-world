package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.AdminApi;
import se.metria.markkoll.openapi.model.MarkkollUserDto;
import se.metria.markkoll.openapi.model.RoleTypeDto;
import se.metria.markkoll.openapi.model.UserAndRoleDto;
import se.metria.markkoll.openapi.model.UserInfoDto;
import se.metria.markkoll.security.MarkkollRole;
import se.metria.markkoll.service.admin.AdminService;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController implements AdminApi {
    @NonNull
    private final AdminService adminService;

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<Void> addRoleProjektadmin(UUID projektId, String userId) {
        adminService.addProjektRole(userId, RoleTypeDto.PROJEKTADMIN, projektId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<Void> addRoleProjekthandlaggare(UUID projektId, String userId) {
        adminService.addProjektRole(userId, RoleTypeDto.PROJEKTHANDLAGGARE, projektId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<Void> setProjektUsersRoles(UUID projektId, List<UserAndRoleDto> usersAndRoles) {
        adminService.setProjektUsersRoles(projektId, usersAndRoles);

        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(MarkkollRole.GLOBAL_ADMIN)
    public ResponseEntity<Void> createKundadmin(String kundId, @Valid UserInfoDto userInfoDto) throws Exception {
        try {
            adminService.createKundadmin(kundId, userInfoDto);

            return ResponseEntity.ok().build();
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION') || hasRole('admin')")
    public ResponseEntity<Void> createUser(String kundId, @Valid UserInfoDto userInfoDto) throws Exception {
        try {
            adminService.createKundanvandare(kundId, userInfoDto);

            return ResponseEntity.ok().build();
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<Void> removeRoleProjektadmin(UUID projektId, String userId) {
        adminService.removeProjektRole(userId, RoleTypeDto.PROJEKTADMIN, projektId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasProjektPermission(#projektId, 'ADMINISTRATION')")
    public ResponseEntity<Void> removeRoleProjekthandlaggare(UUID projektId, String userId) {
        adminService.removeProjektRole(userId, RoleTypeDto.PROJEKTHANDLAGGARE, projektId);

        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(MarkkollRole.GLOBAL_ADMIN)
    public ResponseEntity<Void> resetAllRoles() {
        adminService.resetAllRoles();

        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(MarkkollRole.GLOBAL_ADMIN)
    public ResponseEntity<List<MarkkollUserDto>> getKundAdminsForKund(String kundId) throws Exception {
        var users = adminService.getKundadminsForKund(kundId);
        return ResponseEntity.ok(users);
    }
}
