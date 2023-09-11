package se.metria.markhandlaggning.service.projekt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markhandlaggning.service.projekt.dto.Projekt;
import se.metria.markhandlaggning.service.projekt.dto.SaveProjekt;
import se.metria.markhandlaggning.service.projekt.entity.ProjektEntity;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProjektService {

    private ProjektRepository repository;

    public ProjektService(ProjektRepository repository){
        this.repository = repository;
    }

    @Transactional
    public ProjektEntity getProjekt(UUID id) {
        return repository.getOne(id);
    }

    @Transactional
    public Page<Projekt> getAllaProjekt(int page, int pagesize) {
        var pageRequest = PageRequest.of(page, pagesize, Sort.Direction.DESC,"skapadDatum");
        return repository.findAll(pageRequest).map(Projekt::fromEntity);
    }

    @Transactional
    public Projekt createProjekt(SaveProjekt saveProjekt) throws IOException {
        return Projekt.fromEntity(persist(saveProjekt.toEntity()));
    }
    private ProjektEntity persist(ProjektEntity entity) throws IOException {
        return repository.saveAndFlush(entity);
    }

    @Transactional
    public void deleteProjekt(UUID id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    @Transactional
    public ProjektEntity updateProjektStatus(UUID id, String status){
        ProjektEntity projektToUpdate = this.repository.getOne(id);
        projektToUpdate.setStatus(status);
        projektToUpdate.setFelmeddelande(null);
        return this.repository.save(projektToUpdate);
    }
}
