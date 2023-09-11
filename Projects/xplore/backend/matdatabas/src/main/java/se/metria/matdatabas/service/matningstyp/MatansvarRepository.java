package se.metria.matdatabas.service.matningstyp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.matningstyp.entity.MatansvarEntity;

@Repository
public interface MatansvarRepository extends JpaRepository<MatansvarEntity, Integer> {
	Page<MatansvarEntity> findAllByMatansvarigAnvandargruppId(Integer anvandargruppId, Pageable pageable);
}

