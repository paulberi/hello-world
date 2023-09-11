package se.metria.matdatabas.service.matrunda;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.matrunda.dto.EditMatrunda;
import se.metria.matdatabas.service.matrunda.dto.Matrunda;
import se.metria.matdatabas.service.matrunda.dto.MatrundaMatningstyp;
import se.metria.matdatabas.service.matrunda.dto.RapporteringMatningstyp;
import se.metria.matdatabas.service.matrunda.exception.MatrundaConflictException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class MatrundaRapporteringServiceTest {

	@Autowired
	private MatrundaService matrundaService;

	@Autowired
	private MatrundaRepository repository;

	@Autowired
	private MatrundaRapporteringDataService matrundaRapporteringDataService;

	@MockBean
	private MatdatabasUser user;

	private RapporteringMatningstyp.RapporteringMatningstypBuilder defaultMatningstyp = RapporteringMatningstyp.builder()
			.matobjekt("Mitt mätobjekt")
			.matningstyp("Nedmätning")
			.storhet("Nivå")
			.enhet("m")
			.lage("På gräsmattan")
			.status1(1)
			.status2(1)
			.status3(1)
			.maxPejlbartDjup(20d)
			.varde1("20,2")
			.varde2("20,4")
			.varde3("20,5");

	@Test
	void en_tom_mätrunda_genererar_en_tomt_fältprotokoll() throws IOException {
		MatrundaRapporteringDataService matrundaRapporteringDataService = mock(MatrundaRapporteringDataService.class);

		when(matrundaRapporteringDataService.getMatrundaName(any())).thenReturn("Min mätrunda");
		when(matrundaRapporteringDataService.getMatningstyper(any(), any())).thenReturn(Collections.emptyList());

		var faltrapportService = new MatrundaRapporteringService(matrundaRapporteringDataService);

		var excelFile = faltrapportService.generateForMatrunda(1, LocalDateTime.now());
		assertNotNull(excelFile);
		assertTrue(excelFile.size() > 0);
	}

	@Test
	void en_fältrapport_kan_genereras() throws IOException {
		MatrundaRapporteringDataService matrundaRapporteringDataService = mock(MatrundaRapporteringDataService.class);

		var matningstyp1 = defaultMatningstyp.build();

		var matningstyp2 = defaultMatningstyp.build();
		matningstyp2.setLage("Bakom grästmattan");

		var matningstyp3 = defaultMatningstyp.build();
		matningstyp3.setLage("Framför grästmattan");


		when(matrundaRapporteringDataService.getMatrundaName(any())).thenReturn("Min mätrunda");
		when(matrundaRapporteringDataService.getMatningstyper(any(), any())).thenReturn(
				List.of(
						matningstyp1,
						matningstyp2,
						matningstyp3
				)
		);

		var faltrapportService = new MatrundaRapporteringService(matrundaRapporteringDataService);

		var excelFile = faltrapportService.generateForMatrunda(1, LocalDateTime.now());
		assertNotNull(excelFile);
		assertTrue(excelFile.size() > 0);
	}

	@Test
	@Sql({"/data/MatrundaServiceIT.sql"})
	@WithMockUserTillstandshandlaggare
	void inaktiva_matobjekt_ska_inte_hämtas() throws MatrundaConflictException {
		// Given
		Matrunda matrunda1 = matrundaService.createMatrunda(EditMatrunda.builder()
				.namn("matrunda")
				.aktiv(true)
				.beskrivning("beskrivning1")
				.matningstyper(List.of(
						MatrundaMatningstyp.builder().matningstypId(10002).ordning((short)2).build(),
						MatrundaMatningstyp.builder().matningstypId(10007).ordning((short)3).build())
				).build());

		// When
		var matningstyper = matrundaRapporteringDataService.getMatningstyper(matrunda1.getId(), LocalDateTime.now());

		// Then
		assertEquals(matningstyper.size(), 1);
		assertEquals(matningstyper.get(0).getMatningstypId(), 10002);
	}
}
