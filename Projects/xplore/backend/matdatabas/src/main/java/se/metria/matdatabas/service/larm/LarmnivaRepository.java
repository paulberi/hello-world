package se.metria.matdatabas.service.larm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.larm.entity.LarmnivaEntity;

@Repository
public interface LarmnivaRepository extends JpaRepository<LarmnivaEntity, Integer> {
    Boolean existsByNamn(String namn);
}
