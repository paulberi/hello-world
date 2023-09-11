package se.metria.markkoll.service.utskick.utskickvpfn;

import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.utskick.UtskickVpDto;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface UtskickVpFn {
    List<UtskickVpDto> get(List<MarkagareDto> signatarer, String utskickTitle, Optional<MarkagareDto> kontaktperson);
}
