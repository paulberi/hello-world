package se.metria.markkoll.repository.finfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.finfo.FinfoAgareEntity;

import java.util.Set;
import java.util.UUID;

@Repository
public interface FinfoAgareRepository extends JpaRepository<FinfoAgareEntity, UUID> {
    Set<FinfoAgareEntity> findByFastighetId(UUID fastighetId);
}