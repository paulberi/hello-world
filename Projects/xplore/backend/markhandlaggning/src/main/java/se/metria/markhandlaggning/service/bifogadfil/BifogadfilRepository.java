package se.metria.markhandlaggning.service.bifogadfil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.markhandlaggning.service.bifogadfil.entity.BifogadfilEntity;

import java.util.List;
import java.util.UUID;

@Repository
interface BifogadfilRepository extends JpaRepository<BifogadfilEntity, UUID> {

    void deleteByIdIn(List<UUID> ids);
    boolean existsByIdIn(List<UUID> ids);

}
