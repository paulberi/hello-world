package se.metria.markkoll.repository.projekt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.projekt.FiberProjektEntity;

import java.util.UUID;

public interface FiberProjektRepository extends JpaRepository<FiberProjektEntity, UUID> {
    @Query("SELECT p.varderingsprotokoll FROM FiberProjektEntity p WHERE p.id = :projektId")
    Boolean getShouldHaveVarderingsprotokoll(UUID projektId);
}
