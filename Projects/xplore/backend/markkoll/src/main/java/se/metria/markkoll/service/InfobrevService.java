package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.InfobrevsjobbEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;
import se.metria.markkoll.openapi.model.DokumentTypDto;
import se.metria.markkoll.openapi.model.FilterAndTemplateDto;
import se.metria.markkoll.openapi.model.InfobrevsjobbProgressDto;
import se.metria.markkoll.repository.infobrev.InfobrevsjobbRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.dokument.DokumentmallService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.dokument.DokumentUtil;
import se.metria.markkoll.util.dokument.FileType;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static se.metria.markkoll.util.CollectionUtil.modelMapperList;

@Service
@Slf4j
@RequiredArgsConstructor
public class InfobrevService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final CachingService cachingService;

    @NonNull
    private final FastighetService fastighetService;

    @NonNull
    private final DokumentGeneratorService dokumentGeneratorService;

    @NonNull
    private final InfobrevsjobbRepository infobrevsjobbRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final LoggService loggService;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final DokumentmallService dokumentmallService;

    public void cancelInfobrevsjobb(UUID infobrevsjobbId) {
        infobrevsjobbRepository.setInfobrevsjobbStatus(infobrevsjobbId, AvtalsjobbStatusDto.CANCELLED);
    }

    @Transactional
    public UUID createInfobrevsjobb(UUID projektId, FilterAndTemplateDto filterAndTemplate) {
        var fastigheter = fastighetService.getFastigheterAndSamfalligheter(projektId, filterAndTemplate.getFilter());

        var projektnamn = projektRepository.getNamn(projektId);
        var mallnamn = dokumentmallService.get(filterAndTemplate.getTemplate()).getNamn();

        var infobrevsjobbEntity = InfobrevsjobbEntity.builder()
                .status(AvtalsjobbStatusDto.NONE)
                .fastigheter(modelMapperList(fastigheter, modelMapper, FastighetEntity.class))
                .generated(0)
                .total(fastigheter.size())
                .projektId(projektId)
                .filnamn(DokumentUtil.createFilename(FileType.ZIP, projektnamn, mallnamn, LocalDate.now()))
                .build();

        var savedEntity = infobrevsjobbRepository.saveAndFlush(infobrevsjobbEntity);
        aclService.createObject(savedEntity.getId(), InfobrevsjobbEntity.class, projektId, ProjektEntity.class);

        log.info("Infobrevsjobb {} skapat för projekt: {} med mall: {}", savedEntity.getId(), projektId, filterAndTemplate.getTemplate());

        return savedEntity.getId();
    }

    @Transactional
    @Async("taskExecutor")
    public void runInfobrevsjobb(UUID infobrevsjobbId, UUID dokumentId) {
        infobrevsjobbRepository.setInfobrevsjobbStatus(infobrevsjobbId, AvtalsjobbStatusDto.IN_PROGRESS);

        try {
            var bytes = infobrevZipData(infobrevsjobbId, dokumentId);
            var path = saveZipTempFile(bytes);
            infobrevsjobbRepository.setInfobrevsjobbPath(infobrevsjobbId, path);
        } catch (Exception e) {
            infobrevsjobbRepository.setInfobrevsjobbStatus(infobrevsjobbId, AvtalsjobbStatusDto.ERROR);
            throw new MarkkollException(MarkkollError.INFOBREV_ERROR, e);
        }

        infobrevsjobbRepository.setInfobrevsjobbStatus(infobrevsjobbId, AvtalsjobbStatusDto.DONE);
        loggService.addInfobrevHandelse(infobrevsjobbId);
    }

    private byte[] infobrevZipData(UUID infobrevsjobbId, UUID dokumentId) throws Exception {
        var baos = new ByteArrayOutputStream();
        try (var zos = new ZipOutputStream(baos))
        {
            var infobrevsjobb = infobrevsjobbRepository.getInfobrevsjobb(infobrevsjobbId)
                .orElseThrow(() ->  new MarkkollException(MarkkollError.INFOBREV_ERROR));
            var fastigheter = infobrevsjobbRepository.getInfobrevsjobbFastigheter(infobrevsjobbId);

            for (var fastighet: fastigheter) {
                var pdf = dokumentGeneratorService.getInfobrev(infobrevsjobb.getProjektId(),
                    fastighet.getId(), dokumentId);
                var entry = pdfZipEntry(fastighet.getFastighetsbeteckning());

                zos.putNextEntry(entry);
                zos.write(pdf.getInputStream().readAllBytes());
                zos.closeEntry();

                infobrevsjobbRepository.incrementGeneratedCounter(infobrevsjobbId);
            }
            cachingService.evictKartorCache();
        }

        return baos.toByteArray();
    }

    @Transactional
    public InfobrevsjobbProgressDto getInfobrevsjobbProgress(UUID infobrevsjobbId) {
        var progress = infobrevsjobbRepository.findInfobrevsjobbProgressById(infobrevsjobbId)
                .orElseThrow(() -> new MarkkollException(MarkkollError.INFOBREV_ERROR));

        log.debug("Hämtade progress för infobrevsjobb: {}", infobrevsjobbId);

        return modelMapper.map(progress, InfobrevsjobbProgressDto.class);
    }

    public Optional<Resource> getInfobrev(UUID infobrevsjobbId) {
        var infobrevsjobbEntity = infobrevsjobbRepository.findById(infobrevsjobbId)
                .orElseThrow(() -> new MarkkollException(MarkkollError.INFOBREV_ERROR));

        try {
            log.info("Hämtar infobrevsdata för infobrevsjobb {}...", infobrevsjobbId);

            if (infobrevsjobbEntity.getStatus() == AvtalsjobbStatusDto.DONE) {
                var file = new File(infobrevsjobbEntity.getPath());
                var bytes = new FileNameAwareByteArrayResource(FileUtils.readFileToByteArray(file),
                    infobrevsjobbEntity.getFilnamn());
                log.info("Läste in infobrevsdata för infobrevsjobb {}", infobrevsjobbId);
                return Optional.of(bytes);
            }
            else if (infobrevsjobbEntity.getStatus() == AvtalsjobbStatusDto.ERROR) {
                log.error("Kan inte hämta felaktigt skapat infobrev för infobrevsjobb {}", infobrevsjobbId);
                throw new MarkkollException(MarkkollError.INFOBREV_ERROR);
            }
            else {
                log.warn("Ingen data finns att hämta för infobrev i infobrevsjobb {}", infobrevsjobbId);
                return Optional.empty();
            }
        } catch (IOException e) {
            log.error("Fel inträffade vid genering av infobrev {}", infobrevsjobbEntity.getId());
            throw new MarkkollException(MarkkollError.INFOBREV_ERROR, e);
        }
    }

    public void deleteInfobrevsjobb(UUID infobrevsjobbId) {
        log.info("Tar bort infobrevsjobb med id: " + infobrevsjobbId);

        if (!infobrevsjobbRepository.existsById(infobrevsjobbId)) {
            throw new EntityNotFoundException();
        }

        infobrevsjobbRepository.deleteById(infobrevsjobbId);
    }

    private ZipEntry pdfZipEntry(String fbet) {
        var filename = DokumentUtil.getPdfFilename(DokumentTypDto.INFOBREV, fbet);

        return new ZipEntry(filename);
    }

    private String saveZipTempFile(byte[] data) throws IOException {
        File file = File.createTempFile(DokumentUtil.getTempFilePrefix(DokumentTypDto.INFOBREV), ".zip", null);
        file.deleteOnExit();

        log.info("Skriver zipfil till sökväg: {}", file.getAbsolutePath());
        var fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();

        return file.getAbsolutePath();
    }
}
