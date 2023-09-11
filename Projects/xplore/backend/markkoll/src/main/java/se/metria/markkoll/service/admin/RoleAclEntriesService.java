package se.metria.markkoll.service.admin;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.model.Permission;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.RoleDto;
import se.metria.markkoll.openapi.model.RoleTypeDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.util.CollectionUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static se.metria.markkoll.security.MkPermission.*;

@MarkkollService
@RequiredArgsConstructor
public class RoleAclEntriesService {

    @NonNull
    private final ProjektRepository projektRepository;

    public Set<AclEntry> getRoleAclEntries(RoleTypeDto roleType, Serializable objectId) {
        switch (roleType) {
            case PROJEKTADMIN:
                return getProjektadminAclEntries(objectId);
            case PROJEKTHANDLAGGARE:
                return getProjekthandlaggareAclEntries(objectId);
            case KUNDADMIN:
                return getKundadminAclEntries(objectId);
            case KUNDANVANDARE:
                return getKundanvandareAclEntries(objectId);
            default:
                throw new IllegalArgumentException("Okänd rolltyp: " + roleType);
        }
    }

    public Set<AclEntry> getRolesAclEntries(Collection<RoleDto> roles) {
        var aclEntries = roles.stream()
            .map(role -> getRoleAclEntries(role.getRoleType(), role.getObjectId()))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        var entriesGroupedByObject = aclEntries.stream()
            .collect(Collectors.groupingBy(aclEntry -> new AclObject(aclEntry.getObjectId(), aclEntry.getObjectClass())));

        return entriesGroupedByObject.entrySet().stream()
            .map(entry -> mergeEntriesPermissions(entry.getKey(), entry.getValue()))
            .collect(Collectors.toSet());
    }

    private AclEntry AclEntryProjekt(Serializable projektId, Permission... permissions) {
        return AclEntry.of(getProjektId(projektId), ProjektEntity.class, permissions);
    }

    private AclEntry AclEntryKund(Serializable kundId, Permission... permissions) {
        return AclEntry.of(getKundId(kundId), KundEntity.class, permissions);
    }

    private Set<AclEntry> getKundadminAclEntries(Serializable objectId) {
        return CollectionUtil.asSet(AclEntryKund(objectId, KUND_READ, ADMINISTRATION, CREATE, DELETE, READ,
            WRITE));
    }

    private Set<AclEntry> getKundanvandareAclEntries(Serializable objectId) {
        return CollectionUtil.asSet(AclEntryKund(objectId, KUND_READ, CREATE));
    }

    private Set<AclEntry> getProjektadminAclEntries(Serializable objectId) {
        var projektId = getProjektId(objectId);
        return CollectionUtil.asSet(
            AclEntryProjekt(projektId, ADMINISTRATION, CREATE, DELETE, READ, WRITE)
        );
    }

    private Set<AclEntry> getProjekthandlaggareAclEntries(Serializable objectId) {
        var projektId = getProjektId(objectId);
        return CollectionUtil.asSet(
            AclEntryProjekt(projektId, READ, WRITE, CREATE, DELETE)
        );
    }

    private String getKundId(Serializable objectId) {
        return objectId.toString();
    }

    private UUID getProjektId(Serializable objectId) {
        return UUID.fromString(objectId.toString());
    }

    private AclEntry mergeEntriesPermissions(AclObject aclObject, Collection<AclEntry> entries) {
        var permissionsMerged = entries.stream()
            .map(AclEntry::getPermissions)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        return new AclEntry(aclObject.objectId, aclObject.getObjectClass(), permissionsMerged);
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
