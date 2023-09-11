package se.metria.matdatabas.service.anvandare;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.anvandare.model.AnvandareEntity;

import java.time.LocalDateTime;

@Repository
public interface AnvandareRepository extends JpaRepository<AnvandareEntity, Integer> {
	Page<AnvandareEntity> findAllByAktivTrue(Pageable pageable);
	Boolean existsByInloggningsnamn(String inloggningsnamn);
	Boolean existsByDefaultKartlagerId(Integer id);
	Page<AnvandareEntity> findAllByAnvandargrupperId(Integer id, Pageable pageable);
	Integer countByAnvandargrupperId(Integer id);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE matdatabas.anvandare SET inloggad_senast = :inloggadSenast WHERE id = :id", nativeQuery = true)
	void updateInloggadSenast(@Param("inloggadSenast") LocalDateTime inloggadSenast, @Param("id") Integer id);
}
