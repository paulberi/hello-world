package se.metria.markkoll.repository.indata;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.indata.IndataEntity;

import java.util.UUID;

public interface IndataRepository extends JpaRepository<IndataEntity, UUID> {
    void deleteByProjektId(UUID projektId);
}
