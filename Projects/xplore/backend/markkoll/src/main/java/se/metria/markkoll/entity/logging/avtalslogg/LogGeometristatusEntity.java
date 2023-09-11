package se.metria.markkoll.entity.logging.avtalslogg;

import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "log_geometristatus")
public class LogGeometristatusEntity extends Auditable<UUID> {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "avtal_id")
    AvtalEntity avtal;

    GeometristatusDto geometristatus;

    public LogGeometristatusEntity(AvtalEntity avtal, GeometristatusDto geometristatus) {
        this.avtal = avtal;
        this.geometristatus = geometristatus;
    }
}
