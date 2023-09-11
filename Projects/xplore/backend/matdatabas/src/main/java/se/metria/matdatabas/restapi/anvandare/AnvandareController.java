package se.metria.matdatabas.restapi.anvandare;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.AnvandareApi;
import se.metria.matdatabas.openapi.model.AnvandareDto;
import se.metria.matdatabas.openapi.model.AnvandarePageDto;
import se.metria.matdatabas.openapi.model.EditAnvandareDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.anvandare.dto.EditAnvandare;
import se.metria.matdatabas.service.anvandare.exception.*;

import javax.annotation.security.RolesAllowed;
import javax.mail.MessagingException;
import javax.validation.Valid;

@RequestMapping(value = "/api")
@RestController
public class AnvandareController implements AnvandareApi {

	private AnvandareService anvandareService;
	private ModelMapper mapper;

	public AnvandareController(AnvandareService anvandareService, ModelMapper mapper) {
		this.anvandareService = anvandareService;
		this.mapper = mapper;
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<AnvandarePageDto> anvandareGet(Integer page, Integer size, String sortProperty, String sortDirection, Boolean visaInaktiva) {
		var anvandare = anvandareService.getAnvandare(page, size, sortProperty, Sort.Direction.valueOf(sortDirection.toUpperCase()), visaInaktiva);
		var anvandarePage = mapper.map(anvandare, AnvandarePageDto.class);
		return ResponseEntity.ok(anvandarePage);
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<AnvandareDto> anvandarePost(@Valid EditAnvandareDto editAnvandareDto) {
		try {
			EditAnvandare editAnvandare = mapper.map(editAnvandareDto, EditAnvandare.class);
			Anvandare anvandare = anvandareService.createAnvandare(editAnvandare);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(anvandare, AnvandareDto.class));
		} catch (AnvandarnamnConflictException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (MessagingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<AnvandareDto> anvandareIdGet(Integer id) {
		try {
			Anvandare anvandare = anvandareService.getAnvandare(id);
			return ResponseEntity.ok(mapper.map(anvandare, AnvandareDto.class));
		} catch (AnvandareNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<AnvandareDto> anvandareIdPut(Integer id, @Valid EditAnvandareDto editAnvandareDto) {
		try {
			EditAnvandare editAnvandare = mapper.map(editAnvandareDto, EditAnvandare.class);
			Anvandare anvandare = anvandareService.updateAnvandare(id, editAnvandare);
			return ResponseEntity.ok(mapper.map(anvandare, AnvandareDto.class));
		} catch (AnvandareNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<Void> anvandareIdDelete(Integer id) {
		try {
			anvandareService.deleteAnvandare(id);
			return ResponseEntity.noContent().build();
		} catch (AnvandareNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (AnvandareHasLoggedInException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<AnvandareDto> anvandareIdAnonymiseraPut(Integer id) {
		try {
			Anvandare anvandare = anvandareService.anonymizeAnvandare(id);
			return ResponseEntity.ok(mapper.map(anvandare, AnvandareDto.class));
		} catch (AnvandareNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
