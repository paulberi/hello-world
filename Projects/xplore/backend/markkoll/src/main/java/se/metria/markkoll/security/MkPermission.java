package se.metria.markkoll.security;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

public final class MkPermission extends BasePermission {
    public static final Permission KUND_READ = new MkPermission(1 << 5, 'K');

    protected MkPermission(int mask, char code) {
        super(mask, code);
    }
}
