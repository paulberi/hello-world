package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.mapcms.entity.MeddelandeEntity;

import java.util.Optional;
import java.util.UUID;

public interface MeddelandeRepository extends JpaRepository<MeddelandeEntity, UUID> {
    @Query("SELECT m FROM MeddelandeEntity m where m.id=:meddelandeId and m.fran.dialog.id=:dialogId and m.fran.dialog.projekt.id=:projektId and m.fran.dialog.projekt.kund.id=:kundId")
    public Optional<MeddelandeEntity> getMeddelandeWithId(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId);
}
