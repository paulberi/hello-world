package se.metria.markkoll.repository.vardering.fiber;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.fiber.FiberIntrangAkerOchSkogsmarkEntity;

import java.util.UUID;

public interface FiberIntrangAkerOchSkogsmarkRepository extends JpaRepository<FiberIntrangAkerOchSkogsmarkEntity, UUID> {
}