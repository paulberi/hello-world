package se.metria.mapcms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import se.metria.mapcms.commons.utils.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
@Table(name = "fil")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class FilEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    private String mimetyp;

    @NonNull
    private String filnamn;

    private String beskrivning;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] fil;
}
