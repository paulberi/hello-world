package se.metria.matdatabas.restapi.matningslogg;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.MatningsloggApi;
import se.metria.matdatabas.openapi.model.MatningsloggPageDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.matningslogg.MatningsloggService;

import javax.annotation.security.RolesAllowed;

@RequestMapping(value = "/api")
@RestController
public class MatningsloggController implements MatningsloggApi {

	private MatningsloggService service;
	private ModelMapper mapper;

	public MatningsloggController(MatningsloggService service, ModelMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<MatningsloggPageDto> matningsloggGet(Long matningId, Integer page, Integer size,
															   String sortProperty, String sortDirection) {
		var matningsloggPage = service.getMatningslogg(matningId, page, size, sortProperty,
				Sort.Direction.valueOf(sortDirection.toUpperCase()));
		return ResponseEntity.ok(mapper.map(matningsloggPage, MatningsloggPageDto.class));
	}

}
