package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.ConfigDto;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.AvtalstypEvaluator;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.ConstantAvtalstypEvaluator;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.DefaultAvtalstypEvaluator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AvtalstypEvaluatorService {
    @NonNull
    private final KundConfigService kundConfigService;

    /**
     * Hämta nyckel-värde-par från kundconfig, och avgör vilka filter som ska användas för att avgöra
     * avtalstyper. Nycklarna vi är intresserade av är av formen
     * <indataTyp>_[INTRANG|SERVIS|REV]_FILTER. Värdena i paren anger det värdet i det relevanta attributet i våra
     * features (t.ex. attributet "owner" för dpcom/dppower) som bestämmer vad som är intrång, servis, rev, o.s.v.
     */
    public AvtalstypEvaluator getAvtalstypEvaluator(String kundId, IndataTypDto indataTyp) {
        var configs = kundConfigService.getKund(kundId).getConfigurations();

        var filters = new HashMap<String, AvtalstypDto>();

        addFilters(filters, configs, indataTyp, "_SERVIS_FILTER", AvtalstypDto.SERVIS);

        // Keeping old filters for future reference. This code is going to be either refactored or replaced in the near future.
        // addFilters(filters, configs, indataTyp, "_INTRANG_FILTER", Avtalstyp.INTRANG);
        // addFilters(filters, configs, indataTyp, "_REV_FILTER", Avtalstyp.REV);

        if (filters.size() == 0) {
            return new ConstantAvtalstypEvaluator(AvtalstypDto.INTRANG);
        }
        else {
            return new DefaultAvtalstypEvaluator(filters, AvtalstypDto.INTRANG);
        }
    }

    private void
    addFilters(Map<String, AvtalstypDto> filters,
               Collection<ConfigDto> configs,
               IndataTypDto indataTyp,
               String filterSuffix,
               AvtalstypDto avtalstyp) {

        configs.stream()
            .filter(config -> (indataTyp.toString() + filterSuffix).toUpperCase().equals(config.getKey().toUpperCase()))
            .map(config -> config.getValue())
            .forEach(value -> filters.put(value == null ? null : value.toUpperCase(), avtalstyp));
    }
}
