
package se.metria.markkoll.repository.vardering.fiber;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.fiber.FiberOvrigIntrangsersattningEntity;

import java.util.UUID;

public interface FiberOvrigIntrangsersattningRepository extends JpaRepository<FiberOvrigIntrangsersattningEntity, UUID> {
}