package se.metria.markkoll.service.admin;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@MarkkollService
@RequiredArgsConstructor
public class AclService {
    @NonNull
    private final MutableAclService mutableAclService;

    @Transactional
    public void addAclEntries(Sid sid, Collection<AclEntry> aclEntries) {
        if (aclEntries == null || aclEntries.isEmpty()) {
            return;
        }

        for (var entry: aclEntries) {
            var oi = new ObjectIdentityImpl(entry.getObjectClass(), entry.getObjectId());
            var acl = (MutableAcl)mutableAclService.readAclById(oi);

            for (var permission: entry.getPermissions()) {
                acl.insertAce(acl.getEntries().size(), permission, sid, true);
            }

            mutableAclService.updateAcl(acl);
        }
    }

    @Transactional
    public void createObject(Serializable objectId, Class<?> objectClass) {
        var oi = new ObjectIdentityImpl(objectClass, objectId);

        mutableAclService.createAcl(oi);
    }

    @Transactional
    public void
    createObject(Serializable objectId, Class<?> objectClass, Serializable parentObjectId, Class<?> parentObjectClass) {
        var oi = new ObjectIdentityImpl(objectClass, objectId);
        var parentOi = new ObjectIdentityImpl(parentObjectClass, parentObjectId);

        var acl = mutableAclService.createAcl(oi);
        var parentAcl = mutableAclService.readAclById(parentOi);

        acl.setParent(parentAcl);

        mutableAclService.updateAcl(acl);
    }

    @Transactional
    public void deleteChildObjects(Serializable parentObjectId, Class<?> parentObjectClass, Class<?> childClass) {
        var parentOi = new ObjectIdentityImpl(parentObjectClass, parentObjectId);

        var children = mutableAclService.findChildren(parentOi);

        if (children == null) {
            return;
        }

        var childrenOfType = children.stream()
            .filter(child -> child.getType() == childClass.getCanonicalName())
            .collect(Collectors.toList());

        for (var child: childrenOfType) {
            mutableAclService.deleteAcl(child, true);
        }
    }

    @Transactional
    public void deleteObject(Serializable objectId, Class<?> objectClass) {
        var oi = new ObjectIdentityImpl(objectClass, objectId);

        mutableAclService.deleteAcl(oi, true);
    }

    @Transactional
    public Set<Serializable>
    getChildObjects(Serializable parentObjectId, Class<?> parentObjectClass, Class<?> childClass) {

        var parentOi = new ObjectIdentityImpl(parentObjectClass, parentObjectId);

        var children = mutableAclService.findChildren(parentOi);

        if (children == null) {
            return new HashSet<>();
        }
        else {
            return children.stream()
                .filter(oi -> oi.getType().equals(childClass.getCanonicalName()))
                .map(oi -> oi.getIdentifier())
                .collect(Collectors.toSet());
        }
    }

    @Transactional
    public Map<Sid, Set<Permission>> getPermissionsForObject(Serializable objectId, Class<?> objectClass) {
        var oi = new ObjectIdentityImpl(objectClass, objectId);

        var acl = mutableAclService.readAclById(oi);

        var aclsGroupedByUser = acl.getEntries().stream()
            .filter(entry -> entry.isGranting())
            .filter(entry -> entry.getSid() instanceof PrincipalSid)
            .collect(Collectors.groupingBy(AccessControlEntry::getSid));

        return aclsGroupedByUser.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> mergePermissions(entry.getValue())));
    }

    public boolean objectExists(Serializable objectId, Class<?> objectClass) {
        var oi = new ObjectIdentityImpl(objectClass, objectId);

        try {
            mutableAclService.readAclById(oi);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Transactional
    public void removeAclEntries(Sid sid, Collection<AclEntry> aclEntries) {
        if (aclEntries == null || aclEntries.isEmpty()) {
            return;
        }

        for (var entry: aclEntries) {
            var oi = new ObjectIdentityImpl(entry.getObjectClass(), entry.getObjectId());
            var acl = (MutableAcl) mutableAclService.readAclById(oi);

            for (var permission: entry.getPermissions()) {
                acl.deleteAce(getAceIndex(acl, sid, permission));
            }

            mutableAclService.updateAcl(acl);
        }
    }

    @Transactional
    public void removeAllAclEntries(Sid sid, Serializable objectId, Class<?> objectClass) {
        var oi = new ObjectIdentityImpl(objectClass, objectId);

        var acl = (MutableAcl) mutableAclService.readAclById(oi);

        for (var i = acl.getEntries().size() - 1; i >= 0; i--) {
            var entry = acl.getEntries().get(i);
            if (entry.getSid().equals(sid)) {
                acl.deleteAce(i);
            }
        }

        mutableAclService.updateAcl(acl);
    }

    private Integer getAceIndex(Acl acl, Sid sid, Permission permission) {
        var entries = acl.getEntries();
        for (var i = 0; i < entries.size(); i++) {
            var entry = entries.get(i);

            if (entry.getPermission().getMask() == permission.getMask() && entry.getSid().equals(sid)) {
                return i;
            }
        }

        throw new IllegalArgumentException();
    }

    private Set<Permission>
    mergePermissions(Collection<AccessControlEntry> entries) {
        return entries.stream().map(e -> e.getPermission()).collect(Collectors.toSet());
    }

    @Data
    @RequiredArgsConstructor
    private class AclObject {
        @NonNull
        private Serializable objectId;

        @NonNull
        private Class<?> objectClass;
    }
}
