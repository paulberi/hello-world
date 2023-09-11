package se.metria.matdatabas.restapi.larm;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.LarmApi;
import se.metria.matdatabas.openapi.model.LarmPageDto;
import se.metria.matdatabas.openapi.model.MatningstypSearchFilterDto;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.anvandare.exception.AnvandareNotFoundException;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.dto.Larm;
import se.metria.matdatabas.service.larm.query.LarmSearchFilter;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RequestMapping(value = "/api")
@RestController
public class LarmController implements LarmApi {

	private LarmService larmService;
	private ModelMapper mapper;
	private MatdatabasUser matdatabasUser;
	private AnvandareService anvandareService;

	public LarmController(LarmService larmService,
						  ModelMapper mapper,
						  MatdatabasUser matdatabasUser,
						  AnvandareService anvandareService) {
		this.larmService = larmService;
		this.mapper = mapper;
		this.matdatabasUser = matdatabasUser;
		this.anvandareService = anvandareService;
	}

	@Override
	public ResponseEntity<LarmPageDto> larmPost(@NotNull @Valid Integer page, @NotNull @Valid Boolean egnaLarm,
												@Valid Integer size, @Valid String sortProperty,
												@Valid String sortDirection, @Valid Integer larmStatus,
												@Valid List<Integer> larmTillAnvandargruppIds, @Valid Integer larmniva,
												@Valid MatningstypSearchFilterDto matningstypSearchFilterDto) {

		if (matningstypSearchFilterDto == null) {
			matningstypSearchFilterDto = new MatningstypSearchFilterDto();
		}
		MatningstypSearchFilter mappedFilter = mapper.map(matningstypSearchFilterDto, MatningstypSearchFilter.class);
		LarmSearchFilter larmSearchFilter = LarmSearchFilter.builder().build();
		if (larmStatus != null) {
			larmSearchFilter.setLarmStatus(larmStatus.shortValue());
		}
		if (larmniva != null) {
			larmSearchFilter.setLarmniva(larmniva);
		}
		if (egnaLarm) {
			try {
				Anvandare anvandare = anvandareService.getAnvandare(this.matdatabasUser.getId());
				larmSearchFilter.setLarmTillAnvandargruppIds(anvandare.getAnvandargrupper());
			} catch (AnvandareNotFoundException e) {
				return ResponseEntity.notFound().build();
			}
		} else {
			if (larmTillAnvandargruppIds != null) {
				larmSearchFilter.setLarmTillAnvandargruppIds(larmTillAnvandargruppIds);
			}
		}
		var sort = Sort.unsorted();
		if (sortProperty != null) {
			sort = Sort.Direction.fromOptionalString(sortDirection)
					.map(dir -> Sort.by(dir, sortProperty))
					.orElse(Sort.by(sortProperty));
		}
		PageRequest pageRequest = PageRequest.of(page == null ? 0 : page, size == null ? 10 : size, sort);
		Page<Larm> result = larmService.getLarmListPage(larmSearchFilter, mappedFilter, pageRequest);

		return ResponseEntity.ok(mapper.map(result, LarmPageDto.class));
	}

	@Override
	public ResponseEntity<Void> larmKvittera(@NotNull @Size(min = 1) @Valid List<Long> id) {
		this.larmService.kvittera(id, this.matdatabasUser.getId());
		return ResponseEntity.noContent().build();
	}
}
