package se.metria.matdatabas.service.matningstyp;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.definitionmatningstyp.exception.DefinitionMatningstypConflictException;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.gransvarde.command.SaveGransvarde;
import se.metria.matdatabas.service.handelse.HandelseService;
import se.metria.matdatabas.service.matning.MatningvardeValidator;
import se.metria.matdatabas.service.matningslogg.MatningsloggService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matningstyp.dto.SaveMatningstyp;
import se.metria.matdatabas.service.matningstyp.exception.MatningstypHasMatningarException;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektConflictException;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektNotFoundException;
import se.metria.matdatabas.service.matrunda.MatrundaService;
import se.metria.matdatabas.service.matrunda.dto.EditMatrunda;
import se.metria.matdatabas.service.matrunda.dto.Matrunda;
import se.metria.matdatabas.service.matrunda.dto.MatrundaMatningstyp;
import se.metria.matdatabas.service.matrunda.exception.MatrundaConflictException;
import se.metria.matdatabas.service.matrunda.exception.MatrundaNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class MatningstypServiceIT {

	@Autowired
	private MatobjektService matobjektService;

	@Autowired
	private DefinitionMatningstypService definitionMatningstypService;

	@Autowired
	MatningstypService matningstypService;

	@Autowired
	HandelseService handelseService;

	@Autowired
	GransvardeService gransvardeService;

	@Autowired
	private MatningstypRepository matningstypRepository;

	@Autowired
	private MatrundaService matrundaService;

	@MockBean
	private MatdatabasUser user;
	
	@MockBean
	private MatningsloggService matningsloggService;
	
	@MockBean
	private MatningvardeValidator validator;

	public static final Integer MATOBJEKT_GRUNDVATTEN = 1;
	public static final Integer MATOBJEKT_VATTENKEMI = 2;

	public static final Integer DEFINITION_NEDMATNING = 1;
	public static final Integer DEFINITION_SATTNING = 9;
	public static final Integer DEFINITION_ESCHERICHIA = 16;

	SaveMatningstyp.SaveMatningstypBuilder matningstypDefault = SaveMatningstyp.builder()
			.matintervallAntalGanger((short) 1)
			.matintervallTidsenhet(MatningstypConstants.TIDSENHET_VECKA)
			.paminnelseDagar((short) 3)
			.aktiv(true);

	@AfterEach
	void afterEach() {
		matningstypRepository.deleteAll();
	}

	@Test
	void is_this_working() {
		assertNotNull(matningstypService);
	}

	@WithMockUserTillstandshandlaggare
	@Test
	void en_tillståndshandläggare_ska_kunna_hantera_nivå_nedmätning() throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningstypHasMatningarException, MatobjektNotFoundException {
		var matobjekt = matobjektService.getMatobjekt(MATOBJEKT_GRUNDVATTEN);
		var definition = definitionMatningstypService.findById(DEFINITION_NEDMATNING).orElseThrow();
		var matningstyp = matningstypDefault.build();
		matningstyp.setBerakningReferensniva(23d);
		matningstyp.setBerakningKonstant(1.0405d);
		matningstyp.setMaxPejlbartDjup(23d);

		// try to set some things that should be ignored
		matningstyp.setFixpunkt("ASDF");
		matningstyp.setEnhet("hPa");
		matningstyp.setDecimaler((short) 3);

		var saved = matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp);

		compareGrunduppgifter(matningstyp, saved);

		assertEquals(matningstyp.getBerakningReferensniva(), saved.getBerakningReferensniva());
		assertEquals(matningstyp.getBerakningKonstant(), saved.getBerakningKonstant());
		assertEquals(matningstyp.getMaxPejlbartDjup(), saved.getMaxPejlbartDjup());

		// confirm that we couldn't modify some settings
		assertNull(saved.getFixpunkt());
		assertEquals(definition.getEnhet(), saved.getEnhet());
		assertEquals(definition.getDecimaler(), saved.getDecimaler());

		// Try to update some values
		matningstyp.setBerakningReferensniva(25d);
		matningstyp.setBerakningKonstant(1.1234d);
		matningstyp.setMaxPejlbartDjup(20d);
		matningstypService.save(saved.getId(), matningstyp);
		var updated = matningstypService.findById(saved.getId());
		assertTrue(updated.isPresent());

		var handelser = handelseService.getHandelseForMatobjekt(matobjekt.getId(), 0, 10, "id", Sort.Direction.ASC);
		assertEquals(1, handelser.getTotalElements());
		assertEquals("Ändrade attribut för beräkning", handelser.getContent().get(0).getBenamning());

		var exists = matningstypService.existsInMatobjekt(matobjekt.getId(), saved.getId());
		assertTrue(exists);

		canFind(saved.getId());
		canDelete(saved.getId());		
	}

	@WithMockUserTillstandshandlaggare
	@Test
	void en_tillståndshandläggare_ska_kunna_hantera_vattenkemi() throws MatningstypHasMatningarException, MatobjektNotFoundException {
		var matobjekt = matobjektService.getMatobjekt(MATOBJEKT_VATTENKEMI);
		var definition = definitionMatningstypService.findById(DEFINITION_ESCHERICHIA).orElseThrow();
		var matningstyp = matningstypDefault.build();
		matningstyp.setEnhet("cfu/L");
		matningstyp.setDecimaler((short) 5);

		// try to set some things that should be ignored
		matningstyp.setBerakningReferensniva(64d);
		matningstyp.setBerakningKonstant(42d);

		var saved = matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp);

		compareGrunduppgifter(matningstyp, saved);

		assertEquals("cfu/L", saved.getEnhet());
		assertEquals((short) 5, saved.getDecimaler());
		assertNull(saved.getBerakningReferensniva());
		assertNull(saved.getBerakningKonstant());

		var exists = matningstypService.existsInMatobjekt(Integer.MAX_VALUE, saved.getId());
		assertFalse(exists);
		exists = matningstypService.existsInMatobjekt(matobjekt.getId(), saved.getId());
		assertTrue(exists);

		canFind(saved.getId());
		canDelete(saved.getId());		
	}

	@WithMockUserTillstandshandlaggare
	@Test
	void systemet_tillåter_inte_ogiltiga_tidsintervall() throws MatobjektNotFoundException {
		var matobjekt = matobjektService.getMatobjekt(MATOBJEKT_GRUNDVATTEN);
		var definition = definitionMatningstypService.findById(DEFINITION_NEDMATNING).orElseThrow();
		var matningstyp = matningstypDefault.build();
		
		var exists = matningstypService.existsInMatobjekt(matobjekt.getId(), Integer.MAX_VALUE);
		assertFalse(exists);

		matningstyp.setMatintervallTidsenhet(MatningstypConstants.TIDSENHET_TIMME);
		matningstyp.setMatintervallAntalGanger((short) 74);
		assertThrows(IllegalArgumentException.class, () -> matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp));

		matningstyp.setMatintervallTidsenhet(MatningstypConstants.TIDSENHET_DAG);
		matningstyp.setMatintervallAntalGanger((short) -1);
		assertThrows(IllegalArgumentException.class, () -> matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp));

		matningstyp.setMatintervallTidsenhet(MatningstypConstants.TIDSENHET_DAG);
		matningstyp.setMatintervallAntalGanger((short) 30);
		assertThrows(IllegalArgumentException.class, () -> matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp));
	}

	@WithMockUserTillstandshandlaggare
	@Test
	void systemet_tillåter_borttagning_av_mätningstyper_med_gransvärden_satta() throws MatningstypHasMatningarException, MatobjektNotFoundException {
		var matobjekt = matobjektService.getMatobjekt(MATOBJEKT_GRUNDVATTEN);
		var definition = definitionMatningstypService.findById(DEFINITION_NEDMATNING).orElseThrow();
		var matningstyp = matningstypDefault.build();
		matningstyp.setBerakningReferensniva(23d);
		matningstyp.setBerakningKonstant(1.0405d);
		matningstyp.setMaxPejlbartDjup(23d);

		var savedMatningstyp = matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp);

		var gransvarde = SaveGransvarde.builder()
				.matningstypId(savedMatningstyp.getId())
				.gransvarde(2.0d)
				.larmnivaId(1)
				.typAvKontroll((short) 0)
				.aktiv(true)
				.build();

		gransvardeService.createGransvarde(gransvarde);
		gransvardeService.createGransvarde(gransvarde);
		assertEquals(2, gransvardeService.getGransvardenListForMatningstyp(savedMatningstyp.getId()).size());

		matningstypService.deleteById(savedMatningstyp.getId());
		assertTrue(gransvardeService.getGransvardenListForMatningstyp(savedMatningstyp.getId()).isEmpty());
		assertTrue(matningstypService.findById(savedMatningstyp.getId()).isEmpty());
	}

	@WithMockUserTillstandshandlaggare
	@Test
	void matrundor_ska_uppdateras_vid_borttagning() throws MatrundaConflictException, MatningstypHasMatningarException, MatrundaNotFoundException, MatobjektNotFoundException {

		var savedMatobjekt = matobjektService.getMatobjekt(MATOBJEKT_GRUNDVATTEN);
		var nedmatningDefinition = definitionMatningstypService.findById(DEFINITION_NEDMATNING).orElseThrow();
		var escheriaDefinition  = definitionMatningstypService.findById(DEFINITION_ESCHERICHIA).orElseThrow();

		var nedmatning = matningstypDefault.build();
		nedmatning.setBerakningReferensniva(23d);
		nedmatning.setBerakningKonstant(1.0405d);
		nedmatning.setMaxPejlbartDjup(23d);
		var savedNedmatning = matningstypService.create(savedMatobjekt.getId(), nedmatningDefinition.getId(), nedmatning);

		var escheria = matningstypDefault.build();
		escheria.setEnhet("g/L");
		escheria.setDecimaler((short) 2);
		var savedEscheria = matningstypService.create(savedMatobjekt.getId(), escheriaDefinition.getId(), escheria);

		// Definiera mätrunda med 2 mätningstyper.
		List<MatrundaMatningstyp> matrundaMatningstyper = new ArrayList<>();
		matrundaMatningstyper.add(MatrundaMatningstyp.builder()
				.matningstypId(savedNedmatning.getId())
				.ordning((short) 0)
				.build());
		matrundaMatningstyper.add(MatrundaMatningstyp.builder()
				.matningstypId(savedEscheria.getId())
				.ordning((short) 1)
				.build());

		EditMatrunda matrunda = EditMatrunda.builder()
				.aktiv(true)
				.namn("Testrunda")
				.beskrivning("Testrunda")
				.matningstyper(matrundaMatningstyper).build();

		var savedMatrunda = matrundaService.createMatrunda(matrunda);
		assertEquals(2, savedMatrunda.getMatningstyper().size());

		// När mätningstyperna tas bort tas de också bort från mätrundan.
		matningstypService.deleteById(savedNedmatning.getId());
		Matrunda modifiedMatrunda = matrundaService.getMatrunda(savedMatrunda.getId());
		assertEquals(1, modifiedMatrunda.getMatningstyper().size());

		matningstypService.deleteById(savedEscheria.getId());
		Matrunda emptyMatrunda = matrundaService.getMatrunda(savedMatrunda.getId());
		assertTrue(emptyMatrunda.getMatningstyper().isEmpty());
	}

	@WithMockUserTillstandshandlaggare
	@Test
	void systemet_tvingar_värden_för_nivå_nedmätning() throws MatobjektNotFoundException {
		var matobjekt = matobjektService.getMatobjekt(MATOBJEKT_GRUNDVATTEN);
		var definition = definitionMatningstypService.findById(DEFINITION_NEDMATNING).orElseThrow();
		var matningstyp = matningstypDefault.build();

		assertThrows(NullPointerException.class, () -> matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp));
	}

	@WithMockUserMatrapportor
	@Test
	void koll_om_mätningstyp_finns_på_mätobjket_skall_returnera_false_om_det_inte_finns() {
		var exists = matningstypService.existsInMatobjekt(Integer.MAX_VALUE, Integer.MAX_VALUE);
		assertFalse(exists);
	}

	private void compareGrunduppgifter(SaveMatningstyp before, Matningstyp after) {
		assertEquals(before.getMatintervallAntalGanger(), after.getMatintervallAntalGanger());
		assertEquals(before.getMatintervallTidsenhet(), after.getMatintervallTidsenhet());
		assertEquals(before.getPaminnelseDagar(), after.getPaminnelseDagar());
		assertEquals(before.getAktiv(), after.getAktiv());
	}

	private void canFind(Integer id) {
		var fetchedMTyp = matningstypService.findById(id);
		assertTrue(fetchedMTyp.isPresent());
		assertEquals(id, fetchedMTyp.get().getId());
	}

	private void canDelete(Integer id) throws MatningstypHasMatningarException {
		matningstypService.deleteById(id);
		var deleted = matningstypService.findById(id);
		assertFalse(deleted.isPresent());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	@Sql({"/data/test_create_matningstyper.sql"})
	void möjligt_att_hämta_ut_paginerade_mätningstypMätobjekt() throws Exception {
		MatningstypSearchFilter filter = MatningstypSearchFilter.builder().build();
		var page = matningstypService.getMatningstypMatobjektPage(filter, PageRequest.of(0, 10));
		assertEquals(1, page.getTotalPages());
		assertEquals(10, page.getTotalElements());
		assertEquals(0, page.getNumber());
		assertEquals(10, page.getContent().size());

		page = matningstypService.getMatningstypMatobjektPage(filter, PageRequest.of(0, 5));
		assertEquals(2, page.getTotalPages());
		assertEquals(10, page.getTotalElements());
		assertEquals(0, page.getNumber());
		assertEquals(5, page.getContent().size());

		page = matningstypService.getMatningstypMatobjektPage(filter, PageRequest.of(1, 5));
		assertEquals(2, page.getTotalPages());
		assertEquals(10, page.getTotalElements());
		assertEquals(1, page.getNumber());
		assertEquals(5, page.getContent().size());

		filter.setMatobjektNamn("Mätobjekt7");
		page = matningstypService.getMatningstypMatobjektPage(filter, PageRequest.of(0, 10));
		assertEquals(1, page.getTotalPages());
		assertEquals(2, page.getTotalElements());
		assertEquals(0, page.getNumber());
		assertEquals(2, page.getContent().size());
	}

}
