package se.metria.matdatabas.service.anvandare;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.TemplateEngine;
import se.metria.matdatabas.MatdatabasConfiguration;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.anvandare.dto.EditAnvandare;
import se.metria.matdatabas.service.anvandare.exception.*;
import se.metria.matdatabas.service.anvandare.behorighet.Behorighet;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.systemlogg.SystemloggRepository;
import se.metria.matdatabas.service.systemlogg.SystemloggService;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;
import se.metria.matdatabas.service.systemlogg.handelse.HandelseTyp;
import se.metria.xplore.keycloak.service.KeyCloakService;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MatdatabasConfiguration.class, SystemloggService.class }))
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class AnvandareServiceIT {

	List<Anvandare> anvandare = new ArrayList<>();

	@TestConfiguration
	static class AnvandareServiceTestContextConfiguration {

		@Autowired
		private AnvandareRepository repository;

		@Autowired
		private SystemloggService systemloggService;

		@MockBean
		private KeyCloakService keyCloakService;

		@MockBean
		private JavaMailSender javaMailSender;

		@Autowired
		private TemplateEngine emailTemplateEngine;

		@Bean
		public AnvandareService getAnvandareService() {
			return new AnvandareService(repository, systemloggService, keyCloakService, javaMailSender, emailTemplateEngine);
		}
	}

	@Autowired
	private AnvandareRepository repository;

	@Autowired
	private AnvandareService service;

	@Autowired
	private SystemloggService systemloggService;

	@Autowired
	private SystemloggRepository systemloggRepository;

	@Autowired
	private KeyCloakService keyCloakService;

	@Autowired
	private JavaMailSender javaMailSender;

	@MockBean
	private MatdatabasUser user;

	@MockBean
	private AnvandargruppService anvandargruppService;

	@AfterEach
	public void tearDown() {
		systemloggRepository.deleteAll();
		repository.deleteAll();
	}

	@BeforeEach
	public void beforeEach() {
		when(keyCloakService.createUser(any(),any(), any(),any())).thenReturn(KeyCloakService.CreateUserResult.USER_CREATED);
		when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));
	}

	private void addAnvandare(int numberOfUsers) {
		try {
			anvandare.clear();
			for (int i = 0; i < numberOfUsers; i++) {
				anvandare.add(service.createAnvandare(EditAnvandare.builder()
						.namn("namn" + i)
						.foretag("foretag" + i)
						.aktiv(i % 2 == 0)
						.inloggningsnamn("inloggningsnamn" + i)
						.behorighet((short) (i % 3))
						.skickaEpost(i % 2 == 1)
						.epost("epost" + i)
						.build()));
			}
		} catch (AnvandarnamnConflictException | MessagingException e) {
			fail("Misslyckades skapa användare för testfallet");
		}
	}

	@Test
	void hämta_användare_med_paginering() {
		addAnvandare(8);

		final var page0 = service.getAnvandare(0, 5, "id", Sort.Direction.ASC, true);
		assertEquals(2, page0.getTotalPages());
		assertEquals(8+1, page0.getTotalElements()); // En extra pga systemanvändare i bastestdata
		assertEquals(0, page0.getNumber());
		assertEquals(5, page0.getContent().size());

//		IntStream.rangeClosed(0, 4).forEach(i -> {
//			var a = page0.getContent().get(i);
//			assertEquals("namn" + i, a.getNamn());
//			assertEquals("foretag" + i, a.getForetag());
//			assertEquals(i % 2 == 0, a.getAktiv());
//			assertEquals("inloggningsnamn" + i, a.getInloggningsnamn());
//			assertEquals(i % 3, a.getBehorighet().intValue());
//			assertEquals(i, a.getDefaultKartlagerId());
//			assertEquals(i % 2 == 1, a.getSkickaEpost());
//			assertEquals("epost" + i, a.getEpost());
//		});

		final var page1 = service.getAnvandare(1, 5, "id", Sort.Direction.ASC, true);
		assertEquals(2, page1.getTotalPages());
		assertEquals(8+1, page1.getTotalElements()); // En extra pga systemanvändare i bastestdata
		assertEquals(1, page1.getNumber());
		assertEquals(3+1, page1.getContent().size()); // En extra pga systemanvändare i bastestdata

//		IntStream.rangeClosed(5, 7).forEach(i -> {
//			var a = page1.getContent().get(i - 5);
//			assertEquals("namn" + i, a.getNamn());
//			assertEquals("foretag" + i, a.getForetag());
//			assertEquals(i % 2 == 0, a.getAktiv());
//			assertEquals("inloggningsnamn" + i, a.getInloggningsnamn());
//			assertEquals(i % 3, a.getBehorighet().intValue());
//			assertEquals(i, a.getDefaultKartlagerId());
//			assertEquals(i % 2 == 1, a.getSkickaEpost());
//			assertEquals("epost" + i, a.getEpost());
//		});
	}

	@Test
	void hämta_användare_med_paginering_och_omvänd_sortering() {
		addAnvandare(8);

		final var page0 = service.getAnvandare(0, 10, "id", Sort.Direction.DESC, true);
		assertEquals(1, page0.getTotalPages());
		assertEquals(8+1, page0.getTotalElements()); // En extra pga systemanvändare i bastestdata
		assertEquals(0, page0.getNumber());
		assertEquals(8+1, page0.getContent().size());// En extra pga systemanvändare i bastestdata

		IntStream.rangeClosed(0, 7).forEach(i -> {
			var a = page0.getContent().get(7 - i);
			assertEquals("namn" + i, a.getNamn());
			assertEquals("foretag" + i, a.getForetag());
			assertEquals(i % 2 == 0, a.getAktiv());
			assertEquals("inloggningsnamn" + i, a.getInloggningsnamn());
			assertEquals(i % 3, a.getBehorighet().intValue());
			assertEquals(i % 2 == 1, a.getSkickaEpost());
			assertEquals("epost" + i, a.getEpost());
		});
	}

	@Test
	void hämta_endast_aktiva_användare_med_paginering() {
		addAnvandare(8);

		final var page0 = service.getAnvandare(0, 5, "id", Sort.Direction.ASC, false);
		assertEquals(1, page0.getTotalPages());
		assertEquals(5, page0.getTotalElements()); // En extra pga systemanvändare i bastestdata
		assertEquals(0, page0.getNumber());
		assertEquals(5, page0.getContent().size()); // En extra pga systemanvändare i bastestdata

		for(Anvandare a : page0.getContent()) {
			assertEquals(true, a.getAktiv());
		}
	}

	@Test
	void när_en_administratör_hämtar_med_id_så_ska_användaren_returneras() throws AnvandareNotFoundException {
		addAnvandare(8);

		for (Anvandare a : this.anvandare) {
			Anvandare anvandare = service.getAnvandare(a.getId());
			assertEquals(a.getId(), anvandare.getId());
			assertEquals(a.getNamn(), anvandare.getNamn());
		};
	}

	@Test
	void när_en_användare_hämtas_med_felaktigt_id_så_skall_det_inte_hämtas() {
		addAnvandare(8);

		assertThrows(AnvandareNotFoundException.class, () -> {
			service.getAnvandare(Integer.MAX_VALUE);
		});
	}
	
	@Test
	void när_en_användare_skapas_så_ska_den_lagras_korrekt() throws AnvandarnamnConflictException, MessagingException {

		EditAnvandare editAnvandare = EditAnvandare.builder()
				.namn("namn")
				.foretag("foretag")
				.aktiv(false)
				.inloggningsnamn("inloggningsnamn")
				.behorighet((short)1)
				.skickaEpost(true)
				.epost("epost")
				.build();

		Anvandare anvandare = service.createAnvandare(editAnvandare);

		Mockito.verify(keyCloakService).createUser(eq("inloggningsnamn"), any(), any(), any());
		Mockito.verify(javaMailSender).send(any(MimeMessage.class));
		assertTrue(anvandare.getId() > 0);
		assertEquals(editAnvandare.getNamn(), anvandare.getNamn());
		assertEquals(editAnvandare.getForetag(), anvandare.getForetag());
		assertEquals(editAnvandare.getAktiv(), anvandare.getAktiv());
		assertEquals(editAnvandare.getInloggningsnamn(), anvandare.getInloggningsnamn());
		assertEquals(editAnvandare.getBehorighet(), anvandare.getBehorighet());
		assertEquals(editAnvandare.getDefaultKartlagerId(), anvandare.getDefaultKartlagerId());
		assertEquals(editAnvandare.getSkickaEpost(), anvandare.getSkickaEpost());
		assertEquals(editAnvandare.getEpost(), anvandare.getEpost());
		assertNotNull(anvandare.getSkapadDatum());
		assertNotNull(anvandare.getAndradDatum());
		assertEquals(0, anvandare.getAndradAvId());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny användare"))
				.collect(Collectors.toList()).size());
	}

	@Test
	void fel_ska_inträffa_när_en_användare_skapas_med_existerande_användarnamn() {
		addAnvandare(1);
		
		EditAnvandare editAnvandare = EditAnvandare.builder()
				.namn("nytt namn")
				.foretag("nytt foretag")
				.aktiv(false)
				.inloggningsnamn(anvandare.get(0).getInloggningsnamn())
				.behorighet((short)1)
				.defaultKartlagerId(2)
				.skickaEpost(true)
				.epost("ny epost")
				.build();

		assertThrows(AnvandarnamnConflictException.class, () -> {
			service.createAnvandare(editAnvandare);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ny användare"))
				.collect(Collectors.toList()).size());
	}

	@Test
	void när_användare_uppdateras_så_ska_allt_lagras_korrekt() throws AnvandareNotFoundException {
		addAnvandare(1);

		Anvandare anvandare = this.anvandare.get(0);

		EditAnvandare editAnvandare = EditAnvandare.builder()
				.namn("namnnamn")
				.foretag("foretagforetag")
				.aktiv(!anvandare.getAktiv())
				.inloggningsnamn(anvandare.getInloggningsnamn()) // Inloggningsnamn får inte ändras
				.behorighet((short)0)
				.skickaEpost(!anvandare.getSkickaEpost())
				.epost("epostepost")
				.build();

		Anvandare uppdateradAnvandare = service.updateAnvandare(anvandare.getId(), editAnvandare);
		assertEquals(anvandare.getId(), uppdateradAnvandare.getId());
		assertEquals(editAnvandare.getNamn(), uppdateradAnvandare.getNamn());
		assertEquals(editAnvandare.getForetag(), uppdateradAnvandare.getForetag());
		assertEquals(editAnvandare.getAktiv(),uppdateradAnvandare.getAktiv());
		assertEquals(editAnvandare.getInloggningsnamn(), uppdateradAnvandare.getInloggningsnamn());
		assertEquals(editAnvandare.getBehorighet(), uppdateradAnvandare.getBehorighet());
		assertEquals(editAnvandare.getDefaultKartlagerId(), uppdateradAnvandare.getDefaultKartlagerId());
		assertEquals(editAnvandare.getSkickaEpost(), uppdateradAnvandare.getSkickaEpost());
		assertEquals(editAnvandare.getEpost(), uppdateradAnvandare.getEpost());
		assertEquals(uppdateradAnvandare.getSkapadDatum(), anvandare.getSkapadDatum());
		assertTrue(uppdateradAnvandare.getAndradDatum().isAfter(anvandare.getAndradDatum()));
		assertEquals(0, uppdateradAnvandare.getAndradAvId());

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(2, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ändrade inställningar för användare"))
				.collect(Collectors.toList()).size());
	}

	@Test
	void när_användare_uppdateras_med_felaktigt_id_skall_det_inte_uppdateras() {
		EditAnvandare editAnvandare = EditAnvandare.builder()
				.namn("namn")
				.foretag("foretag")
				.aktiv(false)
				.inloggningsnamn("inloggningsnamn")
				.behorighet((short)1)
				.defaultKartlagerId(2)
				.skickaEpost(true)
				.epost("epost")
				.build();

		assertThrows(AnvandareNotFoundException.class, () -> {
			service.updateAnvandare(Integer.MAX_VALUE, editAnvandare);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(0, systemloggEntries.size());
		assertEquals(0, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Ändrade säkerhetsinställningar för användare"))
				.collect(Collectors.toList()).size());
	}

	@Test
	void kontrollera_om_existerande_användarnamn_finns() {
		addAnvandare(8);

		anvandare.forEach(a ->  {
			assertTrue(service.exists(a.getInloggningsnamn()));
		});
	}

	@Test
	void kontrollera_om_ett_icke_existerande_användarnamn_finns() {
		addAnvandare(8);

		assertFalse(service.exists("nytt-och-fräscht-användarnamn"));
	}

	@Test
	void ta_bort_användare_som_finns() throws AnvandareNotFoundException, AnvandareHasLoggedInException {
		addAnvandare(1);

		Integer anvandarId = anvandare.get(0).getId();

		service.getAnvandare(anvandarId);
		service.deleteAnvandare(anvandarId);

		assertThrows(AnvandareNotFoundException.class, () -> {
			service.getAnvandare(anvandarId);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(2, systemloggEntries.size());
		assertEquals(1, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Tog bort användare"))
				.collect(Collectors.toList()).size());
	}

	@Test
	void ta_bort_användare_som_inte_finns() {
		addAnvandare(8);

		assertThrows(AnvandareNotFoundException.class, () -> {
			service.deleteAnvandare(anvandare.get(0).getId() - 1);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(8, systemloggEntries.size());
		assertEquals(0, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Tog bort användare"))
				.collect(Collectors.toList()).size());
	}

	@Test
	void försök_ta_bort_användare_som_finns_men_loggat_in() {
		addAnvandare(1);

		assertThrows(AnvandareHasLoggedInException.class, () -> {
			Integer anvandarId = anvandare.get(0).getId();
			service.updateInloggadSenast(anvandarId);
			service.deleteAnvandare(anvandarId);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(0, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Tog bort användare"))
				.collect(Collectors.toList()).size());
	}

	@Test
	void anonymisera_användare() throws AnvandareNotFoundException {
		addAnvandare(8);

		for (Anvandare a : this.anvandare) {
			assertNotNull(a.getNamn());
			assertNotNull(a.getForetag());
			assertNotNull(a.getInloggningsnamn());
			assertNotNull(a.getEpost());
			//assertNotNull(anvandare.getAnvandargrupper());

			service.anonymizeAnvandare(a.getId());

			Anvandare anonymAnvandare = service.getAnvandare(a.getId());
			String anonymizedName = Behorighet.getNameById(a.getBehorighet()) + a.getId();

			assertEquals(anonymizedName, anonymAnvandare.getNamn());
			assertNull(anonymAnvandare.getForetag());
			assertEquals(anonymizedName, anonymAnvandare.getInloggningsnamn());
			assertNull(anonymAnvandare.getEpost());
			assertFalse(anonymAnvandare.getAktiv());
			assertNull(anonymAnvandare.getAnvandargrupper());
		}

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(16, systemloggEntries.size());
		assertEquals(8, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Anonymiserade användare"))
				.collect(Collectors.toList()).size());
	}

	@Test
	void anonymisera_användare_som_inte_finns() {
		addAnvandare(1);

		assertThrows(AnvandareNotFoundException.class, () -> {
			service.anonymizeAnvandare(anvandare.get(0).getId() - 1);
		});

		List<SystemloggEntry> systemloggEntries = getSystemloggEntries();
		assertEquals(1, systemloggEntries.size());
		assertEquals(0, systemloggEntries.stream()
				.filter(e -> e.getBeskrivning().startsWith("Anonymiserade användare"))
				.collect(Collectors.toList()).size());
	}

	private List<SystemloggEntry> getSystemloggEntries() {
		 return systemloggRepository.findAll().stream()
				.map(SystemloggEntry::fromEntity)
				.filter(e -> e.getHandelse().equals(HandelseTyp.ANVANDARE.getId()))
				.collect(Collectors.toList());
	}
}
