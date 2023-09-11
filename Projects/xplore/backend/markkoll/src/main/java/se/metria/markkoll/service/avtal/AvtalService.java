package se.metria.markkoll.service.avtal;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.avtal.AvtalMetadataEntity;
import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.avtal.AvtalsjobbRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.vardering.elnat.ElnatVarderingsprotokollRepository;
import se.metria.markkoll.service.AvtalUtskickZipService;
import se.metria.markkoll.service.CachingService;
import se.metria.markkoll.service.UtskicksnummerService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.dokument.DokumentmallService;
import se.metria.markkoll.service.geometristatus.GeometristatusService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.FileUtil;
import se.metria.markkoll.util.dokument.DokumentUtil;
import se.metria.markkoll.util.dokument.FileType;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AvtalService {
    @NonNull
    private final AvtalsinstallningarRepository avtalsinstallningarRepository;

    @NonNull
    private final AclService aclService;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final AvtalUtskickZipService avtalUtskickZipService;

    @NonNull
    private final Clock clock;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @NonNull
    private final AvtalsjobbRepository avtalsjobbRepository;

    @NonNull
    private final CachingService cachingService;

    @NonNull
    private final LoggService loggService;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final DokumentmallService dokumentmallService;

    @NonNull
    private final UtskicksnummerService utskicksnummerService;

    @NonNull
    private final MarkagareService markagareService;

    @NonNull
    private final GeometristatusService geometristatusService;

    @NonNull
    private final ElnatVarderingsprotokollRepository elnatVarderingsprotokollRepository;

    private ModelMapper modelMapper = getModelMapper();

    public Integer avtalCount(UUID projektId, FastighetsfilterDto fastighetsfilter) {
        if(fastighetsfilter.getFastighetsIds() != null && !fastighetsfilter.getFastighetsIds().isEmpty()) {
            return avtalRepository.findAllByProjektIdAndFastighetIdIn(projektId, fastighetsfilter.getFastighetsIds()).size();
        } else {
            return avtalRepository.avtalFiltered(projektId, fastighetsfilter).size();
        }

    }

    public void cancelAvtalsjobb(UUID avtalsjobbId) {
        avtalsjobbRepository.setAvtalsjobbStatus(avtalsjobbId, AvtalsjobbStatusDto.CANCELLED);
    }

    public List<AvtalDto> getAvtal(UUID projektId, Iterable<UUID> fastighetIds) {
        var entities = avtalRepository.findAllByProjektIdAndFastighetIdIn(projektId, fastighetIds);
        
        return CollectionUtil.modelMapperList(entities, modelMapper, AvtalDto.class);
    }

    public AvtalDto getAvtal(UUID projektId, UUID fastighetId) {
        var entity = avtalRepository.findByProjektIdAndFastighetId(projektId, fastighetId);

        return modelMapper.map(entity, AvtalDto.class);
    }

    public AvtalDto getAvtal(UUID avtalId) {
        var entity = avtalRepository.findById(avtalId).orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, AvtalDto.class);
    }

    public void updateAvtal(AvtalDto avtalDto) {
        var entity = avtalRepository.findById(avtalDto.getId()).orElseThrow(EntityNotFoundException::new);

        modelMapper.map(avtalDto, entity);

        avtalRepository.save(entity);
    }

    @Transactional
    public void updateAvtalsstatus(UUID fastighetId, UUID projektId, AvtalsstatusDto avtalsstatus) {
        updateAvtalsstatus(projektId, Arrays.asList(fastighetId), avtalsstatus);
    }

    @Transactional
    public void updateAvtalsstatus(UUID projektId, Collection<UUID> fastighetIds, AvtalsstatusDto avtalsstatus) {
        log.info("Sätter avtalsstatus till {} för {} avtal i projekt {}", avtalsstatus, fastighetIds.size(), projektId);

        var previousAvtalsstatusar = avtalRepository.findByProjektIdAndFastighetIdIn(projektId, fastighetIds);

        var avtalIds = previousAvtalsstatusar.stream()
            .map(ps -> ps.getId())
            .collect(Collectors.toList());

        markagareService.updateAvtalsstatus(avtalIds, avtalsstatus);

        if (avtalsstatus != AvtalsstatusDto.EJ_BEHANDLAT) {
            log.info("Avtalsstatus ändrad från {}. Uppdaterar geometristatus till {}",
                AvtalsstatusDto.EJ_BEHANDLAT, GeometristatusDto.OFORANDRAD);

            var changedFromEjBehandlatAvtalIds = previousAvtalsstatusar.stream()
                .filter(ps -> ps.getAvtalsstatus() == AvtalsstatusDto.EJ_BEHANDLAT)
                .map(ps -> ps.getId())
                .collect(Collectors.toList());

            geometristatusService.setGeometristatus(projektId, changedFromEjBehandlatAvtalIds, GeometristatusDto.OFORANDRAD);
        }
    }

    public AvtalMetadataDto getAvtalMetadata(UUID avtalId) {
        var avtalMetadata = avtalRepository.getOne(avtalId).getMetadata();
        return modelMapper.map(avtalMetadata, AvtalMetadataDto.class);
    }

    @Transactional
    public UUID createAvtalsjobb(UUID projektId, FilterAndTemplateDto filterAndTemplate) {
        List<AvtalEntity> avtal;
        if(filterAndTemplate.getFilter().getFastighetsIds() != null && !filterAndTemplate.getFilter().getFastighetsIds().isEmpty()) {
            avtal = avtalRepository.findAllByProjektIdAndFastighetIdIn(projektId, filterAndTemplate.getFilter().getFastighetsIds());
        } else {
            avtal = avtalRepository.avtalFiltered(projektId, filterAndTemplate.getFilter());
        }

        var projektnamn = projektRepository.getNamn(projektId);
        var mallnamn = dokumentmallService.get(filterAndTemplate.getTemplate()).getNamn();

        var mappstrategi = avtalsinstallningarRepository.getMappstrategi(projektId);

        var formatter = DateTimeFormatter.ofPattern("yyMMdd");
        var date = LocalDate.now(clock).format(formatter);

        var filnamn = DokumentUtil.createFilename(FileType.ZIP,
            projektnamn.substring(0, Math.min(projektnamn.length(), 19)),
            mallnamn.substring(0, Math.min(mallnamn.length(), 19)),
            date);

        var avtalsjobbEntity = AvtalsjobbEntity
                .builder()
                .status(AvtalsjobbStatusDto.NONE)
                .avtal(avtal)
                .generated(0)
                .total(avtal.size())
                .filnamn(filnamn)
                .mappstrategi(mappstrategi)
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
                avtalsjobbRepository.setAvtalsjobbStatus(avtalsjobbId, AvtalsjobbStatusDto.DONE);
                loggService.addAvtalHandelse(avtalsjobbId);
                avtalsjobbRepository.setPath(avtalsjobbId, path);
            }
        } catch (Exception e) {
            avtalsjobbRepository.setAvtalsjobbStatus(avtalsjobbId, AvtalsjobbStatusDto.ERROR);
            throw new MarkkollException(MarkkollError.AVTAL_ERROR, e);
        }

    }

    @Transactional(readOnly = true)
    public List<EditElnatVpDto> getEditElnatVp(UUID projektId, FastighetsfilterDto fastighetsfilterDto) {
        var avtalIds = avtalRepository.avtalIdsFiltered(projektId, fastighetsfilterDto);
        var editElnatVpViews = avtalRepository.getEditElnatVps(avtalIds);
        return editElnatVpViews.stream()
                .map(view -> new EditElnatVpDto()
                    .fastighetsId(view.getFastighetsId())
                    .avtalMetadata(modelMapper.map(view.getAvtalMetadata(), AvtalMetadataDto.class))
                    .fastighetsbeteckning(view.getFastighetsbeteckning())
                    .vpMetadata(modelMapper.map(view.getElnatVpMetadata(), ElnatVarderingsprotokollMetadataDto.class)))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<EditElnatVpDto> updateEditElnatVpAndAvtalMetadata(UUID projektId, List<EditElnatVpDto> editElnatVpDtos) {
        var fastighetIds = editElnatVpDtos.stream()
            .map(editVp -> editVp.getFastighetsId())
            .collect(Collectors.toList());

        var avtalFastighetIds = avtalRepository.getFastighetAvtalIds(projektId, fastighetIds);
        var editVpMap = editElnatVpDtos.stream()
            .collect(Collectors.toMap(evp -> evp.getFastighetsId(), evp -> evp));

        for (var avtalFastighetId: avtalFastighetIds) {
            var editVp = editVpMap.get(avtalFastighetId.getFastighetId());
            var avtalId = avtalFastighetId.getAvtalId();
            avtalRepository.updateMetadata(avtalId, editVp.getAvtalMetadata());
            elnatVarderingsprotokollRepository.updateMetadata(avtalId, editVp.getVpMetadata());
        }

        return  editElnatVpDtos;
    }

    @Transactional
    public AvtalMetadataDto setAvtalMetadata(UUID avtalId, AvtalMetadataDto avtalMetadataDto) {
        var existing = avtalRepository.getOne(avtalId);

        var avtalMetadataEntity = modelMapper.map(avtalMetadataDto, AvtalMetadataEntity.class);
        existing.setMetadata(avtalMetadataEntity);

        var updated = avtalRepository.save(existing);

        return modelMapper.map(updated.getMetadata(), AvtalMetadataDto.class);
    }

    public void setSkogsfastighet(UUID avtalId, Boolean skogsfastighet) {
        var updated = avtalRepository.setSkogsfastighet(avtalId, skogsfastighet);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void setTillvaratagandeTyp(UUID avtalId, TillvaratagandeTypDto tillvaratagandeTyp) {
        var updated = avtalRepository.setTillvaratagandeTyp(avtalId, tillvaratagandeTyp);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public void setEgetTillvaratagande(UUID avtalId, int egetTillvaratagande) {
        var entity = avtalRepository.findById(avtalId).orElseThrow(EntityNotFoundException::new);

        entity.setEgetTillvaratagande(egetTillvaratagande);
    }

    public AvtalsjobbProgressDto getAvtalsjobbProgress(UUID avtalsjobbId) {
        var progress = avtalsjobbRepository.findAvtalsjobbProgressById(avtalsjobbId)
                .orElseThrow(() -> new MarkkollException(MarkkollError.AVTAL_ERROR));

        log.debug("Hämtade progress för avtalsjobb: {}", avtalsjobbId);

        return modelMapper.map(progress, AvtalsjobbProgressDto.class);
    }

    @Transactional
    public Resource
    getAvtalZipFastighet(UUID projektId, UUID fastighetId)
            throws IllegalAccessException, IOException, Docx4JException, InvalidFormatException {
        var kundId = projektRepository.getKundId(projektId);
        var dokumentmall = dokumentmallService.getSelected(kundId, DokumentTypDto.MARKUPPLATELSEAVTAL);

        var zip = avtalUtskickZipService.getAvtalZip(projektId, fastighetId, dokumentmall.getId());

        if (avtalsinstallningarRepository.getMappstrategi(projektId) == AvtalMappstrategiDto.FLAT) {
            var baos = new ByteArrayOutputStream();
            try (var zos = new ZipOutputStream(baos)) {
                var prefix = FileNameUtils.getBaseName(zip.getFilename()) + "_";
                unzipAndWrite(zip, zos, AvtalMappstrategiDto.FLAT, prefix);
            }
            zip = new FileNameAwareByteArrayResource(baos.toByteArray(), zip.getFilename());
        }

        CreateAvtalsjobbFromAvtal(zip.getByteArray(), projektId, fastighetId);

        cachingService.evictKartorCache();

        return zip;
    }

    public Optional<Resource> getAvtalZipAvtalsjobb(UUID projektId, UUID avtalsjobbId) {
        var avtalsjobbEntity = avtalsjobbRepository.findById(avtalsjobbId)
                .orElseThrow(() -> new MarkkollException(MarkkollError.AVTAL_ERROR));

        try {
            log.info("Hämtar avtalsdata för avtalsjobb {}...", avtalsjobbId);

            if (avtalsjobbEntity.getStatus() == AvtalsjobbStatusDto.DONE) {
                log.info(avtalsjobbEntity.toString());
                var file = new File(avtalsjobbEntity.getPath());

                // TODO: Vi borde redan ha ett filnamn sen innan?
                var bytes = new FileNameAwareByteArrayResource(FileUtils.readFileToByteArray(file),
                    avtalsjobbEntity.getFilnamn());
                log.info("Läste in avtalsdata för avtalsjobb {}", avtalsjobbId);
                return Optional.of(bytes);
            }
            else if (avtalsjobbEntity.getStatus() == AvtalsjobbStatusDto.ERROR) {
                log.error("Kan inte hämta felaktigt skapat avtal för avtalsjobb {}", avtalsjobbId);
                throw new MarkkollException(MarkkollError.AVTAL_ERROR);
            }
            else {
                log.warn("Ingen data finns att hämta för avtal i avtalsjobb {}", avtalsjobbId);
                return Optional.empty();
            }
        } catch (IOException e) {
            log.error("Fel inträffade vid genering av avtal {}", avtalsjobbEntity.getId());
            throw new MarkkollException(MarkkollError.AVTAL_ERROR, e);
        }
    }

    public String getFastighetsBeteckning(UUID avtalId) {
        return avtalRepository.getFastighetsbeteckning(avtalId);
    }

    public void deleteAvtalsjobb(UUID avtalsjobbId) {
        log.info("Tar bort avtalsjobb med id: " + avtalsjobbId);

        if (!avtalsjobbRepository.existsById(avtalsjobbId)) {
            throw new EntityNotFoundException();
        }

        avtalsjobbRepository.deleteById(avtalsjobbId);
    }

    @Transactional
    public UUID create(UUID projektId, UUID fastighetId) {
        var projekt = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);
        var fastighet = fastighetRepository.findById(fastighetId).orElseThrow(EntityNotFoundException::new);

        var avtalEntity = AvtalEntity.builder()
            .projekt(projekt)
            .fastighet(fastighet)
            .build();

        var avtalId=  avtalRepository.save(avtalEntity).getId();

        aclService.createObject(avtalId, AvtalEntity.class, projektId, ProjektEntity.class);

        return avtalId;
    }

    @Transactional
    public List<UUID> create(UUID projektId, List<UUID> fastighetIds) {
        var projekt = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);

        var fastigheter = fastighetRepository.findAllById(fastighetIds);

        List<AvtalEntity> avtalEntities = new ArrayList<AvtalEntity>();
        for (var fastighet: fastigheter) {
            var avtalEntity = new AvtalEntity();
            avtalEntity.setProjekt(projekt);
            avtalEntity.setFastighet(fastighet);

            avtalEntities.add(avtalEntity);
        }

        avtalEntities = avtalRepository.saveAll(avtalEntities);

        for (var avtal: avtalEntities) {
            aclService.createObject(avtal.getId(), AvtalEntity.class, projektId, ProjektEntity.class);
        }

        return avtalEntities.stream()
            .map(av -> av.getId())
            .collect(Collectors.toList());
    }

    public AvtalsstatusDto getAvtalsstatus(UUID avtalId) {
        return avtalRepository.getAvtalsstatus(avtalId);
    }

    public int getUtskicksnummer(UUID avtalId) {
        return avtalRepository.findById(avtalId).orElseThrow(EntityNotFoundException::new).getUtskicksnummer();
    }

    @Transactional
    public int updateUtskicksnummer(UUID avtalId) {
        var entity = avtalRepository.findById(avtalId).orElseThrow(EntityNotFoundException::new);

        var avtalsstatus = avtalRepository.getAvtalsstatus(avtalId);
        var noIncrementStatuses = Arrays.asList(
            AvtalsstatusDto.AVTAL_SIGNERAT,
            AvtalsstatusDto.ERSATTNING_UTBETALAS,
            AvtalsstatusDto.ERSATTNING_UTBETALD
        );

        if (noIncrementStatuses.contains(avtalsstatus)) {
            return entity.getUtskicksnummer();
        }
        else {
            var kundId = avtalRepository.getKundId(avtalId);

            var utskicksnummer = utskicksnummerService.increment(kundId);

            entity.setUtskicksnummer(utskicksnummer);

            return utskicksnummer;
        }
    }

    private ModelMapper getModelMapper() {
        var modelMapper = new ModelMapper();

        return modelMapper;
    }

    private void
    CreateAvtalsjobbFromAvtal(byte[] avtalData,
                              UUID projektId,
                              UUID fastighetId) throws IOException {

        var avtalEntity = avtalRepository.getAvtalByProjektIdAndFastighetId(projektId, fastighetId);

        var avtalsjobb = AvtalsjobbEntity.builder()
            .status(AvtalsjobbStatusDto.DONE)
            .avtal(Arrays.asList(avtalEntity))
            .generated(1)
            .total(1)
            .filnamn("")
            .mappstrategi(AvtalMappstrategiDto.FLAT)
            .path(FileUtil.saveTempFile(avtalData, "avtal"))
            .build();

        var saved = avtalsjobbRepository.saveAndFlush(avtalsjobb);

        aclService.createObject(saved.getId(), AvtalsjobbEntity.class, projektId, ProjektEntity.class);
        loggService.addAvtalHandelse(saved.getId());
    }

    private byte[] avtalZipData(UUID avtalsjobbId, UUID dokumentId) throws Exception {

        var baos = new ByteArrayOutputStream();
        try (var zos = new ZipOutputStream(baos))
        {
            var mappstrategi = avtalsjobbRepository.getMappstrategi(avtalsjobbId);

            var avtalList = avtalsjobbRepository.getAvtal(avtalsjobbId);
            for (var avtal: avtalList) {
                if (avtalsjobbRepository.getAvtalsjobbStatus(avtalsjobbId) == AvtalsjobbStatusDto.CANCELLED) {
                    return null;
                }

                var avtalZip = avtalUtskickZipService.getAvtalZip(avtal.getId(), dokumentId);
                var prefix = avtalList.size() == 1 ?
                    "" : FileNameUtils.getBaseName(avtalZip.getFilename()) + (mappstrategi == AvtalMappstrategiDto.FLAT ? "_" : "/");

                unzipAndWrite(avtalZip, zos, mappstrategi, prefix);

                avtalsjobbRepository.incrementGeneratedCounter(avtalsjobbId);
            }
            cachingService.evictKartorCache();
        }

        return baos.toByteArray();
    }

    private void unzipAndWrite(Resource avtalZip, ZipOutputStream zos, AvtalMappstrategiDto strategi, String prefix) throws IOException {
        var zis = new ZipInputStream(avtalZip.getInputStream());

        for (var entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry()) {
            if (strategi == AvtalMappstrategiDto.FLAT && isAFolder(entry)) {
                continue;
            }

            var entryName = strategi == AvtalMappstrategiDto.FLAT ?
                FileNameUtils.getBaseName(entry.getName()) + "." + FileNameUtils.getExtension(entry.getName()) :
                entry.getName();

            var bytes = zis.readAllBytes();
            var nextEntry = new ZipEntry(prefix + entryName);
            zos.putNextEntry(nextEntry);
            zos.write(bytes);
            zos.closeEntry();
        }
    }

    private boolean isAFolder(ZipEntry entry) {
        return FileNameUtils.getExtension(entry.getName()).isEmpty();
    }
}
