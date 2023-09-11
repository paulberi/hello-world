package se.metria.xplore.kundconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.xplore.kundconfig.entity.AbonnemangEntity;

import java.util.UUID;

@Repository
public interface AbonnemangRepository extends JpaRepository<AbonnemangEntity, UUID>  {
}
