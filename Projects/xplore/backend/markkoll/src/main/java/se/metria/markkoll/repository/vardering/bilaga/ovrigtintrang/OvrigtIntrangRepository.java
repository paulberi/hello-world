package se.metria.markkoll.repository.vardering.bilaga.ovrigtintrang;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.ovrigtintrang.OvrigtIntrangEntity;

import java.util.UUID;

public interface OvrigtIntrangRepository extends JpaRepository<OvrigtIntrangEntity, UUID> {
}
