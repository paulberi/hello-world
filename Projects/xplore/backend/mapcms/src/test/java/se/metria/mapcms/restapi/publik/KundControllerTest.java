package se.metria.mapcms.restapi.publik;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.mapcms.commons.utils.FileNameAwareByteArrayResource;
import se.metria.mapcms.entity.KundEntity;
import se.metria.mapcms.openapi.model.KundRspDto;
import se.metria.mapcms.restapi.publik.KundController;
import se.metria.mapcms.service.publik.PublikKundService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.mapcms.testdata.TestData.*;
import static se.metria.mapcms.util.ResponseBodyMatchers.responseBody;


@DisplayName("KundController")
@WebMvcTest(controllers = KundController.class)
class KundControllerTest {

    @MockBean
    private PublikKundService kundService;

    @Autowired
    private MockMvc mvc;

    @Test
    void sa_ska_det_ga_at_hamta_en_kund_med_slug() throws Exception {

        var kund= testKund();
        when(kundService.getBySlug(kund.getSlug())).thenReturn(kund);

        final var result=mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kund.getSlug())
                .accept(MediaType.APPLICATION_JSON)
                );
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(kund, KundRspDto.class));
        verify(kundService).getBySlug(kund.getSlug());
    }

    @Test
    void sa_ska_det_ga_at_hamta_en_logo_med_slug() throws Exception {

        final KundEntity kund = testKundEntity();
        final FileNameAwareByteArrayResource fileNameAwareByteArrayResource =
                new FileNameAwareByteArrayResource(
                    kund.getLogotyp().getFil(),
                    kund.getLogotyp().getFilnamn()
                    );
        when(kundService.getLogotypBySlug(kund.getSlug())).thenReturn(fileNameAwareByteArrayResource);

        final var result=mvc.perform(MockMvcRequestBuilders
                .get("/api/kunder/"+kund.getSlug()+"/logotyp")
        );
        result
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileNameAwareByteArrayResource.getByteArray()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        verify(kundService).getLogotypBySlug(kund.getSlug());
    }
}