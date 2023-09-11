package se.metria.matdatabas.restapi.gransvarde;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.GransvardeApi;
import se.metria.matdatabas.openapi.model.GransvardeDto;
import se.metria.matdatabas.openapi.model.SaveGransvardeDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.gransvarde.command.SaveGransvarde;
import se.metria.matdatabas.service.gransvarde.exception.GransvardeNotFoundException;
import se.metria.matdatabas.service.gransvarde.exception.InactivatingGransvardeWithActiveLarmsException;
import se.metria.matdatabas.service.larm.LarmService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RequestMapping(value = "/api")
@RestController
public class GransvardeController implements GransvardeApi {

	private GransvardeService gransvardeService;
	private LarmService larmService;
	private ModelMapper mapper;

	public GransvardeController(GransvardeService gransvardeService, LarmService larmService, ModelMapper mapper) {
		this.gransvardeService = gransvardeService;
		this.larmService = larmService;
		this.mapper = mapper;
	}

   	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Override
	public ResponseEntity<Void> gransvardeIdPut(Integer id, @Valid SaveGransvardeDto saveGransvardeDto) {
		try {
			gransvardeService.updateGransvarde(id, mapper.map(saveGransvardeDto, SaveGransvarde.class));
		} catch (GransvardeNotFoundException | InactivatingGransvardeWithActiveLarmsException e) {
			return ResponseEntity.badRequest().build();
		}
	   return ResponseEntity.noContent().build();
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Override
	public ResponseEntity<GransvardeDto> gransvardePost(@Valid SaveGransvardeDto saveGransvardeDto) {
		var gransvarde = gransvardeService.createGransvarde(mapper.map(saveGransvardeDto, SaveGransvarde.class));
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(gransvarde, GransvardeDto.class));
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Override
	public ResponseEntity<Boolean> gransvardeIdCanDeactivateGet(Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(gransvardeService.canDeactivate(id));
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Override
	public ResponseEntity<Void> gransvardeIdDelete(Integer id) {
		gransvardeService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Override
	public ResponseEntity<Void> gransvardeIdOptions(Integer id) {
		var noContent = ResponseEntity.noContent();
		if (larmService.countByGransvardeId(id) == 0) {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS).build();
		} else {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.OPTIONS).build();
		}
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Override
	public ResponseEntity<GransvardeDto> gransvardeIdGet(Integer id) {
		final var gransvarde = gransvardeService.findById(id);
		if (gransvarde.isPresent()) {
			return ResponseEntity.ok(mapper.map(gransvarde.get(), GransvardeDto.class));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
