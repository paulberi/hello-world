package se.metria.matdatabas.service.definitionmatningstyp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.definitionmatningstyp.entity.DefinitionMatningstypEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DefinitionMatningstypRepository extends JpaRepository<DefinitionMatningstypEntity, Integer> {
	List<DefinitionMatningstypEntity> findByMatobjektTyp(Short matobjektTyp);
	boolean existsByNamnAndMatobjektTyp(String namn, Short matobjektTyp);
	Optional<DefinitionMatningstypEntity> getDefinitionMatningstypEntityByNamn(String namn);
	Optional<DefinitionMatningstypEntity> getDefinitionMatningstypEntityByBerakningstyp(Berakningstyp berakningstyp);
}
