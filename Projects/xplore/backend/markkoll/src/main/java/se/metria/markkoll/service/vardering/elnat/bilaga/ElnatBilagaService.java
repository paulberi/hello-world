package se.metria.markkoll.service.vardering.elnat.bilaga;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.vardering.bilaga.BilagaEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.BilagaTypDto;
import se.metria.markkoll.openapi.model.ElnatBilagaDto;
import se.metria.markkoll.repository.fil.FilRepository;
import se.metria.markkoll.repository.vardering.bilaga.BilagaRepository;
import se.metria.markkoll.repository.vardering.elnat.ElnatVarderingsprotokollRepository;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.util.CollectionUtil;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ElnatBilagaService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final BilagaRepository bilagaRepository;

    @NonNull
    private final FilRepository filRepository;

    @NonNull
    private final FilService filService;

    @NonNull
    private final ElnatVarderingsprotokollRepository varderingsprotokollRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @Transactional
    public ElnatBilagaDto create(UUID filId, UUID varderingsprotokollId, BilagaTypDto bilagaTyp) {
        log.info("Skapar bilaga av typ {} med fil {} till värderingsprotokoll {}", bilagaTyp, filId,
            varderingsprotokollId);

        var filEntity = filRepository.findById(filId).orElseThrow(EntityNotFoundException::new);
        var vpEntity = varderingsprotokollRepository.findById(varderingsprotokollId)
            .orElseThrow(EntityNotFoundException::new);

        var bilagaEntity = BilagaEntity.builder()
            .fil(filEntity)
            .bilagaTyp(bilagaTyp)
            .varderingsprotokoll(vpEntity)
            .build();

        var saved = bilagaRepository.save(bilagaEntity);

        aclService.createObject(saved.getId(), BilagaEntity.class,
            varderingsprotokollId, ElnatVarderingsprotokollEntity.class);

        return modelMapper.map(saved, ElnatBilagaDto.class);
    }

    @Transactional
    public void delete(UUID bilagaId) {
        log.info("Tar bort bilaga " + bilagaId);

        bilagaRepository.deleteById(bilagaId);

        aclService.deleteObject(bilagaId, BilagaEntity.class);
    }

    public ElnatBilagaDto get(UUID bilagaId) {
        var entity = bilagaRepository.findById(bilagaId).orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, ElnatBilagaDto.class);
    }

    public Resource getData(UUID bilagaId) {
        var entity = bilagaRepository.findById(bilagaId).orElseThrow(EntityNotFoundException::new);

        return filService.getData(entity.getFil().getId());
    }

    public List<ElnatBilagaDto> getAll(UUID vpId) {
        var entities = bilagaRepository.findAllByVarderingsprotokollId(vpId);

        return CollectionUtil.modelMapperList(entities, modelMapper, ElnatBilagaDto.class);
    }
}
