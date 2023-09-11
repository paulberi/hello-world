package se.metria.markkoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.LedningsagareEntity;

import java.util.List;
import java.util.UUID;

public interface LedningsagareRepository extends JpaRepository<LedningsagareEntity, UUID> {
    void deleteByIdAndKundId(UUID ledningsagareId, String kundId);

    List<LedningsagareEntity> findAllByKundId(String kundId);
}
