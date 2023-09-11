package se.metria.markkoll.service.markagare;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UtbetalningEvent {
    private Collection<UUID> updatedAvtalspartIds;
    private LocalDate utbetalningsdatum;
}
