package se.metria.xplore.kundconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.xplore.kundconfig.entity.PermissionsEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionsRepository extends JpaRepository<PermissionsEntity, UUID> {
    List<PermissionsEntity> findByUserIdAndProdukt(String userId, String produkt);
}
