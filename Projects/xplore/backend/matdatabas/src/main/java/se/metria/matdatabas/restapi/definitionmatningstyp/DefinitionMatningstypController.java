package se.metria.matdatabas.restapi.definitionmatningstyp;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.DefinitionmatningstypApi;
import se.metria.matdatabas.openapi.model.DefinitionMatningstypDto;
import se.metria.matdatabas.openapi.model.DefinitionMatningstypPageDto;
import se.metria.matdatabas.openapi.model.SaveDefinitionMatningstypDto;
import se.metria.matdatabas.openapi.model.SaveMatningstypDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.dto.SaveDefinitionMatningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.exception.DefinitionMatningstypConflictException;
import se.metria.matdatabas.service.matningstyp.MatningstypService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping(value = "/api")
@RestController
public class DefinitionMatningstypController implements DefinitionmatningstypApi {
	private DefinitionMatningstypService definitionMatningstypService;
	private MatningstypService matningstypService;
	private ModelMapper mapper;

	public DefinitionMatningstypController(DefinitionMatningstypService definitionMatningstypService, MatningstypService matningstypService, ModelMapper mapper) {
		this.definitionMatningstypService = definitionMatningstypService;
		this.matningstypService = matningstypService;
		this.mapper = mapper;
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	@Override
	public ResponseEntity<DefinitionMatningstypDto> definitionmatningstypIdGet(Integer id) {
		var definition = definitionMatningstypService.findById(id).map(d -> mapper.map(d, DefinitionMatningstypDto.class));
		return ResponseEntity.of(definition);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	@Override
	public ResponseEntity<DefinitionMatningstypPageDto> definitionmatningstypGet(@NotNull @Valid Integer page, @Valid Integer matobjektTyp, @Valid Boolean andringsbar, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection) {
		var definitioner = definitionMatningstypService.findAll(matobjektTyp != null ? matobjektTyp.shortValue() : null, andringsbar, page, size, sortProperty, Sort.Direction.valueOf(sortDirection.toUpperCase()));
		return ResponseEntity.ok(mapper.map(definitioner, DefinitionMatningstypPageDto.class));
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<Void> definitionmatningstypIdPut(Integer id, @Valid SaveDefinitionMatningstypDto saveDefinitionMatningstypDto) {
		try {
			definitionMatningstypService.save(id, mapper.map(saveDefinitionMatningstypDto, SaveDefinitionMatningstyp.class));
		} catch (DefinitionMatningstypConflictException e) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.noContent().build();
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<DefinitionMatningstypDto> definitionmatningstypPost(@Valid SaveDefinitionMatningstypDto saveDefinitionMatningstypDto) {
		try {
			var definition = definitionMatningstypService.save(mapper.map(saveDefinitionMatningstypDto, SaveDefinitionMatningstyp.class));
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(definition, DefinitionMatningstypDto.class));
		} catch (DefinitionMatningstypConflictException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<Void> definitionmatningstypIdDelete(Integer id) {
		definitionMatningstypService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<Void> definitionmatningstypIdOptions(Integer id) {
		var noContent = ResponseEntity.noContent();
		if (matningstypService.countByDefinitionMatningstypId(id) == 0) {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS).build();
		} else {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.OPTIONS).build();
		}
	}
}
