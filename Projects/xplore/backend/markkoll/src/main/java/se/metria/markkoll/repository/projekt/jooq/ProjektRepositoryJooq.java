package se.metria.markkoll.repository.projekt.jooq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.metria.markkoll.entity.projekt.ProjektEntity;

import java.util.Collection;
import java.util.UUID;

public interface ProjektRepositoryJooq {
    Page<ProjektEntity> pageFiltered(Pageable pageable, String searchFilter, Collection<UUID> projektFilter);
}
