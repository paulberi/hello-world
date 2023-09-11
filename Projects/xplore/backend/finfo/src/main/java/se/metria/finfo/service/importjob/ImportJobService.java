package se.metria.finfo.service.importjob;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.metria.finfo.entity.importjob.ImportJobEntity;
import se.metria.finfo.openapi.model.ImportJobInfoDto;
import se.metria.finfo.openapi.model.ImportJobStatusDto;
import se.metria.finfo.repository.ImportJobRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;
import java.util.concurrent.Callable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ImportJobService {
    @NonNull
    private final ImportJobRepository importJobRepository;

    @NonNull
    private final TaskExecutor taskExecutor;

    @NonNull
    private final ModelMapper modelMapper;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public UUID createJob(ImportJobOptions opts, Callable<String> job) {
        var jobEntity = new ImportJobEntity();
        jobEntity.setType(opts.getType());

        jobEntity = importJobRepository.save(jobEntity);

        var jobId = jobEntity.getId();
        taskExecutor.execute(() -> executeJob(job, jobId));

        return jobEntity.getId();
    }

    public ImportJobInfoDto getJobInfo(UUID jobId) {
        var importJobEntity = importJobRepository.findById(jobId).orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(importJobEntity, ImportJobInfoDto.class);
    }

    private void executeJob(Callable<String> job, UUID jobId) {
        try {
            log.info("Exekverar importjobb " + jobId);
            importJobRepository.updateStatus(jobId, ImportJobStatusDto.IN_PROGRESS);

            var resource = job.call();

            importJobRepository.updateResource(jobId, resource);
            importJobRepository.updateStatus(jobId, ImportJobStatusDto.DONE);
            log.info("Importjobb {} klart", jobId);
        } catch (Exception e) {
            importJobRepository.updateStatus(jobId, ImportJobStatusDto.FAILED);
            log.error("Importjobb {} misslyckades:", jobId, e);
        }
    }
}
