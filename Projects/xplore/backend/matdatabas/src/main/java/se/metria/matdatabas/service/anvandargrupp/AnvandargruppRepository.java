package se.metria.matdatabas.service.anvandargrupp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.anvandargrupp.entity.AnvandargruppEntity;

@Repository
public interface AnvandargruppRepository extends JpaRepository<AnvandargruppEntity, Integer> {
	@Query(value = "SELECT ag FROM AnvandargruppEntity ag ORDER BY ag.anvandareCount ASC",
			countQuery = "SELECT COUNT(ag) FROM AnvandargruppEntity ag")
	Page<AnvandargruppEntity> findAllByAnvandareCountAsc(Pageable pageable);

	@Query(value = "SELECT ag FROM AnvandargruppEntity ag ORDER BY ag.anvandareCount DESC",
			countQuery = "SELECT COUNT(ag) FROM AnvandargruppEntity ag")
	Page<AnvandargruppEntity> findAllByAnvandareCountDesc(Pageable pageable);

	Boolean existsByNamn(String namn);
}
