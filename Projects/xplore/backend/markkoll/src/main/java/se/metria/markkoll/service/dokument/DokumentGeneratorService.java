package se.metria.markkoll.service.dokument;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.FastighetsfilterDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.DokumentConverterService;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.service.dokument.bindings.MarkkollBindingsService;
import se.metria.markkoll.service.dokument.bindings.data.MarkkollBindings;
import se.metria.markkoll.service.utskick.UtskickDto;
import se.metria.markkoll.service.utskick.UtskickService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.dokument.dokumentgenerator.DokumentGenerator;
import se.metria.markkoll.util.dokument.DokumentUtil;
import se.metria.markkoll.util.dokument.FileType;

import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DokumentGeneratorService {
    @NonNull
    private final FilService filService;

    @NonNull
    private final DokumentmallService dokumentmallService;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final DokumentGenerator dokumentGenerator;

    @NonNull
    private final DokumentConverterService dokumentConverterService;

    @NonNull
    private final MarkkollBindingsService markkollBindingsService;

    @NonNull
    private final HMSGeneratorService HMSGeneratorService;

    @NonNull
    private final Clock clock;

    @NonNull
    private final UtskickService utskickService;

    public ByteArrayResource
    addDocProperties(Resource resource) throws IOException, Docx4JException {
        log.info("Lägger till DocProperties till dokument {}", resource.getFilename());
        return dokumentGenerator.addDocProperties(resource, MarkkollBindings.class);
    }

    @Transactional
    public Resource getInfobrev(UUID projektId, UUID fastighetId, UUID dokumentmallId)
        throws IOException, Docx4JException, IllegalAccessException
    {
        log.info("Genererar infobrev för fastighet {} i projekt {}", fastighetId, projektId);

        var infobrevsMall = dokumentmallService.getFileData(dokumentmallId);

        var avtalId = avtalRepository.getIdByProjektIdAndFastighetId(projektId, fastighetId);
        var utskick = utskickService.getInfobrevUtskick(avtalId);

        var infobrevsData = markkollBindingsService.getBindings(avtalId, utskick);

        return pdfFromTemplate(infobrevsMall, infobrevsData);
    }

    @Transactional
    public Resource
    getAvtal(UUID avtalId, UUID dokumentmallId, UtskickDto utskick)
        throws IOException, IllegalAccessException, Docx4JException, MarkkollException
    {
        log.info("Genererar avtal för avtal {} med mall {}", avtalId, dokumentmallId);

        var avtalsmall = dokumentmallService.getFileData(dokumentmallId);
        var avtalsdata = markkollBindingsService.getBindings(avtalId, utskick);

        return pdfFromTemplate(avtalsmall, avtalsdata);
    }

    public Resource
    getVarderingSkogsmark(UUID projektId, FastighetsfilterDto fastighetsfilter) throws IOException
    {
        log.info("Påbörjar generering av skogsmarksvärdering för projekt {}", projektId);

        var avtalIds = avtalRepository.avtalIdsFiltered(projektId, fastighetsfilter);
        var xlsx = HMSGeneratorService.getVarderingSkogsmark(avtalIds);

        var projektnamn = projektRepository.getNamn(projektId);
        var filename = DokumentUtil.createFilename(FileType.XLSX, "hms", projektnamn,
            LocalDate.now(clock));

        return new FileNameAwareByteArrayResource(xlsx, filename);
    }

    public Resource getVarderingSkogsmark(UUID avtalId) throws IOException {
        log.info("Genererar skogsmarksvärdering för avtal {}", avtalId);

        var xlsx = HMSGeneratorService.getVarderingSkogsmark(Arrays.asList(avtalId));
        var fastighetsBeteckning = avtalRepository.getFastighetsbeteckning(avtalId);
        var filename = DokumentUtil.createFilename(FileType.XLSX, "hms", fastighetsBeteckning,
            LocalDate.now(clock));

        return new FileNameAwareByteArrayResource(xlsx, filename);
    }

    private Resource
    pdfFromTemplate(Resource resource, Object data) throws IOException, Docx4JException, IllegalAccessException
    {
        var docx = dokumentGenerator.docxFromTemplate(resource, data);
        return dokumentConverterService.convertToPdf(docx);
    }

    private Resource
    pdfFromTemplate(InputStream stream, Object data) throws IOException, Docx4JException, IllegalAccessException
    {
        var docx = dokumentGenerator.docxFromTemplate(stream, data);
        return dokumentConverterService.convertToPdf(docx);
    }
}
