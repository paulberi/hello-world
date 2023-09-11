package se.metria.matdatabas.service.matrunda;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.matningstyp.MatningstypRepository;
import se.metria.matdatabas.service.matobjekt.MatobjektRepository;
import se.metria.matdatabas.service.matrunda.dto.EditMatrunda;
import se.metria.matdatabas.service.matrunda.dto.ListMatrunda;
import se.metria.matdatabas.service.matrunda.dto.Matrunda;
import se.metria.matdatabas.service.matrunda.dto.MatrundaMatningstyp;
import se.metria.matdatabas.service.matrunda.exception.MatrundaConflictException;
import se.metria.matdatabas.service.matrunda.exception.MatrundaNotFoundException;
import se.metria.matdatabas.service.systemlogg.SystemloggRepository;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;
import se.metria.matdatabas.service.systemlogg.handelse.HandelseTyp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class MatrundaServiceIT {

	@Autowired
	private MatrundaRepository repository;

	@Autowired
	private MatrundaService service;

	@Autowired
	private SystemloggRepository systemloggRepository;

	@Autowired
	private MatningstypRepository matningstypRepository;

	@Autowired
	private MatobjektRepository matobjektRepository;

	@MockBean
	private MatdatabasUser user;

	List<Matrunda> matrundor = new ArrayList<>();

	@BeforeEach
	void setUp() {
		when(user.getId()).thenReturn(0);
		matrundor.clear();
	}

	@AfterEach
	void tearDown() {
		repository.deleteAll();
		matningstypRepository.deleteAll();
		matobjektRepository.deleteAll();
		systemloggRepository.deleteAll();
	}

	private void addMatrundor(int count, boolean aktiv) {
		try {
			for (int i = 0; i < count; i++) {
				matrundor.add(service.createMatrunda(EditMatrunda.builder()
						.namn(i + UUID.randomUUID().toString())
						.aktiv(aktiv)
						.beskrivning("beskrivning"+ i)
						.build()));
			}
		} catch (MatrundaConflictException e) {
			fail("Misslyckades skapa mätrundor för testfallet: " + e.getMessage());
		}
	}

	private void addMatrundor(int count) {
		addMatrundor(count, true);
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_mätrundor_med_paginering() {
		addMatrundor(8);

		final var page0 = service.getMatrundor(0, 5, "id", Sort.Direction.ASC);
		assertEquals(2, page0.getTotalPages());
		assertEquals(8, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(5, page0.getContent().size());

		IntStream.rangeClosed(0, 4).forEach(i -> {
			var mr = page0.getContent().get(i);
			assertEquals(matrundor.get(i).getId(), mr.getId());
			assertEquals(matrundor.get(i).getNamn(), mr.getNamn());
			assertEquals(matrundor.get(i).getBeskrivning(), mr.getBeskrivning());
		});

		final var page1 = service.getMatrundor(1, 5, "id", Sort.Direction.ASC);
		assertEquals(2, page1.getTotalPages());
		assertEquals(8, page1.getTotalElements());
		assertEquals(1, page1.getNumber());
		assertEquals(3, page1.getContent().size());

		IntStream.rangeClosed(5, 7).forEach(i -> {
			var mr = page1.getContent().get(i-5);
			assertEquals(matrundor.get(i).getId(), mr.getId());
			assertEquals(matrundor.get(i).getNamn(), mr.getNamn());
			assertEquals(matrundor.get(i).getBeskrivning(), mr.getBeskrivning());
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_alla_aktiva_matrundor() {
		addMatrundor(8, true);
		addMatrundor(8, false);

		Page<ListMatrunda> matrundor = service.getMatrundor(0, 20, "id", Sort.Direction.ASC, true);
		assertEquals(8, matrundor.getTotalElements());

		Page<ListMatrunda> matrundorPaged = service.getMatrundor(0, 20, "id", Sort.Direction.ASC);
		assertEquals(16, matrundorPaged.getTotalElements());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_mätrundor_med_paginering_sorterat_pa_namn() {
		addMatrundor(3);

		var page = service.getMatrundor(0, 3, "namn", Sort.Direction.DESC);
		assertEquals(1, page.getTotalPages());
		assertEquals(3, page.getTotalElements());
		assertEquals(0, page.getNumber());

		IntStream.rangeClosed(0, 2).forEach(i -> {
			var mr = page.getContent().get(i);
			assertEquals(matrundor.get(2-i).getId(), mr.getId());
			assertEquals(matrundor.get(2-i).getNamn(), mr.getNamn());
			assertEquals(matrundor.get(2-i).getBeskrivning(), mr.getBeskrivning());
		});
	}

	@Test
	@Sql({"/data/MatrundaServiceIT.sql"})
	@WithMockUserTillstandshandlaggare
	void hämta_mätrundor_med_paginering_som_har_mätobjekt() throws Exception {
		Matrunda matrunda1 = service.createMatrunda(EditMatrunda.builder()
				.namn("namn3")
				.aktiv(true)
				.beskrivning("beskrivning1")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10001).ordning((short)1).build())
				).build());
		Matrunda matrunda2 = service.createMatrunda(EditMatrunda.builder()
				.namn("namn1")
				.aktiv(false)
				.beskrivning("beskrivning2")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10002).ordning((short)2).build(),
						MatrundaMatningstyp.builder().matningstypId(10003).ordning((short)3).build())
				).build());
		Matrunda matrunda3 = service.createMatrunda(EditMatrunda.builder()
				.namn("namn2")
				.aktiv(true)
				.beskrivning("beskrivning3")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10004).ordning((short)4).build(),
						MatrundaMatningstyp.builder().matningstypId(10005).ordning((short)5).build(),
						MatrundaMatningstyp.builder().matningstypId(10006).ordning((short)6).build())
				).build());

		var page = service.getMatrundor(0, 10, "namn", Sort.Direction.ASC);
		assertEquals(1, page.getTotalPages());
		assertEquals(3, page.getTotalElements());
		assertEquals(0, page.getNumber());

		ListMatrunda listMatrunda = page.getContent().get(0);
		assertEquals(matrunda2.getId(), listMatrunda.getId());
		assertEquals(matrunda2.getNamn(), listMatrunda.getNamn());
		assertEquals(matrunda2.getBeskrivning(), listMatrunda.getBeskrivning());
		assertEquals(matrunda2.getAktiv(), listMatrunda.getAktiv());
		assertEquals(2, listMatrunda.getAntalMatobjekt());

		listMatrunda = page.getContent().get(1);
		assertEquals(matrunda3.getId(), listMatrunda.getId());
		assertEquals(matrunda3.getNamn(), listMatrunda.getNamn());
		assertEquals(matrunda3.getBeskrivning(), listMatrunda.getBeskrivning());
		assertEquals(matrunda3.getAktiv(), listMatrunda.getAktiv());
		assertEquals(3, listMatrunda.getAntalMatobjekt());

		listMatrunda = page.getContent().get(2);
		assertEquals(matrunda1.getId(), listMatrunda.getId());
		assertEquals(matrunda1.getNamn(), listMatrunda.getNamn());
		assertEquals(matrunda1.getBeskrivning(), listMatrunda.getBeskrivning());
		assertEquals(matrunda1.getAktiv(), listMatrunda.getAktiv());
		assertEquals(1, listMatrunda.getAntalMatobjekt());
	}

	@Test
	@Transactional
	@Sql({"/data/MatrundaServiceIT.sql"})
	@WithMockUserTillstandshandlaggare
	void när_en_tillståndshandläggare_hämtar_med_id_så_ska_mätrundan_returneras() throws Exception {

		Matrunda inMatrunda = service.createMatrunda(EditMatrunda.builder()
				.namn("namn")
				.aktiv(true)
				.beskrivning("beskrivning")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10001).ordning((short) 6).build(),
						MatrundaMatningstyp.builder().matningstypId(10002).ordning((short) 5).build(),
						MatrundaMatningstyp.builder().matningstypId(10003).ordning((short) 4).build(),
						MatrundaMatningstyp.builder().matningstypId(10004).ordning((short) 3).build(),
						MatrundaMatningstyp.builder().matningstypId(10005).ordning((short) 2).build(),
						MatrundaMatningstyp.builder().matningstypId(10006).ordning((short) 1).build())
				).build());

		Matrunda outMatrunda = service.getMatrunda(inMatrunda.getId());

		assertEquals(inMatrunda.getId(), outMatrunda.getId());
		assertEquals(inMatrunda.getNamn(), outMatrunda.getNamn());
		assertEquals(inMatrunda.getAktiv(), outMatrunda.getAktiv());
		assertEquals(inMatrunda.getBeskrivning(), outMatrunda.getBeskrivning());
		assertEquals(inMatrunda.getMatningstyper().size(), outMatrunda.getMatningstyper().size());

		for (MatrundaMatningstyp mrmt : inMatrunda.getMatningstyper()) {
			assertTrue(outMatrunda.getMatningstyper().contains(mrmt));
		}
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_mätrunda_hämtas_med_felaktigt_id_så_skall_det_inte_hämtas() {
		assertThrows(MatrundaNotFoundException.class, () -> {
			service.getMatrunda(Integer.MAX_VALUE);
		});
	}

	@Test
	@Transactional
	@Sql({"/data/MatrundaServiceIT.sql"})
	@WithMockUserTillstandshandlaggare
	void när_en_mätrunda_skapas_så_ska_den_lagras_korrekt() throws Exception {
		when(user.getId()).thenReturn(0);

		EditMatrunda editMatrunda = EditMatrunda.builder()
				.namn("namn")
				.aktiv(false)
				.beskrivning("beskrivning")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10005).ordning((short) 1).build()))
				.build();

		Matrunda matrunda = service.createMatrunda(editMatrunda);

		assertTrue(matrunda.getId() > 0);
		assertEquals(editMatrunda.getNamn(), matrunda.getNamn());
		assertEquals(editMatrunda.getAktiv(), matrunda.getAktiv());
		assertEquals(editMatrunda.getBeskrivning(), matrunda.getBeskrivning());
		assertEquals(editMatrunda.getMatningstyper().get(0), matrunda.getMatningstyper().get(0));
		assertNotNull(matrunda.getSkapadDatum());
		assertNotNull(matrunda.getAndradDatum());
		assertEquals(0, matrunda.getAndradAvId());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny mätrunda"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void fel_ska_inträffa_när_en_mätrundaskapas_med_existerande_namn() throws Exception {
		addMatrundor(1);

		final EditMatrunda editMatrunda = EditMatrunda.builder()
				.namn(matrundor.get(0).getNamn())
				.aktiv(true)
				.beskrivning("annan beskrivning")
				.build();

		assertThrows(MatrundaConflictException.class, () -> {
			service.createMatrunda(editMatrunda);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny mätrunda"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@Sql({"/data/MatrundaServiceIT.sql"})
	@WithMockUserTillstandshandlaggare
	void när_en_mätrunda_uppdateras_så_ska_allt_lagras_korrekt() throws Exception {
		when(user.getId()).thenReturn(0);

		addMatrundor(1);

		Matrunda matrunda = matrundor.get(0);

		assertTrue(matrunda.getMatningstyper().isEmpty());

		EditMatrunda editMatrunda = EditMatrunda.builder()
				.namn("nytt namn")
				.aktiv(!matrunda.getAktiv())
				.beskrivning("ny beskrivning")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10001).ordning((short) 2).build(),
						MatrundaMatningstyp.builder().matningstypId(10002).ordning((short) 1).build()))
				.build();

		Matrunda uppdateradMatrunda = service.updateMatrunda(matrundor.get(0).getId(), editMatrunda);

		assertEquals(matrunda.getId(), uppdateradMatrunda.getId());
		assertEquals(editMatrunda.getNamn(), uppdateradMatrunda.getNamn());
		assertEquals(editMatrunda.getAktiv(), uppdateradMatrunda.getAktiv());
		assertEquals(editMatrunda.getBeskrivning(), uppdateradMatrunda.getBeskrivning());
		assertEquals(2, uppdateradMatrunda.getMatningstyper().size());

		for (MatrundaMatningstyp mrmt : editMatrunda.getMatningstyper()) {
			assertTrue(uppdateradMatrunda.getMatningstyper().contains(mrmt));
		}

		assertEquals(matrunda.getSkapadDatum(), uppdateradMatrunda.getSkapadDatum());
		assertTrue(uppdateradMatrunda.getAndradDatum().isAfter(matrunda.getAndradDatum()));
		assertEquals(0, uppdateradMatrunda.getAndradAvId());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();

		assertEquals(2, systemloggEntries.size());
		assertEquals(1, (int) systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny mätrunda")).count());
		assertEquals(1, (int) systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Inställningar ändrade för mätrunda")).count());

		editMatrunda = EditMatrunda.builder()
				.namn("nytt namn")
				.aktiv(!matrunda.getAktiv())
				.beskrivning("ny beskrivning")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10001).ordning((short) 1).build(),
						MatrundaMatningstyp.builder().matningstypId(10003).ordning((short) 2).build(),
						MatrundaMatningstyp.builder().matningstypId(10006).ordning((short) 3).build()))
				.build();

		uppdateradMatrunda = service.updateMatrunda(matrundor.get(0).getId(), editMatrunda);

		assertEquals(3, uppdateradMatrunda.getMatningstyper().size());
		for (MatrundaMatningstyp mrmt : editMatrunda.getMatningstyper()) {
			assertTrue(uppdateradMatrunda.getMatningstyper().contains(mrmt));
		}

		systemloggEntries = getSystemloggEntries();

		assertEquals(3, systemloggEntries.size());
		assertEquals(1, (int) systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny mätrunda")).count());
		assertEquals(2, (int) systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Inställningar ändrade för mätrunda")).count());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void fel_ska_inträffa_när_en_mätrunda_som_inte_finns_uppdateras() {
		EditMatrunda editMatrunda = EditMatrunda.builder()
				.namn("nytt namn")
				.aktiv(true)
				.beskrivning("ny beskrivning")
				.build();

		assertThrows(MatrundaNotFoundException.class, () -> {
			service.updateMatrunda(Integer.MAX_VALUE, editMatrunda);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(0, systemloggEntries.size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_om_existerande_mätrundenamn_finns() {
		addMatrundor(8);

		matrundor.forEach(mr ->  {
			assertTrue(service.exists(mr.getNamn()));
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_om_ett_icke_existerande_mätrundenamn_finns() {
		addMatrundor(8);

		assertFalse(service.exists("nytt-och-fräscht-mätrundenamn"));
	}

	@Test
	@Transactional
	@Sql({"/data/MatrundaServiceIT.sql"})
	@WithMockUserTillstandshandlaggare
	void ta_bort_mätrunda_som_finns() throws Exception {

		EditMatrunda editMatrunda = EditMatrunda.builder()
				.namn("namn")
				.aktiv(false)
				.beskrivning("beskrivning")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10005).ordning((short) 1).build(),
						MatrundaMatningstyp.builder().matningstypId(10003).ordning((short) 2).build()))
				.build();

		Matrunda matrunda = service.createMatrunda(editMatrunda);

		service.getMatrunda(matrunda.getId());
		service.deleteMatrunda(matrunda.getId());

		assertThrows(MatrundaNotFoundException.class, () -> {
			service.getMatrunda(matrunda.getId());
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(2, systemloggEntries.size());
		assertEquals(
				String.format("Ny mätrunda #%d '%s' skapad", matrunda.getId(), matrunda.getNamn()),
				systemloggEntries.get(0).getBeskrivning());
		assertEquals(
				String.format("Mätrunda #%d '%s' raderades", matrunda.getId(), matrunda.getNamn()),
				systemloggEntries.get(1).getBeskrivning());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void ta_bort_mätrunda_som_inte_finns() {
		assertThrows(MatrundaNotFoundException.class, () -> {
			service.deleteMatrunda(Integer.MAX_VALUE);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(0, systemloggEntries.size());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_modifiera_mätrundor() {
		assertThrows(AccessDeniedException.class, () -> {
			service.createMatrunda(new EditMatrunda());
		});

		assertThrows(AccessDeniedException.class, () -> {
			service.updateMatrunda(1, new EditMatrunda());
		});

		assertThrows(AccessDeniedException.class, () -> {
			service.deleteMatrunda(1);
		});
	}

	private List<SystemloggEntry> getSystemloggEntries() {
		return systemloggRepository.findAll().stream()
				.map(SystemloggEntry::fromEntity)
				.filter(e -> e.getHandelse().equals(HandelseTyp.MATNINGSTYP.getId()))
				.collect(Collectors.toList());
	}
}
