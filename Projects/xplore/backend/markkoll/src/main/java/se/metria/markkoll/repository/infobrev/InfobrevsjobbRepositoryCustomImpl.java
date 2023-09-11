package se.metria.markkoll.repository.infobrev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.InfobrevsjobbEntity;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class InfobrevsjobbRepositoryCustomImpl implements InfobrevsjobbRepositoryCustom {
    @Autowired
    @Lazy
    InfobrevsjobbRepository infobrevsjobbRepository;

    @Override
    @Transactional
    public void incrementGeneratedCounter(UUID infobrevsjobbId) {
        var entity = infobrevsjobbRepository.getOne(infobrevsjobbId);
        entity.setGenerated(entity.getGenerated() + 1);
    }

    @Override
    @Transactional
    public void setInfobrevsjobbStatus(UUID infobrevsjobbId, AvtalsjobbStatusDto status) {
        var entity = infobrevsjobbRepository.getOne(infobrevsjobbId);
        entity.setStatus(status);
    }

    @Override
    @Transactional
    public void setInfobrevsjobbPath(UUID infobrevsjobbId, String path) {
        var entity = infobrevsjobbRepository.getOne(infobrevsjobbId);
        entity.setPath(path);
    }

    @Override
    @Transactional
    public Collection<FastighetEntity> getInfobrevsjobbFastigheter(UUID infobrevsjobbId) {
        var entity = infobrevsjobbRepository.getOne(infobrevsjobbId);

        return entity.getFastigheter();
    }

    @Override
    @Transactional
    public Optional<InfobrevsjobbEntity> getInfobrevsjobb(UUID infobrevsjobbId) {
        return infobrevsjobbRepository.findById(infobrevsjobbId);
    }
}
