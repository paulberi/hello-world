package se.metria.markkoll.util.vardering.vpgenerator;

import org.junit.jupiter.api.Test;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;
import se.metria.markkoll.util.dokument.ExcelUtil;
import se.metria.markkoll.util.vardering.vpgeneratorstrategy.ElnatVpGeneratorStrategy;
import se.metria.markkoll.util.vardering.VpGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.metria.markkoll.util.AccountUtil.MARKKOLL_SYSTEM_USER_FRIENDLY_NAME;

class VpGeneratorTest {
    VpGenerator vpGenerator = new VpGenerator<>(new ElnatVpGeneratorStrategy());

    @Test
    void ska_skriva_ut_dokumentets_författare() throws Exception {
        // Given
        var author = "MARKKOLL";

        // When
        var generated = vpGenerator.generateVarderingsprotokoll(new ElnatVarderingsprotokollDto(), null,
            null, author, null);

        // Then
        var wb = ExcelUtil.openWorkbook(generated.getByteArray());
        assertEquals(author, wb.getProperties().getCoreProperties().getCreator());

    }

    @Test
    void ska_skriva_ut_markkoll_som_författare_om_ingen_författare_anges() throws Exception {
        // When
        var generated = vpGenerator.generateVarderingsprotokoll(new ElnatVarderingsprotokollDto(), null,
            null, null, null);

        // Then
        var wb = ExcelUtil.openWorkbook(generated.getByteArray());
        assertEquals(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME , wb.getProperties().getCoreProperties().getCreator());
    }

    @Test
    void ska_skriva_ut_vem_som_senast_ändrade_dokumentet() throws Exception {
        // Given
        var lastModifiedBy = "Gustav II Adolf";

        // When
        var generated = vpGenerator.generateVarderingsprotokoll(new ElnatVarderingsprotokollDto(), null,
            null, null, lastModifiedBy);

        // Then
        var wb = ExcelUtil.openWorkbook(generated.getByteArray());
        assertEquals(lastModifiedBy, wb.getProperties().getCoreProperties().getLastModifiedByUser());
    }

    @Test
    void ska_skriva_markkoll_som_den_som_senast_ändrade_dokumentet_om_inget_anges() throws Exception {
        // When
        var generated = vpGenerator.generateVarderingsprotokoll(new ElnatVarderingsprotokollDto(), null,
            null, null, null);

        // Then
        var wb = ExcelUtil.openWorkbook(generated.getByteArray());
        assertEquals(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME, wb.getProperties().getCoreProperties().getLastModifiedByUser());
    }
}