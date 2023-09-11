package se.metria.markkoll.util.dokument.forteckninggenerator;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.util.MSE_CompareExcelFiles;

import java.util.Arrays;

class ForteckningGeneratorTest {
    ForteckningGenerator forteckningGenerator = new ForteckningGenerator();

    @Test
    void test() throws Exception {
        // Given
        var template = new ClassPathResource("forteckningGenerator/ForteckningTemplate.xlsx");

        var data = Arrays.asList(
            new ForteckningTemplateData("1234", "Big C", "Caymanöarna"), //Mina pengar är i ett skatteparadis. Försök stoppa mig.
            new ForteckningTemplateData("5678", "Small C", null)
        );

        // When
        var xlsxActual = forteckningGenerator.generate(template.getInputStream(), data);

        // Then
        var xlsxExpect = new ClassPathResource("forteckningGenerator/ForteckningExpect.xlsx");
        MSE_CompareExcelFiles.verifyAllSheets(xlsxExpect.getInputStream().readAllBytes(), xlsxActual);
    }
}