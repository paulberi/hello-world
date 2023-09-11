package se.metria.xplore.samrad.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import se.metria.xplore.samrad.commons.utils.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projekt_filer")
@ToString
public class FilEntity  extends Auditable<UUID> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;


    private String filNamn;


    private String mimetyp;


    private String beskrivning;


    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] fil;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] thumbnail;


    private boolean publik;



    public ByteArrayResource getByteArrayResource() {
        return new ByteArrayResource(fil, filNamn);
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
