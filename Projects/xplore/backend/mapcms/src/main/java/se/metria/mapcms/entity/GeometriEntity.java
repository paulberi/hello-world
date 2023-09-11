package se.metria.mapcms.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "geometri")
public class GeometriEntity {
    @Id
    @GeneratedValue
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projekt_id")
    private ProjektEntity projekt;

    @Column(columnDefinition = "geometry")
    private Geometry geom;

    @Column(columnDefinition = "jsonb")
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
    private Object properties;



}
