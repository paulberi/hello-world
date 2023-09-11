package se.metria.matdatabas.service.matningstyp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatningstypRepository extends JpaRepository<MatningstypEntity, Integer> {
	Integer countByMatansvarigAnvandargruppId(Integer gruppId);
	Integer countByDefinitionMatningstypId(Integer definitionMatningstypId);
	List<MatningstypEntity> findByMatobjektId(Integer matobjektId);
	
	@Query("SELECT m.id FROM MatningstypEntity m where m.matobjektId = :matobjektId")
	List<Integer> findIdsByMatobjektId(Integer matobjektId);

	List<MatningstypEntity> findByInstrument(String instrument);

	Optional<MatningstypEntity> findByDefinitionMatningstypId(Integer definitionMatningstypId);

	boolean existsByIdAndMatobjektId(Integer id, Integer matobjektId);
}
