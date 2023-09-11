package se.metria.markkoll.repository.intrang;

import java.util.Collection;
import java.util.UUID;

public interface OmradesintrangRepositoryCustom {
    void saveAllOmradesintrang(UUID fastighetId, UUID versionId);

    void saveAllOmradesintrang(Collection<UUID> fastighetIds, UUID versionId);
}
