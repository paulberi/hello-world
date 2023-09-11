package se.metria.matdatabas.service.larm;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.larm.query.LarmSearchFilter;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Givet larm tjänst")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class LarmServiceIT {

	@Autowired
	private LarmnivaRepository repository;

	@Autowired
	private LarmService service;

	@MockBean
	private MatdatabasUser user;

	@BeforeAll
	static void setup(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_matningstyper.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_gransvarden.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_matning.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_create_larm.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_larm_datum.sql"));
		}
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void möjligt_att_hämta_ut_paginerad_larmlista() {
		LarmSearchFilter larmSearchFilter = LarmSearchFilter.builder().build();
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder().build();
		var page = service.getLarmListPage(larmSearchFilter, matningstypSearchFilter, PageRequest.of(0,12));
		assertEquals(3, page.getTotalPages());
		assertEquals(29, page.getTotalElements());
		assertEquals(0, page.getNumber());
		assertEquals(12, page.getContent().size());

		larmSearchFilter.setLarmStatus(Larmstatus.LARM);
		page = service.getLarmListPage(larmSearchFilter, matningstypSearchFilter, PageRequest.of(0,12));
		assertEquals(2, page.getTotalPages());
		assertEquals(17, page.getTotalElements());
		assertEquals(0, page.getNumber());
		assertEquals(12, page.getContent().size());

		larmSearchFilter.setLarmStatus(null);
		matningstypSearchFilter.setMatobjektNamn("Mätobjekt3");
		page = service.getLarmListPage(larmSearchFilter, matningstypSearchFilter, PageRequest.of(0,12));
		assertEquals(1, page.getTotalPages());
		assertEquals(4, page.getTotalElements());
		assertEquals(0, page.getNumber());
		assertEquals(4, page.getContent().size());
	}

	@Test
	@WithMockUserTillstandshandlaggare
	void en_tillståndshandläggare_ska_kunna_kvittera_larm() {
		List<Long> larmIds = new ArrayList<>();
		for (long i = 1; i < 9; i++) {
			larmIds.add(i);
		}
		LarmSearchFilter larmSearchFilter = LarmSearchFilter.builder().build();
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder().build();
		larmSearchFilter.setLarmStatus(Larmstatus.LARM);
		var larmCountBefore = service.getLarmList(larmSearchFilter, matningstypSearchFilter).size();
		service.kvittera(larmIds, user.getId());
		var larmCountAfter = service.getLarmList(larmSearchFilter, matningstypSearchFilter).size();
		assertEquals(larmCountBefore - larmIds.size(), larmCountAfter);

	}

	@Test
	@WithMockUserTillstandshandlaggare
	void filtrera_mätningar_mellan_datum() {
		// Given
		var includeIds = new ArrayList<Integer>();
		includeIds.add(1);

		var matningstypSearchFilter = MatningstypSearchFilter
				.builder()
				.includeIds(includeIds)
				.matningFromDatum(LocalDateTime.parse("2020-01-02T00:00:00"))
				.matningToDatum(LocalDateTime.parse("2020-01-05T00:00:00"))
				.build();
		var larmSearchFilter = LarmSearchFilter
				.builder()
				.larmStatus(Larmstatus.LARM)
				.build();

		// When
		var larmList = service.getLarmList(larmSearchFilter, matningstypSearchFilter);

		// Then
		assertEquals(3, larmList.size());
	}
}
