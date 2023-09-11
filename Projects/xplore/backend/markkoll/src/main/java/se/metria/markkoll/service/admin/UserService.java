package se.metria.markkoll.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.admin.RoleEntity;
import se.metria.markkoll.entity.admin.UserEntity;
import se.metria.markkoll.openapi.model.MarkkollUserDto;
import se.metria.markkoll.openapi.model.RoleDto;
import se.metria.markkoll.openapi.model.RoleTypeDto;
import se.metria.markkoll.openapi.model.UserInfoDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.admin.UserRepository;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.RoleUtil;
import se.metria.xplore.geoserveradmin.service.GeoserverAdminService;
import se.metria.xplore.keycloak.service.KeyCloakService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@MarkkollService
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final GeoserverAdminService geoserverAdminService;

    @NonNull
    private final KeyCloakService keyCloakService;

    @NonNull
    private final KundRepository kundRepository;

    @NonNull
    private final KundService kundService;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final RoleAclService roleAclService;

    @NonNull
    private final UserRepository userRepository;

    @Value("${markkoll.admin.disable-external-services-create:false}")
    boolean disableExternalServicesCreate;

    @Value("${markkoll.admin.disable-external-services-delete:false}")
    boolean disableExternalServicesDelete;

    @Transactional
    public void addRole(String userId, RoleTypeDto roleType, Serializable objectId) {
        log.info("Lägger till roll till användare {}: {} {}", userId, roleType, objectId);

        var userEntity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        var added = userEntity.addRole(new RoleEntity(objectId.toString(), roleType, userEntity));

        if (added) {
            log.info("Roll tillagd till användare {}: {} {}", userId, roleType, objectId);
            roleAclService.addRole(userId, roleType, objectId);
        }
    }

    @Transactional
    public String create(UserInfoDto userInfo, String kundId) throws IOException {
        log.info("Skapar användare {} för kund", userInfo.getEmail(), kundId);

        var userId = userInfo.getEmail().toLowerCase();

        if (userRepository.existsById(userId)) {
            throw new EntityExistsException();
        }

        var kundEntity = kundRepository.findById(kundId).orElseThrow(EntityNotFoundException::new);

        var userEntity = new UserEntity(userId, userInfo.getEfternamn(), userInfo.getEmail(), userInfo.getFornamn(),
            kundEntity);

        userRepository.save(userEntity);
        aclService.createObject(userId, UserEntity.class, kundId, KundEntity.class);

        if (!disableExternalServicesCreate) {
            keyCloakService.createUser(userId, userInfo.getFornamn(), userInfo.getEfternamn(), "Ettlösenordlångtöver16tecken");
            keyCloakService.addRealmRoleToUser(userId, "markkoll");

            geoserverAdminService.createUser(geoserverUsername(userId), "markkoll", true);
            geoserverAdminService.addRoleToUser(kundId, geoserverUsername(userId));
        }

        log.info("Användare {} skapad", userInfo.getEmail());

        return userId;
    }

    @Transactional
    public void delete(String userId) {
        log.info("Tar bort användare: {}", userId);

        var entity = userRepository.getOne(userId);
        for (var role: entity.getRoles()) {
            roleAclService.removeRole(userId, role.getRoleType(), role.getObjectId());
        }
        userRepository.delete(entity);

        aclService.deleteObject(userId, UserEntity.class);

        if (!disableExternalServicesDelete) {
            keyCloakService.deleteUserByUsername(userId);
            geoserverAdminService.deleteUser(userId);
        }

        log.info("Användare borttagen: {}", userId);
    }

    public MarkkollUserDto get(String userId) {
        var entity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, MarkkollUserDto.class);
    }

    public Set<String> getAllIds() {
        return userRepository.getAllIds();
    }

    public String getKund(String userId) {
        return userRepository.getKundId(userId);
    }

    public MarkkollUserDto getCurrentUser() {
        var userId = getPreferredUsername();

        var entity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, MarkkollUserDto.class);
    }

    public String getCurrentUserId() {
        return getPreferredUsername();
    }

    public List<MarkkollUserDto> getProjektUsers(UUID projektId) {
        var entities = userRepository.getUsersForRoles(projektId.toString(),
            RoleUtil.getProjektRoleTypes());

        return CollectionUtil.modelMapperList(entities, modelMapper, MarkkollUserDto.class);
    }

    public Set<String> getProjektUsersIds(UUID projektId) {
        return userRepository.getUserIdsForRoles(projektId.toString(),
            RoleUtil.getProjektRoleTypes());
    }

    public List<RoleDto> getRoles(String userId) {
        var roleEntities = userRepository.getRoles(userId);

        return CollectionUtil.modelMapperList(roleEntities, modelMapper, RoleDto.class);
    }

    @Transactional
    public List<MarkkollUserDto>
    getUsersWithPermission(Serializable objectId, Class<?> objectClass, Permission permission) {

        var usersPermissions = aclService.getPermissionsForObject(objectId, objectClass);

        var usersRead = usersPermissions.entrySet().stream()
            .filter(entry -> entry.getValue().contains(permission))
            .map(entry -> entry.getKey())
            .filter(sid -> sid instanceof PrincipalSid)
            .map(sid -> (PrincipalSid)sid)
            .map(principalSid -> principalSid.getPrincipal())
            .collect(Collectors.toList());

        var entities = userRepository.findAllById(usersRead);

        return CollectionUtil.modelMapperList(entities, modelMapper, MarkkollUserDto.class);
    }

    /* REQUIRES_NEW här så JPA flushar. Vi använder orphan removal när vi tar bort rollerna, och om vi försöker lägga
    * till samma roll igen senare utan att flusha, så kommer det krocka med den unique constraint vi har för rollen. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeProjektRoles(String userId, UUID projektId) {
        log.info("Tar bort projektroller i projekt {} för användare {}", projektId, userId);

        var userEntity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        for (var roleType: RoleUtil.getProjektRoleTypes()) {
            var removed = userEntity.removeRole(new RoleEntity(projektId.toString(), roleType, userEntity));

            if (removed) {
                log.info("Roll borttagen från användare {}: {} {}", userId, roleType, projektId);
                roleAclService.removeRole(userId, roleType, projektId);
            }
        }
    }

    @Transactional
    public void removeRole(String userId, RoleTypeDto roleType, Serializable objectId) {
        log.info("Tar bort roll från användare {}: {} {}", userId, roleType, objectId);

        var userEntity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        var removed = userEntity.removeRole(new RoleEntity(objectId.toString(), roleType, userEntity));

        if (removed) {
            log.info("Roll borttagen från användare {}: {} {}", userId, roleType, objectId);
            roleAclService.removeRole(userId, roleType, objectId);
        }
    }

    @Transactional
    public void resetAllRoles(String userId) {
        log.info("Nollställer alla roller för användare {}", userId);

        var userEntity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        for (var role: userEntity.getRoles()) {
            roleAclService.resetRole(userId, role.getRoleType(), role.getObjectId());
        }
    }

    public List<MarkkollUserDto> getKundadminsForKund(String kundId){
        var entities = userRepository.getUserForKundByRoleType(kundId, RoleTypeDto.KUNDADMIN);

        return CollectionUtil.modelMapperList(entities, modelMapper, MarkkollUserDto.class);
    }

    @Transactional
    public MarkkollUserDto updateUserInfo(String userId, UserInfoDto updatedUserInfo) {
        var userEntity = userRepository.getOne(userId);

        BeanUtils.copyProperties(updatedUserInfo, userEntity);

        return modelMapper.map(userEntity, MarkkollUserDto.class);
    }

    private String getPreferredUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var keycloakPrincipal = (KeycloakPrincipal)authentication.getPrincipal();
        return keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    private String geoserverUsername(String username) {
        return username.replaceAll("\\.", "_");
    }
}
