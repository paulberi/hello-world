package se.metria.markkoll.entity.logging.projektlogg;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.ProjektLoggTypeDto;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "projektlogg")
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
public class ProjektLoggEntity extends Auditable<UUID> {
    @Id
    @Builder.Default
    UUID id = UUID.randomUUID();

    @ManyToOne
    ProjektEntity projekt;

    @Transient
    ProjektLoggTypeDto projektLoggType;
}
