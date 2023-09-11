package se.metria.matdatabas.restapi.larm;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.LarmnivaApi;
import se.metria.matdatabas.openapi.model.LarmnivaDto;
import se.metria.matdatabas.openapi.model.SaveLarmnivaDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.dto.Larmniva;
import se.metria.matdatabas.service.larm.dto.SaveLarmniva;
import se.metria.matdatabas.service.larm.exception.LarmnivaNamnConflictException;
import se.metria.matdatabas.service.larm.exception.LarmnivaNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "/api")
@RestController
public class LarmnivaController implements LarmnivaApi {

	private LarmService larmService;
	private GransvardeService gransvardeService;
	private ModelMapper mapper;

	public LarmnivaController(LarmService larmService, ModelMapper mapper, GransvardeService gransvardeService) {
		this.larmService = larmService;
		this.gransvardeService = gransvardeService;
		this.mapper = mapper;
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<List<LarmnivaDto>> larmnivaGet() {
		List<Larmniva> larmnivaer = larmService.getLarmnivaer();
		return ResponseEntity.ok(larmnivaer.stream()
				.map(larmniva -> mapper.map(larmniva, LarmnivaDto.class))
				.collect(Collectors.toList()));
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<LarmnivaDto> larmnivaPost(@Valid SaveLarmnivaDto saveLarmnivaDto) {
		try {
			SaveLarmniva saveLarmniva = mapper.map(saveLarmnivaDto, SaveLarmniva.class);
			Larmniva larmniva = larmService.createLarmniva(saveLarmniva);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(larmniva, LarmnivaDto.class));
		} catch (LarmnivaNamnConflictException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<Void> larmnivaIdPut(Integer id, @Valid SaveLarmnivaDto saveLarmnivaDto) {
		try {
			larmService.updateLarmniva(id, mapper.map(saveLarmnivaDto, SaveLarmniva.class));
			return ResponseEntity.noContent().build();
		} catch (LarmnivaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<Void> larmnivaIdDelete(Integer id) {
		larmService.deleteLarmNiva(id);
		return ResponseEntity.noContent().build();
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<Void> larmnivaIdOptions(Integer id) {
		var noContent = ResponseEntity.noContent();
		if (gransvardeService.countByLarmnivaId(id) == 0) {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS).build();
		} else {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.OPTIONS).build();
		}
	}
}
