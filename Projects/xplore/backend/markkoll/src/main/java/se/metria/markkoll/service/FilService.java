package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.openapi.model.FilDto;
import se.metria.markkoll.repository.fil.FilRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final FilRepository filRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @Transactional
    public FilEntity create(Resource resource, String mimeType, String kundId) throws IOException {
        var bytes = resource.getInputStream().readAllBytes();
        return create(bytes, resource.getFilename(), mimeType, null, kundId);
    }

    @Transactional
    public FilEntity create(MultipartFile file, String kundId) throws IOException {
        return create(file.getBytes(), file.getOriginalFilename(), file.getContentType(), null, kundId);
    }

    @Transactional
    public FilEntity create(byte[] bytes, String filename, String mimeType, String skapadAv, String kundId) {
        log.info("Skapar ny fil: {}", filename);

        var entity = new FilEntity();
        entity.setFilnamn(filename);
        entity.setMimeTyp(mimeType);
        entity.setFil(bytes);
        entity.setSkapadAv(skapadAv);
        entity.setKundId(kundId);

        var saved = filRepository.saveAndFlush(entity);

        aclService.createObject(saved.getId(), FilEntity.class, kundId, KundEntity.class);

        log.info("Fil skapad: {}", saved.getId());

        return saved;
    }

    public FilDto get(UUID filId) {
        var entity = filRepository.findById(filId).orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, FilDto.class);
    }

    public List<FilDto> getBilagorFiler(UUID avtalId) {
        log.info("Hämtar filer tillhörande bilagor för avtal {}", avtalId);
        var entities = filRepository.getFiler(avtalId);

        log.info("Hämtade {} filer", entities.size());
        return CollectionUtil.modelMapperList(entities, modelMapper, FilDto.class);
    }

    public List<Resource> getBilagorFilerData(UUID avtalId) {
        return getBilagorFiler(avtalId).stream()
            .map(fil -> getData(fil.getId()))
            .collect(Collectors.toList());
    }

    public Resource getData(UUID filId) {
        log.info("Hämtar data för fil {}", filId);
        var entity = filRepository.findById(filId).orElseThrow(EntityNotFoundException::new);

        return new FileNameAwareByteArrayResource(entity.getFil(), entity.getFilnamn());
    }

    public void delete(UUID filId) {
        log.info("Tar bort fil: {}", filId);

        aclService.deleteObject(filId, FilEntity.class);

        filRepository.deleteById(filId);
    }

    @Deprecated
    public FilEntity getFilEntity(UUID filId) {
        log.info("Hämtar fil: {}", filId);
        return filRepository.findById(filId).orElseThrow(EntityNotFoundException::new);
    }

    public String getFilnamn(UUID filId) {
        log.info("Hämtar filnamn för fil {}", filId);

        return filRepository.getFilnamn(filId);
    }
}
