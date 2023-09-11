package se.metria.xplore.samrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.xplore.samrad.entities.KundEntity;

import java.util.Optional;
import java.util.UUID;

public interface KundRepository extends JpaRepository<KundEntity, String>{

    Optional<KundEntity> findBySlug(String slug);
}
