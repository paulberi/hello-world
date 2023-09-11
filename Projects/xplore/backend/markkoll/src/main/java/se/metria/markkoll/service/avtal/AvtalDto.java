package se.metria.markkoll.service.avtal;

import lombok.Data;
import se.metria.markkoll.openapi.model.TillvaratagandeTypDto;

import java.util.UUID;

@Data
public class AvtalDto {
    private UUID id;

    private int ersattning;

    private int egetTillvaratagande;

    private boolean skogsfastighet;

    private TillvaratagandeTypDto tillvaratagandeTyp;
}
