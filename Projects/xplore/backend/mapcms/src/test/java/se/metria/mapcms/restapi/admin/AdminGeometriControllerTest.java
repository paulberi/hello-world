
package se.metria.mapcms.restapi.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.mapcms.annotations.MapcmsMVCTest;
import se.metria.mapcms.openapi.model.GeometriRspDto;
import se.metria.mapcms.security.mock.WithMockUserFromMarkkollRealm;
import se.metria.mapcms.service.admin.AdminGeometriService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.mapcms.testdata.TestData.*;
import static se.metria.mapcms.util.ResponseBodyMatchers.responseBody;

@DisplayName("GeometriAdminController")
@MapcmsMVCTest(controllers = AdminGeometriController.class)
class AdminGeometriControllerTest {

    @MockBean
    AdminGeometriService geometriService;
    @Autowired
    private MockMvc mvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_skapa_geometri_for_projekt() throws Exception {
        var kundId=mockUUID(1);
        var projektId= mockUUID(2);
        var geometriReqDto=geometriReqDtoData();
        var geometriRspDto = geometriData();

        when(geometriService.createGeometri(eq(kundId), eq(projektId), eq(geometriReqDto))).thenReturn(geometriRspDto);

        final var result= mvc.perform((MockMvcRequestBuilders
                .post("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/geometrier"))
                .content(mapper.writeValueAsString(geometriReqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(geometriRspDto, GeometriRspDto.class));

        verify(geometriService).createGeometri(eq(kundId), eq(projektId), eq(geometriReqDto));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_delete_geometri_for_projekt() throws Exception {
        var kundId=mockUUID(1);
        var projektId= mockUUID(2);
        var geometriId = mockUUID(3);

        final var result= mvc.perform((MockMvcRequestBuilders
                .delete("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/geometrier/"+geometriId))
                .with(csrf()));

        result
                .andExpect(status().is(204));

        verify(geometriService).deleteGeometri(eq(kundId), eq(projektId), eq(geometriId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_hämta_geometri_for_projekt() throws Exception {
        var kundId=mockUUID(1);
        var projektId= mockUUID(2);
        var geometriId = mockUUID(3);
        var geometriRspDto = geometriData();

        when(geometriService.getGeometri(eq(kundId), eq(projektId), eq(geometriId))).thenReturn(geometriRspDto);

        final var result= mvc.perform((MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/geometrier/"+geometriId))
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(geometriRspDto, GeometriRspDto.class));


        verify(geometriService).getGeometri(eq(kundId), eq(projektId), eq(geometriId));

    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_hämta_geometri_list_for_projekt() throws Exception {
        var kundId=mockUUID(1);
        var projektId= mockUUID(2);
        List<GeometriRspDto> geometriRspDtoList = new ArrayList<>(0);

        when(geometriService.listGeometrier(eq(kundId), eq(projektId))).thenReturn(geometriRspDtoList);

        final var result= mvc.perform((MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/geometrier/"))
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(geometriRspDtoList, GeometriRspDto.class));


        verify(geometriService).listGeometrier(eq(kundId), eq(projektId));
    }
}    