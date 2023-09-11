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
import se.metria.mapcms.entity.FilEntity;
import se.metria.mapcms.openapi.model.FilRspDto;
import se.metria.mapcms.openapi.model.ProjektOversattningDto;
import se.metria.mapcms.openapi.model.ProjektRspDto;
import se.metria.mapcms.security.mock.WithMockUserFromMarkkollRealm;
import se.metria.mapcms.service.admin.AdminProjektService;
import se.metria.mapcms.service.admin.filer.ProjektFilService;

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

@DisplayName("ProjektAdminController")
@MapcmsMVCTest(controllers = AdminProjektController.class)
class ProjektAdminControllerTest {

    @MockBean
    AdminProjektService projectService;

    @MockBean
    ProjektFilService projektFilService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ModelMapper modelMapper;

    ObjectMapper mapper = new ObjectMapper();


    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_create_Oversattning_For_Projekt() throws Exception {

        var projekt= oversattProjekt();
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);

        when(projectService.oversattaProjekt(kundId, projektId, projekt)).thenReturn(projekt);

        final var result=mvc.perform(MockMvcRequestBuilders
                .post("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/oversattningar")
                .content(mapper.writeValueAsString(projekt))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(projekt, ProjektOversattningDto.class));

        verify(projectService).oversattaProjekt(eq(kundId),eq(projektId),eq(projekt));

    }


    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_create_Projekt() throws Exception {
        var kundId=mockUUID(0);
        var projekt= projektdata();
        var projektReqDto=projektReqDtoData();

        when(projectService.createProjektForKund(eq(kundId), eq(projektReqDto))).thenReturn(projekt);

        final var result= mvc.perform((MockMvcRequestBuilders
                .post("/api/admin/kunder/"+kundId+"/projekt"))
                .content(mapper.writeValueAsString(projektReqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(projekt, ProjektRspDto.class));

        verify(projectService).createProjektForKund(eq(kundId), eq(projektReqDto));
    }


    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_delete_Fil_For_Projekt() throws Exception {

        var filId= mockUUID(0);
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);

        final var result =mvc.perform(MockMvcRequestBuilders
                .delete("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/filer/"+filId)
                .with(csrf()));

        result.andExpect(status().isOk());

        verify(projektFilService).deleteProjektFile(eq(kundId), eq(projektId), eq(filId));

    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_delete_Oversattning_For_Projekt() throws Exception {
        var sprakkod= "en";
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);

        final var result =mvc.perform(MockMvcRequestBuilders
                .delete("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/oversattningar/"+sprakkod)
                .with(csrf()));

        result.andExpect(status().isOk());

        verify(projectService).deleteOversattningForKundProjekt(eq(kundId), eq(projektId), eq(sprakkod));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_delete_Projekt() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);

        final var result =mvc.perform(MockMvcRequestBuilders
                .delete("/api/admin/kunder/"+kundId+"/projekt/"+projektId)
                .with(csrf()));

        result.andExpect(status().isOk());

        verify(projectService).deleteProjekt(eq(kundId), eq(projektId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_get_Fil_For_Projekt() throws Exception {

        var file= filRspDtoData();
        var filId = mockUUID(0);
        var projektId= mockUUID(1);
        var kundId= mockUUID(2);
        FilEntity filEntity=modelMapper.map(file, FilEntity.class);

        when(projektFilService.getProjektFilRspById(eq(kundId),eq(projektId),eq(filId))).thenReturn(file);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/filer/"+filId)
                .with(csrf()));
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(file, FilRspDto.class));
        verify(projektFilService).getProjektFilRspById(eq(kundId),eq(projektId),eq(filId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_get_Fil_Innehall_For_Projekt() throws Exception {
        var file= filEntityData();
        var filId = mockUUID(0);
        var projektId= mockUUID(1);
        var kundId= mockUUID(2);

        when(projektFilService.getProjektFilById(eq(kundId),eq(projektId),eq(filId))).thenReturn(file);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/filer/"+filId+"/innehall")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(content().bytes(file.getFil()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        verify(projektFilService).getProjektFilById(eq(kundId),eq(projektId),eq(filId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_get_Oversattning_For_Projekt() throws Exception {

        var projekt= oversattProjekt();
        var sprakkod="sv";
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);

        when(projectService.getOversattningById(eq(kundId), eq(projektId), eq(sprakkod))).thenReturn(projekt);

        final var result=mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/oversattningar/"+sprakkod)
                .with(csrf()));
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(projekt, ProjektOversattningDto.class));

        verify(projectService).getOversattningById(eq(kundId), eq(projektId), eq(sprakkod));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_get_Projekt() throws Exception {

        var projekt=projektdata();
        var sprakkod="sv";
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);

        when(projectService.getProjektForKund(eq(kundId), eq(projektId), eq(sprakkod))).thenReturn(projekt);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/?lang="+sprakkod)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(projekt, ProjektRspDto.class));

        verify(projectService).getProjektForKund(eq(kundId), eq(projektId), eq(sprakkod));
    }


    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_list_Filer_For_Projekt() throws Exception {

        var filRspDtoList=filRspDtoListData();
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);

        when(projektFilService.listProjektFiler(eq(kundId), eq(projektId))).thenReturn(filRspDtoList);

        final var result=mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/filer")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(filRspDtoList, FilRspDto.class));

        verify(projektFilService).listProjektFiler(eq(kundId), eq(projektId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_list_Oversattningar_For_Projekt() throws Exception {

        List<ProjektOversattningDto> oversattningDtoList=new ArrayList<>();
        oversattningDtoList.add(oversattProjekt());
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);

        when(projectService.listOversattningarForKundProjekt(eq(kundId), eq(projektId))).thenReturn(oversattningDtoList);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/oversattningar")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(oversattningDtoList, ProjektOversattningDto.class));

        verify(projectService).listOversattningarForKundProjekt(eq(kundId), eq(projektId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_list_Projekt() throws Exception {

        var projektList=listaAvProjekts();
        var sprakkod="sv";
        var kundId=mockUUID(1);

        when(projectService.listProjektForKund(eq(kundId), eq(sprakkod))).thenReturn(projektList);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/?lang="+sprakkod)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(projektList, ProjektRspDto.class));

        verify(projectService).listProjektForKund(eq(kundId), eq(sprakkod));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_update_Fil_For_Projekt() {


    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_update_Oversattning_For_Projekt() throws Exception {

        var sprakkod="en";
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var projekt= projektTextDtoData();
        var returnProjekt= oversattProjekt();

        when(projectService.updateOversattningForKundProjekt(eq(kundId), eq(projektId), eq(sprakkod), eq(projekt))).thenReturn(returnProjekt);

        final var result=mvc.perform(MockMvcRequestBuilders
                .put("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/oversattningar/"+sprakkod)
                .content(mapper.writeValueAsString(projekt))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(returnProjekt, ProjektOversattningDto.class));

        verify(projectService).updateOversattningForKundProjekt(eq(kundId), eq(projektId), eq(sprakkod), eq(projekt));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void så_ska_det_gå_at_update_Projekt() throws Exception {

        var kundId=mockUUID(0);
        var projektId = mockUUID(2);
        var projekt= projektdata();
        var projektReqDto=projektReqDtoData();

        when(projectService.updateProjektForKund(eq(kundId),eq(projektId), eq(projektReqDto))).thenReturn(projekt);

        final var result= mvc.perform(MockMvcRequestBuilders
                .put("/api/admin/kunder/"+kundId+"/projekt/"+projektId)
                .content(mapper.writeValueAsString(projektReqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(projekt, ProjektRspDto.class));

        verify(projectService).updateProjektForKund(eq(kundId),eq(projektId), eq(projektReqDto));
    }
}