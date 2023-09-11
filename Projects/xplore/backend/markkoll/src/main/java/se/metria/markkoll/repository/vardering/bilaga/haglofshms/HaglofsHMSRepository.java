package se.metria.markkoll.repository.vardering.bilaga.haglofshms;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.haglofshms.HaglofsHMSEntity;

import java.util.UUID;

public interface HaglofsHMSRepository extends JpaRepository<HaglofsHMSEntity, UUID> {
}
