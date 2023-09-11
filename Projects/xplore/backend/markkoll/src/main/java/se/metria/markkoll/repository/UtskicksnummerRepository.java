package se.metria.markkoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.UtskicksnummerEntity;

public interface UtskicksnummerRepository extends JpaRepository<UtskicksnummerEntity, String> {
}
