package se.metria.markkoll.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.RoleTypeDto;

import java.io.Serializable;

@MarkkollService
@RequiredArgsConstructor
@Slf4j
public class RoleAclService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final RoleAclEntriesService roleAclEntriesService;

    @Transactional
    public void addRole(String userId, RoleTypeDto roleType, Serializable objectId) {
        var entries = roleAclEntriesService.getRoleAclEntries(roleType, objectId);
        aclService.addAclEntries(new PrincipalSid(userId), entries);
    }

    @Transactional
    public void removeRole(String userId, RoleTypeDto roleType, Serializable objectId) {
        var permissions = roleAclEntriesService.getRoleAclEntries(roleType, objectId);
        aclService.removeAclEntries(new PrincipalSid(userId), permissions);
    }

    @Transactional
    public void resetRole(String userId, RoleTypeDto roleType, Serializable objectId) {
        var entries = roleAclEntriesService.getRoleAclEntries(roleType, objectId);
        var sid = new PrincipalSid(userId);

        for (var e: entries) {
            aclService.removeAllAclEntries(sid, e.getObjectId(), e.getObjectClass());
        }
        aclService.addAclEntries(sid, entries);
    }
}
