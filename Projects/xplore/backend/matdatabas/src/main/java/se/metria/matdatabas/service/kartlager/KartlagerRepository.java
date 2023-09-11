package se.metria.matdatabas.service.kartlager;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.matdatabas.service.kartlager.entity.KartlagerEntity;

import java.util.List;

public interface KartlagerRepository extends JpaRepository<KartlagerEntity, Integer> {
	List<KartlagerEntity> findAllByOrderByOrdningDesc();
}
