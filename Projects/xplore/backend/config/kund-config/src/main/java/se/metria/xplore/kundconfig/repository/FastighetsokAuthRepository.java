package se.metria.xplore.kundconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.kundconfig.entity.FastighetsokAuthEntity;

import java.util.Optional;
import java.util.UUID;

public interface FastighetsokAuthRepository extends JpaRepository<FastighetsokAuthEntity, UUID> {
    Optional<FastighetsokAuthEntity> findByKundId(String kundId);
}
