package se.metria.matdatabas.service.matningslogg;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypConstants;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.definitionmatningstyp.exception.DefinitionMatningstypConflictException;
import se.metria.matdatabas.service.definitionmatningstyp.exception.DefinitionMatningstypNotFoundException;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.*;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matning.dto.ReviewMatning;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypRepository;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektConflictException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp.INFILTRATION_MEDEL_FLODE;

@DisplayName("Givet mätningslogg service tjänst")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Sql({"/data/test_create_matningstyper.sql"})
class MatningsloggServiceIT {
	
	@Autowired
	private MatobjektService matobjektService;

	@Autowired
	private DefinitionMatningstypService definitionMatningstypService;

	@Autowired
	MatningstypService matningstypService;
	
	@Autowired
	private MatningService matningService;

	@Autowired
	private MatningsloggService service;
		
	@MockBean
	private MatdatabasUser user;

	@MockBean
	private MatningvardeValidator validator;

	@Autowired
	private MatningstypRepository matningstypRepository;

	@Autowired
	private MatningRepository matningRepository;

	@Autowired
	private MatningsloggRepository matningsloggRepository;


	private static final Integer MATNINGSTYP_NEDMATNING = 1;
	private static final Integer MATNINGSTYP_PORTTRYCK = 2;
	private static final Integer MATNINGSTYP_SATTNING = 3;
	private static final Integer MATNINGSTYP_NEDMATNING_MAX = 6;
	private static final Integer MATNINGSTYP_FLODE = 5;
	private static final Integer MATNINGSTYP_VATTENTRYCK = 7;
	private static final Integer MATNINGSTYP_LUFTTRYCK = 8;
	private static final Integer MATNINGSTYP_MEDELFLÖDE = 9;
	private static final Integer MATNINGSTYP_TUNNEL_MEDELFLÖDE = 10;

	
	SaveMatning.SaveMatningBuilder saveMatning = SaveMatning.builder()
			.avlastDatum(LocalDateTime.now())
			.avlastVarde(23.456d)
			.beraknatVarde(12.3456d)
			.felkod(Matningsfelkod.OK)
			.kommentar("En kommentar")
			.rapportor("Mätbolaget");
	
	Matning.MatningBuilder otherMatning = Matning.builder()
			.avlastDatum(LocalDateTime.now())
			.avlastVarde(13.456d)
			.felkod(Matningsfelkod.OK)
			.kommentar("En annan kommentar")
			.rapportor("Mätbolaget2");

	private Integer userId = 42;

	@BeforeEach
	void setUp() throws Exception {
		when(user.getId()).thenReturn(userId);
		when(validator.validate(any(), any(), any())).thenReturn(true);
	}

	@AfterEach
	void tearDown() {
		matningsloggRepository.deleteAll();
		matningRepository.deleteAll();
		matningstypRepository.deleteAll();
	}

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt... 
	void när_mätning_rapporteras_till_logg_skall_den_sparas_i_logg()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		var matningsTyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var matning = saveMatning.build();
		var matningId = createMatning(matningsTyp, matning);

		var matningslogg = service.addHandelseRapporterad(matningsTyp, matningId, matning.getAvlastVarde(), null);

		assertEquals(MatningsloggHandelse.RAPPORTERAT, matningslogg.getHandelse());
		assertTrue(matningslogg.getBeskrivning().contains(matning.getAvlastVarde() + matningsTyp.getEnhet()));
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt...
	void när_mätning_med_felkod_rapporteras_till_logg_skall_den_sparas_i_logg()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		var felkod = Matningsfelkod.FRUSET;
		var matning = saveMatning.felkod(felkod).build();
		var matningsTyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var matningId = createMatning(matningsTyp, matning);

		var matningslogg = service.addHandelseRapporteradFelkod(matningId, felkod, null);

		assertEquals(MatningsloggHandelse.RAPPORTERAT, matningslogg.getHandelse());
		assertTrue(matningslogg.getBeskrivning().contains(felkod));
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}

	@Test
	@WithMockUserAdministrator
	void när_mätning_ändras_till_logg_skall_den_sparas_i_logg()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		// Given
		var matningsTyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var matningId = createMatning(matningsTyp, saveMatning.build());
		var oldValue = 13.1;
		var newValue = 5.4;
		var beskrivning = String.format("Värde från %1$.4f till %2$.4f", oldValue, newValue);

		// When
		var matningslogg = service.addHandelseAndrat(matningId, oldValue, newValue, null, null, null, null, null, null);

		// Then
		assertEquals(MatningsloggHandelse.ANDRAT, matningslogg.getHandelse());
		assertEquals(beskrivning, matningslogg.getBeskrivning());
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt... 
	void när_mätning_med_mätningstyp_sättning_rapporteras_till_logg_skall_den_sparas_i_logg_med_fixpunkt()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		var matningsTyp = matningstypService.findById(MATNINGSTYP_SATTNING).orElseThrow();
		var fixpunkt = "Fixpunkt:AB42";
		matningsTyp.setFixpunkt(fixpunkt);
		var matning = saveMatning.build();
		var matningId = createMatning(matningsTyp, matning);
		var matningslogg = service.addHandelseRapporterad(matningsTyp, matningId, matning.getAvlastVarde(), null);
		assertEquals(MatningsloggHandelse.RAPPORTERAT, matningslogg.getHandelse());
		assertTrue(matningslogg.getBeskrivning().contains(fixpunkt));
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt...
	void när_mätning_importeras_till_logg_skall_den_sparas_i_logg()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		var matning = saveMatning.build();
		var matningsTyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var matningId = createMatning(matningsTyp, matning);
		var beraknadEnhet = "Pa";

		var matningslogg = service.addHandelseImporterad(matningsTyp, matningId, matning.getAvlastVarde(), matning.getBeraknatVarde(), beraknadEnhet, null);

		assertEquals(MatningsloggHandelse.IMPORTERAT, matningslogg.getHandelse());
		assertTrue(matningslogg.getBeskrivning().contains(matning.getAvlastVarde() + matningsTyp.getEnhet()));
		assertTrue(matningslogg.getBeskrivning().contains(matning.getBeraknatVarde() + beraknadEnhet));
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt...
	void när_mätning_med_felkod_importeras_till_logg_skall_den_sparas_i_logg()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		var felkod = Matningsfelkod.FRUSET;
		var matning = saveMatning.felkod(felkod).build();
		var matningsTyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var matningId = createMatning(matningsTyp, matning);

		var matningslogg = service.addHandelseImporteradFelkod(matningId, felkod, "En kommentar" );

		assertEquals(MatningsloggHandelse.IMPORTERAT, matningslogg.getHandelse());
		assertTrue(matningslogg.getBeskrivning().contains(felkod));
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt...
	void när_mätning_med_mätningstyp_sättning_importeras_till_logg_skall_den_sparas_i_logg_med_fixpunkt()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		var matningsTyp = matningstypService.findById(MATNINGSTYP_SATTNING).orElseThrow();
		var fixpunkt = "Fixpunkt:AB42";
		matningsTyp.setFixpunkt(fixpunkt);
		var matningId = createMatning(matningsTyp, saveMatning.build());
		var matningslogg = service.addHandelseImporterad(matningsTyp, matningId, 123., null, "m", null);
		assertEquals(MatningsloggHandelse.IMPORTERAT, matningslogg.getHandelse());
		assertTrue(matningslogg.getBeskrivning().contains(fixpunkt));
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}
	
	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt... 
	void när_mätning_beräknad_från_parametrar_läggs__till_i_logg_skall_den_sparas_i_logg()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		var matningsTyp = matningstypService.findById(MATNINGSTYP_FLODE).orElseThrow();
		var matning = saveMatning.build();
		var matningId = createMatning(matningsTyp, matning);
		matningsTyp.setBerakningKonstant(1d);
		matningsTyp.setBerakningReferensniva(2d);
		var refnivaNamn = "Referensnivå";
		service.addHandelseRapporterad(matningsTyp, matningId, matning.getAvlastVarde(), null);
		var matningslogg = service.addHandelseBeraknadFromParams(true, matningsTyp, matningId, refnivaNamn);
		var matningslogg2 = service.addHandelseBeraknadFromParams(true, matningsTyp, matningId, refnivaNamn);
		assertEquals(MatningsloggHandelse.BERAKNING, matningslogg.getHandelse());
		assertEquals(String.format("%1$s, %2$s: %3$s m, Gradningskonstant: %4$s", matningsTyp.getTyp(), refnivaNamn, matningsTyp.getBerakningReferensniva(), matningsTyp.getBerakningKonstant())
				, matningslogg.getBeskrivning());
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}
	
	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt... 
	void när_mätning_beräknad_från_föregående_värde_läggs__till_i_logg_skall_den_sparas_i_logg()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException, DefinitionMatningstypNotFoundException {
		var definitionMatningstyp = definitionMatningstypService.findByBerakningstyp(INFILTRATION_MEDEL_FLODE);
		var matningsTyp = matningstypService.findByDefinitionMatningstypId(definitionMatningstyp.getId());
		var matningId = createMatning(matningsTyp, saveMatning.build());
		var prev = otherMatning.build();
		var matningslogg = service.addHandelseBeraknadFromPrev(true, matningsTyp, matningId, Optional.of(prev));
		assertEquals(MatningsloggHandelse.BERAKNING, matningslogg.getHandelse());
		assertEquals(String.format("%1$s, Föregående mätvärde (%2$s): %3$.4f m3", matningsTyp.getTyp(), prev.getAvlastDatum().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), prev.getAvlastVarde())
				, matningslogg.getBeskrivning());
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}
	
	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt... 
	void när_mätning_beräknad_från_annat_värde_läggs__till_i_logg_skall_den_sparas_i_logg()
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException, DefinitionMatningstypNotFoundException {
		var definitionMatningstyp = definitionMatningstypService.findByBerakningstyp(INFILTRATION_MEDEL_FLODE);
		var matningsTyp = matningstypService.findByDefinitionMatningstypId(definitionMatningstyp.getId());
		var matningId = createMatning(matningsTyp, saveMatning.build());
		matningsTyp.setBerakningKonstant(1d);
		var other = otherMatning.build();
		var matningslogg = service.addHandelseBeraknadFromOther(true, matningsTyp, matningId, Optional.of(other), DefinitionMatningstypConstants.LUFTTRYCK_PER_TIMME);
		assertEquals(MatningsloggHandelse.BERAKNING, matningslogg.getHandelse());
		assertEquals(String.format("%1$s, Beräkningskonstant: %2$s, %3$s (%4$s): %5$.4f hPa", matningsTyp.getTyp(), matningsTyp.getBerakningKonstant(), 
				DefinitionMatningstypConstants.LUFTTRYCK_PER_TIMME, other.getAvlastDatum().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), other.getAvlastVarde())
				, matningslogg.getBeskrivning());
		assertEquals(matningId, matningslogg.getMatningId());
		assertEquals(userId, matningslogg.getLoggatAvId());
		assertNotNull(matningslogg.getLoggatDatum());
		assertTrue(LocalDateTime.now().isAfter(matningslogg.getLoggatDatum()));
	}

	@Test
	@WithMockUserAdministrator // Vi borde köra som Mätrapportör men då får vi inte skapa mätobjekt...
	void när_sida_med_mätningslogg_hämtas_ska_den_hämta_alla_poster_för_mätningen() throws MatobjektConflictException,
			DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		var matningsTyp = matningstypService.findById(MATNINGSTYP_TUNNEL_MEDELFLÖDE).orElseThrow();
		var matningId = createMatning(matningsTyp, saveMatning.build());

		var reviewMatning = ReviewMatning.builder()
				.avlastVarde(30d)
				.operation(Matningoperation.REPLACE);
		matningService.review(matningId, matningsTyp, reviewMatning.build());
		reviewMatning  = ReviewMatning.builder()
				.status(Matningstatus.GODKANT);
		matningService.review(matningId, matningsTyp, reviewMatning.build());

		var page0 = service.getMatningslogg(matningId, 0, 5,
				"loggatDatum", Sort.Direction.DESC);
		assertEquals(1, page0.getTotalPages());
		assertEquals(4, page0.getTotalElements());
		assertEquals(0, page0.getNumber());
		assertEquals(4, page0.getContent().size());
	}

	private Long createMatning(Matningstyp matningstyp, SaveMatning toSave)
			throws MatobjektConflictException, DefinitionMatningstypConflictException, MatningIllegalMatvarde, AlreadyGodkandException {
		return matningService.create(matningstyp, toSave, false, StandardKallsystem.MiljöKoll.getNamn()).getMatning().getId();
	}

}
