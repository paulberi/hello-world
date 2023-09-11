package se.metria.xplore.kundconfig.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.xplore.kundconfig.openapi.model.PermissionsDto;
import se.metria.xplore.kundconfig.openapi.model.ProduktDto;
import se.metria.xplore.kundconfig.openapi.model.XpUserDto;
import se.metria.xplore.kundconfig.security.MockWebSecurityConfig;
import se.metria.xplore.kundconfig.security.ResourceServerConfig;
import se.metria.xplore.kundconfig.security.WithMockUserAdmin;
import se.metria.xplore.kundconfig.service.PermissionsService;
import se.metria.xplore.kundconfig.service.UserService;
import se.metria.xplore.kundconfig.util.CollectionUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.xplore.kundconfig.common.ResponseBodyMatchers.responseBody;

@WebMvcTest(
        controllers = UserController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MockWebSecurityConfig.class}),
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ResourceServerConfig.class}))
@DisplayName("Givet UserController")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermissionsService permissionsService;

    @MockBean
    private UserService userService;

    final String baseUrl = "/api/user";

    @Test
    @WithMockUserAdmin
    void så_ska_det_gå_att_hämta_användarbehörigheter() throws Exception {
        // Given
        var user = "chka";
        var produkt = ProduktDto.MARKKOLL;
        var permissions = CollectionUtil.objectsAsList(
                new PermissionsDto(),
                new PermissionsDto()
        );

        when(permissionsService.getPermissions(any(), any())).thenReturn(permissions);

        // When
        var result = mockMvc.perform(MockMvcRequestBuilders
                .get(baseUrl + "/" + user + "/permissions")
                .queryParam("produkt", produkt.toString())
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(permissions, PermissionsDto.class));

        verify(permissionsService).getPermissions(eq(user), eq(produkt));
    }

    @Test
    @WithMockUserAdmin
    void så_ska_det_gå_att_hämta_en_användare() throws Exception {
        // Given
        var mockUser = "emed";
        var user = new XpUserDto();

        when(userService.getUser(any())).thenReturn(user);

        // When
        var result = mockMvc.perform(MockMvcRequestBuilders
        .get(baseUrl + "/" + mockUser));

        // Then
        result.andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(user, XpUserDto.class));

        verify(userService).getUser(eq(mockUser));

    }

}
