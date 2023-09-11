package se.metria.markkoll.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.openapi.model.GeometristatusDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "avtal_geometristatus")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeometristatusEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "avtal_id")
    AvtalEntity avtal;

    @ManyToOne
    @JoinColumn(name = "version_id")
    ImportVersionEntity version;

    GeometristatusDto geometristatus;

    public GeometristatusEntity(GeometristatusDto geometristatus, AvtalEntity avtal) {
        this.geometristatus = geometristatus;
        this.avtal = avtal;
    }
}
