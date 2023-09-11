package se.metria.markkoll.repository.fil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.FilEntity;

import java.util.Set;
import java.util.UUID;

@Repository
public interface FilRepository extends JpaRepository<FilEntity, UUID> {
    @Query("SELECT f.filnamn FROM FilEntity f WHERE f.id = :filId")
    String getFilnamn(UUID filId);

    @Query("SELECT f " +
             "FROM FilEntity f " +
             "JOIN BilagaEntity b " +
               "ON b.fil.id = f.id " +
            "WHERE b.varderingsprotokoll.avtal.id = :avtalId")
    Set<FilEntity> getFiler(UUID avtalId);
}
