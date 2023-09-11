package se.metria.finfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.finfo.entity.registerenhet.RegisterenhetEntity;

import java.util.Optional;
import java.util.UUID;

public interface RegisterenhetRepository extends JpaRepository<RegisterenhetEntity, UUID> {
    Optional<RegisterenhetEntity> findTopByUuidOrderByImporteradDatumDesc(UUID registerenhetId);
}
