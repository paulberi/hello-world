package se.metria.markkoll.entity.samradEntities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import se.metria.markkoll.entity.projekt.ProjektEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "samrad_projekt_i_markkoll")
public class SamradProjektidEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String samradId;

    @OneToOne
    private ProjektEntity markkollProjekt;
}
