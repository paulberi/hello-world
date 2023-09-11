package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.mapcms.entity.PersonEntity;

public interface PersonRepository extends JpaRepository<PersonEntity, String> {
}
