package se.metria.markkoll.repository.fastighet;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.samfallighet.SamfallighetMerInfoEntity;

import java.util.Optional;
import java.util.UUID;

public interface SamfallighetMerInfoRepository extends JpaRepository<SamfallighetMerInfoEntity, UUID> {
    Boolean existsBySamfallighetIdAndKundId(UUID samfallighetId, String kundId);

    Optional<SamfallighetMerInfoEntity> findBySamfallighetIdAndKundId(UUID samfId, String kundId);
}
