package se.metria.matdatabas.service.paminnelse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.paminnelse.dto.Paminnelse;
import se.metria.matdatabas.service.paminnelse.query.PaminnelseSearchFilter;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaminnelseService {

	private PaminnelseJooqRepository paminnelseJooqRepository;
	private AnvandargruppService anvandargruppService;

	public PaminnelseService(PaminnelseJooqRepository paminnelseJooqRepository,
							 AnvandargruppService anvandargruppService) {
		this.paminnelseJooqRepository = paminnelseJooqRepository;
		this.anvandargruppService = anvandargruppService;
	}

	public Integer getForsenadeMatningarCountForAnvandare(Integer anvandarId) {
		List<Integer> anvandargrupper = anvandargruppService.getAnvandargrupperForAnvandare(anvandarId);
		if (anvandargrupper.isEmpty()) {
			return 0;
		}
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder()
				.matansvarigAnvandargruppIds(anvandargrupper)
				.build();
		PaminnelseSearchFilter paminnelseSearchFilter = PaminnelseSearchFilter.builder()
				.onlyForsenade(true)
				.build();
		return paminnelseJooqRepository.getPaminnelserList(matningstypSearchFilter, paminnelseSearchFilter).size();
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Page<Paminnelse> getMatningstypPaminnelsePage(MatningstypSearchFilter matningstypSearchFilter,
														 PaminnelseSearchFilter paminnelseSearchFilter,
														 Pageable pageable) {
		return paminnelseJooqRepository.getPaminnelserPage(matningstypSearchFilter,
				paminnelseSearchFilter, pageable).map(Paminnelse::new);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public List<Paminnelse> getMatningstypPaminnelseList(MatningstypSearchFilter matningstypSearchFilter,
														 PaminnelseSearchFilter paminnelseSearchFilter) {
		return paminnelseJooqRepository.getPaminnelserList(matningstypSearchFilter,
				paminnelseSearchFilter).stream().map(Paminnelse::new).collect(Collectors.toList());
	}
}
