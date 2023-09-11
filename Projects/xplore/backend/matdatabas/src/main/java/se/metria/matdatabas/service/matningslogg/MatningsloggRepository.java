package se.metria.matdatabas.service.matningslogg;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.metria.matdatabas.service.matningslogg.entity.MatningsloggEntity;

@Repository
public interface MatningsloggRepository extends JpaRepository<MatningsloggEntity, Long> {
	Page<MatningsloggEntity> findAllByMatningId(Long matningId, Pageable pageable);
}
