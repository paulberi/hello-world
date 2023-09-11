package se.metria.markkoll.repository.vardering.bilaga.akernorm74;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.akernorm74.HinderEntity;

import java.util.UUID;

public interface HinderRepository extends JpaRepository <HinderEntity, UUID>{
}
