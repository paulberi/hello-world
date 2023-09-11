package se.metria.matdatabas.restapi.matobjektgrupp;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.MatobjektgruppApi;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.matobjektgrupp.MatobjektgruppService;
import se.metria.matdatabas.service.matobjektgrupp.dto.EditMatobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.dto.Matobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.exception.MatobjektgruppConflictException;
import se.metria.matdatabas.service.matobjektgrupp.exception.MatobjektgruppNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RequestMapping(value = "/api")
@RestController
public class MatobjektgruppController implements MatobjektgruppApi {

	private MatobjektgruppService matobjektgruppService;
	private ModelMapper mapper;

	public MatobjektgruppController(MatobjektgruppService matobjektgruppService, ModelMapper mapper) {
		this.matobjektgruppService = matobjektgruppService;
		this.mapper = mapper;
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatobjektgruppPageDto> matobjektgruppGet(Integer page, Integer size, String sortProperty, String sortDirection, Integer kategori) {
		var matobjektgrupper = matobjektgruppService.getMatobjektgrupper(page, size, sortProperty, Sort.Direction.valueOf(sortDirection.toUpperCase()), kategori);
		var matobjektgrupperPage = mapper.map(matobjektgrupper, MatobjektgruppPageDto.class);
		return ResponseEntity.ok(matobjektgrupperPage);
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<MatobjektgruppDto> matobjektgruppPost(@Valid EditMatobjektgruppDto editMatobjektgruppDto) {
		try {
			EditMatobjektgrupp editMatobjektgrupp = mapper.map(editMatobjektgruppDto, EditMatobjektgrupp.class);
			Matobjektgrupp matobjektgrupp = matobjektgruppService.createMatobjektgrupp(editMatobjektgrupp);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(matobjektgrupp, MatobjektgruppDto.class));
		} catch (MatobjektgruppConflictException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatobjektgruppDto> matobjektgruppIdGet(Integer id) {
		try {
			Matobjektgrupp matobjektgrupp = matobjektgruppService.getMatobjektgrupp(id);
			return ResponseEntity.ok(mapper.map(matobjektgrupp, MatobjektgruppDto.class));
		} catch (MatobjektgruppNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}


	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<MatobjektgruppDto> matobjektgruppIdPut(Integer id, @Valid EditMatobjektgruppDto editMatobjektgruppDto) {
		try {
			EditMatobjektgrupp editMatobjektgrupp = mapper.map(editMatobjektgruppDto, EditMatobjektgrupp.class);
			Matobjektgrupp matobjektgrupp = matobjektgruppService.updateMatobjektgrupp(id, editMatobjektgrupp);
			return ResponseEntity.ok(mapper.map(matobjektgrupp, MatobjektgruppDto.class));
		} catch (MatobjektgruppNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<Void> matobjektgruppIdDelete(Integer id) {
		try {
			matobjektgruppService.deleteMatobjektgrupp(id);
			return ResponseEntity.noContent().build();
		} catch (MatobjektgruppNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
