package se.metria.matdatabas.service.matobjektgrupp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.matobjektgrupp.dto.EditMatobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.dto.ListMatobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.dto.Matobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.entity.MatobjektgruppEntity;
import se.metria.matdatabas.service.matobjektgrupp.exception.MatobjektgruppConflictException;
import se.metria.matdatabas.service.matobjektgrupp.exception.MatobjektgruppNotFoundException;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import javax.annotation.security.RolesAllowed;

@Service
public class MatobjektgruppService {
	private MatobjektgruppRepository repository;
	private SystemloggService systemloggService;

	public MatobjektgruppService(MatobjektgruppRepository repository, SystemloggService systemloggService) {
		this.repository = repository;
		this.systemloggService = systemloggService;
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Page<ListMatobjektgrupp> getMatobjektgrupper(Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection, Integer kategori) {
		if ("antalMatobjekt".equalsIgnoreCase(sortProperty)) {
			PageRequest pageRequest = PageRequest.of(page, pageSize);
			if (sortDirection.equals(Sort.Direction.ASC)) {
				return repository.findAllByKategoriEqualsAndMatobjektCountAsc(kategori.shortValue(), pageRequest)
						.map(ListMatobjektgrupp::fromEntity);
			} else {
				return repository.findAllByKategoriEqualsAndMatobjektCountDesc(kategori.shortValue(), pageRequest)
						.map(ListMatobjektgrupp::fromEntity);
			}
		} else {
			PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
			if (kategori != null) {
				return repository.findAllByKategoriEquals(kategori.shortValue(), pageRequest)
						.map(ListMatobjektgrupp::fromEntity);
			} else {
				return repository.findAll(pageRequest)
						.map(ListMatobjektgrupp::fromEntity);
			}

		}
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Matobjektgrupp getMatobjektgrupp(Integer id) throws MatobjektgruppNotFoundException {
		return Matobjektgrupp.fromEntity(this.findMatobjektgrupp(id));
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matobjektgrupp createMatobjektgrupp(EditMatobjektgrupp editMatobjektgrupp) throws MatobjektgruppConflictException {
		if (exists(editMatobjektgrupp.getNamn())) {
			throw new MatobjektgruppConflictException();
		}

		Matobjektgrupp matobjektgrupp = Matobjektgrupp.fromEntity(persist(editMatobjektgrupp.toEntity()));

		systemloggService.addHandelseMatobjektgruppCreated(matobjektgrupp);

		return matobjektgrupp;
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matobjektgrupp updateMatobjektgrupp(Integer id, EditMatobjektgrupp editMatobjektgrupp) throws MatobjektgruppNotFoundException {
		MatobjektgruppEntity entity = findMatobjektgrupp(id);
		Matobjektgrupp before = Matobjektgrupp.fromEntity(entity);
		Matobjektgrupp after = Matobjektgrupp.fromEntity(persist(editMatobjektgrupp.copyToEntity(entity)));

		systemloggService.addHandelseMatobjektgruppModified(before, after);

		return after;
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public void deleteMatobjektgrupp(Integer id) throws MatobjektgruppNotFoundException {
		Matobjektgrupp matobjektgrupp = Matobjektgrupp.fromEntity(findMatobjektgrupp(id));

		repository.deleteById(id);

		systemloggService.addHandelseMatobjektgruppRemoved(matobjektgrupp);
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public boolean exists(String namn) {
		return repository.existsByNamn(namn);
	}

	private MatobjektgruppEntity findMatobjektgrupp(Integer id) throws MatobjektgruppNotFoundException {
		return repository.findById(id).orElseThrow(() -> new MatobjektgruppNotFoundException());
	}

	private MatobjektgruppEntity persist(MatobjektgruppEntity entity) {
		return repository.saveAndFlush(entity);
	}
}
