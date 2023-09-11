package se.metria.matdatabas.service.handelse;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.bifogadfil.BifogadfilService;
import se.metria.matdatabas.service.bifogadfil.dto.BifogadfilInfo;
import se.metria.matdatabas.service.bifogadfil.exception.BifogadfilNotFoundException;
import se.metria.matdatabas.service.common.ChangeLog;
import se.metria.matdatabas.service.handelse.dto.Handelse;
import se.metria.matdatabas.service.handelse.dto.SaveHandelse;
import se.metria.matdatabas.service.handelse.entity.HandelseEntity;

@Service
public class HandelseService {

	private HandelseRepository repository;

	private BifogadfilService bifogadfilService;

	private Map<String, String> propertiesMapping = Map.of(
		"benamning", "benamning",
		"beskrivning", "beskrivning",
		"foretag", "loggadAv",
		"datum", "loggadDatum"
	);

	public HandelseService(HandelseRepository repository, BifogadfilService bifogadfilService) {
		this.repository = repository;
		this.bifogadfilService = bifogadfilService;
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Page<Handelse> getHandelseForMatobjekt(Integer matobjektId, Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection) {
		var sortPropertyMapped = propertiesMapping.getOrDefault(sortProperty, sortProperty);
		var pageRequest = PageRequest.of(page, pageSize, sortDirection, sortPropertyMapped);
		var handelsePage = repository.findByMatobjektId(matobjektId, pageRequest);
		var bildIds = handelsePage.getContent().stream()
			.flatMap(h -> h.getBifogadeBilderIds().stream())
			.collect(toSet());
		var bifogadfilMap = getBifogadfilMap(bildIds);
		return handelsePage.map(h -> Handelse.fromEntity(h, bifogadfilMap));
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Optional<Handelse> getHandelse(Integer matobjektId, Integer handelseId) {
		var optHandelse = repository.findById(handelseId);
		if (optHandelse.isPresent() &&  optHandelse.get().getMatobjektId().equals(matobjektId)) {
			var handelse = optHandelse.get();
			var bifogadfilMap = getBifogadfilMap(handelse.getBifogadeBilderIds());
			return Optional.of(Handelse.fromEntity(handelse, bifogadfilMap));
		}
		return Optional.empty();
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Handelse create(Integer matobjektId, SaveHandelse save) {
		var created = repository.save(new HandelseEntity(matobjektId, save));
		var bifogadfilMap = getBifogadfilMap(created.getBifogadeBilderIds());
		return Handelse.fromEntity(created, bifogadfilMap);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Handelse save(Integer matobjektId, Integer handelseId, SaveHandelse save) {
		var optUpdate = repository.findById(handelseId);
		if (optUpdate.isPresent() && optUpdate.get().getMatobjektId().equals(matobjektId)) {
			var update = optUpdate.get();
			var deleteFiles = new HashSet<>(update.getBifogadeBilderIds());
			deleteFiles.removeAll(save.getBifogadeBilderIds());
			update.save(save);
			deleteBifogadefiler(deleteFiles);
			var bifogadfilMap = getBifogadfilMap(update.getBifogadeBilderIds());
			return Handelse.fromEntity(update, bifogadfilMap);
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public void delete(Integer matobjektId, Integer handelseId) {
		var handelse = repository.findById(handelseId)
				.filter(h -> h.getMatobjektId().equals(matobjektId))
				.orElseThrow(EntityNotFoundException::new);
		delete(handelse);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public void deleteByMatobjektId(Integer matobjektId) {
		repository.findByMatobjektId(matobjektId).forEach(this::delete);
	}

	private void delete(HandelseEntity handelse) {
		var deleteFiles = new HashSet<>(handelse.getBifogadeBilderIds());
		repository.deleteById(handelse.getId());
		deleteBifogadefiler(deleteFiles);
	}

	public void addHandelseAndradeBerakningar(Integer matobjektId, String matningstyp, ChangeLog changes) {
		SaveHandelse save = SaveHandelse.builder()
				.benamning("Ändrade attribut för beräkning")
				.datum(LocalDateTime.now())
				.beskrivning(matningstyp + ": " + changes.toString())
				.bifogadeBilderIds(new HashSet<>())
				.build();
		create(matobjektId, save);
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
