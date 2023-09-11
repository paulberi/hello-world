package se.metria.matdatabas.service.kartlager;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.matdatabas.service.kartlager.entity.KartlagerfilEntity;

import java.util.UUID;

public interface KartlagerfilRepository extends JpaRepository<KartlagerfilEntity, UUID> {
}
