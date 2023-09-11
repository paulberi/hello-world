package se.metria.markhandlaggning.service.projekt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.markhandlaggning.service.projekt.entity.ProjektEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjektRepository extends JpaRepository<ProjektEntity, UUID> {

    void removeByIdIn(List<UUID> ids);

    Page<ProjektEntity> findAllByIdExistsOrderBySkapadDatumDesc(Pageable pageable);

}
