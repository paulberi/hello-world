package se.metria.markkoll.repository.vardering.bilaga.enstakaTrad;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.enstakatrad.EnstakaTradEntity;

import java.util.UUID;

public interface EnstakaTradRepository extends JpaRepository<EnstakaTradEntity, UUID> {
}
