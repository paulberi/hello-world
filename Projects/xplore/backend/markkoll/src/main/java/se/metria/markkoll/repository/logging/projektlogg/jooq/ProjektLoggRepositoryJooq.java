package se.metria.markkoll.repository.logging.projektlogg.jooq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.metria.markkoll.entity.logging.projektlogg.ProjektLoggEntity;
import se.metria.markkoll.openapi.model.ProjektLoggFilterDto;

import java.util.List;
import java.util.UUID;

public interface ProjektLoggRepositoryJooq {
    Page<ProjektLoggEntity>
    getProjektLoggPage(UUID projektId, Pageable pageable, List<ProjektLoggFilterDto> filter);
}
