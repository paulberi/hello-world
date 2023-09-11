package se.metria.matdatabas.service.rapport;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.apache.commons.collections.CollectionUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserTillstandshandlaggare;
import se.metria.matdatabas.service.rapport.dto.RapportGrafSettings;
import se.metria.matdatabas.service.rapport.dto.RapportMottagare;
import se.metria.matdatabas.service.rapport.dto.RapportSettings;
import se.metria.matdatabas.service.rapport.exception.RapportSettingsErrorException;
import se.metria.matdatabas.service.systemlogg.SystemloggService;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Givet rapportservice tjänst")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RapportServiceIT {
    @MockBean
    private MatdatabasUser user;

    @Autowired
    private RapportService rapportService;

    @MockBean
    private SystemloggService systemloggService;

    @Autowired
    private RapportRepository rapportRepository;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("data/test_rapporter.sql"));
        }
    }

    @AfterEach
    public void tearDown() {
        rapportRepository.deleteAll();
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void när_ny_rapport_skapas_skall_den_sparas_korrekt() throws Exception {
        when(systemloggService.addHandelseRapportCreated(any(), any())).thenReturn(new SystemloggEntry());

        final var rapport = rapportSettings1().build();

        final var created = rapportService.createRapportSettings(rapport);

        assertNotNull(created.getId());
        assertNotNull(created.getAndradAvId());
        assertNotNull(created.getAndradDatum());
        assertNotNull(created.getSkapadDatum());
        assertRapportequals(rapport, created);

        verify(systemloggService).addHandelseRapportCreated(created.getId(), created.getNamn());
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void det_ska_inte_gå_att_skapa_två_rapporter_med_samma_namn() throws Exception {
        final var rapport1 = rapportSettings1().build();
        final var rapport2 = rapportSettings2().namn(rapport1.getNamn()).build();

        rapportService.createRapportSettings(rapport1);

        assertThrows(RapportSettingsErrorException.class, () -> {
            rapportService.createRapportSettings(rapport2);
        });
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void det_ska_inte_gå_att_skapa_rapporter_med_identiska_rapportmottagare() throws Exception {
        final var mottagare = Arrays.asList(new RapportMottagare[]{rapportMottagare1().build(), rapportMottagare1().build()});
        final var rapport = rapportSettings1().rapportMottagare(mottagare).build();

        assertThrows(RapportSettingsErrorException.class, () -> {
            rapportService.createRapportSettings(rapport);
        });
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void det_ska_inte_gå_att_skapa_rapporter_som_saknar_grafer() throws Exception {
        final var rapport = rapportSettings1().rapportGraf(new ArrayList<>()).build();

        assertThrows(RapportSettingsErrorException.class, () -> {
            rapportService.createRapportSettings(rapport);
        });
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void det_ska_inte_gå_att_skapa_rapporter_där_grafer_saknar_rubriker() throws Exception {
        final var graf1 = Arrays.asList(new RapportGrafSettings[] { rapportGraf1().rubrik(null).build() });
        final var rapport1 = rapportSettings1().rapportGraf(graf1).build();
        assertThrows(RapportSettingsErrorException.class, () -> {
            rapportService.createRapportSettings(rapport1);
        });

        final var graf2 = Arrays.asList(new RapportGrafSettings[] { rapportGraf1().rubrik("").build() });
        final var rapport2 = rapportSettings1().rapportGraf(graf2).build();
        assertThrows(RapportSettingsErrorException.class, () -> {
            rapportService.createRapportSettings(rapport2);
        });
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void det_ska_inte_gå_att_skapa_rapporter_där_grafer_saknar_mätvärden() throws Exception {
        final var graf = Arrays.asList(new RapportGrafSettings[] {
            rapportGraf1().matningstyper(new ArrayList<>())
                          .gransvarden(new ArrayList<>())
                          .build()
        });
        final var rapport = rapportSettings1().rapportGraf(graf).build();
        assertThrows(RapportSettingsErrorException.class, () -> {
            rapportService.createRapportSettings(rapport);
        });
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void när_ny_rapport_uppdateras_skall_den_sparas_korrekt() throws Exception {
        when(systemloggService.addHandelseRapportModified(any(), any())).thenReturn(new SystemloggEntry());

        final var newRapport = rapportSettings1().build();
        final var created = rapportService.createRapportSettings(newRapport);

        final var updatedRapport = rapportSettings2().build();
        final var updated = rapportService.updateRapportSettings(created.getId(), updatedRapport);

        assertNotNull(updated.getId());
        assertNotNull(updated.getAndradAvId());
        assertNotNull(updated.getAndradDatum());
        assertNotNull(updated.getSkapadDatum());
        assertRapportequals(updatedRapport, updated);

        verify(systemloggService).addHandelseRapportModified(updated.getId(), updated.getNamn());
    }

    @Test
    @WithMockUserTillstandshandlaggare
    void det_ska_gå_att_radera_rapporter() throws Exception {
        when(systemloggService.addHandelseRapportRemoved(any(), any())).thenReturn(new SystemloggEntry());

        final var rapport = rapportSettings1().build();
        final var created = rapportService.createRapportSettings(rapport);

        assertTrue(rapportService.rapportNamnExists(created.getNamn()));

        assertTrue(rapportService.deleteRapportSettings(created.getId()));

        verify(systemloggService).addHandelseRapportRemoved(created.getId(), created.getNamn());
    }

    void assertRapportequals(RapportSettings rapport, RapportSettings saved) {
        assertEquals(rapport.getAktiv(), saved.getAktiv());
        assertEquals(rapport.getMejlmeddelande(), saved.getMejlmeddelande());
        assertEquals(rapport.getDataperiodFrom(), saved.getDataperiodFrom());
        assertEquals(rapport.getBeskrivning(), saved.getBeskrivning());
        assertEquals(rapport.getInledningInformation(), saved.getInledningInformation());
        assertEquals(rapport.getInledningRubrik(), saved.getInledningRubrik());
        assertEquals(rapport.getLagesbild(), saved.getLagesbild());
        assertEquals(rapport.getNamn(), saved.getNamn());
        assertEquals(rapport.getTidsintervall(), saved.getTidsintervall());

        assertMottagareEquals(rapport.getRapportMottagare(), saved.getRapportMottagare());
        assertGraferEquals(rapport.getRapportGraf(), saved.getRapportGraf());
    }

    void assertMottagareEquals(List<RapportMottagare> mottagare, List<RapportMottagare> saved) {
        assertEquals(mottagare.size(), saved.size());
        final var mottagareSet = new HashSet<>(mottagare);
        final var savedSet = new HashSet<>(saved);

        // The elements in these lists could be the same, but be ordered differently.
        assertTrue(CollectionUtils.isEqualCollection(mottagareSet, savedSet));
    }

    void assertGraferEquals(List<RapportGrafSettings> grafer, List<RapportGrafSettings> saved) {
        assertEquals(grafer.size(), saved.size());
        for (int i = 0; i < grafer.size(); i++) {
            final var g = grafer.get(i);
            final var s = saved.get(i);

            assertNotNull(s.getId());

            assertEquals(g.getInfo(), s.getInfo());
            assertEquals(g.getRubrik(), s.getRubrik());

            // The elements in these lists could be the same, but be ordered differently.
            assertTrue(CollectionUtils.isEqualCollection(
                g.getGransvarden().stream().collect(Collectors.toSet()),
                s.getGransvarden().stream().collect(Collectors.toSet()))
            );

            assertTrue(CollectionUtils.isEqualCollection(
                g.getMatningstyper().stream().collect(Collectors.toSet()),
                s.getMatningstyper().stream().collect(Collectors.toSet()))
            );
        }
    }

    RapportSettings.RapportSettingsBuilder rapportSettings1() {
        final var grafer = Arrays.asList(new RapportGrafSettings[]{rapportGraf1().build(), rapportGraf2().build()});
        final var mottagare = Arrays.asList(new RapportMottagare[]{rapportMottagare1().build()});

        return RapportSettings.builder()
                .namn("testrapport")
                .aktiv(true)
                .mejlmeddelande("Ett meddelande")
                .beskrivning("skapad rapport")
                .dataperiodFrom(RapportConstants.MONTHS_6)
                .lagesbild("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee")
                .rorelsereferensdatum(LocalDateTime.parse("2016-05-27T11:52"))
                .tidsintervall(Tidsintervall.MANADSVIS)
                .startDatum(LocalDateTime.parse("2016-05-29T12:34"))
                .inledningRubrik("rubrik")
                .inledningInformation("och information")
                .senastSkickad(LocalDateTime.parse("2020-06-29T00:00"))
                .rapportMottagare(mottagare)
                .rapportGraf(grafer);
    }

    RapportSettings.RapportSettingsBuilder rapportSettings2() {
        final var mottagare = Arrays.asList(new RapportMottagare[]{rapportMottagare1().build(), rapportMottagare2().build()});
        final var grafer = Arrays.asList(new RapportGrafSettings[]{rapportGraf2().build(), rapportGraf3().build()});

        return RapportSettings.builder()
                              .namn("uppdaterad testrapport")
                              .aktiv(false)
                              .mejlmeddelande("ett nytt meddelande")
                              .beskrivning("en rapport som är uppdaterad")
                              .dataperiodFrom(RapportConstants.YEARS_10)
                              .lagesbild("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee")
                              .rorelsereferensdatum(LocalDateTime.parse("2018-05-27T13:00"))
                              .tidsintervall(Tidsintervall.ARSVIS)
                              .startDatum(LocalDateTime.parse("2020-01-01T00:00"))
                              .inledningRubrik("ny rubrik")
                              .inledningInformation("ny information")
                              .senastSkickad(LocalDateTime.parse("2021-07-30T00:00"))
                              .rapportMottagare(mottagare)
                              .rapportGraf(grafer);
    }

    RapportMottagare.RapportMottagareBuilder rapportMottagare1() {
        return RapportMottagare.builder()
                               .namn("Anonym")
                               .epost("test@email.se");
    }

    RapportMottagare.RapportMottagareBuilder rapportMottagare2() {
        return RapportMottagare.builder()
                               .namn("anvandare")
                               .epost("anvandare@metria.se");
    }

    RapportGrafSettings.RapportGrafSettingsBuilder rapportGraf1() {
        return RapportGrafSettings.builder()
                                   .rubrik("Min graf")
                                   .info("med beskrivning")
                                   .matningstyper(Arrays.asList(new Integer[]{2350, 2343}))
                                   .gransvarden(new ArrayList<>());
    }

    RapportGrafSettings.RapportGrafSettingsBuilder rapportGraf2() {
        return RapportGrafSettings.builder()
                                   .rubrik("Min andra graf")
                                   .info("även den med beskrivning")
                                   .matningstyper(Arrays.asList(new Integer[]{2343}))
                                   .gransvarden(Arrays.asList(new Integer[]{303}));
    }

    RapportGrafSettings.RapportGrafSettingsBuilder rapportGraf3() {
        return RapportGrafSettings.builder()
                                   .rubrik("Min tredje graf")
                                   .info("ytterst utförlig beskrivning")
                                   .matningstyper(Arrays.asList(new Integer[]{2350}))
                                   .gransvarden(Arrays.asList(new Integer[]{303}));
    }
}
