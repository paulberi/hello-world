package se.metria.matdatabas.restapi.anvandargrupp;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.AnvandargruppApi;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.anvandargrupp.dto.Anvandargrupp;
import se.metria.matdatabas.service.anvandargrupp.dto.EditAnvandargrupp;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.dto.Larm;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matansvar;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RequestMapping(value = "/api")
@RestController
public class AnvandargruppController implements AnvandargruppApi {

	private AnvandargruppService anvandargruppService;
	private AnvandareService anvandareService;
	private MatningstypService matningstypService;
	private LarmService larmService;
	private ModelMapper mapper;

	public AnvandargruppController(AnvandargruppService anvandargruppService, AnvandareService anvandareService,
								   MatningstypService matningstypService, LarmService larmService, ModelMapper mapper) {
		this.anvandargruppService = anvandargruppService;
		this.anvandareService = anvandareService;
		this.matningstypService = matningstypService;
		this.larmService = larmService;
		this.mapper = mapper;
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	@Override
	public ResponseEntity<AnvandargruppPageDto> anvandargruppGet(Integer page, Integer size, String sortProperty, String sortDirection) {
		var anvandargrupper = anvandargruppService.getAnvandargrupper(page, size, sortProperty, Sort.Direction.valueOf(sortDirection.toUpperCase()));
		var anvandarPage = mapper.map(anvandargrupper, AnvandargruppPageDto.class);
		return new ResponseEntity<>(anvandarPage, HttpStatus.OK);
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	@Override
	public ResponseEntity<AnvandargruppDto> anvandargruppIdGet(Integer id) {
		Optional<Anvandargrupp> grupp = anvandargruppService.getAnvandargrupp(id);
		return ResponseEntity.of(grupp.map(g -> mapper.map(g, AnvandargruppDto.class)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<AnvandargruppDto> anvandargruppIdPut(Integer id, @Valid EditAnvandargruppDto input) {
		EditAnvandargrupp edit = mapper.map(input, EditAnvandargrupp.class);
		Optional<Anvandargrupp> grupp = anvandargruppService.editAnvandargrupp(id, edit);
		return ResponseEntity.of(grupp.map(g -> mapper.map(g, AnvandargruppDto.class)));
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<Void> anvandargruppIdDelete(Integer id) {
		anvandargruppService.deleteAnvandargrupp(id);
		return ResponseEntity.noContent().build();
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<Void> anvandargruppIdOptions(Integer id) {
		var noContent = ResponseEntity.noContent();
		if (canDelete(id)) {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS).build();
		} else {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.OPTIONS).build();
		}
	}

	private boolean canDelete(Integer id) {
		Integer anvandareCount = this.anvandareService.getAnvandareCountForAnvandargrupp(id);
		Integer matansvarCount = this.matningstypService.getMatansvarForAnvandargruppCount(id);
		Integer larmCount = this.larmService.getLarmCountForAnvandargrupp(id);
		return anvandareCount == 0 && matansvarCount == 0 && larmCount == 0;
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<AnvandargruppDto> anvandargruppPost(@Valid EditAnvandargruppDto input) {
		EditAnvandargrupp edit = mapper.map(input, EditAnvandargrupp.class);
		Anvandargrupp grupp = anvandargruppService.addAnvandargrupp(edit);
		return ResponseEntity.ok(mapper.map(grupp, AnvandargruppDto.class));
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<MatansvarPageDto> getMatansvar(Integer id, @NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection) {
		Page<Matansvar> matansvarForAnvandargrupp = matningstypService.getMatansvarForAnvandargrupp(id, page, size, sortProperty, Sort.Direction.fromString(sortDirection));
		var matansvarPage = mapper.map(matansvarForAnvandargrupp, MatansvarPageDto.class);
		return new ResponseEntity<>(matansvarPage, HttpStatus.OK);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<LarmPageDto> getLarm(Integer id, @NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection) {
		Page<Larm> larmForAnvandargrupp = larmService.getLarmForAnvandargrupp(id, page, size, sortProperty, Sort.Direction.fromString(sortDirection));
		var larmPage = mapper.map(larmForAnvandargrupp, LarmPageDto.class);
		return new ResponseEntity<>(larmPage, HttpStatus.OK);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Override
	public ResponseEntity<AnvandarePageDto> getAnvandareForAnvandargrupp(Integer id, @NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection) {
		Page<Anvandare> anvandareForAnvandargrupp = anvandareService.getAnvandareForAnvandargrupp(id, page, size, sortProperty, Sort.Direction.fromString(sortDirection));
		var anvandarPage = mapper.map(anvandareForAnvandargrupp, AnvandarePageDto.class);
		return new ResponseEntity<>(anvandarPage, HttpStatus.OK);
	}

}
