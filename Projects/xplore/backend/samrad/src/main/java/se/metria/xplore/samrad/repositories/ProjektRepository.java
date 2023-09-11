package se.metria.xplore.samrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.samrad.entities.ProjektEntity;

import java.util.UUID;


public interface ProjektRepository extends JpaRepository<ProjektEntity, UUID> {
}
