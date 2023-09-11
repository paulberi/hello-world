package se.metria.markkoll.repository.infobrev;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.InfobrevsjobbEntity;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface InfobrevsjobbRepositoryCustom {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void incrementGeneratedCounter(UUID infobrevsjobbId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setInfobrevsjobbStatus(UUID infobrevsjobbId, AvtalsjobbStatusDto status);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setInfobrevsjobbPath(UUID infobrevsjobbId, String path);

    Collection<FastighetEntity> getInfobrevsjobbFastigheter(UUID infobrevsjobbId);

    Optional<InfobrevsjobbEntity> getInfobrevsjobb(UUID infobrevsjobbId);
}
