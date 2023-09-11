package se.metria.markkoll.service.indata;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.indata.IndataEntity;
import se.metria.markkoll.entity.indata.KallfilEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.repository.indata.IndataRepository;
import se.metria.markkoll.repository.indata.KallfilRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.xplore.maputils.GeometryUtil;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndataService {
    @NonNull
    private final IndataRepository indataRepository;

    @NonNull
    private final KallfilRepository kallfilRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @Transactional
    public List<UUID> update(UUID projektId, UUID kallfilId, Indata indata) {
        var projekt = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);
        var kallfil = kallfilRepository.findById(kallfilId).orElseThrow(EntityNotFoundException::new);

        indataRepository.deleteByProjektId(projektId);

        var indataIds = new ArrayList<UUID>();
        for (var featureEntry: indata.getFeatureMap().entrySet()) {
            var features = featureEntry.getValue();
            var layer = featureEntry.getKey();

            var entityIds = saveFeatures(features, layer, projekt, kallfil);

            indataIds.addAll(entityIds);
        }
        return indataIds;
    }

    private List<UUID>
    saveFeatures(Collection<SimpleFeature> features, String layer, ProjektEntity projekt, KallfilEntity kallfil) {

        var entities = features.stream()
            .map(f -> getIndataEntity(f, layer, projekt, kallfil))
            .collect(Collectors.toList());

        entities = indataRepository.saveAll(entities);

        var entityIds = entities.stream().map(e -> e.getId()).collect(Collectors.toList());

        return entityIds;
    }

    private IndataEntity
    getIndataEntity(SimpleFeature feature, String layer, ProjektEntity projektEntity, KallfilEntity kallfilEntity) {

        var entity = new IndataEntity();
        entity.setGeom((Geometry)feature.getDefaultGeometryProperty().getValue());
        entity.setFeatureProperties(GeometryUtil.getPropertiesAsJson(feature));
        entity.setLayer(layer);
        entity.setProjekt(projektEntity);
        entity.setKallfil(kallfilEntity);

        return entity;
    }
}
