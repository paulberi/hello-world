package se.metria.markkoll.repository.vardering.bilaga.akernorm74;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.vardering.bilaga.akernorm74.Bilaga74ArsAkernormEntity;

import java.util.UUID;

public interface Bilaga74ArsAkernorm
    extends JpaRepository<Bilaga74ArsAkernormEntity, UUID> {
}
