package se.metria.matdatabas.service.matobjektgrupp;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.matobjektgrupp.dto.EditMatobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.dto.Matobjektgrupp;
import se.metria.matdatabas.service.matobjektgrupp.exception.MatobjektgruppConflictException;
import se.metria.matdatabas.service.matobjektgrupp.exception.MatobjektgruppNotFoundException;
import se.metria.matdatabas.service.systemlogg.SystemloggRepository;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;
import se.metria.matdatabas.service.systemlogg.handelse.HandelseTyp;

import java.util.ArrayList;
import java.util.List;
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
class MatobjektgruppServiceIT {

	@Autowired
	private MatobjektgruppRepository repository;

	@Autowired
	private MatobjektgruppService service;

	@Autowired
	private SystemloggRepository systemloggRepository;

	@MockBean
	private MatdatabasUser user;

	List<Matobjektgrupp> matobjektgrupper = new ArrayList<>();

	@AfterEach
	public void tearDown() {
		repository.deleteAll();
		systemloggRepository.deleteAll();
	}

	private void addMatobjektgrupper(int numberOfMatobjektgrupper) {
		try {
			matobjektgrupper.clear();
			for (int i = 0; i < numberOfMatobjektgrupper; i++) {
				matobjektgrupper.add(service.createMatobjektgrupp(EditMatobjektgrupp.builder()
						.namn("namn" + i)
						.kategori((short)(i % 6))
						.beskrivning("beskrivning"+ i)
						.kartsymbol((short)(i % 9))
						.build()));
			}
		} catch (MatobjektgruppConflictException e) {
			fail("Misslyckades skapa mätobjektgrupper för testfallet");
		}
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_matobjektgrupper_med_paginering() {
		addMatobjektgrupper(8);

		final var page0 = service.getMatobjektgrupper(0, 5, "id", Sort.Direction.ASC, null);
		assertEquals(2, page0.getTotalPages());
		assertEquals(8, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(5, page0.getContent().size());

		IntStream.rangeClosed(0, 4).forEach(i -> {
			var mg = page0.getContent().get(i);
			assertEquals(matobjektgrupper.get(i).getId(), mg.getId());
			assertEquals(matobjektgrupper.get(i).getNamn(), mg.getNamn());
			assertEquals(matobjektgrupper.get(i).getKategori(), mg.getKategori());
			assertEquals(matobjektgrupper.get(i).getBeskrivning(), mg.getBeskrivning());
		});

		final var page1 = service.getMatobjektgrupper(1, 5, "id", Sort.Direction.ASC, null);
		assertEquals(2, page1.getTotalPages());
		assertEquals(8, page1.getTotalElements());
		assertEquals(1, page1.getNumber());
		assertEquals(3, page1.getContent().size());

		IntStream.rangeClosed(5, 7).forEach(i -> {
			var mg = page1.getContent().get(i-5);
			assertEquals(matobjektgrupper.get(i).getId(), mg.getId());
			assertEquals(matobjektgrupper.get(i).getNamn(), mg.getNamn());
			assertEquals(matobjektgrupper.get(i).getKategori(), mg.getKategori());
			assertEquals(matobjektgrupper.get(i).getBeskrivning(), mg.getBeskrivning());
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_matobjektgrupper_med_paginering_sorterat_på_namn() {
		addMatobjektgrupper(8);

		final var pageAsc = service.getMatobjektgrupper(0, 8, "namn", Sort.Direction.ASC, null);
		assertEquals(1, pageAsc.getTotalPages());
		assertEquals(8, pageAsc.getTotalElements());
		assertEquals(0, pageAsc.getNumber());
		assertEquals(8, pageAsc.getContent().size());

		IntStream.rangeClosed(0, 7).forEach(i -> {
			assertEquals("namn" + i, pageAsc.getContent().get(i).getNamn());
		});

		final var pageDesc = service.getMatobjektgrupper(0, 8, "namn", Sort.Direction.DESC,null);
		assertEquals(1, pageDesc.getTotalPages());
		assertEquals(8, pageDesc.getTotalElements());
		assertEquals(0, pageDesc.getNumber());
		assertEquals(8, pageDesc.getContent().size());

		IntStream.rangeClosed(1, 8).forEach(i -> {
			assertEquals("namn" + (8-i), pageDesc.getContent().get(i-1).getNamn());
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void hämta_matobjektgrupper_med_paginering_och_filtrerat_på_kategori() {
		addMatobjektgrupper(8);

		final var pageKategori0 = service.getMatobjektgrupper(0, 8, "id", Sort.Direction.ASC, 0);
		assertEquals(1, pageKategori0.getTotalPages());
		assertEquals(2, pageKategori0.getTotalElements());
		assertEquals(0, pageKategori0.getNumber());
		assertEquals(2, pageKategori0.getContent().size());

		pageKategori0.forEach(mg -> assertEquals((short)0, mg.getKategori()));

		final var pageKategori5 = service.getMatobjektgrupper(0, 8, "id", Sort.Direction.DESC,5);
		assertEquals(1, pageKategori5.getTotalPages());
		assertEquals(1, pageKategori5.getTotalElements());
		assertEquals(0, pageKategori5.getNumber());
		assertEquals(1, pageKategori5.getContent().size());

		pageKategori5.forEach(mg -> assertEquals((short)5, mg.getKategori()));

		final var pageKategori1 = service.getMatobjektgrupper(0, 8, "namn", Sort.Direction.DESC,1);
		assertEquals(1, pageKategori1.getTotalPages());
		assertEquals(2, pageKategori1.getTotalElements());
		assertEquals(0, pageKategori1.getNumber());
		assertEquals(2, pageKategori1.getContent().size());

		assertEquals((short)1, pageKategori1.getContent().get(0).getKategori());
		assertEquals("namn7", pageKategori1.getContent().get(0).getNamn());
		assertEquals((short)1, pageKategori1.getContent().get(1).getKategori());
		assertEquals("namn1", pageKategori1.getContent().get(1).getNamn());
	}


	@Test
	@Transactional
	@WithMockUserTillstandshandlaggare
	void när_en_tillståndshandläggare_hämtar_med_id_så_ska_mätobjektgruppen_returneras() throws Exception {
		addMatobjektgrupper(8);

		for (Matobjektgrupp mg : this.matobjektgrupper) {
			Matobjektgrupp matobjektgrupp = service.getMatobjektgrupp(mg.getId());
			assertEquals(mg.getId(), matobjektgrupp.getId());
			assertEquals(mg.getNamn(), matobjektgrupp.getNamn());
			assertEquals(mg.getKategori(), matobjektgrupp.getKategori());
			assertEquals(mg.getKartsymbol(), matobjektgrupp.getKartsymbol());
			assertEquals(mg.getBeskrivning(), matobjektgrupp.getBeskrivning());
		};
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_mätobjektgrupp_hämtas_med_felaktigt_id_så_skall_det_inte_hämtas() {
		assertThrows(MatobjektgruppNotFoundException.class, () -> {
			service.getMatobjektgrupp(Integer.MAX_VALUE);
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_en_mätobjektgrupp_skapas_så_ska_den_lagras_korrekt() throws Exception {
		when(user.getId()).thenReturn(0);

		EditMatobjektgrupp editMatobjektgrupp = EditMatobjektgrupp.builder()
				.namn("namn")
				.kategori((short)2)
				.beskrivning("beskrivning")
				.kartsymbol((short)4)
				.build();

		Matobjektgrupp matobjektgrupp = service.createMatobjektgrupp(editMatobjektgrupp);

		assertTrue(matobjektgrupp.getId() > 0);
		assertEquals(editMatobjektgrupp.getNamn(), matobjektgrupp.getNamn());
		assertEquals(editMatobjektgrupp.getKategori(), matobjektgrupp.getKategori());
		assertEquals(editMatobjektgrupp.getBeskrivning(), matobjektgrupp.getBeskrivning());
		assertEquals(editMatobjektgrupp.getKartsymbol(), matobjektgrupp.getKartsymbol());
		assertNotNull(matobjektgrupp.getSkapadDatum());
		assertNotNull(matobjektgrupp.getAndradDatum());
		assertEquals(0, matobjektgrupp.getAndradAvId());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny mätobjektgrupp"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void fel_ska_inträffa_när_en_mätobjektgrupp_skapas_med_existerande_namn() throws Exception{
		addMatobjektgrupper(1);

		final EditMatobjektgrupp editMatobjektgrupp = EditMatobjektgrupp.builder()
				.namn(matobjektgrupper.get(0).getNamn())
				.kategori((short)2)
				.beskrivning("annan beskrivning")
				.kartsymbol((short)4)
				.build();

		assertThrows(MatobjektgruppConflictException.class, () -> {
			service.createMatobjektgrupp(editMatobjektgrupp);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny mätobjektgrupp"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_ett_mätobjektgrupp_uppdateras_så_ska_allt_lagras_korrekt() throws Exception {
		when(user.getId()).thenReturn(0);

		addMatobjektgrupper(1);

		Matobjektgrupp matobjektgrupp = matobjektgrupper.get(0);
		EditMatobjektgrupp editMatobjektgrupp = EditMatobjektgrupp.builder()
				.namn("nytt namn")
				.kategori((short)5)
				.beskrivning("ny beskrivning")
				.kartsymbol((short)8)
				.build();

		Matobjektgrupp uppdateradMatobjektgrupp = service.updateMatobjektgrupp(matobjektgrupper.get(0).getId(), editMatobjektgrupp);

		assertEquals(matobjektgrupp.getId(), uppdateradMatobjektgrupp.getId());
		assertEquals(editMatobjektgrupp.getNamn(), uppdateradMatobjektgrupp.getNamn());
		assertEquals(editMatobjektgrupp.getKategori(), uppdateradMatobjektgrupp.getKategori());
		assertEquals(editMatobjektgrupp.getBeskrivning(), uppdateradMatobjektgrupp.getBeskrivning());
		assertEquals(editMatobjektgrupp.getKartsymbol(), uppdateradMatobjektgrupp.getKartsymbol());
		assertEquals(matobjektgrupp.getSkapadDatum(), uppdateradMatobjektgrupp.getSkapadDatum());
		assertTrue(uppdateradMatobjektgrupp.getAndradDatum().isAfter(matobjektgrupp.getAndradDatum()));
		assertEquals(0, uppdateradMatobjektgrupp.getAndradAvId());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(2, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny mätobjektgrupp"))
				.collect(Collectors.toList()).size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Inställningar ändrade för mätobjektgrupp"))
				.collect(Collectors.toList()).size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void fel_ska_inträffa_när_en_mätobjektgrupp_som_inte_finns_uppdateras() {
		EditMatobjektgrupp editMatobjektgrupp = EditMatobjektgrupp.builder()
				.namn("nytt namn")
				.kategori((short)5)
				.beskrivning("ny beskrivning")
				.kartsymbol((short)8)
				.build();

		assertThrows(MatobjektgruppNotFoundException.class, () -> {
			service.updateMatobjektgrupp(Integer.MAX_VALUE, editMatobjektgrupp);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(0, systemloggEntries.size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_om_existerande_mätobjektgruppnamn_finns() {
		addMatobjektgrupper(8);

		matobjektgrupper.forEach(mg ->  {
			assertTrue(service.exists(mg.getNamn()));
		});
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void kontrollera_om_ett_icke_existerande_mätobjektgruppnamn_finns() {
		addMatobjektgrupper(8);

		assertFalse(service.exists("nytt-och-fräscht-mätobjektgruppnamn"));
	}

	@Test
	@Transactional
	@WithMockUserTillstandshandlaggare
	void ta_bort_mätobjektgrupp_som_finns() throws Exception {
		addMatobjektgrupper(1);

		Integer id = matobjektgrupper.get(0).getId();

		service.getMatobjektgrupp(id);
		service.deleteMatobjektgrupp(id);

		assertThrows(MatobjektgruppNotFoundException.class, () -> {
			service.getMatobjektgrupp(id);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(2, systemloggEntries.size());
		assertEquals(
				String.format("Ny mätobjektgrupp #%d '%s' skapad", matobjektgrupper.get(0).getId(), matobjektgrupper.get(0).getNamn()),
				systemloggEntries.get(0).getBeskrivning());
		assertEquals(
				String.format("Mätobjektgrupp #%d '%s' raderades", matobjektgrupper.get(0).getId(), matobjektgrupper.get(0).getNamn()),
				systemloggEntries.get(1).getBeskrivning());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void ta_bort_mätobjektgrupp_som_inte_finns() {
		assertThrows(MatobjektgruppNotFoundException.class, () -> {
			service.deleteMatobjektgrupp(Integer.MAX_VALUE);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(0, systemloggEntries.size());
	}

	@Test
	@WithMockUserMatrapportor
	void en_mätrapportör_ska_inte_hantera_mätobjektgrupper_men_får_läsa() {
		service.getMatobjektgrupper(0, 5, "id", Sort.Direction.ASC, null);

		try {
			service.getMatobjektgrupp(1);
		} catch (MatobjektgruppNotFoundException e) {
			// Det fanns ingen med det är ok, vi ville bara testa att man får göra anropet
		}

		assertThrows(AccessDeniedException.class, () -> {
			service.createMatobjektgrupp(new EditMatobjektgrupp());
		});

		assertThrows(AccessDeniedException.class, () -> {
			service.updateMatobjektgrupp(1, new EditMatobjektgrupp());
		});

		assertThrows(AccessDeniedException.class, () -> {
			service.deleteMatobjektgrupp(1);
		});
		assertThrows(AccessDeniedException.class, () -> {
			service.exists("blabla");
		});
	}

	private List<SystemloggEntry> getSystemloggEntries() {
		return systemloggRepository.findAll().stream()
				.map(SystemloggEntry::fromEntity)
				.filter(e -> e.getHandelse().equals(HandelseTyp.MATNINGSTYP.getId()))
				.collect(Collectors.toList());
	}
}
