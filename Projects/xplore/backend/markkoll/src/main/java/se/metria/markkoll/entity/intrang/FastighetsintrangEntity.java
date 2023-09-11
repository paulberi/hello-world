package se.metria.markkoll.entity.intrang;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "fastighetsintrang")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FastighetsintrangEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "fastighet_id")
    FastighetEntity fastighet;

    @ManyToOne
    @JoinColumn(name = "version_id")
    ImportVersionEntity version;

    Geometry geom;

    @Column(name = "type")
    String typ;

    @Column(name = "subtype")
    String subtyp;

    String status;

    @Enumerated(EnumType.STRING)
    AvtalstypDto avtalstyp;
}
