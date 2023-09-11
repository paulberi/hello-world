package se.metria.markkoll.service.projekt;

import se.metria.markkoll.openapi.model.GeometristatusDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AvtalUpdater {
    UUID addManuelltAvtal(UUID projektId, UUID fastighetId);

    List<UUID> addImportAvtal(UUID projektId, List<UUID> fastighetId, GeometristatusDto geometristatus);

    void updateAvtal(UUID projektId, UUID fastighetId);

    void updateAvtal(UUID projektId, Collection<UUID> fastighetId, GeometristatusDto geometristatus);

    void updateVarderingsprotokoll(UUID projektId, Collection<UUID> fastighetIds);
}
