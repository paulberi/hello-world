package se.metria.matdatabas.service.matningstyp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypRepository;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.handelse.HandelseService;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matningstyp.dto.*;
import se.metria.matdatabas.service.matningstyp.entity.MatningstypEntity;
import se.metria.matdatabas.service.matningstyp.exception.MatningstypHasMatningarException;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.matrunda.MatrundaService;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatningstypService {
	private MatningstypRepository repository;
	private MatansvarRepository matansvarRepository;
	private MatningService matningService;
	private DefinitionMatningstypService definitionMatningstypService;
	private HandelseService handelseService;
	private GransvardeService gransvardeService;
	private MatrundaService matrundaService;
	private MatningstypJooqRepository matningstypJooqRepository;
	private DefinitionMatningstypRepository definitionMatningstypRepository;

	public MatningstypService(MatningstypRepository repository,
							  MatansvarRepository matansvarRepository,
							  MatningService matningService,
							  DefinitionMatningstypService definitionMatningstypService,
							  HandelseService handelseService,
							  GransvardeService gransvardeService,
							  MatrundaService matrundaService,
							  MatningstypJooqRepository matningstypJooqRepository,
							  DefinitionMatningstypRepository definitionMatningstypRepository) {
		this.repository = repository;
		this.matansvarRepository = matansvarRepository;
		this.matningService = matningService;
		this.definitionMatningstypService = definitionMatningstypService;
		this.handelseService = handelseService;
		this.gransvardeService = gransvardeService;
		this.matrundaService = matrundaService;
		this.matningstypJooqRepository = matningstypJooqRepository;
		this.definitionMatningstypRepository = definitionMatningstypRepository;
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Page<Matansvar> getMatansvarForAnvandargrupp(Integer anvandargruppId, Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection) {
		PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		var matansvar = matansvarRepository.findAllByMatansvarigAnvandargruppId(anvandargruppId, pageRequest);
		return matansvar.map(Matansvar::fromEntity);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Integer getMatansvarForAnvandargruppCount(Integer anvandargruppId) {
		return repository.countByMatansvarigAnvandargruppId(anvandargruppId);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public List<Matningstyp> getMatningstyperForMatobjekt(Integer matobjektId) {
		List<MatningstypEntity> matningstyper = repository.findByMatobjektId(matobjektId);
		return matningstyper.stream()
				.map(entity -> {
					Optional<Matning> latestMatning = matningService.getLatestMatningForMatningstyp(entity.getId());
					Matningstyp matningstyp = Matningstyp.fromEntity(entity);
					latestMatning.ifPresent(matningstyp::setLatestMatning);
					return matningstyp;
				})
				.collect(Collectors.toList());
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Optional<Matningstyp> findById(Integer id) {
		return repository.findById(id).map(Matningstyp::fromEntity);
	}
	
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public List<Matningstyp> findByInstrument(String instrument) {
		return repository.findByInstrument(instrument).stream()
				.map(Matningstyp::fromEntity)
				.collect(Collectors.toList());
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public boolean canDelete(Integer id) {
		return !matningService.hasMatningar(id);
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Transactional
	public void deleteById(Integer id) throws MatningstypHasMatningarException {
		if (!matningService.hasMatningar(id)) {
			gransvardeService.deleteByMatningstypId(id);
			matrundaService.removeFromMatrundor(id);
			repository.deleteById(id);
		} else {
			throw new MatningstypHasMatningarException();
		}
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Transactional
	public void deleteByMatobjektId(Integer matobjektId) throws MatningstypHasMatningarException {
		List<Integer> ids = repository.findIdsByMatobjektId(matobjektId);
		for (Integer id : ids) {
			deleteById(id);
		}
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Transactional
	public void save(Integer id, SaveMatningstyp save) {
		var existingMatningstyp = repository.findById(id).orElseThrow();
		var definition = existingMatningstyp.getDefinitionMatningstyp();

		existingMatningstyp.update(save, definition);

		if (!existingMatningstyp.getChangeLog().isEmpty()) {
			handelseService.addHandelseAndradeBerakningar(existingMatningstyp.getMatobjektId(), definition.getNamn(), existingMatningstyp.getChangeLog());
		}
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matningstyp create(Integer matobjektId, Integer definitionMatningstypId, SaveMatningstyp save) {
		var definition = definitionMatningstypRepository.findById(definitionMatningstypId).orElseThrow();
		var matningstyp = new MatningstypEntity(matobjektId, save, definition);
		return Matningstyp.fromEntity(repository.save(matningstyp));
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Integer countByDefinitionMatningstypId(Integer id) {
		return repository.countByDefinitionMatningstypId(id);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Page<MatningstypMatobjekt> getMatningstypMatobjektPage(MatningstypSearchFilter filter, Pageable pageable) {
		return matningstypJooqRepository.matningstyperAntalEjGranskadePage(filter, pageable).map(MatningstypMatobjekt::new);
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public List<MatningstypMatobjekt> getMatningstypMatobjektList(MatningstypSearchFilter filter) {
		return matningstypJooqRepository.matningstyperAntalEjGranskadeList(filter).stream().map(MatningstypMatobjekt::new).collect(Collectors.toList());
	}

	public Page<MatningstypAntalMatningar> getMatningstyperAntalMatningar(MatningstypSearchFilter filter,
																		  LocalDateTime fromDatum, LocalDateTime tomDatum,
																		  Short matningStatus, Pageable pageable) {
		return matningstypJooqRepository.matningstyperAntalMatningar(filter, fromDatum, tomDatum, matningStatus, pageable)
				.map(MatningstypAntalMatningar::new);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public boolean existsInMatobjekt(Integer matobjektId, Integer id) {
		return repository.existsByIdAndMatobjektId(id, matobjektId);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Matningstyp findByDefinitionMatningstypId(Integer definitionId) {
		var optionalMatningstypEntity = repository.findByDefinitionMatningstypId(definitionId);
		return optionalMatningstypEntity.map(Matningstyp::fromEntity).orElse(null);
	}
}
