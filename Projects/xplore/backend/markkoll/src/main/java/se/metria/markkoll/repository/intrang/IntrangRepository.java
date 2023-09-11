package se.metria.markkoll.repository.intrang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.intrang.IntrangEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IntrangRepository extends JpaRepository<IntrangEntity, UUID> {

    @Query(value = "SELECT st_asgeojson(st_buffer(st_union(geom), 0.0001)) " +
            "FROM markkoll.intrang " +
            "JOIN markkoll.projekt " +
            "ON projekt.id = :projektId " +
            "JOIN markkoll.current_version " +
            "ON current_version.projekt_id = projekt.id " +
            "WHERE current_version.version_id = intrang.version_id",
            nativeQuery = true)
    Optional<String> getIntrangWithBuffertByProjekt(UUID projektId);

    @Query(value = "SELECT i " +
            "FROM IntrangEntity i " +
            "JOIN i.version.projekt p " +
            "WHERE p.id = :projektId " +
            "AND i.version.id = p.currentVersion.id ")
    Optional<List<IntrangEntity>> findAllByProjektId(UUID projektId);

    List<IntrangEntity> findAllByVersionId(UUID versionId);
}
