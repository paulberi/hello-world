package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.mapcms.entity.ProjektEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjektRepository extends JpaRepository<ProjektEntity, UUID> {

    @Query("SELECT p FROM ProjektEntity p WHERE p.kund.id = :kundId and p.id = :projektId")
    public Optional<ProjektEntity> getProjektForKund(UUID kundId, UUID projektId);

    @Query("SELECT p FROM ProjektEntity p WHERE p.kund.id = :kundId")
    public Optional<List<ProjektEntity>> listProjektForKund(UUID kundId);

    @Query("SELECT p FROM ProjektEntity p WHERE p.kund.id = :kundId " +
            "and p.publiceringStatus in " +
            "(se.metria.mapcms.openapi.model.PubliceringStatusDto.PUBLICERAD, " +
            "se.metria.mapcms.openapi.model.PubliceringStatusDto.ARKIVERAD)")
    public Optional<List<ProjektEntity>> listPublikaProjektForKund(UUID kundId);

    @Query("SELECT p FROM ProjektEntity p WHERE p.kund.id = :kundId and p.slug = :slug")
    public Optional<ProjektEntity> getProjektForKundBySlug(UUID kundId, String slug);

    @Query("SELECT p FROM ProjektEntity p WHERE  p.slug = :slug")
    public Optional<ProjektEntity> getProjektForKundBySlugOnly(String slug);

    @Query("SELECT count(p) > 0 FROM ProjektEntity p WHERE p.kund.id = :kundId AND p.id = :projektId")
    public boolean checkIfProjektExists(UUID kundId, UUID projektId);
}
