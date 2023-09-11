package se.metria.matdatabas.service.meddelande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.meddelande.entity.MeddelandeEntity;

@Repository
public interface MeddelandeRepository extends JpaRepository<MeddelandeEntity, Integer> {
}
