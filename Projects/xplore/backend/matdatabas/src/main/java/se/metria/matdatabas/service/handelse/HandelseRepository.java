package se.metria.matdatabas.service.handelse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.metria.matdatabas.service.handelse.entity.HandelseEntity;

import java.util.List;

@Repository
interface HandelseRepository extends JpaRepository<HandelseEntity, Integer> {
	Page<HandelseEntity> findByMatobjektId(Integer matobjektId, Pageable pageable);
	List<HandelseEntity> findByMatobjektId(Integer matobjektId);
}
