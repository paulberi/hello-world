package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.mapcms.entity.GeometriEntity;

import java.util.List;
import java.util.UUID;

public interface GeometriRepository extends JpaRepository<GeometriEntity, UUID> {

    @Query("select g from GeometriEntity g where g.projekt.id = :projectId and g.projekt.kund.id = :kundID")
    List<GeometriEntity> findAll(UUID kundID, UUID projectId);

    @Query("SELECT g FROM GeometriEntity g WHERE g.projekt.id = :projektId " +
            "AND g.id = :geometriId AND g.projekt.kund.id = :kundId")
    GeometriEntity findGeometri(UUID kundId, UUID projektId, UUID geometriId);

}