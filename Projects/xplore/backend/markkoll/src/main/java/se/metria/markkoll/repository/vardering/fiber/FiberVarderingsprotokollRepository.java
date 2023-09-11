package se.metria.markkoll.repository.vardering.fiber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;

import javax.persistence.Table;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Table(name = "varderingsprotokoll", schema = "FIBER")
public interface FiberVarderingsprotokollRepository extends JpaRepository<FiberVarderingsprotokollEntity, UUID> {

    @Query("SELECT vp FROM FiberVarderingsprotokollEntity vp WHERE vp.avtal.id = :avtalId")
    Optional<FiberVarderingsprotokollEntity> getWithAvtalId(UUID avtalId);

    @Query("SELECT vp.avtal.id FROM FiberVarderingsprotokollEntity vp where vp.id = :vpId")
    UUID getAvtalId(UUID vpId);

    Optional<FiberVarderingsprotokollEntity> findByAvtalId(UUID avtalId);

    List<FiberVarderingsprotokollEntity> findAllByAvtalIdIn(Iterable<UUID> avtalIds);
}
