package se.metria.markkoll.util;

import se.metria.markkoll.openapi.model.RoleDto;
import se.metria.markkoll.openapi.model.RoleTypeDto;

import java.util.Arrays;
import java.util.List;

public class RoleUtil {
    public static List<RoleTypeDto> getProjektRoleTypes() {
        return Arrays.asList(RoleTypeDto.PROJEKTHANDLAGGARE, RoleTypeDto.PROJEKTADMIN);
    }

    public static boolean isProjektRole(RoleDto role) {
        return getProjektRoleTypes().contains(role);
    }
}
