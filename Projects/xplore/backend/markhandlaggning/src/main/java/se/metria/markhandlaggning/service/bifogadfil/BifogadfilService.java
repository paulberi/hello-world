package se.metria.markhandlaggning.service.bifogadfil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markhandlaggning.service.bifogadfil.dto.Bifogadfil;
import se.metria.markhandlaggning.service.bifogadfil.dto.SaveBifogadfil;
import se.metria.markhandlaggning.service.bifogadfil.entity.BifogadfilEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BifogadfilService {

    private BifogadfilRepository repository;

    public BifogadfilService(BifogadfilRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Bifogadfil> getBifogadfil(UUID id) {
        return repository.findById(id)
                .map(Bifogadfil::fromEntity);
    }
    @Transactional
    public void deleteBifogadfil(UUID id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
    @Transactional
    public void deleteBifogadfilByIds(List<UUID> ids){
        if (repository.existsByIdIn(ids)) {
            repository.deleteByIdIn(ids);
        }
    }

    @Transactional
    public Bifogadfil createBifogadfil(SaveBifogadfil saveBifogadfil) throws IOException {
        return Bifogadfil.fromEntity(persist(saveBifogadfil.toEntity()));
    }
    
    private BifogadfilEntity persist(BifogadfilEntity entity) throws IOException {
        return repository.saveAndFlush(entity);
    }
}
