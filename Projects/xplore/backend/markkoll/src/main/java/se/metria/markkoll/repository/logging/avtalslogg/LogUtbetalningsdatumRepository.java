package se.metria.markkoll.repository.logging.avtalslogg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.logging.avtalslogg.LogUtbetalningsdatumEntity;

import java.util.List;
import java.util.UUID;

public interface LogUtbetalningsdatumRepository extends JpaRepository<LogUtbetalningsdatumEntity, UUID> {
    @Query("SELECT log " +
        "     FROM LogUtbetalningsdatumEntity log " +
        "    WHERE log.avtalspart.avtal.id = :avtalId")
    List<LogUtbetalningsdatumEntity> findAllByAvtalId(UUID avtalId);
}
