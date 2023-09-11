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
@Table(name = "nyhet")
public class NyhetEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;


    private String rubrik;


    private String innehall;


    private SynpunktStatusDto status;


    @OneToMany(cascade = CascadeType.ALL)
    private List<FilEntity> FilEntityList;


}
