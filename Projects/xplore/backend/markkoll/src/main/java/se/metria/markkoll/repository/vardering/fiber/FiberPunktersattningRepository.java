package se.metria.markkoll.repository.vardering.fiber;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.fiber.FiberPunktersattningEntity;

import java.util.UUID;

public interface FiberPunktersattningRepository extends JpaRepository<FiberPunktersattningEntity, UUID> {
}
