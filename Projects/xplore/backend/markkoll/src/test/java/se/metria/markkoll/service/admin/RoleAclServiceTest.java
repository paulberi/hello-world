package se.metria.markkoll.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.acls.domain.PrincipalSid;
import se.metria.markkoll.openapi.model.RoleTypeDto;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RoleAclServiceTest {
    RoleAclService roleAclService;

    AclService mockAclService;
    RoleAclEntriesService mockRoleAclEntriesService;

    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockRoleAclEntriesService = mock(RoleAclEntriesService.class);

        roleAclService = new RoleAclService(mockAclService, mockRoleAclEntriesService);
    }

    @Test
    void så_ska_det_gå_att_lägga_till_en_roll() {
        // Given
        var userId = "userId";
        var roleType = RoleTypeDto.KUNDADMIN;
        var objectId = "objectId";
        var rolePermissions = new HashSet<AclEntry>();

        when(mockRoleAclEntriesService.getRoleAclEntries(eq(roleType), eq(objectId))).thenReturn(rolePermissions);

        // When
        roleAclService.addRole(userId, roleType, objectId);

        // Then
        verify(mockAclService).addAclEntries(eq(new PrincipalSid(userId)), eq(rolePermissions));
    }

    @Test
    void så_ska_det_gå_att_ta_bort_en__roll() {
        // Given
        var userId = "userId";
        var roleType = RoleTypeDto.KUNDADMIN;
        var objectId = "objectId";
        var rolePermissions = new HashSet<AclEntry>();

        when(mockRoleAclEntriesService.getRoleAclEntries(eq(roleType), eq(objectId))).thenReturn(rolePermissions);

        // When
        roleAclService.removeRole(userId, roleType, objectId);

        // Then
        verify(mockAclService).removeAclEntries(eq(new PrincipalSid(userId)), eq(rolePermissions));
    }
}