package se.metria.markkoll.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.openapi.model.DokumentInfoDto;
import se.metria.markkoll.openapi.model.DokumentTypDto;
import se.metria.markkoll.openapi.model.DokumentmallDto;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.dokument.DokumentmallService;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@DisplayName("Givet DokumentmallController")
@MarkkollMVCTest(controllers = DokumentmallController.class)
@WithMockUser
public class DokumentmallControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    DokumentmallService dokumentmallService;

    @MockBean
    DokumentGeneratorService dokumentGeneratorService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void så_ska_det_gå_att_skapa_en_dokumentmall() throws Exception {
        // Given
        var info = new DokumentInfoDto()
            .dokumenttyp(DokumentTypDto.MARKUPPLATELSEAVTAL)
            .selected(true)
            .namn("namn");
        var dokumentmall = new DokumentmallDto();

        var kundId = "kundId";
        var doc = new MockMultipartFile("file", "file.docx", "application/octet-stream", "dokument".getBytes());
        var infoMultipart = new MockMultipartFile("dokumentInfo", "dokumentInfo", "application/json",
            mapper.writeValueAsString(info).getBytes());

        when(dokumentmallService.create(eq(info), eq(kundId), any())).thenReturn(dokumentmall);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
            .multipart("/api/kund/{kundId}/dokumentmall", kundId)
            .file(doc)
            .file(infoMultipart)
            .with(csrf())
        );

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(dokumentmall)));

        verify(dokumentmallService).create(eq(info), eq(kundId), any());
    }

    @Test
    void så_ska_felmeddelande_fås_tillbaka_om_det_inte_går_att_skapa_en_dokumentmall() throws Exception {
        // Given
        var info = new DokumentInfoDto()
            .dokumenttyp(DokumentTypDto.MARKUPPLATELSEAVTAL)
            .selected(true)
            .namn("namn");

        var kundId = "kundId";
        var doc = new MockMultipartFile("file", "file.docx", "application/octet-stream", "dokument".getBytes());
        var infoMultipart = new MockMultipartFile("dokumentInfo", "dokumentInfo", "application/json",
            mapper.writeValueAsString(info).getBytes());

        when(dokumentmallService.create(eq(info), eq(kundId), any())).thenThrow(new IOException());

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
            .multipart("/api/kund/{kundId}/dokumentmall", kundId)
            .file(doc)
            .file(infoMultipart)
            .with(csrf())
        );

        // Then
        result.andExpect(status().is(400));

        verify(dokumentmallService).create(eq(info), eq(kundId), any());
    }

    @Test
    void så_ska_det_gå_att_ta_bort_en_dokumentmall() throws Exception {
        // Given
        var kundId = "kundId";
        var dokumentmallId = mockUUID(0);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
            .delete("/api/kund/{kundId}/dokumentmall/{dokumentmallId}", kundId, dokumentmallId)
            .with(csrf())
        );

        // Then
        result.andExpect(status().isOk());

        verify(dokumentmallService).delete(eq(dokumentmallId));
    }

    @Test
    void så_ska_det_gå_att_hämta_en_lista_med_dokumentmallsinformation() throws Exception {
        // Given
        var kundId = "kundId";
        var dokumentmallar = Arrays.asList(new DokumentmallDto(), new DokumentmallDto());

        when(dokumentmallService.getKundDokumentmallar(eq(kundId))).thenReturn(dokumentmallar);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/kund/{kundId}/dokumentmall", kundId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(dokumentmallar)))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dokumentmallService).getKundDokumentmallar(eq(kundId));
    }

    @Test
    void så_ska_det_gå_att_hämta_innehållet_i_ett_dokument() throws Exception {
        // Given
        var kundId = "kundId";
        var dokumentId = mockUUID(1);
        var docProcessed = new ByteArrayResource("behandlatdokument".getBytes());

        when(dokumentmallService.getFileData(any())).thenReturn(docProcessed);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/kund/{kundId}/dokumentmall/{dokumentmallId}/data", kundId, dokumentId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().bytes(docProcessed.getByteArray()))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        verify(dokumentmallService).getFileData(eq(dokumentId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_behandla_ett_dokument() throws Exception {
        // Given
        var kundId = "kundId";
        var doc = new MockMultipartFile("file", "file.docx", "application/octet-stream", "dokument".getBytes());
        var docProcessed = new ByteArrayResource("behandlatdokument".getBytes());

        when(dokumentGeneratorService.addDocProperties(any())).thenReturn(docProcessed);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
            .multipart("/api/kund/{kundId}/dokumentmall/prepare", kundId)
            .file(doc)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().bytes(docProcessed.getByteArray()))
            .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        verify(dokumentGeneratorService).addDocProperties(argThat(new ResourceContentMatcher(doc.getResource())));
    }

    @Test
    void så_ska_det_gå_att_uppdatera_dokumentmallsinformation() throws Exception {
        // Given
        var kundId = "kundId";
        var dokumentmallId = mockUUID(0);
        var dokumentmall = new DokumentmallDto()
            .id(mockUUID(1))
            .namn("namn")
            .kundId("kundId2")
            .dokumenttyp(DokumentTypDto.MARKUPPLATELSEAVTAL)
            .selected(true);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
            .put("/api/kund/{kundId}/dokumentmall/{dokumentmallId}", kundId, dokumentmallId)
            .content(mapper.writeValueAsString(dokumentmall))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(dokumentmall)));

        verify(dokumentmallService).update(eq(dokumentmallId), eq(dokumentmall));
    }

    private class ResourceContentMatcher implements ArgumentMatcher<Resource> {
        private Resource left;

        public ResourceContentMatcher(Resource left) {
            this.left = left;
        }

        @Override
        public boolean matches(Resource right) {
            try {
                return Arrays.equals(left.getInputStream().readAllBytes(), right.getInputStream().readAllBytes());
            } catch (IOException e) {
                return false;
            }
        }
    }
}
