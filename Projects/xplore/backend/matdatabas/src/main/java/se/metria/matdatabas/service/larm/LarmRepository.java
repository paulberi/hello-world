package se.metria.matdatabas.service.larm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.larm.entity.LarmEntity;

import java.util.Collection;
import java.util.List;

@Repository
public interface LarmRepository extends JpaRepository<LarmEntity, Long> {
	Integer countByStatusAndGransvardeId(Short status, Integer gransvardeId);
	Integer countByGransvardeId(Integer gransvardeId);
	List<LarmEntity> findAllByMatningId(Long matningId);
}
