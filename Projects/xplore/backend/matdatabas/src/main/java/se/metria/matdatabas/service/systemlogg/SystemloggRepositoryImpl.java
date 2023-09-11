package se.metria.matdatabas.service.systemlogg;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import se.metria.matdatabas.service.anvandare.model.AnvandareEntity;
import se.metria.matdatabas.service.anvandare.model.AnvandareEntity_;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity_;

@Repository
public class SystemloggRepositoryImpl implements SystemloggRepositoryCustom {

	private EntityManager em;
		
	public SystemloggRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	public Map<String, String> getAnvandare(Set<Integer> anvIds) {
		var builder = em.getCriteriaBuilder();
		var query = builder.createTupleQuery();
		var root = query.from(AnvandareEntity.class);
		query.select(builder.tuple(root.get(AnvandareEntity_.id), root.get(AnvandareEntity_.namn)))
			.where(root.get(AnvandareEntity_.id).in(anvIds));
	    return em.createQuery(query).getResultList().stream()		
			.collect(toMap(t -> "#" + t.get(0, Integer.class), t -> t.get(1, String.class)));
	}
}
