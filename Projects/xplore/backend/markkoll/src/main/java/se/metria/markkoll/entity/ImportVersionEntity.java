package se.metria.markkoll.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.metria.markkoll.entity.intrang.FastighetsintrangEntity;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.entity.intrang.OmradesintrangEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "importversion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@EqualsAndHashCode(exclude = {
        "omradesintrang", "fastighetsintrang", "projekt", "isCurrentVersion", "geometristatus"
})
@ToString(exclude = {"omradesintrang", "fastighetsintrang", "projekt", "isCurrentVersion", "geometristatus"})
@EntityListeners(AuditingEntityListener.class)
public class ImportVersionEntity {
    @Id
    @GeneratedValue
    UUID id;

    @NotNull
    String filnamn;

    @NotNull
    Double buffert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projekt_id")
    ProjektEntity projekt;

    @CreatedDate
    @Column(name = "skapad_datum")
    LocalDateTime skapadDatum;

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
    Set<IntrangEntity> intrang = new HashSet<>();

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
    Set<OmradesintrangEntity> omradesintrang = new HashSet<>();

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
    Set<FastighetsintrangEntity> fastighetsintrang = new HashSet<>();

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
    Set<GeometristatusEntity> geometristatus = new HashSet<>();

    public void addGeometristatus(GeometristatusEntity geometristatusEntity) {
        geometristatus.add(geometristatusEntity);
        geometristatusEntity.setVersion(this);
    }

    public void addIntrang(IntrangEntity intrangEntity) {
        intrang.add(intrangEntity);
        intrangEntity.setVersion(this);
    }

    public void addIntrang(Set<IntrangEntity> intrangSet) {
        for(var intrang: intrangSet) {
            addIntrang(intrang);
        }
    }

    public void addOmradesintrang(OmradesintrangEntity omradesintrangEntity) {
        omradesintrang.add(omradesintrangEntity);
        omradesintrangEntity.setVersion(this);
    }

    public void addFastighetsintrang(FastighetsintrangEntity fastighetsintrangEntity) {
        fastighetsintrang.add(fastighetsintrangEntity);
        fastighetsintrangEntity.setVersion(this);
    }
}
