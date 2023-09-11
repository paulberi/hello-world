package se.metria.markkoll.repository.geometristatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.GeometristatusEntity;
import se.metria.markkoll.openapi.model.GeometristatusDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GeometristatusRepository extends JpaRepository<GeometristatusEntity, UUID> {
    @Query("SELECT gs FROM GeometristatusEntity gs JOIN AvtalEntity a ON a.id = gs.avtal.id WHERE a.fastighet.id = :fastighetId AND gs.version.id = :versionId")
    Optional<GeometristatusEntity> findByFastighetIdAndVersionId(UUID fastighetId, UUID versionId);

    @Query("SELECT gs " +
             "FROM GeometristatusEntity gs " +
             "JOIN ProjektEntity p " +
               "ON p.id = :projektId " +
             "JOIN AvtalEntity a " +
               "ON a.id = gs.avtal.id " +
            "WHERE a.fastighet.id = :fastighetId " +
              "AND gs.version.id = p.currentVersion.id")
    Optional<GeometristatusEntity> findByFastighetIdAndProjektId(UUID fastighetId, UUID projektId);

    void deleteByVersionIdAndAvtalId(UUID versionId, UUID avtalId);

    List<GeometristatusAvtalId> findByVersionIdAndAvtalIdIn(UUID versionId, Iterable<UUID> avtalIds);

    @Modifying
    @Query("UPDATE GeometristatusEntity gs " +
        "      SET gs.geometristatus = :geometristatus " +
        "    WHERE gs.version.id = :versionId " +
        "      AND gs.avtal.id IN :avtalIds")
    void updateGeometristatus(UUID versionId, Iterable<UUID> avtalIds, GeometristatusDto geometristatus);
}
