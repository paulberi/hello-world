package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.mapcms.entity.KundEntity;

import java.util.Optional;
import java.util.UUID;

public interface KundRepository extends JpaRepository<KundEntity, UUID> {

    public Optional<KundEntity> findByVhtNyckel(String vhtNyckel);

    public Optional<KundEntity> findBySlug(String slug);

}
