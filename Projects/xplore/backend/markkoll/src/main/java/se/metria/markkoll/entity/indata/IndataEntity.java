package se.metria.markkoll.entity.indata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.*;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Geometry;
import se.metria.markkoll.entity.projekt.ProjektEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "indata")
public class IndataEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private Geometry geom;

    @Type(type = "jsonb-node")
    @Column(name = "feature_properties", columnDefinition = "jsonb")
    private JsonNode featureProperties = JsonNodeFactory.instance.objectNode();

    private String layer;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    ProjektEntity projekt;

    @ManyToOne(fetch = FetchType.LAZY)
    KallfilEntity kallfil;
}
