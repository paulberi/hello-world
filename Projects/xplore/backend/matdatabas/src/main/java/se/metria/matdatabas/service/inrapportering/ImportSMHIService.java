package se.metria.matdatabas.service.inrapportering;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.metria.matdatabas.service.inrapportering.model.smhi.SmhiData;
import se.metria.matdatabas.service.inrapportering.model.smhi.SmhiObservation;
import se.metria.matdatabas.service.inrapportering.model.smhi.SmhiParameterCode;
import se.metria.matdatabas.service.inrapportering.model.smhi.SmhiPeriodCode;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matobjekt.MatobjektConstants;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.Jobstatus;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.MatobjektTyp.MATOBJEKT_TYP;

@Service
public class ImportSMHIService {
    private WebClient webClient;
    private DSLContext createSql;
    private MatobjektService matobjektService;
    private MatningService matningService;
    private SystemloggService systemloggService;
    private MatningstypService matningstypService;

    private final static String SMHI_RAPPORTOR = "SMHI";

    private final Logger logger = LoggerFactory.getLogger(ImportSMHIService.class);


    public ImportSMHIService(@Qualifier("proxyWebclientBuilder") WebClient.Builder webClientBuilder,
                             DSLContext createSql,
                             MatobjektService matobjektService,
                             MatningService matningService,
                             MatningstypService matningstypService,
                             SystemloggService systemloggService,
                             @Value("${importservice.smhi-metobs.url}") String baseUrl) {


        WebClient.Builder webclientBuilder = webClientBuilder.baseUrl(baseUrl);
        this.webClient = webclientBuilder.build();
        this.createSql = createSql;
        this.matobjektService = matobjektService;
        this.matningService = matningService;
        this.matningstypService = matningstypService;
        this.systemloggService = systemloggService;
    }


    public void importSMHI() {
        String namePattern = "";
        List<String> matobjekt = matobjektService.getMatobjektNamn(namePattern, (int) MatobjektConstants.TYP_VADERSTATION);
        if (matobjekt.isEmpty()) {
            logger.info("SMHI import: Inga väderstationer konfigurerade.");
            return;
        }

        // There are only a few hardcoded import periods in the SMHI API to choose from. In case a bug or something else
        // causes a failed import, it becomes easier to fix if we just import everything from the latest month.
        int importedCount = updateDataFromSmhi("Väderstation","Dygnsnederbörd", SmhiParameterCode.DYGNSNEDERBÖRD, SmhiPeriodCode.LATEST_MONTHS);
        importedCount += updateDataFromSmhi("Väderstation","Temperatur", SmhiParameterCode.MOMENTAN_TEMPERATUR, SmhiPeriodCode.LATEST_MONTHS);
        systemloggService.addHandelseImportDone(JobServicetyper.IMPORT_SMHI, importedCount);
    }

    private int updateDataFromSmhi(String matobjektTypNamn, String definitionMatningstypNamn, SmhiParameterCode smhiParameterCode, SmhiPeriodCode smhiPeriodCode) {
        var toFetch = createSql.select(MATNINGSTYP.ID, MATNINGSTYP.INSTRUMENT)
                .from(MATOBJEKT_TYP)
                .innerJoin(DEFINITION_MATNINGSTYP).on(MATOBJEKT_TYP.ID.eq(DEFINITION_MATNINGSTYP.MATOBJEKT_TYP))
                .innerJoin(MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
                .where(MATOBJEKT_TYP.NAMN.eq(matobjektTypNamn)
                        .and(DEFINITION_MATNINGSTYP.NAMN.eq(definitionMatningstypNamn))
                        .and(MATNINGSTYP.AKTIV)
                        .and(MATNINGSTYP.INSTRUMENT.isNotNull())
                )
                .fetch();

        var importedCount = 0;
        for (Record toFetchRecord : toFetch) {
            Integer matningstypId = toFetchRecord.get(MATNINGSTYP.ID);
            String station = toFetchRecord.get(MATNINGSTYP.INSTRUMENT);

            Matningstyp matningstyp = matningstypService.findById(matningstypId).get();

            SmhiObservation observation;

            try {
                observation = fetchData(smhiParameterCode.getValue(), station, smhiPeriodCode.getValue());
            } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
                logger.error("SMHI import: Http error: " + e.getStatusText() + " Parameter: "+smhiParameterCode+" Station: "+station);

                continue;
            }

            for (SmhiData data : observation.getValue()) {
                LocalDateTime avlastDatum;

                if (smhiParameterCode==SmhiParameterCode.DYGNSNEDERBÖRD) {
                    avlastDatum = LocalDateTime.parse(data.getRef()+"T18:00", ISO_LOCAL_DATE_TIME);
                } else {
                    Instant date = Instant.ofEpochMilli(data.getDate());
                    avlastDatum = LocalDateTime.ofInstant(date, ZoneId.systemDefault());
                }

                SaveMatning saveMatning = SaveMatning.builder()
                        .avlastVarde(data.getValue())
                        .avlastDatum(avlastDatum)
                        .rapportor(SMHI_RAPPORTOR)
                        .build();

                if ("Y".equalsIgnoreCase(data.getQuality())) {
                    saveMatning.setKommentar("Ej kontrollerat av meteorologisk mätdata");
                }


                try {
                    matningService.create(matningstyp, saveMatning, true, StandardKallsystem.SMHImetobs.getNamn());
                    importedCount++;
                } catch (MatningIllegalMatvarde matningIllegalMatvarde) {
                    logger.warn("SMHI import: IllegalMatvarde Parameter: "+smhiParameterCode+" Station: "+station+" Value: "+data.getValue());
                } catch (AlreadyGodkandException e) {
                    logger.warn("SMHI import: AlreadyGodkandException Parameter: "+smhiParameterCode+" Station: "+station+" Value: "+data.getValue());
                }
            }
        }

        return importedCount;
    }

    public SmhiObservation fetchData(String parameterCode, String stationCode, String periodCode) {
        var observation = webClient
                .get()
                .uri("/api/version/1.0/parameter/{parameterCode}/station/{stationCode}/period/{periodCode}/data.json"
                        , parameterCode, stationCode, periodCode)
                .accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(SmhiObservation.class)
                .block();

        // No observations were found. Safer and more proper to replace the null value with an empty collection.
        if (observation.getValue() == null) {
            observation.setValue(new ArrayList<>());
        }

        return observation;
    }
}
