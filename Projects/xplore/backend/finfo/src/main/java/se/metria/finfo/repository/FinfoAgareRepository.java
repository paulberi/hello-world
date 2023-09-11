package se.metria.finfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.finfo.entity.agare.FinfoAgareEntity;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Repository
public interface FinfoAgareRepository extends JpaRepository<FinfoAgareEntity, UUID> {
    Set<FinfoAgareEntity> findAllByFastighetIdIn(Collection<UUID> fastighetIds);
}