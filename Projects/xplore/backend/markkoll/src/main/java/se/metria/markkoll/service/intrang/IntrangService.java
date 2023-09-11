package se.metria.markkoll.service.intrang;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.openapi.model.ProjektIntrangDto;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.IntrangRepository;
import se.metria.markkoll.service.indata.Indata;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.AvtalstypEvaluator;
import se.metria.markkoll.service.intrang.converters.featurestointrangset.DPPowerFeaturesToIntrangSetConverter;
import se.metria.markkoll.service.intrang.converters.featurestointrangset.FeaturesToIntrangSetConverter;
import se.metria.markkoll.service.intrang.converters.featurestointrangset.SingleFeatureToIntrangSetConverter;
import se.metria.markkoll.service.intrang.converters.featuretointrang.DpComFeatureToIntrangConverter;
import se.metria.markkoll.service.intrang.converters.featuretointrang.TrimbleFeatureToIntrangConverter;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.modelmapper.GeometryToGeoJsonConverter;
import se.metria.xplore.maputils.GeometryUtil;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static se.metria.markkoll.openapi.model.IndataTypDto.DPCOM;
import static se.metria.markkoll.openapi.model.IndataTypDto.TRIMBLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntrangService {
    @NonNull
    private final IntrangRepository intrangRepository;

    @NonNull
    private final FastighetsintrangRepository fastighetsintrangRepository;

    private ModelMapper modelMapper = getModelMapper();

    public List<ProjektIntrangDto> getIntrang(UUID projektId) {
        var entities = intrangRepository.findAllByProjektId(projektId)
            .orElseThrow(EntityNotFoundException::new);

        return CollectionUtil.modelMapperList(entities, modelMapper, ProjektIntrangDto.class);
    }

    public List<ProjektIntrangDto> getIntrangForVersion(UUID versionId) {
        var entities = intrangRepository.findAllByVersionId(versionId);

        return CollectionUtil.modelMapperList(entities, modelMapper, ProjektIntrangDto.class);
    }

    public Optional<String> getExtentFromIntrangByProjektId(UUID projektId) {
        var extent = fastighetsintrangRepository.getExtentByProjektId(projektId);

        // Om vi inte hittar några intrång så använder vi Sverigekartan som fallback
        if (extent == null) {
            GeometryFactory gf = new GeometryFactory();
            // Extent för Sverige i ESPG:3006
            var c1 = new Coordinate(27762,6087846);
            var c2 = new Coordinate(1120500,7699707);
            var polygon = new Coordinate[]{
                    new Coordinate(c1.x, c1.y),
                    new Coordinate(c2.x, c1.y),
                    new Coordinate(c2.x, c2.y),
                    new Coordinate(c1.x, c2.y),
                    new Coordinate(c1.x, c1.y)
            };
            extent = gf.createPolygon(polygon);
        }

        return Optional.of(GeometryUtil.toGeoJSON(extent));
    }

    public Set<IntrangEntity> getIntrang(Indata indata, AvtalstypEvaluator avtalstypEvaluator) {
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
            .collect(Collectors.toSet());
    }

    public Set<ProjektIntrangDto> getIntrangDto(Indata indata, AvtalstypEvaluator avtalstypEvaluator) {
        return getIntrang(indata, avtalstypEvaluator).stream()
            .map(intr -> modelMapper.map(intr, ProjektIntrangDto.class))
            .collect(Collectors.toSet());
    }

    public Optional<String> getIntrangWithBuffertByProjekt(UUID projektId) {
        return intrangRepository.getIntrangWithBuffertByProjekt(projektId);
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
