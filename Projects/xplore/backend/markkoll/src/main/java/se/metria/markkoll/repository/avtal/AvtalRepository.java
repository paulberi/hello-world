package se.metria.markkoll.repository.avtal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.openapi.model.AvtalMetadataDto;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.TillvaratagandeTypDto;

import java.util.*;

public interface AvtalRepository extends JpaRepository<AvtalEntity, UUID>, AvtalRepositoryCustom {
    Boolean existsByFastighetId(UUID avtalId);

    @Query("SELECT av FROM AvtalEntity av WHERE av.projekt.id = :projektId AND av.fastighet.id = :fastighetId")
    Optional<AvtalEntity> findByProjektIdAndFastighetId(UUID projektId, UUID fastighetId);

    @Query("SELECT a.id FROM AvtalEntity a WHERE a.projekt.id = :projektId AND a.fastighet.id = :fastighetId")
    UUID getIdByProjektIdAndFastighetId(UUID projektId, UUID fastighetId);

    @Query("SELECT a FROM AvtalEntity a WHERE a.projekt.id = :projektId AND a.fastighet.id = :fastighetId")
    AvtalEntity getAvtalByProjektIdAndFastighetId(UUID projektId, UUID fastighetId);

    @Query("SELECT DISTINCT a " +
            "FROM AvtalEntity a " +
            "JOIN ProjektEntity p " +
            "ON p.id = a.projekt.id " +
            "WHERE a.fastighet.id = :fastighetId " +
            "AND p.kundId = :kundId")
    List<AvtalEntity> findByFastighetIdAndKundId(UUID fastighetId, String kundId);

    @Query("SELECT a.id " +
            "FROM AvtalEntity a " +
            "JOIN ImportVersionEntity iv " +
            "ON iv.projekt.id = a.projekt.id " +
            "WHERE a.fastighet.id = :fastighetId " +
            "AND iv.id = :versionId")
    UUID findIdByVersionIdAndFastighetId(UUID versionId, UUID fastighetId);

    @Query("SELECT avtal " +
            "FROM AvtalEntity avtal " +
            "JOIN ProjektEntity p ON avtal.projekt.id = p.id " +
            "JOIN avtal.geometristatus ag ON ag.version.id = p.currentVersion.id AND ag.avtal.id = avtal.id " +
            "WHERE avtal.projekt.id = :projektId AND ag.geometristatus = 'NY'"
    )
    Set<AvtalEntity> findNewByProjektId(UUID projektId);

    @Query("SELECT avtal " +
            "FROM AvtalEntity avtal " +
            "JOIN ProjektEntity p ON avtal.projekt.id = p.id " +
            "JOIN avtal.geometristatus ag ON ag.version.id = p.currentVersion.id AND ag.avtal.id = avtal.id " +
            "WHERE avtal.projekt.id = :projektId AND ag.geometristatus = 'UPPDATERAD'"
    )
    Set<AvtalEntity> findUpdatedByProjektId(UUID projektId);

    @Query("SELECT avtal.id " +
        "     FROM AvtalEntity avtal " +
        "     JOIN ProjektEntity p ON avtal.projekt.id = p.id " +
        "     JOIN avtal.geometristatus ag ON ag.version.id = p.currentVersion.id AND ag.avtal.id = avtal.id " +
        "    WHERE avtal.projekt.id = :projektId AND ag.geometristatus = 'UPPDATERAD'"
    )
    Set<UUID> findUpdatedIdsByProjektId(UUID projektId);

    @Query(value = "SELECT status " +
            "FROM avtalsstatus_view " +
            "WHERE projekt_id = :projektId " +
            "AND fastighet_id = :fastighetId",
            nativeQuery = true)
    AvtalsstatusDto getAvtalsstatus(UUID projektId, UUID fastighetId); // native query, behöver "nakna" UUIDs...

    @Query(value = "SELECT status " +
        "FROM avtalsstatus_view " +
        "JOIN avtal " +
        "ON avtal.fastighet_id = avtalsstatus_view.fastighet_id " +
        "AND avtal.projekt_id = avtalsstatus_view.projekt_id " +
        "WHERE avtal.id = :avtalId ",
        nativeQuery = true)
    AvtalsstatusDto getAvtalsstatus(UUID avtalId);

    @Query("SELECT a.projekt.id FROM AvtalEntity a WHERE a.id = :avtalId")
    UUID getProjektId(UUID avtalId);

    @Query("SELECT a.fastighet.id FROM AvtalEntity a WHERE a.id = :avtalId")
    UUID getFastighetId(UUID avtalId);

    List<AvtalEntity> findAllByProjektIdAndFastighetIdIn(UUID projektId, Iterable<UUID> fastighetIds);

    @Query("SELECT a.fastighet.fastighetsbeteckning FROM AvtalEntity a WHERE a.id = :avtalId")
    String getFastighetsbeteckning(UUID avtalId);

    @Modifying
    @Transactional
    @Query("UPDATE AvtalEntity a SET a.skogsfastighet = :skogsfastighet WHERE a.id = :avtalId")
    Integer setSkogsfastighet(UUID avtalId, Boolean skogsfastighet);

    @Modifying
    @Transactional
    @Query("UPDATE AvtalEntity a SET a.tillvaratagandeTyp = :tillvaratagandeTyp WHERE a.id = :avtalId")
    Integer setTillvaratagandeTyp(UUID avtalId, TillvaratagandeTypDto tillvaratagandeTyp);

    @Query("SELECT a.skogsfastighet FROM AvtalEntity a WHERE a.id = :avtalId")
    Boolean getSkogsfastighet(UUID avtalId);

    @Query("SELECT a.tillvaratagandeTyp FROM AvtalEntity a WHERE a.id = :avtalId")
    TillvaratagandeTypDto getTillvaratagandeTyp(UUID avtalId);

    @Query("SELECT p.kundId FROM ProjektEntity p JOIN p.avtal av WHERE av.id = :avtalId")
    String getKundId(UUID avtalId);

    @Query("SELECT av.id " +
        "     FROM AvtalEntity av " +
        "     JOIN av.fastighet f " +
        "    WHERE f.fastighetsbeteckning = :fastighetsbeteckning " +
        "      AND f.kommunnamn = :kommun " +
        "      AND av.projekt.id = :projektId")
    UUID getIdByFastighetsbeteckningAndKommun(UUID projektId, String fastighetsbeteckning, String kommun);

    @Query("SELECT CASE WHEN COUNT(av) > 0 THEN true ELSE false END " +
        "     FROM AvtalEntity av " +
        "     JOIN av.avtalsparter ap " +
        "     JOIN ap.markagare m " +
        "    WHERE m.agareSaknas = true " +
        "      AND av.fastighet.id = :fastighetId " +
        "      AND av.projekt.id = :projektId")
    boolean agareSaknas(UUID projektId, UUID fastighetId);

    @Query("SELECT av.id " +
        "FROM AvtalEntity av " +
        "JOIN ImportVersionEntity iv " +
        "ON iv.projekt.id = av.projekt.id " +
        "WHERE iv.id = :versionId")
    List<UUID> findByVersionId(UUID versionId);

    @Modifying
    @Query(value =
        "UPDATE avtal " +
            "   SET avtalsstatus = COALESCE(markkoll.\"getStatusForFastighet\"(projekt_id, fastighet_id), :defaultAvtalsstatus) " +
            " WHERE id IN :avtalIds ",
        nativeQuery = true)
    void refreshAvtalsstatus(Collection<UUID> avtalIds, String defaultAvtalsstatus);

    @Modifying
    @Query(value =
        "UPDATE avtal " +
        "   SET avtalsstatus = COALESCE(markkoll.\"getStatusForFastighet\"(projekt_id, fastighet_id), 'EJ_BEHANDLAT') " +
        " WHERE id = :avtalId ",
        nativeQuery = true)
    void refreshAvtalsstatus(UUID avtalId);

    @Query("SELECT av.id " +
        "FROM AvtalEntity av " +
        "WHERE av.projekt.id = :projektId " +
        "AND av.fastighet.id IN :fastighetIds")
    List<UUID> findAllIdsByProjektIdAndFastighetIdIn(UUID projektId, Iterable<UUID> fastighetIds);

    @Query("SELECT f.lan                  AS lan," +
        "          f.kommunnamn           AS kommun," +
        "          f.id                   AS fastighetsnummer,  " +
        "          f.fastighetsbeteckning AS fastighet," +
        "          m.andel                AS andel," +
        "          m.agartyp              AS agartyp," +
        "          per                    AS person, " +
        "          ap.id                  AS avtalspartId," +
        "          av.id                  AS avtalId," +
        "          p.id                   AS sokId" +
        "     FROM FastighetEntity f" +
        "     JOIN AvtalEntity av ON av.fastighet.id = f.id " +
        "     JOIN AvtalspartEntity ap ON av.id = ap.avtal.id" +
        "     JOIN MarkagareEntity m ON m.id = ap.markagare.id " +
        "     JOIN PersonEntity per ON m.person.id = per.id" +
        "     JOIN ProjektEntity p ON av.projekt.id = p.id" +
        "    WHERE av.id IN :avtalIds" +
        "      AND av.skogsfastighet = true ")
    List<HaglofExportView> getHaglofExport(Iterable<UUID> avtalIds);

    /* Fick göra det här till en enskild native query istället för att hämta mottagarreferensen i resten av
    * Haglöf-exporten. Trots left joins m.m. så fick jag inga rader tillbaka om inte alla avtal hade en kontaktperson
    * tilldelad. Något tjorv med att man får göra en join två gånger på samma tabell? */
    @Query(value = "SELECT CAST(ap.id AS VARCHAR) AS avtalspartId, " +
        "                  kper.namn AS mottagarreferens" +
        "             FROM avtal av" +
        "             JOIN avtalspart ap ON ap.avtal_id = av.id" +
        "        LEFT JOIN kontaktperson kp ON av.id = kp.avtal_id " +
        "             JOIN avtalspart kap ON kp.avtalspart_id = kap.id " +
        "             JOIN markagare m ON kap.markagare_id = m.id" +
        "             JOIN person kper ON m.person_id = kper.id " +
        "            WHERE av.id IN :avtalIds",
        nativeQuery = true)
    List<MottagarreferensView> getMottagarreferenser(Iterable<UUID> avtalIds);

    List<AvtalIdAvtalsstatus> findByProjektIdAndFastighetIdIn(UUID projektId, Iterable<UUID> fastighetIds);

    @Query("SELECT f.id                   AS fastighetsId," +
        "          f.fastighetsbeteckning AS fastighetsbeteckning," +
        "          vp.metadata            AS elnatVpMetadata," +
        "          av.metadata            AS avtalMetadata" +
        "     FROM AvtalEntity av" +
        "     JOIN FastighetEntity f" +
        "       ON av.fastighet.id = f.id " +
        "     JOIN av.varderingsprotokoll vp" +
        "    WHERE av.id IN :avtalIds")
    List<EditElnatVpView> getEditElnatVps(Iterable<UUID> avtalIds);

    @Modifying
    @Query("UPDATE AvtalEntity av" +
        "      SET av.metadata.stationsnamn = :#{#avtalMetadata.stationsnamn}," +
        "          av.metadata.matandeStation = :#{#avtalMetadata.matandeStation}, " +
        "          av.metadata.franStation = :#{#avtalMetadata.franStation}, " +
        "          av.metadata.tillStation = :#{#avtalMetadata.tillStation}, " +
        "          av.metadata.markslag = :#{#avtalMetadata.markslag} " +
        "    WHERE av.id = :avtalId")
    void updateMetadata(UUID avtalId, AvtalMetadataDto avtalMetadata);

    @Query("SELECT av.id AS avtalId," +
        "          av.fastighet.id AS fastighetId" +
        "      FROM AvtalEntity av" +
        "      JOIN av.fastighet f " +
        "     WHERE av.projekt.id = :projektId " +
        "       AND av.fastighet.id IN :fastighetIds")
    List<AvtalFastighetView> getFastighetAvtalIds(UUID projektId, Iterable<UUID> fastighetIds);
}
