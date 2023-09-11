package se.metria.markhandlaggning.service.bifogadfil.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
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
@Table(name="bifogad_fil")
public class BifogadfilEntity {

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

    @NotNull
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] fil;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BifogadfilEntity)) return false;
        BifogadfilEntity that = (BifogadfilEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
