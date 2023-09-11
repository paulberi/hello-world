package se.metria.markkoll.entity.intrang;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "omradesintrang")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OmradesintrangEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "fastighet_id"),
            @JoinColumn(name = "omrade_nr")
    })
    FastighetOmradeEntity fastighetomrade;

    String littera;

    @ManyToOne
    @JoinColumn(name = "version_id")
    ImportVersionEntity version;

    Geometry geom;

    String type;

    String subtype;

    Double spanningsniva;

    String status;

    @Enumerated(EnumType.STRING)
    AvtalstypDto avtalstyp;
}
