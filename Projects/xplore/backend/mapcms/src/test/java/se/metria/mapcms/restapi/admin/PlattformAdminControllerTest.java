package se.metria.mapcms.restapi.admin;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.mapcms.annotations.MapcmsMVCTest;
import se.metria.mapcms.openapi.model.SprakDto;
import se.metria.mapcms.security.mock.WithMockUserFromMarkkollRealm;
import se.metria.mapcms.service.admin.AdminPlattformService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.mapcms.testdata.TestData.sprakData;
import static se.metria.mapcms.util.ResponseBodyMatchers.responseBody;

@DisplayName("PlattformAdminController")
@MapcmsMVCTest(controllers = AdminPlattformController.class)
class PlattformAdminControllerTest {

    @MockBean
    AdminPlattformService plattformService;

    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_hämta_en_lista_med_tillgängliga_språk_i_platform() throws Exception {


        var tillgangliaSprak= sprakData();

        when(plattformService.listSprak()).thenReturn(tillgangliaSprak);

        final var result=mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/sprak")
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(tillgangliaSprak, SprakDto.class));
        verify(plattformService).listSprak();

    }
}