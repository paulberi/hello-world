package se.metria.markkoll.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.openapi.model.RoleTypeDto;
import se.metria.markkoll.openapi.model.UserAndRoleDto;
import se.metria.markkoll.openapi.model.UserInfoDto;
import se.metria.markkoll.service.admin.AdminService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollMVCTest(controllers = AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdminService adminService;

    @Test
    @WithMockUser()
    void så_ska_det_gå_att_lägga_till_en_projektadminroll() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var userId = "userId";

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .put("/api/admin/projekt/{projektId}/user/{userId}/role/admin", projektId, userId)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(adminService).addProjektRole(eq(userId), eq(RoleTypeDto.PROJEKTADMIN), eq(projektId));
    }

    @Test
    @WithMockUser()
    void så_ska_det_gå_att_lägga_till_en_projekthandläggarroll() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var userId = "userId";

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .put("/api/admin/projekt/{projektId}/user/{userId}/role/handlaggare", projektId, userId)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(adminService).addProjektRole(eq(userId), eq(RoleTypeDto.PROJEKTHANDLAGGARE), eq(projektId));
    }

    @Test
    @WithMockUser()
    void så_ska_det_gå_att_ta_bort_en_projektadminroll() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var userId = "userId";

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .delete("/api/admin/projekt/{projektId}/user/{userId}/role/admin", projektId, userId)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(adminService).removeProjektRole(eq(userId), eq(RoleTypeDto.PROJEKTADMIN), eq(projektId));
    }

    @Test
    @WithMockUser()
    void så_ska_det_gå_att_ta_bort_en_projekthandläggarroll() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var userId = "userId";

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .delete("/api/admin/projekt/{projektId}/user/{userId}/role/handlaggare", projektId, userId)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(adminService).removeProjektRole(eq(userId), eq(RoleTypeDto.PROJEKTHANDLAGGARE), eq(projektId));
    }

    @Test
    @WithMockUser()
    void så_ska_det_gå_att_skapa_en_ny_användare() throws Exception {
        // Given
        var kundId = "kundId";
        var userInfo = new UserInfoDto()
            .email("email@email.se")
            .fornamn("förnamn")
            .efternamn("efternamn");

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .post("/api/admin/kund/{kundId}/user", kundId)
                .content(new ObjectMapper().writeValueAsString(userInfo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(adminService).createKundanvandare(eq(kundId), eq(userInfo));
    }

    @Test
    @WithMockUser(roles = "admin")
    void så_ska_det_gå_att_skapa_en_ny_kundadmin() throws Exception {
        // Given
        var kundId = "kundId";
        var userInfo = new UserInfoDto()
            .email("email@email.se")
            .fornamn("förnamn")
            .efternamn("efternamn");

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .post("/api/admin/kund/{kundId}/user/admin", kundId)
                .content(new ObjectMapper().writeValueAsString(userInfo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(adminService).createKundadmin(eq(kundId), eq(userInfo));
    }

    @Test
    @WithMockUser
    void så_ska_det_gå_att_sätta_roller_i_ett_projekt_för_en_uppsättning_med_användare() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var usersAndRoles = Arrays.asList(
            new UserAndRoleDto().userId("user1").roleType(RoleTypeDto.KUNDADMIN),
            new UserAndRoleDto().userId("user1").roleType(RoleTypeDto.PROJEKTADMIN),
            new UserAndRoleDto().userId("user2").roleType(RoleTypeDto.PROJEKTHANDLAGGARE)
        );

        // When
        var result = mvc.perform(
            MockMvcRequestBuilders
                .put("/api/admin/projekt/{projektId}/user/roles", projektId)
                .content(new ObjectMapper().writeValueAsString(usersAndRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(adminService).setProjektUsersRoles(eq(projektId), eq(usersAndRoles));
    }
}