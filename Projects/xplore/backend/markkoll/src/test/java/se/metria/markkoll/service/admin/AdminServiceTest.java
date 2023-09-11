package se.metria.markkoll.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.metria.markkoll.openapi.model.RoleTypeDto;
import se.metria.markkoll.openapi.model.UserAndRoleDto;
import se.metria.markkoll.openapi.model.UserInfoDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.RoleUtil;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class AdminServiceTest {
    AdminService adminService;

    KundService mockKundService;
    ProjektRepository mockProjektRepository;
    RoleAclService mockRoleAclService;
    UserService mockUserService;

    @BeforeEach
    void beforeEach() {
        mockKundService = mock(KundService.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockRoleAclService = mock(RoleAclService.class);
        mockUserService = mock(UserService.class);

        adminService = new AdminService(mockKundService, mockProjektRepository, mockRoleAclService, mockUserService);
    }

    @ParameterizedTest
    @MethodSource("projektroller")
    void
    så_ska_exception_kastas_om_rolltypen_som_tilldelas_inte_är_en_projektroll(RoleTypeDto roleType, boolean projektroll)
    {
        // Given
        var userId = "Kundanvändare";
        var projektId = mockUUID(0);

        when(mockProjektRepository.existsById(eq(projektId))).thenReturn(true);

        // When
        if (!projektroll) {
            assertThrows(IllegalArgumentException.class, () -> adminService.addProjektRole(userId, roleType, projektId));
            verify(mockRoleAclService, times(0)).addRole(any(), any(), any());
        }
        else {
            assertDoesNotThrow(() -> adminService.addProjektRole(userId, roleType, projektId));
        }
    }

    @Test
    void så_ska_exception_kastas_om_projektet_inte_existerar_när_man_lägger_till_en_roll() {
        // Given
        var userId = "Kundanvändare";
        var roleType = RoleTypeDto.PROJEKTADMIN;
        var projektId = mockUUID(0);

        when(mockProjektRepository.existsById(eq(projektId))).thenReturn(false);

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> adminService.addProjektRole(userId, roleType, projektId));
        verify(mockRoleAclService, times(0)).addRole(any(), any(), any());
    }

    @Test
    void så_ska_en_projektroll_kunna_läggas_till() {
        // Given
        var userId = "Kundanvändare";
        var roleType = RoleTypeDto.PROJEKTADMIN;
        var projektId = mockUUID(0);

        when(mockProjektRepository.existsById(eq(projektId))).thenReturn(true);

        // When
        adminService.addProjektRole(userId, roleType, projektId);

        // Then
        verify(mockUserService).addRole(eq(userId), eq(roleType), eq(projektId));
    }

    @ParameterizedTest
    @MethodSource("projektroller")
    void
    så_ska_exception_kastas_om_rolltypen_som_tas_bort_inte_är_en_projektroll(RoleTypeDto roleType, boolean projektroll)
    {
        // Given
        var userId = "Kundanvändare";
        var projektId = mockUUID(0);

        when(mockProjektRepository.existsById(eq(projektId))).thenReturn(true);

        // When
        if (!projektroll) {
            assertThrows(IllegalArgumentException.class, () -> adminService.removeProjektRole(userId, roleType, projektId));
            verify(mockRoleAclService, times(0)).removeRole(any(), any(), any());
        }
        else {
            assertDoesNotThrow(() -> adminService.removeProjektRole(userId, roleType, projektId));
        }
    }

    @Test
    void så_ska_exception_kastas_om_projektet_inte_existerar_när_man_tar_bort_en_roll() {
        // Given
        var userId = "Kundanvändare";
        var roleType = RoleTypeDto.PROJEKTADMIN;
        var projektId = mockUUID(0);

        when(mockProjektRepository.existsById(eq(projektId))).thenReturn(false);

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> adminService.removeProjektRole(userId, roleType, projektId));
        verify(mockRoleAclService, times(0)).removeRole(any(), any(), any());
    }

    @Test
    void så_ska_en_projektroll_kunna_tas_bort() {
        // Given
        var userId = "Kundanvändare";
        var roleType = RoleTypeDto.PROJEKTADMIN;
        var projektId = mockUUID(0);

        when(mockProjektRepository.existsById(eq(projektId))).thenReturn(true);

        // When
        adminService.removeProjektRole(userId, roleType, projektId);

        // Then
        verify(mockRoleAclService).removeRole(eq(userId), eq(roleType), eq(projektId));
    }

    @Test
    void så_ska_det_gå_att_skapa_en_användare() throws Exception {
        // Given
        var kundId = "kundId";
        var userId = "userId";
        var userInfo = new UserInfoDto().email(userId);

        when(mockKundService.exists(eq(kundId))).thenReturn(true);
        when(mockUserService.create(eq(userInfo), eq(kundId))).thenReturn(userId);

        // When
        adminService.createKundanvandare(kundId, userInfo);

        // Then
        verify(mockUserService).create(eq(userInfo), eq(kundId));
        verify(mockUserService).addRole(eq(userId), eq(RoleTypeDto.KUNDANVANDARE), eq(kundId));
    }

    @Test
    void så_ska_exception_kastas_om_man_försöker_skapa_en_användare_för_en_icke_existerande_kund() {
        // Given
        var kundId = "kundId";
        var userInfo = new UserInfoDto();

        when(mockKundService.exists(eq(kundId))).thenReturn(false);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> adminService.createKundanvandare(kundId, userInfo));
    }

    @Test
    void så_ska_det_gå_att_skapa_en_kundadmin() throws Exception {
        // Given
        var kundId = "kundId";
        var userInfo = new UserInfoDto();
        var userId = "userId";

        when(mockUserService.create(eq(userInfo), eq(kundId))).thenReturn(userId);

        // When
        adminService.createKundadmin(kundId, userInfo);

        // Then
        verify(mockKundService).createIfNotExists(eq(kundId));
        verify(mockUserService).create(eq(userInfo), eq(kundId));
        verify(mockUserService).addRole(eq(userId), eq(RoleTypeDto.KUNDADMIN), eq(kundId));
    }

    @Test
    void så_ska_det_gå_att_sätta_roller_för_en_uppsättning_med_användare() {
        // Given
        var projektId = mockUUID(0);
        var userIds = CollectionUtil.asSet("userId1", "userId2");
        var usersAndRoles = Arrays.asList(
            new UserAndRoleDto().userId("user1").roleType(RoleTypeDto.PROJEKTADMIN),
            new UserAndRoleDto().userId("user2").roleType(RoleTypeDto.PROJEKTHANDLAGGARE)
        );

        when(mockUserService.getProjektUsersIds(eq(projektId))).thenReturn(userIds);

        // When
        adminService.setProjektUsersRoles(projektId, usersAndRoles);

        // Then
        for (var userId: userIds) {
            verify(mockUserService).removeProjektRoles(eq(userId), eq(projektId));
        }

        for (var userAndRole: usersAndRoles) {
            verify(mockUserService).addRole(eq(userAndRole.getUserId()), eq(userAndRole.getRoleType()), eq(projektId));
        }
    }

    private static Stream<Arguments> projektroller() {
        return Arrays.stream(RoleTypeDto.values())
            .map(roleType -> Arguments.of(roleType, RoleUtil.getProjektRoleTypes().contains(roleType)));
    }
}