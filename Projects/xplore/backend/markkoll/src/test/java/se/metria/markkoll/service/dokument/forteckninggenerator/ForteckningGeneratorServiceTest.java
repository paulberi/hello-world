package se.metria.markkoll.service.dokument.forteckninggenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.openapi.model.FastighetsfilterDto;
import se.metria.markkoll.openapi.model.FilterAndTemplateDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.dokument.DokumentmallRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.util.MSE_CompareExcelFiles;
import se.metria.markkoll.util.dokument.forteckninggenerator.ForteckningGenerator;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
public class ForteckningGeneratorServiceTest {
    @InjectMocks
    ForteckningGeneratorService forteckningGeneratorService;

    @Mock
    AvtalRepository avtalRepository;

    @Mock
    AvtalspartRepository avtalspartRepository;

    @Mock
    DokumentmallRepository dokumentmallRepository;

    @Mock
    ProjektRepository projektRepository;

    @Spy
    ModelMapper modelMapper = new ModelMapper();

    @Spy
    ForteckningGenerator forteckningGenerator = new ForteckningGenerator();

    @Test
    public void generate() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var filterAndTemplate = new FilterAndTemplateDto()
            .filter(new FastighetsfilterDto())
            .template(mockUUID(1));

        var template = new ClassPathResource("forteckningGenerator/ForteckningTemplate.xlsx");
        var data = Arrays.asList(
            forteckningGeneratorData("<<1234>>", "Big C", "Caymanöarna"), //Mina pengar är i ett skatteparadis. Försök stoppa mig.
            forteckningGeneratorData("<<5678>>", "Small C", null)
        );

        when(dokumentmallRepository.getDokumentFileData(any())).thenReturn(template.getInputStream().readAllBytes());
        when(avtalspartRepository.getForteckningGeneratorData(any())).thenReturn(data);
        when(projektRepository.getNamn(any())).thenReturn("projektnamn");
        when(dokumentmallRepository.getDokumentmallNamn(any())).thenReturn("mall");

        // When
        var forteckning = forteckningGeneratorService.generate(projektId, filterAndTemplate);

        // Then
        var xlsxExpect = new ClassPathResource("forteckningGenerator/ForteckningExpect.xlsx");
        MSE_CompareExcelFiles.verifyAllSheets(xlsxExpect.getInputStream().readAllBytes(),
            forteckning.getInputStream().readAllBytes());
        assertEquals("projektnamn_mall.xlsx", forteckning.getFilename());
    }

    private ForteckningGeneratorData forteckningGeneratorData(String organisationsnummer, String namn, String bankLand) {
        var data = new ForteckningGeneratorDataImpl();
        data.setMottagareOrganisationsnummer(organisationsnummer);
        data.setMottagareNamn(namn);
        data.setBankLand(bankLand);
        return data;
    }
}