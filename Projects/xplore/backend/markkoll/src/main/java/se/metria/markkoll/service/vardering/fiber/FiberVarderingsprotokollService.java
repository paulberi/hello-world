package se.metria.markkoll.service.vardering.fiber;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.FiberVarderingsprotokollDto;
import se.metria.markkoll.openapi.model.FiberVarderingsprotokollMetadataDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.vardering.fiber.FiberVarderingsprotokollRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.klassificering.FiberKlassificeringService;
import se.metria.markkoll.service.vardering.VarderingsprotokollService;
import se.metria.markkoll.util.modelmapper.FiberVpConverter;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FiberVarderingsprotokollService implements VarderingsprotokollService<FiberVarderingsprotokollDto> {

    @NonNull
    private final AclService aclService;

    @NonNull
    private final FiberKlassificeringService fiberKlassificeringService;

    @NonNull
    private final FiberVarderingsprotokollRepository fiberVarderingsprotokollRepository;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final OmradesintrangRepository omradesintrangRepository;

    private ModelMapper modelMapper = getModelMapper();

    @Transactional
    public UUID create(FiberVarderingsprotokollDto fiberVarderingsprotokollDto, UUID avtalId) {
        log.debug("Skapar fibervärderingsprotokoll för avtal {}", avtalId);

        var entity = modelMapper.map(fiberVarderingsprotokollDto, FiberVarderingsprotokollEntity.class);
        var avtal = avtalRepository.findById(avtalId).orElseThrow(EntityNotFoundException::new);
        entity.setAvtal(avtal);

        var saved = fiberVarderingsprotokollRepository.save(entity);

        aclService.createObject(saved.getId(), FiberVarderingsprotokollEntity.class, avtalId, AvtalEntity.class);

        return saved.getId();
    }

    public void create(Collection<FiberVarderingsprotokollDto> vps, UUID avtalId) {
        var avtal = avtalRepository.findById(avtalId).orElseThrow(EntityNotFoundException::new);

        var entities = new ArrayList<FiberVarderingsprotokollEntity>();
        for (var vp: vps) {
            var entity = modelMapper.map(vp, FiberVarderingsprotokollEntity.class);
            entity.setAvtal(avtal);
            entities.add(entity);
        }

        fiberVarderingsprotokollRepository.saveAll(entities);

        for (var e: entities) {
            aclService.createObject(e.getId(), FiberVarderingsprotokollEntity.class, avtalId, AvtalEntity.class);
        }
    }

    public FiberVarderingsprotokollDto get(UUID fiberVarderingsprotokollId) {
        log.info("Hämtar fibervärderingsprotokoll {}", fiberVarderingsprotokollId);

        var entity = fiberVarderingsprotokollRepository.findById(fiberVarderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, FiberVarderingsprotokollDto.class);
    }

    @Override
    public Optional<FiberVarderingsprotokollDto> getWithAvtalId(UUID avtalId) {
        log.info("Hämtar fibervärderingsprotokoll {}", avtalId);
        
        var entityOpt = fiberVarderingsprotokollRepository.getWithAvtalId(avtalId);

        return entityOpt.map(entity -> modelMapper.map(entity, FiberVarderingsprotokollDto.class));
    }

    @Override
    public FiberVarderingsprotokollDto getEmptyVarderingsprotokoll() {
        return new FiberVarderingsprotokollDto()
            .metadata(new FiberVarderingsprotokollMetadataDto().varderingstidpunkt(LocalDateTime.now()));
    }

    @Override
    public FiberVarderingsprotokollDto getKlassificeratVarderingsprotokoll(UUID avtalId, UUID versionId) {
        var klassificering = fiberKlassificeringService.klassificera(avtalId, versionId);
        return fiberKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void createKlassificeradeVarderingsprotokoll(UUID versionId, Collection<UUID> avtalIds) {
        var avtalList = avtalRepository.findAllById(avtalIds);

        var now = LocalDateTime.now();

        var entities = new ArrayList<FiberVarderingsprotokollEntity>();
        for (var avtal: avtalList) {
            var vpEntity = createKlassificeradVpEntity(avtal, versionId, now);
            entities.add(vpEntity);
        }
        fiberVarderingsprotokollRepository.saveAll(entities);

        for (var entity: entities) {
            aclService.createObject(entity.getId(), FiberVarderingsprotokollEntity.class, entity.getAvtal().getId(),
                AvtalEntity.class);
        }
    }

    @Override
    @Transactional
    public void updateKlassificeradeVarderingsprotokoll(UUID versionId, Collection<UUID> avtalIds) {
        var now = LocalDateTime.now();

        var vpEntitites = fiberVarderingsprotokollRepository.findAllByAvtalIdIn(avtalIds);
        for (var vpEntity: vpEntitites) {
            var vpDto = createKlassificeradVpDto(versionId, vpEntity.getAvtal().getId(), now);
            modelMapper.map(vpDto, vpEntity);
        }

        fiberVarderingsprotokollRepository.saveAll(vpEntitites);
    }

    @Transactional
    public void delete(UUID fiberVarderingsprotokollId) {
        log.info("Tar bort fibervärderingsprotokoll {}", fiberVarderingsprotokollId);

        if (!fiberVarderingsprotokollRepository.existsById(fiberVarderingsprotokollId)) {
            throw new EntityNotFoundException();
        }

        fiberVarderingsprotokollRepository.deleteById(fiberVarderingsprotokollId);
        aclService.deleteObject(fiberVarderingsprotokollId, FiberVarderingsprotokollEntity.class);
        fiberVarderingsprotokollRepository.flush();
    }

    @Transactional
    public void
    update(FiberVarderingsprotokollDto vpDto) {
        log.info("Uppdaterar värderingsprotokoll {}", vpDto.getId());

        var vpEntity = fiberVarderingsprotokollRepository.findById(vpDto.getId())
            .orElseThrow(EntityNotFoundException::new);

        modelMapper.map(vpDto, vpEntity);

        fiberVarderingsprotokollRepository.save(vpEntity);
    }

    @Transactional
    public void update(FiberVarderingsprotokollDto vpDto, UUID avtalId) {
        var vpEntity = fiberVarderingsprotokollRepository.findByAvtalId(avtalId)
            .orElseThrow(EntityNotFoundException::new);

        var vpId = vpEntity.getId();
        modelMapper.map(vpDto, vpEntity);
        vpEntity.setId(vpId);

        fiberVarderingsprotokollRepository.save(vpEntity);
    }

    private ModelMapper getModelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.addConverter(new FiberVpConverter());
        return modelMapper;
    }

    private FiberVarderingsprotokollDto createKlassificeradVpDto(UUID versionId, UUID avtalId, LocalDateTime time) {
        var klassificering = fiberKlassificeringService.klassificera(avtalId, versionId);
        return fiberKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering,
            time);
    }

    private FiberVarderingsprotokollEntity
    createKlassificeradVpEntity(AvtalEntity avtal, UUID versionId, LocalDateTime time) {
        var vp = createKlassificeradVpDto(versionId, avtal.getId(), time);

        var vpEntity = modelMapper.map(vp, FiberVarderingsprotokollEntity.class);
        vpEntity.setAvtal(avtal);

        return vpEntity;
    }
}
