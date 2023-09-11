package se.metria.markkoll.service.vardering.elnat.bilaga;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class ElnatBilagaImportServiceTest {

    ElnatBilagaImportService elnatBilagaImportService;

    ElnatBilagaService mockElnatBilagaService;
    ElnatVarderingsprotokollService mockElnatVarderingsprotokollService;
    HinderAkermarkImport hinderAkermarkImport;

    @BeforeEach
    public void beforeEach() {
        mockElnatBilagaService = mock(ElnatBilagaService.class);
        mockElnatVarderingsprotokollService = mock(ElnatVarderingsprotokollService.class);
        hinderAkermarkImport = new HinderAkermarkImport();

        elnatBilagaImportService = new ElnatBilagaImportService(mockElnatBilagaService,
            mockElnatVarderingsprotokollService, hinderAkermarkImport);
    }

    @Test
    void addBilaga() throws IOException {
        // Given
        var vpId = mockUUID(0);
        var vp = new ElnatVarderingsprotokollDto();
        var bilagatyp = BilagaTypDto.AKERNORM_74;

        var resource = new ClassPathResource("varderingsprotokoll/bilaga/hinder_akermark/hinder_akermark.xlsx");
        var mockfile = new MockMultipartFile("file", "file.docx", "application/octet-stream",
            resource.getInputStream().readAllBytes());

        var vpExpect = new ElnatVarderingsprotokollDto()
            .addHinderAkermarkItem(new ElnatHinderAkermarkDto().ersattning(1723));
        var bilagaExpect = new ElnatBilagaDto();

        when(mockElnatVarderingsprotokollService.get(eq(vpId))).thenReturn(vp);
        when(mockElnatVarderingsprotokollService.addBilaga(eq(mockfile), eq(bilagatyp), eq(vpId)))
            .thenReturn(bilagaExpect);

        // When
        var bilaga = elnatBilagaImportService.addBilaga(mockfile, bilagatyp, vpId);

        // Then
        verify(mockElnatVarderingsprotokollService).addBilaga(eq(mockfile), eq(BilagaTypDto.AKERNORM_74), eq(vpId));
        verify(mockElnatVarderingsprotokollService).update(eq(vpExpect));

        assertEquals(bilagaExpect, bilaga);
    }

    @Test
    void removeBilaga() {
        // Given
        var bilagaId = mockUUID(0);
        var vp = new ElnatVarderingsprotokollDto()
            .addHinderAkermarkItem(new ElnatHinderAkermarkDto().ersattning(1723));
        var bilaga = new ElnatBilagaDto()
            .id(bilagaId)
            .typ(ElnatBilagaTypDto.AKERNORM_74);

        var vpExpect = new ElnatVarderingsprotokollDto().hinderAkermark(new ArrayList<>());

        when(mockElnatVarderingsprotokollService.getWithBilagaid(eq(bilagaId))).thenReturn(vp);
        when(mockElnatBilagaService.get(eq(bilagaId))).thenReturn(bilaga);

        // When
        elnatBilagaImportService.removeBilaga(bilagaId);

        // Then
        verify(mockElnatVarderingsprotokollService).removeBilaga(eq(bilagaId));
        verify(mockElnatVarderingsprotokollService).update(eq(vpExpect));
    }
}