package se.metria.markkoll.repository.vardering.bilaga.enstakaTrad;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.enstakatrad.VaganlaggningEntity;

import java.util.UUID;

public interface VaganlaggningRepository extends JpaRepository<VaganlaggningEntity, UUID> {
}
