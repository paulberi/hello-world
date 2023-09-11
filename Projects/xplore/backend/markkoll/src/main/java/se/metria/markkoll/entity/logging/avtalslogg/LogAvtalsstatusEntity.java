package se.metria.markkoll.entity.logging.avtalslogg;

import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "log_avtalsstatus")
public class LogAvtalsstatusEntity extends Auditable<UUID> {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "avtalspart_id")
    AvtalspartEntity avtalspart;

    AvtalsstatusDto avtalsstatus;

    public LogAvtalsstatusEntity(AvtalspartEntity avtalspart, AvtalsstatusDto avtalsstatus) {
        this.avtalspart = avtalspart;
        this.avtalsstatus = avtalsstatus;
    }
}
