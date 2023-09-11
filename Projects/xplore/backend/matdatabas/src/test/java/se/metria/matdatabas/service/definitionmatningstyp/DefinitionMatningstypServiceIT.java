package se.metria.matdatabas.service.definitionmatningstyp;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
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
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.definitionmatningstyp.dto.SaveDefinitionMatningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.exception.DefinitionMatningstypConflictException;
import se.metria.matdatabas.service.matobjekt.MatobjektConstants;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class DefinitionMatningstypServiceIT {

	@Autowired
	private DefinitionMatningstypService definitionMatningstypService;

	@Autowired
	private DefinitionMatningstypRepository repository;

	@MockBean
	private MatdatabasUser user;

	private SaveDefinitionMatningstyp.SaveDefinitionMatningstypBuilder defaultDefinition = SaveDefinitionMatningstyp.builder()
			.matobjektTyp((short) 3)
			.namn("TestDefinition")
			.decimaler((short) 2)
			.enhet("m")
			.automatiskInrapportering(false)
			.automatiskGranskning(false)
			.beskrivning("TestBeskrivning");

	@Test
	void is_this_working() {
		assertNotNull(definitionMatningstypService);
	}

	@BeforeEach
	void beforeEach() {
		repository.deleteAll();
	}

	@WithMockUserAdministrator
	@Test
	void en_administratör_ska_kunna_skapa_en_ny_definition() throws DefinitionMatningstypConflictException {
		var definition = definitionMatningstypService.save(defaultDefinition.build());
		assertEquals("TestDefinition", definition.getNamn());
		assertEquals(0, (int)definition.getGraftyp());
	}

	@WithMockUserAdministrator
	@Test
	void en_administratör_ska_kunna_hantera_definitioner() throws DefinitionMatningstypConflictException {
		var d1 = defaultDefinition.build();
		var d2 = defaultDefinition.build();
		var d3 = defaultDefinition.build();
		d1.setNamn("D1");
		d2.setNamn("D2");
		d2.setMatobjektTyp((short)2);
		d3.setNamn("D3");
		var savedD1 = definitionMatningstypService.save(d1);
		var savedD2 = definitionMatningstypService.save(d2);
		var savedD3 = definitionMatningstypService.save(d3);

		var all = definitionMatningstypService.findAll(null, null, 0, 10, "namn", Sort.Direction.ASC);
		assertEquals(3, all.getTotalElements());

		var filtered = definitionMatningstypService.findAll((short) 2, null, 0, 10, "namn", Sort.Direction.ASC);
		assertEquals(1, filtered.getTotalElements());

		var andringsbara = definitionMatningstypService.findAll(null, true, 0, 10, "namn", Sort.Direction.ASC);
		assertEquals(3, andringsbara.getTotalElements());

		var ejAndringsbara = definitionMatningstypService.findAll(null, false, 0, 10, "namn", Sort.Direction.ASC);
		assertEquals(0, ejAndringsbara.getTotalElements());

		d3.setNamn("D3 Uppdaterad");
		definitionMatningstypService.save(savedD3.getId(), d3);

		var updatedD3 = definitionMatningstypService.findById(savedD3.getId());
		assertTrue(updatedD3.isPresent());
		assertEquals(d3.getNamn(), updatedD3.get().getNamn());

		definitionMatningstypService.delete(savedD3.getId());
		assertFalse(definitionMatningstypService.findById(savedD3.getId()).isPresent());
	}

	@WithMockUserAdministrator
	@Test
	void definitioners_namn_ska_vara_unika_inom_matobjektstypen() throws DefinitionMatningstypConflictException {
		var d1 = defaultDefinition.build();
		d1.setNamn("Definition");
		definitionMatningstypService.save(d1);

		var d2 = defaultDefinition.build();
		d2.setNamn("Definition");
		assertThrows(DefinitionMatningstypConflictException.class, () -> {
			definitionMatningstypService.save(d2);
		});

		var d3 = defaultDefinition.build();
		d3.setNamn("Definition");
		d3.setMatobjektTyp(MatobjektConstants.TYP_VATTENKEMI);
		assertDoesNotThrow(() -> {
			definitionMatningstypService.save(d3);
		});
	}

	@WithMockUserTillstandshandlaggare
	@Test
	void en_tillståndshandläggare_ska_inte_kunna_skapa_definitioner() {
		assertThrows(AccessDeniedException.class, () -> {
			definitionMatningstypService.save(defaultDefinition.build());
		});
	}

	@WithMockUserMatrapportor
	@Test
	void en_mätrapportör_ska_inte_kunna_skapa_definitioner() {
		assertThrows(AccessDeniedException.class, () -> {
			definitionMatningstypService.save(defaultDefinition.build());
		});
	}
}
