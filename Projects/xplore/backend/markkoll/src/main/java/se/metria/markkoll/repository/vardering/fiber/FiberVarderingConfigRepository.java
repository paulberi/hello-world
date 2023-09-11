package se.metria.markkoll.repository.vardering.fiber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.vardering.fiber.FiberVarderingConfigEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FiberVarderingConfigRepository extends JpaRepository<FiberVarderingConfigEntity, UUID> {

    @Query("SELECT cfg " +
        "     FROM FiberVarderingConfigEntity cfg " +
        "     JOIN cfg.varderingKundAgare vka " +
        "    WHERE vka.kund.id = :kundId " +
        "      AND vka.person.personnummer = :personnummer")
    Optional<FiberVarderingConfigEntity> getAgareConfig(String kundId, String personnummer);

    @Query("SELECT cfg " +
        "     FROM FiberVarderingConfigEntity cfg " +
        "     JOIN cfg.varderingKundAgare vka " +
        "    WHERE vka.kund.id = :kundId " +
        "      AND vka.person IS NULL")
    Optional<FiberVarderingConfigEntity> getKundDefaultConfig(String kundId);

    @Query("SELECT cfg " +
            "     FROM FiberVarderingConfigEntity cfg " +
            "     JOIN cfg.varderingKundAgare vka " +
            "    WHERE vka.kund.id = :kundId " +
            "      AND vka.person IS NOT NULL")
    List<FiberVarderingConfigEntity> getAllKundAvtalspartConfig(String kundId);

    @Query("SELECT cfg " +
        "     FROM FiberVarderingConfigEntity cfg " +
        "     JOIN cfg.varderingKundAgare vka " +
        "    WHERE vka.kund IS NULL " +
        "      AND vka.person IS NULL")
    Optional<FiberVarderingConfigEntity> getMetriaDefault();
}
