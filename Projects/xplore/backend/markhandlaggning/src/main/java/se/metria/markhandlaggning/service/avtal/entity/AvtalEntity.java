package se.metria.markhandlaggning.service.avtal.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
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
@Table(name="avtal")
public class AvtalEntity {

    @Id
    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String filnamn;

    @NotNull
    @Column(name="mime_typ")
    private String mimeTyp;

    @CreationTimestamp
    @Column(name="skapad_datum")
    private LocalDateTime skapadDatum;

    @UpdateTimestamp
    @Column(name="uppdaterad_datum")
    private LocalDateTime uppdateradDatum;


    @NotNull
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] fil;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvtalEntity)) return false;
        AvtalEntity that = (AvtalEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
