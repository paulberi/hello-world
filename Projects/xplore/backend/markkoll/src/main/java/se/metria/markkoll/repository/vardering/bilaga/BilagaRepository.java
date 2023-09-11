package se.metria.markkoll.repository.vardering.bilaga;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.BilagaEntity;

import java.util.List;
import java.util.UUID;

public interface BilagaRepository extends JpaRepository<BilagaEntity, UUID> {
    List<BilagaEntity> findAllByVarderingsprotokollId(UUID vpId);
}
