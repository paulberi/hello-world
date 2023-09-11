package se.metria.markhandlaggning.service.avtal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AvtalService {

    @Value("${markhandlaggning.avtalsurl}")
    private String avtalsurl;

    private final AvtalRepository repository;

    public AvtalService(AvtalRepository repository){
        this.repository = repository;
    }

    @Transactional
    public void deleteAvtal(UUID id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public String getUrl(UUID id){
        var filnamn = repository.getFilnamnById(id);
        return filnamn.map(value -> avtalsurl + value.getFilnamn()).orElse(null);
    }
}
