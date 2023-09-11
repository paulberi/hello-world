package se.metria.matdatabas.service.analys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.analys.dto.Analys;
import se.metria.matdatabas.service.analys.dto.EditAnalys;
import se.metria.matdatabas.service.analys.entity.AnalysEntity;
import se.metria.matdatabas.service.analys.exception.AnalysConflictException;
import se.metria.matdatabas.service.analys.exception.AnalysNotFoundException;
import se.metria.matdatabas.service.analys.exception.AnalysSaveMatningException;
import se.metria.matdatabas.service.bifogadfil.BifogadfilService;
import se.metria.matdatabas.service.bifogadfil.dto.BifogadfilInfo;
import se.metria.matdatabas.service.bifogadfil.exception.BifogadfilNotFoundException;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.Matningsfelkod;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
public class AnalysService {
	private AnalysRepository repository;
	private MatningService matningService;
	private MatningstypService matningstypService;
	private BifogadfilService bifogadfilService;

	public AnalysService(AnalysRepository repository, MatningService matningService,
						 MatningstypService matningstypService, BifogadfilService bifogadfilService) {
		this.repository = repository;
		this.matningService = matningService;
		this.matningstypService = matningstypService;
		this.bifogadfilService = bifogadfilService;
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Page<Analys> getAnalyser(Integer matobjektId, Integer page, Integer pageSize,
									String sortProperty, Sort.Direction sortDirection) {
		PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		var analysPage = repository.findAllByMatobjektId(matobjektId, pageRequest);
		var rapportIds = analysPage.getContent().stream()
				.flatMap(h -> h.getRapportIds().stream())
				.collect(toSet());
		var bifogadfilMap = getBifogadfilMap(rapportIds);
		return analysPage.map(h -> Analys.fromEntity(h, bifogadfilMap));
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Analys getAnalys(Integer id) throws AnalysNotFoundException {
		AnalysEntity analysEntity = this.findAnalys(id);
		var bifogadfilMap = getBifogadfilMap(analysEntity.getRapportIds());
		Analys analys = Analys.fromEntity(analysEntity, bifogadfilMap);
		List<Matning> matningar = this.loadAnalysMatningar(analys);
		analys.setMatningar(matningar);
		return analys;
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Analys createAnalys(EditAnalys editAnalys) throws AnalysConflictException, AnalysSaveMatningException {
		if (exists(editAnalys.getMatobjektId(), editAnalys.getAnalysDatum())) {
			throw new AnalysConflictException();
		}
		List<Matning> matningar = this.saveAnalysMatningar(editAnalys);
		var created = repository.save(new AnalysEntity(editAnalys));
		var bifogadfilMap = getBifogadfilMap(created.getRapportIds());
		Analys analys = Analys.fromEntity(created, bifogadfilMap);
		analys.setMatningar(matningar);
		return analys;
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Analys updateAnalys(Integer id, EditAnalys editAnalys) throws AnalysNotFoundException, AnalysSaveMatningException {
		AnalysEntity update = this.findAnalys(id);
		List<Matning> matningar = this.saveAnalysMatningar(editAnalys);
		var deleteFiles = new HashSet<>(update.getRapportIds());
		deleteFiles.removeAll(editAnalys.getRapporter());
		update.update(editAnalys);
		repository.save(update);
		deleteBifogadefiler(deleteFiles);
		var bifogadfilMap = getBifogadfilMap(update.getRapportIds());
		Analys analys = Analys.fromEntity(update, bifogadfilMap);
		analys.setMatningar(matningar);
		return analys;
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public boolean exists(Integer matobjektId, LocalDateTime analysDatum) {
		return repository.existsByMatobjektIdAndAnalysDatum(matobjektId, analysDatum);
	}

	private AnalysEntity findAnalys(Integer id) throws AnalysNotFoundException {
		return repository.findById(id).orElseThrow(AnalysNotFoundException::new);
	}

	private List<Matning> saveAnalysMatningar(EditAnalys editAnalys) throws AnalysSaveMatningException {
		return editAnalys.getMatningar().stream().map(matning -> {
			Matningstyp matningstyp = this.matningstypService.findById(matning.getMatningstypId()).orElseThrow(AnalysSaveMatningException::new);
			var saveMatning = SaveMatning.builder()
					.avlastDatum(editAnalys.getAnalysDatum())
					.avlastVarde(matning.getAvlastVarde())
					.felkod(Matningsfelkod.OK)
					.kommentar(editAnalys.getKommentar())
					.rapportor(editAnalys.getRapportor())
					.inomDetektionsomrade(matning.getInomDetektionsomrade())
					.build();
			try {
				return matningService.create(matningstyp, saveMatning, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
			} catch (Exception e) {
				throw new AnalysSaveMatningException();
			}
		}).collect(Collectors.toList());
	}

	private List<Matning> loadAnalysMatningar(Analys analys) {
		List<Matning> matningar = new ArrayList<>();
		List<Matningstyp> matningstypList = matningstypService.getMatningstyperForMatobjekt(analys.getMatobjektId());
		matningstypList.forEach(matningstyp -> {
			Matning matning = matningService.getMatningByMatningstypAndDatum(matningstyp, analys.getAnalysDatum());
			if (matning != null) {
				matningar.add(matning);
			}
		});
		return matningar;
	}

	private void deleteBifogadefiler(Set<UUID> filIds) {
		try {
			if (!filIds.isEmpty()) {
				bifogadfilService.deleteBifogadefiler(new ArrayList<>(filIds));
			}
		} catch (BifogadfilNotFoundException e) {
			// Leave it...
		}
	}

	private Map<UUID, BifogadfilInfo> getBifogadfilMap(Set<UUID> bildIds) {
		return bifogadfilService.getBifogadfilInfos(bildIds).stream()
				.collect(toMap(BifogadfilInfo::getId, b -> b));
	}
}
