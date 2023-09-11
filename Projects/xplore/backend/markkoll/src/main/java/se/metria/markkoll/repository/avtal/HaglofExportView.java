package se.metria.markkoll.repository.avtal;

import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.openapi.model.AgartypDto;

import java.util.UUID;

public interface HaglofExportView {
    UUID getAvtalspartId();
    UUID getAvtalId();

    String getLan();
    String getKommun();
    String getFastighetsnummer();
    String getFastighet();
    String getAndel();
    PersonEntity getPerson();
    String getSokId();
    AgartypDto getAgartyp();
}
