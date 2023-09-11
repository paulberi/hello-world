package se.metria.markkoll.repository.geometristatus;

import se.metria.markkoll.openapi.model.GeometristatusDto;

import java.util.UUID;

public interface GeometristatusAvtalId {
    UUID getAvtalId();
    GeometristatusDto getGeometristatus();
}
