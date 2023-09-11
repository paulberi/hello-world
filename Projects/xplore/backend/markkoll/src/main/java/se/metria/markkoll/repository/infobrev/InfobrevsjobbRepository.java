package se.metria.markkoll.repository.infobrev;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.InfobrevsjobbEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfobrevsjobbRepository extends JpaRepository<InfobrevsjobbEntity, UUID>, InfobrevsjobbRepositoryCustom {
    Optional<InfobrevsjobbProgress> findInfobrevsjobbProgressById(UUID infobrevsjobbId);

    @Query("SELECT ibj.projektId FROM InfobrevsjobbEntity ibj WHERE ibj.id = :infobrevsjobbId")
    UUID getProjektId(UUID infobrevsjobbId);

    @Query("SELECT ibj.total FROM InfobrevsjobbEntity ibj WHERE ibj.id = :infobrevsjobbId")
    Integer getTotal(UUID infobrevsjobbId);
}
