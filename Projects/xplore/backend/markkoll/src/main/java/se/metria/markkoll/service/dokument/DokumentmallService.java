package se.metria.markkoll.service.dokument;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.markkoll.entity.DokumentmallEntity;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.DokumentInfoDto;
import se.metria.markkoll.openapi.model.DokumentTypDto;
import se.metria.markkoll.openapi.model.DokumentmallDto;
import se.metria.markkoll.repository.dokument.DokumentmallRepository;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.util.CollectionUtil;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DokumentmallService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final DokumentmallRepository dokumentmallRepository;

    @NonNull
    private final FilService filService;

    @NonNull
    private final ModelMapper modelMapper;

    @Transactional
    public DokumentmallDto create(DokumentInfoDto dokumentInfo, String kundId, MultipartFile multipartFile) throws IOException {

        log.info("Skapar ny dokumentmall för kund {} med filnamn {}", kundId, multipartFile.getOriginalFilename());

        var fil = filService.create(multipartFile, kundId);

        var entity = modelMapper.map(dokumentInfo, DokumentmallEntity.class);
        entity.setFil(fil);
        entity.setKundId(kundId);

        // TODO: Kopplingstabell istället så man slipper all den här logiken
        if(dokumentInfo.getSelected()) {
            var selectedDokument =
                dokumentmallRepository.getAllByKundIdAndDokumenttypAndSelectedIsTrue(kundId, entity.getDokumenttyp());

            selectedDokument.forEach(dokument -> {
                log.info("Sätter selected till false för {}", dokument.getId());
                dokument.setSelected(false);
            });
        }

        var saved = dokumentmallRepository.saveAndFlush(entity);

        log.info("Dokument {} skapat: ", saved.getId());

        aclService.createObject(saved.getId(), DokumentmallEntity.class, kundId, KundEntity.class);

        return modelMapper.map(saved, DokumentmallDto.class);
    }

    public DokumentmallDto get(UUID dokumentId) {
        log.info("Hämtar dokument {}", dokumentId);

        var entity = dokumentmallRepository.findById(dokumentId).orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, DokumentmallDto.class);
    }

    public DokumentmallDto getSelected(String kundId, DokumentTypDto dokumenttyp) {
        var selected = getSelectedEntity(kundId, dokumenttyp);

        return modelMapper.map(selected, DokumentmallDto.class);
    }

    public List<DokumentmallDto> getKundDokumentmallar(String kundId) {
        log.info("Hämtar dokument för kund {}", kundId);

        var dokument = dokumentmallRepository.findByKundIdOrderBySkapadDatumDesc(kundId);

        return CollectionUtil.modelMapperList(dokument, modelMapper, DokumentmallDto.class);
    }

    public ByteArrayResource getFileData(UUID dokumentId) throws MarkkollException {
        log.info("Hämtar fildata för dokument {}", dokumentId);

        var dokument = dokumentmallRepository.findById(dokumentId)
            .orElseThrow(() -> new MarkkollException(MarkkollError.DOKUMENT_ERROR));

        return dokument.getFil().getByteArrayResource();
    }

    public ByteArrayResource getFileData(String kundId, DokumentTypDto dokumenttyp) throws MarkkollException {
        log.info("Hämtar dokument av typ {} för kund {}", dokumenttyp, kundId);

        var selected = getSelectedEntity(kundId, dokumenttyp);

        return this.getFileData(selected.getId());
    }

    public String getFileName(UUID dokumentId) {
        log.info("Hämtar filnamn för dokument {}", dokumentId);

        var filId = dokumentmallRepository.getFilId(dokumentId);
        return filService.getFilnamn(filId);
    }

    @Transactional
    public void update(UUID dokumentmallId, DokumentmallDto dokumentDto) {
        log.info("Uppdaterar dokument {}", dokumentmallId);

        var kundId = dokumentDto.getKundId();

        var existing = dokumentmallRepository.findById(dokumentmallId).orElseThrow(EntityNotFoundException::new);

        var selectedDokument =
            dokumentmallRepository.getAllByKundIdAndDokumenttypAndSelectedIsTrue(kundId, dokumentDto.getDokumenttyp());

        // TODO: Kopplingstabell istället så man slipper all den här logiken
        selectedDokument.forEach(dokument -> {
                log.info("Sätter selected till false för {}", dokument.getId());
                dokument.setSelected(false);
            }
        );

        BeanUtils.copyProperties(dokumentDto, existing, "id");
    }

    @Transactional
    public void delete(UUID dokumentId) {
        log.info("Tar bort dokument {}", dokumentId);

        dokumentmallRepository.deleteById(dokumentId);
        aclService.deleteObject(dokumentId, DokumentmallEntity.class);
    }

    private DokumentmallEntity getSelectedEntity(String kundId, DokumentTypDto dokumenttyp) {
        var dokumentList = dokumentmallRepository.getAllByKundIdAndDokumenttypAndSelectedIsTrue(kundId, dokumenttyp);
        if(dokumentList.size() == 0) {
            log.error("Kunde inte hitta dokument av type {} för kund {}", dokumenttyp, kundId);
            throw new MarkkollException(MarkkollError.DOKUMENT_FIND_ERROR);
        }
        else if (dokumentList.size() > 1) {
            log.warn("Mer än en dokumentmall av typ {} för kund {} är satt som vald", dokumenttyp, kundId);
        }

        return dokumentList.get(0);
    }
}
