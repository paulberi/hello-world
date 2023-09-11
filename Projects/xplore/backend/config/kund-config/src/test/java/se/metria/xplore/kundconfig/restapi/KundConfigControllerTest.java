package se.metria.xplore.kundconfig.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.xplore.kundconfig.openapi.model.FastighetsokAuthDto;
import se.metria.xplore.kundconfig.openapi.model.KundPageDto;
import se.metria.xplore.kundconfig.openapi.model.MetriaMapsAuthDto;
import se.metria.xplore.kundconfig.security.MockWebSecurityConfig;
import se.metria.xplore.kundconfig.security.ResourceServerConfig;
import se.metria.xplore.kundconfig.security.WithMockUserAdmin;
import se.metria.xplore.kundconfig.security.WithMockUserGlobalAdmin;
import se.metria.xplore.kundconfig.service.AbonnemangService;
import se.metria.xplore.kundconfig.service.AuthService;
import se.metria.xplore.kundconfig.service.KundService;
import se.metria.xplore.kundconfig.service.UserService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.xplore.kundconfig.common.ResponseBodyMatchers.responseBody;

@WebMvcTest(
        controllers = KundConfigController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MockWebSecurityConfig.class}),
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ResourceServerConfig.class}))
@DisplayName("Givet KundConfigController")
public class KundConfigControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KundService kundService;

    @MockBean
    private AbonnemangService abonnemangService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    final String baseUrl = "/api/kund/";

    @Test
    @WithMockUserAdmin
    void så_ska_det_gå_att_hämta_en_sida_med_kunder() throws Exception {
        // Given
        Integer page = 1;
        Integer size = 2;
        var kundPage = new KundPageDto();
        when(kundService.getKundPage(any(), any())).thenReturn(kundPage);

        // When
        var result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(baseUrl + "page")
                        .param("page", page.toString())
                        .param("size", size.toString())
                        .with(csrf())
        );

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(kundPage, kundPage.getClass()));

        verify(kundService).getKundPage(eq(page), eq(size));
    }

    @Test
    @WithMockUserAdmin
    void så_ska_det_gå_att_hämta_autentiseringsuppgifter_för_fsök() throws Exception {
        // Given
        var kundId = "kundId";
        var authDto = new FastighetsokAuthDto().kundmarke("kundmarke");

        when(authService.getFastighetsokAuth(any())).thenReturn(authDto);

        // When
        var result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(baseUrl + kundId + "/fastighetsok" )
                        .with(csrf())
        );

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(authDto, authDto.getClass()));

        verify(authService).getFastighetsokAuth(eq(kundId));
    }

    @Test
    @WithMockUserAdmin
    void så_ska_det_gå_att_hämta_autentiseringsuppgifter_för_metria_maps() throws Exception {
        // Given
        var kundId = "kundId";
        var authDto = new MetriaMapsAuthDto().password("password");

        when(authService.getMetriaMapsAuth(any())).thenReturn(authDto);

        // When
        var result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(baseUrl + kundId + "/metria-maps" )
                        .with(csrf())
        );

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(authDto, authDto.getClass()));

        verify(authService).getMetriaMapsAuth(eq(kundId));
    }

    @Test
    @WithMockUserAdmin
    void så_ska_det_gå_att_redigera_autentiseringsuppgifter_för_metria_maps() throws Exception {
        // Given
        var kundId = "kundId";
        var authDto = new MetriaMapsAuthDto()
            .password("password")
            .id(UUID.randomUUID())
            .kundId(kundId)
            .username("username");

        // When
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .put(baseUrl + kundId + "/metria-maps" )
                .content(objectMapper.writeValueAsString(authDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        // Then
        result.andExpect(status().isOk());
        verify(authService).editMetriaMapsAuth(eq(authDto));
    }

    @Test
    @WithMockUserAdmin
    void så_ska_det_gå_att_redigera_autentiseringsuppgifter_för_fastighetsök() throws Exception {
        // Given
        var kundId = "kundId";
        var authDto = new FastighetsokAuthDto()
            .password("password")
            .id(UUID.randomUUID())
            .kundId(kundId)
            .username("username")
            .kundmarke("kundmärke");

        // When
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .put(baseUrl + kundId + "/fastighetsok" )
                .content(objectMapper.writeValueAsString(authDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        // Then
        result.andExpect(status().isOk());
        verify(authService).editFastighetsokAuth(eq(authDto));
    }

    @Test
    @WithMockUserGlobalAdmin
    void så_ska_det_gå_att_återskapa_geofence_regler_för_alla_kunder() throws Exception {
        // When
        var result = mockMvc.perform(
                MockMvcRequestBuilders
                        .put(baseUrl + "/geoserver/reset-rules" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // Then
        result.andExpect(status().isOk());
        verify(kundService).resetGeofenceRules();
    }
}
