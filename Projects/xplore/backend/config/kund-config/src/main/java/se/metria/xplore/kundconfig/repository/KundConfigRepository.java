package se.metria.xplore.kundconfig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.xplore.kundconfig.entity.KundEntity;

@Repository
public interface KundConfigRepository extends JpaRepository<KundEntity, String> {
    Page<KundEntity> findAll(Pageable pageable);
}
