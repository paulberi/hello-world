package se.metria.markkoll.repository.intrang;

import java.util.Collection;
import java.util.UUID;

public interface FastighetsintrangRepositoryCustom {
    void saveAllFastighetsintrang(UUID fastighetId, UUID versionId);

    void saveAllFastighetsintrang(Collection<UUID> fastighetId, UUID versionId);
}
