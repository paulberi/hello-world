package se.metria.xplore.samrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.samrad.entities.SynpunktSvarEntity;

import java.util.UUID;

public interface SynpunktSvarRepository extends JpaRepository<SynpunktSvarEntity, UUID> {
}
