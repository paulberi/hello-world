package se.metria.markkoll.entity.logging.avtalslogg;

import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "log_utbetalningsdatum")
public class LogUtbetalningsdatumEntity extends Auditable<UUID> {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    AvtalspartEntity avtalspart;

    LocalDate utbetalningsdatum;

    public LogUtbetalningsdatumEntity(AvtalspartEntity avtalspart, LocalDate utbetalningsdatum) {
        this.avtalspart = avtalspart;
        this.utbetalningsdatum = utbetalningsdatum;
    }
}
