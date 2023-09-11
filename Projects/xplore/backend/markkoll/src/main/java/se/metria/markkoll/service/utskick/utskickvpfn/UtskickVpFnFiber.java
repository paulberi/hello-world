package se.metria.markkoll.service.utskick.utskickvpfn;

import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.utskick.UtskickVpDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UtskickVpFnFiber implements UtskickVpFn {
    @Override
    public List<UtskickVpDto> get(List<MarkagareDto> signatarer, String utskickTitle, Optional<MarkagareDto> kontaktperson) {
        return signatarer.stream()
            .map(s -> UtskickVpDto.builder()
                .title(s.getNamn())
                .signatarer(Arrays.asList(s))
                .kontaktperson(kontaktperson)
                .build())
            .collect(Collectors.toList());
    }
}
