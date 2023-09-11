package se.metria.matdatabas.service.inrapportering;

import org.apache.commons.csv.CSVFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import se.metria.matdatabas.common.ProxyProperties;
import se.metria.matdatabas.service.inrapportering.exception.MatningstypNotFoundException;
import se.metria.matdatabas.service.inrapportering.exception.MatningstypInvalidException;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.Jobstatus;
import se.metria.matdatabas.service.scheduler.SchedulerService;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static se.metria.matdatabas.service.inrapportering.ImportFel.*;
import static se.metria.matdatabas.service.matobjekt.MatobjektService.RH00_TO_RH2000;


@Service
public class ImportVattenstandService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String RAPPORTOR = "Stockholms Hamnar";

    private final static String INSTRUMENT_MALAREN = "LevelMala_Avg";
    private final static String INSTRUMENT_SALTSJON = "LevelSalt_Avg";

    /* Below is used to transform from RH00 to RH2000 */
    private final static Double MALAREN_REF = 0.26;
    private final static Double SALTSJON_REF = -0.40;


    private MatningstypService matningstypService;
    private MatningService matningService;
    private SystemloggService systemloggService;
    private SchedulerService schedulerService;
    private ProxyProperties proxyProperties;

    @Value("${importservice.stockholmnshamnar.url}")
    private String fileUrl;

    public ImportVattenstandService(MatningstypService matningstypService,
                                    MatningService matningService,
                                    SystemloggService systemloggService,
                                    SchedulerService schedulerService,
                                    ProxyProperties proxyProperties) {
        this.matningstypService = matningstypService;
        this.matningService = matningService;
        this.systemloggService = systemloggService;
        this.schedulerService = schedulerService;
        this.proxyProperties = proxyProperties;
    }

    public Short importVattenstand(LocalDateTime fromDate, LocalDateTime toDate) {
        Short status = Jobstatus.FAILED;

        List<Matningstyp> matningstyper = new ArrayList<Matningstyp>();
        matningstyper.addAll(matningstypService.findByInstrument(INSTRUMENT_MALAREN));
        matningstyper.addAll(matningstypService.findByInstrument(INSTRUMENT_SALTSJON));
        if (matningstyper.isEmpty()) {
            logger.info("Vattenstånd import: Inga mätningstyper konfigurerade.");
            return Jobstatus.ABORTED;
        }

        BufferedInputStream in = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            String from = fromDate.format(formatter);
            String to = toDate.format(formatter);
            String url = getFileUrl(from, to, fileUrl);

            if (StringUtils.isNotEmpty(proxyProperties.getHost())) {
                SocketAddress addr = new
                        InetSocketAddress(proxyProperties.getHost(), proxyProperties.getPort());
                Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

                URLConnection urlConnection = new URL(url).openConnection(proxy);

                in = new BufferedInputStream(urlConnection.getInputStream());
            } else {
                in = new BufferedInputStream(new URL(url).openStream());
            }

            List<Matning> matningar = parseAndSavematningar(in);
            systemloggService.addHandelseImportDone(JobServicetyper.IMPORT_VATTENSTAND, matningar.size());
            status = Jobstatus.OK;
        } catch (MatningstypNotFoundException e) {
            systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_VATTENSTAND, IMPORT_FEL_MATNINGSTYP);
            logger.error("Vattenstånd import: Ingen mätningstyp uppsatt", e);
        } catch (MatningstypInvalidException e) {
            systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_VATTENSTAND, IMPORT_FEL_MATNINGSTYP);
            logger.error("Vattenstånd import: Felaktig mätningstyp ", e);
        } catch (IOException e) {
            systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_VATTENSTAND, IMPORT_FEL_INLASNING);
            logger.error("Vattenstånd import:", e);
        } catch (NumberFormatException e) {
            systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_VATTENSTAND, IMPORT_FEL_FORMAT);
            logger.error("Vattenstånd import: Fel format", e);
        }

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                // ignore if close failed
            }
        }
        return status;
    }

    public List<Matning> parseAndSavematningar(InputStream csv) throws MatningstypNotFoundException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        LocalDateTime date;
        Double varde;

        var csvRecords = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withTrim()
                .withCommentMarker('#')
                .parse(new InputStreamReader(csv, StandardCharsets.UTF_8.name()))
                .getRecords();
        var saveMatningarMalaren = new ArrayList<SaveMatning>();
        var saveMatningarSaltsjon = new ArrayList<SaveMatning>();

        for (var csvRecord : csvRecords) {
            if (csvRecord.size() >= 4) {
                logger.warn(csvRecord.toString());
                date = LocalDateTime.parse(csvRecord.get(0) + csvRecord.get(1), formatter);

                String malaren = csvRecord.get(2);
                if (malaren != null && !malaren.trim().isEmpty()) {
                    varde = Double.parseDouble(malaren) / 100 + RH00_TO_RH2000 + MALAREN_REF;
                    saveMatningarMalaren.add(addMatning(varde, date));
                }

                String saltsjon = csvRecord.get(3);
                if (saltsjon != null && !saltsjon.trim().isEmpty()) {
                    varde = Double.parseDouble(saltsjon) / 100 + RH00_TO_RH2000 + SALTSJON_REF;
                    saveMatningarSaltsjon.add(addMatning(varde, date));
                }
            }
        }

        List<Matning> matningarMalaren = saveMatningForInstrument(saveMatningarMalaren, INSTRUMENT_MALAREN);
        List<Matning> matningarSaltsjon = saveMatningForInstrument(saveMatningarSaltsjon, INSTRUMENT_SALTSJON);

        List<Matning> matningar = new ArrayList<Matning>();
        matningar.addAll(matningarMalaren);
        matningar.addAll(matningarSaltsjon);

        return matningar;
    }

    private SaveMatning addMatning(Double varde, LocalDateTime date) {
        SaveMatning saveMatning = SaveMatning.builder()
                .avlastVarde(varde)
                .avlastDatum(date)
                .rapportor(RAPPORTOR)
                .build();
        return saveMatning;
    }

    private List<Matning> saveMatningForInstrument(List<SaveMatning> matningar, String instrument) throws MatningstypNotFoundException, MatningstypInvalidException {
        List<Matningstyp> matningstyper = matningstypService.findByInstrument(instrument);
        List<Matning> imports = new ArrayList<>();

        if (matningstyper.size() == 0) {
            logger.warn("Vattenstånd import: Ingen mätningstyp för instrument: " + instrument);
            throw new MatningstypNotFoundException();
        } else if (matningstyper.size() > 1) {
            logger.warn("Vattenstånd import: För många mätningstyper för instrument: " + instrument);
            throw new MatningstypInvalidException();
        }

        Matningstyp matningstyp = matningstyper.get(0);

        imports.addAll(matningar.stream().map(saveMatning -> {
            try {
                return matningService.create(matningstyp, saveMatning, true, StandardKallsystem.StockholmsHamnar.getNamn()).getMatning();
            } catch (MatningIllegalMatvarde matningIllegalMatvarde) {
                logger.warn("Vattenstånd import: IllegalMatvarde Mätningstyp: " + matningstyp.getId() + " Value: " + saveMatning.getAvlastVarde());
                return null;
            } catch (AlreadyGodkandException ex) {
                logger.warn("Vattenstånd import: AlreadyGodkand Previous: " + ex.getMatningPrevious().toString() +
                        " Current: " + ex.getMatningCurrent().toString());
                return null;
            }
        }).collect(Collectors.toList()));

        return imports.stream().filter(matning -> matning != null).collect(Collectors.toList());
    }

    private String getFileUrl(String from, String to, String fileUrl) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(fileUrl);
        uriComponentsBuilder.queryParam("macro", "www/SH2//SH2_LevelMalaSalt.ic");
        uriComponentsBuilder.queryParam("from", from);
        uriComponentsBuilder.queryParam("to", to);
        uriComponentsBuilder.queryParam("path", "/usr/airviro/data/sthlmshamnar/");
        uriComponentsBuilder.queryParam("lang", "swe");
        uriComponentsBuilder.queryParam("macropath", "");
        uriComponentsBuilder.queryParam("outtype", "txt");
        return uriComponentsBuilder.build().toUriString();
    }
}
