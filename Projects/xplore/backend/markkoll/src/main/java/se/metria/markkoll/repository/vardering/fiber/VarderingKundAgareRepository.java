package se.metria.markkoll.repository.vardering.fiber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.vardering.fiber.VarderingKundAgareEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VarderingKundAgareRepository extends JpaRepository<VarderingKundAgareEntity, UUID> {

    @Query("SELECT cfg FROM VarderingKundAgareEntity cfg where cfg.kund.id = :kundId and cfg.person is not null")
    Optional<List<VarderingKundAgareEntity>> findAllByKundAndPersonNotNull(String kundId);

    @Query("SELECT cfg FROM VarderingKundAgareEntity cfg where cfg.kund.id = :kundId and cfg.person.id = :personId")
    Optional<VarderingKundAgareEntity> getByKundAndPerson(String kundId, UUID personId);

    @Query("SELECT cfg FROM VarderingKundAgareEntity cfg where cfg.kund.id = :kundId and cfg.varderingConfig.id = :varderingId")
    VarderingKundAgareEntity getByKundAndAndVarderingConfig(String kundId, UUID varderingId);
}
