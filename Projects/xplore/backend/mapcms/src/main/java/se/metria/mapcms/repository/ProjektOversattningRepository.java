package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.mapcms.entity.ProjektOversattningEntity;

import java.util.Optional;
import java.util.UUID;

public interface ProjektOversattningRepository extends JpaRepository<ProjektOversattningEntity, String> {

    @Query("SELECT p FROM ProjektOversattningEntity p WHERE p.projekt.id = :projektId and p.sprakkod = :sprakkod")
    public Optional<ProjektOversattningEntity> getByprojektIdAndSprakkod(UUID projektId, String sprakkod);


}
