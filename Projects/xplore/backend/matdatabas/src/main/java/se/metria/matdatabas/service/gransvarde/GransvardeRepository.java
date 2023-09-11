package se.metria.matdatabas.service.gransvarde;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.gransvarde.entity.GransvardeEntity;

import java.util.List;

@Repository
public interface GransvardeRepository extends JpaRepository<GransvardeEntity, Integer> {
	Integer countByLarmnivaId(Integer larmnivaId);
	List<Integer> findIdByMatningstypId(Integer matningstypId);
	void deleteByMatningstypId(Integer matningstypId);
}
