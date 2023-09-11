package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.mapcms.entity.AnvandareEntity;

import java.util.Optional;
import java.util.UUID;

public interface AnvandareRepository extends JpaRepository<AnvandareEntity, UUID> {

    @Query("SELECT a FROM AnvandareEntity  a where a.epost=:epost and a.kundId=:kundId")
    public Optional<AnvandareEntity> findByEmail(String epost, UUID kundId);
}
