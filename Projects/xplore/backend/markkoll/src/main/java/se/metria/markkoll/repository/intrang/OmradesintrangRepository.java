package se.metria.markkoll.repository.intrang;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.intrang.OmradesintrangEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OmradesintrangRepository extends JpaRepository<OmradesintrangEntity, UUID>, OmradesintrangRepositoryCustom {
    @Query("  SELECT max(omradesintrang.spanningsniva) " +
           "    FROM OmradesintrangEntity omradesintrang " +
           "    JOIN AvtalEntity avtal " +
           "      ON avtal.fastighet.id = omradesintrang.fastighetomrade.fastighetId " +
           "   WHERE omradesintrang.version.id = avtal.projekt.currentVersion.id " +
           "     AND avtal.id = :avtalId " +
           "GROUP BY omradesintrang.fastighetomrade.fastighetId")
    Double calculateHighestSpanningsniva(UUID avtalId);

    @Query("SELECT omr from OmradesintrangEntity omr WHERE omr.fastighetomrade.fastighetId = :fastighetId")
    List<OmradesintrangEntity> findByFastighetId(UUID fastighetId);

    @Query("SELECT omr " +
            "FROM OmradesintrangEntity omr " +
            "JOIN AvtalEntity av ON av.fastighet.id = omr.fastighetomrade.fastighetId " +
            "WHERE av.id = :avtalId " +
            "AND omr.avtalstyp IN :avtalstyper " +
            "AND omr.version.id = :versionId")
    List<OmradesintrangEntity> findByAvtalIdAndAvtalstyper(UUID avtalId, UUID versionId, Iterable<AvtalstypDto> avtalstyper);

    @Query("SELECT extent(buffer(omr.geom, 5.)) " +
            "FROM OmradesintrangEntity omr " +
            "JOIN ProjektEntity p " +
            "ON p.currentVersion.id = omr.version.id " +
            "WHERE omr.fastighetomrade.fastighetId = :fastighetId " +
            "AND omr.fastighetomrade.omradeNr = :omradeNr " +
            "AND p.id = :projektId " +
            "GROUP BY p.id, omr.fastighetomrade.fastighetId, omr.fastighetomrade.omradeNr")
    Optional<Geometry> getExtentBuffered(UUID fastighetId, UUID projektId, Long omradeNr);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE markkoll.intrang " +
        "              SET geom = ST_LineMerge(geom) " +
        "            WHERE ST_GeometryType(geom) = 'ST_MultiLineString';",
        nativeQuery = true)
    int mergeLines();

    default Envelope getEnvelopeBuffered(UUID fastighetId, UUID projektId, Long omradeNr) {
        return getExtentBuffered(fastighetId, projektId, omradeNr).get().getEnvelopeInternal();
    }
}
