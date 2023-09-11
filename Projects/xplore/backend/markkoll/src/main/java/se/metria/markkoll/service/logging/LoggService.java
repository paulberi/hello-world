package se.metria.markkoll.service.logging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalsjobbRepository;
import se.metria.markkoll.repository.infobrev.InfobrevsjobbRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.fastighet.FastighetService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class LoggService {
    @NonNull
    private final FastighetService fastighetService;

    @NonNull
    private final ProjektLoggService projektLoggService;

    @NonNull
    private final AvtalsjobbRepository avtalsjobbRepository;

    @NonNull
    private final InfobrevsjobbRepository infobrevsjobbRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    public static final Integer SAMTLIGA = null;

    @Transactional
    public ProjektLoggItemDto
    addManuellFastighetHandelse(UUID projektId, UUID fastighetId, ManuellFastighethandelseTypDto typ) {
        var fastighet = fastighetService.getFastighet(projektId, fastighetId);

        return projektLoggService.createManuellFastighetHandelse(projektId, fastighet.getFastighetsbeteckning(), typ);
    }

    public ProjektLoggItemDto
    addProjektHandelse(UUID projektId, ProjekthandelseTypDto projekthandelsetyp) {
        return projektLoggService.createProjektHandelse(projektId, projekthandelsetyp);
    }

    public ProjektLoggItemDto
    addAvtalHandelse(UUID avtalsjobbId) {
        var projektId = avtalsjobbRepository.getProjektId(avtalsjobbId);
        var avtalTotal = avtalsjobbRepository.getTotal(avtalsjobbId);
        var antalFastigheter = avtalTotal == projektRepository.getNumOfAvtal(projektId) ? SAMTLIGA : avtalTotal;

        return projektLoggService.createAvtalHandelse(projektId, avtalsjobbId, antalFastigheter);
    }

    public ProjektLoggItemDto addInfobrevHandelse(UUID infobrevsjobbId) {
        var projektId = infobrevsjobbRepository.getProjektId(infobrevsjobbId);
        var infobrevTotal = infobrevsjobbRepository.getTotal(infobrevsjobbId);
        var antalFastigheter = infobrevTotal == projektRepository.getNumOfAvtal(projektId) ? SAMTLIGA : infobrevTotal;

        return projektLoggService.createInfobrevHandelse(projektId, infobrevsjobbId, antalFastigheter);
    }

    public ProjektLoggPageDto
    getProjektLoggPage(UUID projektId,
                       Integer pageNum,
                       Integer size,
                       List<ProjektLoggFilterDto> filter,
                       Sort.Direction sortDirection)
    {
        var pageRequest = PageRequest.of(pageNum, size, sortDirection, "datum");

        return projektLoggService.getProjektLoggPage(projektId, pageRequest, filter);
    }
}
