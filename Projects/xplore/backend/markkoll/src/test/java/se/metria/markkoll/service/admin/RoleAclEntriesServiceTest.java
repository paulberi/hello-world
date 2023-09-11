package se.metria.markkoll.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.RoleDto;
import se.metria.markkoll.openapi.model.RoleTypeDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.util.CollectionUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.security.MkPermission.*;

class RoleAclEntriesServiceTest {
    RoleAclEntriesService roleAclEntriesService;

    ProjektRepository mockProjektRepository;

    static final UUID projektId = mockUUID(2);
    static final String kundId = "kundId";

    @BeforeEach
    void beforeEach() {
        mockProjektRepository = mock(ProjektRepository.class);

        roleAclEntriesService = new RoleAclEntriesService(mockProjektRepository);
    }

    @ParameterizedTest
    @MethodSource("rolePermissionsData")
    void rolePermissionstest(Serializable objectId, RoleTypeDto roleTypeDto, Set<AclEntry> objectPermissionsExpect) {
        // Given
        when(mockProjektRepository.getProjektIds(eq(kundId)))
            .thenReturn(CollectionUtil.asSet(mockUUID(0), mockUUID(1)));
        when(mockProjektRepository.getKundId(eq(projektId))).thenReturn(kundId);

        // When
        var objectPermissions = roleAclEntriesService.getRoleAclEntries(roleTypeDto, objectId);

        //Then
        assertEquals(objectPermissionsExpect, objectPermissions);
    }

    @Test
    void userRolePermissionsTest() {
        // Given
        var kundId1 = "kundId1";
        var kundId2 = "kundId2";
        var projektId1 = mockUUID(0);
        var projektId2 = mockUUID(1);
        var projektId3 = mockUUID(2);

        var roles = Arrays.asList(
            new RoleDto().objectId(projektId1.toString()).roleType(RoleTypeDto.PROJEKTADMIN),
            new RoleDto().objectId(projektId2.toString()).roleType(RoleTypeDto.PROJEKTHANDLAGGARE),
            new RoleDto().objectId(kundId1).roleType(RoleTypeDto.KUNDADMIN),
            new RoleDto().objectId(projektId3.toString()).roleType(RoleTypeDto.PROJEKTHANDLAGGARE)
        );

        when(mockProjektRepository.getProjektIds(eq(kundId1))).thenReturn(CollectionUtil.asSet(projektId1, projektId2));
        when(mockProjektRepository.getKundId(eq(projektId1))).thenReturn(kundId1);
        when(mockProjektRepository.getKundId(eq(projektId2))).thenReturn(kundId1);
        when(mockProjektRepository.getKundId(eq(projektId3))).thenReturn(kundId2);

        // When
        var objectPermissions = roleAclEntriesService.getRolesAclEntries(roles);

        // Then
        var entriesExpect = CollectionUtil.asSet(
            AclEntry.of(projektId1, ProjektEntity.class, ADMINISTRATION, CREATE, DELETE, READ, WRITE),
            AclEntry.of(kundId1, KundEntity.class, KUND_READ, ADMINISTRATION, CREATE, DELETE, READ, WRITE),
            AclEntry.of(projektId2, ProjektEntity.class, READ, WRITE, CREATE, DELETE),
            AclEntry.of(projektId3, ProjektEntity.class, READ, WRITE, CREATE, DELETE)
        );

        assertEquals(entriesExpect, objectPermissions);
    }

    private static Stream<Arguments> rolePermissionsData() {
        return Stream.of(
            Arguments.of(
                projektId,
                RoleTypeDto.PROJEKTADMIN,
                CollectionUtil.asSet(
                    AclEntry.of(projektId, ProjektEntity.class, ADMINISTRATION, CREATE, DELETE, READ, WRITE)
                )
            ),
            Arguments.of(
                projektId,
                RoleTypeDto.PROJEKTHANDLAGGARE,
                CollectionUtil.asSet(
                    AclEntry.of(projektId, ProjektEntity.class, READ, WRITE, CREATE, DELETE)
                )
            ),
            Arguments.of(
                kundId,
                RoleTypeDto.KUNDADMIN,
                CollectionUtil.asSet(
                    AclEntry.of(kundId, KundEntity.class, KUND_READ, ADMINISTRATION, CREATE, DELETE, READ, WRITE)
                )
            ),
            Arguments.of(
                kundId,
                RoleTypeDto.KUNDANVANDARE,
                CollectionUtil.asSet(
                    AclEntry.of(kundId, KundEntity.class, KUND_READ, CREATE)
                )
            )
        );
    }
}