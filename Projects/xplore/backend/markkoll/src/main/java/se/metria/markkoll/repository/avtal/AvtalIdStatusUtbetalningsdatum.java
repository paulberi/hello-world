package se.metria.markkoll.repository.avtal;

import se.metria.markkoll.openapi.model.AvtalsstatusDto;

import java.time.LocalDate;
import java.util.UUID;

public interface AvtalIdStatusUtbetalningsdatum {
    UUID getId();
    AvtalsstatusDto getAvtalsstatus();
    LocalDate getUtbetalningsdatum();
}
