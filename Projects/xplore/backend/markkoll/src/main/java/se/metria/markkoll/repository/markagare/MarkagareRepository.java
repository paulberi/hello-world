package se.metria.markkoll.repository.markagare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.markagare.MarkagareEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MarkagareRepository extends JpaRepository<MarkagareEntity, UUID> {
    Set<MarkagareEntity> findByFastighetIdAndKundId(UUID fastighetId, String kundId);
    Optional<MarkagareEntity> findByPersonIdAndFastighetId(UUID personId, UUID fastighetId);
}
