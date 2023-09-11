package se.metria.matdatabas.service.matobjekt;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppRepository;
import se.metria.matdatabas.service.bifogadfil.BifogadfilService;
import se.metria.matdatabas.service.bifogadfil.dto.Bifogadfil;
import se.metria.matdatabas.service.bifogadfil.dto.SaveBifogadfil;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.handelse.HandelseService;
import se.metria.matdatabas.service.handelse.dto.SaveHandelse;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.kallsystem.dto.Kallsystem;
import se.metria.matdatabas.service.matning.Matningsfelkod;
import se.metria.matdatabas.service.matning.MatningvardeValidator;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportError;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportFormat;
import se.metria.matdatabas.service.matningslogg.MatningsloggService;
import se.metria.matdatabas.service.matningstyp.MatningstypConstants;
import se.metria.matdatabas.service.matningstyp.MatningstypRepository;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.SaveMatningstyp;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.matobjekt.dto.EditMatobjekt;
import se.metria.matdatabas.service.matobjekt.dto.Matobjekt;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektConflictException;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektNotFoundException;
import se.metria.matdatabas.service.matobjektgrupp.MatobjektgruppRepository;
import se.metria.matdatabas.service.matobjektgrupp.MatobjektgruppService;
import se.metria.matdatabas.service.matobjektgrupp.dto.EditMatobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.dto.Matobjektgrupp;
import se.metria.matdatabas.service.systemlogg.SystemloggRepository;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;
import se.metria.matdatabas.service.systemlogg.handelse.HandelseTyp;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static se.metria.matdatabas.service.matningstyp.MatningstypServiceIT.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class MatobjektServiceIT {
	@Autowired
	DataSource dataSource;

	@Autowired
	private MatobjektRepository matobjektRepository;

	@Autowired
	private MatobjektService service;

	@Autowired
	private AnvandargruppRepository anvandargruppRepository;

	@Autowired
	DefinitionMatningstypService definitionMatningstypService;

	@Autowired
	MatningstypService matningstypService;

	@Autowired
	private SystemloggRepository systemloggRepository;

	@Autowired
	private BifogadfilService bifogadfilService;

	@Autowired
	private MatobjektgruppService matobjektgruppService;

	@Autowired
	private MatobjektgruppRepository matobjektgruppRepository;

	@Autowired
	private HandelseService handelseService;

	@MockBean
	private MatdatabasUser user;
	
	@MockBean
	private MatningsloggService matningsloggService;

	@Autowired
	private MatningstypRepository matningstypRepository;

	@Autowired
	private MatningvardeValidator validator;

	List<Matobjekt> matobjekt = new ArrayList<>();

	private static final String CSVFILE_STANDARD = "standard.csv"; 
	private static final String CSVFILE_INSTRUMENT = "instrument.csv"; 
	
	SaveHandelse.SaveHandelseBuilder defaultHandelse = SaveHandelse.builder()
			.benamning("Hinder")
			.beskrivning("Stopp i rör")
			.datum(LocalDateTime.now())
			.bifogadeBilderIds(new HashSet<>());

	@BeforeEach
	@FlywayTest // Rensa databasen inför varje test
	public void before() {

	}

	private void addMatobjekt(int numberOfMatobjekt) {
		try {
			matobjekt.clear();
			for (int i = 0; i < numberOfMatobjekt; i++) {
				matobjekt.add(service.createMatobjekt(EditMatobjekt.builder()
						.typ((short) (i % 6))
						.namn("namn" + i)
						.aktiv(i % 2 == 0 ? true : false)
						.kontrollprogram(i % 2 == 1 ? true : false)
						.posN(BigDecimal.valueOf(10 + i))
						.posE(BigDecimal.valueOf(20 + i))
						.fastighet("fastighet" + i)
						.lage("läge" + i)
						.build()));
			}
		} catch (MatobjektConflictException e) {
			fail("Misslyckades skapa mätobjekt för testfallet");
		}
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_matobjekt_med_paginering() {
		matobjektRepository.deleteAll();

		addMatobjekt(8);

		MatningstypSearchFilter filter = new MatningstypSearchFilter();
		PageRequest firstPage = PageRequest.of(0, 5, Sort.Direction.ASC, "id");
		final var page0 = service.getMatobjekt(filter, firstPage);
		assertEquals(2, page0.getTotalPages());
		assertEquals(8, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(5, page0.getContent().size());

		IntStream.rangeClosed(0, 4).forEach(i -> {
			var lm = page0.getContent().get(i);
			assertEquals(matobjekt.get(i).getTyp(), lm.getTyp());
			assertEquals(matobjekt.get(i).getNamn(), lm.getNamn());
			assertEquals(matobjekt.get(i).getAktiv(), lm.getAktiv());
			assertEquals(matobjekt.get(i).getFastighet(), lm.getFastighet());
			assertEquals(matobjekt.get(i).getLage(), lm.getLage());
		});

		PageRequest secondPage = PageRequest.of(1, 5, Sort.Direction.ASC, "id");
		final var page1 = service.getMatobjekt(filter, secondPage);
		assertEquals(2, page1.getTotalPages());
		assertEquals(8, page1.getTotalElements());
		assertEquals(1, page1.getNumber());
		assertEquals(3, page1.getContent().size());

		IntStream.rangeClosed(5, 7).forEach(i -> {
			var lm = page1.getContent().get(i - 5);
			assertEquals(matobjekt.get(i).getTyp(), lm.getTyp());
			assertEquals(matobjekt.get(i).getNamn(), lm.getNamn());
			assertEquals(matobjekt.get(i).getAktiv(), lm.getAktiv());
			assertEquals(matobjekt.get(i).getFastighet(), lm.getFastighet());
			assertEquals(matobjekt.get(i).getLage(), lm.getLage());
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_matobjekt_med_paginering_sorterat_på_typ() throws Exception {
		matobjektRepository.deleteAll();

		int NUM_MATOBJEKT = 8;
		matobjekt.clear();
		for (int i = 0; i < NUM_MATOBJEKT; i++) {
			matobjekt.add(service.createMatobjekt(EditMatobjekt.builder()
					.typ((short) (i % 6))
					.namn("namn" + i)
					.aktiv(true)
					.kontrollprogram(true)
					.posN(BigDecimal.valueOf(10))
					.posE(BigDecimal.valueOf(20))
					.fastighet("fastighet")
					.lage("läge")
					.build()));
		}

		MatningstypSearchFilter filter = new MatningstypSearchFilter();
		PageRequest firstPageAsc = PageRequest.of(0, 8, Sort.Direction.ASC, "typ");
		var page0 = service.getMatobjekt(filter, firstPageAsc);
		assertEquals(1, page0.getTotalPages());
		assertEquals(8, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(8, page0.getContent().size());

		assertEquals((short) 0, page0.getContent().get(0).getTyp());
		assertEquals((short) 0, page0.getContent().get(1).getTyp());
		assertEquals((short) 1, page0.getContent().get(2).getTyp());
		assertEquals((short) 1, page0.getContent().get(3).getTyp());
		assertEquals((short) 2, page0.getContent().get(4).getTyp());
		assertEquals((short) 3, page0.getContent().get(5).getTyp());
		assertEquals((short) 4, page0.getContent().get(6).getTyp());
		assertEquals((short) 5, page0.getContent().get(7).getTyp());

		PageRequest firstPageDesc = PageRequest.of(0, 8, Sort.Direction.DESC, "typ");
		page0 = service.getMatobjekt(filter, firstPageDesc);
		assertEquals(1, page0.getTotalPages());
		assertEquals(8, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(8, page0.getContent().size());

		assertEquals((short) 5, page0.getContent().get(0).getTyp());
		assertEquals((short) 4, page0.getContent().get(1).getTyp());
		assertEquals((short) 3, page0.getContent().get(2).getTyp());
		assertEquals((short) 2, page0.getContent().get(3).getTyp());
		assertEquals((short) 1, page0.getContent().get(4).getTyp());
		assertEquals((short) 1, page0.getContent().get(5).getTyp());
		assertEquals((short) 0, page0.getContent().get(6).getTyp());
		assertEquals((short) 0, page0.getContent().get(7).getTyp());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_matobjekt_med_paginering_sorterat_på_namn() throws Exception {
		matobjektRepository.deleteAll();

		int NUM_MATOBJEKT = 8;
		matobjekt.clear();
		for (int i = 0; i < NUM_MATOBJEKT; i++) {
			matobjekt.add(service.createMatobjekt(EditMatobjekt.builder()
					.typ((short) 0)
					.namn("namn" + (NUM_MATOBJEKT - i))
					.aktiv(true)
					.kontrollprogram(true)
					.posN(BigDecimal.valueOf(10))
					.posE(BigDecimal.valueOf(20))
					.fastighet("fastighet")
					.lage("läge")
					.build()));
		}

		// String namn, Integer typ, Integer status, String fastighet, List<Integer> matobjektgrupper, Integer anvandargrupp
		MatningstypSearchFilter filter = new MatningstypSearchFilter();
		PageRequest firstPageAsc = PageRequest.of(0, 5, Sort.Direction.ASC, "namn");
		final var pageAsc = service.getMatobjekt(filter, firstPageAsc);
		assertEquals(2, pageAsc.getTotalPages());
		assertEquals(8, pageAsc.getTotalElements());
		assertEquals(0, pageAsc.getNumber());
		assertEquals(5, pageAsc.getContent().size());

		IntStream.rangeClosed(1, 5).forEach(i -> {
			var a = pageAsc.getContent().get(i - 1);
			assertEquals("namn" + i, a.getNamn());
		});

		PageRequest firstPageDesc = PageRequest.of(0, 5, Sort.Direction.DESC, "namn");
		final var pageDesc = service.getMatobjekt(filter, firstPageDesc);
		assertEquals(2, pageDesc.getTotalPages());
		assertEquals(8, pageDesc.getTotalElements());
		assertEquals(0, pageDesc.getNumber());
		assertEquals(5, pageDesc.getContent().size());

		IntStream.rangeClosed(0, 4).forEach(i -> {
			var a = pageDesc.getContent().get(i);
			assertEquals("namn" + (NUM_MATOBJEKT - i), a.getNamn());
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_matobjekt_med_paginering_och_id_lista() {
		addMatobjekt(8);

		List<Matobjekt> selected = List.of(matobjekt.get(0), matobjekt.get(3), matobjekt.get(6), matobjekt.get(7));
		List<Integer> selectedIds = selected.stream().map(Matobjekt::getId).collect(Collectors.toList());

		MatningstypSearchFilter filter = new MatningstypSearchFilter();
		filter.setMatobjektIds(selectedIds);
		PageRequest firstPageAsc = PageRequest.of(0, 5, Sort.Direction.ASC, "id");
		final var page0 = service.getMatobjekt(filter, firstPageAsc);
		assertEquals(1, page0.getTotalPages());
		assertEquals(4, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(4, page0.getContent().size());

		IntStream.rangeClosed(0, 3).forEach(i -> {
			Matobjekt m = selected.get(i);
			var lm = page0.getContent().get(i);
			assertEquals(m.getTyp(), lm.getTyp());
			assertEquals(m.getNamn(), lm.getNamn());
			assertEquals(m.getAktiv(), lm.getAktiv());
			assertEquals(m.getFastighet(), lm.getFastighet());
			assertEquals(m.getLage(), lm.getLage());
		});

		// Both typ and ids
		filter.setMatobjektTyp((short) 0);
		final var page1 = service.getMatobjekt(filter, firstPageAsc);
		assertEquals(1, page1.getTotalPages());
		assertEquals(2, page1.getTotalElements());
		assertEquals(0, page1.getNumber());
		assertEquals(2, page1.getContent().size());

		List<Matobjekt> matobjektWithTyp0 = selected.stream().filter(m -> m.getTyp() == 0).collect(Collectors.toList());

		IntStream.rangeClosed(0, 1).forEach(i -> {
			Matobjekt m = matobjektWithTyp0.get(i);
			var lm = page1.getContent().get(i);
			assertEquals(m.getTyp(), lm.getTyp());
			assertEquals(m.getNamn(), lm.getNamn());
			assertEquals(m.getAktiv(), lm.getAktiv());
			assertEquals(m.getFastighet(), lm.getFastighet());
			assertEquals(m.getLage(), lm.getLage());
		});
	}

	@Test
	@Transactional
	@WithMockUserTillstandshandlaggare
	void när_en_tillståndshandläggare_hämtar_med_id_så_ska_mätobjektet_returneras() throws Exception {
		addMatobjekt(8);

		for (Matobjekt m : this.matobjekt) {
			Matobjekt matobjekt = service.getMatobjekt(m.getId());
			assertEquals(m.getId(), matobjekt.getId());
			assertEquals(m.getNamn(), matobjekt.getNamn());
		}
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_ett_mätobjekt_hämtas_med_felaktigt_id_så_skall_det_inte_hämtas() {
		assertThrows(MatobjektNotFoundException.class, () -> {
			service.getMatobjekt(Integer.MAX_VALUE);
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_ett_mätobjekt_skapas_så_ska_den_lagras_korrekt() throws Exception {
		when(user.getId()).thenReturn(0);

		EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 0)
				.namn("namn")
				.aktiv(true)
				.kontrollprogram(true)
				.posN(BigDecimal.valueOf(10))
				.posE(BigDecimal.valueOf(20))
				.fastighet("fastighet")
				.lage("läge")
				.build();

		Matobjekt matobjekt = service.createMatobjekt(editMatobjekt);

		assertTrue(matobjekt.getId() > 0);
		assertEquals(editMatobjekt.getTyp(), matobjekt.getTyp());
		assertEquals(editMatobjekt.getNamn(), matobjekt.getNamn());
		assertEquals(editMatobjekt.getAktiv(), matobjekt.getAktiv());
		assertEquals(editMatobjekt.getKontrollprogram(), matobjekt.getKontrollprogram());
		assertEquals(editMatobjekt.getPosN(), matobjekt.getPosN());
		assertEquals(editMatobjekt.getPosE(), matobjekt.getPosE());
		assertEquals(editMatobjekt.getFastighet(), matobjekt.getFastighet());
		assertEquals(editMatobjekt.getLage(), matobjekt.getLage());
		assertNotNull(matobjekt.getSkapadDatum());
		assertNotNull(matobjekt.getAndradDatum());
		assertEquals(0, matobjekt.getAndradAvId());
		assertEquals(0, matobjekt.getMatrundor().size());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Nytt mätobjekt"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void fel_ska_inträffa_när_ett_mätobjekt_skapas_med_existerande_namn() throws Exception {
		addMatobjekt(1);

		final EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 1)
				.namn(matobjekt.get(0).getNamn())
				.aktiv(false)
				.kontrollprogram(false)
				.posN(BigDecimal.valueOf(11))
				.posE(BigDecimal.valueOf(22))
				.fastighet("ny fastighet")
				.lage("nytt läge")
				.build();

		assertThrows(MatobjektConflictException.class, () -> {
			service.createMatobjekt(editMatobjekt);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Nytt mätobjekt"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_ett_mätobjekt_uppdateras_så_ska_allt_lagras_korrekt() throws Exception {
		when(user.getId()).thenReturn(0);

		addMatobjekt(1);

		EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 1)
				.namn("nytt namn")
				.aktiv(false)
				.kontrollprogram(false)
				.posN(matobjekt.get(0).getPosN())
				.posE(matobjekt.get(0).getPosE())
				.fastighet("ny fastighet")
				.lage("nytt läge")
				.build();

		Matobjekt uppdateratMatobjekt = service.updateMatobjekt(matobjekt.get(0).getId(), editMatobjekt);

		assertTrue(uppdateratMatobjekt.getId() > 0);
		assertEquals(editMatobjekt.getTyp(), uppdateratMatobjekt.getTyp());
		assertEquals(editMatobjekt.getNamn(), uppdateratMatobjekt.getNamn());
		assertEquals(editMatobjekt.getAktiv(), uppdateratMatobjekt.getAktiv());
		assertEquals(editMatobjekt.getKontrollprogram(), uppdateratMatobjekt.getKontrollprogram());
		assertEquals(editMatobjekt.getPosN(), uppdateratMatobjekt.getPosN());
		assertEquals(editMatobjekt.getPosE(), uppdateratMatobjekt.getPosE());
		assertEquals(editMatobjekt.getFastighet(), uppdateratMatobjekt.getFastighet());
		assertEquals(editMatobjekt.getLage(), uppdateratMatobjekt.getLage());
		assertNotNull(uppdateratMatobjekt.getSkapadDatum());
		assertNotNull(uppdateratMatobjekt.getAndradDatum());
		assertEquals(0, uppdateratMatobjekt.getAndradAvId());
		assertNotNull(uppdateratMatobjekt.getMatrundor());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(2, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Nytt mätobjekt"))
				.collect(Collectors.toList()).size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Inställningar ändrade för mätobjekt"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void fel_ska_inträffa_när_ett_mätobjekt_som_inte_finns_uppdateras() {
		EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 1)
				.namn("namn")
				.aktiv(false)
				.kontrollprogram(false)
				.posN(BigDecimal.valueOf(10))
				.posE(BigDecimal.valueOf(20))
				.fastighet("fastighet")
				.lage("läge")
				.build();

		assertThrows(MatobjektNotFoundException.class, () -> {
			service.updateMatobjekt(Integer.MAX_VALUE, editMatobjekt);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(0, systemloggEntries.size());
		assertEquals(0, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Inställningar ändrade för mätobjekt"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_om_existerande_mätobjektnamn_finns() {
		addMatobjekt(8);

		matobjekt.forEach(m -> {
			assertTrue(service.exists(m.getNamn()));
		});
	}


	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_om_ett_icke_existerande_mätobjektnamn_finns() {
		addMatobjekt(8);

		assertFalse(service.exists("nytt-och-fräscht-mätobjektnamn"));
	}

	@Test
	@Transactional
	@WithMockUserTillstandshandlaggare
	void ta_bort_mätobjekt_utan_mätdata() throws Exception {
		addMatobjekt(1);
		Integer id = matobjekt.get(0).getId();

		SaveHandelse handelse1 = defaultHandelse.build();
		SaveHandelse handelse2 = defaultHandelse.build();
		handelseService.create(id, handelse1);
		handelseService.create(id, handelse2);

		var saved = service.getMatobjekt(id);
		assertEquals(id, saved.getId());

		service.deleteMatobjekt(id);

		assertThrows(MatobjektNotFoundException.class, () -> {
			service.getMatobjekt(id);
		});

		assertThrows(MatobjektNotFoundException.class, () -> {
			service.deleteMatobjekt(id);
		});

		var handelser = handelseService.getHandelseForMatobjekt(id, 1, 10, "id", Sort.Direction.ASC);
		assertTrue(handelser.isEmpty());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(2, systemloggEntries.size());
		assertEquals(
				String.format("Nytt mätobjekt #%d '%s' skapat", matobjekt.get(0).getId(), matobjekt.get(0).getNamn()),
				systemloggEntries.get(0).getBeskrivning());
		assertEquals(
				String.format("Mätobjekt #%d '%s' raderades", matobjekt.get(0).getId(), matobjekt.get(0).getNamn()),
				systemloggEntries.get(1).getBeskrivning());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void ta_bort_mätobjekt_som_inte_finns() {
		assertThrows(MatobjektNotFoundException.class, () -> {
			service.deleteMatobjekt(Integer.MAX_VALUE);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(0, systemloggEntries.size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hantera_bild_i_matobjekt() throws Exception {

		Bifogadfil bild1 = addBifogadfil();
		Bifogadfil bild2 = addBifogadfil();

		EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 1)
				.namn("namn")
				.aktiv(false)
				.kontrollprogram(false)
				.posN(BigDecimal.valueOf(10))
				.posE(BigDecimal.valueOf(20))
				.fastighet("fastighet")
				.lage("läge")
				.bifogadBildId(bild1.getId())
				.build();


		Matobjekt matobjekt = service.createMatobjekt(editMatobjekt);
		assertEquals(bild1.getId(), matobjekt.getBifogadBildId());

		// Byt bild
		editMatobjekt.setBifogadBildId(bild2.getId());
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		assertEquals(bild2.getId(), matobjekt.getBifogadBildId());

		// Ta bort bild
		editMatobjekt.setBifogadBildId(null);
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		assertNull(matobjekt.getBifogadBildId());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hantera_dokument_i_matobjekt() throws Exception {

		Bifogadfil bf1 = addBifogadfil();
		Bifogadfil bf2 = addBifogadfil();
		Bifogadfil bf3 = addBifogadfil();

		List<UUID> ids = List.of(bf1.getId());

		EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 1)
				.namn("namn")
				.aktiv(false)
				.kontrollprogram(false)
				.posN(BigDecimal.valueOf(10))
				.posE(BigDecimal.valueOf(20))
				.fastighet("fastighet")
				.lage("läge")
				.dokument(ids)
				.build();

		// Lägg till dokument när mätobjektet skapas
		Matobjekt matobjekt = service.createMatobjekt(editMatobjekt);
		for (UUID id : ids) {
			assertTrue(matobjekt.getDokument().contains(id));
		}
		assertTrue(bifogadfilService.getBifogadfil(bf1.getId()).isPresent());

		// Lägg till dokument
		ids = List.of(bf1.getId(), bf2.getId());
		editMatobjekt.setDokument(ids);
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		for (UUID id : ids) {
			assertTrue(matobjekt.getDokument().contains(id));
		}
		assertTrue(bifogadfilService.getBifogadfil(bf1.getId()).isPresent());
		assertTrue(bifogadfilService.getBifogadfil(bf2.getId()).isPresent());

		// Byt ut dokument
		ids = List.of(bf3.getId());
		editMatobjekt.setDokument(ids);
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		for (UUID id : ids) {
			assertTrue(matobjekt.getDokument().contains(id));
		}
		assertTrue(bifogadfilService.getBifogadfil(bf1.getId()).isEmpty());
		assertTrue(bifogadfilService.getBifogadfil(bf2.getId()).isEmpty());
		assertTrue(bifogadfilService.getBifogadfil(bf3.getId()).isPresent());

		// Ta bort dokument
		ids = List.of();
		editMatobjekt.setDokument(ids);
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		assertEquals(matobjekt.getDokument(), ids);
		assertTrue(bifogadfilService.getBifogadfil(bf1.getId()).isEmpty());
		assertTrue(bifogadfilService.getBifogadfil(bf2.getId()).isEmpty());
		assertTrue(bifogadfilService.getBifogadfil(bf3.getId()).isEmpty());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void mätobjekt_ska_kunna_skapas_med_mätobjektgrupper() throws Exception {
		Matobjektgrupp mg1 = addMatobjektgrupp("grupp1");
		Matobjektgrupp mg2 = addMatobjektgrupp("grupp2");
		Matobjektgrupp mg3 = addMatobjektgrupp("grupp3");

		List<Integer> ids = List.of(mg1.getId(), mg2.getId(), mg3.getId());

		EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 0)
				.namn("namn")
				.aktiv(false)
				.kontrollprogram(false)
				.posN(BigDecimal.valueOf(11))
				.posE(BigDecimal.valueOf(22))
				.fastighet("fastighet")
				.lage("läge")
				.matobjektgrupper(ids)
				.build();

		Matobjekt matobjekt = service.createMatobjekt(editMatobjekt);

		assertEquals(ids.size(), matobjekt.getMatobjektgrupper().size());

		for (Integer id : ids) {
			assertTrue(matobjekt.getMatobjektgrupper().contains(id));
		}
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void mätobjekt_ska_kunna_hantera_tillägg_och_borttag_av_mätobjektgrupper() throws Exception {
		Matobjektgrupp mg1 = addMatobjektgrupp("grupp1");
		Matobjektgrupp mg2 = addMatobjektgrupp("grupp2");
		Matobjektgrupp mg3 = addMatobjektgrupp("grupp3");

		EditMatobjekt editMatobjekt = EditMatobjekt.builder()
				.typ((short) 0)
				.namn("namn")
				.aktiv(false)
				.kontrollprogram(false)
				.posN(BigDecimal.valueOf(11))
				.posE(BigDecimal.valueOf(22))
				.fastighet("fastighet")
				.lage("läge")
				.build();

		// Inga grupper
		List<Integer> ids = List.of();
		Matobjekt matobjekt = service.createMatobjekt(editMatobjekt);
		assertEquals(ids.size(), matobjekt.getMatobjektgrupper().size());

		// En grupp
		ids = List.of(mg1.getId());
		editMatobjekt.setMatobjektgrupper(ids);
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		assertEquals(ids.size(), matobjekt.getMatobjektgrupper().size());
		for (Integer id : ids) {
			assertTrue(matobjekt.getMatobjektgrupper().contains(id));
		}

		// Två andra grupper
		ids = List.of(mg2.getId(), mg3.getId());
		editMatobjekt.setMatobjektgrupper(ids);
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		assertEquals(ids.size(), matobjekt.getMatobjektgrupper().size());
		for (Integer id : ids) {
			assertTrue(matobjekt.getMatobjektgrupper().contains(id));
		}

		// Tre grupper
		ids = List.of(mg1.getId(), mg2.getId(), mg3.getId());
		editMatobjekt.setMatobjektgrupper(ids);
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		assertEquals(ids.size(), matobjekt.getMatobjektgrupper().size());
		for (Integer id : ids) {
			assertTrue(matobjekt.getMatobjektgrupper().contains(id));
		}

		// Inga grupper
		ids = List.of();
		editMatobjekt.setMatobjektgrupper(ids);
		matobjekt = service.updateMatobjekt(matobjekt.getId(), editMatobjekt);
		assertEquals(ids.size(), matobjekt.getMatobjektgrupper().size());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		List<SystemloggEntry> updated = systemloggEntries.stream().filter(e ->
				e.getBeskrivning().startsWith("Inställningar ändrade för mätobjekt") && e.getBeskrivning().contains("mätobjektgrupper")
		).collect(Collectors.toList());

		assertEquals(4, updated.size());
		assertTrue(containsMatobjektgrupp(updated.get(0).getBeskrivning(),
				List.of(),
				List.of(mg1.getId())));
		assertTrue(containsMatobjektgrupp(updated.get(1).getBeskrivning(),
				List.of(mg1.getId()),
				List.of(mg2.getId(), mg3.getId())));
		assertTrue(containsMatobjektgrupp(updated.get(2).getBeskrivning(),
				List.of(mg2.getId(), mg3.getId()),
				List.of(mg1.getId(), mg2.getId(), mg3.getId())));
		assertTrue(containsMatobjektgrupp(updated.get(3).getBeskrivning(),
				List.of(mg1.getId(), mg2.getId(), mg3.getId()),
				List.of()));
	}

	private boolean containsMatobjektgrupp(String beskrivning, List<Integer> before, List<Integer> after) {
		return containsAddedMatobjektGrupp(beskrivning, before, after) ||
				containsRemovedMatobjektGrupp(beskrivning, before, after);
	}

	private boolean containsAddedMatobjektGrupp(String beskrivning, List<Integer> before, List<Integer> after) {
		Matcher matcher = Pattern.compile("Tillagda mätobjektgrupper: \\[(.*?)]").matcher(beskrivning);
		if (matcher.find()) {
			var added = new HashSet<>(after);
			added.removeAll(before);

			var idsInBeskrivning = matcher.group(1).isEmpty() ? List.of() :
					Stream.of(matcher.group(1).split(","))
							.map(String::trim)
							.map(Integer::valueOf)
							.collect(Collectors.toSet());

			return idsInBeskrivning.equals(added);
		}

		return false;
	}

	private boolean containsRemovedMatobjektGrupp(String beskrivning, List<Integer> before, List<Integer> after) {
		Matcher matcher = Pattern.compile("Borttagna mätobjektgrupper: \\[(.*?)]").matcher(beskrivning);
		if (matcher.find()) {
			var removed = new HashSet<>(before);
			removed.removeAll(after);

			var idsInBeskrivning = matcher.group(1).isEmpty() ? List.of() :
					Stream.of(matcher.group(1).split(","))
							.map(String::trim)
							.map(Integer::valueOf)
							.collect(Collectors.toSet());

			return idsInBeskrivning.equals(removed);
		}

		return false;
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_få_modifiera_mätobjekt() {
		assertThrows(AccessDeniedException.class, () -> {
			service.createMatobjekt(new EditMatobjekt());
		});

		assertThrows(AccessDeniedException.class, () -> {
			service.updateMatobjekt(1, new EditMatobjekt());
		});

		assertThrows(AccessDeniedException.class, () -> {
			service.deleteMatobjekt(1);
		});
	}

	private Matobjektgrupp addMatobjektgrupp(String namn) throws Exception {
		EditMatobjektgrupp editMatobjektgrupp = EditMatobjektgrupp.builder()
				.namn(namn)
				.kategori((short) 0)
				.beskrivning("beskrivning")
				.kartsymbol((short) 0)
				.build();

		return matobjektgruppService.createMatobjektgrupp(editMatobjektgrupp);
	}

	private Bifogadfil addBifogadfil() throws IOException {
		SaveBifogadfil saveBifogadfil = SaveBifogadfil.builder()
				.filnamn("filnamn")
				.mimeTyp("mimeTyp")
				.fil(new byte[]{1, 2, 3})
				.build();
		return bifogadfilService.createBifogadfil(saveBifogadfil);
	}

	private List<SystemloggEntry> getSystemloggEntries() {
		return systemloggRepository.findAll().stream()
				.map(SystemloggEntry::fromEntity)
				.filter(e -> e.getHandelse().equals(HandelseTyp.MATNINGSTYP.getId()))
				.collect(Collectors.toList());
	}
	
	@Test
	// Ska egentligen vara WithMockUserMatrapportor men då kan vi inte skapa testdata
	@WithMockUserAdministrator
	void när_mätningar_läses_in_från_CSVfil_standard_skall_mätningarna_läsas_in() throws IOException, MatobjektNotFoundException {
		final var kallsystemNamn = StandardKallsystem.RH2000_GRANSKAT.getNamn();
		var kallsystem = createKallsystem(kallsystemNamn);

		SaveMatningstyp.SaveMatningstypBuilder matningstypDefault = SaveMatningstyp.builder()
				.matintervallAntalGanger((short) 1)
				.matintervallTidsenhet(MatningstypConstants.TIDSENHET_VECKA)
				.paminnelseDagar((short) 3)
				.aktiv(true);


		var matobjekt = service.getMatobjekt(MATOBJEKT_GRUNDVATTEN);
		var definition = definitionMatningstypService.findById(DEFINITION_NEDMATNING).orElseThrow();
		var matningstyp = matningstypDefault.build();
		matningstyp.setBerakningReferensniva(23d);
		matningstyp.setBerakningKonstant(1.0405d);
		matningstyp.setMaxPejlbartDjup(23d);

		var savedMatningstyp = matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp);


		try (var csv = getCsvFileContent(CSVFILE_STANDARD)) {
			var matningar = service.parseCsvImport(csv, ImportFormat.STANDARD, kallsystem);
			assertEquals(5, matningar.size());
			
			var optMatningX = matningar.stream().filter(im -> "matobjektX".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatningX.isPresent());
			var matningX = optMatningX.get();
			assertEquals(1, matningX.getImportFel().size());
			assertTrue(matningX.getImportFel().contains(ImportError.MATOBJEKT_MISSING));
			
			var optMatningY = matningar.stream().filter(im -> "matobjektY".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatningY.isPresent());
			var matningY = optMatningY.get();
			assertEquals(5, matningY.getImportFel().size());
			assertTrue(matningY.getImportFel().contains(ImportError.MATOBJEKT_MISSING));
			assertTrue(matningY.getImportFel().contains(ImportError.AVLASTDATUM_FORMAT));
			assertTrue(matningY.getImportFel().contains(ImportError.INOMDETEKTIONSOMRADE_VALUE));
			assertTrue(matningY.getImportFel().contains(ImportError.AVLASTVARDE_FORMAT));
			assertTrue(matningY.getImportFel().contains(ImportError.KOMMENTAR_LENGTH));

			var optMatningZ = matningar.stream().filter(im -> "matobjektZ".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatningZ.isPresent());
			var matningZ = optMatningZ.get();
			// Gör inget om både beräknat och uppmätt värde saknas om mätningen inte är OK
			assertTrue(!matningZ.getFelkod().equals(Matningsfelkod.OK));
			assertTrue(!matningZ.getImportFel().contains(ImportError.VARDE_MISSING));

			var optMatning1 = matningar.stream().filter(im -> "Mätobjekt1".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatning1.isPresent());
			var matning1 = optMatning1.get();
			assertEquals(Matningsfelkod.OK, matning1.getFelkod());
			assertTrue(!matning1.getImportFel().contains(ImportError.VARDE_MISSING));
			assertEquals("3,0", matning1.getAvlastVarde());
			assertEquals("10,0", matning1.getBeraknatVarde());

		}
	}

	@Test
	// Ska egentligen vara WithMockUserMatrapportor men då kan vi inte skapa testdata
	@WithMockUserAdministrator
	void när_mätningar_läses_in_från_CSVfil_standard_skall_mätningarna_läsas_in_och_transformeras() throws IOException, MatobjektNotFoundException {
		final var kallsystemNamn = StandardKallsystem.RH00Stockholm.getNamn();
		var kallsystem = createKallsystem(kallsystemNamn);

		SaveMatningstyp.SaveMatningstypBuilder matningstypDefault = SaveMatningstyp.builder()
				.matintervallAntalGanger((short) 1)
				.matintervallTidsenhet(MatningstypConstants.TIDSENHET_VECKA)
				.paminnelseDagar((short) 3)
				.aktiv(true);


		{
			var matobjekt = service.getMatobjekt(MATOBJEKT_GRUNDVATTEN);
			var definition = definitionMatningstypService.findById(DEFINITION_NEDMATNING).orElseThrow();
			var matningstyp = matningstypDefault.build();
			matningstyp.setBerakningReferensniva(23d);
			matningstyp.setBerakningKonstant(1.0405d);
			matningstyp.setMaxPejlbartDjup(23d);

			var savedMatningstyp = matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp);
		}

		{
			var matobjektId = service.getMatobjektIdByNamn("MätobjektSattning");
			var matobjekt = service.getMatobjekt(matobjektId.get());
			var definition = definitionMatningstypService.findById(DEFINITION_SATTNING).orElseThrow();
			var matningstyp = matningstypDefault.build();
			matningstyp.setFixpunkt("Fixpunkten");

			var savedMatningstyp = matningstypService.create(matobjekt.getId(), definition.getId(), matningstyp);
		}




		try (var csv = getCsvFileContent(CSVFILE_STANDARD)) {
			var matningar = service.parseCsvImport(csv, ImportFormat.STANDARD, kallsystem);
			assertEquals(5, matningar.size());

			var optMatningX = matningar.stream().filter(im -> "matobjektX".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatningX.isPresent());
			var matningX = optMatningX.get();
			assertEquals(1, matningX.getImportFel().size());
			assertTrue(matningX.getImportFel().contains(ImportError.MATOBJEKT_MISSING));

			var optMatningY = matningar.stream().filter(im -> "matobjektY".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatningY.isPresent());
			var matningY = optMatningY.get();
			assertEquals(5, matningY.getImportFel().size());
			assertTrue(matningY.getImportFel().contains(ImportError.MATOBJEKT_MISSING));
			assertTrue(matningY.getImportFel().contains(ImportError.AVLASTDATUM_FORMAT));
			assertTrue(matningY.getImportFel().contains(ImportError.INOMDETEKTIONSOMRADE_VALUE));
			assertTrue(matningY.getImportFel().contains(ImportError.AVLASTVARDE_FORMAT));
			assertTrue(matningY.getImportFel().contains(ImportError.KOMMENTAR_LENGTH));

			var optMatningZ = matningar.stream().filter(im -> "matobjektZ".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatningZ.isPresent());
			var matningZ = optMatningZ.get();
			// Gör inget om både beräknat och uppmätt värde saknas om mätningen inte är OK
			assertTrue(!matningZ.getFelkod().equals(Matningsfelkod.OK));
			assertTrue(!matningZ.getImportFel().contains(ImportError.VARDE_MISSING));

			var optMatning1 = matningar.stream().filter(im -> "Mätobjekt1".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatning1.isPresent());
			var matning1 = optMatning1.get();
			assertEquals(Matningsfelkod.OK, matning1.getFelkod());
			assertTrue(!matning1.getImportFel().contains(ImportError.VARDE_MISSING));
			assertEquals("3,0", matning1.getAvlastVarde());
			assertEquals("10,525", matning1.getBeraknatVarde());

			var optMatningSattning = matningar.stream().filter(im -> "MätobjektSattning".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatningSattning.isPresent());
			var matningSattning = optMatningSattning.get();
			assertEquals(Matningsfelkod.OK, matningSattning.getFelkod());
			assertTrue(matningSattning.getImportFel().isEmpty());
			assertEquals("7,525", matningSattning.getAvlastVarde());
		}
	}


	@Test
	@WithMockUserMatrapportor 
	void när_mätningar_läses_in_från_CSVfil_instrument_skall_mätningarna_läsas_in() throws IOException {
		final var kallsystemNamn = StandardKallsystem.RH2000_GRANSKAT.getNamn();
		var kallsystem = createKallsystem(kallsystemNamn);


		try (var csv = getCsvFileContent(CSVFILE_INSTRUMENT)) {
			var matningar = service.parseCsvImport(csv, ImportFormat.INSTRUMENT, kallsystem);
			assertEquals(2, matningar.size());

			{
				var matning = matningar.get(0);
				assertEquals(1, matning.getImportFel().size());
				assertTrue(matning.getImportFel().contains(ImportError.INSTUMENT_MISSING));
			}

			{
				var matning = matningar.get(1);
				assertEquals(1, matning.getImportFel().size());
				assertTrue(matning.getImportFel().contains(ImportError.INSTUMENT_MISSING));
			}
		}
	}

	private final Kallsystem createKallsystem(String kallsystemNamn) {
		return Kallsystem
				.builder()
				.namn(kallsystemNamn)
				.beskrivning("beskrivning")
				.defaultGodkand(false)
				.manuellImport(true)
				.tips("tips")
				.build();
	}

	@Test
	@WithMockUserTillstandshandlaggare // Borde vara mätrapportör
	void när_kartinfo_begärs_för_mätobjekt_så_ska_den_hämtas() {
		matobjektRepository.deleteAll();

		addMatobjekt(8);

		MatningstypSearchFilter filter = new MatningstypSearchFilter();
		PageRequest pageRequest = PageRequest.of(0,50);

		final var result = service.getMatobjektMapinfo(filter);
		assertEquals(8, result.size());
	}


	private InputStream getCsvFileContent(String fileName) throws IOException {
		return this.getClass().getResourceAsStream("/csv/" + fileName);
	}
	
}
