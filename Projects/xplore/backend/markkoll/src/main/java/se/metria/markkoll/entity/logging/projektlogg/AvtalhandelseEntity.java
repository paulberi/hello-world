package se.metria.markkoll.entity.logging.projektlogg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "projektlogg_avtalhandelse")
@SuperBuilder
public class AvtalhandelseEntity extends ProjektLoggEntity {
    @OneToOne
    AvtalsjobbEntity avtalsjobb;

    Integer antalFastigheter;
}
