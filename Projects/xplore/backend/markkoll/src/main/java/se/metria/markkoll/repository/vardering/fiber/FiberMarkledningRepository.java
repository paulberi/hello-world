package se.metria.markkoll.repository.vardering.fiber;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.fiber.FiberMarkledningEntity;

import java.util.UUID;

public interface FiberMarkledningRepository extends JpaRepository<FiberMarkledningEntity, UUID> {
}
