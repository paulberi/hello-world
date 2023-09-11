package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;
import se.metria.markkoll.openapi.model.DokumentTypDto;
import se.metria.markkoll.openapi.model.FilterAndTemplateDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.avtal.AvtalsjobbRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.util.FileUtil;
import se.metria.markkoll.util.dokument.DokumentUtil;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AvtalsjobbService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final AvtalsjobbRepository avtalsjobbRepository;

    @NonNull
    private final AvtalUtskickZipService avtalUtskickZipService;

    @NonNull
    private final CachingService cachingService;

    @NonNull
    private final LoggService loggService;

    public void cancelAvtalsjobb(UUID avtalsjobbId) {
        avtalsjobbRepository.setAvtalsjobbStatus(avtalsjobbId, AvtalsjobbStatusDto.CANCELLED);
    }

    @Transactional
    public UUID createAvtalsjobb(UUID projektId, FilterAndTemplateDto filterAndTemplate) {
        var avtal = avtalRepository.avtalFiltered(projektId, filterAndTemplate.getFilter());

        var avtalsjobbEntity = AvtalsjobbEntity
            .builder()
            .status(AvtalsjobbStatusDto.NONE)
            .avtal(avtal)
            .generated(0)
            .total(avtal.size())
            .build();

        var savedEntity = avtalsjobbRepository.saveAndFlush(avtalsjobbEntity);
        aclService.createObject(savedEntity.getId(), AvtalsjobbEntity.class, projektId, ProjektEntity.class);

        log.info("Avtalsjobb {} skapat för projekt: {} med mall: {}", savedEntity.getId(), projektId, filterAndTemplate.getTemplate());

        return savedEntity.getId();
    }

    @Transactional
    @Async("taskExecutor")
    public void runAvtalsjobb(UUID avtalsjobbId, UUID dokumentId) {
        avtalsjobbRepository.setAvtalsjobbStatus(avtalsjobbId, AvtalsjobbStatusDto.IN_PROGRESS);

        try {
            var bytes = avtalZipData(avtalsjobbId, dokumentId);
            if (bytes != null) {
                var path = FileUtil.saveTempFile(bytes,
                    DokumentUtil.getTempFilePrefix(DokumentTypDto.MARKUPPLATELSEAVTAL), null).getAbsolutePath();

                avtalsjobbRepository.setPath(avtalsjobbId, path);
                avtalsjobbRepository.setAvtalsjobbStatus(avtalsjobbId, AvtalsjobbStatusDto.DONE);
                loggService.addAvtalHandelse(avtalsjobbId);
            }
        } catch (Exception e) {
            avtalsjobbRepository.setAvtalsjobbStatus(avtalsjobbId, AvtalsjobbStatusDto.ERROR);
            throw new MarkkollException(MarkkollError.AVTAL_ERROR, e);
        }
    }

    private byte[] avtalZipData(UUID avtalsjobbId, UUID dokumentId) throws Exception {

        var baos = new ByteArrayOutputStream();
        try (var zos = new ZipOutputStream(baos))
        {
            for (var avtal: avtalsjobbRepository.getAvtal(avtalsjobbId)) {
                var status = avtalsjobbRepository.getAvtalsjobbStatus(avtalsjobbId);
                if (status == AvtalsjobbStatusDto.CANCELLED) {
                    return null;
                }

                var avtalZip = avtalUtskickZipService.getAvtalZip(avtal.getId(), dokumentId);

                var formatter = DateTimeFormatter.ofPattern("yyMMdd");
                var entryVp = new ZipEntry(avtalZip.getFilename() + LocalDate.now().format(formatter));
                zos.putNextEntry(entryVp);
                zos.write(avtalZip.getByteArray());
                zos.closeEntry();

                avtalsjobbRepository.incrementGeneratedCounter(avtalsjobbId);
            }
            cachingService.evictKartorCache();
        }

        return baos.toByteArray();
    }
}
