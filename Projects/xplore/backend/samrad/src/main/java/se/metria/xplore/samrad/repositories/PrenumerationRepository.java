package se.metria.xplore.samrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.samrad.entities.PrenumerationEntity;

import java.util.UUID;

public interface PrenumerationRepository extends JpaRepository<PrenumerationEntity, UUID> {
}
