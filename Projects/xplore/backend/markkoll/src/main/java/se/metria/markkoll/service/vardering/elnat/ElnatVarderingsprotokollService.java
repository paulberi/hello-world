package se.metria.markkoll.service.vardering.elnat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollConfigEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollMetadataEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.vardering.elnat.ElnatVarderingsprotokollRepository;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.klassificering.ElnatKlassificeringService;
import se.metria.markkoll.service.vardering.VarderingsprotokollService;
import se.metria.markkoll.service.vardering.elnat.bilaga.ElnatBilagaService;
import se.metria.markkoll.util.CollectionUtil;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElnatVarderingsprotokollService implements VarderingsprotokollService<ElnatVarderingsprotokollDto> {
    @NonNull
    private final AvtalsinstallningarRepository avtalsinstallningarRepository;

    @NonNull
    private final AclService aclService;

    @NonNull
    private final ElnatBilagaService bilagaService;

    @NonNull
    private final ElnatKlassificeringService elnatKlassificeringService;

    @NonNull
    private final FilService filService;

    @NonNull
    private final ElnatVarderingsprotokollRepository varderingsprotokollRepository;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final OmradesintrangRepository omradesintrangRepository;

    public static String ELEKTRISK_STARKSTROMSLEDNING = "Elektrisk starkströmsledning";

    @Transactional
    public UUID create(ElnatVarderingsprotokollDto varderingsprotokollDto, UUID avtalId) {
        log.debug("Skapar elnätsvärderingsprotokoll för avtal {}", avtalId);

        var entity = modelMapper.map(varderingsprotokollDto, ElnatVarderingsprotokollEntity.class);
        var avtal = avtalRepository.findById(avtalId).orElseThrow(EntityNotFoundException::new);
        entity.setAvtal(avtal);

        var saved = varderingsprotokollRepository.save(entity);

        aclService.createObject(saved.getId(), ElnatVarderingsprotokollEntity.class, avtalId, AvtalEntity.class);

        return saved.getId();
    }

    @Override
    @Transactional
    public void createKlassificeradeVarderingsprotokoll(UUID versionId, Collection<UUID> avtalIds) {
        var avtalList = avtalRepository.findAllById(avtalIds);

        var now = LocalDateTime.now();

        var entities = new ArrayList<ElnatVarderingsprotokollEntity>();
        for (var avtal: avtalList) {
            var vpEntity = createKlassificeradVpEntity(avtal, versionId, now);
            entities.add(vpEntity);
        }
        varderingsprotokollRepository.saveAll(entities);

        for (var entity: entities) {
            aclService.createObject(entity.getId(), ElnatVarderingsprotokollEntity.class, entity.getAvtal().getId(),
                AvtalEntity.class);
        }
    }

    @Override
    @Transactional
    public void updateKlassificeradeVarderingsprotokoll(UUID versionId, Collection<UUID> avtalIds) {
        var now = LocalDateTime.now();

        var vpEntitites = varderingsprotokollRepository.findAllByAvtalIdIn(avtalIds);
        for (var vpEntity: vpEntitites) {
            var vpDto = createKlassificeradVpDto(versionId, vpEntity.getAvtal().getId(), now);
            var id = vpEntity.getId();
            modelMapper.map(vpDto, vpEntity);
            vpEntity.setId(id);
        }

        varderingsprotokollRepository.saveAll(vpEntitites);
    }

    public ElnatVarderingsprotokollDto get(UUID varderingsprotokollId) {
        log.info("Hämtar elnätsvärderingsprotokoll {}", varderingsprotokollId);

        var entity = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, ElnatVarderingsprotokollDto.class);
    }

    @Override
    public Optional<ElnatVarderingsprotokollDto> getWithAvtalId(UUID avtalId) {
        log.info("Hämtar elnätsvärderingsprotokoll för avtal {}", avtalId);
        
        var entityOpt = varderingsprotokollRepository.getWithAvtalId(avtalId);

        return entityOpt.map(entity -> modelMapper.map(entity, ElnatVarderingsprotokollDto.class));
    }

    @Override
    public ElnatVarderingsprotokollDto getEmptyVarderingsprotokoll() {
        return new ElnatVarderingsprotokollDto();
    }

    @Override
    public ElnatVarderingsprotokollDto getKlassificeratVarderingsprotokoll(UUID avtalId, UUID versionId) {
        var klassificering = elnatKlassificeringService.klassificera(avtalId, versionId);
        return elnatKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering, LocalDateTime.now());
    }

    @Transactional
    public void delete(UUID varderingsprotokollId) {
        log.info("Tar bort elnätsvärderingsprotokoll {}", varderingsprotokollId);

        if (!varderingsprotokollRepository.existsById(varderingsprotokollId)) {
            throw new EntityNotFoundException();
        }

        varderingsprotokollRepository.deleteById(varderingsprotokollId);

        aclService.deleteObject(varderingsprotokollId, ElnatVarderingsprotokollEntity.class);
    }

    public ElnatVarderingsprotokollDto getWithBilagaid(UUID bilagaId) {
        var vpId = varderingsprotokollRepository.getIdWithBilagaId(bilagaId)
            .orElseThrow(EntityExistsException::new);

        return get(vpId);
    }

    @Transactional
    public void
    removeBilaga(UUID bilagaId) {
        bilagaService.delete(bilagaId);
    }

    @Transactional
    public void
    updateMetadata(UUID varderingsprotokollId,
                   ElnatVarderingsprotokollMetadataDto varderingsprotokollMetadataDto)
    {
        log.info("Uppdaterar metadata för elnätsvärderingsprotokoll {}", varderingsprotokollId);

        var metadataEntity = modelMapper.map(varderingsprotokollMetadataDto,
            ElnatVarderingsprotokollMetadataEntity.class);

        var vpEntity = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        BeanUtils.copyProperties(metadataEntity, vpEntity.getMetadata());
    }

    @Transactional
    public void
    updateConfig(UUID varderingsprotokollId,
                 ElnatVarderingsprotokollConfigDto varderingsprotokollConfigDto)
    {
        log.info("Uppdaterar beräkningskonfiguration för elnätsvärderingsprotokoll {}", varderingsprotokollId);

        var configEntity = modelMapper.map(varderingsprotokollConfigDto,
            ElnatVarderingsprotokollConfigEntity.class);

        var vpEntity = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        BeanUtils.copyProperties(configEntity, vpEntity.getConfig());
    }

    @Transactional
    public void updateLittera(UUID varderingsprotokollId, String littera) {
        var vp = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        vp.getMetadata().setLedning(littera);
    }

    @Transactional
    public void
    updatePrisomrade(UUID varderingsprotokollId, ElnatPrisomradeDto prisomrade) {
        log.info("Uppdaterar prisområde för elnätsvärderingsprotokoll {} till {}", varderingsprotokollId, prisomrade);

        var vpEntity = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        vpEntity.setPrisomrade(prisomrade);
    }

    public ElnatVarderingsprotokollConfigDto getConfig(UUID varderingsprotokollId) {
        var entity = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, ElnatVarderingsprotokollConfigDto.class);
    }

    @Transactional(rollbackFor = IOException.class)
    public ElnatBilagaDto
    addBilaga(MultipartFile file, BilagaTypDto bilagaTypDto, UUID varderingsprotokollId) throws IOException {

        log.info("Lägger till bilaga {} med namn {} till elnätsvärderingsprotokoll {}", file.getOriginalFilename(),
            bilagaTypDto, varderingsprotokollId);

        var kundId = varderingsprotokollRepository.getKundId(varderingsprotokollId);
        var filId = filService.create(file, kundId).getId();

        return bilagaService.create(filId, varderingsprotokollId, bilagaTypDto);
    }

    @Transactional
    public void update(ElnatVarderingsprotokollDto elnatVp) {
        log.info("Uppdaterar värderingsprotokoll {}", elnatVp.getId());

        var vpEntity = varderingsprotokollRepository.findById(elnatVp.getId())
            .orElseThrow(EntityNotFoundException::new);

        modelMapper.map(elnatVp, vpEntity);

        varderingsprotokollRepository.save(vpEntity);
    }

    @Transactional
    public void update(ElnatVarderingsprotokollDto elnatVp, UUID avtalId) {
        var vpEntity = varderingsprotokollRepository.findByAvtalId(avtalId)
            .orElseThrow(EntityNotFoundException::new);

        var vpId = vpEntity.getId();
        modelMapper.map(elnatVp, vpEntity);
        vpEntity.setId(vpId);

        varderingsprotokollRepository.save(vpEntity);
    }

    @Transactional
    public void updateRotnetto(UUID varderingsprotokollId, double ersattning) {
        var entity = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        entity.setRotnetto(ersattning);
    }

    @Transactional
    public void setMarkvarde(UUID varderingsprotokollId, int ersattning) {

        var vp = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        CollectionUtil.emptyCollection(vp.getLedningSkogsmark());
        vp.addLedningSkogsmark(ELEKTRISK_STARKSTROMSLEDNING, ersattning);
    }

    private ElnatVarderingsprotokollDto createKlassificeradVpDto(UUID versionId, UUID avtalId, LocalDateTime time) {
        // TODO: Hitta något sätt att optimera den här intrångshämtningen
        var klassificering = elnatKlassificeringService.klassificera(avtalId, versionId);
        return elnatKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering, time);
    }

    private ElnatVarderingsprotokollEntity
    createKlassificeradVpEntity(AvtalEntity avtal, UUID versionId, LocalDateTime time) {
        var vp = createKlassificeradVpDto(versionId, avtal.getId(), time);

        var vpEntity = modelMapper.map(vp, ElnatVarderingsprotokollEntity.class);
        vpEntity.setAvtal(avtal);

        return vpEntity;
    }
}
