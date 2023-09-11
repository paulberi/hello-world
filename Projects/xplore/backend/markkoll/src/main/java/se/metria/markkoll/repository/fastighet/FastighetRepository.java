package se.metria.markkoll.repository.fastighet;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.repository.fastighet.jooq.FastighetRepositoryJooq;

import java.util.*;

@Repository
public interface FastighetRepository extends JpaRepository<FastighetEntity, UUID>, FastighetRepositoryJooq {
    @Query("SELECT extent(f.geom) FROM FastighetOmradeEntity f WHERE f.fastighetId = :fastighetId")
    Optional<Geometry> getExtent(UUID fastighetId);

    @Query("SELECT f.fastighetsbeteckning FROM FastighetEntity f WHERE f.id = :fastighetId")
    String getFastighetsbeteckning(UUID fastighetId);

    @Query("SELECT DISTINCT f " +
             "FROM FastighetEntity f " +
             "JOIN AvtalEntity a " +
               "ON a.fastighet.id = f.id " +
            "WHERE a.projekt.id = :projektId")
    List<FastighetEntity> getFastigheter(UUID projektId);

    @Query("SELECT DISTINCT f.id " +
            "FROM FastighetEntity f " +
            "JOIN AvtalEntity a " +
            "ON a.fastighet.id = f.id " +
            "JOIN GeometristatusEntity g " +
            "ON g.avtal.id = a.id " +
            "WHERE g.version.id = :versionId")
    List<UUID> getAllByVersionId(UUID versionId);
    
    List<FastighetEntity> getAllByIdIn(Set<UUID> registerenheter);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
        "     FROM FastighetEntity f " +
        "     JOIN f.markagare m " +
        "    WHERE m.agareSaknas = true " +
        "      AND f.id = :fastighetId")
    boolean agareSaknas(UUID fastighetId);

    @Query("SELECT DISTINCT f " +
            "FROM FastighetEntity f " +
            "JOIN AvtalEntity a " +
            "ON a.fastighet.id = f.id " +
            "WHERE a.projekt.id = :projektId " +
            "AND f.id IN :fastighetIds")
    List<FastighetEntity> getFastigheterAndSamfalligheterByIdIn(UUID projektId, List<UUID> fastighetIds);

    @Query("SELECT DISTINCT f.id" +
        "     FROM FastighetEntity f" +
        "     JOIN OmradesintrangEntity oi" +
        "       ON oi.fastighetomrade.fastighetId = f.id" +
        "     JOIN ProjektEntity p " +
        "       ON p.currentVersion.id = oi.version.id" +
        "    WHERE p.id = :projektId" +
        "      AND oi.avtalstyp IN :avtalstyper")
    List<UUID> getFastighetIdsWithAvtalstyper(UUID projektId, Iterable<AvtalstypDto> avtalstyper);
}
