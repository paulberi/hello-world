package se.metria.markkoll.service.dokument.bindings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.dokument.bindings.data.IntrangBindings;
import se.metria.markkoll.service.dokument.bindings.data.MarkkollBindings;
import se.metria.markkoll.service.dokument.bindings.data.ProjektBindings;
import se.metria.markkoll.service.utskick.UtskickDto;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarkkollBindingsService {
    @NonNull
    private final AvtalBindingsService avtalBindingsService;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final BeredareBindingsService beredareBindingsService;

    @NonNull
    private final MarkagareBindingsService markagareBindingsService;

    @NonNull
    private final ProjektBindingsService projektBindingsService;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final UtskickBindingsService utskickBindingsService;

    public MarkkollBindings
    getBindings(UUID avtalId, UtskickDto utskick) throws IOException {
        var projektId = avtalRepository.getProjektId(avtalId);
        var fastighetId = avtalRepository.getFastighetId(avtalId);

        return getBindings(projektId, fastighetId, utskick);
    }

    @Deprecated
    public MarkkollBindings getBindings(UUID projektId, UUID fastighetId, UtskickDto utskick)
        throws IOException
    {
        var projekttyp = projektRepository.getProjekttyp(projektId);
        var avtalId = avtalRepository.getIdByProjektIdAndFastighetId(projektId, fastighetId);

        return MarkkollBindings.builder()
            .avtalBindings(avtalBindingsService.getAvtalBindings(projektId, fastighetId))
            .beredareBindings(beredareBindingsService.getBeredareBindings(projektId))
            .ersattningBindings(avtalBindingsService.getErsattningBindings(avtalId))
            .intrangBindings(getIntrangBindings(avtalId, projekttyp))
            .markagareBindings(markagareBindingsService.getMarkagareBindings(projektId, fastighetId))
            .projektBindings(getProjektBindings(projektId, projekttyp))
            .utskickBindings(utskickBindingsService.getUtskickBindings(utskick))
            .build();
    }

    private IntrangBindings getIntrangBindings(UUID avtalId, ProjektTypDto projekttyp) {
        switch (projekttyp) {
            case FIBER:
                return avtalBindingsService.getFiberIntrangBindings(avtalId);
            case LOKALNAT:
            case REGIONNAT:
                return avtalBindingsService.getElnatIntrangBindings(avtalId);
            default:
                throw new IllegalArgumentException("Okänd projekttyp: " + projekttyp);
        }
    }

    private ProjektBindings getProjektBindings(UUID projektId, ProjektTypDto projekttyp) throws IOException {
        switch (projekttyp) {
            case FIBER:
                return projektBindingsService.getFiberProjektProperties(projektId);
            case LOKALNAT:
            case REGIONNAT:
                return projektBindingsService.getElnatProjektProperties(projektId);
            default:
                throw new IllegalArgumentException("Okänd projekttyp: " + projekttyp);
        }
    }
}
