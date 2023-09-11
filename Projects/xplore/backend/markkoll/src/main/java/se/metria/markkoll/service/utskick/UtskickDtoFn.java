package se.metria.markkoll.service.utskick;

import se.metria.markkoll.openapi.model.FastighetDto;
import se.metria.markkoll.openapi.model.MarkagareDto;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface UtskickDtoFn {
    UtskickDto apply(FastighetDto fastighetDto,
                     List<MarkagareDto> agareAtAdress,
                     String lopnummer,
                     Optional<MarkagareDto> mottagareAvtal);
}
