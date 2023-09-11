package se.metria.xplore.samrad.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import se.metria.xplore.samrad.commons.utils.Auditable;
import se.metria.xplore.samrad.openapi.model.SamradStatusDto;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projekt")
@ToString
public class ProjektEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NonNull
    private String rubrik;


    private String ingress;


    private String brodtext;


    @OneToMany(cascade = CascadeType.ALL)
    private List<FilEntity> filEntityList;


    private String slug;


    private SamradStatusDto status;


    @OneToMany(cascade = CascadeType.ALL)
    private List<SynpunktEntity> synpunktEntityList;


    @OneToMany(cascade = CascadeType.ALL)
    private List<NyhetEntity> nyhetEntityList;
}
