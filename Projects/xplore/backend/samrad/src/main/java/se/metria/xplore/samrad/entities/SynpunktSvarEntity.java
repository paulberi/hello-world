package se.metria.xplore.samrad.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import se.metria.xplore.samrad.commons.utils.Auditable;
import se.metria.xplore.samrad.openapi.model.SynpunktStatusDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "synpunkt_svar")
public class SynpunktSvarEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;


    private SynpunktStatusDto status;


    private String rubrik;


    private String innehall;



    @OneToMany(cascade = CascadeType.ALL)
    private List<FilEntity> filEntityList;
}
