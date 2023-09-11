package se.metria.xplore.samrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.samrad.entities.AnvandareEntity;

import java.util.UUID;

public interface AnvandareRepository extends JpaRepository<AnvandareEntity, UUID> {
}
