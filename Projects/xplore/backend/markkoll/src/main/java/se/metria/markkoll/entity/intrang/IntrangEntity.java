package se.metria.markkoll.entity.intrang;

import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.openapi.model.AvtalstypDto;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "intrang")
@TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class)
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntrangEntity {

    @Id
    @GeneratedValue
    UUID id;

    @Column
    Geometry geom;

    String littera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    ImportVersionEntity version;

    @Column
    String type;

    @Column
    String subtype;
    
    @Column
    Double spanningsniva;

    @Column
    String status;

    @Enumerated(EnumType.STRING)
    AvtalstypDto avtalstyp = AvtalstypDto.INTRANG;
}
