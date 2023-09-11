package se.metria.xplore.samrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.samrad.entities.SynpunktEntity;

import java.util.UUID;

public interface SynpunktRepository extends JpaRepository<SynpunktEntity, UUID> {
}
