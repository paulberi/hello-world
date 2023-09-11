package se.metria.matdatabas.service.matning;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import se.metria.matdatabas.service.definitionmatningstyp.entity.DefinitionMatningstypEntity;
import se.metria.matdatabas.service.matning.entity.MatningEntity;
import se.metria.matdatabas.service.matning.entity.MatningEntity_;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity_;

@Repository
public class MatningRepositoryImpl implements MatningRepositoryCustom {

	private EntityManager em;
	
	public MatningRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<MatningEntity> findByOtherMatningstyp(Matningstyp matningstyp, DefinitionMatningstypEntity otherMatningstyp, LocalDateTime from, LocalDateTime to) {
		var builder = em.getCriteriaBuilder();
		var query = builder.createQuery(MatningEntity.class);
		var root = query.from(MatningstypEntity.class);
		var joinMatning = root.join(MatningstypEntity_.matningar);
		query.select(joinMatning)
			.where(builder.and(
				builder.equal(root.get(MatningstypEntity_.matobjektId), matningstyp.getMatobjektId()),
				builder.equal(root.get(MatningstypEntity_.definitionMatningstyp), otherMatningstyp),
				builder.between(joinMatning.get(MatningEntity_.avlastDatum), from, to)
			));
		return em.createQuery(query).getResultList();
	}
}
