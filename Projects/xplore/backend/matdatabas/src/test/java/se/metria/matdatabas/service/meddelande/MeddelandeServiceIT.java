package se.metria.matdatabas.service.meddelande;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.MatdatabasConfiguration;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.service.meddelande.dto.EditMeddelande;
import se.metria.matdatabas.service.meddelande.dto.Meddelande;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Givet meddelande tjänst med fem meddelanden")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { MatdatabasConfiguration.class }))
@AutoConfigureEmbeddedDatabase
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class MeddelandeServiceIT {

	private static final Integer USER_ID_0 = 0;
	private static final Integer USER_ID_1 = 1;

	@TestConfiguration
	static class MeddelandeServiceTestContextConfiguration {
		
		@Autowired
		private MeddelandeRepository repository;

		@Bean
		public MeddelandeService getMeddelandeService() {
			return new MeddelandeService(repository);
		}
	}

	@Autowired
	private MeddelandeRepository repository;

	@Autowired
	private MeddelandeService service;

	@MockBean
	private MatdatabasUser user;

	private static String RUBRIK = "TEST_RUBRIK";
	private static String MEDDELANDE = "TEST_MEDDELANDE";
	private static String URL = "http://TEST_URL";
	private static LocalDate NEWER = LocalDate.of(2019, 11, 29);
	private static LocalDate OLDER = LocalDate.of(2018, 11, 29);
	private EditMeddelande.EditMeddelandeBuilder editMeddelandeBuilder;

	@BeforeEach
    public void setUp() {
		editMeddelandeBuilder = EditMeddelande.builder().datum(NEWER).rubrik(RUBRIK).meddelande(MEDDELANDE).url(URL);
		IntStream.rangeClosed(0, 2).forEach(i -> service.createMeddelande(editMeddelandeBuilder.datum(NEWER).build()));
		IntStream.rangeClosed(3, 4).forEach(i -> service.createMeddelande(editMeddelandeBuilder.datum(OLDER).build()));
    }
	
	@AfterEach
    public void tearDown() {
    	repository.deleteAll();
    }
	
	@Test
	void när_första_sidan_hämtas_skall_den_inehålla_de_tre_senaste_meddelandena() {
		var page = service.getMeddelanden(0, 3);
		assertEquals(2, page.getTotalPages());
		assertEquals(5, page.getTotalElements());
		assertEquals(0, page.getNumber());

		var meddelanden = page.getContent();
		assertEquals(3, meddelanden.size());

		IntStream.rangeClosed(0, 2).forEach(i -> {
			var meddelande = meddelanden.get(i);
			assertEquals(RUBRIK, meddelande.getRubrik());
			assertEquals(URL, meddelande.getUrl());
			assertEquals(MEDDELANDE, meddelande.getMeddelande());
			assertTrue(meddelande.getDatum().isAfter(OLDER));
		});
	}
	
	@Test
	void när_andra_sidan_hämtas_skall_den_inehålla_de_tre_äldsta_meddelandena() {
		var page = service.getMeddelanden(1, 3);
		assertEquals(2, page.getTotalPages());
		assertEquals(5, page.getTotalElements());
		assertEquals(1, page.getNumber());

		var meddelanden = page.getContent();
		assertEquals(2, meddelanden.size());

		IntStream.rangeClosed(0, 1).forEach(i -> {
			var meddelande = meddelanden.get(i);
			assertEquals(RUBRIK, meddelande.getRubrik());
			assertEquals(URL, meddelande.getUrl());
			assertEquals(MEDDELANDE, meddelande.getMeddelande());
			assertTrue(meddelande.getDatum().isBefore(NEWER));
		});
	}
	
	@Test
	void när_ett_meddelande_hämtas_med_id_skall_det_hämtas() {
		var meddelande = persistTestMeddelande();
		var opt = service.getMeddelande(meddelande.getId());
		assertTrue(opt.isPresent());
		assertEquals(meddelande.getId(), opt.get().getId());
	}
	
	@Test
	void när_ett_meddelande_hämtas_med_felaktigt_id_skall_det_inte_hämtas() {
		var opt = service.getMeddelande(Integer.MAX_VALUE);
		assertFalse(opt.isPresent());		
	}
	
	@Test
	void när_nytt_meddelande_skapas_skall_det_skapas_med_korrekta_värden_på_alla_attribut() {
		when(user.getId()).thenReturn(USER_ID_0);
		var now = LocalDateTime.now();
		sleep();

		var meddelande = editMeddelandeBuilder.datum(LocalDate.now()).build();
		var nytt = service.createMeddelande(meddelande);

		assertTrue(nytt.getId() > 0);
		assertEquals(RUBRIK, nytt.getRubrik());
		assertEquals(URL, nytt.getUrl());
		assertEquals(MEDDELANDE, nytt.getMeddelande());
		assertEquals(LocalDate.now(), nytt.getDatum());
		assertEquals(USER_ID_0, nytt.getAndradAvId());
		assertTrue(now.isBefore(nytt.getSkapadDatum()));
		assertTrue(now.isBefore(nytt.getAndradDatum()));
	}
	
	@Test
	void när_meddelande_uppdateras_skall_det_uppdateras_med_korrekta_värden_på_alla_attribut() {
		when(user.getId()).thenReturn(USER_ID_1);
		var meddelande = persistTestMeddelande();

		var nyRubrik = "Ny rubrik";
		var nyUrl = "Ny url";
		var nyttMeddelande = "Nytt meddelande";

		var now = LocalDateTime.now();
		var edit = editMeddelandeBuilder.rubrik(nyRubrik).url(nyUrl).meddelande(nyttMeddelande).build();
		sleep();

		var opt = service.editMeddelande(meddelande.getId(), edit);
		assertTrue(opt.isPresent());
		var updated = opt.get();
		assertTrue(updated.getId() > 0);
		assertEquals(nyRubrik, updated.getRubrik());
		assertEquals(nyUrl, updated.getUrl());
		assertEquals(nyttMeddelande, updated.getMeddelande());
		assertEquals(USER_ID_1, updated.getAndradAvId());

		assertTrue(now.isAfter(updated.getSkapadDatum()));
		assertTrue(now.isBefore(updated.getAndradDatum()));
	}
	
	@Test
	void när_meddelande_uppdateras_med_felaktigt_id_skall_det_inte_uppdateras() {
		var meddelande = editMeddelandeBuilder.build();
		var opt = service.editMeddelande(Integer.MAX_VALUE, meddelande);
		assertFalse(opt.isPresent());		
	}
	
	@Test
	void när_meddelande_tas_bort_skall_det_tas_bort() {
		var meddelande = persistTestMeddelande();
		var deleted = service.deleteMeddelande(meddelande.getId());
		assertTrue(deleted);
	}
	
	@Test
	void när_meddelande_tas_bort_med_felaktigt_id_skall_det_inte_tas_bort() {
		var deleted = service.deleteMeddelande(Integer.MAX_VALUE);
		assertFalse(deleted);
	}
	
	private Meddelande persistTestMeddelande() {
		var meddelande = editMeddelandeBuilder.build();
		return service.createMeddelande(meddelande);
	}
	

	private void sleep() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}		
	}
}
