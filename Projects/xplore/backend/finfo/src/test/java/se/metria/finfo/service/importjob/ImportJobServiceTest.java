package se.metria.finfo.service.importjob;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.task.TaskExecutor;
import se.metria.finfo.config.ModelMapperConfiguration;
import se.metria.finfo.entity.importjob.ImportJobEntity;
import se.metria.finfo.openapi.model.ImportJobInfoDto;
import se.metria.finfo.openapi.model.ImportJobStatusDto;
import se.metria.finfo.repository.ImportJobRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportJobServiceTest {
    @InjectMocks
    ImportJobService importJobService;

    @Mock
    TaskExecutor taskExecutor;

    @Mock
    ImportJobRepository importJobRepository;

    @Spy
    ModelMapper modelMapper = ModelMapperConfiguration.modelMapper();

    @Test
    void createJob() {
        // Given
        var jobIdExpect = UUID.fromString("dec6215b-e71b-4506-b4f0-78f51688f1f3");
        var opts = new ImportJobOptions();
        opts.setType("typ");

        var entity = new ImportJobEntity();
        entity.setType("typ");

        var entity2 = new ImportJobEntity();
        entity2.setId(jobIdExpect);
        when(importJobRepository.save(any())).thenReturn(entity2);

        // When
        var jobIdActual = importJobService.createJob(opts, () ->  null);

        // Then;
        verify(importJobRepository).save(any());
        verify(taskExecutor).execute(any()); // Hur testar vi att rätt callback anropas??

        assertEquals(jobIdExpect, jobIdActual);
    }

    @Test
    void getJobInfo() {
        // Given
        var jobId = UUID.fromString("dec6215b-e71b-4506-b4f0-78f51688f1f3");

        var importJobEntity = new ImportJobEntity();
        importJobEntity.setStatus(ImportJobStatusDto.DONE);
        importJobEntity.setResource("resurs");
        importJobEntity.setType("typ");

        when(importJobRepository.findById(eq(jobId))).thenReturn(Optional.of(importJobEntity));

        // When
        var jobInfoActual = importJobService.getJobInfo(jobId);

        // Then
        var jobInfoExpect = new ImportJobInfoDto()
            .status(ImportJobStatusDto.DONE)
            .resource("resurs")
            .type("typ");

        assertEquals(jobInfoExpect, jobInfoActual);
        modelMapper.validate();
    }
}