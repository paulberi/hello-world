package se.metria.matdatabas.restapi.matrunda;

import static se.metria.matdatabas.restapi.bifogadfil.BifogadFilHelper.toFilLink;
import static se.metria.matdatabas.restapi.bifogadfil.BifogadFilHelper.toThumbnailLink;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.metria.matdatabas.openapi.api.MatrundaApi;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matrunda.MatrundaRapporteringService;
import se.metria.matdatabas.service.matrunda.MatrundaService;
import se.metria.matdatabas.service.matrunda.dto.EditMatrunda;
import se.metria.matdatabas.service.matrunda.dto.Matrunda;
import se.metria.matdatabas.service.matrunda.dto.RapporteringMatningstyp;
import se.metria.matdatabas.service.matrunda.exception.MatrundaConflictException;
import se.metria.matdatabas.service.matrunda.exception.MatrundaNotFoundException;

@RequestMapping(value = "/api")
@RestController
public class MatrundaController implements MatrundaApi {

	private MatrundaService matrundaService;
	private MatrundaRapporteringService matrundaRapporteringService;
	private MatningService matningService;
	private ModelMapper mapper;
	
	public MatrundaController(MatrundaService matrundaService, MatrundaRapporteringService matrundaRapporteringService, MatningService matningService, ModelMapper mapper) {
		this.matrundaService = matrundaService;
		this.matrundaRapporteringService = matrundaRapporteringService;
		this.matningService = matningService;
		this.mapper = mapper;
		if (this.mapper.getTypeMap(RapporteringMatningstyp.class, RapporteringMatningstypDto.class) == null) {
			this.mapper.createTypeMap(RapporteringMatningstyp.class, RapporteringMatningstypDto.class);
		}
		var typeMap = this.mapper.getTypeMap(RapporteringMatningstyp.class, RapporteringMatningstypDto.class);		
		typeMap.addMappings(m -> m.using(toFilLink).map(RapporteringMatningstyp::getBifogadBildId, RapporteringMatningstypDto::setBifogadBildLink));
		typeMap.addMappings(m -> m.using(toThumbnailLink).map(RapporteringMatningstyp::getBifogadBildId, RapporteringMatningstypDto::setBifogadBildThumbnail));
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<MatrundaPageDto> matrundaGet(@NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection, @Valid Boolean onlyAktiva) {
		var matrundor = matrundaService.getMatrundor(page, size, sortProperty,  Sort.Direction.valueOf(sortDirection.toUpperCase()), onlyAktiva);
		var matrundorPage = mapper.map(matrundor, MatrundaPageDto.class);
		return ResponseEntity.ok(matrundorPage);
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<MatrundaDto> matrundaPost(@Valid EditMatrundaDto editMatrundaDto) {
		try {
			EditMatrunda editMatrunda = mapper.map(editMatrundaDto, EditMatrunda.class);
			Matrunda matrunda = matrundaService.createMatrunda(editMatrunda);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(matrunda, MatrundaDto.class));
		} catch (MatrundaConflictException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<MatrundaDto> matrundaIdGet(Integer id) {
		try {
			Matrunda matrunda = matrundaService.getMatrunda(id);
			return ResponseEntity.ok(mapper.map(matrunda, MatrundaDto.class));
		} catch (MatrundaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<MatrundaDto> matrundaIdPut(Integer id, @Valid EditMatrundaDto editMatrundaDto) {
		try {
			EditMatrunda editMatrunda = mapper.map(editMatrundaDto, EditMatrunda.class);
			Matrunda matrunda = matrundaService.updateMatrunda(id, editMatrunda);
			return ResponseEntity.ok(mapper.map(matrunda, MatrundaDto.class));
		} catch (MatrundaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<Void> matrundaIdDelete(Integer id) {
		try {
			matrundaService.deleteMatrunda(id);
			return ResponseEntity.noContent().build();
		} catch (MatrundaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<Resource> matrundaIdFaltprotokollXlsxGet(Integer id, @Valid LocalDate startDate) {
		LocalDateTime startDateTime;
		try {
			if (startDate == null) {
				startDateTime = LocalDateTime.now();
			} else {
				startDateTime = startDate.atStartOfDay();
			}

			ByteArrayResource resource = new ByteArrayResource(matrundaRapporteringService.generateForMatrunda(id, startDateTime).toByteArray());
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=faltprotokoll.xlsx");

			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
					.body(resource);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<List<RapporteringMatningstypDto>> matrundaIdMatningstyperGet(Integer id, @Valid LocalDate startDate) {
		LocalDateTime startDateTime;

		if (startDate == null) {
			startDateTime = LocalDateTime.now();
		} else {
			startDateTime = startDate.atStartOfDay();
		}

		List<RapporteringMatningstyp> matningstyper = matrundaRapporteringService.getMatningstyper(id, startDateTime);
		List<RapporteringMatningstypDto> response = matningstyper.stream()
				.map(m -> mapper.map(m, RapporteringMatningstypDto.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<List<RapporteringMatningstypDto>> matrundaMatobjektIdMatningstyperGet(Integer id, @Valid LocalDate startDate) {
		LocalDateTime startDateTime;

		if (startDate == null) {
			startDateTime = LocalDateTime.now();
		} else {
			startDateTime = startDate.atStartOfDay();
		}

		List<RapporteringMatningstyp> matningstyper = matrundaRapporteringService.getMatningstyperFromMatobjekt(id, startDateTime);
		List<RapporteringMatningstypDto> response = matningstyper.stream()
				.map(m -> mapper.map(m, RapporteringMatningstypDto.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<Map<String, MatningDto>> senasteMatningarGet(Integer id) {
		Map<String, MatningDto> map = new HashMap<String, MatningDto>();
		try {
			for (var matning: matrundaService.getLatestMatningarForMatrunda(id)) {
				var matningDto = mapper.map(matning, MatningDto.class);
				map.put(matning.getMatningstypId().toString(), matningDto);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		return ResponseEntity.ok(map);
	}
}
