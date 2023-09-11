package se.metria.matdatabas.service.matning;

import java.time.LocalDateTime;
import java.util.List;

import se.metria.matdatabas.service.definitionmatningstyp.entity.DefinitionMatningstypEntity;
import se.metria.matdatabas.service.matning.entity.MatningEntity;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;

public interface MatningRepositoryCustom {

	public List<MatningEntity> findByOtherMatningstyp(Matningstyp matningstyp, DefinitionMatningstypEntity otherMatningstyp, LocalDateTime from, LocalDateTime to);
}
