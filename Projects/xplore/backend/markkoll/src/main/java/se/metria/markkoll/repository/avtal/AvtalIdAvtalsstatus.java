package se.metria.markkoll.repository.avtal;

import se.metria.markkoll.openapi.model.AvtalsstatusDto;

import java.util.UUID;

public interface AvtalIdAvtalsstatus {
    UUID getId();
    AvtalsstatusDto getAvtalsstatus();
}
