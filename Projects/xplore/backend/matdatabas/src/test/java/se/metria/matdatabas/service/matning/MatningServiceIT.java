package se.metria.matdatabas.service.matning;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserObservator;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypConstants;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.gransvarde.GransvardeRepository;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.gransvarde.command.SaveGransvarde;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.larm.LarmRepository;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matning.dto.ReviewMatning;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportFormat;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningslogg.MatningsloggService;
import se.metria.matdatabas.service.matningslogg.dto.Matningslogg;
import se.metria.matdatabas.service.matningstyp.MatningstypRepository;
import se.metria.matdatabas.service.matningstyp.MatningstypService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@DisplayName("Givet mätnings service tjänst")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Sql({"/data/test_create_matningstyper.sql"})
class MatningServiceIT {

	private static final String CSVFILE_STANDARD = "standard.csv";
	private static final String CSVFILE_INSTRUMENT = "instrument.csv";
	private static final String CSVFILE_BAD_ARGUMENT = "bad_argument.csv";
	private static final String CSVFILE_MISSING_ARGUMENT = "missing_argument.csv";
	private static final String CSVFILE_EXTRA_ARGUMENT = "extra_argument.csv";

	@Autowired
	MatningstypService matningstypService;

	@Autowired
	private MatningService matningService;

	@MockBean
	private MatdatabasUser user;

	@MockBean
	private MatningsloggService matningsloggService;

	@Autowired
	private LarmService larmService;

	@Autowired
	private GransvardeService gransvardeService;

	@Autowired
	private MatningstypRepository matningstypRepository;

	@Autowired
	private MatningRepository matningRepository;

	@Autowired
	private LarmRepository larmRepository;

	@Autowired
	private DefinitionMatningstypService definitionMatningstypService;

	@Autowired
	private GransvardeRepository gransvardeRepository;

	private static final Integer MATNINGSTYP_NEDMATNING = 1;
	private static final Integer MATNINGSTYP_PORTTRYCK = 2;
	private static final Integer MATNINGSTYP_NEDMATNING_MAX = 6;
	private static final Integer MATNINGSTYP_FLODE = 5;
	private static final Integer MATNINGSTYP_VATTENTRYCK = 7;
	private static final Integer MATNINGSTYP_LUFTTRYCK = 8;
	private static final Integer MATNINGSTYP_MEDELFLÖDE = 9;
	private static final Integer MATNINGSTYP_TUNNEL_MEDELFLÖDE = 10;
	
	private static final LocalDateTime testNow = LocalDateTime.now();


	SaveMatning.SaveMatningBuilder saveMatning = SaveMatning.builder()
			.avlastDatum(testNow)
			.avlastVarde(23.456d)
			.felkod(Matningsfelkod.OK)
			.kommentar("En kommentar")
			.rapportor("Mätbolaget");

	SaveMatning.SaveMatningBuilder updateMatning = SaveMatning.builder()
			.avlastDatum(testNow)
			.avlastVarde(13.37d)
			.felkod(Matningsfelkod.OK)
			.kommentar("En uppdaterad kommentar")
			.rapportor("Mätamera AB");

	SaveGransvarde.SaveGransvardeBuilder gransvardeMin = SaveGransvarde.builder()
			.matningstypId(0)
			.gransvarde(24.5d)
			.larmnivaId(1)
			.larmTillAnvandargruppId(1)
			.typAvKontroll(TypAvKontroll.MIN)
			.aktiv(true);

	SaveGransvarde.SaveGransvardeBuilder gransvardeMax = SaveGransvarde.builder()
			.matningstypId(1)
			.gransvarde(5.)
			.larmnivaId(1)
			.larmTillAnvandargruppId(1)
			.typAvKontroll(TypAvKontroll.MAX)
			.aktiv(true);

	SaveGransvarde.SaveGransvardeBuilder gransvardeMax1 = SaveGransvarde.builder()
			.matningstypId(1)
			.gransvarde(5.)
			.larmnivaId(1)
			.larmTillAnvandargruppId(1)
			.typAvKontroll(TypAvKontroll.MAX)
			.aktiv(true);

	SaveGransvarde.SaveGransvardeBuilder gransvardeMax2 = SaveGransvarde.builder()
			.matningstypId(1)
			.gransvarde(10.)
			.larmnivaId(2)
			.larmTillAnvandargruppId(1)
			.typAvKontroll(TypAvKontroll.MAX)
			.aktiv(true);

	@AfterEach
	void tearDown() {

		larmRepository.deleteAll();
		gransvardeRepository.deleteAll();
		matningRepository.deleteAll();
		matningstypRepository.deleteAll();
	}

	void assertMatningEqualsSaveMatning(Matning matning, SaveMatning saveMatning) {
		assertEquals(matning.getAvlastDatum().truncatedTo(ChronoUnit.SECONDS), saveMatning.getAvlastDatum().truncatedTo(ChronoUnit.SECONDS));
		assertEquals(matning.getAvlastVarde(), saveMatning.getAvlastVarde());
		assertEquals(matning.getFelkod(), saveMatning.getFelkod());
		assertEquals(matning.getKommentar(), saveMatning.getKommentar());
		assertEquals(matning.getRapportor(), saveMatning.getRapportor());
	}

	@Test
	@WithMockUserMatrapportor
	void när_mätning_uppdateras_skall_den_sparas() throws MatningIllegalMatvarde, AlreadyGodkandException, AlreadyGodkandException {
		// Given
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseAndrat(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var matning = matningService.create(matningstyp, saveMatning.build(), false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		var updated = updateMatning.build();

		// When
		var updatedFound = matningService.update((long)matning.getId(), matningstyp, updated, StandardKallsystem.MiljöKoll.getNamn()).getMatning();

		// Then
		assertNotNull(updatedFound.getId());
		assertEquals(Detektionsomrade.INOM, updatedFound.getInomDetektionsomrade());
		assertEquals(Matningstatus.EJGRANSKAT, updatedFound.getStatus());
		assertMatningEqualsSaveMatning(updatedFound, updated);
		verify(matningsloggService).addHandelseRapporterad(matningstyp, updatedFound.getId(), matning.getAvlastVarde(), "En kommentar");
		verify(matningsloggService).addHandelseAndrat(eq(updatedFound.getId()),
				eq(matning.getAvlastVarde()), eq(updatedFound.getAvlastVarde()),
				eq("En kommentar"), eq("En uppdaterad kommentar"),
				eq(testNow.truncatedTo(ChronoUnit.SECONDS)), eq(testNow.truncatedTo(ChronoUnit.SECONDS)), // Tiden blir avrundad, så lurigt att göra gämförelse här
				eq(Matningsfelkod.OK), eq(Matningsfelkod.OK));
	}

	@Test
	@WithMockUserMatrapportor
	void när_ny_mätning_sparas_skall_den_sparas() throws MatningIllegalMatvarde, AlreadyGodkandException, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var toSave = saveMatning.build();
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		var count = matningService.getEjGranskadeMatningarForMatningstyp(matningstyp.getId());
		assertEquals(1, count);
		assertNotNull(saved.getId());

		assertMatningEqualsSaveMatning(saved, toSave);
		assertEquals(Detektionsomrade.INOM, saved.getInomDetektionsomrade());
		assertEquals(Matningstatus.EJGRANSKAT, saved.getStatus());
		verify(matningsloggService).addHandelseRapporterad(matningstyp, saved.getId(), toSave.getAvlastVarde(), "En kommentar");
	}

	@Test
	@WithMockUserMatrapportor
	void när_ny_mätning_som_importerats_sparas_skall_den_sparas() throws MatningIllegalMatvarde, AlreadyGodkandException, AlreadyGodkandException {
		when(matningsloggService.addHandelseImporterad(any(), any(), any(), any(), any(), any() )).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var definitionMatningstyp = definitionMatningstypService.findById(matningstyp.getId()).orElseThrow();
		var beraknatVardeEnhet = definitionMatningstyp.getBeraknadEnhet();
		var toSave = saveMatning.build();
		var saved = matningService.create(matningstyp, toSave, true, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		var count = matningService.getEjGranskadeMatningarForMatningstyp(matningstyp.getId());
		assertEquals(1, count);
		assertNotNull(saved.getId());

		assertMatningEqualsSaveMatning(saved, toSave);
		assertEquals(Detektionsomrade.INOM, saved.getInomDetektionsomrade());
		assertEquals(Matningstatus.EJGRANSKAT, saved.getStatus());
		verify(matningsloggService).addHandelseImporterad(matningstyp, saved.getId(), toSave.getAvlastVarde(),
				toSave.getBeraknatVarde(), beraknatVardeEnhet, "En kommentar");
	}


	@Test
	@WithMockUserMatrapportor
	void när_ny_mätning_sparas_med_felkod_skall_den_sparas_med_status_ej_granskad() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_NEDMATNING).orElseThrow();
		var toSave = saveMatning
				.felkod(Matningsfelkod.HINDER)
				.build();
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertMatningEqualsSaveMatning(saved, toSave);
		assertEquals(Detektionsomrade.INOM, saved.getInomDetektionsomrade());
		assertEquals(Matningstatus.EJGRANSKAT, saved.getStatus());
		verify(matningsloggService).addHandelseRapporteradFelkod(saved.getId(), saved.getFelkod(), "En kommentar");
	}

	@Test
	@WithMockUserMatrapportor
	void när_ny_mätning_sparas_med_samma_datum_skall_den_befintliga_uppdateras() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var toSave = saveMatning.build();
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		verify(matningsloggService).addHandelseRapporterad(matningstyp, saved.getId(), toSave.getAvlastVarde(), "En kommentar");
		verifyNoMoreInteractions(matningsloggService);
		var toSave2 = saveMatning
				.avlastDatum(toSave.getAvlastDatum())
				.kommentar("En ny kommentar")
				.build();
		var saved2 = matningService.create(matningstyp, toSave2, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		var count = matningService.getEjGranskadeMatningarForMatningstyp(matningstyp.getId());

		assertEquals(1, count);
		assertEquals(saved.getId(), saved2.getId());
		assertEquals(toSave2.getKommentar(), saved2.getKommentar());
	}

	@Test
	@WithMockUserAdministrator
	void när_ny_mätning_sparas_med_ogiltiga_mätvärden_skall_fel_kastas() {
		var matningstyp = matningstypService.findById(MATNINGSTYP_NEDMATNING).orElseThrow();

		saveMatning.avlastVarde(201d);
		var toSave = saveMatning.build();
		assertThrows(MatningIllegalMatvarde.class, () -> matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()));
	}

	@ParameterizedTest
	@WithMockUserTillstandshandlaggare
	@CsvSource({
			"true, 1",
			"false, 0"})
	void när_ny_mätning_sparas_skall_larm_skapas_om_gransvardet_ar_aktivt(boolean aktivt, Integer expected)
			throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var gransvarde = gransvardeMin.build();
		gransvarde.setMatningstypId(matningstyp.getId());
		gransvarde.setAktiv(aktivt);
		var gransvardeEntity = gransvardeService.createGransvarde(gransvarde);
		var toSave = saveMatning.build();
		var result = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn());
		var count = larmService.countByStatusAndGransvardeId((short) 0, gransvardeEntity.getId());
		assertEquals(expected, count);

		if (expected > 0) {
			var larmList = larmService.getLarmsForMatning(result.getMatning().getId());
			assertEquals(expected, larmList.size());

			larmList.forEach(larm -> {
				assertEquals(result.getMatning().getBeraknatVarde() == null ?
						result.getMatning().getAvlastVarde() :
						result.getMatning().getBeraknatVarde(),
						larm.getVarde());
				assertEquals(gransvarde.getGransvarde(), larm.getGransvarde());
				assertEquals(gransvarde.getTypAvKontroll(), Short.valueOf(larm.getTypAvKontroll().toString()));
				assertEquals(gransvarde.getLarmTillAnvandargruppId(), larm.getAnvandargruppId());
				assertEquals(gransvarde.getLarmnivaId(), larm.getLarmnivaId());
			});
		}
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_ny_mätning_sparas_skall_larm_inte_skapas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var gransvarde = gransvardeMin.build();
		gransvarde.setTypAvKontroll((short) 0);
		gransvarde.setMatningstypId(matningstyp.getId());
		var gransvardeEntity = gransvardeService.createGransvarde(gransvarde);

		var toSave = saveMatning.build();
		matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn());
		var count = larmService.countByStatusAndGransvardeId((short) 0, gransvardeEntity.getId());
		assertEquals(0, count);
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_larm_existerar_som_inte_är_kvitterat_ska_inte_nytt_larm_skapas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var gransvarde = gransvardeMin.build();
		gransvarde.setMatningstypId(matningstyp.getId());
		var gransvardeEntity = gransvardeService.createGransvarde(gransvarde);

		var toSave1 = saveMatning.build();
		matningService.create(matningstyp, toSave1, false, StandardKallsystem.MiljöKoll.getNamn());
		var count = larmService.countByStatusAndGransvardeId((short) 0, gransvardeEntity.getId());
		assertEquals(1, count);

		var toSave2 = saveMatning.build();
		matningService.create(matningstyp, toSave2, false, StandardKallsystem.MiljöKoll.getNamn());
		count = larmService.countByStatusAndGransvardeId((short) 0, gransvardeEntity.getId());
		assertEquals(1, count);
	}

	@Test
	@WithMockUserMatrapportor
	void när_nivå_nedmätning_sparas_skall_värde_beräknas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromParams(anyBoolean(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_NEDMATNING_MAX).orElseThrow();
		var toSave = saveMatning
				.avlastVarde(10d)
				.build();

		matningstyp.setBerakningReferensniva(100d);
		matningstyp.setBerakningKonstant(2d);
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertMatningEqualsSaveMatning(saved, toSave);
		assertEquals(Detektionsomrade.INOM, saved.getInomDetektionsomrade());
		assertEquals(Matningstatus.EJGRANSKAT, saved.getStatus());
		assertEquals(80d, saved.getBeraknatVarde());

		verify(matningsloggService).addHandelseRapporterad(matningstyp, saved.getId(), toSave.getAvlastVarde(), "En kommentar");
		verify(matningsloggService).addHandelseBeraknadFromParams(true, matningstyp, saved.getId(), "Referensnivå");
	}

	@Test
	@WithMockUserMatrapportor
	void när_nivå_portryck_sparas_skall_värde_beräknas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromParams(anyBoolean(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_PORTTRYCK).orElseThrow();
		var toSave = saveMatning
				.avlastVarde(19.64)
				.build();
		matningstyp.setBerakningReferensniva(100d);
		matningstyp.setBerakningKonstant(2d);
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertMatningEqualsSaveMatning(saved, toSave);
		assertEquals(Detektionsomrade.INOM, saved.getInomDetektionsomrade());
		assertEquals(Matningstatus.EJGRANSKAT, saved.getStatus());
		assertEquals(102.4, saved.getBeraknatVarde());

		verify(matningsloggService).addHandelseRapporterad(matningstyp, saved.getId(), toSave.getAvlastVarde(), "En kommentar");
		verify(matningsloggService).addHandelseBeraknadFromParams(true, matningstyp, saved.getId(), "Spetsnivå");
	}

	@Test
	@WithMockUserMatrapportor
	void när_infiltration_medelflöde_sparas_skall_värde_beräknas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromPrev(anyBoolean(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_MEDELFLÖDE).orElseThrow();
		LocalDateTime datePrev = testNow.truncatedTo(ChronoUnit.SECONDS).minusMinutes(2);
		var toSavePrev = saveMatning
				.avlastVarde(20d)
				.avlastDatum(datePrev)
				.build();
		var savedPrev = matningService.create(matningstyp, toSavePrev, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(savedPrev.getId());
		assertEquals(datePrev, savedPrev.getAvlastDatum());
		assertEquals(20d, savedPrev.getAvlastVarde());

		LocalDateTime date = datePrev.plusMinutes(2);
		var toSave = saveMatning
				.avlastVarde(10d)
				.avlastDatum(date)
				.build();
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date, saved.getAvlastDatum());
		assertEquals(10d, saved.getAvlastVarde());
		assertEquals(-5000d, saved.getBeraknatVarde());

		verify(matningsloggService).addHandelseRapporterad(matningstyp, savedPrev.getId(), toSavePrev.getAvlastVarde(), "En kommentar");
		verify(matningsloggService).addHandelseRapporterad(matningstyp, saved.getId(), saved.getAvlastVarde(), "En kommentar");
		verify(matningsloggService).addHandelseBeraknadFromPrev(false, matningstyp, savedPrev.getId(), Optional.empty());
		verify(matningsloggService).addHandelseBeraknadFromPrev(true, matningstyp, saved.getId(), Optional.of(savedPrev));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_tunnelvatten_medelflöde_sparas_skall_värde_beräknas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromPrev(anyBoolean(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();

		/* Föregående mätning som är ok */
		LocalDateTime datePrev = testNow.truncatedTo(ChronoUnit.SECONDS).minusMinutes(2);
		var toSavePrev = SaveMatning.builder()
				.avlastVarde(10d)
				.avlastDatum(datePrev)
				.felkod(Matningsfelkod.OK)
				.build();

		var savedPrev = matningService.create(matningstyp, toSavePrev, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(savedPrev.getId());
		assertEquals(datePrev, savedPrev.getAvlastDatum());
		assertEquals(10d, savedPrev.getAvlastVarde());

		/** Felaktigt värde som ska ignoreras i beräkningen */
		LocalDateTime datePrevFel = testNow.truncatedTo(ChronoUnit.SECONDS).minusMinutes(1);
		var toSavePrevFel = SaveMatning.builder()
				.avlastVarde(99d)
				.avlastDatum(datePrevFel)
				.felkod(Matningsfelkod.HINDER)
				.build();

		var savedPrevFel = matningService.create(matningstyp, toSavePrevFel, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(savedPrevFel.getId());
		assertEquals(datePrevFel, savedPrevFel.getAvlastDatum());
		assertEquals(99d, savedPrevFel.getAvlastVarde());

		/* Felaktigt väre som också ska ignoreras i beräkningen */
		LocalDateTime datePrevFel2 = testNow.truncatedTo(ChronoUnit.SECONDS).minusMinutes(1);
		var toSavePrevFel2 = SaveMatning.builder()
				.avlastVarde(99d)
				.avlastDatum(datePrevFel2)
				.felkod(Matningsfelkod.OK)
				.build();

		var savedPrevFel2 = matningService.create(matningstyp, toSavePrevFel2, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		savedPrevFel2 = matningService.review(
				savedPrevFel2.getId(),
				null,
				ReviewMatning.builder().status(Matningstatus.FEL).build()
		);
		assertNotNull(savedPrevFel2.getId());
		assertEquals(datePrevFel2, savedPrevFel2.getAvlastDatum());
		assertEquals(99d, savedPrevFel2.getAvlastVarde());
		assertEquals(Matningstatus.FEL, savedPrevFel2.getStatus());

		/* Felaktigt väre som också ska ignoreras i beräkningen */
		LocalDateTime datePrevFel3 = testNow.truncatedTo(ChronoUnit.SECONDS).minusMinutes(1);
		var toSavePrevFel3 = SaveMatning.builder()
				.avlastVarde(99d)
				.avlastDatum(datePrevFel3)
				.felkod(Matningsfelkod.HINDER)
				.build();

		var savedPrevFel3 = matningService.create(matningstyp, toSavePrevFel3, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		savedPrevFel3 = matningService.review(
				savedPrevFel3.getId(),
				null,
				ReviewMatning.builder().status(Matningstatus.GODKANT).build()
		);
		assertNotNull(savedPrevFel3.getId());
		assertEquals(datePrevFel3, savedPrevFel3.getAvlastDatum());
		assertEquals(99d, savedPrevFel3.getAvlastVarde());
		assertEquals(Matningstatus.GODKANT, savedPrevFel3.getStatus());


		/* Sista mätning som ska få rätt värde beräknat */
		LocalDateTime date = datePrev.plusMinutes(2);
		var toSave = SaveMatning.builder()
				.avlastVarde(20d)
				.avlastDatum(date)
				.felkod(Matningsfelkod.OK)
				.build();
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date, saved.getAvlastDatum());
		assertEquals(20d, saved.getAvlastVarde());
		assertEquals(5000d, saved.getBeraknatVarde());

		verify(matningsloggService).addHandelseRapporterad(matningstyp, savedPrev.getId(), savedPrev.getAvlastVarde(), null);
		verify(matningsloggService).addHandelseRapporterad(matningstyp, saved.getId(), saved.getAvlastVarde(), null);
		verify(matningsloggService).addHandelseBeraknadFromPrev(false, matningstyp, savedPrev.getId(), Optional.empty());
		verify(matningsloggService).addHandelseBeraknadFromPrev(true, matningstyp, saved.getId(), Optional.of(savedPrev));
	}

	@Test
	@WithMockUserMatrapportor
	void när_nivå_vatten_luft_tryck_sparas_skall_värde_beräknas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromOther(anyBoolean(), any(), any(), any(), any())).thenReturn(new Matningslogg());


		var matningstyp = matningstypService.findById(MATNINGSTYP_VATTENTRYCK).orElseThrow();
		var matningstypLuft = matningstypService.findById(MATNINGSTYP_LUFTTRYCK).orElseThrow();

		LocalDateTime dateLuft = testNow.truncatedTo(ChronoUnit.SECONDS);
		var toSaveLuft = saveMatning
				.avlastVarde(10d)
				.avlastDatum(dateLuft)
				.build();
		var savedLuft = matningService.create(matningstypLuft, toSaveLuft, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(savedLuft.getId());
		assertEquals(dateLuft, savedLuft.getAvlastDatum());
		assertEquals(10d, savedLuft.getAvlastVarde());

		LocalDateTime date = dateLuft.plusSeconds(10);
		var toSave = saveMatning
				.avlastVarde(20d)
				.avlastDatum(date)
				.build();

		matningstyp.setBerakningKonstant(2d);
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date, saved.getAvlastDatum());
		assertMatningEqualsSaveMatning(saved, toSave);
		assertEquals(Detektionsomrade.INOM, saved.getInomDetektionsomrade());
		assertEquals(Matningstatus.EJGRANSKAT, saved.getStatus());
		assertTrue(Math.abs(2.098 - saved.getBeraknatVarde()) < 0.00001);

		verify(matningsloggService).addHandelseRapporterad(matningstypLuft, savedLuft.getId(), savedLuft.getAvlastVarde(), "En kommentar");
		verify(matningsloggService).addHandelseRapporterad(matningstyp, saved.getId(), saved.getAvlastVarde(), "En kommentar");
		verify(matningsloggService).addHandelseBeraknadFromOther(true, matningstyp, saved.getId(),
				Optional.of(savedLuft), DefinitionMatningstypConstants.LUFTTRYCK_PER_TIMME);
	}

	@Test
	@WithMockUserMatrapportor
	void när_mätvärde_uppdateras_skall_värde_beräknas_igen() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromPrev(anyBoolean(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();
		var savePrevVarde = 10d;
		var saveVarde = 20d;
		var updateVarde = 30d;
		LocalDateTime datePrev = testNow.truncatedTo(ChronoUnit.SECONDS).minusMinutes(2);
		var toSavePrev = saveMatning
				.avlastVarde(savePrevVarde)
				.avlastDatum(datePrev)
				.build();

		var savedPrev = matningService.create(matningstyp, toSavePrev, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(savedPrev.getId());
		assertEquals(datePrev, savedPrev.getAvlastDatum());
		assertEquals(savePrevVarde, savedPrev.getAvlastVarde());

		LocalDateTime date = datePrev.plusMinutes(2);
		var toSave = saveMatning
				.avlastVarde(saveVarde)
				.avlastDatum(date)
				.build();
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date, saved.getAvlastDatum());
		assertEquals(saveVarde, saved.getAvlastVarde());
		assertEquals(5000d, saved.getBeraknatVarde());

		var toUpdate = saveMatning
				.avlastVarde(updateVarde)
				.avlastDatum(date)
				.build();
		var updated = matningService.create(matningstyp, toUpdate, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertEquals(date, updated.getAvlastDatum());
		assertEquals(updateVarde, updated.getAvlastVarde());
		assertEquals(10000d, updated.getBeraknatVarde());

		verify(matningsloggService, times(1)).addHandelseRapporterad(matningstyp, saved.getId(), saveVarde, "En kommentar");
		verify(matningsloggService, times(1)).addHandelseRapporterad(matningstyp, saved.getId(), updateVarde, "En kommentar");
		verify(matningsloggService, times(2)).addHandelseBeraknadFromPrev(true, matningstyp, saved.getId(), Optional.of(savedPrev));
	}

	@Test
	@WithMockUserMatrapportor
	void när_sida_med_mätningar_hämtas_ska_den_sorteras_på_datum() throws
			MatningIllegalMatvarde, AlreadyGodkandException {
		var matningstyp = matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();

		LocalDateTime date = testNow.truncatedTo(ChronoUnit.SECONDS);
		LocalDateTime dateStart = date.minusDays(5);
		for (int i = 1; i < 6; i++) {
			matningService.create(matningstyp, SaveMatning.builder()
					.avlastDatum(dateStart.plusDays(i))
					.avlastVarde((double) i)
					.felkod(Matningsfelkod.OK)
					.kommentar("En kommentar")
					.rapportor("Mätbolaget")
					.build(), false, StandardKallsystem.MiljöKoll.getNamn());
		}

		var page0 = matningService.getMatningarForMatningstyp(matningstyp.getId(), 0, 5,
				"avlastDatum", Sort.Direction.DESC, null, null, null);
		assertEquals(1, page0.getTotalPages());
		assertEquals(5, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(5, page0.getContent().size());
		assertEquals(date, page0.getContent().get(0).getAvlastDatum());
		assertEquals(5d, page0.getContent().get(0).getAvlastVarde());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_sida_med_mätningar_hämtas_ska_den_filtreras_på_status() throws MatningIllegalMatvarde, AlreadyGodkandException {
		var matningstyp =matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();

		LocalDateTime date = testNow.truncatedTo(ChronoUnit.SECONDS);
		LocalDateTime dateStart = date.minusDays(5);
		ReviewMatning.ReviewMatningBuilder reviewMatning = ReviewMatning.builder()
				.status(Matningstatus.GODKANT);

		for (int i = 1; i < 6; i++) {
			var matning = matningService.create(matningstyp, SaveMatning.builder()
					.avlastDatum(dateStart.plusDays(i))
					.avlastVarde((double) i)
					.felkod(Matningsfelkod.OK)
					.kommentar("En kommentar")
					.rapportor("Mätbolaget")
					.build(), false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
			if (i > 3) {
				matningService.review(matning.getId(), matningstyp, reviewMatning.build());
			}
		}

		var page0 = matningService.getMatningarForMatningstyp(matningstyp.getId(), 0, 5,
				"avlastDatum", Sort.Direction.DESC, null, null, Matningstatus.EJGRANSKAT);
		assertEquals(1, page0.getTotalPages());
		assertEquals(3, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(3, page0.getContent().size());
	}

	@Test
	@WithMockUserMatrapportor
	void när_sida_med_mätningar_hämtas_ska_den_filtreras_på_datum() throws MatningIllegalMatvarde, AlreadyGodkandException {
		var matningstyp = matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();

		LocalDateTime date = testNow.truncatedTo(ChronoUnit.SECONDS);
		LocalDateTime dateStart = date.minusDays(5);
		for (int i = 1; i < 6; i++) {
			matningService.create(matningstyp, SaveMatning.builder()
					.avlastDatum(dateStart.plusDays(i))
					.avlastVarde((double) i)
					.felkod(Matningsfelkod.OK)
					.kommentar("En kommentar")
					.rapportor("Mätbolaget")
					.build(), false, StandardKallsystem.MiljöKoll.getNamn());
		}

		var page0 = matningService.getMatningarForMatningstyp(matningstyp.getId(), 0, 5,
				"avlastDatum", Sort.Direction.DESC, date.minusDays(3), date.minusDays(1), null);
		assertEquals(1, page0.getTotalPages());
		assertEquals(3, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(3, page0.getContent().size());
		assertEquals(date.minusDays(1), page0.getContent().get(0).getAvlastDatum());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_mätvärde_korrigeras_skall_värde_beräknas_igen() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseRapporterad(any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromPrev(anyBoolean(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();

		LocalDateTime datePrev = testNow.truncatedTo(ChronoUnit.SECONDS).minusMinutes(2);
		var toSavePrev = saveMatning
				.avlastVarde(10d)
				.avlastDatum(datePrev)
				.build();
		var savedPrev = matningService.create(matningstyp, toSavePrev, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(savedPrev.getId());
		assertEquals(datePrev, savedPrev.getAvlastDatum());
		assertEquals(10d, savedPrev.getAvlastVarde());

		LocalDateTime date = datePrev.plusMinutes(2);
		var toSave = saveMatning
				.avlastVarde(20d)
				.avlastDatum(date)
				.build();
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date, saved.getAvlastDatum());
		assertEquals(20d, saved.getAvlastVarde());
		assertEquals(5000d, saved.getBeraknatVarde());

		var approveMatning = ReviewMatning.builder()
				.status(Matningstatus.GODKANT);
		matningService.review(saved.getId(), matningstyp, approveMatning.build());

		var reviewMatning = ReviewMatning.builder()
				.avlastVarde(30d)
				.operation(Matningoperation.REPLACE);
		var reviewed = matningService.review(saved.getId(), matningstyp, reviewMatning.build());

		assertEquals(date, reviewed.getAvlastDatum());
		assertEquals(30d, reviewed.getAvlastVarde());
		assertEquals(10000d, reviewed.getBeraknatVarde());
		assertEquals(Matningstatus.EJGRANSKAT, reviewed.getStatus());

		verify(matningsloggService).addHandelseKorrigerad(saved.getId(), 20d, 30d);
		verify(matningsloggService, times(2)).addHandelseBeraknadFromPrev(true, matningstyp, reviewed.getId(), Optional.of(savedPrev));
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void när_mätvärde_godkänns_eller_felmarkeras_skall_det_loggas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseStatus(any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromPrev(anyBoolean(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();

		LocalDateTime date = testNow.truncatedTo(ChronoUnit.SECONDS).minusMinutes(2);

		var toSave = saveMatning
				.avlastVarde(20d)
				.avlastDatum(date)
				.build();
		var saved = matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date, saved.getAvlastDatum());
		assertEquals(20d, saved.getAvlastVarde());

		var reviewMatning = ReviewMatning.builder()
				.status(Matningstatus.GODKANT);
		var reviewed = matningService.review(saved.getId(), matningstyp, reviewMatning.build());

		assertEquals(date, reviewed.getAvlastDatum());
		assertEquals(20d, reviewed.getAvlastVarde());

		verify(matningsloggService).addHandelseStatus(reviewed.getId(), Matningstatus.GODKANT);

		reviewMatning = ReviewMatning.builder()
				.status(Matningstatus.FEL);
		reviewed = matningService.review(saved.getId(), matningstyp, reviewMatning.build());

		assertEquals(date, reviewed.getAvlastDatum());
		assertEquals(20d, reviewed.getAvlastVarde());
		verify(matningsloggService).addHandelseStatus(reviewed.getId(), Matningstatus.FEL);
	}

	@Test
	@WithMockUserObservator
	void när_sida_med_mätningar_hämtas_av_observatör_ska_den_inte_tillåtas_för_andra_status_än_godkänt() throws MatningIllegalMatvarde, AlreadyGodkandException {
		var matningstyp =matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();

		assertThrows(AccessDeniedException.class, () -> {
			var page0 = matningService.getMatningarForMatningstyp(matningstyp.getId(), 0, 5,
					"avlastDatum", Sort.Direction.DESC, null, null, Matningstatus.EJGRANSKAT);
		});
		assertThrows(AccessDeniedException.class, () -> {
			var page0 = matningService.getMatningarForMatningstyp(matningstyp.getId(), 0, 5,
					"avlastDatum", Sort.Direction.DESC, null, null, Matningstatus.FEL);
		});
		assertThrows(AccessDeniedException.class, () -> {
			var page0 = matningService.getMatningarForMatningstyp(matningstyp.getId(), 0, 5,
					"avlastDatum", Sort.Direction.DESC, null, null, null);
		});

		var page0 = matningService.getMatningarForMatningstyp(matningstyp.getId(), 0, 5,
				"avlastDatum", Sort.Direction.DESC, null, null, Matningstatus.GODKANT);

	}

	@Test
	@WithMockUserMatrapportor
	void när_mätningar_läses_in_från_CSVfil_standard_skall_mätningarna_läsas_in() throws IOException {
		try (var csv = getCsvFileContent(CSVFILE_STANDARD)) {
			var matningar = matningService.parseCsvImport(csv, ImportFormat.STANDARD);
			assertEquals(5, matningar.size());
			var optMatningX = matningar.stream().filter(im -> "matobjektX".equals(im.getMatobjekt())).findFirst();
			assertTrue(optMatningX.isPresent());
			var matningX = optMatningX.get();
			assertEquals("Nivå", matningX.getMatningstyp());
			assertEquals("2020-02-03 13:23", matningX.getAvlastDatum());
			assertEquals(">", matningX.getInomDetektionsomrade());
			assertEquals("23.5", matningX.getAvlastVarde());
			assertEquals("10.7", matningX.getBeraknatVarde());
			assertEquals("m", matningX.getEnhetAvlast());
			assertEquals("Ok", matningX.getFelkod());
			assertEquals("Kommentar", matningX.getKommentar());
		}
	}

	@Test
	@WithMockUserMatrapportor
	void när_mätningar_läses_in_från_CSVfil_instrument_skall_mätningarna_läsas_in() throws IOException {
		try (var csv = getCsvFileContent(CSVFILE_INSTRUMENT)) {
			var matningar = matningService.parseCsvImport(csv, ImportFormat.INSTRUMENT);
			assertEquals(2, matningar.size());
			{
				var matning = matningar.get(0);
				assertEquals("12345", matning.getInstrument());
				assertEquals("2020-02-03 13:23", matning.getAvlastDatum());
				assertEquals("23,50", matning.getAvlastVarde());
				assertEquals("Ok", matning.getFelkod());
				assertEquals("Kommentar", matning.getKommentar());
			}

			{
				var matning = matningar.get(1);
				assertEquals("12345", matning.getInstrument());
				assertEquals("2020-02-03 13:29", matning.getAvlastDatum());
				assertEquals("", matning.getAvlastVarde());
				assertEquals("Hinder", matning.getFelkod());
				assertEquals("Kommentar", matning.getKommentar());
			}
		}
	}

	@Test
	@WithMockUserMatrapportor
	void när_mätningar_läses_in_från_CSVfil_med_ogiltig_header_skall_mätningarna_ej_läsas_in()
			throws IOException {
		try (var csv = getCsvFileContent(CSVFILE_BAD_ARGUMENT)) {
   			assertThrows(IllegalArgumentException.class, () -> matningService.parseCsvImport(csv, ImportFormat.STANDARD));
		}
	}

	@Test
	@WithMockUserMatrapportor
	void när_mätningar_läses_in_från_CSVfil_med_frånvarande_nödvändig_header_skall_mätningarna_ej_läsas_in()
			throws IOException {
		try (var csv = getCsvFileContent(CSVFILE_MISSING_ARGUMENT)) {
			assertThrows(IllegalArgumentException.class, () -> matningService.parseCsvImport(csv, ImportFormat.STANDARD));
		}
	}

	@Test
	@WithMockUserMatrapportor
	void när_mätningar_läses_in_från_CSVfil_med_överflödig_header_skall_mätningarna_ej_läsas_in()
			throws IOException {
		try (var csv = getCsvFileContent(CSVFILE_EXTRA_ARGUMENT)) {
			assertThrows(IllegalArgumentException.class, () -> matningService.parseCsvImport(csv, ImportFormat.STANDARD));
		}
	}

	@Test
	@WithMockUserAdministrator
	void när_importerat_mätvärde_automatiskt_granskats_ska_det_loggas() throws MatningIllegalMatvarde, AlreadyGodkandException {
		when(matningsloggService.addHandelseImporterad(any(), any(), any(), any(), any(), any())).thenReturn(new Matningslogg());
		when(matningsloggService.addHandelseBeraknadFromOther(anyBoolean(), any(), any(), any(), any())).thenReturn(new Matningslogg());
		var matningstyp = matningstypService.findById(MATNINGSTYP_VATTENTRYCK).orElseThrow();

		LocalDateTime date = testNow.truncatedTo(ChronoUnit.SECONDS).minusDays(3);

		var toSave = saveMatning
				.avlastVarde(20d)
				.avlastDatum(date)
				.build();
		var saved = matningService.create(matningstyp, toSave, true, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date, saved.getAvlastDatum());
		assertEquals(20d, saved.getAvlastVarde());
		assertEquals(saved.getStatus(), Matningstatus.GODKANT);
		verify(matningsloggService).addHandelseAutomatisktGranskat(saved.getId(), Matningstatus.GODKANT);

		toSave = saveMatning
				.avlastVarde(31d)
				.avlastDatum(date.plusDays(1))
				.build();
		saved = matningService.create(matningstyp, toSave, true, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date.plusDays(1), saved.getAvlastDatum());
		assertEquals(31d, saved.getAvlastVarde());
		assertEquals(saved.getStatus(), Matningstatus.EJGRANSKAT);
		verify(matningsloggService).addHandelseAutomatisktGranskat(saved.getId(), Matningstatus.EJGRANSKAT);

		toSave = saveMatning
				.avlastVarde(9d)
				.avlastDatum(date.plusDays(2))
				.build();
		saved = matningService.create(matningstyp, toSave, true, StandardKallsystem.MiljöKoll.getNamn()).getMatning();
		assertNotNull(saved.getId());
		assertEquals(date.plusDays(2), saved.getAvlastDatum());
		assertEquals(9d, saved.getAvlastVarde());
		assertEquals(saved.getStatus(), Matningstatus.EJGRANSKAT);
		verify(matningsloggService).addHandelseAutomatisktGranskat(saved.getId(), Matningstatus.EJGRANSKAT);

	}

	@Test
	@WithMockUserAdministrator
	void när_mätvärde_rapporteras_ska_enbart_ett_larm_genereras() throws AlreadyGodkandException, MatningIllegalMatvarde {
		final var GRANSVARDE_MAX = 1.3;
		var matningstyp = matningstypService.findById(MATNINGSTYP_NEDMATNING).orElseThrow();
		var gransvarde1 = gransvardeMax.gransvarde(GRANSVARDE_MAX / 2.).build();
		var gransvarde2 = gransvardeMax.gransvarde(GRANSVARDE_MAX).build();

		gransvardeService.createGransvarde(gransvarde1);
		gransvardeService.createGransvarde(gransvarde2);

		var saveMatning = this.saveMatning.avlastVarde(GRANSVARDE_MAX * 2).build();
		matningService.create(matningstyp, saveMatning, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning();

		var larmList = larmService.getLarmForMatningstyp(matningstyp.getId());

		assertEquals(larmList.size(), 1);
		assertEquals(larmList.get(0).getGransvarde(), GRANSVARDE_MAX);
	}

	@Test
	@WithMockUserMatrapportor
	private InputStream getCsvFileContent(String fileName) throws IOException {
		return this.getClass().getResourceAsStream("/csv/" + fileName);
	}
}
