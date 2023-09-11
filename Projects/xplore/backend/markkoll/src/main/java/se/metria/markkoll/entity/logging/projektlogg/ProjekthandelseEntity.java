package se.metria.markkoll.entity.logging.projektlogg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.ProjekthandelseTypDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "projektlogg_projekthandelse")
@SuperBuilder
public class ProjekthandelseEntity extends ProjektLoggEntity {
    @ManyToOne
    ProjektEntity projekt;

    @Column
    ProjekthandelseTypDto projekthandelsetyp;
}
