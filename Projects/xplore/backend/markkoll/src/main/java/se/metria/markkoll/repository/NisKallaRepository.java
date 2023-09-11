package se.metria.markkoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.NisKallaEntity;

@Repository
public interface NisKallaRepository extends JpaRepository<NisKallaEntity, String> {
}
