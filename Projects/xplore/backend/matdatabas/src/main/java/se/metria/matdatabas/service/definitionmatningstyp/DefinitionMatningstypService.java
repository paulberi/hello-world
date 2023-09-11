package se.metria.matdatabas.service.definitionmatningstyp;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.dto.SaveDefinitionMatningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.entity.DefinitionMatningstypEntity;
import se.metria.matdatabas.service.definitionmatningstyp.exception.DefinitionMatningstypConflictException;
import se.metria.matdatabas.service.definitionmatningstyp.exception.DefinitionMatningstypNotFoundException;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefinitionMatningstypService {

	private DefinitionMatningstypRepository repository;

	public DefinitionMatningstypService(DefinitionMatningstypRepository repository) {
		this.repository = repository;
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Page<DefinitionMatningstyp> findAll(Short matobjektTyp, Boolean andringsbar, Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection) {
		PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		var definition = new DefinitionMatningstypEntity();
		definition.setMatobjektTyp(matobjektTyp);
		definition.setAndringsbar(andringsbar);
		return repository.findAll(Example.of(definition), pageRequest).map(DefinitionMatningstyp::fromEntity);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public List<DefinitionMatningstyp> findByMatobjektTyp(Short matobjektTyp) {
		return repository.findByMatobjektTyp(matobjektTyp).stream()
				.map(DefinitionMatningstyp::fromEntity)
				.collect(Collectors.toList());
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Optional<DefinitionMatningstyp> findById(Integer id) {
		return repository.findById(id).map(DefinitionMatningstyp::fromEntity);
	}

	public DefinitionMatningstyp findByNamn(String namn) throws DefinitionMatningstypNotFoundException {
		var definitionMatningstypEntity = repository.getDefinitionMatningstypEntityByNamn(namn)
				.orElseThrow(() -> new DefinitionMatningstypNotFoundException());
		return DefinitionMatningstyp.fromEntity(definitionMatningstypEntity);
	}

	public DefinitionMatningstyp findByBerakningstyp(Berakningstyp berakningstyp) throws DefinitionMatningstypNotFoundException {
		var definitionMatningstypEntity = repository.getDefinitionMatningstypEntityByBerakningstyp(berakningstyp)
				.orElseThrow(() -> new DefinitionMatningstypNotFoundException());
		return DefinitionMatningstyp.fromEntity(definitionMatningstypEntity);
	}


	@Transactional
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void save(Integer id, SaveDefinitionMatningstyp save) throws DefinitionMatningstypConflictException {
		var existing = repository.findById(id);
		if (existing.isPresent()) {
			var definition = existing.get();
			if (!definition.getNamn().equals(save.getNamn())) {
				if (repository.existsByNamnAndMatobjektTyp(save.getNamn(), save.getMatobjektTyp())) {
					throw new DefinitionMatningstypConflictException(save.getNamn() + " already exists");
				}
			}
			definition.save(save);
		} else {
			throw new EntityNotFoundException();
		}
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public DefinitionMatningstyp save(SaveDefinitionMatningstyp save) throws DefinitionMatningstypConflictException {
		if (repository.existsByNamnAndMatobjektTyp(save.getNamn(), save.getMatobjektTyp())) {
			throw new DefinitionMatningstypConflictException(save.getNamn() + " already exists");
		}
		var definition = new DefinitionMatningstypEntity(save);
		return DefinitionMatningstyp.fromEntity(repository.save(definition));
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void delete(Integer id) {
		repository.deleteById(id);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public boolean existsByNamnAndMatobjektTyp(String namn, Short matobjektTyp) {
		return repository.existsByNamnAndMatobjektTyp(namn, matobjektTyp);
	}
}
