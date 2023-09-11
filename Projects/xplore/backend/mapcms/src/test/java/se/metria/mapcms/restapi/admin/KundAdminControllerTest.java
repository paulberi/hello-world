package se.metria.mapcms.restapi.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.mapcms.annotations.MapcmsMVCTest;
import se.metria.mapcms.openapi.model.KundRspDto;
import se.metria.mapcms.openapi.model.SprakvalDto;
import se.metria.mapcms.openapi.model.SprakvalRspDto;
import se.metria.mapcms.security.mock.WithMockUserFromMarkkollRealm;
import se.metria.mapcms.service.admin.AdminKundService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.mapcms.testdata.TestData.*;
import static se.metria.mapcms.util.ResponseBodyMatchers.responseBody;

@DisplayName("KundAdminController")
@MapcmsMVCTest(controllers = AdminKundController.class)
@WithMockUserFromMarkkollRealm
class KundAdminControllerTest {

    @MockBean
    AdminKundService kundService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ModelMapper modelMapper;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUserFromMarkkollRealm
    void getKundBySlugOrVhtNyckel() throws Exception {

        var kund= kundRspDtoData();

        when(kundService.getByIdOrVhtNyckel(kund.getVhtNyckel())).thenReturn(kund);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kund.getVhtNyckel())
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(kund, KundRspDto.class));

        verify(kundService).getByIdOrVhtNyckel(kund.getVhtNyckel());
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void getLogoForKund() throws Exception {

        var file= filData();
        var kundId= mockUUID(2);

        when(kundService.getLogotyp(eq(kundId))).thenReturn(file);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/logotyp")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(content().bytes(file.getInputStream().readAllBytes()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        
        verify(kundService).getLogotyp(eq(kundId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void listSprakForKund() throws Exception {

        var sprak= sprakValRspData();
        List<SprakvalRspDto> sprakList= new ArrayList<>();
        sprakList.add(sprak);

        var kundId= mockUUID(2);

        when(kundService.listSprak(kundId)).thenReturn(sprakList);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/sprak")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(sprakList, SprakvalRspDto.class));

        verify(kundService).listSprak(kundId);
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void updateSprakForKund() throws Exception {

        var sprak= sprakValRspData();
        List<SprakvalRspDto> sprakvalRspDtoList= new ArrayList<>();
        sprakvalRspDtoList.add(sprak);

        SprakvalDto sprakvalDto=new SprakvalDto();
        sprakvalDto= modelMapper.map(sprak, SprakvalDto.class);
        List< SprakvalDto> sprakList=new ArrayList<>();
        sprakList.add(sprakvalDto);

        var kundId= mockUUID(2);

        when(kundService.updateSprak(kundId, sprakList)).thenReturn(sprakvalRspDtoList);

        final var result= mvc.perform(MockMvcRequestBuilders
                .put("/api/admin/kunder/"+kundId+"/sprak")
                .content(mapper.writeValueAsString(sprakList))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(sprakvalRspDtoList,SprakvalRspDto.class));

        verify(kundService).updateSprak(kundId, sprakList);
    }
}