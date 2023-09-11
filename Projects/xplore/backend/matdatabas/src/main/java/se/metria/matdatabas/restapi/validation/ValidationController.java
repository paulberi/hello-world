package se.metria.matdatabas.restapi.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.ValidationApi;
import se.metria.matdatabas.openapi.model.ValidationResultDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.analys.AnalysService;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjektgrupp.MatobjektgruppService;
import se.metria.matdatabas.service.matrunda.MatrundaService;
import se.metria.matdatabas.service.rapport.RapportService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RequestMapping(value = "/api")
@RestController
public class ValidationController implements ValidationApi {

	private AnvandareService anvandareService;
	private AnvandargruppService anvandargruppService;
	private MatobjektService matobjektService;
	private MatobjektgruppService matobjektgruppService;
	private DefinitionMatningstypService definitionMatningstypService;
	private MatrundaService matrundaService;
	private AnalysService analysService;
	private RapportService rapportService;

	public ValidationController(AnvandareService anvandareService,
								AnvandargruppService anvandargruppService,
								MatobjektService matobjektService,
								MatobjektgruppService matobjektgruppService,
								DefinitionMatningstypService definitionMatningstypService,
								MatrundaService matrundaService,
								RapportService rapportService,
								AnalysService analysService) {
		this.anvandareService = anvandareService;
		this.anvandargruppService = anvandargruppService;
		this.matobjektService = matobjektService;
		this.matobjektgruppService = matobjektgruppService;
		this.definitionMatningstypService = definitionMatningstypService;
		this.matrundaService = matrundaService;
		this.analysService = analysService;
		this.rapportService = rapportService;
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<ValidationResultDto> validationAnvandareExistsGet(String anvandarnamn) {
		return ResponseEntity.ok(new ValidationResultDto().result(
				anvandareService.exists(anvandarnamn)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public ResponseEntity<ValidationResultDto> validationAnvandargruppExistsGet(@NotNull @Valid String namn) {
		return ResponseEntity.ok(new ValidationResultDto().result(
				anvandargruppService.exists(namn)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<ValidationResultDto> validationMatobjektExistsGet(String namn) {
		return ResponseEntity.ok(new ValidationResultDto().result(
				matobjektService.exists(namn)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<ValidationResultDto> validationMatobjektgruppExistsGet(String namn) {
		return ResponseEntity.ok(new ValidationResultDto().result(
				matobjektgruppService.exists(namn)));
	}

	@Override
	public ResponseEntity<ValidationResultDto> validationDefinitionmatningstypExistsGet(@NotNull @Valid String namn, @NotNull @Valid Integer matobjektTyp) {
		return ResponseEntity.ok(new ValidationResultDto().result(
				definitionMatningstypService.existsByNamnAndMatobjektTyp(namn, matobjektTyp.shortValue())
		));
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<ValidationResultDto> validationMatrundaExistsGet(String namn) {
		return ResponseEntity.ok(new ValidationResultDto().result(
				matrundaService.exists(namn)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<ValidationResultDto> validationMatobjektAnalysExistsGet(Integer matobjektId, LocalDateTime analysDatum) {
		return ResponseEntity.ok(new ValidationResultDto().result(
				analysService.exists(matobjektId, analysDatum)));
	}

	@Override
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public ResponseEntity<ValidationResultDto> validationRapportExistsGet(String namn) {
		return ResponseEntity.ok(new ValidationResultDto().result(
				rapportService.rapportNamnExists(namn)
		));
	}
}
