package se.metria.markkoll.repository.version.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.ImportVersionEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface VersionRepository extends JpaRepository<ImportVersionEntity, UUID> {
    List<ImportVersionEntity> findAllByProjektId(UUID projektId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM ProjektEntity p WHERE p.currentVersion.id = :versionId")
    Boolean isCurrentVersion(UUID versionId);

    @Query("SELECT p.currentVersion.id FROM ProjektEntity p WHERE p.id = :projektId")
    UUID getCurrentVersionId(UUID projektId);

    @Query("SELECT p.currentVersion FROM ProjektEntity p WHERE p.id = :projektId")
    ImportVersionEntity getCurrentVersion(UUID projektId);

    @Query("SELECT v.projekt.id FROM ImportVersionEntity v WHERE v.id = :versionId")
    UUID getProjektId(UUID versionId);
}
