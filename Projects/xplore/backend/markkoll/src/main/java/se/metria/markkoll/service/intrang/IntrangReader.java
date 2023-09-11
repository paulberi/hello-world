package se.metria.markkoll.service.intrang;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.openapi.model.ProjektIntrangDto;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.service.indata.Indata;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.AvtalstypEvaluator;
import se.metria.markkoll.service.intrang.converters.featurestointrangset.DPPowerFeaturesToIntrangSetConverter;
import se.metria.markkoll.service.intrang.converters.featurestointrangset.FeaturesToIntrangSetConverter;
import se.metria.markkoll.service.intrang.converters.featurestointrangset.SingleFeatureToIntrangSetConverter;
import se.metria.markkoll.service.intrang.converters.featuretointrang.DpComFeatureToIntrangConverter;
import se.metria.markkoll.service.intrang.converters.featuretointrang.TrimbleFeatureToIntrangConverter;
import se.metria.markkoll.util.modelmapper.GeometryToGeoJsonConverter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static se.metria.markkoll.openapi.model.IndataTypDto.DPCOM;
import static se.metria.markkoll.openapi.model.IndataTypDto.TRIMBLE;

@Component
@RequiredArgsConstructor
public class IntrangReader {
    private ModelMapper modelMapper = getModelMapper();

    public List<ProjektIntrangDto> getIntrang(Indata indata, AvtalstypEvaluator avtalstypEvaluator) {
        var converter = getFeaturesToIntrangConverter(indata.getIndataTyp(),
            avtalstypEvaluator);

        var features = indata.getFeatureMap().values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        var intrang = converter.convert(features);

        return intrang.stream()
            .filter(intr -> intr.getType() != IntrangstypDto.OKAND.getValue())
            .filter(intr -> intr.getStatus() != IntrangsStatusDto.BEVARAS.getValue())
            .filter(intr -> intr.getAvtalstyp() != null)
            .map(intr -> modelMapper.map(intr, ProjektIntrangDto.class))
            .collect(Collectors.toList());
    }

    private FeaturesToIntrangSetConverter
    getFeaturesToIntrangConverter(IndataTypDto indataTyp, AvtalstypEvaluator avtalstypEvaluator) {
        switch(indataTyp) {
            case DPCOM:
                var dpComConverter = new DpComFeatureToIntrangConverter(avtalstypEvaluator);
                return new SingleFeatureToIntrangSetConverter(dpComConverter, DPCOM);
            case DPPOWER:
                return new DPPowerFeaturesToIntrangSetConverter(avtalstypEvaluator);
            case TRIMBLE:
                return new SingleFeatureToIntrangSetConverter(
                    new TrimbleFeatureToIntrangConverter(avtalstypEvaluator), TRIMBLE
                );
            default:
                throw new IllegalArgumentException("Saknar konverterare för " + indataTyp);
        }
    }

    private ModelMapper getModelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.addConverter(new GeometryToGeoJsonConverter());
        return modelMapper;
    }
}
