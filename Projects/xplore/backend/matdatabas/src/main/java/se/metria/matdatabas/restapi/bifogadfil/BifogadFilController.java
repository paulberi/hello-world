package se.metria.matdatabas.restapi.bifogadfil;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.matdatabas.openapi.api.BifogadfilApi;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.bifogadfil.BifogadfilService;
import se.metria.matdatabas.service.bifogadfil.dto.Bifogadfil;
import se.metria.matdatabas.service.bifogadfil.dto.BifogadfilInfo;
import se.metria.matdatabas.service.bifogadfil.dto.SaveBifogadfil;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@RequestMapping(value = "/api")
@RestController
public class BifogadFilController implements BifogadfilApi {

	private BifogadfilService bifogadfilService;
	private ModelMapper mapper;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public BifogadFilController(BifogadfilService bifogadfilService, ModelMapper mapper) {
		this.bifogadfilService = bifogadfilService;
		this.mapper = mapper;
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<List<BifogadfilDto>> bifogadfilGet(List<UUID> ids) {
		Set<BifogadfilInfo> bifogadeFiler = bifogadfilService.getBifogadfilInfos(new HashSet<>(ids));
		return ResponseEntity.ok(bifogadeFiler.stream()
				.map(bf -> mapper.map(bf, BifogadfilDto.class))
				.collect(Collectors.toList()));
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<BifogadfilDto> bifogadfilPost(MultipartFile multipartFile) {
		try {
			SaveBifogadfil saveBifogadfil = SaveBifogadfil.builder()
					.filnamn(multipartFile.getOriginalFilename())
					.mimeTyp(multipartFile.getContentType())
					.fil(multipartFile.getBytes())
					.build();
			Bifogadfil bifogadfil = bifogadfilService.createBifogadfil(saveBifogadfil);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(bifogadfil, BifogadfilDto.class));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<BifogadfilDto> bifogadfilIdGet(UUID id) {
		Optional<Bifogadfil> bifogadfil = bifogadfilService.getBifogadfil(id);
		return ResponseEntity.ok(mapper.map(bifogadfil.get(), BifogadfilDto.class));
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<Resource> bifogadfilIdDataGet(UUID id) {
		Optional<Bifogadfil> optionalBifogadfil = bifogadfilService.getBifogadfil(id);
		if (optionalBifogadfil.isPresent()) {
			Bifogadfil bifogadfil = optionalBifogadfil.get();
			ByteArrayResource resource = new ByteArrayResource(bifogadfil.getFil());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + bifogadfil.getFilnamn());

			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(bifogadfil.getFil().length)
					.contentType(APPLICATION_OCTET_STREAM)
					.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<Resource> bifogadfilIdThumbnailGet(UUID id) {
		Optional<Bifogadfil> optionalBifogadfil = bifogadfilService.getBifogadfil(id);

		if (optionalBifogadfil.isPresent() && optionalBifogadfil.get().getThumbnail() == null) {
			try {
				bifogadfilService.updateThumbnail(id);
			} catch (IOException e) {
				logger.error("Failed to update thumbnail.");
				return ResponseEntity.notFound().build();
			}
			optionalBifogadfil = bifogadfilService.getBifogadfil(id);
		}

		if (optionalBifogadfil.isPresent() && optionalBifogadfil.get().getThumbnail() != null) {
			Bifogadfil bifogadfil = optionalBifogadfil.get();
			ByteArrayResource resource = new ByteArrayResource(bifogadfil.getThumbnail());

			HttpHeaders headers = new HttpHeaders();
			var index = bifogadfil.getFilnamn().indexOf(".");
			var thumbName = index > -1 ? bifogadfil.getFilnamn().substring(0, index) + "_thumbnail" + bifogadfil.getFilnamn().substring(index) : 
				bifogadfil.getFilnamn() + "_thumbnail";
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + thumbName);

			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(bifogadfil.getThumbnail().length)
					.contentType(APPLICATION_OCTET_STREAM)
					.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
}
