package se.metria.matdatabas.service.analys;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.service.analys.dto.EditAnalys;
import se.metria.matdatabas.service.analys.exception.AnalysConflictException;
import se.metria.matdatabas.service.analys.exception.AnalysNotFoundException;
import se.metria.matdatabas.service.analys.exception.AnalysSaveMatningException;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.matning.Detektionsomrade;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.Matningsfelkod;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypConstants;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matningstyp.dto.SaveMatningstyp;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjekt.dto.Matobjekt;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektNotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Givet analys service tjänst")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class AnalysServiceIT {
	
	@Autowired
	private MatobjektService matobjektService;

	@Autowired
	private DefinitionMatningstypService definitionMatningstypService;

	@Autowired
	MatningstypService matningstypService;
	
	@Autowired
	private MatningService service;
		
	@MockBean
	private MatdatabasUser user;

	@Autowired
	private AnalysService analysService;

	private static final Integer MATOBJEKT_VATTENKEMI = 2;

	SaveMatningstyp.SaveMatningstypBuilder matningstypDefault = SaveMatningstyp.builder()
			.matintervallAntalGanger((short) 1)
			.matintervallTidsenhet(MatningstypConstants.TIDSENHET_VECKA)
			.paminnelseDagar((short) 3)
			.aktiv(true);


	EditAnalys.EditAnalysBuilder analysPh = EditAnalys.builder()
			.kommentar("Testar spara analys")
			.rapportor("Mätbolaget");

	private Matobjekt matobjekt1;
	private DefinitionMatningstyp defPh;
	private DefinitionMatningstyp defBly;

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt...
	void när_analys_sparas_skall_matningar_sparas() throws AnalysConflictException, MatobjektNotFoundException {
		matobjekt1 = matobjektService.getMatobjekt(MATOBJEKT_VATTENKEMI);
		defPh = definitionMatningstypService.findById(10).orElseThrow();
		defBly = definitionMatningstypService.findById(15).orElseThrow();
		var matningstypPh = createMatningstyp(defPh, matobjekt1);
		var matningstypBly = createMatningstyp(defBly, matobjekt1);

		LocalDateTime testDate = LocalDateTime.of(1987, Month.APRIL, 8, 12, 30);
		LocalDateTime date = testDate.truncatedTo(ChronoUnit.SECONDS);
		var analysSave = analysPh.build();
		analysSave.setMatobjektId(matobjekt1.getId());
		analysSave.setAnalysDatum(date);

		List<Matning> matningar = new ArrayList<>();
		Matning.MatningBuilder matning0 = Matning.builder()
				.avlastVarde(10d)
				.felkod(Matningsfelkod.OK)
				.inomDetektionsomrade(Detektionsomrade.INOM)
				.matningstypId(matningstypPh.getId());

		Matning.MatningBuilder matning1 = Matning.builder()
				.avlastVarde(20d)
				.felkod(Matningsfelkod.OK)
				.inomDetektionsomrade(Detektionsomrade.INOM)
				.matningstypId(matningstypBly.getId());

		matningar.add(matning0.build());
		matningar.add(matning1.build());

		analysSave.setMatningar(matningar);

		var analys = analysService.createAnalys(analysSave);
		assertEquals(2, analys.getMatningar().size());
	}

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt...
	void när_analys_med_felaktig_matning_sparas_fel_kastas() throws MatobjektNotFoundException {
		var defPh = definitionMatningstypService.findById(10).orElseThrow();
		var defBly = definitionMatningstypService.findById(15).orElseThrow();
		var matobjekt = matobjektService.getMatobjekt(MATOBJEKT_VATTENKEMI);
		var matningstypPh = createMatningstyp(defPh, matobjekt);
		var matningstypBly = createMatningstyp(defBly, matobjekt);
		LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		var analysSave = analysPh.build();
		analysSave.setMatobjektId(matobjekt.getId());
		analysSave.setAnalysDatum(date);

		List<Matning> matningar = new ArrayList<>();
		Matning.MatningBuilder matning0 = Matning.builder()
				.avlastVarde(10d)
				.felkod(Matningsfelkod.OK)
				.inomDetektionsomrade(Detektionsomrade.INOM)
				.matningstypId(matningstypPh.getId());

		Matning.MatningBuilder matning1 = Matning.builder()
				.felkod(Matningsfelkod.OK)
				.inomDetektionsomrade(Detektionsomrade.INOM)
				.matningstypId(matningstypBly.getId());

		matningar.add(matning0.build());
		matningar.add(matning1.build());

		analysSave.setMatningar(matningar);
		assertThrows(AnalysSaveMatningException.class, () -> {
			analysService.createAnalys(analysSave);
		});

	}

	@Test
	@WithMockUserAdministrator
	void när_analys_hämtas_skall_mätningar_med_samma_datum_hämtas()
			throws AnalysConflictException, AnalysNotFoundException, MatningIllegalMatvarde, MatobjektNotFoundException, AlreadyGodkandException {
		var defPh = definitionMatningstypService.findById(10).orElseThrow();
		var defBly = definitionMatningstypService.findById(15).orElseThrow();
		var matobjekt = matobjektService.getMatobjekt(MATOBJEKT_VATTENKEMI);
		var matningstypPh = createMatningstyp(defPh, matobjekt);
		var matningstypBly = createMatningstyp(defBly, matobjekt);

		LocalDateTime testDate = LocalDateTime.of(1986, Month.APRIL, 8, 12, 30);
		LocalDateTime date = testDate.truncatedTo(ChronoUnit.SECONDS);
		var analysSave = analysPh.build();
		analysSave.setMatobjektId(matobjekt.getId());
		analysSave.setAnalysDatum(date);

		List<Matning> matningar = new ArrayList<>();
		Matning.MatningBuilder matning0 = Matning.builder()
				.avlastVarde(10d)
				.felkod(Matningsfelkod.OK)
				.inomDetektionsomrade(Detektionsomrade.INOM)
				.matningstypId(matningstypPh.getId());

		Matning.MatningBuilder matning1 = Matning.builder()
				.avlastVarde(20d)
				.felkod(Matningsfelkod.OK)
				.inomDetektionsomrade(Detektionsomrade.INOM)
				.matningstypId(matningstypBly.getId());


		SaveMatning.SaveMatningBuilder saveMatning = SaveMatning.builder()
				.felkod(Matningsfelkod.OK)
				.kommentar("En kommentar")
				.rapportor("Mätbolaget");

		var toSave = saveMatning
				.avlastVarde(30d)
				.avlastDatum(date.minusSeconds(30))
				.build();

		var saved = service.create(matningstypBly, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());

		matningar.add(matning0.build());
		matningar.add(matning1.build());
		analysSave.setMatningar(matningar);

		var analys = analysService.createAnalys(analysSave);
		assertEquals(2, analys.getMatningar().size());

		var getAnalys = analysService.getAnalys(analys.getId());
		assertEquals(2, getAnalys.getMatningar().size());
		assertEquals(20d, getAnalys.getMatningar().get(1).getAvlastVarde());
	}

	private Matningstyp createMatningstyp(DefinitionMatningstyp def, Matobjekt matobjekt) {
		var matningstyp = matningstypDefault.build();
		matningstyp.setEnhet(def.getEnhet());
		matningstyp.setDecimaler(def.getDecimaler());
		return matningstypService.create(matobjekt.getId(), def.getId(), matningstyp);
	}
}
