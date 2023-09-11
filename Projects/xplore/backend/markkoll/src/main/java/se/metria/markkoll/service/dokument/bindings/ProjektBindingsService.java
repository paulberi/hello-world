package se.metria.markkoll.service.dokument.bindings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.FiberInfoDto;
import se.metria.markkoll.openapi.model.ProjektInfoDto;
import se.metria.markkoll.service.projekt.ElnatProjektService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.dokument.bindings.data.ProjektBindings;
import se.metria.markkoll.service.map.MapService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@MarkkollService
@RequiredArgsConstructor
@Transactional
public class ProjektBindingsService {
    @NonNull
    private final ElnatProjektService elnatProjektService;

    @NonNull
    private final FiberProjektService fiberProjektService;

    @NonNull
    private final MapService mapService;

    public ProjektBindings getElnatProjektProperties(UUID projektId) throws IOException {
        var elnatProjekt = elnatProjektService.getProjektDto(projektId)
            .orElseThrow(EntityNotFoundException::new);

        var builder = getGeneralProjektBindings(elnatProjekt.getProjektInfo());

        return builder
            .ledningsagare(elnatProjekt.getElnatInfo().getLedningsagare())
            .ledningsstracka(elnatProjekt.getElnatInfo().getLedningsstracka())
            .varderingsprotokollBifogas(true)
            .build();
    }

    public ProjektBindings getFiberProjektProperties(UUID projektId) throws IOException {
        var fiberProjekt = fiberProjektService.getProjektDto(projektId)
            .orElseThrow(EntityNotFoundException::new);

        var builder = getGeneralProjektBindings(fiberProjekt.getProjektInfo());

        return addFiberProjektBindings(fiberProjekt.getFiberInfo(), builder).build();
    }

    private ProjektBindings.ProjektBindingsBuilder
    addFiberProjektBindings(FiberInfoDto fiberInfoDto, ProjektBindings.ProjektBindingsBuilder builder)
    {
        var bidragsprojekt = fiberInfoDto.getBidragsprojekt() == null ? false : fiberInfoDto.getBidragsprojekt();

        return builder
            .ersattningSkaErlaggas(!bidragsprojekt)
            .ersattningSkaInteUtga(bidragsprojekt)
            .ledningsagare(fiberInfoDto.getLedningsagare())
            .ledningsstracka(fiberInfoDto.getLedningsstracka())
            .bidragsprojekt(bidragsprojekt)
            .seBilagaA(!bidragsprojekt)
            .varderingsprotokollBifogas(fiberInfoDto.getVarderingsprotokoll());
    }

    private ProjektBindings.ProjektBindingsBuilder getGeneralProjektBindings(ProjektInfoDto projekt) throws IOException {
        if (projekt == null) {
            return ProjektBindings.builder();
        }
        else {
            return ProjektBindings.builder()
                .startdatum(projekt.getStartDatum() == null ? null : projekt.getStartDatum().toLocalDate())
                .projektnamn(projekt.getNamn())
                .uppdragsnummer(projekt.getUppdragsnummer())
                .projektkartaMedProjektomrade(List.of(mapService.getProjektkarta(projekt.getId(), true)))
                .projektkartaMedStrackning(List.of(mapService.getProjektkarta(projekt.getId(), false)))
                .utbetalningskonto(projekt.getUtbetalningskonto())
                .projektnummer(projekt.getProjektnummer())
                .ansvarigProjektledare(projekt.getAnsvarigProjektledare())
                .ansvarigKontruktor(projekt.getAnsvarigKonstruktor())
                .beskrivning(projekt.getBeskrivning());
        }
    }
}
