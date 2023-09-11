package se.metria.markkoll.service.geometristatus;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.GeometristatusEntity;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.geometristatus.GeometristatusRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GeometristatusService {
    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final GeometristatusRepository geometristatusRepository;

    @NonNull
    private final VersionRepository versionRepository;

    @NonNull
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void createGeometristatus(UUID projektId, Iterable<UUID> avtalIds, GeometristatusDto geometristatus) {
        var version = versionRepository.getCurrentVersion(projektId);
        if (version == null) {
            throw new EntityNotFoundException();
        }

        var avtalEntities = avtalRepository.findAllById(avtalIds);

        var geometristatusEntities = new ArrayList<GeometristatusEntity>();
        for (var avtalEntity: avtalEntities) {
            var geometristatusEntity = GeometristatusEntity.builder()
                .avtal(avtalEntity)
                .version(version)
                .geometristatus(geometristatus)
                .build();

            geometristatusEntities.add(geometristatusEntity);
        }

        geometristatusRepository.saveAll(geometristatusEntities);
    }

    public GeometristatusDto getGeometristatus(UUID projektId, UUID fastighetId) {
        var entity = geometristatusRepository.findByFastighetIdAndProjektId(fastighetId,
            projektId).orElseThrow(EntityNotFoundException::new);

        return entity.getGeometristatus();
    }

    @Transactional
    public void setGeometristatus(UUID projektId, Collection<UUID> avtalIds, GeometristatusDto geometristatus) {
        log.info("Sätter geometristatus till {} för {} avtal", geometristatus, avtalIds.size());

        var versionId = versionRepository.getCurrentVersionId(projektId);

        var changedStatusAvtalIds = geometristatusRepository.findByVersionIdAndAvtalIdIn(versionId, avtalIds)
            .stream().filter(gs -> gs.getGeometristatus() != geometristatus)
            .map(gs -> gs.getAvtalId())
            .collect(Collectors.toList());

        geometristatusRepository.updateGeometristatus(versionId, changedStatusAvtalIds, geometristatus);
        publisher.publishEvent(new GeometristatusEvent(changedStatusAvtalIds, geometristatus));
    }
}
