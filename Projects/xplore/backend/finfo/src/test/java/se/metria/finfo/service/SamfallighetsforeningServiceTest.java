package se.metria.finfo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import se.metria.finfo.config.ModelMapperConfiguration;
import se.metria.finfo.data.SamfallighetsforeningTestdata;
import se.metria.finfo.openapi.model.RegisterenhetRequestDto;
import se.metria.finfo.repository.SamfallighetsforeningRepository;
import se.metria.finfo.service.fsok.FsokImportService;
import se.metria.finfo.service.importjob.ImportJobOptions;
import se.metria.finfo.service.importjob.ImportJobService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.finfo.data.SamfallighetsforeningTestdata.*;
import static se.metria.finfo.service.SamfallighetsforeningService.TRANSAKTION_SAMFALLIGHETSFORENING;

@ExtendWith(MockitoExtension.class)
class SamfallighetsforeningServiceTest {
    @InjectMocks
    SamfallighetsforeningService samfallighetsforeningService;

    @Mock
    FsokImportService fsokImportService;

    @Mock
    ImportJobService importJobService;

    @Mock
    SamfallighetsforeningRepository samfallighetsforeningRepository;

    @Spy
    ModelMapper modelMapper = ModelMapperConfiguration.modelMapper();

    @Test
    void create() throws Exception {
        // Given
        var samfallighetsforening = samfallighetsforening();

        // When
        var dtoActual = samfallighetsforeningService.create(samfallighetsforening);

        // Then
        verify(samfallighetsforeningRepository).save(any());
        assertEquals(samfallighetsforeningDto(), dtoActual);
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
        var jobIdActual = samfallighetsforeningService.createJob(request, resourcePath);

        // Then
        assertEquals(jobId, jobIdActual);

        var opts = new ImportJobOptions();
        opts.setType(TRANSAKTION_SAMFALLIGHETSFORENING);
        verify(importJobService).createJob(eq(opts), any()); // Hur testar vi att rätt callback anropas??
    }

    @Test
    void get() throws Exception {
        // Given
        var samfUuid = UUID.fromString("dec6215b-e71b-4506-b4f0-78f51688f1f3");

        when(samfallighetsforeningRepository.findTopByUuidOrderByImporteradDatumDesc(eq(samfUuid)))
            .thenReturn(Optional.of(SamfallighetsforeningTestdata.samfallighetsforeningEntity()));

        // When
        var samfActual = samfallighetsforeningService.get(samfUuid);

        // Then
        var samfExpect = SamfallighetsforeningTestdata.samfallighetsforeningDto();
        assertEquals(samfExpect, samfActual);
    }
}