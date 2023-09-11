package se.metria.markkoll.service.projekt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.metria.markkoll.repository.projekt.ProjektRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AvtalUpdaterFactory {
    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final ElnatProjektService elnatProjektService;

    @NonNull
    private final FiberProjektService fiberProjektService;

    public AvtalUpdater getAvtalUpdater(UUID projektId) {
        var projektTyp = projektRepository.getProjekttyp(projektId);

        switch (projektTyp) {
            case FIBER:
                return fiberProjektService;
            case LOKALNAT:
            case REGIONNAT:
                return elnatProjektService;
            default:
                throw new IllegalArgumentException("Okänd projekttyp: " + projektTyp);
        }
    }
}
