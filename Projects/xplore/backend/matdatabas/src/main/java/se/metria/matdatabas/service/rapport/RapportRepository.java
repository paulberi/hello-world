package se.metria.matdatabas.service.rapport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.rapport.entity.RapportSettingsEntity;

@Repository
public interface RapportRepository extends JpaRepository<RapportSettingsEntity, Integer> {
    Boolean existsByNamn(String namn);
}
