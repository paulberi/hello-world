package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningAnledning;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningEntity;
import se.metria.markkoll.repository.FastighetsforteckningRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@MarkkollService
@RequiredArgsConstructor
@Slf4j
public class FastighetsforteckningService {
    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @NonNull
    private final FastighetsforteckningRepository fastighetsforteckningRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @Transactional
    public UUID
    create(UUID projektId, UUID fastighetId, UUID avtalId, FastighetsforteckningAnledning anledning)
    {
        log.info("Skapar fastighetsförteckning för avtal {} för fastighet {} i projekt {} av anledningen {}",
            avtalId, fastighetId, projektId, anledning);

        var projekt = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);
        var fastighet = fastighetRepository.findById(fastighetId).orElseThrow(EntityNotFoundException::new);
        var avtal = avtalRepository.findById(avtalId).orElseThrow(EntityNotFoundException::new);

        var entity = FastighetsforteckningEntity.builder()
            .avtal(avtal)
            .fastighet(fastighet)
            .projekt(projekt)
            .anledning(anledning)
            .build();

        return fastighetsforteckningRepository.save(entity).getId();
    }

    @Transactional
    public List<UUID>
    create(UUID projektId, List<UUID> avtalIds, FastighetsforteckningAnledning anledning) {
        var projekt = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);
        var avtalList = avtalRepository.findAllById(avtalIds);

        List<FastighetsforteckningEntity> ffEntities = new ArrayList<>();
        for (var avtal: avtalList) {
            var fastighet = avtal.getFastighet();

            var ffEntity =  FastighetsforteckningEntity.builder()
                .avtal(avtal)
                .fastighet(fastighet)
                .projekt(projekt)
                .anledning(anledning)
                .build();

            ffEntities.add(ffEntity);
        }

        fastighetsforteckningRepository.saveAll(ffEntities);

        return ffEntities.stream()
            .map(ff -> ff.getId())
            .collect(Collectors.toList());
    }

    @Transactional
    public void setExcluded(UUID projektId, Iterable<UUID> fastighetIds, Boolean exkluderad) {
        var entities = fastighetsforteckningRepository
            .findAllByProjektIdAndFastighetIdIn(projektId, fastighetIds);

        for (var entity: entities) {
            entity.setExcluded(exkluderad);
        }

        fastighetsforteckningRepository.saveAll(entities);
    }

    @Transactional
    public void setExcluded(UUID projektId, UUID fastighetId, Boolean exkluderad) {
        var entity = fastighetsforteckningRepository
                .findByProjektIdAndFastighetId(projektId, fastighetId).orElseThrow(EntityNotFoundException::new);

        entity.setExcluded(exkluderad);
    }

    public List<FastighetsforteckningEntity> getIncludedByProjektId(UUID projektId) {
        return fastighetsforteckningRepository.getIncludedByProjektId(projektId);
    }
}
