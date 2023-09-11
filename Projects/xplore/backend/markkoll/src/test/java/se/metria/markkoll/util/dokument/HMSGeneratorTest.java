package se.metria.markkoll.util.dokument;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.util.MSE_CompareExcelFiles;
import se.metria.markkoll.util.dokument.hms_generator.Entry;
import se.metria.markkoll.util.dokument.hms_generator.HMSGenerator;

import java.util.Arrays;

@MarkkollServiceTest
@DisplayName("Givet HaglofHMS")
public class HMSGeneratorTest {
    HMSGenerator HMSGenerator = new HMSGenerator();

    @Test
    void så_ska_det_gå_att_generera_ett_dokument() throws Exception {
        // Given
        var xlsxExpect = new ClassPathResource("hms/hmsGenerator.xlsx").getInputStream();

        var entries = Arrays.asList(
            Entry.builder()
                .fastighet("fastighet")
                .fastighetsnummer("fnr1")
                .sokId("111")
                .andel("1/3")
                .namn("namn")
                .gatuadress("adress")
                .postnr("567")
                .postort("ort1")
                .personnummer("1")
                .lan("Län1")
                .kommun("Kommun1")
                .co("Co1")
                .telefonArbete("telearb1")
                .ePost("email1")
                .bankkonto("Bankkonto1")
                .mottagarreferens("mottagare1")
                .build(),
            Entry.builder()
                .fastighet("samma fastighet")
                .fastighetsnummer("fnr2")
                .sokId("222")
                .andel("2/3")
                .namn("men annat namn")
                .gatuadress("och adress")
                .postnr("000")
                .postort("ort2")
                .personnummer("2")
                .lan("Län2")
                .kommun("Kommun2")
                .co("Co2")
                .telefonArbete("telearb2")
                .ePost("email2")
                .bankkonto("Bankkonto2")
                .mottagarreferens("mottagare2")
                .build()
        );

        // When
        var xlsxActual = HMSGenerator.generate(entries);

        // Then
        MSE_CompareExcelFiles.verifyAllSheets(xlsxExpect.readAllBytes(), xlsxActual);
    }
}
