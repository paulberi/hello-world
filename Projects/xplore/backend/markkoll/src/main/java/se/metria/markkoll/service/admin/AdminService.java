package se.metria.markkoll.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.MarkkollUserDto;
import se.metria.markkoll.openapi.model.RoleTypeDto;
import se.metria.markkoll.openapi.model.UserAndRoleDto;
import se.metria.markkoll.openapi.model.UserInfoDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.util.RoleUtil;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@MarkkollService
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    @NonNull
    private final KundService kundService;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final RoleAclService roleAclService;

    @NonNull
    private final UserService userService;

    @Transactional
    public void addProjektRole(String userId, RoleTypeDto roleType, UUID projektId) {
        assertProjekt(projektId, roleType);

        log.info("Lägger till rollen {} för användaren {} i projekt {}", roleType, userId, projektId);

        userService.addRole(userId, roleType, projektId);
    }

    @Transactional(rollbackFor = IOException.class)
    public void createKundanvandare(String kundId, UserInfoDto userInfo) throws IOException {
        if (!kundService.exists(kundId)) {
            throw new IllegalArgumentException("Kund existerar inte: " + kundId);
        }

        createUserWithRole(userInfo, RoleTypeDto.KUNDANVANDARE, kundId);
    }

    @Transactional(rollbackFor = IOException.class)
    public void createKundadmin(String kundId, UserInfoDto userInfo) throws IOException {
        kundService.createIfNotExists(kundId);

        createUserWithRole(userInfo, RoleTypeDto.KUNDADMIN, kundId);
    }

    @Transactional
    public void removeProjektRole(String userId, RoleTypeDto roleType, UUID projektId) {
        assertProjekt(projektId, roleType);

        log.info("Tar bort rollen {} för användaren {} i projekt {}", roleType, userId, projektId);

        roleAclService.removeRole(userId, roleType, projektId);
    }

    @Transactional
    public void setProjektUsersRoles(UUID projektId, Collection<UserAndRoleDto> usersAndRoles) {
        assertUsersAndRoles(usersAndRoles);

        var userIds = userService.getProjektUsersIds(projektId);

        log.info("Uppdaterar {} roller för {} användare", usersAndRoles.size(), userIds.size());

        for (var userId: userIds) {
            userService.removeProjektRoles(userId, projektId);
        }

        for (var userAndRole: usersAndRoles) {
            userService.addRole(userAndRole.getUserId(), userAndRole.getRoleType(), projektId);
        }
    }

    @Transactional
    public void deleteAllProjektUsersRoles(UUID projektId) {

        var userIds = userService.getProjektUsersIds(projektId);

        log.info("Tar bort samtliga roller för {} användare i projektet {}", userIds.size(), projektId);

        for (var userId: userIds) {
            userService.removeProjektRoles(userId, projektId);
        }
    }

    @Transactional
    public void resetAllRoles() {
        var userIds = userService.getAllIds();
        log.info("Nollställer behörigheter för {} användare", userIds.size());

        for (var userId: userIds) {
            userService.resetAllRoles(userId);
        }
    }
    
    public List<MarkkollUserDto> getKundadminsForKund(String kundId) {
        return userService.getKundadminsForKund(kundId);
    }

    private void assertProjekt(UUID projektId, RoleTypeDto roleType) {
        if (!isProjektRole(roleType)) {
            throw new IllegalArgumentException("Inte en projektroll: " + roleType);
        }
        else if (!projektRepository.existsById(projektId)) {
            throw new EntityNotFoundException();
        }
    }

    private void assertUsersAndRoles(Collection<UserAndRoleDto> usersAndRoles) {
        for (var userAndRole: usersAndRoles) {
            var roleType = userAndRole.getRoleType();
            var userId = userAndRole.getUserId();

            if (!isProjektRole(roleType)) {
                throw new IllegalArgumentException(MessageFormat.format(
                    "Ej giltig projektroll ({}) tilldelad till användare {}", roleType, userId));
            }
        }
    }

    private void
    createUserWithRole(UserInfoDto userInfo, RoleTypeDto roleType, String kundId) throws IOException
    {

        log.info("Skapar användare {} med roll {} för kund {}", userInfo.getEmail(), roleType, kundId);

        var userId = userService.create(userInfo, kundId);
        userService.addRole(userId, roleType, kundId);
    }

    private boolean isProjektRole(RoleTypeDto roleType) {
        return RoleUtil.getProjektRoleTypes().contains(roleType);
    }
}
