package se.metria.matdatabas.restapi.export;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import se.metria.matdatabas.common.ValueFormatter;
import se.metria.matdatabas.openapi.model.MatningstypAntalMatningarPageDto;
import se.metria.matdatabas.openapi.model.MatningstypSearchFilterDto;
import se.metria.matdatabas.openapi.model.RapportTypEnumDto;
import se.metria.matdatabas.restapi.export.pivot.CsvPivotResponseWriter;
import se.metria.matdatabas.restapi.export.pivot.InlakagePivotRecord;
import se.metria.matdatabas.restapi.export.pivot.VattenkemiPivotRecord;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.matning.MatningJooqRepository;
import se.metria.matdatabas.service.matning.Matningstatus;
import se.metria.matdatabas.service.matning.query.MatningSearchFilter;
import se.metria.matdatabas.service.matningstyp.MatningstypJooqRepository;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.MatningstypAntalMatningar;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class ExportController {

	private MatningstypService matningstypService;
	private MatningstypJooqRepository matningstypRepository;
	private MatningJooqRepository matningRepository;
	private ModelMapper mapper;
	private ValueFormatter formatter;

	public ExportController(MatningstypService matningstypService,
							MatningstypJooqRepository matningstypRepository,
							MatningJooqRepository matningRepository,
							ModelMapper mapper,
							ValueFormatter formatter) {
		this.matningstypService = matningstypService;
		this.matningstypRepository = matningstypRepository;
		this.matningRepository = matningRepository;
		this.mapper = mapper;
		this.formatter = formatter;
	}

	@RequestMapping(value = "/export/matningstyper",
			produces = {"application/json"},
			consumes = {"application/json"},
			method = RequestMethod.POST)
	@Transactional(readOnly = true)
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatningstypAntalMatningarPageDto> matningstyperPost(
			@Valid Integer page,
			@Valid Integer size,
			@Valid String sortProperty,
			@Valid String sortDirection,
			@Valid @RequestParam(value = "fromDatum", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDatum,
			@Valid @RequestParam(value = "tomDatum", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tomDatum,
			@RequestBody @Valid MatningstypSearchFilterDto filter) {
		if (filter == null) {
			filter = new MatningstypSearchFilterDto();
		}
		MatningstypSearchFilter mappedFilter = mapper.map(filter, MatningstypSearchFilter.class);

		// Mätrapportör eller bättre får se icke godkända mätningar
		Short status = Matningstatus.GODKANT;

		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (authority.getAuthority().equalsIgnoreCase(MatdatabasRole.MATRAPPORTOR)) {
				status = null;
			}
		}

		var sort = Sort.unsorted();
		if (sortProperty != null) {
			sort = Sort.Direction.fromOptionalString(sortDirection)
					.map(dir -> Sort.by(dir, sortProperty))
					.orElse(Sort.by(sortProperty));
		}
		PageRequest pageRequest = PageRequest.of(page == null ? 0 : page, size == null ? 10 : size, sort);


		Page<MatningstypAntalMatningar> result = matningstypService.getMatningstyperAntalMatningar(mappedFilter, fromDatum, tomDatum, status, pageRequest);

		return ResponseEntity.ok(mapper.map(result, MatningstypAntalMatningarPageDto.class));
	}

	@RequestMapping(value = "/export/export",
			produces = {"text/csv"},
			consumes = {"application/json"},
			method = RequestMethod.POST)
	@Transactional(readOnly = true)
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public void matningstypExportStreaming(HttpServletResponse response,
										   @NotNull @Valid @RequestParam(value = "rapportTyp") RapportTypEnumDto rapportTyp,
										   @Valid @RequestParam(value = "fromDatum", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDatum,
										   @Valid @RequestParam(value = "tomDatum", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tomDatum,
										   @RequestBody @Valid MatningstypSearchFilterDto filter) {
		if (filter == null) {
			filter = new MatningstypSearchFilterDto();
		}

		MatningstypSearchFilter mappedFilter = mapper.map(filter, MatningstypSearchFilter.class);

		// Mätrapportör eller bättre får se icke godkända mätningar
		Short status = Matningstatus.GODKANT;

		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (authority.getAuthority().equalsIgnoreCase(MatdatabasRole.MATRAPPORTOR)) {
				status = null;
			}
		}

		try {
			switch (rapportTyp) {
				case MATOBJEKT:
					setCsvResponseHeaders("mätobjekt_export", response);
					exportMatobjekt(mappedFilter, response);
					break;
				case MATDATA:
					setCsvResponseHeaders("mätdata_export", response);
					exportMatdata(mappedFilter, fromDatum, tomDatum, status, response);
					break;
				case VATTENKEMI:
					setCsvResponseHeaders("vattenkemi_export", response);
					exportVattenkemi(mappedFilter, fromDatum, tomDatum, status, response);
					break;
				case INLACKAGE:
					setCsvResponseHeaders("inläckage_export", response);
					exportInlakage(mappedFilter, fromDatum, tomDatum, response);
					break;
				default:
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void exportMatobjekt(MatningstypSearchFilter filter, HttpServletResponse response) throws IOException {
		Stream<MatobjektExportRow> rowStream = matningstypRepository.matningstyperStream(filter).map(row -> new MatobjektExportRow(row, formatter));

		var csvResponseWriter = new CsvResponseWriter<>(MatobjektExportRow.class);
		csvResponseWriter.write(rowStream, response.getOutputStream(), true);
	}

	private void exportMatdata(MatningstypSearchFilter matningstypFilter, LocalDateTime from, LocalDateTime to, Short status, HttpServletResponse response) throws IOException {
		MatningSearchFilter matningFilter = MatningSearchFilter.builder()
				.from(from)
				.to(to)
				.status(status)
				.build();

		Stream<MatdataExportRow> rowStream = matningRepository.matningarStream(matningstypFilter, matningFilter).map(row -> new MatdataExportRow(row, formatter));

		var csvResponseWriter = new CsvResponseWriter<>(MatdataExportRow.class);

		csvResponseWriter.write(rowStream, response.getOutputStream(), true);
	}

	private void exportVattenkemi(MatningstypSearchFilter matningstypFilter, LocalDateTime from, LocalDateTime to, Short status, HttpServletResponse response) throws IOException {
		List<VattenkemiPivotRecord> columns = matningstypRepository.matningstyperList(matningstypFilter).stream().map(record -> new VattenkemiPivotRecord(record, formatter)).collect(Collectors.toList());

		MatningSearchFilter matningFilter = MatningSearchFilter.builder()
				.from(from)
				.to(to)
				.status(status)
				.build();

		List<VattenkemiPivotRecord> data = matningRepository.matningar(matningstypFilter, matningFilter).stream().map(record -> new VattenkemiPivotRecord(record, formatter)).collect(Collectors.toList());

		var writer = new CsvPivotResponseWriter("Datum");
		writer.write(columns, data, response.getOutputStream(), true);
	}

	private void exportInlakage(MatningstypSearchFilter filter, LocalDateTime from, LocalDateTime to, HttpServletResponse response) throws IOException {
		List<InlakagePivotRecord> columns = matningstypRepository.matningstyperList(filter).stream().map(record -> new InlakagePivotRecord(record, formatter)).collect(Collectors.toList());

		MatningSearchFilter matningFilter = MatningSearchFilter.builder()
				.from(from)
				.to(to)
				.status(Matningstatus.GODKANT)
				.build();

		List<InlakagePivotRecord> data = matningRepository.matningar(filter, matningFilter).stream().map(record -> new InlakagePivotRecord(record, formatter)).collect(Collectors.toList());

		var writer = new CsvPivotResponseWriter("Datum");
		writer.write(columns, data, response.getOutputStream(), true);
	}


	void setCsvResponseHeaders(String filenamePrefix, HttpServletResponse response) {
		var date = LocalDate.now().toString();
		String filename = filenamePrefix + "_" + date + ".csv";
		response.addHeader("Content-disposition", "inline;filename=" + filename);
		response.setContentType("application/csv");
	}
}
