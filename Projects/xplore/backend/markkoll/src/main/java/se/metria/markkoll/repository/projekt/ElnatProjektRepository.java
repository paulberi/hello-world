package se.metria.markkoll.repository.projekt;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.projekt.ElnatProjektEntity;

import java.util.UUID;

public interface ElnatProjektRepository extends JpaRepository<ElnatProjektEntity, UUID> {
}
