package se.metria.markkoll.repository.logging.projektlogg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.logging.projektlogg.ProjektLoggEntity;
import se.metria.markkoll.repository.logging.projektlogg.jooq.ProjektLoggRepositoryJooq;

import java.util.UUID;

@Repository
public interface ProjektLoggRepository extends JpaRepository<ProjektLoggEntity, UUID>, ProjektLoggRepositoryJooq {
}