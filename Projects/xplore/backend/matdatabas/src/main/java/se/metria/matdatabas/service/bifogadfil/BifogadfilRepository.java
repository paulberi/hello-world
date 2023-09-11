package se.metria.matdatabas.service.bifogadfil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.metria.matdatabas.service.bifogadfil.entity.BifogadfilEntity;
import se.metria.matdatabas.service.bifogadfil.entity.BifogadfilInfoView;

@Repository
interface BifogadfilRepository extends JpaRepository<BifogadfilEntity, UUID> {

	Optional<BifogadfilInfoView> findBifogadfilInfoById(UUID id);

	List<BifogadfilInfoView> findAllBifogadfilInfoByIdIn(Collection<UUID> ids);

	void removeByIdIn(List<UUID> ids);
}
