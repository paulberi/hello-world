package se.metria.markkoll.repository.indata;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.indata.KallfilEntity;

import java.util.UUID;

public interface KallfilRepository extends JpaRepository<KallfilEntity, UUID> {
}
