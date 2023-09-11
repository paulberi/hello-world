package se.metria.mapcms.restapi.publik;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.openapi.model.*;
import se.metria.mapcms.security.mock.WithMockUserFromBankIdRealm;
import se.metria.mapcms.security.mock.WithMockUserFromMarkkollRealm;
import se.metria.mapcms.service.publik.PublikDialogService;
import se.metria.mapcms.service.publik.PublikMeddelandeService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.mapcms.testdata.TestData.*;
import static se.metria.mapcms.util.ResponseBodyMatchers.responseBody;

@DisplayName("MinaSidorController")
@WebMvcTest(controllers = MinaSidorController.class)
class MinaSidorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PublikDialogService publikDialogService;

    @MockBean
    private PublikMeddelandeService publikMeddelandeService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUserFromBankIdRealm
    void så_ska_det_gå_att_create_Dialog_With_MessageForUser() throws Exception {

        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogRspDto=dialogRspMetadata();
        List<MultipartFile> filer= Arrays.asList(createMultipartFile());
        DialogReqDto dialog=new DialogReqDto();
        MeddelandeReqDto meddelande=new MeddelandeReqDto();

        when(publikDialogService.startDialog(eq(kundId), eq(projektId), any(), any(), eq(filer))).thenReturn(dialogRspDto);

        final var result= mvc.perform(MockMvcRequestBuilders
                .multipart("/api/kunder/"+kundId+"/projekt/"+projektId+"/mina-sidor/dialoger")
                .file((MockMultipartFile) filer.get(0))
                .param("dialog", mapper.writeValueAsString(dialog))
                .param("meddelande", mapper.writeValueAsString(meddelande))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(csrf()));

        result
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(dialogRspDto, DialogRspDto.class));

        verify(publikDialogService).startDialog(eq(kundId), eq(projektId), any(), any(), eq(filer));
    }

    @Test
    @WithMockUserFromBankIdRealm
    void createMeddelandeInDialogForUser() throws Exception {


        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(6);
        var meddelandeRsp= meddelandeRspDtoMetadata();
        var meddelande=new MeddelandeReqDto();
        List<MultipartFile> filer= Arrays.asList(createMultipartFile());

        when(publikMeddelandeService.createMeddelandeForUser(eq(kundId), eq(projektId),eq(dialogId), any(), eq(filer))).thenReturn(meddelandeRsp);

        final var result=mvc.perform(MockMvcRequestBuilders
                .multipart("/api/kunder/"+kundId+"/projekt/"+projektId+"/mina-sidor/dialoger/"+dialogId)
                .file((MockMultipartFile) filer.get(0))
                .param("meddelande", mapper.writeValueAsString(meddelande))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(csrf()));

        result
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(meddelandeRsp, MeddelandeRspDto.class));

        verify(publikMeddelandeService).createMeddelandeForUser(eq(kundId), eq(projektId),eq(dialogId), any(), eq(filer));
    }

    @Test
    @WithMockUserFromBankIdRealm
    void getDialogForUser() throws Exception {
        var kundId=mockUUID(1);
        var projektId = mockUUID(2);
        var dialogId= mockUUID(0);

        when(publikDialogService.getDialogForUserWithId(eq(kundId), eq(projektId), eq(dialogId))).thenReturn(dialogKomplettMetadata());

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kundId+"/projekt/"+projektId+"/mina-sidor/dialoger/"+dialogId)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(dialogKomplettMetadata(), DialogKomplettDto.class));

        verify(publikDialogService).getDialogForUserWithId(eq(kundId), eq(projektId), eq(dialogId));
    }

    @Test
    @WithMockUserFromBankIdRealm
    void getFilForDialogForUser() throws Exception {

        var file= filEntityData();
        var filId = mockUUID(0);
        var projektId= mockUUID(1);
        var kundId= mockUUID(2);
        var meddelandeId = mockUUID(3);
        var dialogId= mockUUID(4);

        when(publikMeddelandeService.getDialogFilForUserMeddelande(eq(kundId),eq(projektId), eq(dialogId), eq(meddelandeId), eq(filId))).thenReturn(file);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kundId+"/projekt/"+projektId+"/mina-sidor/dialoger/"+dialogId+"/meddelanden/"+meddelandeId+"/filer/"+filId+"/innehall")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(content().bytes(file.getFil()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        verify(publikMeddelandeService).getDialogFilForUserMeddelande(eq(kundId),eq(projektId), eq(dialogId), eq(meddelandeId), eq(filId));

    }

    @Test
    @WithMockUserFromBankIdRealm
    void listDialogerForUser() throws Exception {

        var projektId= mockUUID(1);
        var kundId= mockUUID(2);
        List<DialogRspDto> dialogs=Arrays.asList(dialogRspMetadata());

        when(publikDialogService.listDialogerForUser(eq(kundId), eq(projektId))).thenReturn(dialogs);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kundId+"/projekt/"+projektId+"/mina-sidor/dialoger")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(dialogs, DialogRspDto.class));

        verify(publikDialogService).listDialogerForUser(eq(kundId), eq(projektId));
    }

    @Test
    void det_ska_inte_gå_att_listDialogerForUser_utan_authentication() throws Exception {

        var projektId= mockUUID(1);
        var kundId= mockUUID(2);
        List<DialogRspDto> dialogs=Arrays.asList(dialogRspMetadata());

        when(publikDialogService.listDialogerForUser(eq(kundId), eq(projektId))).thenReturn(dialogs);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kundId+"/projekt/"+projektId+"/mina-sidor/dialoger")
                .with(csrf()));

        result
                .andExpect(status().isUnauthorized());
    }
}