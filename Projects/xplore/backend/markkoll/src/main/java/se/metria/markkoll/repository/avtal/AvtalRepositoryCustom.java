package se.metria.markkoll.repository.avtal;

import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.openapi.model.FastighetsfilterDto;

import java.util.List;
import java.util.UUID;

public interface AvtalRepositoryCustom {
    List<AvtalEntity> avtalFiltered(UUID projektId, FastighetsfilterDto filter);

    List<UUID> avtalIdsFiltered(UUID projektId, FastighetsfilterDto filter);
}
