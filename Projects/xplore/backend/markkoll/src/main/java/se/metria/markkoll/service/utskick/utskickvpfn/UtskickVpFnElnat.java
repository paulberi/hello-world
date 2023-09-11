package se.metria.markkoll.service.utskick.utskickvpfn;

import lombok.RequiredArgsConstructor;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.utskick.UtskickVpDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UtskickVpFnElnat implements UtskickVpFn {
    @Override
    public List<UtskickVpDto> get(List<MarkagareDto> signatarer, String utskickTitle, Optional<MarkagareDto> kontaktperson) {
        var utskickVpDto = UtskickVpDto.builder()
            .signatarer(signatarer)
            .title(utskickTitle)
            .kontaktperson(kontaktperson)
            .build();

        return Arrays.asList(utskickVpDto);
    }
}
