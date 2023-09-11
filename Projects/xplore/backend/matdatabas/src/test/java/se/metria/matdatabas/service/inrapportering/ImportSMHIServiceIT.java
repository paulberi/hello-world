package se.metria.matdatabas.service.inrapportering;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.QueueDispatcher;
import org.apache.commons.io.IOUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.jooq.DSLContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.security.mock.WithMockUserAdministrator;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypConstants;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matningstyp.dto.SaveMatningstyp;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjekt.dto.EditMatobjekt;
import se.metria.matdatabas.service.matobjekt.dto.Matobjekt;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektConflictException;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.SchedulerService;
import se.metria.matdatabas.service.systemlogg.SystemloggService;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;


@DisplayName("Importera SMHI data")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@FlywayTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ImportSMHIServiceIT {
    public static MockWebServer mockBackEnd;
    static String baseUrl;

    @MockBean
    private MatdatabasUser user;

    @MockBean
    private SystemloggService systemloggService;

    @Autowired
    private MatningstypService matningstypService;

    @Autowired
    private MatobjektService matobjektService;

    @SpyBean
    public MatningService matningService;

    @SpyBean
    public SchedulerService schedulerService;

    @Autowired
    private DefinitionMatningstypService definitionMatningstypService;

    @Autowired
    private DSLContext create;

    SaveMatningstyp.SaveMatningstypBuilder matningstypDefault = SaveMatningstyp.builder()
            .matintervallAntalGanger((short) 1)
            .matintervallTidsenhet(MatningstypConstants.TIDSENHET_VECKA)
            .paminnelseDagar((short) 3)
            .aktiv(true);

    private final EditMatobjekt.EditMatobjektBuilder smhiMatobjekt = EditMatobjekt.builder()
            .typ((short) 5)
            .namn("SMHI_stockholm")
            .aktiv(false)
            .kontrollprogram(false)
            .posN(new BigDecimal(654.321))
            .posE(new BigDecimal(456.789))
            .fastighet("fastighet2")
            .lage("läge2");

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();

        QueueDispatcher dispatcher = new QueueDispatcher();
        dispatcher.setFailFast(true);
        mockBackEnd.setDispatcher(dispatcher);

        mockBackEnd.start();

        baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());

    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @WithMockUserAdministrator
    void SMHIImportDygnsnederbörd() throws MatobjektConflictException, IOException, MatningIllegalMatvarde, JobExecutionException, AlreadyGodkandException {
        when(systemloggService.addHandelseImportDone(any(), any())).thenReturn(new SystemloggEntry());

        createDygnsnederbordMatningstyp("SMHI_Stockholm");

        ImportSMHIService smhiService =  new ImportSMHIService(WebClient.builder(),create, matobjektService,
                matningService, matningstypService, systemloggService, baseUrl);

        enqueueBackendJson("/smhi/smhi-api-parameter-5-station-98210-period-latest-day.json");

        // Do real work
        smhiService.importSMHI();

        // validate data imported
        verify(matningService).create(any(),any(),anyBoolean(), eq(StandardKallsystem.SMHImetobs.getNamn()));
        verify(systemloggService).addHandelseImportDone(JobServicetyper.IMPORT_SMHI, 1);
    }

    private Matningstyp createMatningstyp(DefinitionMatningstyp def, Matobjekt matobjekt) {
        var matningstyp = matningstypDefault.build();
        matningstyp.setEnhet(def.getEnhet());
        matningstyp.setDecimaler(def.getDecimaler());
        matningstyp.setGranskasAutomatiskt(false);
        matningstyp.setInstrument("98210");
        return matningstypService.create(matobjekt.getId(), def.getId(), matningstyp);
    }

    @Test
    @WithMockUserAdministrator
    void SMHIImportIngaMatningar() throws MatobjektConflictException, IOException {
        // Given
        when(systemloggService.addHandelseImportDone(any(), any())).thenReturn(new SystemloggEntry());

        createDygnsnederbordMatningstyp("SMHI_Stockholm_2");

        enqueueBackendJson("/smhi/inga-observationer.json");

        ImportSMHIService smhiService =  new ImportSMHIService(WebClient.builder(),create, matobjektService,
                matningService, matningstypService, systemloggService, baseUrl);

        // When
        smhiService.importSMHI();

        // Then
        verify(systemloggService).addHandelseImportDone(JobServicetyper.IMPORT_SMHI, 0);
    }

    private void createDygnsnederbordMatningstyp(String name) throws MatobjektConflictException {
        Matobjekt matobjekt = matobjektService.createMatobjekt(smhiMatobjekt.namn(name).build());
        var dygnsnederbord = definitionMatningstypService.findById(17).orElseThrow();
        createMatningstyp(dygnsnederbord, matobjekt);
    }

    private void enqueueBackendJson(String jsonPath) throws IOException {
        InputStream inputStream = ImportVattenstandServiceIT.class.getResourceAsStream(jsonPath);
        String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .setResponseCode(HttpStatus.OK.value())
                .setBody(json)
        );
    }
}
