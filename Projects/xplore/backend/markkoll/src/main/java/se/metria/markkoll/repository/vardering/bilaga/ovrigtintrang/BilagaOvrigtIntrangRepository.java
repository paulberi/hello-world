package se.metria.markkoll.repository.vardering.bilaga.ovrigtintrang;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.ovrigtintrang.BilagaOvrigtIntrangEntity;

import java.util.UUID;

public interface BilagaOvrigtIntrangRepository
    extends JpaRepository<BilagaOvrigtIntrangEntity, UUID> {
}
