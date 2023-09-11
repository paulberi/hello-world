package se.metria.markhandlaggning.service.projekt.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="projekt")
public class ProjektEntity {

    @Id
    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(name="projektnr")
    private String projektnr;

    @NotNull
    @Column(name="avtalstyp")
    private String avtalstyp;

    @Column(name="status")
    private String status;

    @NotNull
    @Column(name="shape_id")
    private UUID shapeId;

    @NotNull
    @Column(name="markagarlista_id")
    private UUID markagarlistaId;

    @Column(name="avtals_id")
    private UUID avtalsId;

    @CreationTimestamp
    @Column(name="skapad_datum")
    private LocalDateTime skapadDatum;

    @UpdateTimestamp
    @Column(name="uppdaterad_datum")
    private LocalDateTime uppdateradDatum;

    @Column(name="felmeddelande")
    private String felmeddelande;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjektEntity)) return false;
        ProjektEntity that = (ProjektEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
