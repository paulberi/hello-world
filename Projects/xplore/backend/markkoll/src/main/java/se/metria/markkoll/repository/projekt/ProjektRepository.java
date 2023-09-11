package se.metria.markkoll.repository.projekt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.AvtalMappstrategiDto;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.repository.projekt.jooq.ProjektRepositoryJooq;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProjektRepository extends JpaRepository<ProjektEntity, UUID>, ProjektRepositoryJooq {
    @Query("SELECT CASE WHEN COUNT(ff) > 0 THEN true ELSE false END " +
        "FROM FastighetsforteckningEntity ff " +
        "WHERE ff.projekt.id = :projektId " +
        "AND ff.fastighet.id = :fastighetId")
    Boolean containsFastighet(UUID projektId, UUID fastighetId);

    @Query("SELECT CASE WHEN p.currentVersion IS NULL THEN null ELSE p.currentVersion.id END " +
           "  FROM ProjektEntity p " +
           " WHERE p.id = :projektId")
    UUID getCurrentVersionId(UUID projektId);

    @Query("SELECT p.id FROM ProjektEntity p JOIN p.avtal av WHERE av.id = :avtalId")
    UUID getIdByAvtalId(UUID avtalId);

    @Query("SELECT p.namn FROM ProjektEntity p WHERE p.id = :projektId")
    String getNamn(UUID projektId);

    Boolean existsByIdAndKundId(UUID projektId, String kund);

    @Query("SELECT p.kundId FROM ProjektEntity p WHERE p.id = :projektId")
    String getKundId(UUID projektId);

    @Query("SELECT p.id FROM ProjektEntity p WHERE p.currentVersion.id = :versionId")
    Optional<UUID> projektIdByVersionId(UUID versionId);

    @Query("SELECT p.projekttyp FROM ProjektEntity p WHERE p.id = :projektId")
    ProjektTypDto getProjekttyp(UUID projektId);

    @Query("SELECT COUNT (a) FROM AvtalEntity a WHERE a.projekt.id = :projektId")
    Integer getNumOfAvtal(UUID projektId);

    @Query("SELECT p.id FROM ProjektEntity p WHERE p.kundId = :kundId")
    Set<UUID> getProjektIds(String kundId);

    @Query("SELECT p.projekttyp FROM ProjektEntity p JOIN p.avtal av WHERE av.id = :avtalId")
    ProjektTypDto getProjekttypAvtal(UUID avtalId);

    @Query("SELECT p.uppdragsnummer FROM ProjektEntity p JOIN p.avtal av WHERE av.id = :avtalId")
    String getUppdragsnummerAvtal(UUID avtalId);

    @Query("SELECT ff.fastighet.id FROM FastighetsforteckningEntity ff " +
        "    WHERE ff.projekt.id = :projektId " +
        "      AND ff.fastighet.id IN :fastighetIds")
    List<UUID> filterExistingRegisterenheterInProjekt(UUID projektId, Iterable<UUID> fastighetIds);

    @Query("SELECT p.projekttyp                                           AS projektTyp," +
        "          p.utskicksstrategi                                     AS utskicksstrategi, " +
        "          CASE WHEN p.projekttyp = 'FIBER' THEN fp.varderingsprotokoll ELSE true END AS shouldHaveVp" +
        "     FROM ProjektEntity p " +
        "LEFT JOIN FiberProjektEntity fp ON fp.id = p.id" +
        "     JOIN AvtalEntity av ON av.projekt.id = p.id" +
        "    WHERE av.id = :avtalId")
    UtskickProjektView getUtskickProjektInfo(UUID avtalId);
}