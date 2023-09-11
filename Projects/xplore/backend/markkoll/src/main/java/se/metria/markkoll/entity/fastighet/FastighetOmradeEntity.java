package se.metria.markkoll.entity.fastighet;


import lombok.*;
import org.locationtech.jts.geom.Geometry;
import se.metria.markkoll.entity.intrang.OmradesintrangEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "fastighet_omrade")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@IdClass(OmradeId.class)
@ToString(exclude={"geom", "omradesIntrang"})
@EqualsAndHashCode(exclude={"omradesIntrang"})
public class FastighetOmradeEntity implements Serializable {
    @Id
    @Column(name = "fastighet_id")
    UUID fastighetId;

    @Id
    @Column
    Long omradeNr;

    @Column
    Geometry geom;

    @OneToMany(mappedBy = "fastighetomrade", cascade = CascadeType.PERSIST)
    Set<OmradesintrangEntity> omradesIntrang = new HashSet<>();

    public OmradeId getOmradeId(){ return new OmradeId(fastighetId, omradeNr);}

    public void addIntrang(OmradesintrangEntity omradesintrangEntity) {
        omradesIntrang.add(omradesintrangEntity);
        omradesintrangEntity.setFastighetomrade(this);
    }
}

