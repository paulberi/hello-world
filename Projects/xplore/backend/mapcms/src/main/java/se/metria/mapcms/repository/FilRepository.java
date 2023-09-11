package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.mapcms.entity.FilEntity;

import java.util.Optional;
import java.util.UUID;

public interface FilRepository extends JpaRepository<FilEntity, UUID> {


    @Query("SELECT f FROM MeddelandeEntity m " +
            " JOIN m.filer f " +
            " WHERE f.id=:filId " +
            "AND m.id=:meddelandeId " +
            "AND m.fran.dialog.projekt.id=:projektId " +
            "AND m.fran.dialog.projekt.kund.id=:kundId")
    public Optional<FilEntity> getFilForMeddelandeWithId(UUID kundId, UUID projektId, UUID meddelandeId, UUID filId);


    @Query("SELECT f" +
            "     FROM ProjektEntity p " +
            "     JOIN p.filer f" +
            "    WHERE p.id = :projektId" +
            "      AND p.kund.id = :kundId" +
            "       AND f.id=:filId")
    public Optional<FilEntity> getFilForProjekt(UUID kundId, UUID projektId, UUID filId);

}
