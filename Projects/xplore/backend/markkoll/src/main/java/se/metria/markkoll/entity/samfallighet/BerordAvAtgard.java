package se.metria.markkoll.entity.samfallighet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.openapi.finfo.model.FinfoAtgardstypDto;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BerordAvAtgard {
    @Enumerated(EnumType.STRING)
    private FinfoAtgardstypDto atgardstyp;
    private LocalDate registreringsdatum;
    private String aktbeteckning;
    private String atgard;
}
