package se.metria.xplore.samrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.xplore.samrad.entities.NyhetEntity;

import java.util.UUID;

public interface NyhetsRepository extends JpaRepository<NyhetEntity, UUID> {
}
