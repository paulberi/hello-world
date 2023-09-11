package se.metria.markkoll.repository.samradRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.samradEntities.SamradProjektidEntity;

import java.util.UUID;

public interface SamradProjektidRepository extends JpaRepository<SamradProjektidEntity, UUID> {
    public SamradProjektidEntity getBySamradId(String samradId);
}
