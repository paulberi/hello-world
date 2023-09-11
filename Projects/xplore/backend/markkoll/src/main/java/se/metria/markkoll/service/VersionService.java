package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.ProjektIntrangDto;
import se.metria.markkoll.openapi.model.VersionDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.modelmapper.GeoJsonToGeometryConverter;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VersionService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final VersionRepository versionRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    private ModelMapper modelMapper = getModelMapper();

    @Transactional
    public UUID create(UUID projektId, VersionDto versionDto, Collection<ProjektIntrangDto> intrangDtos) {
        var projekt = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);

        var versionEntity = modelMapper.map(versionDto, ImportVersionEntity.class);
        versionEntity.setProjekt(projekt);

        var intrangEntities = CollectionUtil.modelMapperList(intrangDtos, modelMapper, IntrangEntity.class);
        for (var intrangEntity: intrangEntities) {
            versionEntity.addIntrang(intrangEntity);
        }

        versionRepository.save(versionEntity);

        aclService.createObject(versionEntity.getId(), ImportVersionEntity.class, projektId, ProjektEntity.class);

        versionDto.setId(versionEntity.getId());

        return versionEntity.getId();
    }

    private ModelMapper getModelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.addConverter(new GeoJsonToGeometryConverter());
        modelMapper.typeMap(ProjektIntrangDto.class, IntrangEntity.class).addMappings(m -> m.skip(IntrangEntity::setId));
        return modelMapper;
    }
}
