package se.metria.markkoll.repository.avtal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;
import se.metria.markkoll.openapi.model.AvtalMappstrategiDto;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvtalsjobbRepository extends JpaRepository<AvtalsjobbEntity, UUID> {
    Optional<AvtalsjobbProgress> findAvtalsjobbProgressById(UUID avtalsjobbId);

    @Query(value = "SELECT aj.avtal FROM AvtalsjobbEntity aj WHERE aj.id = :avtalsjobbId")
    List<AvtalEntity> getAvtal(UUID avtalsjobbId);

    @Query("SELECT DISTINCT p.id " +
             "FROM ProjektEntity p " +
             "JOIN AvtalEntity a " +
               "ON a.projekt.id = p.id " +
             "JOIN AvtalsjobbEntity aj " +
               "ON a MEMBER OF aj.avtal " +
            "WHERE aj.id = :avtalsjobbId")
    UUID getProjektId(UUID avtalsjobbId);

    @Modifying(clearAutomatically = true)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE AvtalsjobbEntity a SET a.path = :path WHERE a.id = :avtalsjobbId")
    void setPath(UUID avtalsjobbId, String path);

    @Modifying(clearAutomatically = true)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE AvtalsjobbEntity a SET a.status = :status WHERE a.id = :avtalsjobbId")
    void setAvtalsjobbStatus(UUID avtalsjobbId, AvtalsjobbStatusDto status);

    @Query("SELECT a.status FROM AvtalsjobbEntity a WHERE a.id = :avtalsjobbId")
    AvtalsjobbStatusDto getAvtalsjobbStatus(UUID avtalsjobbId);

    @Modifying(clearAutomatically = true)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE AvtalsjobbEntity a SET a.generated = a.generated + 1 WHERE a.id = :avtalsjobbId")
    void incrementGeneratedCounter(UUID avtalsjobbId);

    @Query("SELECT aj.total FROM AvtalsjobbEntity aj WHERE aj.id = :avtalsjobbId")
    Integer getTotal(UUID avtalsjobbId);

    @Query("SELECT aj.mappstrategi FROM AvtalsjobbEntity aj WHERE aj.id = :avtalsjobbId")
    AvtalMappstrategiDto getMappstrategi(UUID avtalsjobbId);
}
