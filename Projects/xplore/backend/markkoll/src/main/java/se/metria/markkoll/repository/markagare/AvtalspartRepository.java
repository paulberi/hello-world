package se.metria.markkoll.repository.markagare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.repository.avtal.AvtalIdStatusUtbetalningsdatum;
import se.metria.markkoll.service.dokument.forteckninggenerator.ForteckningGeneratorData;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvtalspartRepository extends JpaRepository<AvtalspartEntity, UUID> {
    List<AvtalspartEntity> findAllByIdIn(Collection<UUID> avtalspartIds);

    @Query("SELECT a.kontaktperson.id FROM AvtalEntity a WHERE a.projekt.id = :projektId AND a.fastighet.id = :fastighetId")
    Optional<UUID> findKontaktpersonId(UUID projektId, UUID fastighetId);

    @Query("SELECT a.kontaktperson " +
             "FROM AvtalEntity a " +
             "JOIN AvtalspartEntity ap " +
               "ON ap.avtal.id = a.id " +
            "WHERE a.id = :avtalId")
    Optional<AvtalspartEntity> findKontaktperson(UUID avtalId);

    @Deprecated
    @Query("SELECT a From AvtalspartEntity a WHERE a.avtal.projekt.id = :projektId AND a.avtal.fastighet.id = :fastighetId")
    List<AvtalspartEntity> findByProjektIdAndFastighetId(UUID projektId, UUID fastighetId);

    @Query("SELECT av.projekt.id " +
             "FROM AvtalEntity av " +
             "JOIN AvtalspartEntity ap " +
               "ON ap.avtal.id = av.id " +
            "WHERE ap.id = :avtalspartId")
    UUID getProjektId(UUID avtalspartId);

    List<AvtalspartEntity> findByAvtalId(UUID avtalId);

    @Query("SELECT av.markagare.person.personnummer FROM AvtalspartEntity av WHERE av.id = :avtalspartId")
    String getPersonnnummer(UUID avtalspartId);

    @Query("SELECT av.avtal.id FROM AvtalspartEntity av WHERE av.id = :avtalspartId")
    UUID getAvtalId(UUID avtalspartId);

    @Query(" SELECT ap " +
        "      FROM AvtalspartEntity ap " +
        "JOIN FETCH ap.markagare m " +
        "JOIN FETCH m.person p " +
        "     WHERE ap.avtal.id IN :avtalIds")
    List<AvtalspartEntity> getAvtalsparter(Collection<UUID> avtalIds);

    @Query("SELECT ap FROM AvtalspartEntity ap " +
        "     JOIN ap.avtal av " +
        "    WHERE av.projekt.id = :projektId " +
        "      AND av.fastighet.id IN :fastighetIds")
    List<AvtalspartEntity> setAvtalsstatus(UUID projektId, Iterable<UUID> fastighetIds);

    List<AvtalIdStatusUtbetalningsdatum> findByAvtalIdIn(Iterable<UUID> avtalIds);

    @Modifying
    @Query("UPDATE AvtalspartEntity ap SET ap.avtalsstatus = :avtalsstatus WHERE ap.id IN :avtalspartIds")
    void setAvtalsstatus(Iterable<UUID> avtalspartIds, AvtalsstatusDto avtalsstatus);

    @Modifying
    @Query("UPDATE AvtalspartEntity ap SET ap.utbetalningsdatum = :utbetalningsdatum WHERE ap.id IN :avtalspartIds")
    void setUtbetalningsdatum(Iterable<UUID> avtalspartIds, LocalDate utbetalningsdatum);

    @Query("SELECT ap " +
        "     FROM AvtalspartEntity ap" +
        "     JOIN FETCH ap.markagare ma" +
        "     JOIN FETCH ma.styrelsemedlem sm " +
        "    WHERE ma.kundId = :kundId" +
        "      AND ma.fastighet.id = :samfId" +
        "      AND ma.agartyp = 'STYRELSEMEDLEM'")
    List<AvtalspartEntity> findStyrelsemedlemmar(String kundId, UUID samfId);

    @Query("SELECT p.postort      AS mottagarePostort," +
        "          p.adress       AS mottagareAdress, " +
        "          p.postnummer   AS mottagarePostnummer,  " +
        "          p.personnummer AS mottagareOrganisationsnummer," +
        "          p.namn         AS mottagareNamn, " +
        "          ''             AS mottagareIban10," +
        "          ''             AS bankLand," +
        "          p.bankkonto    AS mottagareBankkonto," +
        "          ''             AS mottagareBanknamn," +
        "          ''             AS mottagareIban," +
        "          ''             AS mottagareSwift" +
        "     FROM AvtalspartEntity ap" +
        "     JOIN ap.markagare m" +
        "     JOIN m.person p" +
         "   WHERE ap.avtal.id IN :avtalIds")
    List<ForteckningGeneratorData> getForteckningGeneratorData(Iterable<UUID> avtalIds);
}
