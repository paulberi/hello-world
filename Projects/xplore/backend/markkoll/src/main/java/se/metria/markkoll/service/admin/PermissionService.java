package se.metria.markkoll.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.RoleDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.RoleUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@MarkkollService
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    @NonNull
    private final PermissionEvaluator permissionEvaluator;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final UserService userService;

    @Transactional
    public <T extends Serializable> Set<T>
    filterObjectsByPermission(String userId, Class<?> objectClass, Permission permission)
    {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var roles = userService.getRoles(userId);

        return roles.stream()
            .map(this::getProjektIds)
            .flatMap(Collection::parallelStream)
            .map(id ->(T)id)
            .filter(id -> permissionEvaluator.hasPermission(auth, id, objectClass.getName(), permission))
            .collect(toSet());
    }

    private Set<UUID> getProjektIds(RoleDto role) {
        if (RoleUtil.isProjektRole(role)) {
            return CollectionUtil.asSet(UUID.fromString(role.getObjectId()));
        }
        else {
            return projektRepository.getProjektIds(role.getObjectId());
        }
    }
}
