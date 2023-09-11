package se.metria.matdatabas.service.gransvarde;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.openapi.model.GransvardeDto;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.gransvarde.command.SaveGransvarde;
import se.metria.matdatabas.service.gransvarde.exception.GransvardeNotFoundException;
import se.metria.matdatabas.service.gransvarde.exception.InactivatingGransvardeWithActiveLarmsException;
import se.metria.matdatabas.service.handelse.HandelseService;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.larm.LarmRepository;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.Matningsfelkod;
import se.metria.matdatabas.service.matning.TypAvKontroll;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypRepository;
import se.metria.matdatabas.service.matningstyp.MatningstypService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Givet gränsvärde tjänst med fem gränsvärden")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Sql({"/data/test_create_matningstyper.sql"})
class GransvardeServiceIT {

	private static final Integer MATNINGSTYP_1 = 1;
	private static final Integer MATNINGSTYP_FLODE = 5;

	@Autowired
	private GransvardeService service;

	@MockBean
	private MatdatabasUser user;

	@Autowired
	private MatningstypService matningstypService;

	@Autowired
	private MatningService matningService;

	@Autowired
	private LarmService larmService;

	@BeforeEach
	void setUp() {
		service.createGransvarde(SaveGransvarde.builder()
				.matningstypId(MATNINGSTYP_1)
				.gransvarde(-0.5)
				.larmnivaId(2)
				.larmTillAnvandargruppId(1)
				.typAvKontroll((short) 1)
				.aktiv(true)
				.build());
		service.createGransvarde(SaveGransvarde.builder()
				.matningstypId(MATNINGSTYP_1)
				.gransvarde(1.14)
				.larmnivaId(2)
				.larmTillAnvandargruppId(1)
				.typAvKontroll((short) 1)
				.aktiv(true)
				.build());
		service.createGransvarde(SaveGransvarde.builder()
				.matningstypId(MATNINGSTYP_1)
				.gransvarde(10.22)
				.larmnivaId(2)
				.larmTillAnvandargruppId(1)
				.typAvKontroll((short) 1)
				.aktiv(true)
				.build());
		service.createGransvarde(SaveGransvarde.builder()
				.matningstypId(MATNINGSTYP_1)
				.gransvarde(14.10)
				.larmnivaId(2)
				.larmTillAnvandargruppId(1)
				.typAvKontroll((short) 1)
				.aktiv(true)
				.build());
		service.createGransvarde(SaveGransvarde.builder()
				.matningstypId(MATNINGSTYP_1)
				.gransvarde(-10.10)
				.larmnivaId(2)
				.larmTillAnvandargruppId(1)
				.typAvKontroll((short) 1)
				.aktiv(true)
				.build());

	}

	@Test
	@WithMockUserTillstandshandlaggare
	@Transactional
	void när_man_sparar_ett_nytt_värde_är_returvärdet_korrekt() {
		SaveGransvarde save = SaveGransvarde.builder()
				.matningstypId(MATNINGSTYP_1)
				.gransvarde(-10.10)
				.larmnivaId(2)
				.larmTillAnvandargruppId(1)
				.typAvKontroll((short) 1)
				.aktiv(true)
				.build();

		GransvardeDto gransvarde = service.createGransvarde(save);
		assertNotNull(gransvarde);
		assertNotNull(gransvarde.getId());
		assertEquals(save.getMatningstypId(), gransvarde.getMatningstypId());
		assertEquals(save.getGransvarde(), gransvarde.getGransvarde());
		assertEquals(save.getLarmnivaId(), gransvarde.getLarmnivaId());
		assertEquals(save.getLarmTillAnvandargruppId(), gransvarde.getLarmTillAnvandargruppId());
		assertEquals(Integer.valueOf(save.getTypAvKontroll()), gransvarde.getTypAvKontroll());
		assertEquals(save.getAktiv(), gransvarde.getAktiv());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	@Transactional
	void när_listan_för_gränsvärden_hämtas_skall_den_innehålla_fem_händelser() {
		List<GransvardeDto> gransvarden = service.getGransvardenListForMatningstyp(MATNINGSTYP_1);
		for (GransvardeDto gransvarde : gransvarden) {
			assertNotNull(gransvarde.getId());
			assertNotNull(gransvarde.getMatningstypId());
			assertNotNull(gransvarde.getTypAvKontroll());
			assertNotNull(gransvarde.getGransvarde());
			assertNotNull(gransvarde.getLarmnivaId());
			assertNotNull(gransvarde.getLarmTillAnvandargruppId());
			assertNotNull(gransvarde.getMatobjektId());
			assertNotNull(gransvarde.getMatobjektNamn());
			assertNotNull(gransvarde.getLarmnivaNamn());
			assertNotNull(gransvarde.getAktiv());
		}
		assertEquals(5, gransvarden.size());
	}

	@ParameterizedTest
	@WithMockUserTillstandshandlaggare
	@Transactional
	@CsvSource({
			"true, 23.456d",
			"false, 25.456d"})
	void när_ett_gränsvärde_har_ett_ej_kvitterat_larm_kan_det_ej_avaktiveras(boolean shouldHaveLarm, Double varde)
			throws MatningIllegalMatvarde, AlreadyGodkandException {
		var matningstyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var gransvarde = SaveGransvarde.builder()
				.matningstypId(0)
				.gransvarde(24.5d)
				.larmnivaId(1)
				.larmTillAnvandargruppId(1)
				.typAvKontroll(TypAvKontroll.MIN)
				.aktiv(true)
				.build();
		gransvarde.setMatningstypId(matningstyp.getId());
		var gransvardeEntity = service.createGransvarde(gransvarde);

		var toSave = SaveMatning.builder()
				.avlastDatum(LocalDateTime.now())
				.avlastVarde(varde)
				.felkod(Matningsfelkod.OK)
				.kommentar("En kommentar")
				.rapportor("Mätbolaget")
				.build();
		matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn());
		var count = larmService.countByStatusAndGransvardeId((short) 0, gransvardeEntity.getId());

		assertEquals(shouldHaveLarm ? 1: 0, count);

		gransvarde.setAktiv(false);

		if (shouldHaveLarm) {
			assertThrows(InactivatingGransvardeWithActiveLarmsException.class,
					() -> service.updateGransvarde(gransvardeEntity.getId(), gransvarde));
		} else {
			assertDoesNotThrow(() -> service.updateGransvarde(gransvardeEntity.getId(), gransvarde));
		}
	}
}
