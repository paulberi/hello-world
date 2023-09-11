package se.metria.markkoll.repository.logging.avtalslogg;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.logging.avtalslogg.LogAvtalsstatusEntity;

import java.util.UUID;

public interface LogAvtalsstatusRepository extends JpaRepository<LogAvtalsstatusEntity, UUID> {
}
