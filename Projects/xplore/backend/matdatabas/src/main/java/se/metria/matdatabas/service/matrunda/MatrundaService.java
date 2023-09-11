package se.metria.matdatabas.service.matrunda;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matrunda.dto.EditMatrunda;
import se.metria.matdatabas.service.matrunda.dto.ListMatrunda;
import se.metria.matdatabas.service.matrunda.dto.Matrunda;
import se.metria.matdatabas.service.matrunda.entity.MatrundaEntity;
import se.metria.matdatabas.service.matrunda.exception.MatrundaConflictException;
import se.metria.matdatabas.service.matrunda.exception.MatrundaNotFoundException;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatrundaService {

	MatrundaRepository repository;
	MatrundaJooqRepository jooqRepository;
	private SystemloggService systemloggService;

	public MatrundaService(MatrundaRepository repository, MatrundaJooqRepository jooqRepository,SystemloggService systemloggService) {
		this.repository = repository;
		this.systemloggService = systemloggService;
		this.jooqRepository = jooqRepository;
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Page<ListMatrunda> getMatrundor(Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection) {
		return getMatrundor(page, pageSize, sortProperty, sortDirection, false);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Page<ListMatrunda> getMatrundor(Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection, boolean onlyAktiva) {
		var pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		if (onlyAktiva) {
			return repository.findAllByAktivTrue(pageRequest).map(ListMatrunda::fromEntity);
		} else {
			return repository.findAll(pageRequest).map(ListMatrunda::fromEntity);
		}
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Matrunda getMatrunda(Integer id) throws MatrundaNotFoundException {
		return Matrunda.fromEntity(this.findMatrunda(id));
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matrunda createMatrunda(EditMatrunda editMatrunda) throws MatrundaConflictException {
		if (exists(editMatrunda.getNamn())) {
			throw new MatrundaConflictException(editMatrunda.getNamn() + " already exists.");
		}

		var matrunda = persist(new MatrundaEntity(editMatrunda));

		systemloggService.addHandelseMatrundaCreated(matrunda.getId(), matrunda.getNamn());

		return Matrunda.fromEntity(matrunda);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matrunda updateMatrunda(Integer id, EditMatrunda editMatrunda) throws MatrundaNotFoundException {
		MatrundaEntity matrunda = findMatrunda(id);
		matrunda.update(editMatrunda);

		if (!matrunda.getChangeLog().isEmpty()) {
			systemloggService.addHandelseMatrundaModified(matrunda.getId(), matrunda.getNamn(), matrunda.getChangeLog());
		}

		return Matrunda.fromEntity(persist(matrunda));
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public void deleteMatrunda(Integer id) throws MatrundaNotFoundException {
		MatrundaEntity matrunda = findMatrunda(id);

		repository.deleteById(id);

		systemloggService.addHandelseMatrundaRemoved(matrunda.getId(), matrunda.getNamn());
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public List<Matning> getLatestMatningarForMatrunda(Integer matrundaId) {
		var records = this.jooqRepository.senasteMatningarForMatrunda(matrundaId);

		return records.stream()
				.map(Matning::new)
				.collect(Collectors.toList());
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public void removeFromMatrundor(Integer matningstypId) {
		var matrundor = repository.findByMatrundaMatningstyperMatningstypId(matningstypId);
		matrundor.forEach(matrunda -> {
			matrunda.removeMatningstyp(matningstypId);
			if (!matrunda.getChangeLog().isEmpty()) {
				systemloggService.addHandelseMatrundaModified(matrunda.getId(), matrunda.getNamn(), matrunda.getChangeLog());
			}
		});
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public boolean exists(String namn) {
		return repository.existsByNamn(namn);
	}

	private MatrundaEntity findMatrunda(Integer id) throws MatrundaNotFoundException {
		return repository.findById(id).orElseThrow(MatrundaNotFoundException::new);
	}

	private MatrundaEntity persist(MatrundaEntity entity) {
		return repository.saveAndFlush(entity);
	}
}
