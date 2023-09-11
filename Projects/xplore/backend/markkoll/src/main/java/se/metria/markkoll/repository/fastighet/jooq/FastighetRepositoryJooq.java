package se.metria.markkoll.repository.fastighet.jooq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.openapi.model.*;

import java.util.List;
import java.util.UUID;

public interface FastighetRepositoryJooq {
    List<FastighetEntity>
    registerenheterFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter);

    Integer
    registerenheterCountFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter);

    Page<FastighetDto>
    fastighetPageFiltered(UUID projektId, Pageable pageable, FastighetsfilterDto fastighetsfilter);

    Page<FastighetDto>
    samfallighetPageFiltered(UUID projektId, Pageable pageable, FastighetsfilterDto fastighetsfilter);

    List<FastighetEntity>
    fastigheterFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter);

    List<FastighetEntity>
    fastigheterAndSamfalligheterFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter);

    List<FastighetEntity>
    samfalligheterFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter);

    List<String> getIngaendeFastigheterBeteckningar(UUID samfallighetsId);

    // Intrång
    List<IntrangDto> findIntrangInfoForFastighet(UUID fastighetId, UUID projektId);

    // TODO: Ska ingå i avtal det här
    AvtalsstatusDto fastighetAvtalsstatus(UUID fastighetId, UUID projektId);
    Integer getErsattningForFastighet(UUID fastighetId, UUID projektId);
    String getAnteckningForFastighet(UUID fastighetId, UUID projektId);
    Integer setFastighetsInfoForProjekt(UUID fastighetId, UUID projektId, FastighetsProjektInfoDto info);
}
