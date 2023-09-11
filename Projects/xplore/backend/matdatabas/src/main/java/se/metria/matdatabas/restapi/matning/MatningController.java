package se.metria.matdatabas.restapi.matning;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.MatningarApi;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.matning.MatningService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RequestMapping(value = "/api")
@RestController
public class MatningController implements MatningarApi {
		
	private MatningService matningService;

	public MatningController(MatningService matningService) {
		this.matningService = matningService;
	}

	@Override
	public ResponseEntity<Void> matningarGodkann(@NotNull @Size(min = 1) @Valid List<Long> id) {
		this.matningService.godkann(id);

		return ResponseEntity.noContent().build();
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<Void> matningarDelete(@NotNull @Size(min = 1) @Valid List<Long> id) {
		matningService.deleteMatningar(id);
		return ResponseEntity.noContent().build();
	}
}
