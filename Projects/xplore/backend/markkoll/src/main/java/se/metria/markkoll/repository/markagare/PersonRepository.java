package se.metria.markkoll.repository.markagare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.markagare.PersonEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {
    Optional<PersonEntity> findByPersonnummerAndKundId(String personnummer, String kundId);

    Optional<PersonEntity>
    findByNamnAndAdressAndPostnummerAndKundId(String namn, String adress, String postnummer, String kundId);

    @Query("SELECT av.kontaktperson.markagare.person " +
        "     FROM AvtalEntity av " +
        "    WHERE av.id = :avtalId")
    Optional<PersonEntity> getKontaktpersonForAvtal(UUID avtalId);
}
