package se.metria.markkoll.entity.logging.projektlogg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import se.metria.markkoll.openapi.model.ManuellFastighethandelseTypDto;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "projektlogg_manuell_fastighethandelse")
@SuperBuilder
public class ManuellFastighethandelseEntity extends ProjektLoggEntity {
    String fastighetsbeteckning;

    ManuellFastighethandelseTypDto typ;
}
