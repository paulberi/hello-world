package se.metria.markkoll.service.markagare;

import lombok.AllArgsConstructor;
import lombok.Data;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AvtalsstatusUpdatedEvent {
    private Collection<UUID> updatedAvtalspartIds;
    private AvtalsstatusDto avtalsstatus;
}
