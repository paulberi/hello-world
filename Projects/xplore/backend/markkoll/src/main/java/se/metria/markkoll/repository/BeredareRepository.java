package se.metria.markkoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.BeredareEntity;

import java.util.UUID;

public interface BeredareRepository extends JpaRepository<BeredareEntity, UUID> {
    BeredareEntity findByProjektId(UUID projektId);
}
