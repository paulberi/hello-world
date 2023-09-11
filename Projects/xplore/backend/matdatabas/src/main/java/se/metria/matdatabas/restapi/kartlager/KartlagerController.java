package se.metria.matdatabas.restapi.kartlager;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.KartlagerApi;
import se.metria.matdatabas.openapi.model.KartlagerDto;
import se.metria.matdatabas.openapi.model.SaveKartlagerDto;
import se.metria.matdatabas.service.kartlager.KartlagerService;
import se.metria.matdatabas.service.kartlager.command.SaveKartlager;
import se.metria.matdatabas.service.kartlager.dto.Kartlager;
import se.metria.matdatabas.service.kartlager.dto.Kartlagerfil;
import se.metria.matdatabas.service.kartlager.exception.KartlagerInUseException;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequestMapping(value = "/api")
@RestController
public class KartlagerController implements KartlagerApi {

	private KartlagerService kartlagerService;
	private ModelMapper mapper;

	public KartlagerController(KartlagerService kartlagerService, ModelMapper mapper) {
		this.kartlagerService = kartlagerService;
		this.mapper = mapper;
	}

	@Override
	public ResponseEntity<List<KartlagerDto>> kartlagerGet() {
		List<Kartlager> kartlager = kartlagerService.findAll();
		return ResponseEntity.ok(kartlager.stream()
				.map(kl -> mapper.map(kl, KartlagerDto.class))
				.collect(Collectors.toList()));
	}

	@Override
	public ResponseEntity<KartlagerDto> kartlagerPost(@Valid SaveKartlagerDto saveKartlagerDto) {
		SaveKartlager kartlager = mapper.map(saveKartlagerDto, SaveKartlager.class);
		Kartlager saved = kartlagerService.save(kartlager);
		return ResponseEntity.ok(mapper.map(saved, KartlagerDto.class));
	}

	@Override
	public ResponseEntity<KartlagerDto> kartlagerIdGet(Integer id) {
		return ResponseEntity.of(kartlagerService.findById(id).map(kl -> mapper.map(kl, KartlagerDto.class)));
	}

	@Override
	public ResponseEntity<KartlagerDto> kartlagerIdPut(Integer id, @Valid SaveKartlagerDto saveKartlagerDto) {
		SaveKartlager kartlager = mapper.map(saveKartlagerDto, SaveKartlager.class);
		Kartlager saved = kartlagerService.update(id, kartlager);
		return ResponseEntity.ok(mapper.map(saved, KartlagerDto.class));
	}

	@Override
	public ResponseEntity<Void> kartlagerIdDelete(Integer id) {
		try {
			kartlagerService.delete(id);
		} catch (KartlagerInUseException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
		}
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> kartlagerOrderPost(@Valid List<Integer> requestBody) {
		kartlagerService.changeOrder(requestBody);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Resource> kartlagerFilerFilIdGet(UUID id) {
		Optional<Kartlagerfil> fil = kartlagerService.getKartlagerfil(id);

		return fil.map(f -> {
			String data = kartlagerService.getKartlagerfilData(f.getId());
			Resource resource = new ByteArrayResource(data.getBytes(StandardCharsets.UTF_8));

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + f.getFilnamn());

			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
					.headers(headers)
					.body(resource);
		}).orElse(ResponseEntity.notFound().build());
	}

	@Override
	public ResponseEntity<List<String>> kartlagerStilarGet() {
		return ResponseEntity.ok(kartlagerService.getStyles());
	}

	@Override
	public ResponseEntity<List<Object>> kartlagerTreeGet() {
		List<Object> result = new ArrayList<>(kartlagerService.getLayerTree());
		return ResponseEntity.ok(result);
	}
}
