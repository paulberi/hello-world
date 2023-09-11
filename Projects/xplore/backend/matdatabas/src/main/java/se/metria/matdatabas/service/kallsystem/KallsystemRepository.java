package se.metria.matdatabas.service.kallsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.metria.matdatabas.service.kallsystem.entity.KallsystemEntity;

public interface KallsystemRepository extends JpaRepository<KallsystemEntity, String>, JpaSpecificationExecutor<KallsystemEntity> {
}
