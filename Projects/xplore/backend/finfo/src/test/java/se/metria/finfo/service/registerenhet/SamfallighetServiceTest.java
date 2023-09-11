package se.metria.finfo.service.registerenhet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import se.metria.finfo.config.ModelMapperConfiguration;
import se.metria.finfo.data.SamfallighetTestdata;
import se.metria.finfo.openapi.model.RegisterenhetRequestDto;
import se.metria.finfo.repository.RegisterenhetRepository;
import se.metria.finfo.service.fsok.FsokImportService;
import se.metria.finfo.service.importjob.ImportJobOptions;
import se.metria.finfo.service.importjob.ImportJobService;
import se.metria.finfo.util.FastighetsinformationToSamfallighetEntityMapper;
import se.metria.finfo.util.XmlUtil;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.finfo.data.SamfallighetTestdata.samfallighetDto;
import static se.metria.finfo.service.registerenhet.SamfallighetService.TRANSAKTION_ALLMAN_MINI;

@ExtendWith(MockitoExtension.class)
public class SamfallighetServiceTest {
    @InjectMocks
    SamfallighetService samfallighetService;

    @Spy
    FastighetsinformationToSamfallighetEntityMapper samfMapper = new FastighetsinformationToSamfallighetEntityMapper();

    @Mock
    FsokImportService fsokImportService;

    @Mock
    ImportJobService importJobService;

    @Spy
    ModelMapper modelMapper = ModelMapperConfiguration.modelMapper();

    @Mock
    RegisterenhetRepository registerenhetRepository;

    @Test
    void create() throws Exception {
        // Given
        var fastighetsinformation = XmlUtil.parseFastighetsinformation("samfallighet.xml");

        // When
        var samf = samfallighetService.create(fastighetsinformation);

        // Then
        verify(registerenhetRepository).save(any());
        modelMapper.validate();
        assertEquals(samfallighetDto(), samf);
    }

    @Test
    void createJob() {
        // Given
        var jobId = UUID.fromString("919ae766-00ca-4498-9efd-59e2e1df05ff");
        var resourcePath = "resource/path";
        var request = new RegisterenhetRequestDto()
            .kundmarke("kundmarke")
            .username("username")
            .password("password")
            .nyckel("dec6215b-e71b-4506-b4f0-78f51688f1f3");

        when(importJobService.createJob(any(), any())).thenReturn(jobId);

        // When
        var jobIdActual = samfallighetService.createJob(request, resourcePath);

        // Then
        assertEquals(jobId, jobIdActual);

        var opts = new ImportJobOptions();
        opts.setType(TRANSAKTION_ALLMAN_MINI);
        verify(importJobService).createJob(eq(opts), any()); // Hur testar vi att rätt callback anropas??
    }

    @Test
    void get() {
        // Given
        var samfUuid = UUID.fromString("dec6215b-e71b-4506-b4f0-78f51688f1f3");

        when(registerenhetRepository.findTopByUuidOrderByImporteradDatumDesc(eq(samfUuid)))
            .thenReturn(Optional.of(SamfallighetTestdata.samfallighetEntity()));

        // When
        var samf = samfallighetService.get(samfUuid);

        // Then
        modelMapper.validate();
        assertEquals(SamfallighetTestdata.samfallighetDto(), samf);
    }
}
