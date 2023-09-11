package se.metria.markhandlaggning.service.avtal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.markhandlaggning.service.avtal.entity.AvtalEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvtalRepository extends JpaRepository<AvtalEntity, UUID> {
    Optional<Filnamn> getFilnamnById(UUID id);
    
    interface Filnamn{
        String getFilnamn();
    }
}


