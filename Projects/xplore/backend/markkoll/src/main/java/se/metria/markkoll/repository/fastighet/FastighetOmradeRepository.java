package se.metria.markkoll.repository.fastighet;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.entity.fastighet.OmradeId;

import java.util.List;
import java.util.UUID;

@Repository
public interface FastighetOmradeRepository extends JpaRepository<FastighetOmradeEntity, OmradeId> {
    @Query("SELECT fo.omradeNr FROM FastighetOmradeEntity fo WHERE fo.fastighetId = :fastighetId")
    List<Long> getOmradeNrs(UUID fastighetId);

    @Query("SELECT extent(fo.geom) " +
             "FROM FastighetOmradeEntity fo " +
            "WHERE fo.fastighetId = :fastighetId " +
              "AND fo.omradeNr = :omradeNr")
    Geometry getExtent(UUID fastighetId, Long omradeNr);

    @Query("SELECT extent(buffer(fo.geom, :buffer)) " +
            "FROM FastighetOmradeEntity fo " +
            "WHERE fo.fastighetId = :fastighetId " +
            "AND fo.omradeNr = :omradeNr")
    Geometry getExtentBuffered(UUID fastighetId, Long omradeNr, float buffer);

    @Query("SELECT geometrytype(fo.geom) " +
             "FROM FastighetOmradeEntity fo " +
            "WHERE fo.fastighetId = :fastighetId " +
              "AND fo.omradeNr = :omradeNr")
    String getGeometryType(UUID fastighetId, Long omradeNr);

    List<FastighetOmradeEntity> findAllByFastighetIdIn(Iterable<UUID> fastighetIds);
}
