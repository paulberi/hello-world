package se.metria.finfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import se.metria.finfo.entity.importjob.ImportJobEntity;
import se.metria.finfo.openapi.model.ImportJobStatusDto;

import java.util.UUID;

public interface ImportJobRepository extends JpaRepository<ImportJobEntity, UUID> {
    @Transactional
    @Modifying
    @Query("UPDATE ImportJobEntity job SET job.status = :status where job.id = :jobId")
    public void updateStatus(UUID jobId, ImportJobStatusDto status);

    @Transactional
    @Modifying
    @Query("UPDATE ImportJobEntity job SET job.resource = :resource where job.id = :jobId")
    public void updateResource(UUID jobId, String resource);
}
