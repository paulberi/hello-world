package se.metria.matdatabas;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.openapi.model.SaveKartlagerDto;
import se.metria.matdatabas.openapi.model.SaveKartlagerfilDto;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.service.kartlager.KartlagerService;
import se.metria.matdatabas.service.kartlager.command.SaveKartlager;
import se.metria.matdatabas.service.kartlager.command.SaveKartlagerfil;
import se.metria.matdatabas.service.kartlager.dto.Kartlager;
import se.metria.matdatabas.service.kartlager.dto.Kartlagerfil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@AutoConfigureMockMvc
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class KartlagerIT {
	@Autowired
	private KartlagerService service;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	MatdatabasUser user;

	private UUID kartlagerfilId1 = UUID.fromString("1d578de7-be29-40be-8669-cc4469a41d8e");

	@BeforeAll
	static void setup(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/kartlager.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/kartlager_fil.sql"));
		}
	}

	@Test
	@WithMockUserAdministrator
	@Transactional
	void getAllKartlager() {
		assertNotNull(service);
		assertEquals(5, service.findAll().size());

		Optional<Kartlager> akalla = service.findAll().stream()
				.filter(kartlager -> kartlager.getNamn().equals("Tunnelbana Akalla-Barkarby"))
				.findFirst();
		assertTrue(akalla.isPresent());
		// Kartlagerfiler is skipped when fetching a collection.
		assertNull(akalla.get().getKartlagerfiler());
	}

	@Test
	@WithMockUserAdministrator
	@Transactional
	void getKartlager() {
		Optional<Kartlager> akalla = service.findById(3);

		assertTrue(akalla.isPresent());
		// Kartlagerfiler is not skipped when fetching a single object.
		assertEquals(3, akalla.get().getKartlagerfiler().size());
		for (Kartlagerfil f : akalla.get().getKartlagerfiler()) {
			assertEquals("Anläggning", f.getStil());
		}
	}

	/**
	 * Try reversing the list of kartlager.
	 */
	@Test
	@WithMockUserAdministrator
	@Transactional
	void reverseOrder() {
		List<Kartlager> kartlager = service.findAll();
		List<Integer> kartlagerIds = kartlager.stream()
				.filter(Kartlager::getAndringsbar)
				.map(Kartlager::getId)
				.collect(Collectors.toList());

		Collections.reverse(kartlagerIds);
		service.changeOrder(kartlagerIds);

		List<Kartlager> kartlagerReordered = service.findAll();
		List<Integer> kartlagerIdsReordered = kartlagerReordered.stream()
				.filter(Kartlager::getAndringsbar)
				.map(Kartlager::getId)
				.collect(Collectors.toList());

		assertEquals(kartlagerIds, kartlagerIdsReordered);
	}

	@Test
	@WithMockUserAdministrator
	@Transactional
	void saveKartlager() {
		var filBuilder = SaveKartlagerfil.builder()
				.filnamn("test.geojson")
				.fil("{\"type\": \"FeatureCollection\",\"features\": []}")
				.stil("Anläggning");

		SaveKartlager högdalsdepån = SaveKartlager.builder()
				.namn("Högdalsdepån")
				.grupp("Projekt")
				.beskrivning("Projekt för Högdalsdepån")
				.visa(true)
				.kartlagerfil(filBuilder.build())
				.kartlagerfil(filBuilder.build())
				.kartlagerfil(filBuilder.build())
				.build();

		Kartlager saved = service.save(högdalsdepån);
		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(3, saved.getKartlagerfiler().size());
		assertTrue(saved.getKartlagerfiler().stream().allMatch(fil -> fil.getId() != null && fil.getFilnamn() != null));

		// Try updating the kartlagerfiler collection.
		Kartlagerfil retained = saved.getKartlagerfiler().iterator().next();

		SaveKartlager högdalsdepånUpdate = SaveKartlager.builder()
				.namn("Högdalsdepån, uppdaterad")
				.grupp("Projekt, uppdaterad")
				.beskrivning("Projekt för Högdalsdepån, uppdaterad")
				.visa(true)
				// Add one new kartlagerfil and keep one of the old ones.
				.kartlagerfil(filBuilder.build())
				.kartlagerfil(SaveKartlagerfil.builder().id(retained.getId()).stil(retained.getStil()).build())
				.build();

		Kartlager updated = service.update(saved.getId(), högdalsdepånUpdate);

		assertNotNull(updated);
		assertNotNull(updated.getId());
		assertEquals(2, updated.getKartlagerfiler().size());
		assertTrue(updated.getKartlagerfiler().stream().allMatch(fil -> fil.getId() != null && fil.getFilnamn() != null));
		assertEquals(saved.getId(), updated.getId());
		assertEquals(updated.getNamn(), "Högdalsdepån, uppdaterad");
		assertEquals(updated.getGrupp(), "Projekt, uppdaterad");
		assertEquals(updated.getBeskrivning(), "Projekt för Högdalsdepån, uppdaterad");

		service.delete(updated.getId());

		assertTrue(service.findById(updated.getId()).isEmpty());
	}

	@Test
	@WithMockUserMatrapportor
	@Transactional
	void getKartlagerfil() {
		String kartlagerfil = service.getKartlagerfilData(kartlagerfilId1);
		assertNotNull(kartlagerfil);
		assertFalse(kartlagerfil.isEmpty());
	}

	@Test
	@WithMockUserMatrapportor
	void getStyles() {
		List<String> styles = service.getStyles();
		assertEquals(5, styles.size());
	}

	// REST API tests
	@Test
	@WithMockUserMatrapportor
	void restGetKartlagerCollection() throws Exception {
		mockMvc.perform(get("/api/kartlager")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(5)))
				// Note, the results are ordered by "ordning" descending
				.andExpect(jsonPath("$[3].ordning", equalTo(1)))
				.andExpect(jsonPath("$[3].namn", equalTo("Tunnelbana Akalla-Barkarby")))
				.andExpect(jsonPath("$[3].grupp", equalTo("Projekt")))
				.andExpect(jsonPath("$[3].kartlagerfiler", equalTo(null)));
	}

	@Test
	@WithMockUserAdministrator
	@Transactional
	void restPostKartlagerCollection() throws Exception {
		SaveKartlagerDto kartlager = new SaveKartlagerDto()
				.namn("Nytt kartlager")
				.grupp("Ny grupp")
				.beskrivning("Beskrivning")
				.visa(true)
				.addKartlagerfilerItem(new SaveKartlagerfilDto()
						.filnamn("lager.geojson")
						.fil("{\"type\": \"FeatureCollection\",\"features\": []}")
						.stil("Anläggning"));

		mockMvc.perform(post("/api/kartlager")
				.content(mapper.writeValueAsString(kartlager))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUserMatrapportor
	@Transactional
	void restPostKartlagerCollection_forbidden() throws Exception {
		mockMvc.perform(post("/api/kartlager")
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserMatrapportor
	void restGetKartlager() throws Exception {
		mockMvc.perform(get("/api/kartlager/3")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("ordning", equalTo(1)))
				.andExpect(jsonPath("namn", equalTo("Tunnelbana Akalla-Barkarby")))
				.andExpect(jsonPath("grupp", equalTo("Projekt")))
				.andExpect(jsonPath("kartlagerfiler", hasSize(3)));
	}

	@Test
	@Transactional
	@WithMockUserAdministrator
	void restPutKartlager() throws Exception {
		SaveKartlagerDto kartlager = new SaveKartlagerDto()
				.namn("Nytt namn")
				.grupp("Ny grupp")
				.beskrivning("Ny beskrivning")
				.visa(true)
				.addKartlagerfilerItem(new SaveKartlagerfilDto()
						.filnamn("lager.geojson")
						.fil("{\"type\": \"FeatureCollection\",\"features\": []}")
						.stil("Anläggning"));

		mockMvc.perform(put("/api/kartlager/3")
				.content(mapper.writeValueAsString(kartlager))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	@Transactional
	@WithMockUserMatrapportor
	void restPutKartlager_forbidden() throws Exception {
		mockMvc.perform(put("/api/kartlager/3")
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	@WithMockUserAdministrator
	void restDeleteKartlager() throws Exception {
		mockMvc.perform(delete("/api/kartlager/3")
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@Transactional
	@WithMockUserMatrapportor
	void restDeleteKartlager_forbidden() throws Exception {
		mockMvc.perform(delete("/api/kartlager/3")
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserAdministrator
	@Transactional
	void restChangeOrder() throws Exception {
		mockMvc.perform(post("/api/kartlager/order")
				.content("[5,4,3,2,1]")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUserMatrapportor
	@Transactional
	void restChangeOrder_forbidden() throws Exception {
		mockMvc.perform(post("/api/kartlager/order")
				.content("[5,4,3,2,1]")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUserMatrapportor
	void restStilar() throws Exception {
		mockMvc.perform(get("/api/kartlager/stilar")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	@WithMockUserMatrapportor
	void restGetKartlagerfil() throws Exception {
		mockMvc.perform(get("/api/kartlager/filer/" + kartlagerfilId1.toString())
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("type", equalTo("FeatureCollection")));
	}

	@Test
	@WithMockUserMatrapportor
	void restTree() throws Exception {
		mockMvc.perform(get("/api/kartlager/tree")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}
}
