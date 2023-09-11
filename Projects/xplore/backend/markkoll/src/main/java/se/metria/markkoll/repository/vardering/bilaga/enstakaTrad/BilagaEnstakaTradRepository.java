package se.metria.markkoll.repository.vardering.bilaga.enstakaTrad;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.enstakatrad.BilagaEnstakaTradEntity;

import java.util.UUID;

public interface BilagaEnstakaTradRepository
    extends JpaRepository<BilagaEnstakaTradEntity, UUID> {
}
