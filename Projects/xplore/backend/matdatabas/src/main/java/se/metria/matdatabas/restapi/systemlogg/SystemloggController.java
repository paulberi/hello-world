package se.metria.matdatabas.restapi.systemlogg;

import java.time.LocalDateTime;

import javax.annotation.security.RolesAllowed;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.metria.matdatabas.openapi.api.SystemloggApi;
import se.metria.matdatabas.openapi.model.SystemloggPageDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

@RequestMapping(value = "/api")
@RestController
public class SystemloggController implements SystemloggApi {

	private SystemloggService service;
	private ModelMapper mapper;
	
	public SystemloggController(SystemloggService service, ModelMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<SystemloggPageDto> systemloggGet(Integer page, Integer size, String sortProperty, String sortDirection, 
			LocalDateTime fromDatum, LocalDateTime tomDatum, Integer handelse) {		
		var handelseShort = handelse != null ? handelse.shortValue() : null;
		var systemloggPage = service.getSystemlogg(page, size, sortProperty, Sort.Direction.valueOf(sortDirection.toUpperCase()), fromDatum, tomDatum, handelseShort);
		return ResponseEntity.ok(mapper.map(systemloggPage, SystemloggPageDto.class));
	}

}
