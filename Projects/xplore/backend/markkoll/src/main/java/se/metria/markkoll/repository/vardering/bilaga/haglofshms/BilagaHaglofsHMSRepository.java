package se.metria.markkoll.repository.vardering.bilaga.haglofshms;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.haglofshms.BilagaHaglofsHMSEntity;

import java.util.UUID;

public interface BilagaHaglofsHMSRepository
    extends JpaRepository<BilagaHaglofsHMSEntity, UUID> {
}
