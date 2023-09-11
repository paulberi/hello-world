package se.metria.markkoll.service.vardering.elnat.bilaga;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HinderAkermarkImportTest {
    HinderAkermarkImport hinderAkermarkImport = new HinderAkermarkImport();

    @Test
    public void ska_importera_värden_från_bilagafil() throws IOException {
        // Given
        var vp = new ElnatVarderingsprotokollDto();
        var bilaga = new ClassPathResource("varderingsprotokoll/bilaga/hinder_akermark/hinder_akermark.xlsx");

        // When
        var vpActual = hinderAkermarkImport.onBilagaAdd(bilaga, vp);

        // Then
        assertEquals(1, vpActual.getHinderAkermark().size());
        assertEquals(1723, vpActual.getHinderAkermark().get(0).getErsattning());
    }

    @Test
    public void ska_kasta_exception_om_bilagan_inte_är_en_giltigt_xlsx() {
        // Given
        var vp = new ElnatVarderingsprotokollDto();
        var bilaga = new ClassPathResource("varderingsprotokoll/bilaga/hinder_akermark/inte_en_bilaga.docx");

        // When
        assertThrows(MarkkollException.class, () -> hinderAkermarkImport.onBilagaAdd(bilaga, vp),
            MarkkollError.BILAGA_ERROR_OPEN_FILE.toString());
    }

    @Test
    public void ska_kasta_exception_om_bilagan_inte_går_att_validera() {
        // Given
        var vp = new ElnatVarderingsprotokollDto();
        var bilaga = new ClassPathResource("varderingsprotokoll/bilaga/hinder_akermark/varderingsprotokoll-2022.xlsx");

        // When
        assertThrows(MarkkollException.class, () -> hinderAkermarkImport.onBilagaAdd(bilaga, vp),
            MarkkollError.BILAGA_ERROR_VALIDATION.toString());
    }

    @Test
    public void ska_kasta_exception_om_bilagan_innehåller_ogiltiga_värden() {
        // Given
        var vp = new ElnatVarderingsprotokollDto();
        var bilaga = new ClassPathResource("varderingsprotokoll/bilaga/hinder_akermark/ogiltigt_varde.xlsx");

        // When
        assertThrows(MarkkollException.class, () -> hinderAkermarkImport.onBilagaAdd(bilaga, vp),
            MarkkollError.BILAGA_ERROR_VALUE.toString());
    }
}