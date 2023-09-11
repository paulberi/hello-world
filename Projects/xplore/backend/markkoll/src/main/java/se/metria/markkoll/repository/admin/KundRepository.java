package se.metria.markkoll.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.admin.KundEntity;

import java.util.List;
import java.util.UUID;

public interface KundRepository extends JpaRepository<KundEntity, String> {
    @Query("select k.id from KundEntity k")
    List<String> getAllKundIds();

    @Query("SELECT k " +
        "     FROM KundEntity k " +
        "     JOIN ProjektEntity p " +
        "       ON p.kundId = k.id " +
        "    WHERE p.id = :projektId")
    KundEntity getKundByProjektId(UUID projektId);
}
