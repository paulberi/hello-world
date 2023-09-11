package se.metria.xplore.kundconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.kundconfig.entity.MetriaMapsAuthEntity;

import java.util.Optional;
import java.util.UUID;

public interface MetriaMapsAuthRepository extends JpaRepository<MetriaMapsAuthEntity, UUID> {
    Optional<MetriaMapsAuthEntity> findByKundId(String kundId);
}
