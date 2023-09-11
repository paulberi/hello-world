package se.metria.markkoll.service.dokument.bindings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.metria.markkoll.service.BeredareService;
import se.metria.markkoll.service.dokument.bindings.data.BeredareBindings;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeredareBindingsService {
    @NonNull
    private final BeredareService beredareService;

    public BeredareBindings getBeredareBindings(UUID projektId) {
        var beredare = beredareService.get(projektId);

        return BeredareBindings.builder()
            .adress(beredare.getAdress())
            .namn(beredare.getNamn())
            .ort(beredare.getOrt())
            .postnummer(beredare.getPostnummer())
            .telefonnummer(beredare.getTelefonnummer())
            .build();
    }
}
