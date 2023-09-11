package se.metria.matdatabas.service.meddelande;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.metria.matdatabas.service.meddelande.dto.EditMeddelande;
import se.metria.matdatabas.service.meddelande.dto.Meddelande;
import se.metria.matdatabas.service.meddelande.entity.MeddelandeEntity;

import java.time.*;
import java.util.Optional;

@Service
public class MeddelandeService {
	
	private MeddelandeRepository repository;

	public MeddelandeService(MeddelandeRepository repository) {
		this.repository = repository;
	}

	public Optional<Meddelande> getMeddelande(int id) {
		return repository.findById(id).map(this::map);
	}
	
	public Page<Meddelande> getMeddelanden(int page, int size) {
		var pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "datum", "skapadDatum");
		return repository.findAll(pageRequest).map(this::map);
	}
	
	public Meddelande createMeddelande(EditMeddelande create) {
		MeddelandeEntity entity = new MeddelandeEntity(map(create.getDatum()), create.getRubrik(), create.getUrl(), create.getMeddelande());
		return map(repository.saveAndFlush(entity));
	}
	
	public Optional<Meddelande> editMeddelande(Integer id, EditMeddelande edit) {
		return repository.findById(id).map(m -> {
			m.setRubrik(edit.getRubrik());
			m.setDatum(map(edit.getDatum()));
			m.setMeddelande(edit.getMeddelande());
			m.setUrl(edit.getUrl());
			return repository.saveAndFlush(m);			
		}).map(this::map);
	}
	
	public boolean deleteMeddelande(Integer id) {
		if (repository.existsById(id)) { 
			repository.deleteById(id);
			return true;
		}
		return false;
	}

	private LocalDate map(LocalDateTime time) {
		return time.toLocalDate();
	}
	private LocalDateTime map(LocalDate time) {
		return time.atTime(LocalTime.NOON).atOffset(ZoneOffset.UTC).toLocalDateTime();
	}

	private Meddelande map(MeddelandeEntity entity) {
		return Meddelande.builder()
				.id(entity.getId())
				.datum(map(entity.getDatum()))
				.rubrik(entity.getRubrik())
				.meddelande(entity.getMeddelande())
				.url(entity.getUrl())
				.andradAvId(entity.getAndradAvId())
				.andradDatum(entity.getAndradDatum())
				.skapadDatum(entity.getSkapadDatum())
				.build();
	}
}
