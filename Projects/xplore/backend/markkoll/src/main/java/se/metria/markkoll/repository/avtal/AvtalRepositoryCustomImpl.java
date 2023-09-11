package se.metria.markkoll.repository.avtal;

import org.springframework.context.annotation.Lazy;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.openapi.model.FastighetsfilterDto;
import se.metria.markkoll.repository.fastighet.FastighetRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AvtalRepositoryCustomImpl implements AvtalRepositoryCustom {

    AvtalRepository avtalRepository;
    FastighetRepository fastighetRepository;

    public AvtalRepositoryCustomImpl(@Lazy AvtalRepository avtalRepository, FastighetRepository fastighetRepository) {
        this.avtalRepository = avtalRepository;
        this.fastighetRepository = fastighetRepository;
    }
    /*
     * TODO: JOOQ-koden i fastigheterFiltered kommer egentligen behövas refaktoreras lite så det är avtal man hämtar
     *  istället. Det här blir något av en quick fix.
     * */
    @Override
    public List<AvtalEntity> avtalFiltered(UUID projektId, FastighetsfilterDto filter) {
        List<UUID> fastigheter;

        if (filter.getFastighetsIds() != null && !filter.getFastighetsIds().isEmpty()) {
            fastigheter = filter.getFastighetsIds();
        } else {
            fastigheter = fastighetRepository.registerenheterFiltered(projektId, filter).stream()
                .map(f -> f.getId())
                .collect(Collectors.toList());
        }

        return avtalRepository.findAllByProjektIdAndFastighetIdIn(projektId, fastigheter);
    }

    public List<UUID> avtalIdsFiltered(UUID projektId, FastighetsfilterDto filter) {
        List<UUID> fastigheter;

        if (filter.getFastighetsIds() != null && !filter.getFastighetsIds().isEmpty()) {
            fastigheter = filter.getFastighetsIds();
        } else {
            fastigheter = fastighetRepository.registerenheterFiltered(projektId, filter).stream()
                .map(FastighetEntity::getId)
                .collect(Collectors.toList());
        }

        return avtalRepository.findAllIdsByProjektIdAndFastighetIdIn(projektId, fastigheter);
    }
}
