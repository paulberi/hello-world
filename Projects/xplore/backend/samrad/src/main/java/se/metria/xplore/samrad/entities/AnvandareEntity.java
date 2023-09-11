package se.metria.xplore.samrad.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import se.metria.xplore.samrad.commons.utils.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anvandare")
public class AnvandareEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;


    private String namn;


    private String epost;


    private String roll;


    @DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime senastInloggad;


    private boolean aktiv;

}
