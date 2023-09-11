package se.metria.markkoll.service.utskick;

import se.metria.markkoll.openapi.model.MarkagareDto;

import java.util.Optional;

@FunctionalInterface
public interface UtskickDtoOmbudFn {
    UtskickDto apply(MarkagareDto ombud, String lopnummer, Optional<MarkagareDto> mottagareAvtal);
}
