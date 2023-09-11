package se.metria.markkoll.entity;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Table(name = "fil")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
public class FilEntity extends Auditable<UUID>  {

    @Id
    @GeneratedValue
    UUID id;

    @NotNull
    String filnamn;

    @NotNull
    @Column(name="mime_typ")
    String mimeTyp;

    @NotNull
    @Column(name="kund_id")
    String kundId;

    @NotNull
    @Type(type="org.hibernate.type.BinaryType")
    byte[] fil;

    public ByteArrayResource getByteArrayResource() {
        return new ByteArrayResource(fil, filnamn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilEntity)) return false;
        FilEntity that = (FilEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
