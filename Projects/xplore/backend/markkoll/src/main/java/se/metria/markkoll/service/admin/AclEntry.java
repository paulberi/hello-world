package se.metria.markkoll.service.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.acls.model.Permission;
import se.metria.markkoll.util.CollectionUtil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class AclEntry {
    private Serializable objectId;

    private Class<?> objectClass;

    private Set<Permission> permissions;

    public static AclEntry of(Serializable objectId, Class<?> objectClass, Permission... permissions) {
        if (permissions == null) {
            return new AclEntry(objectId, objectClass, new HashSet<>());
        }
        else {
            return new AclEntry(objectId, objectClass, CollectionUtil.asSet(permissions));
        }
    }
}
