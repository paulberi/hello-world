package se.metria.matdatabas.restapi.meddelande;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.MeddelandeApi;
import se.metria.matdatabas.openapi.model.EditMeddelandeDto;
import se.metria.matdatabas.openapi.model.MeddelandeDto;
import se.metria.matdatabas.openapi.model.MeddelandePageDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.meddelande.MeddelandeService;
import se.metria.matdatabas.service.meddelande.dto.EditMeddelande;
import se.metria.matdatabas.service.meddelande.dto.Meddelande;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@RequestMapping(value = "/api")
@RestController
public class MeddelandeController implements MeddelandeApi {
	private MeddelandeService service;
	private ModelMapper mapper;

	public MeddelandeController(MeddelandeService service, ModelMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MeddelandeDto> meddelandeIdGet(Integer id) {
		Optional<Meddelande> meddelande = service.getMeddelande(id);
		return ResponseEntity.of(meddelande.map(m -> mapper.map(m, MeddelandeDto.class)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MeddelandePageDto> meddelandeGet(Integer page, Integer size) {
		var meddelanden = service.getMeddelanden(page, size);
		return ResponseEntity.ok(mapper.map(meddelanden, MeddelandePageDto.class));
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<MeddelandeDto> meddelandePost(@Valid EditMeddelandeDto input) {
		EditMeddelande edit = mapper.map(input, EditMeddelande.class);
		Meddelande meddelande = service.createMeddelande(edit);
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(meddelande, MeddelandeDto.class));
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<MeddelandeDto> meddelandeIdPut(Integer id, @Valid EditMeddelandeDto input) {
		EditMeddelande edit = mapper.map(input, EditMeddelande.class);
		Optional<Meddelande> meddelande = service.editMeddelande(id, edit);
		return ResponseEntity.of(meddelande.map(m -> mapper.map(m, MeddelandeDto.class)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<Void> meddelandeIdDelete(Integer id) {
		if (service.deleteMeddelande(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
