package se.metria.markkoll.entity.logging.projektlogg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import se.metria.markkoll.entity.InfobrevsjobbEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "projektlogg_infobrevhandelse")
@SuperBuilder
public class InfobrevhandelseEntity extends ProjektLoggEntity {
    @OneToOne
    InfobrevsjobbEntity infobrevsjobb;

    Integer antalFastigheter;
}
