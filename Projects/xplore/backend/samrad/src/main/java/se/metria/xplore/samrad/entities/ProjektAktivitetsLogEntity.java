package se.metria.xplore.samrad.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import se.metria.xplore.samrad.commons.config.SamradConfiguration;
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
@Table(name = "projekt_aktivitets_Log")
public class ProjektAktivitetsLogEntity  extends Auditable<UUID> {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String andringar;

}
