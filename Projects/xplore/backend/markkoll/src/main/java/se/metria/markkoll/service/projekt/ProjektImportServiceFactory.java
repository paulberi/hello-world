package se.metria.markkoll.service.projekt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.metria.markkoll.openapi.model.ProjektTypDto;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProjektImportServiceFactory {
    @NonNull
    private final ProjektImportService elnatProjektImportService;

    @NonNull
    private final ProjektImportService fiberProjektImportService;

    @NonNull
    private final ProjektService projektService;

    public ProjektImportService getProjektImportService(ProjektTypDto projektTyp) {
        switch (projektTyp) {
            case FIBER:
                return fiberProjektImportService;
            case LOKALNAT:
            case REGIONNAT:
                return elnatProjektImportService;
            default:
                throw new IllegalArgumentException("Okänd projekttyp: " + projektTyp);
        }
    }

    public ProjektImportService getProjektImportService(UUID projektId) {
        var projektTyp = projektService.getProjektTyp(projektId);
        return getProjektImportService(projektTyp);
    }
}
