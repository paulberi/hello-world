package se.metria.xplore.samrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.samrad.entities.FilEntity;

import java.util.UUID;

public interface FilRepository extends JpaRepository<FilEntity, UUID> {
}
