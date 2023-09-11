package se.metria.markkoll.repository.logging.avtalslogg;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.logging.avtalslogg.LogGeometristatusEntity;

import java.util.List;
import java.util.UUID;

public interface LogGeometristatusRepository extends JpaRepository<LogGeometristatusEntity, UUID> {
    List<LogGeometristatusEntity> findByAvtalId(UUID avtalId);
}
