package se.metria.mapcms.restapi.publik;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.mapcms.annotations.MapcmsMVCTest;
import se.metria.mapcms.openapi.model.ProjektRspDto;
import se.metria.mapcms.restapi.publik.ProjektController;
import se.metria.mapcms.service.publik.PublikProjektService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.mapcms.testdata.TestData.*;
import static se.metria.mapcms.util.ResponseBodyMatchers.responseBody;

@DisplayName("ProjektController")
@MapcmsMVCTest(controllers = ProjektController.class)
class ProjektControllerTest {

    @MockBean
    PublikProjektService projektService;

    @Autowired
    private MockMvc mvc;

    /**
     * **/
    @Test
    void så_det_gå_att_hämta_fil_innehåll_för_Projekt() throws Exception {

        var file= filData();
        var filId = mockUUID(0);
        var projektId= mockUUID(1);
        var kundId= mockUUID(2);

        when(projektService.getFilForProjekt(eq(kundId),eq(projektId),eq(filId))).thenReturn(file);

        final var result= mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kundId+"/projekt/"+projektId+"/filer/"+filId+"/innehall")
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(content().bytes(file.getInputStream().readAllBytes()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        verify(projektService).getFilForProjekt(eq(kundId),eq(projektId),eq(filId));
    }

    @Test
    void så_det_gå_att_hämtag_Projekt_med_Id() throws Exception {

        var projekt= projektdata();
        var projektIdOrSlug= mockUUID(0);
        var kundId= mockUUID(1);
        String lang="sv";

        when(projektService.getProjektWithId(eq(kundId), eq(projektIdOrSlug), eq(lang))).thenReturn(projekt);

        final var result =mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kundId+"/projekt/"+projektIdOrSlug+"/?lang="+lang)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(projekt, ProjektRspDto.class));
        verify(projektService).getProjektWithId(eq(kundId), eq(projektIdOrSlug), eq(lang));
    }

    @Test
    void så_det_gå_att_list_Projekt_för_en_kund() throws Exception {

        var projektList=listaAvProjekts();
        var kundId= mockUUID(0);
        String lang="sv";

        when(projektService.listPublikaProjektForKund(eq(kundId), eq(lang))).thenReturn(projektList);

        final var result =mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kundId+"/projekt/?lang="+lang)
                .with(csrf()));

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(projektList, ProjektRspDto.class));
        verify(projektService).listPublikaProjektForKund(eq(kundId), eq(lang));
    }
}