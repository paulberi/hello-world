package se.metria.matdatabas.service.systemlogg;

import java.util.Map;
import java.util.Set;

public interface SystemloggRepositoryCustom {
	
	public Map<String, String> getAnvandare(Set<Integer> anvIds);

}
