package se.metria.markkoll.service.dokument.bindings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.dokument.bindings.data.UtskickBindings;
import se.metria.markkoll.service.utskick.UtskickDto;
import se.metria.markkoll.util.MarkagareComparator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtskickBindingsService {
    @NonNull
    private final MarkagareService markagareService;

    public UtskickBindings getUtskickBindings(UtskickDto utskick) {
        var mottagare = utskick.getKontaktperson();

        return UtskickBindings.builder()
            .signatarerNamn(getMarkagareNamn(utskick.getSignatarer()))
            .lopnummer(utskick.getLopnummer())
            .mottagareAdress(mottagare.map(ag -> ag.getAdress()).orElse(""))
            .mottagareBankkonto(mottagare.map(ag -> ag.getBankkonto()).orElse(""))
            .mottagareNamn(mottagare.map(ag -> ag.getNamn()).orElse(""))
            .mottagareOrganisationsnummer(getPersonnummer(mottagare))
            .mottagarePostnummer(mottagare.map(ag -> ag.getPostnummer()).orElse(""))
            .mottagarePostort(mottagare.map(ag -> ag.getPostort()).orElse(""))
            .mottagareCoAdress(mottagare.map(ag -> ag.getCoAdress()).orElse(""))
            .build();
    }

    private List<String> getMarkagareNamn(List<MarkagareDto> markagare) {
        return markagare.stream()
            .sorted(new MarkagareComparator())
            .map(MarkagareDto::getNamn)
            .collect(Collectors.toList());
    }

    private String getPersonnummer(Optional<MarkagareDto> mottagare) {
        return mottagare.map(ag -> markagareService.getPersonnummer(ag.getId())).orElse("");
    }
}
