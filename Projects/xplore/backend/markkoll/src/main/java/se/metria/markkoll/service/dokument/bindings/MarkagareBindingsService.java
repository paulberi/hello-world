package se.metria.markkoll.service.dokument.bindings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.dokument.bindings.data.MarkagareBindings;
import se.metria.markkoll.util.MarkagareComparator;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@MarkkollService
@RequiredArgsConstructor
public class MarkagareBindingsService {
    @NonNull
    private final MarkagareService markagareService;

    public MarkagareBindings
    getMarkagareBindings(UUID projektId, UUID fastighetId)
    {
        var markagare = markagareService.getAgareForFastighet(projektId, fastighetId).stream()
            .sorted(new MarkagareComparator())
            .collect(Collectors.toList());

        var markagarePersonuppgifter = markagare.stream()
            .filter(ag -> ag.getInkluderaIAvtal())
            .map(ag -> String.join(", ", ag.getNamn(), ag.getAdress(), ag.getPostnummer(), ag.getPostort()))
            .collect(Collectors.toList());

        return MarkagareBindings.builder()
            .markagareNamn(getMarkagareNamn(markagare))
            .markagareNamnAdress(getMarkagareNamnAdress(markagare))
            .markagareNamnAdressAndel(getMarkagareNamnAdressAndel(markagare))
            .markagareNamnAndel(getMarkagareNamnAndel(markagare))
            .samtligaFastighetsagare(markagarePersonuppgifter)
            .build();
    }

    private List<String> getMarkagareNamn(List<MarkagareDto> markagare) {
        return markagare.stream()
            .map(MarkagareDto::getNamn)
            .collect(Collectors.toList());
    }

    private List<String> getMarkagareNamnAdress(List<MarkagareDto> markagare) {
        return markagare.stream()
            .map(this::markagareNamnAdressString)
            .collect(Collectors.toList());
    }

    private List<String> getMarkagareNamnAdressAndel(List<MarkagareDto> markagare) {
        return markagare.stream()
                .map(this::markagareNamnAdressAndelString)
                .collect(Collectors.toList());
    }


    private List<String> getMarkagareNamnAndel(List<MarkagareDto> markagare) {
        return markagare.stream()
                .map(this::markagareNamnAndelString)
                .collect(Collectors.toList());
    }

    private String markagareNamnAdressString(MarkagareDto agare) {
        var s = new String();

        s += String.format("%s, ", agare.getNamn());

        s += String.format("%s %s %s", agare.getAdress(), agare.getPostnummer(), agare.getPostort());

        return s;
    }

    private String markagareNamnAdressAndelString(MarkagareDto agare) {
        return markagareNamnAdressString(agare) + ", Andel " + agare.getAndel();
    }

    private String markagareNamnAndelString(MarkagareDto agare) {
        return  agare.getNamn() + ", Andel " + agare.getAndel();
    }

}
