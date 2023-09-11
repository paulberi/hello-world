package se.metria.matdatabas.restapi.paminnelse;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.PaminnelserApi;
import se.metria.matdatabas.openapi.model.MatningstypSearchFilterDto;
import se.metria.matdatabas.openapi.model.PaminnelsePageDto;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.paminnelse.PaminnelseService;
import se.metria.matdatabas.service.paminnelse.dto.Paminnelse;
import se.metria.matdatabas.service.paminnelse.query.PaminnelseSearchFilter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping(value = "/api")
@RestController
public class PaminnelseController implements PaminnelserApi {

	private ModelMapper mapper;
	private PaminnelseService paminnelseService;

	public PaminnelseController(ModelMapper mapper,
								PaminnelseService paminnelseService) {
		this.mapper = mapper;
		this.paminnelseService = paminnelseService;
	}

	@Override
	public ResponseEntity<PaminnelsePageDto> paminnelserPost(@NotNull @Valid Integer page, @Valid Integer size,
															 @Valid String sortProperty, @Valid String sortDirection,
															 @Valid Boolean onlyForsenade, @Valid MatningstypSearchFilterDto matningstypSearchFilterDto) {
		if (matningstypSearchFilterDto == null) {
			matningstypSearchFilterDto = new MatningstypSearchFilterDto();
		}
		MatningstypSearchFilter mappedFilter = mapper.map(matningstypSearchFilterDto, MatningstypSearchFilter.class);
		PaminnelseSearchFilter paminnelseSearchFilter = PaminnelseSearchFilter.builder()
				.onlyForsenade(onlyForsenade)
				.build();
		var sort = Sort.unsorted();
		if (sortProperty != null) {
			sort = Sort.Direction.fromOptionalString(sortDirection)
					.map(dir -> Sort.by(dir, sortProperty))
					.orElse(Sort.by(sortProperty));
		}
		PageRequest pageRequest = PageRequest.of(page == null ? 0 : page, size == null ? 10 : size, sort);
		Page<Paminnelse> result = paminnelseService.getMatningstypPaminnelsePage(mappedFilter, paminnelseSearchFilter, pageRequest);

		return ResponseEntity.ok(mapper.map(result, PaminnelsePageDto.class));
	}
}
