package se.metria.matdatabas.restapi.matobjekt;

import static se.metria.matdatabas.restapi.bifogadfil.BifogadFilHelper.toFilLink;
import static se.metria.matdatabas.restapi.bifogadfil.BifogadFilHelper.toThumbnailLink;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.MatobjektApi;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.analys.AnalysService;
import se.metria.matdatabas.service.analys.dto.Analys;
import se.metria.matdatabas.service.analys.dto.EditAnalys;
import se.metria.matdatabas.service.analys.exception.AnalysConflictException;
import se.metria.matdatabas.service.analys.exception.AnalysNotFoundException;
import se.metria.matdatabas.service.analys.exception.AnalysSaveMatningException;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.handelse.HandelseService;
import se.metria.matdatabas.service.handelse.dto.SaveHandelse;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.dto.MatningSaveResult;
import se.metria.matdatabas.service.matning.dto.ReviewMatning;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.MatningstypMatobjekt;
import se.metria.matdatabas.service.matningstyp.dto.SaveMatningstyp;
import se.metria.matdatabas.service.matningstyp.exception.MatningstypHasMatningarException;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjekt.dto.EditMatobjekt;
import se.metria.matdatabas.service.matobjekt.dto.Matobjekt;
import se.metria.matdatabas.service.matobjekt.dto.MatobjektMapinfo;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektConflictException;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektNotFoundException;

@RequestMapping(value = "/api")
@RestController
public class MatobjektController implements MatobjektApi {

	private final GransvardeService gransvardeService;
	private MatobjektService matobjektService;
	private MatningstypService matningstypService;
	private DefinitionMatningstypService definitionMatningstypService;
	private HandelseService handelseService;
	private MatningService matningService;
	private AnalysService analysService;
	private ModelMapper mapper;

	public MatobjektController(MatobjektService matobjektService,
							   MatningstypService matningstypService,
							   DefinitionMatningstypService definitionMatningstypService,
							   HandelseService handelseService,
							   MatningService matningService,
							   AnalysService analysService,
							   GransvardeService gransvardeService,
							   ModelMapper mapper) {
		this.matobjektService = matobjektService;
		this.matningstypService = matningstypService;
		this.definitionMatningstypService = definitionMatningstypService;
		this.handelseService = handelseService;
		this.matningService = matningService;
		this.analysService = analysService;
		this.gransvardeService = gransvardeService;

		this.mapper = mapper;

		Converter<String, UUID> uuidConverter = new AbstractConverter<String, UUID>() {
			protected UUID convert(String source) {
				if (source == null) {
					return null;
				}
				return UUID.fromString(source);
			}
		};

		mapper.addConverter(uuidConverter);

		{
			if (this.mapper.getTypeMap(Matobjekt.class, MatobjektDto.class) == null) {
				this.mapper.createTypeMap(Matobjekt.class, MatobjektDto.class);
			}
			var typeMap = this.mapper.getTypeMap(Matobjekt.class, MatobjektDto.class);
			typeMap.addMappings(m -> m.using(toFilLink).map(Matobjekt::getBifogadBildId, MatobjektDto::setBifogadBildLink));
			typeMap.addMappings(m -> m.using(toThumbnailLink).map(Matobjekt::getBifogadBildId, MatobjektDto::setBifogadBildThumbnail));
		}

		{
			if (this.mapper.getTypeMap(MatobjektMapinfo.class, MatobjektMapinfoDto.class) == null) {
				this.mapper.createTypeMap(MatobjektMapinfo.class, MatobjektMapinfoDto.class);
			}
			var typeMap = this.mapper.getTypeMap(MatobjektMapinfo.class, MatobjektMapinfoDto.class);
			typeMap.addMappings(m -> m.using(toThumbnailLink).map(MatobjektMapinfo::getBifogadBildId, MatobjektMapinfoDto::setBifogadBildThumbnail));
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatobjektPageDto> matobjektGet(@NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection, @Valid MatningstypSearchFilterDto filter) {
		return matobjektPagePost(page, size, sortProperty, sortDirection, filter);
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatobjektPageDto> matobjektPagePost(@NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection, @Valid MatningstypSearchFilterDto filter) {
		MatningstypSearchFilter mappedFilter = mapper.map(filter, MatningstypSearchFilter.class);
		var pageable = pageRequest(page, size, sortProperty, sortDirection);
		var matobjekt = matobjektService.getMatobjekt(mappedFilter, pageable);
		var matobjektPage = mapper.map(matobjekt, MatobjektPageDto.class);
		return ResponseEntity.ok(matobjektPage);
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<MatobjektDto> matobjektPost(@Valid EditMatobjektDto editMatobjektDto) {
		try {
			EditMatobjekt editMatobjekt = mapper.map(editMatobjektDto, EditMatobjekt.class);
			Matobjekt matobjekt = matobjektService.createMatobjekt(editMatobjekt);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(matobjekt, MatobjektDto.class));
		} catch (MatobjektConflictException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatobjektDto> matobjektIdGet(Integer id) {
		try {
			Matobjekt matobjekt = matobjektService.getMatobjekt(id);
			return ResponseEntity.ok(mapper.map(matobjekt, MatobjektDto.class));
		} catch (MatobjektNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<MatobjektDto> matobjektIdPut(Integer id, @Valid EditMatobjektDto editMatobjektDto) {
		try {
			EditMatobjekt editMatobjekt = mapper.map(editMatobjektDto, EditMatobjekt.class);
			Matobjekt matobjekt = matobjektService.updateMatobjekt(id, editMatobjekt);
			return ResponseEntity.ok(mapper.map(matobjekt, MatobjektDto.class));
		} catch (MatobjektNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<List<MatobjektMapinfoDto>> matobjektMapinfoPost(@Valid MatningstypSearchFilterDto matningstypSearchFilterDto) {
		MatningstypSearchFilter mappedFilter = mapper.map(matningstypSearchFilterDto, MatningstypSearchFilter.class);

		Collection<MatobjektMapinfo> matobjekt = matobjektService.getMatobjektMapinfo(mappedFilter);

		var matobjektList = matobjekt.stream().map(m -> mapper.map(m, MatobjektMapinfoDto.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(matobjektList);
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<Void> matobjektIdDelete(Integer id) {
		try {
			matobjektService.deleteMatobjekt(id);
			return ResponseEntity.noContent().build();
		} catch (MatobjektNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (MatningstypHasMatningarException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<Void> matobjektIdOptions(Integer id) {
		try {
			boolean tillstandshandlaggare = false;

			for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
				if (authority.getAuthority().equalsIgnoreCase(MatdatabasRole.TILLSTANDSHANDLAGGARE)) {
					tillstandshandlaggare = true;
				}
			}

			var noContent = ResponseEntity.noContent();
			if (!tillstandshandlaggare) {
				return noContent.allow(HttpMethod.GET, HttpMethod.OPTIONS).build();
			} if (matobjektService.canDelete(id)) {
				return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS).build();
			} else {
				return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.OPTIONS).build();
			}
		} catch (MatobjektNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<List<MatningstypDto>> matobjektIdMatningstyperGet(Integer id) {
		List<MatningstypDto> collect = matningstypService.getMatningstyperForMatobjekt(id).stream()
				.map(matningstyp -> mapper.map(matningstyp, MatningstypDto.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok(collect);
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatningstypDto> matobjektMatobjektIdMatningstyperMatningstypIdGet(Integer matobjektId, Integer matningstypId) {
		return ResponseEntity.of(matningstypService.findById(matningstypId).map(m -> mapper.map(m, MatningstypDto.class)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<Void> matobjektMatobjektIdMatningstyperMatningstypIdPut(Integer matobjektId, Integer matningstypId, @Valid SaveMatningstypDto saveMatningstypDto) {
		matningstypService.save(matningstypId, mapper.map(saveMatningstypDto, SaveMatningstyp.class));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<MatningstypDto> matobjektIdMatningstyperPost(Integer id, @Valid SaveMatningstypDto saveMatningstypDto, @Valid Integer definitionMatningstypId) {
		var save = mapper.map(saveMatningstypDto, SaveMatningstyp.class);
		var saved = matningstypService.create(id, definitionMatningstypId, save);
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(saved, MatningstypDto.class));
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<Void> matobjektMatobjektIdMatningstyperMatningstypIdDelete(Integer matobjektId, Integer matningstypId) {
		try {
			matningstypService.deleteById(matningstypId);
		} catch (MatningstypHasMatningarException e) {
			ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED);
		}
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> matobjektMatobjektIdMatningstyperMatningstypIdOptions(Integer matobjektId, Integer matningstypId) {
		var noContent = ResponseEntity.noContent();
		if (matningstypService.canDelete(matningstypId)) {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS).build();
		} else {
			return noContent.allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.OPTIONS).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<HandelsePageDto> matobjektIdHandelserGet(Integer id, @NotNull @Valid Integer page,
																   @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection) {
		if (!matobjektService.exists(id)) {
			return ResponseEntity.notFound().build();
		}
		var handelse = handelseService.getHandelseForMatobjekt(id, page, size, sortProperty, Sort.Direction.valueOf(sortDirection.toUpperCase()));
		var handelsePage = mapper.map(handelse, HandelsePageDto.class);
		return ResponseEntity.ok(handelsePage);
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<HandelseDto> matobjektIdHandelserHandelseIdGet(Integer id, Integer handelseId) {
		return handelseService.getHandelse(id, handelseId)
				.map(h -> ResponseEntity.ok(mapper.map(h, HandelseDto.class)))
				.orElse(ResponseEntity.notFound().build());
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<HandelseDto> matobjektIdHandelserPost(Integer id, @Valid SaveHandelseDto saveHandelseDto) {
		if (!matobjektService.exists(id)) {
			return ResponseEntity.notFound().build();
		}
		var create = mapper.map(saveHandelseDto, SaveHandelse.class);
		var created = mapper.map(handelseService.create(id, create), HandelseDto.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<HandelseDto> matobjektIdHandelserHandelseIdPut(Integer id, Integer handelseId, @Valid SaveHandelseDto saveHandelseDto) {
		try {
			var update = mapper.map(saveHandelseDto, SaveHandelse.class);
			var updated = mapper.map(handelseService.save(id, handelseId, update), HandelseDto.class);
			return ResponseEntity.ok(updated);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<Void> matobjektIdHandelserHandelseIdDelete(Integer id, Integer handelseId) {
		try {
			handelseService.delete(id, handelseId);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<List<String>> matobjektNamnGet(String q, Integer typ) {
		return ResponseEntity.ok(matobjektService.getMatobjektNamn(q, typ));
	}

	@Override
	public ResponseEntity<String> matobjektIdNamnGet(Integer id) {
		return ResponseEntity.of(matobjektService.getMatobjektNamn(id).map(s -> "\"" + s + "\""));
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<List<String>> matobjektFastigheterGet(String q) {
		return ResponseEntity.ok(matobjektService.getMatobjektFastigheter(q));
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<String> matobjektIdGet(String namn) {
		Optional<Integer> optionalInteger = matobjektService.getMatobjektIdByNamn(namn);
		return optionalInteger.isPresent() ?
				ResponseEntity.ok(optionalInteger.get().toString()) :
				ResponseEntity.notFound().build();
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<List<DefinitionMatningstypDto>> matobjektIdDefinitionmatningstyperGet(Integer id, @Valid Boolean all) {

		var matobjektTyp = matobjektService.getMatobjektTyp(id);
		if (matobjektTyp.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		var excludeIds = new HashSet<Integer>();
		if (!all) {
			matningstypService.getMatningstyperForMatobjekt(id).forEach(m -> excludeIds.add(m.getDefinitionMatningstypId()));
		}

		var definitioner = definitionMatningstypService.findByMatobjektTyp(matobjektTyp.get()).stream()
				.filter(definition -> !excludeIds.contains(definition.getId()))
				.map(definition -> mapper.map(definition, DefinitionMatningstypDto.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(definitioner);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<MatningSaveResultDto> updateMatning(Integer matobjektId, Integer matningstypId, Long matningId, @Valid SaveMatningDto saveMatningDto) {
		if (!matningstypService.existsInMatobjekt(matobjektId, matningstypId) || !matningService.hasMatning(matningstypId, matningId)) {
			return ResponseEntity.notFound().build();
		}

		try {
			var matning = mapper.map(saveMatningDto, SaveMatning.class);
			var matningstyp = matningstypService.findById(matningstypId).get();

			MatningSaveResult saveResult = matningService.update(matningId, matningstyp, matning, StandardKallsystem.MiljöKoll.getNamn());
			return ResponseEntity.status(HttpStatus.OK).body(convertToMatningSaveResultDto(saveResult));
		} catch (MatningIllegalMatvarde | AlreadyGodkandException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private MatningSaveResultDto convertToMatningSaveResultDto(MatningSaveResult saveResult) {
		MatningSaveResultDto matningSaveResultDto = new MatningSaveResultDto();
		{
			matningSaveResultDto.setMatning(mapper.map(saveResult.getMatning(), MatningDto.class));

			List<LarmDto> larmDtoList = new ArrayList<>();

			if (saveResult.getLarm() != null) {
				larmDtoList.addAll(saveResult.getLarm());
			}

			matningSaveResultDto.setLarm(larmDtoList);
		}
		return matningSaveResultDto;
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<MatningSaveResultDto> addMatning(Integer matobjektId, Integer matningstypId, @Valid SaveMatningDto saveMatningDto) {
		if (!matningstypService.existsInMatobjekt(matobjektId, matningstypId)) {
			return ResponseEntity.notFound().build();
		}
		var matningstyp = matningstypService.findById(matningstypId).get();
		var create = mapper.map(saveMatningDto, SaveMatning.class);
		try {
			var saveResult = matningService.create(matningstyp, create, false, StandardKallsystem.MiljöKoll.getNamn());
			return ResponseEntity.status(HttpStatus.CREATED).body(convertToMatningSaveResultDto(saveResult));
		} catch (MatningIllegalMatvarde e) {
			return ResponseEntity.badRequest().build();
		} catch (AlreadyGodkandException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatningDto> getMatning(Integer matobjektId, Integer matningstypId, Long matningId) {
		if (!matningstypService.existsInMatobjekt(matobjektId, matningstypId)) {
			return ResponseEntity.notFound().build();
		}
		try {
			return ResponseEntity.ok(mapper.map(matningService.getMatning(matningId), MatningDto.class));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<MatningDto> reviewMatning(Integer matobjektId, Integer matningstypId,
													Long matningId, @Valid ReviewMatningDto reviewMatningDto) {
		if (!matningstypService.existsInMatobjekt(matobjektId, matningstypId)) {
			return ResponseEntity.notFound().build();
		}
		var matningstyp = matningstypService.findById(matningstypId).get();
		var review = mapper.map(reviewMatningDto, ReviewMatning.class);
		try {
			var reviewed = mapper.map(matningService.review(matningId, matningstyp, review), MatningDto.class);
			return ResponseEntity.ok(reviewed);
		} catch (MatningIllegalMatvarde e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<AnalysDto> addAnalys(Integer id, @Valid EditAnalysDto editAnalysDto) {
		try {
			if (!matobjektService.exists(id)) {
				return ResponseEntity.notFound().build();
			}

			EditAnalys editAnalys = mapper.map(editAnalysDto, EditAnalys.class);
			editAnalys.setMatobjektId(id);
			Analys analys = analysService.createAnalys(editAnalys);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(analys, AnalysDto.class));
		} catch (AnalysConflictException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (AnalysSaveMatningException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<AnalysDto> updateAnalys(Integer id, Integer analysId, @Valid EditAnalysDto editAnalysDto) {
		try {
			if (!matobjektService.exists(id)) {
				return ResponseEntity.notFound().build();
			}
			EditAnalys update = mapper.map(editAnalysDto, EditAnalys.class);
			update.setMatobjektId(id);
			return ResponseEntity.ok(mapper.map(analysService.updateAnalys(analysId, update), AnalysDto.class));
		} catch (AnalysNotFoundException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (AnalysSaveMatningException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<AnalysDto> getAnalys(Integer id, Integer analysId) {
		try {
			if (!matobjektService.exists(id)) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(mapper.map(analysService.getAnalys(analysId), AnalysDto.class));
		} catch (AnalysNotFoundException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<AnalysPageDto> getAnalysPage(Integer id, @NotNull @Valid Integer page,
													   @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection) {
		if (!matobjektService.exists(id)) {
			return ResponseEntity.notFound().build();
		}
		var analys = analysService.getAnalyser(id, page, size, sortProperty, Sort.Direction.valueOf(sortDirection.toUpperCase()));
		var analysPage = mapper.map(analys, AnalysPageDto.class);
		return ResponseEntity.ok(analysPage);
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<CountDto> getOgranskadeForMatningstyp(Integer matningstypId) {
		return ResponseEntity.ok(new CountDto().count(matningService.getEjGranskadeMatningarForMatningstyp(matningstypId)));
	}

	@Override
	public ResponseEntity<MatningstypMatobjektPageDto> matobjektMatningstyperGet(@NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection, @Valid MatningstypSearchFilterDto filter) {
		return matningstyperPage(page, size, sortProperty, sortDirection, filter);
	}

	@Override
	public ResponseEntity<MatningstypMatobjektPageDto> matobjektMatningstyperPost(@NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection, @Valid MatningstypSearchFilterDto filter) {
		return matningstyperPage(page, size, sortProperty, sortDirection, filter);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatningPageDto> matningPageGet(Integer matobjektId, Integer matningstypId, Integer page,
														 Integer size, String sortProperty, String sortDirection,
														 LocalDateTime fromDatum, LocalDateTime tomDatum,
														 Integer status) {
		if (!matningstypService.existsInMatobjekt(matobjektId, matningstypId)) {
			return ResponseEntity.notFound().build();
		}
		var statusShort = status != null ? status.shortValue() : null;

		var matningPage = matningService.getMatningarForMatningstyp(matningstypId, page, size, sortProperty,
				Sort.Direction.valueOf(sortDirection.toUpperCase()), fromDatum, tomDatum, statusShort);
		return ResponseEntity.ok(mapper.map(matningPage, MatningPageDto.class));
	}

	public ResponseEntity<Map<String, MatningDto>> matobjektSenasteMatningarGet(Integer id) {
		Map<String, MatningDto> matningar = new HashMap<>();
		var matningstyper = matningstypService.getMatningstyperForMatobjekt(id);
		for (var matningstyp : matningstyper) {
			var matning = matningService.getLatestMatningForMatningstyp(matningstyp.getId());
			if (matning.isPresent()) {
				var matningDto = mapper.map(matning.get(), MatningDto.class);
				matningar.put(matningstyp.getId().toString(), matningDto);
			}
			else {
				matningar.put(matningstyp.getId().toString(), null);
			}
		}

		return ResponseEntity.ok(matningar);
	}

	private ResponseEntity<MatningstypMatobjektPageDto> matningstyperPage(Integer page, Integer size, String sortProperty, String sortDirection, MatningstypSearchFilterDto filter) {
		MatningstypSearchFilter mappedFilter = mapper.map(filter, MatningstypSearchFilter.class);

		var pageRequest = pageRequest(page, size, sortProperty, sortDirection);

		Page<MatningstypMatobjekt> matningstyper = matningstypService.getMatningstypMatobjektPage(mappedFilter, pageRequest);

		var matningstypMatobjektPageDto = mapper.map(matningstyper, MatningstypMatobjektPageDto.class);

		return ResponseEntity.ok(matningstypMatobjektPageDto);
	}

	private PageRequest pageRequest(Integer page, Integer size, String sortProperty, String sortDirection) {
		var sort = Sort.unsorted();
		if (sortProperty != null) {
			sort = Sort.Direction.fromOptionalString(sortDirection)
					.map(dir -> Sort.by(dir, sortProperty))
					.orElse(Sort.by(sortProperty));
		}

		return PageRequest.of(page == null ? 0 : page, size == null ? Integer.MAX_VALUE : size, sort);
	}
}
