package se.metria.markkoll.repository.intrang;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.intrang.FastighetsintrangEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FastighetsintrangRepository extends JpaRepository<FastighetsintrangEntity, UUID>, FastighetsintrangRepositoryCustom {
    List<FastighetsintrangEntity> findAllByFastighetIdAndVersionId(UUID fastighetId, UUID versionId);

    @Query("SELECT DISTINCT fie.fastighet.id FROM FastighetsintrangEntity fie WHERE fie.version.id = :versionId")
    List<UUID> findAllFastighetIds(UUID versionId);

    @Query("SELECT fie " +
             "FROM FastighetsintrangEntity fie " +
             "JOIN fie.fastighet fast " +
             "JOIN fie.version.projekt p " +
             "JOIN AvtalEntity a " +
               "ON a.fastighet = fast " +
              "AND a.projekt = p")
    List<FastighetsintrangEntity> getAllByAvtalId(UUID avtalId);

    @Query(value = "SELECT st_length(geom) FROM fastighetsintrang WHERE id = :fastighetsintrangId", nativeQuery = true)
    Double getIntrangLength(UUID fastighetsintrangId);

    @Query(value = "SELECT st_asgeojson(st_extent(geom)) FROM markkoll.fastighetsintrang WHERE version_id = :versionId",
            nativeQuery = true)
    Optional<String> getExtentByVersionId(UUID versionId);

    @Query(value = "SELECT extent(fi.geom) " +
            "FROM FastighetsintrangEntity fi " +
            "JOIN ProjektEntity p " +
            "ON p.id = :projektId " +
            "WHERE p.currentVersion.id = fi.version.id")
    Geometry getExtentByProjektId(UUID projektId);

    @Query(value = "SELECT st_asgeojson(st_buffer(st_union(geom), 0.0001)) " +
            "FROM markkoll.fastighetsintrang " +
            "JOIN markkoll.projekt " +
            "ON projekt.id = :projektId " +
            "JOIN markkoll.current_version " +
            "ON current_version.projekt_id = projekt.id " +
            "WHERE current_version.version_id = fastighetsintrang.version_id",
            nativeQuery = true)
    Optional<String> getIntrangByProjekt(UUID projektId);

    @Query(value =
    "    SELECT st_equals(st_collectionextract(st_union(fi1.geom), 2), st_collectionextract(st_union(fi2.geom), 2)) " +
    "           AND st_equals(st_collectionextract(st_union(fi1.geom), 1), st_collectionextract(st_union(fi2.geom), 1)) " +
    "           AND st_equals(st_collectionextract(st_union(fi1.geom), 3), st_collectionextract(st_union(fi2.geom), 3)) " +
    "      FROM markkoll.fastighetsintrang fi1 " +
    "      JOIN markkoll.fastighetsintrang fi2 " +
    "        ON fi1.fastighet_id = fi2.fastighet_id " +
    "     WHERE fi1.version_id = :versionId1 " +
    "       AND fi2.version_id = :versionId2 " +
    "       AND fi1.fastighet_id = :fastighetId " +
    "  GROUP BY fi1.fastighet_id, fi2.fastighet_id;", nativeQuery = true)
    Boolean geometriesEqual(UUID fastighetId, UUID versionId1, UUID versionId2);

    @Query(value = "SELECT CASE WHEN fi1.avtalstyp = fi2.avtalstyp THEN true ELSE false END as av_equal " +
        "     FROM markkoll.fastighetsintrang fi1 " +
        "     JOIN markkoll.fastighetsintrang fi2 " +
        "       ON fi1.fastighet_id = fi2.fastighet_id " +
        "      AND st_equals(fi1.geom, fi2.geom) " +
        "    WHERE fi1.version_id = :versionId1 " +
        "      AND fi2.version_id = :versionId2 " +
        "      AND fi1.fastighet_id = :fastighetId", nativeQuery = true)
    List<Boolean> avtalstypEqual(UUID fastighetId, UUID versionId1, UUID versionId2);

    default Envelope getEnvelopeByProjektId(UUID projektId) {
        return getExtentByProjektId(projektId).getEnvelopeInternal();
    }
}
