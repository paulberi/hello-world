package se.metria.markkoll.repository.vardering;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.elnat.ElnatLedningSkogsmarkEntity;

import java.util.UUID;

public interface LedningSkogsmarkRepository extends JpaRepository<ElnatLedningSkogsmarkEntity, UUID> {
    void deleteAllByVarderingsprotokollId(UUID varderingsprotokollId);
}
