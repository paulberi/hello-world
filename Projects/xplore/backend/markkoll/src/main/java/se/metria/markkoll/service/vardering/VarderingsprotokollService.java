package se.metria.markkoll.service.vardering;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface VarderingsprotokollService<VPDTO> {
    Optional<VPDTO> getWithAvtalId(UUID avtalId);

    VPDTO getEmptyVarderingsprotokoll();

    VPDTO getKlassificeratVarderingsprotokoll(UUID avtalId, UUID versionId);

    UUID create(VPDTO vp, UUID avtalId);

    void createKlassificeradeVarderingsprotokoll(UUID versionId, Collection<UUID> avtalIds);

    void updateKlassificeradeVarderingsprotokoll(UUID versionId, Collection<UUID> avtalIds);

    void update(VPDTO vp, UUID avtalId);
}
