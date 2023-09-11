package se.metria.matdatabas.service.handelse;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.anvandare.AnvandareRepository;
import se.metria.matdatabas.service.anvandare.model.AnvandareEntity;
import se.metria.matdatabas.service.bifogadfil.BifogadfilService;
import se.metria.matdatabas.service.bifogadfil.dto.SaveBifogadfil;
import se.metria.matdatabas.service.handelse.dto.SaveHandelse;
import se.metria.matdatabas.service.handelse.entity.HandelseEntity;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Givet händelse tjänst med fem händelser")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class HandelseServiceIT {
		
	private static final String ANV_NAMN_0 = "Åsa Öst";
	private static final String ANV_NAMN_4 = "Östen Ågren";
	private static final String ANV_NAMN_3 = "Maria Nilsson";
	private static final String ANV_NAMN_2 = "Bengt Ek";
	private static final String ANV_NAMN_1 = "Sven Ek";

	private static final String FORETAG_0 = "Företag0";
	private static final String FORETAG_1 = "Företag1";
	private static final String FORETAG_2 = "Företag2";
	private static final String FORETAG_3 = "Företag3";
	private static final String FORETAG_4 = "Företag4";

	private static final Integer MATOBJEKT_0 = 1;
	private static final Integer MATOBJEKT_1 = 2;

	private static final String FILE_NAME = "bild0.png"; 
	private static final String THUMBNAIL_NAME = "thumbnail.jpg";
	private static final String MIME_TYPE = "image/png";

	@Autowired
	private AnvandareRepository anvRepository;
	
	@Autowired
	private HandelseRepository repository;

	@Autowired
	private BifogadfilService bifogadfilService; 
	
	@Autowired
	private HandelseService service;
		
	@MockBean
	private MatdatabasUser user;
	
	private Integer userId0;
	private Integer handelseId0;
	private UUID bildId0;
	private UUID bildId1;
	
	@BeforeEach
	void setUp() throws Exception {
		bildId0 = bifogadfilService.createBifogadfil(createEditBifogadfil()).getId();
		bildId1 = bifogadfilService.createBifogadfil(createEditBifogadfil()).getId();
		
		userId0 = anvRepository.save(createAnvandare(ANV_NAMN_0, FORETAG_0, true, "user0", (short) 2)).getId();
		var userId1 = anvRepository.save(createAnvandare(ANV_NAMN_1, FORETAG_1, true, "user1", (short) 2)).getId();
		var userId2 = anvRepository.save(createAnvandare(ANV_NAMN_2, FORETAG_2, true, "user2", (short) 2)).getId();
		var userId3 = anvRepository.save(createAnvandare(ANV_NAMN_3, FORETAG_3, true, "user3", (short) 2)).getId();
		var userId4 = anvRepository.save(createAnvandare(ANV_NAMN_4, FORETAG_4, true, "user4", (short) 2)).getId();
		
		when(user.getId()).thenReturn(userId0);
		var now = LocalDateTime.now();
		handelseId0 = repository.save(createHandelse(MATOBJEKT_0, "Benämning0", "Beskrivning0", Collections.singleton(bildId0), now)).getId();
		when(user.getId()).thenReturn(userId1);
		repository.save(createHandelse(MATOBJEKT_0, "Benämning1", "Beskrivning1", now.plusSeconds(1)));
		when(user.getId()).thenReturn(userId2);
		repository.save(createHandelse(MATOBJEKT_0, "Benämning2", "Beskrivning2", now.plusSeconds(2)));
		when(user.getId()).thenReturn(userId3);
		repository.save(createHandelse(MATOBJEKT_1, "Benämning3", "Beskrivning3", now.plusSeconds(3)));
		when(user.getId()).thenReturn(userId4);
		repository.save(createHandelse(MATOBJEKT_1, "Benämning4", "Beskrivning4", now.plusSeconds(4)));
	}

	@AfterEach
	void tearDown() {
		anvRepository.deleteAll();
		repository.deleteAll();
	}
	
	@Test
	@WithMockUserTillstandshandlaggare
	void när_första_sidan_hämtas_för_mätobjekt_0_sorterat_på_datum_skall_den_inehålla_tre_händelser_sorterade_på_datum() {
		var page = service.getHandelseForMatobjekt(MATOBJEKT_0, 0, 3, "datum", Sort.Direction.ASC);
		assertEquals(1, page.getTotalPages());
		assertEquals(3, page.getTotalElements());
		assertEquals(0, page.getNumber());
		var handelser = page.getContent();
		assertEquals(3, handelser.size());
		assertEquals("Benämning0", handelser.get(0).getBenamning());
		assertEquals("Beskrivning0", handelser.get(0).getBeskrivning());
		assertEquals(FORETAG_0, handelser.get(0).getForetag());
		assertEquals(1, handelser.get(0).getBifogadebilder().size());
		var bifogadBild = handelser.get(0).getBifogadebilder().iterator().next();
		assertEquals(FILE_NAME, bifogadBild.getFilnamn());
		assertEquals(MIME_TYPE, bifogadBild.getMimeTyp());
		assertEquals(bildId0, bifogadBild.getId());
		assertEquals("Benämning1", handelser.get(1).getBenamning());
		assertEquals("Beskrivning1", handelser.get(1).getBeskrivning());
		assertEquals(FORETAG_1, handelser.get(1).getForetag());
		assertEquals("Benämning2", handelser.get(2).getBenamning());
		assertEquals("Beskrivning2", handelser.get(2).getBeskrivning());
		assertEquals(FORETAG_2, handelser.get(2).getForetag());
	}
	
	@Test
	@WithMockUserTillstandshandlaggare
	void när_händelse_hämtas_skall_händelsen_hämtas() {
		var optHandelse = service.getHandelse(MATOBJEKT_0, handelseId0);
		assertTrue(optHandelse.isPresent());
		var handelse = optHandelse.get();
		assertEquals("Benämning0", handelse.getBenamning());
		assertEquals("Beskrivning0", handelse.getBeskrivning());
		assertEquals(FORETAG_0, handelse.getForetag());
		assertTrue(handelse.getDatum().isBefore(LocalDateTime.now()));
		assertEquals(1, handelse.getBifogadebilder().size());
		var bifogadBild = handelse.getBifogadebilder().iterator().next();
		assertEquals(FILE_NAME, bifogadBild.getFilnamn());
		assertEquals(MIME_TYPE, bifogadBild.getMimeTyp());
		assertEquals(bildId0, bifogadBild.getId());
	}
	
	@Test
	@WithMockUserTillstandshandlaggare
	void när_händelse_hämtas_med_felaktigt_id_skall_händelsen_inte_hämtas() {
		var optHandelse = service.getHandelse(MATOBJEKT_1, handelseId0);
		assertTrue(optHandelse.isEmpty());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_ny_händelse_sparas_skall_händelsen_sparas() {
		when(user.getId()).thenReturn(userId0);
		
		var benamning = "Ny benämning";
		var beskrivning = "Ny beskrivning";
		var datum = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		var save = SaveHandelse.builder()
				.benamning(benamning)
				.beskrivning(beskrivning)
				.datum(datum)
				.bifogadeBilderIds(Collections.singleton(bildId0))
				.build();
		var saved = service.create(MATOBJEKT_0, save);
		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(benamning, saved.getBenamning());
		assertEquals(beskrivning, saved.getBeskrivning());
		assertEquals(datum, saved.getDatum());
		assertEquals(1, saved.getBifogadebilder().size());
		assertEquals(bildId0, saved.getBifogadebilder().iterator().next().getId());
		
		var page = service.getHandelseForMatobjekt(MATOBJEKT_0, 0, 3, "datum", Sort.Direction.ASC);
		assertEquals(4, page.getTotalElements());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_händelse_uppdateras_skall_händelsen_uppdateras() {
		var benamning = "Ny benämning";
		var beskrivning = "Ny beskrivning";
		var datum = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		var update = SaveHandelse.builder()
				.benamning(benamning)
				.beskrivning(beskrivning)
				.datum(datum)
				.bifogadeBilderIds(Collections.singleton(bildId1))
				.build();
		var updated = service.save(MATOBJEKT_0, handelseId0, update);
		assertNotNull(updated);
		assertEquals(handelseId0, updated.getId());
		assertEquals(benamning, updated.getBenamning());
		assertEquals(beskrivning, updated.getBeskrivning());
		assertEquals(update.getDatum(), updated.getDatum());
		assertEquals(1, updated.getBifogadebilder().size());
		assertEquals(bildId1, updated.getBifogadebilder().iterator().next().getId());
		var optBild0 = bifogadfilService.getBifogadfilInfo(bildId0);
		assertTrue(optBild0.isEmpty());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_händelse_uppdateras_med_felaktigt_mätobjektsid_skall_händelsen_inte_uppdateras() {
		var benamning = "Ny benämning";
		var beskrivning = "Ny beskrivning";
		var datum = LocalDateTime.now();
		var update = SaveHandelse.builder()
				.benamning(benamning)
				.beskrivning(beskrivning)
				.datum(datum)
				.bifogadeBilderIds(Collections.singleton(bildId1))
				.build();
		assertThrows(EntityNotFoundException.class, () -> service.save(MATOBJEKT_1, handelseId0, update));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_händelse_uppdateras_med_felaktigt_händelseid_skall_händelsen_inte_uppdateras() {
		var benamning = "Ny benämning";
		var beskrivning = "Ny beskrivning";
		var datum = LocalDateTime.now();
		var update = SaveHandelse.builder()
				.benamning(benamning)
				.beskrivning(beskrivning)
				.datum(datum)
				.bifogadeBilderIds(Collections.singleton(bildId1))
				.build();
		assertThrows(EntityNotFoundException.class, () -> service.save(MATOBJEKT_0, Integer.MAX_VALUE, update));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_händelse_tas_bort_skall_händelsen_tas_bort() {
		service.delete(MATOBJEKT_0, handelseId0);
		
		var page = service.getHandelseForMatobjekt(MATOBJEKT_0, 0, 3, "datum", Sort.Direction.ASC);
		assertEquals(2, page.getTotalElements());
		var optBild0 = bifogadfilService.getBifogadfilInfo(bildId0);
		assertTrue(optBild0.isEmpty());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_händelse_tas_bort__med_felaktigt_mätobjektsid_skall_händelsen_inte_tas_bort() {
		assertThrows(EntityNotFoundException.class, () -> service.delete(MATOBJEKT_1, handelseId0));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_händelse_tas_bort__med_felaktigt_händelseid_skall_händelsen_inte_tas_bort() {
		assertThrows(EntityNotFoundException.class, () -> service.delete(MATOBJEKT_0, Integer.MAX_VALUE));
	}
	
	private HandelseEntity createHandelse(Integer matobjektId, String benamning, String beskrivning, LocalDateTime loggadDatum) {
		return createHandelse(matobjektId, benamning, beskrivning, Collections.emptySet(), loggadDatum);
	}
	
	private HandelseEntity createHandelse(Integer matobjektId, String benamning, String beskrivning, Set<UUID> bildIds, LocalDateTime loggadDatum) {
		var h = new HandelseEntity();
		h.setMatobjektId(matobjektId);
		h.setBenamning(benamning);
		h.setBeskrivning(beskrivning);
		h.getBifogadeBilderIds().addAll(bildIds);
		h.setLoggadDatum(loggadDatum);
		return h;
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

	private SaveBifogadfil createEditBifogadfil() throws IOException {
		return SaveBifogadfil.builder()
				.filnamn(FILE_NAME)
				.mimeTyp(MIME_TYPE)
				.fil(getFileContent(FILE_NAME))
				.thumbnail(getFileContent(THUMBNAIL_NAME))
				.build();
	}
	
	private byte[] getFileContent(String fileName) throws IOException {
		try (var in = this.getClass().getResourceAsStream("/images/" + fileName)) {
			return in.readAllBytes();
		}
	}
	
}
