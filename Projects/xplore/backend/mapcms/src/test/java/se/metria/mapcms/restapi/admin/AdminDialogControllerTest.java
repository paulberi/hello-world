package se.metria.mapcms.restapi.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.annotations.MapcmsMVCTest;
import se.metria.mapcms.openapi.model.*;
import se.metria.mapcms.security.mock.WithMockUserFromMarkkollRealm;
import se.metria.mapcms.service.admin.AdminDialogService;
import se.metria.mapcms.service.admin.AdminMeddelandeService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.mapcms.testdata.TestData.*;
import static se.metria.mapcms.util.ResponseBodyMatchers.responseBody;


@DisplayName("AdminDialogController")
@MapcmsMVCTest(controllers = AdminDialogController.class)
class AdminDialogControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdminDialogService adminDialogService;

    @MockBean
    private AdminMeddelandeService adminMeddelandeService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUserFromMarkkollRealm
    void createDialogWithMessage() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogRspDto=dialogRspMetadata();
        List<MultipartFile> filer= Arrays.asList(createMultipartFile());
        DialogReqAdminDto dialog=new DialogReqAdminDto();
        MeddelandeReqAdminDto meddelande=new MeddelandeReqAdminDto();

        when(adminDialogService.startDialog(eq(kundId), eq(projektId), any(), any(), eq(filer))).thenReturn(dialogRspDto);

        final var result= mvc.perform(MockMvcRequestBuilders
                .multipart("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger")
                .file((MockMultipartFile) filer.get(0))
                .param("dialog", mapper.writeValueAsString(dialog))
                .param("meddelande", mapper.writeValueAsString(meddelande))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(csrf()));

        result
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(dialogRspDto, DialogRspDto.class));

        verify(adminDialogService).startDialog(eq(kundId), eq(projektId), any(), any(), eq(filer));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void createMeddelandeForDialog() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var meddelandeRsp= meddelandeRspDtoMetadata();
        MeddelandeReqAdminDto meddelande=new MeddelandeReqAdminDto();
        List<MultipartFile> filer= Arrays.asList(createMultipartFile());

        when(adminMeddelandeService.createMeddelande(eq(kundId), eq(projektId),eq(dialogId), any(), eq(filer))).thenReturn(meddelandeRsp);

        final var result=mvc.perform(MockMvcRequestBuilders
                .multipart("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId+"/meddelanden")
                .file((MockMultipartFile) filer.get(0))
                .param("meddelande", mapper.writeValueAsString(meddelande))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(csrf()));

        result
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(meddelandeRsp, MeddelandeRspDto.class));

        verify(adminMeddelandeService).createMeddelande(eq(kundId), eq(projektId),eq(dialogId), any(), eq(filer));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void deleteDialog() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);

        final var result=mvc.perform(MockMvcRequestBuilders
                .delete("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId)
                .with(csrf()));

        result
                .andExpect(status().isOk());
        verify(adminDialogService).deleteDialogWithId(kundId, projektId, dialogId);
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void deleteMeddelandeForDialog() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var meddelandeId= mockUUID(4);

        final var result=mvc.perform(MockMvcRequestBuilders
                .delete("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId+"/meddelanden/"+meddelandeId)
                .with(csrf()));

        result
                .andExpect(status().isOk());
        verify(adminMeddelandeService).deleteMeddelandeById(kundId, projektId, dialogId, meddelandeId);
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void getDialog() throws Exception {
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var dialog= dialogRspMetadata();

        when(adminDialogService.getDialogWithId(eq(kundId), eq(projektId), eq(dialogId))).thenReturn(dialog);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(dialog, DialogRspDto.class));

        verify(adminDialogService).getDialogWithId(eq(kundId), eq(projektId), eq(dialogId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void getMeddelandeForDialog() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var meddelandeId= mockUUID(4);
        var meddelandeRsp= meddelandeRspDtoMetadata();

        when(adminMeddelandeService.getMeddelandeById(eq(kundId), eq(projektId), eq(dialogId), eq(meddelandeId))).thenReturn(meddelandeRsp);

        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId+"/meddelanden/"+meddelandeId)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(meddelandeRsp, MeddelandeRspDto.class));

        verify(adminMeddelandeService).getMeddelandeById(eq(kundId), eq(projektId), eq(dialogId), eq(meddelandeId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void listDialoger() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogs= Arrays.asList(dialogRspMetadata());

        when(adminDialogService.listDialogForProjekt(eq(kundId), eq(projektId))).thenReturn(dialogs);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(dialogs, DialogRspDto.class));

        verify(adminDialogService).listDialogForProjekt(eq(kundId), eq(projektId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void listMeddelandenForDialog() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var meddelanden= Arrays.asList(meddelandeRspDtoMetadata());

        when(adminMeddelandeService.listMeddelanden(eq(kundId), eq(projektId), eq(dialogId))).thenReturn(meddelanden);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId+"/meddelanden")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(meddelanden, MeddelandeRspDto.class));

        verify(adminMeddelandeService).listMeddelanden(eq(kundId), eq(projektId), eq(dialogId));
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void updateDialog() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var dialog= dialogRspMetadata();
        DialogReqAdminDto dialogAdminReq= new DialogReqAdminDto();

        when(adminDialogService.updateDialogWithId(eq(kundId), eq(projektId), eq(dialogId), any())).thenReturn(dialog);

        final var result= mvc.perform(MockMvcRequestBuilders
                .put("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId)
                .content(mapper.writeValueAsString(dialogAdminReq))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(dialog, DialogRspDto.class));

        verify(adminDialogService).updateDialogWithId(eq(kundId), eq(projektId), eq(dialogId), any());
    }

    @Test
    @WithMockUserFromMarkkollRealm
    void updateMeddelandeForDialog() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var meddelandeId= mockUUID(4);
        var meddelande= meddelandeRspDtoMetadata();
        MeddelandeReqAdminDto meddelandeReqAdminDto=new MeddelandeReqAdminDto();

        when(adminMeddelandeService.updateMeddelande(eq(kundId), eq(projektId), eq(dialogId), eq(meddelandeId), any())).thenReturn(meddelande);

        final var result = mvc.perform(MockMvcRequestBuilders
                .put("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId+"/meddelanden/"+meddelandeId)
                .content(mapper.writeValueAsString(meddelandeReqAdminDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(meddelande, MeddelandeRspDto.class));

        verify(adminMeddelandeService).updateMeddelande(eq(kundId), eq(projektId), eq(dialogId), eq(meddelandeId), any());
    }

    @Test
    void det_ska_inte_gå_att_hämta_dialog_utan_role() throws Exception {
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var dialog= dialogRspMetadata();

        when(adminDialogService.getDialogWithId(eq(kundId), eq(projektId), eq(dialogId))).thenReturn(dialog);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/admin/kunder/"+kundId+"/projekt/"+projektId+"/dialoger/"+dialogId)
                .with(csrf()));

        result
                .andExpect(status().isForbidden());
    }
}