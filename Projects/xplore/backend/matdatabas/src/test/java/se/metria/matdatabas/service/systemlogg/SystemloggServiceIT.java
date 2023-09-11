package se.metria.matdatabas.service.systemlogg;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.MatdatabasConfiguration;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.service.anvandare.AnvandareRepository;
import se.metria.matdatabas.service.anvandare.behorighet.Behorighet;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.anvandare.model.AnvandareEntity;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.anvandargrupp.dto.Anvandargrupp;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;
import se.metria.matdatabas.service.systemlogg.entity.SystemloggEntity;
import se.metria.matdatabas.service.systemlogg.handelse.Handelse;
import se.metria.matdatabas.service.systemlogg.handelse.HandelseTyp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Givet systemlogg tjänst med fem logg poster")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MatdatabasConfiguration.class }))
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class SystemloggServiceIT {

	@TestConfiguration
	static class SystemloggServiceTestContextConfiguration {

		@Autowired
		private SystemloggRepository repository;

		@Autowired
		private AnvandargruppService anvandargruppService;

		@Autowired
		@Lazy
		private DefinitionMatningstypService definitionMatningstypService;

		@Autowired
		@Lazy
		private MatningstypService matningstypService;

		@Autowired
		@Lazy
		private MatobjektService matobjektService;

		@Bean
		public SystemloggService getSystemloggService() {
			return new SystemloggService(repository, anvandargruppService, matningstypService, matobjektService,
					definitionMatningstypService);
		}
	}

	private static final String TEST_BESKRIVNING = "Test beskrivning";

	private static final String ANV_NAMN_0 = "Åsa Öst";
	private static final String ANV_NAMN_4 = "Östen Ågren";
	private static final String ANV_NAMN_3 = "Maria Nilsson";
	private static final String ANV_NAMN_2 = "Bengt Ek";
	private static final String ANV_NAMN_1 = "Sven Ek";

	@Autowired
	private SystemloggRepository systemloggRepository;

	@Autowired
	private AnvandareRepository anvRepository;

	@Autowired
	private SystemloggService service;

	@MockBean
	private AnvandargruppService anvandargruppService;

	private Integer userId0;
	
	@MockBean
	private MatdatabasUser user;
	
	private LocalDateTime middleTime;
			
	@BeforeEach
	public void setUp() {
		userId0 = anvRepository.save(createAnvandare(ANV_NAMN_0, "Företag0", true, "user0", (short) 2)).getId();
		var userId1 = anvRepository.save(createAnvandare(ANV_NAMN_1, "Företag1", true, "user1", (short) 2)).getId();
		var userId2 = anvRepository.save(createAnvandare(ANV_NAMN_2, "Företag2", true, "user2", (short) 2)).getId();
		var userId3 = anvRepository.save(createAnvandare(ANV_NAMN_3, "Företag3", true, "user3", (short) 2)).getId();
		var userId4 = anvRepository.save(createAnvandare(ANV_NAMN_4, "Företag4", true, "user4", (short) 2)).getId();
		
		when(user.getId()).thenReturn(userId0);
		systemloggRepository.save(createSystemlogg(TEST_BESKRIVNING + "#" + userId0, HandelseTyp.ANVANDARE.getId()));
		sleep();
		when(user.getId()).thenReturn(userId1);
		systemloggRepository.save(createSystemlogg(TEST_BESKRIVNING + "#" + userId1, HandelseTyp.ANVANDARE.getId()));
		sleep();
		middleTime = LocalDateTime.now();
		sleep();
		when(user.getId()).thenReturn(userId2);
		systemloggRepository.save(createSystemlogg(TEST_BESKRIVNING + "#" + userId2, HandelseTyp.MATNINGSTYP.getId()));
		sleep();
		when(user.getId()).thenReturn(userId3);
		systemloggRepository.save(createSystemlogg(TEST_BESKRIVNING + "#" + userId3, HandelseTyp.INLOGGNING.getId()));
		sleep();
		when(user.getId()).thenReturn(userId4);
		systemloggRepository.save(createSystemlogg(TEST_BESKRIVNING + "#" + userId4, HandelseTyp.ANVANDARE.getId()));
	}

	@Test
	void när_första_sidan_hämtas_sorterat_på_datum_skall_den_inehålla_de_tre_äldsta_loggposterna() {
		var page = service.getSystemlogg(0, 3, "datum", Sort.Direction.ASC, null, null, null);
		assertEquals(2, page.getTotalPages());
		assertEquals(5, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(3, loggPoster.size());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_0, loggPoster.get(0).getBeskrivning());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_1, loggPoster.get(1).getBeskrivning());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_2, loggPoster.get(2).getBeskrivning());
	}

	@Test
	void när_andra_sidan_hämtas_sorterat_på_datum_skall_den_inehålla_de_två_nyaste_loggposterna() {
		var page = service.getSystemlogg(1, 3, "datum", Sort.Direction.ASC, null, null, null);
		assertEquals(2, page.getTotalPages());
		assertEquals(5, page.getTotalElements());
		assertEquals(1, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(2, loggPoster.size());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_3, loggPoster.get(0).getBeskrivning());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_4, loggPoster.get(1).getBeskrivning());
	}

	@Test
	void när_första_sidan_hämtas_sorterat_på_beskrivning_skall_den_inehålla_tre_loggposter_sorteratde_på_beskrivning() {
		var page = service.getSystemlogg(0, 3, "beskrivning", Sort.Direction.ASC, null, null, null);
		assertEquals(2, page.getTotalPages());
		assertEquals(5, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(3, loggPoster.size());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_0, loggPoster.get(0).getBeskrivning());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_1, loggPoster.get(1).getBeskrivning());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_2, loggPoster.get(2).getBeskrivning());
	}

	@Test
	void när_första_sidan_hämtas_sorterat_på_händelse_skall_den_inehålla_tre_loggposter_sorteratde_på_händelse() {
		var page = service.getSystemlogg(0, 3, "handelse", Sort.Direction.ASC, null, null, null);
		assertEquals(2, page.getTotalPages());
		assertEquals(5, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(3, loggPoster.size());
		assertEquals((short) 0, loggPoster.get(0).getHandelse());
		assertEquals((short) 1, loggPoster.get(1).getHandelse());
		assertEquals((short) 2, loggPoster.get(2).getHandelse());
	}

	@Test
	void när_första_sidan_hämtas_sorterat_på_namn_skall_den_inehålla_tre_loggposter_sorteratde_på_namn() {
		var page = service.getSystemlogg(0, 3, "anvandarnamn", Sort.Direction.ASC, null, null, null);
		assertEquals(2, page.getTotalPages());
		assertEquals(5, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(3, loggPoster.size());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_2, loggPoster.get(0).getBeskrivning());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_3, loggPoster.get(1).getBeskrivning());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_1, loggPoster.get(2).getBeskrivning());
	}

	@Test
	void när_första_sidan_hämtas_sorterat_på_namn_i_omvänd_ordning_skall_den_inehålla_tre_loggposter_sorteratde_på_namn_i_omvänd_ordning() {
		var page = service.getSystemlogg(0, 3, "anvandarnamn", Sort.Direction.DESC, null, null, null);
		assertEquals(2, page.getTotalPages());
		assertEquals(5, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(3, loggPoster.size());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_4, loggPoster.get(0).getBeskrivning());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_0, loggPoster.get(1).getBeskrivning());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_1, loggPoster.get(2).getBeskrivning());
	}

	@Test
	void när_första_sidan_hämtas_filtrerat_på_händelse_skall_den_inehålla_loggposter_filtrerade_på_händelse() {
		var page = service.getSystemlogg(0, 3, "handelse", Sort.Direction.ASC, null, null, (short) 1);
		assertEquals(1, page.getTotalPages());
		assertEquals(1, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(1, loggPoster.size());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_3, loggPoster.get(0).getBeskrivning());
	}

	@Test
	void när_första_sidan_hämtas_filtrerat_på_från_datum_skall_den_inehålla_loggposter_filtrerade_på_datum() {		
		var page = service.getSystemlogg(0, 3, "datum", Sort.Direction.ASC, middleTime, null, null);
		assertEquals(1, page.getTotalPages());
		assertEquals(3, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(3, loggPoster.size());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_2, loggPoster.get(0).getBeskrivning());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_3, loggPoster.get(1).getBeskrivning());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_4, loggPoster.get(2).getBeskrivning());
	}

	@Test
	void när_första_sidan_hämtas_filtrerat_på_till_datum_skall_den_inehålla_loggposter_filtrerade_på_datum() {		
		var page = service.getSystemlogg(0, 3, "datum", Sort.Direction.ASC, null, middleTime, null);
		assertEquals(1, page.getTotalPages());
		assertEquals(2, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(2, loggPoster.size());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_0, loggPoster.get(0).getBeskrivning());
		assertEquals(TEST_BESKRIVNING + ANV_NAMN_1, loggPoster.get(1).getBeskrivning());
	}

	@Test
	void när_första_sidan_hämtas_filtrerat_på_datum_och_händelse_skall_den_inehålla_loggposter_filtrerade_på_datum_och_händelse() {		
		var page = service.getSystemlogg(0, 3, "datum", Sort.Direction.ASC, LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(1), (short) 0);
		assertEquals(1, page.getTotalPages());
		assertEquals(1, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var loggPoster = page.getContent();
		assertEquals(1, loggPoster.size());
		assertNotEquals(TEST_BESKRIVNING + ANV_NAMN_2, loggPoster.get(0).getBeskrivning());
	}

	@Test
	void när_första_sidan_hämtas_sorterat_på_felaktig_property_skall_fel_kastas() {
		assertThrows(PropertyReferenceException.class, () -> service.getSystemlogg(0, 3, "gurka", Sort.Direction.DESC, null, null, null));
	}

	@Test
	void när_nytt_systemloggsmeddelande_läggs_till_skall_det_sparas() {
		when(user.getId()).thenReturn(userId0);
		var beskrivning = "En ny test beskrivning";
		var ny = service.addSystemloggEntry(beskrivning, HandelseTyp.MATNINGSTYP.getId());
		assertEquals(beskrivning, ny.getBeskrivning());
		assertEquals(userId0, ny.getAnvandarid());
		assertEquals(HandelseTyp.MATNINGSTYP.getId(), ny.getHandelse());
		assertTrue(LocalDateTime.now().isAfter(ny.getDatum()));
	}


	@Test
	void skapa_händelse_för_användare_inloggad() {
		systemloggRepository.deleteAll();

		when(user.getId()).thenReturn(userId0);
		Handelse handelse = Handelse.USER_LOGGED_IN;

		service.addHandelseUserLoggedIn(userId0);
		List<SystemloggEntry> systemloggEntries = systemloggRepository.findAll().stream()
				.map(SystemloggEntry::fromEntity)
				.filter(e -> e.getHandelse().equals(handelse.getTyp().getId()))
				.collect(Collectors.toList());

		SystemloggEntry systemloggEntry = systemloggEntries.get(0);
		assertEquals(handelse.getTyp().getId(), systemloggEntry.getHandelse());
		assertEquals(userId0, systemloggEntry.getAnvandarid());
		assertEquals(handelse.getBeskrivning(), systemloggEntry.getBeskrivning());
		assertTrue(LocalDateTime.now().isAfter(systemloggEntry.getDatum()));
	}

	@Test
	void skapa_händelse_för_användare_skapad() {
		when(user.getId()).thenReturn(userId0);
		Handelse handelse = Handelse.USER_CREATED;
		Anvandare anvandare = new Anvandare();
		anvandare.setId(userId0);
		anvandare.setBehorighet(Behorighet.TILLSTANDSHANDLAGGARE.getId());

		SystemloggEntry systemloggEntry = service.addHandelseUserCreated(anvandare);

		String expectedBeskrivning = String.format(
				handelse.getBeskrivning(),
				anvandare.getId(),
				Behorighet.getNameById(anvandare.getBehorighet()));

		assertEquals(handelse.getTyp().getId(), systemloggEntry.getHandelse());
		assertEquals(userId0, systemloggEntry.getAnvandarid());
		assertEquals(expectedBeskrivning, systemloggEntry.getBeskrivning());
		assertTrue(LocalDateTime.now().isAfter(systemloggEntry.getDatum()));
	}

	@Test
	void skapa_händelse_för_partiellt_modifierad_användare() {
		when(user.getId()).thenReturn(userId0);
		Handelse handelse = Handelse.USER_MODIFIED;

		Anvandare anvandareBefore = new Anvandare();
		anvandareBefore.setId(userId0);
		anvandareBefore.setNamn("namn");
		anvandareBefore.setForetag("foretag");
		anvandareBefore.setBehorighet(Behorighet.MATRAPPORTOR.getId());
		anvandareBefore.setEpost("epost");

		Anvandare anvandareAfter = new Anvandare();
		anvandareAfter.setId(anvandareBefore.getId());
		anvandareAfter.setNamn(anvandareBefore.getNamn());
		anvandareAfter.setForetag("nytt foretag");
		anvandareAfter.setBehorighet(anvandareBefore.getBehorighet());
		anvandareAfter.setEpost("ny epost");

		SystemloggEntry systemloggEntry = service.addHandelseUserModified(anvandareBefore, anvandareAfter);

		String expectedBeskrivning = String.format(handelse.getBeskrivning(),
				anvandareBefore.getId(),
				Behorighet.getNameById(anvandareBefore.getBehorighet()),
				"Företag, E-postadress");

		assertEquals(handelse.getTyp().getId(), systemloggEntry.getHandelse());
		assertEquals(userId0, systemloggEntry.getAnvandarid());
		assertEquals(expectedBeskrivning, systemloggEntry.getBeskrivning());
		assertTrue(LocalDateTime.now().isAfter(systemloggEntry.getDatum()));
	}

	@Test
	void skapa_händelse_för_fullt_modifierad_användare() {
		when(user.getId()).thenReturn(userId0);
		Handelse handelse = Handelse.USER_MODIFIED;

		Anvandare anvandareBefore = new Anvandare();
		anvandareBefore.setId(userId0);
		anvandareBefore.setNamn("namn");
		anvandareBefore.setForetag("foretag");
		anvandareBefore.setAktiv(true);
		anvandareBefore.setInloggningsnamn("inloggningsnamn");
		anvandareBefore.setBehorighet(Behorighet.MATRAPPORTOR.getId());
		anvandareBefore.setDefaultKartlagerId(0);
		anvandareBefore.setSkickaEpost(true);
		anvandareBefore.setEpost("epost");
		anvandareBefore.setAnvandargrupper(List.of(1,2));

		Anvandare anvandareAfter = new Anvandare();
		anvandareAfter.setId(userId0);
		anvandareAfter.setNamn("namn2");
		anvandareAfter.setForetag("foretag2");
		anvandareAfter.setAktiv(false);
		anvandareAfter.setInloggningsnamn("inloggningsnamn2");
		anvandareAfter.setBehorighet(Behorighet.TILLSTANDSHANDLAGGARE.getId());
		anvandareAfter.setDefaultKartlagerId(1);
		anvandareAfter.setSkickaEpost(false);
		anvandareAfter.setEpost("epost2");
		anvandareAfter.setAnvandargrupper(List.of(1,2,3));

		when(anvandargruppService.getAnvandargrupp(1)).thenReturn(
				Optional.of(new Anvandargrupp(1,"Ett","ett beskr", 1, null, null, null)));
		when(anvandargruppService.getAnvandargrupp(2)).thenReturn(
				Optional.of(new Anvandargrupp(2,"Två","två beskr", 1, null, null, null)));
		when(anvandargruppService.getAnvandargrupp(3)).thenReturn(
				Optional.of(new Anvandargrupp(3,"Tre","Tre beskr", 1, null, null, null)));

		SystemloggEntry systemloggEntry = service.addHandelseUserModified(anvandareBefore, anvandareAfter);

		String expectedBeskrivningStart = String.format("Ändrade inställningar för användare #%d med behörighet: %s. Inställningar:",
				userId0,
				Behorighet.getNameById(anvandareBefore.getBehorighet()));

		assertEquals(handelse.getTyp().getId(), systemloggEntry.getHandelse());
		assertEquals(userId0, systemloggEntry.getAnvandarid());
		assertTrue(systemloggEntry.getBeskrivning().startsWith(expectedBeskrivningStart));
		assertTrue(systemloggEntry.getBeskrivning().contains("Namn"));
		assertTrue(systemloggEntry.getBeskrivning().contains("Företag"));
		assertTrue(systemloggEntry.getBeskrivning().contains("Aktiv"));
		assertTrue(systemloggEntry.getBeskrivning().contains("Användarnamn"));
		assertTrue(systemloggEntry.getBeskrivning().contains("Behörighet"));
		assertTrue(systemloggEntry.getBeskrivning().contains("Kartlager"));
		assertTrue(systemloggEntry.getBeskrivning().contains("Skicka e-post"));
		assertTrue(systemloggEntry.getBeskrivning().contains("E-postadress"));
		assertTrue(systemloggEntry.getBeskrivning().contains("Tillagda användargrupper"));
		assertTrue(LocalDateTime.now().isAfter(systemloggEntry.getDatum()));
	}

	@Test
	void skapa_händelse_för_användare_borttagen() {
		when(user.getId()).thenReturn(userId0);
		Handelse handelse = Handelse.USER_REMOVED;

		Anvandare anvandare = new Anvandare();
		anvandare.setId(userId0);
		anvandare.setBehorighet(Behorighet.TILLSTANDSHANDLAGGARE.getId());

		SystemloggEntry systemloggEntry = service.addHandelseUserRemoved(anvandare);

		String expectedBeskrivning = String.format(
				handelse.getBeskrivning(),
				anvandare.getId(),
				Behorighet.getNameById(anvandare.getBehorighet()));

		assertEquals(handelse.getTyp().getId(), systemloggEntry.getHandelse());
		assertEquals(userId0, systemloggEntry.getAnvandarid());
		assertEquals(expectedBeskrivning, systemloggEntry.getBeskrivning());
		assertTrue(LocalDateTime.now().isAfter(systemloggEntry.getDatum()));
	}

	@Test
	void skapa_händelse_för_användare_anonymiserad() {
		when(user.getId()).thenReturn(userId0);
		Handelse handelse = Handelse.USER_ANONYMIZED;

		Anvandare anvandare = new Anvandare();
		anvandare.setId(userId0);
		anvandare.setBehorighet(Behorighet.TILLSTANDSHANDLAGGARE.getId());

		SystemloggEntry systemloggEntry = service.addHandelseUserAnonymized(anvandare);

		String expectedBeskrivning = String.format(
				handelse.getBeskrivning(),
				anvandare.getId(),
				Behorighet.getNameById(anvandare.getBehorighet()));

		assertEquals(handelse.getTyp().getId(), systemloggEntry.getHandelse());
		assertEquals(userId0, systemloggEntry.getAnvandarid());
		assertEquals(expectedBeskrivning, systemloggEntry.getBeskrivning());
		assertTrue(LocalDateTime.now().isAfter(systemloggEntry.getDatum()));
	}

	private SystemloggEntity createSystemlogg(String beskrivning, Short handelse) {
		var s = new SystemloggEntity();
		s.setBeskrivning(beskrivning);
		s.setHandelse(handelse);
		return s;
	}

	private AnvandareEntity createAnvandare(String namn, String foretag, boolean aktiv, String inloggningsNamn, short behorighet) {
		AnvandareEntity anv = new AnvandareEntity();
		anv.setNamn(namn);
		anv.setForetag(foretag);
		anv.setAktiv(aktiv);
		anv.setInloggningsnamn(inloggningsNamn);
		anv.setBehorighet(behorighet);
		anv.setSkickaEpost(false);
		return anv;
	}

	private void sleep() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}		
	}

}
