package se.metria.markkoll.service.geometristatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import se.metria.markkoll.openapi.model.GeometristatusDto;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GeometristatusEvent {
    private Collection<UUID> avtalIds;
    private GeometristatusDto geometristatusDto;
}
