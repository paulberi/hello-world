package se.metria.markkoll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.projekt.AvtalsinstallningarEntity;
import se.metria.markkoll.openapi.model.AvtalMappstrategiDto;
import se.metria.markkoll.openapi.model.BerakningAbel07Dto;
import se.metria.markkoll.openapi.model.BerakningRevDto;

import java.util.UUID;

public interface AvtalsinstallningarRepository extends JpaRepository<AvtalsinstallningarEntity, UUID>{
    @Query("SELECT ai.mappstrategi FROM AvtalsinstallningarEntity ai WHERE ai.projekt.id = :projektId")
    AvtalMappstrategiDto getMappstrategi(UUID projektId);

    @Query("SELECT ai" +
        "     FROM AvtalsinstallningarEntity ai" +
        "     JOIN ai.projekt p" +
        "     JOIN ImportVersionEntity iv" +
        "       ON iv.projekt.id = p.id" +
        "    WHERE iv.id = :versionId")
    AvtalsinstallningarEntity findByVersionId(UUID versionId);

    @Query("SELECT ai.berakningAbel07 FROM AvtalsinstallningarEntity ai WHERE ai.projekt.id = :projektId")
    BerakningAbel07Dto getBerakningAbel07(UUID projektId);

    @Query("SELECT ai.berakningRev FROM AvtalsinstallningarEntity ai WHERE ai.projekt.id = :projektId")
    BerakningRevDto getBerakningRev(UUID projektId);
}
