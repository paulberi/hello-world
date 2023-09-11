package se.metria.matdatabas.service.larm;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.service.larm.dto.Larmniva;
import se.metria.matdatabas.service.larm.dto.SaveLarmniva;
import se.metria.matdatabas.service.larm.exception.LarmnivaNamnConflictException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Givet larmnivå tjänst med fyra larmnivåer")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class LarmNivaServiceIT {

	@Autowired
	private LarmnivaRepository repository;

	@Autowired
	private LarmService service;

	@MockBean
	private MatdatabasUser user;

	@BeforeEach
	void tearDown() throws Exception {
		repository.deleteAll();
	}

	@Test
	@WithMockUserAdministrator
	void när_listan_för_larmnivåer_hämtas_skall_den_innehålla_fyra_nivåer() {


		addLarmniva("Varningsnivå", "Gränsvärde för varningsnivå");
		addLarmniva("Åtgärdsnivå 1", "Gränsvärde för Åtgärdsnivå 1");
		addLarmniva("Åtgärdsnivå 2", "Gränsvärde för Åtgärdsnivå 2");
		addLarmniva("Annan åtgärd", "Gränsvärde för annan åtgärd");
		List<Larmniva> larmnivaer = service.getLarmnivaer();
		assertEquals(4, larmnivaer.size());
	}

	@Test
	@WithMockUserAdministrator
	void fel_ska_inträffa_när_en_larmniva_skapas_med_existerande_namn() {

		addLarmniva("Varningsnivå", "Gränsvärde för varningsnivå");
		SaveLarmniva saveLarmniva = SaveLarmniva.builder()
				.namn("Varningsnivå")
				.beskrivning("Testar namn")
				.build();

		assertThrows(LarmnivaNamnConflictException.class, () -> {
			service.createLarmniva(saveLarmniva);
		});

	}

	private void addLarmniva(String namn, String beskrivning) {
		try {
			service.createLarmniva(SaveLarmniva.builder()
					.beskrivning(beskrivning)
					.namn(namn)
					.build());
		} catch (LarmnivaNamnConflictException e) {
			fail("Misslyckades skapa användare för testfallet");
		}
	}
}
