package se.metria.matdatabas.service.matning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Mätvärden skall valideras korrekt")
class MatningvardeValidatorTest {

	private MatningvardeValidator validator = new MatningvardeValidator();

	@ParameterizedTest(name = "När Nivå (nedmätning) har ogiltigt värde {0} skall värdet inte godkännas")
	@ValueSource(doubles = {-23d, 0d, 101d}) 
	void när_Nivå_nedmätning_har_ogiltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.NIVA_NEDMATNING)
				.build();

		var matningstyp = Matningstyp.builder()
			.maxPejlbartDjup(100d)
			.build();
		assertFalse(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Nivå (nedmätning) har giltigt värde {0} skall värdet godkännas")
	@ValueSource(doubles = {Double.MIN_VALUE, 0.1, 100d}) 
	void när_Nivå_nedmätning_har_giltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.NIVA_PORTRYCK)
				.build();

		var matningstyp = Matningstyp.builder()
			.maxPejlbartDjup(100d)
			.build();
		assertTrue(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Nivå (portryck) har ogiltigt värde {0} skall värdet inte godkännas")
	@ValueSource(doubles = {200d, 250d}) 
	void när_Nivå_portryck_har_ogiltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.NIVA_PORTRYCK)
				.build();

		var matningstyp = Matningstyp.builder()
			.build();
		assertFalse(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Nivå (nedmätning) har giltigt värde {0} skall värdet godkännas")
	@ValueSource(doubles = {-10d, 0d, 100d}) 
	void när_Nivå_portryck_har_giltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.NIVA_PORTRYCK)
				.build();

		var matningstyp = Matningstyp.builder()
			.build();
		assertTrue(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Infiltration (momentant flöde) har ogiltigt värde {0} skall värdet inte godkännas")
	@ValueSource(doubles = {-200d, -0.1})
	void när_Infiltration_momentant_flöde_har_ogiltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.INFILTRATION_MOMENTANT_FLODE)
				.build();

		var matningstyp = Matningstyp.builder()
			.build();
		assertFalse(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Infiltration (momentant flöde) har giltigt värde {0} skall värdet godkännas")
	@ValueSource(doubles = {0d, 100d}) 
	void när_Infiltration_momentant_flöde_har_giltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.INFILTRATION_MOMENTANT_FLODE)
				.build();

		var matningstyp = Matningstyp.builder()
			.build();
		assertTrue(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Infiltration (medelflöde) har ogiltigt värde {0} skall värdet inte godkännas")
	@ValueSource(doubles = {-200d, -0.1}) 
	void när_Infiltration_medelflöde_har_ogiltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.INFILTRATION_MEDEL_FLODE)
				.build();

		var matningstyp = Matningstyp.builder()
			.build();
		assertFalse(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Infiltration (medelflöde) har giltigt värde {0} skall värdet godkännas")
	@ValueSource(doubles = {0d, 100d}) 
	void när_Infiltration_medelflöde_har_giltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.INFILTRATION_MEDEL_FLODE)
				.build();

		var matningstyp = Matningstyp.builder()
			.build();
		assertTrue(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Tunnelvatten (momentant flöde) har ogiltigt värde {0} skall värdet inte godkännas")
	@ValueSource(doubles = {-200d, -0.1}) 
	void när_Tunnelvatten_momentant_flöde_har_ogiltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.TUNNELVATTEN_MOMENTANT_FLODE)
				.build();

		var matningstyp = Matningstyp.builder()
			.build();
		assertFalse(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När Tunnelvatten (momentant flöde) har giltigt värde {0} skall värdet godkännas")
	@ValueSource(doubles = {0d, 100d}) 
	void när_Tunnelvatten_momentant_flöde_har_giltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(Berakningstyp.TUNNELVATTEN_MOMENTANT_FLODE)
				.build();

		var matningstyp = Matningstyp.builder()
			.build();
		assertTrue(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

	@ParameterizedTest(name = "När annan mätmimgstyp har giltigt värde {0} skall värdet godkännas")
	@ValueSource(doubles = {-10000d, 0d, Double.MAX_VALUE}) 
	void när_annan_mätningstyp_har_giltigt_värde(double varde) {
		var definitionmatningstyp = DefinitionMatningstyp.builder()
				.berakningstyp(null)
				.build();

		var matningstyp = Matningstyp.builder()
			.typ("Annan typ")
			.build();
		assertTrue(validator.validate(definitionmatningstyp, matningstyp, varde));
	}

}
