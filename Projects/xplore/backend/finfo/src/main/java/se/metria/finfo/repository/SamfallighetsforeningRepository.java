package se.metria.finfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.finfo.entity.samfallighetsforening.SamfallighetsforeningEntity;

import java.util.Optional;
import java.util.UUID;

public interface SamfallighetsforeningRepository extends JpaRepository<SamfallighetsforeningEntity, UUID> {
    Optional<SamfallighetsforeningEntity> findTopByUuidOrderByImporteradDatumDesc(UUID samfUUID);
}
