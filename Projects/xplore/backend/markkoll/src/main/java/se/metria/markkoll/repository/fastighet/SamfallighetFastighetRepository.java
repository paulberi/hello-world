package se.metria.markkoll.repository.fastighet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.samfallighet.SamfallighetFastighetId;
import se.metria.markkoll.entity.samfallighet.SamfallighetIngaendeFastighetEntity;

import java.util.UUID;

@Repository
public interface SamfallighetFastighetRepository extends JpaRepository<SamfallighetIngaendeFastighetEntity, SamfallighetFastighetId> {
    void deleteAllBySamfallighetId(UUID samfallighetId);
}
