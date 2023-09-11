package se.metria.markkoll.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.openapi.model.MarkkollUserDto;
import se.metria.markkoll.openapi.model.UserInfoDto;
import se.metria.markkoll.service.admin.UserService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.util.ResponseBodyMatchers.responseBody;

@MarkkollMVCTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser
    void så_ska_det_gå_att_ta_bort_en_användare() throws Exception {
        // Given
        var userId = "userId";

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .delete("/api/user/{userId}", userId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(userService).delete(eq(userId));
    }

    @Test
    @WithMockUser
    void så_ska_det_gå_att_hämta_en_användare() throws Exception {
        // Given
        var userId = "userId";
        var markkollUser = new MarkkollUserDto();

        when(userService.get(eq(userId))).thenReturn(markkollUser);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/user/{userId}", userId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(markkollUser, markkollUser.getClass()));
    }

    @Test
    @WithMockUser
    void så_ska_det_gå_att_uppdatera_användarinformation() throws Exception {
        // Given
        var userId = "userId";
        var markkollUser = new MarkkollUserDto();
        var userInfo = new UserInfoDto();

        when(userService.updateUserInfo(eq(userId), eq(userInfo))).thenReturn(markkollUser);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .patch("/api/user/{userId}", userId)
            .content(new ObjectMapper().writeValueAsString(userInfo))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(markkollUser, markkollUser.getClass()));
    }
}