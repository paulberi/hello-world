package se.metria.markkoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FastighetsforteckningRepository extends JpaRepository<FastighetsforteckningEntity, UUID> {
    @Query("SELECT f " + 
            " FROM FastighetsforteckningEntity f " + 
            " WHERE f.avtal.id = :avtalId "
    )
    FastighetsforteckningEntity getByAvtalId(UUID avtalId);

    @Query("SELECT f " +
            " FROM FastighetsforteckningEntity f " +
            " WHERE f.projekt.id = :projektId " +
            " AND f.excluded = false"
    )
    List<FastighetsforteckningEntity> getIncludedByProjektId(UUID projektId);

    Optional<FastighetsforteckningEntity> findByProjektIdAndFastighetId(UUID projektId, UUID fastighetId);

    List<FastighetsforteckningEntity> findAllByProjektIdAndFastighetIdIn(UUID projektId, Iterable<UUID> fastighetId);
}
