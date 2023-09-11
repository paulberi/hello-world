package se.metria.mapcms.service.publik;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.commons.utils.FileNameAwareByteArrayResource;
import se.metria.mapcms.entity.FilEntity;
import se.metria.mapcms.entity.ProjektEntity;
import se.metria.mapcms.openapi.model.ProjektRspDto;
import se.metria.mapcms.openapi.model.PubliceringStatusDto;
import se.metria.mapcms.repository.FilRepository;
import se.metria.mapcms.repository.ProjektRepository;
import se.metria.mapcms.service.admin.AdminProjektService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PublikProjektService {

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private AdminProjektService projektAdminService;

    @NonNull
    private FilRepository filRepository;


    public List<ProjektRspDto> listPublikaProjektForKund(UUID kundId, String lang) {
        List<ProjektEntity> projekt = projektRepository.listPublikaProjektForKund(kundId).orElseThrow(EntityNotFoundException::new);
        return projekt.stream().map(n -> modelMapper.map(projektAdminService.oversattProjekt(n, lang), ProjektRspDto.class)).collect(Collectors.toList());
    }


    public Resource getFilForProjekt(UUID kundId, UUID projektId, UUID filId) {
        ProjektEntity p = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        FilEntity f= filRepository.getFilForProjekt(kundId, projektId, filId).orElseThrow(EntityNotFoundException::new);
        if(p.getPubliceringStatus().equals(PubliceringStatusDto.PUBLICERAD) || p.getPubliceringStatus().equals(PubliceringStatusDto.ARKIVERAD)){
            return new FileNameAwareByteArrayResource(f.getFil(), f.getFilnamn());
        }else{
            throw new IllegalStateException("Projektet är inte tillgängligt för publiken");
        }
    }

    public ProjektRspDto getProjektWithId(UUID kundId, UUID projektId, String lang) {
        ProjektEntity p = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        if (isProjektForPublic(p)){
            return modelMapper.map(projektAdminService.oversattProjekt(p, lang), ProjektRspDto.class);
        }else {
            throw new IllegalStateException("projektet är inte för publiken");
        }
    }

    public ProjektRspDto getProjektWithSlug(UUID kundId, String projektSlug, String lang){
        ProjektEntity p = projektRepository.getProjektForKundBySlug(kundId, projektSlug).orElseThrow(EntityNotFoundException::new);
        if (isProjektForPublic(p)){
            return modelMapper.map(projektAdminService.oversattProjekt(p, lang), ProjektRspDto.class);
        }else {
            throw new IllegalStateException("projektet är inte för publiken");
        }
    }

    private boolean isProjektForPublic(ProjektEntity p){
        if (p.getPubliceringStatus().equals(PubliceringStatusDto.PUBLICERAD) || p.getPubliceringStatus().equals(PubliceringStatusDto.ARKIVERAD)) {
            return true;
        }
        return false;
    }
}
