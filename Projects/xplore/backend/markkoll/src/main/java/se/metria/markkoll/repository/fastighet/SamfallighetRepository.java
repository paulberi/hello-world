package se.metria.markkoll.repository.fastighet;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.samfallighet.SamfallighetEntity;

import java.util.UUID;

public interface SamfallighetRepository extends JpaRepository<SamfallighetEntity, UUID> {
}
