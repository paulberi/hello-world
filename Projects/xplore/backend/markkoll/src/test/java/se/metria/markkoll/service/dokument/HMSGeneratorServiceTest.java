package se.metria.markkoll.service.dokument;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.config.ModelMapperConfiguration;
import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.openapi.model.AgartypDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.avtal.HaglofExportView;
import se.metria.markkoll.repository.avtal.MottagarreferensView;
import se.metria.markkoll.util.MSE_CompareExcelFiles;
import se.metria.markkoll.util.dokument.hms_generator.HMSGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
public class HMSGeneratorServiceTest {
    @Spy
    HMSGenerator HMSGenerator;

    @Mock
    AvtalRepository avtalRepository;

    @Spy
    ModelMapper modelMapper = new ModelMapperConfiguration().modelMapper();

    @InjectMocks
    HMSGeneratorService hmsGeneratorService;

    @Test
    void getVarderingSkogsmark() throws Exception {
        // Given
        var avtalIds = Arrays.asList(mockUUID(0), mockUUID(1));
        var xlsxExpect = new ClassPathResource("hms/hmsService.xlsx");

        List<HaglofExportView> exports = Arrays.asList(
            new HaglofExportViewImpl(
                mockUUID(0), mockUUID(0), "län1", "kommun1", mockUUID(0).toString(), "fbet1","1/1",
                personEntity("person1", "adress1", "co1", "postort1", "postnummer1", "telefon1", "ePost1", "bankkonto1", "personnummer1"),
                mockUUID(0).toString(), AgartypDto.LF
            ),
            new HaglofExportViewImpl(
                mockUUID(1), mockUUID(1), "län2", "kommun2", mockUUID(1).toString(), "fbet2","1/2",
                personEntity("person2", "adress2", "co2", "postort2", "postnummer2", "telefon2", "ePost2", "bankkonto2", "personnummer2"),
                mockUUID(0).toString(), AgartypDto.OMBUD
            ),
            new HaglofExportViewImpl(
                mockUUID(2), mockUUID(1), "", "", "", "","",
                personEntity("ignorera mig eftersom jag är LF och fastigheten har ett ombud", "", "", "", "", "", "", "", ""),
                "", AgartypDto.LF
            )
        );
        when(avtalRepository.getHaglofExport(any())).thenReturn(exports);


        List<MottagarreferensView> mottagarreferenser = Arrays.asList(
            new MottagarreferensViewImpl(mockUUID(1), "kontaktperson")
        );
        when(avtalRepository.getMottagarreferenser(any())).thenReturn(mottagarreferenser);

        // When
        var xlsxActual = hmsGeneratorService.getVarderingSkogsmark(avtalIds);

        // Then
        MSE_CompareExcelFiles.verifyAllSheets(xlsxExpect.getInputStream().readAllBytes(), xlsxActual);
    }

    private PersonEntity
    personEntity(String namn, String adress, String coAdress, String postort, String postnummer, String telefon,
                 String ePost, String bankkonto, String personnummer) {

        var entity = new PersonEntity();
        entity.setNamn(namn);
        entity.setAdress(adress);
        entity.setCoAdress(coAdress);
        entity.setPostort(postort);
        entity.setPostnummer(postnummer);
        entity.setTelefon(telefon);
        entity.setEPost(ePost);
        entity.setBankkonto(bankkonto);
        entity.setPersonnummer(personnummer);
        return entity;
    }

    @Data
    @AllArgsConstructor
    class MottagarreferensViewImpl implements MottagarreferensView {
        private UUID avtalspartId;
        private String mottagarreferens;
    }

    @Data
    @AllArgsConstructor
    class HaglofExportViewImpl implements HaglofExportView {
        private UUID avtalspartId;
        private UUID avtalId;

        private String lan;
        private String kommun;
        private String fastighetsnummer;
        private String fastighet;
        private String andel;
        private PersonEntity person;
        private String sokId;
        private AgartypDto agartyp;
    }
}
