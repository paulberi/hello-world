package se.metria.matdatabas.service.analys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.analys.entity.AnalysEntity;

import java.time.LocalDateTime;

@Repository
public interface AnalysRepository extends JpaRepository<AnalysEntity, Integer> {

	Boolean existsByMatobjektIdAndAnalysDatum(Integer matobjektId, LocalDateTime analysDatum);

	Page<AnalysEntity> findAllByMatobjektId(Integer matobjektId, Pageable pageable);
}
