package se.metria.markkoll.repository.projekt;

import se.metria.markkoll.db.tables.Utskicksstrategi;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.openapi.model.UtskicksstrategiDto;

public interface UtskickProjektView {
    ProjektTypDto getProjektTyp();
    UtskicksstrategiDto getUtskicksstrategi();
    Boolean getShouldHaveVp();
}
